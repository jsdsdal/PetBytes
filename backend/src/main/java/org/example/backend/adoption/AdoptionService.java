package org.example.backend.adoption;

import org.example.backend.adoption.dto.AdoptionOfferResponse;
import org.example.backend.adoption.dto.AdoptionOffersResponse;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.pet.PetStateService;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.pet.entity.PetStatus;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.ActivityType;
import org.example.backend.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private static final int BATCH_SIZE = 4;
    private static final int MAX_REROLLS_PER_DAY = 3;
    private static final int MAX_ATTEMPTS = 5;

    private final AdoptionOfferRepository offers;
    private final PetRepository pets;
    private final ActivityLogRepository logs;
    private final PersonalitySelectionService personalitySelection;
    private final SpeciesSelectionService speciesSelection;
    private final Random random;
    private final Clock clock;
    @Value("${pet.variant.rate:0.02}") private double variantRate;

    public AdoptionService(AdoptionOfferRepository offers, PetRepository pets, ActivityLogRepository logs, PersonalitySelectionService personalitySelection, SpeciesSelectionService speciesSelection, Random random, Clock clock) {
        this.offers = offers;
        this.pets = pets;
        this.logs = logs;
        this.personalitySelection = personalitySelection;
        this.speciesSelection = speciesSelection;
        this.random = random;
        this.clock = clock;
    }

    @Transactional
    public AdoptionOffersResponse currentBatch(User user) {
        requireNoActivePet(user);
        List<AdoptionOffer> pending = offers.findByUserIdAndStatusOrderByCreatedAtAsc(
                user.getId(), OfferStatus.PENDING);
        if (pending.isEmpty()) {
            pending = createBatch(user);
        }
        return new AdoptionOffersResponse(
                pending.stream().map(AdoptionOfferResponse::from).toList(),
                rerollsLeft(user));
    }

    @Transactional
    public AdoptionOffersResponse reroll(User user) {
        requireNoActivePet(user);
        if (rerollsLeft(user) <= 0) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "No more rerolls today");
        }
        Instant now = clock.instant();
        offers.findByUserIdAndStatus(user.getId(), OfferStatus.PENDING)
                .forEach(o -> o.decline(now));
        List<AdoptionOffer> fresh = createBatch(user);
        return new AdoptionOffersResponse(
                fresh.stream().map(AdoptionOfferResponse::from).toList(),
                rerollsLeft(user));
    }

    @Transactional
    public PetResponse accept(User user, Long offerId, String name) {
        requireNoActivePet(user);
        AdoptionOffer chosen = offers.findByUserIdAndStatus(user.getId(), OfferStatus.PENDING)
                .stream()
                .filter(o -> o.getId().equals(offerId))
                .findFirst()
                .orElseThrow(() -> PetStateService.conflict("No such pending offer"));

        Instant now = clock.instant();
        chosen.accept(now);

        // hele batch expires - tæller ikke som rerolls
        offers.findByUserIdAndStatus(user.getId(), OfferStatus.PENDING)
                .forEach(o -> o.expire(now));

        Pet pet = pets.save(Pet.adopt(name.strip(), chosen.getSpecies(), chosen.getPersonality(),
                chosen.isVariant(), user, now));
        logs.save(new ActivityLog(pet, ActivityType.ADOPTED, null, now, true));
        return PetResponse.from(pet);
    }

    // --- helpers ---

    private List<AdoptionOffer> createBatch(User user) {
        String batchId = java.util.UUID.randomUUID().toString();
        Set<Combo> avoid = recentCombos(user);
        List<AdoptionOffer> batch = new ArrayList<>(BATCH_SIZE);

        for (int i = 0; i < BATCH_SIZE; i++) {
            OfferRoll roll = rollAvoiding(user, avoid);
            avoid.add(roll.combo());          // also avoid dupes within this batch
            batch.add(new AdoptionOffer(user, batchId, roll.species(), roll.personality(),
                    roll.variant(), clock.instant()));
        }
        return offers.saveAll(batch);
    }

    private OfferRoll rollAvoiding(User user, Set<Combo> avoid) {
        OfferRoll roll = roll(user);
        for (int attempt = 1; attempt < MAX_ATTEMPTS && avoid.contains(roll.combo()); attempt++) {
            roll = roll(user);
        }
        return roll;
    }

    private OfferRoll roll(User user) {
        PetPersonality personality = personalitySelection.select(user.getPersonality());
        PetSpecies species = speciesSelection.select(personality);
        boolean variant = random.nextDouble() < variantRate;
        return new OfferRoll(species, personality, variant);
    }

    private Set<Combo> recentCombos(User user) {
        return offers.findTop4ByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(o -> new Combo(o.getSpecies(), o.getPersonality()))
                .collect(Collectors.toSet());
    }

    private int rerollsLeft(User user) {
        Instant since = clock.instant().minus(Duration.ofHours(24));
        long used = offers.countDistinctDeclinedBatchesSince(user.getId(), since);
        return (int) Math.max(0, MAX_REROLLS_PER_DAY - used);
    }

    private void requireNoActivePet(User user) {
        if (pets.existsByOwnerIdAndStatus(user.getId(), PetStatus.ACTIVE)) {
            throw PetStateService.conflict("Shelve your current pet first");
        }
    }

    private record Combo(PetSpecies species, PetPersonality personality) {}
    private record OfferRoll(PetSpecies species, PetPersonality personality, boolean variant) {
        Combo combo() { return new Combo(species, personality); }
    }
}
