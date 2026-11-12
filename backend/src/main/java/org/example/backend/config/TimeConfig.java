package org.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

// til tests af tidsfølsomme operationer
@Configuration
public class TimeConfig {

    @Bean
    Clock clock() { return Clock.systemUTC(); }
}