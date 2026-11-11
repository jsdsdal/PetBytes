package org.example.backend.adoption;

import org.example.backend.adoption.dto.AdoptionOfferResponse;
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
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_DECLINES_PER_DAY = 5;

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
    public AdoptionOfferResponse currentOffer(User user) {
        requireNoActivePet(user);
        AdoptionOffer offer = pendingOffer(user).orElseGet(() -> newOffer(user));
        return respond(offer, user);
    }

    @Transactional
    public AdoptionOfferResponse decline(User user, Long offerId) {
        AdoptionOffer offer = requirePending(user, offerId);
        requireDeclinesLeft(user);
        offer.decline(clock.instant());
        return respond(newOffer(user), user);
    }

    @Transactional
    public PetResponse accept(User user, Long offerId, String name) {
        requireNoActivePet(user);
        AdoptionOffer offer = requirePending(user, offerId);
        offer.accept(clock.instant());
        Pet pet = pets.save(Pet.adopt(name.strip(), offer.getSpecies(), offer.getPersonality(),
                offer.isVariant(), user, clock.instant()));
        logs.save(new ActivityLog(pet, ActivityType.ADOPTED, null, clock.instant()));
        return PetResponse.from(pet);
    }

    private AdoptionOffer requirePending(User user, Long offerId) {
        AdoptionOffer offer = pendingOffer(user)
                .orElseThrow(() -> PetStateService.conflict("No pending offer"));
        if (!offer.getId().equals(offerId)) {
            throw PetStateService.conflict("This offer is no longer current");
        }
        return offer;
    }

    private void requireNoActivePet(User user) {
        if (pets.existsByOwnerIdAndStatus(user.getId(), PetStatus.ACTIVE)) {
            throw PetStateService.conflict("Shelve your current pet first");
        }
    }

    private void requireDeclinesLeft(User user) {
        if (declinesLeft(user) <= 0) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "No more declines today");
        }
    }

    private int declinesLeft(User user) {
        Instant since = clock.instant().minus(Duration.ofHours(24));
        long used = offers.countByUserIdAndStatusAndResolvedAtAfter(user.getId(), OfferStatus.DECLINED, since);
        return (int) Math.max(0, MAX_DECLINES_PER_DAY - used);
    }

    private AdoptionOfferResponse respond(AdoptionOffer offer, User user) {
        return AdoptionOfferResponse.from(offer, declinesLeft(user));
    }

    private AdoptionOffer newOffer(User user) {
        Set<Combo> recent = recentCombos(user);
        OfferRoll roll = rollAvoiding(user, recent);
        return offers.save(new AdoptionOffer(user, roll.species(), roll.personality(),
                roll.variant(), clock.instant()));
    }

    private OfferRoll rollAvoiding(User user, Set<Combo> recent) {
        OfferRoll roll = roll(user);
        for (int attempt = 1; attempt < MAX_ATTEMPTS && recent.contains(roll.combo()); attempt++) {
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
        return offers.findTop3ByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(o -> new Combo(o.getSpecies(), o.getPersonality()))
                .collect(Collectors.toSet());
    }

    private record Combo(PetSpecies species, PetPersonality personality) {}
    private record OfferRoll(PetSpecies species, PetPersonality personality, boolean variant) {
        Combo combo() { return new Combo(species, personality); }
    }
}