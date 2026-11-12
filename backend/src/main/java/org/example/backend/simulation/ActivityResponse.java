package org.example.backend.simulation;

import java.time.Instant;

public record ActivityResponse(
        Long id, ActivityType type, Integer amount, Instant occurredAt, String description, boolean playerInitiated) {

    public static ActivityResponse from(ActivityLog log) {
        return new ActivityResponse(
                log.getId(),
                log.getType(),
                log.getAmount(),
                log.getOccurredAt(),
                log.getType().getDescription(log.isPlayerInitiated()), 
                log.isPlayerInitiated()
        );
    }
}