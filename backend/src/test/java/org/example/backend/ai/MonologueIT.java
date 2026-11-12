package org.example.backend.ai;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.PetRepository;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.timetest.MutableClock;
import org.example.backend.timetest.TestClockConfig;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")   // sets dialogue.provider=scripted → canned generator, no API call
@Import(TestClockConfig.class)
class MonologueIT {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired PetRepository pets;
    @Autowired PasswordEncoder encoder;
    @Autowired MutableClock clock;

    User alice;
    Pet pet;

    @BeforeEach
    void setUp() {
        pets.deleteAll();
        users.deleteAll();
        alice = users.save(new User("alice", "a@a.dk", encoder.encode("pw"), UserPersonality.ANALYTICAL));
        pet = pets.save(Pet.adopt("Mochi", PetSpecies.CAT,
                PetPersonality.ENERGETIC, false, alice, clock.instant()));
    }

    @Test
    void monologue_returns_a_non_empty_dialogue_string() throws Exception {
        mvc.perform(get("/api/pets/" + pet.getId() + "/monologue")
                        .header("Authorization", login("alice", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialogue").isString())
                .andExpect(jsonPath("$.dialogue").isNotEmpty());
    }

    @Test
    void monologue_for_someone_elses_pet_returns_error() throws Exception {
        User bob = users.save(new User("bob", "b@b.dk", encoder.encode("pw"), UserPersonality.CAREGIVER));
        mvc.perform(get("/api/pets/" + pet.getId() + "/monologue")
                        .header("Authorization", login("bob", "pw")))
                .andExpect(status().isNotFound());
    }

    private String login(String username, String password) throws Exception {
        var result = mvc.perform(post("/api/dologin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andReturn();
        return result.getResponse().getHeader("Authorization");
    }
}