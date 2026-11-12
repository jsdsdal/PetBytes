package org.example.backend.adoption;


import org.example.backend.personality.user.UserPersonality;
import org.example.backend.timetest.TestClockConfig;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
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
import org.example.backend.pet.PetRepository;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestClockConfig.class)
class AdoptionFlowIT {

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository users;
    @Autowired
    PetRepository pets;
    @Autowired
    PasswordEncoder encoder;

    @Test
    void full_adoption_flow() throws Exception {
        users.deleteAll();
        pets.deleteAll();
        users.save(new User("alice", "a@a.dk", encoder.encode("pw"), UserPersonality.SPONTANEOUS));

        String token = login("alice", "pw");

        // 1. Current offer — creates one
        var offerBody = mvc.perform(get("/api/adoption/offers/current").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.species").isString())
                .andReturn().getResponse().getContentAsString();

        long offerId = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(offerBody).get("id").asLong();

        // 2. Accept it
        mvc.perform(post("/api/adoption/offers/" + offerId + "/accept")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mochi\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mochi"));

        // 3. Pet now exists
        mvc.perform(get("/api/pets").header("Authorization", token))
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)));
    }

    private String login(String username, String password) throws Exception {
        var result = mvc.perform(post("/api/dologin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andReturn();
        return result.getResponse().getHeader("Authorization");
    }
}