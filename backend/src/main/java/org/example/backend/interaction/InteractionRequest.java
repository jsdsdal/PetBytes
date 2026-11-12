package org.example.backend.interaction;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record InteractionRequest(@Min(1) @Max(100) int amount) {}