package org.example.backend.config;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public DevDataSeeder(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User dev = new User("dev", "dev@example.com", "{noop}dev", UserPersonality.ANALYTICAL);
            userRepository.save(dev);

            Pet.adopt("Mochi", PetSpecies.CAT, PetPersonality.CONTEMPLATIVE, false, dev, Instant.now())
            petRepository.save(pet);
        }
    }
}