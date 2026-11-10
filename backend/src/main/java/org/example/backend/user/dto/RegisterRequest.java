package org.example.backend.user.dto;

import org.example.backend.personality.user.UserPersonality;

public record RegisterRequest (
    String username,
    String email,
    String password,
    UserPersonality personality
){

}

