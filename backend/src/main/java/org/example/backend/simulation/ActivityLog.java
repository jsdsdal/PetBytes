package org.example.backend.simulation;

import jakarta.persistence.*;
import org.example.backend.pet.entity.Pet;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


@Entity
@Table(name = "activity_logs",
        indexes = @Index(columnList = "pet_id, occurred_at"))
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //default er eager, vi behøver ikke loade hele pet pr row
    @JoinColumn(name = "pet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)   // delete pet -> delete log
    private Pet pet;

    @Column(nullable = false)
    private Instant occurredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    private Integer amount;   // optional idk


    public ActivityLog(Pet pet, ActivityType type, Integer amount, Instant occurredAt) {
        this.pet = pet;
        this.type = type;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }

    public ActivityLog() {

    }

    public <V, K> ActivityLog(Pet pet, String activity, Map<K,V> amount) {
    }

    public Long getId() { return id; }
    public Pet getPet() { return pet; }
    public Instant getOccurredAt() { return occurredAt; }
    public ActivityType getType() { return type; }
    public Integer getAmount() { return amount; }

}