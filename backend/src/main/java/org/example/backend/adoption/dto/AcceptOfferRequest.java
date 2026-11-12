package org.example.backend.adoption.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AcceptOfferRequest(@NotBlank @Size(max = 20) String name) {}