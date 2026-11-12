package org.example.backend.ai;

import jakarta.persistence.*;
import org.example.backend.pet.entity.Pet;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "ai_monologues",
        indexes = @Index(columnList = "pet_id, said_at"))
public class AiMonologue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Pet pet;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private Instant saidAt;

    protected AiMonologue() {}

    public AiMonologue(Pet pet, String text) {
        this.pet = pet;
        this.text = text;
        this.saidAt = Instant.now();
    }

    public Long getId() { return id; }
    public Pet getPet() { return pet; }
    public String getText() { return text; }
    public Instant getSaidAt() { return saidAt; }
}