package org.example.backend.timetest;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.ActivityType;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static javax.swing.UIManager.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") @Transactional
@Import(TestClockConfig.class)
class PetLifecycleIT {
    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository users;
    @Autowired
    PetRepository pets;
    @Autowired
    ActivityLogRepository logs;
    @Autowired
    MutableClock clock;
//
//    Pet savedPet() {
//        User owner = users.save(new User("t", "t@t.dk", "x", UserPersonality.ANALYTICAL));
//        Pet pet = new Pet("Mochi", PetSpecies.CAT, false, PetPersonality.ENERGETIC, PetBond.STRANGER, owner);
//        pet.setLastSimulatedAt(clock.instant());
//        return pets.save(pet);
//    }

    // mere følger
}