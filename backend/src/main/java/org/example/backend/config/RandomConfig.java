package org.example.backend.config;  // or wherever your @Configuration classes live

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class RandomConfig {

    @Bean
    public Random random() {
        return new Random();
    }
}