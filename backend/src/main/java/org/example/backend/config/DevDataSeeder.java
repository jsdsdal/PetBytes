package org.example.backend.config;

import org.example.backend.adoption.AdoptionOffer;
import org.example.backend.adoption.AdoptionService;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdoptionService adoptionService;

    public DevDataSeeder(UserRepository userRepository, PetRepository petRepository, PasswordEncoder passwordEncoder, AdoptionService adoptionService) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
        this.adoptionService = adoptionService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByUsername("dev").isPresent()) return;

        User dev = userRepository.save(new User(
                "dev", "dev@example.com",
                passwordEncoder.encode("dev"),
                UserPersonality.ANALYTICAL));

        AdoptionOffer offer = adoptionService.currentOffer(dev);
        adoptionService.accept(dev, offer.getId(), "Mochi");
    }
}