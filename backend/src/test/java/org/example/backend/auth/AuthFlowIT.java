package org.example.backend.auth;

import org.example.backend.personality.user.UserPersonality;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIT {

    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        users.deleteAll();
        users.save(new User("alice", "alice@example.com",
                encoder.encode("pw"), UserPersonality.ANALYTICAL));
    }

    @Test
    void login_returns_bearer_token_and_unlocks_protected_endpoint() throws Exception {
        var result = mvc.perform(post("/api/dologin")
                        // Sender brugernavn og kodeord som Basic-header, ligesom frontenden gør
                        .with(httpBasic("alice", "pw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"username":"alice","password":"pw"}
                        """))
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = result.getResponse().getHeader("Authorization");
        assertThat(authHeader).isNotNull().isNotBlank();

        mvc.perform(get("/api/pets").header("Authorization", authHeader))
                .andExpect(status().isOk());
    }

    @Test
    void bad_credentials_returns_401() throws Exception {
        mvc.perform(post("/api/dologin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alice","password":"wrong"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protected_endpoint_without_token_returns_401() throws Exception {
        mvc.perform(get("/api/pets")).andExpect(status().isUnauthorized());
    }
}