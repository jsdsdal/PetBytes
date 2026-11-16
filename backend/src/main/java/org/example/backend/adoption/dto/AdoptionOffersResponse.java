package org.example.backend.adoption.dto;

import java.util.List;

public record AdoptionOffersResponse(List<AdoptionOfferResponse> offers, int rerollsLeft) {}