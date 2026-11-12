package org.example.backend.pet;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestClockConfig.class)
class PetApiIT {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired PetRepository pets;
    @Autowired PasswordEncoder encoder;
    @Autowired MutableClock clock;

    User alice, bob;
    Pet alicePet;

    @BeforeEach
    void setUp() {
        pets.deleteAll();
        users.deleteAll();
        alice = users.save(new User("alice", "a@a.dk", encoder.encode("pw"), UserPersonality.ANALYTICAL));
        bob   = users.save(new User("bob",   "b@b.dk", encoder.encode("pw"), UserPersonality.CAREGIVER));
        alicePet = pets.save(Pet.adopt("Mochi", PetSpecies.CAT,
                PetPersonality.ENERGETIC, false, alice, clock.instant()));
    }

    @Test
    void list_returns_only_my_pets() throws Exception {
        mvc.perform(get("/api/pets").header("Authorization", login("alice", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Mochi"))
                .andExpect(jsonPath("$[0].species").value("CAT"))
                .andExpect(jsonPath("$[0].personality").value("ENERGETIC"));
    }

    @Test
    void get_by_id_returns_pet_when_owned() throws Exception {
        mvc.perform(get("/api/pets/" + alicePet.getId())
                        .header("Authorization", login("alice", "pw")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mochi"));
    }

    @Test
    void get_by_id_returns_404_for_someone_elses_pet() throws Exception {
        mvc.perform(get("/api/pets/" + alicePet.getId())
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