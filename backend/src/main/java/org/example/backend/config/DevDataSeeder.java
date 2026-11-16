package org.example.backend.config;

import org.example.backend.adoption.AdoptionService;
import org.example.backend.adoption.dto.AdoptionOfferResponse;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.ActivityType;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdoptionService adoptionService;
    private final Clock clock;

    public DevDataSeeder(UserRepository userRepository, PetRepository petRepository,
                         ActivityLogRepository activityLogRepository, PasswordEncoder passwordEncoder,
                         AdoptionService adoptionService, Clock clock) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.activityLogRepository = activityLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.adoptionService = adoptionService;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedDevUser();
        seedDemoUser();
    }

    private void seedDevUser() {
        if (userRepository.findByUsername("dev").isPresent()) return;

        User dev = userRepository.save(new User(
                "dev", "dev@example.com",
                passwordEncoder.encode("dev"),
                UserPersonality.ANALYTICAL));

        AdoptionOfferResponse offer = adoptionService.currentOffer(dev);
        adoptionService.accept(dev, offer.id(), "Mochi");
    }

    // demo / demo: 1 aktiv variant + 4 i daycare
    private void seedDemoUser() {
        if (userRepository.findByUsername("demo").isPresent()) return;

        User demo = userRepository.save(new User(
                "demo", "demo@example.com",
                passwordEncoder.encode("demo"),
                UserPersonality.SPONTANEOUS));

        Instant now = clock.instant();

        // i daycare (ældst først)
        seedPet(demo, "Mochi", PetSpecies.CAT, PetPersonality.SPOILED, false, now.minus(Duration.ofDays(5)), false);
        seedPet(demo, "GUI", PetSpecies.SLIME, PetPersonality.DOCILE, false, now.minus(Duration.ofDays(4)), false);
        seedPet(demo, "Zappy", PetSpecies.ELECTRIC_RAT, PetPersonality.ENERGETIC, false, now.minus(Duration.ofDays(3)), false);
        seedPet(demo, "Tama", PetSpecies.TAMAGOTCHI, PetPersonality.CONTEMPLATIVE, false, now.minus(Duration.ofDays(2)), false);

        // aktiv: samme form som Mochi, men variant
        seedPet(demo, "Glitter", PetSpecies.CAT, PetPersonality.SPOILED, true, now.minus(Duration.ofHours(1)), true);
    }

    private void seedPet(User owner, String name, PetSpecies species, PetPersonality personality,
                         boolean variant, Instant bornAt, boolean active) {
        Pet pet = Pet.adopt(name, species, personality, variant, owner, bornAt);
        pet.markSimulated(clock.instant());   // ingen indhentning af tid ved første kald
        if (!active) pet.shelve();
        pet = petRepository.save(pet);
        activityLogRepository.save(new ActivityLog(pet, ActivityType.ADOPTED, null, bornAt, true));
    }
}