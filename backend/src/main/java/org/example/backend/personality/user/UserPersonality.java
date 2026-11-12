package org.example.backend.personality.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserPersonality {

    ANALYTICAL(
            "Analytical", "Logical and methodical. Drawn to understanding how things work."
    ),

    CAREGIVER(
            "Caregiver", "Empathetic and nurturing. Drawn to helping others."
    ),

    SPONTANEOUS(
            "Spontaneous", "Impulsive and curious. Drawn to novelty and action."
    ),

    METICULOUS(
            "Meticulous", "Precise and careful. Drawn to order and correctness."
    );

    private final String displayName;
    private final String description;

    UserPersonality(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

}