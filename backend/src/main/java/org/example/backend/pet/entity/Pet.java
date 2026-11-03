package org.example.backend.pet.entity;

import jakarta.persistence.*;
import org.example.backend.personality.pet.PetPersonality;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private PetStats stats;

    @Column(nullable = false)
    private LocalDateTime bornAt;

    @Column(nullable = false)
    private LocalDateTime lastSimulatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpecies species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetPersonality personality;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetLifeStage lifeStage;

    protected Pet() {} // JPA vil gerne have en tom constructor

    public Pet(String name, PetSpecies species,
              PetPersonality personality) {

        this.name = name;
        this.species = species;
        this.personality = personality;
        this.stats = PetStats.initial();

        this.bornAt = LocalDateTime.now();
        this.lastSimulatedAt = this.bornAt;

        this.lifeStage = PetLifeStage.KID;
    }

    public void updateLastSimulatedAt(LocalDateTime timestamp) {
        this.lastSimulatedAt = timestamp;
    }

    public void advanceLifeStage(PetLifeStage newStage) {
        this.lifeStage = newStage;
    }

    public void feed(int amount) {
        stats.addHunger(-amount);
    }


    // getters + setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public PetStats getStats() {
        return stats;
    }

    public void setBornAt(LocalDateTime bornAt) { this.bornAt = bornAt; }

    public LocalDateTime getLastSimulatedAt() {
        return lastSimulatedAt;
    }

    public void setLastSimulatedAt(LocalDateTime lastSimulatedAt) {
        this.lastSimulatedAt = lastSimulatedAt;
    }

    public PetSpecies getSpecies() {
        return species;
    }

    public void setSpecies(PetSpecies species) {
        this.species = species;
    }

    public PetPersonality getPersonality() {
        return personality;
    }

    public void setPersonality(PetPersonality personality) {
        this.personality = personality;
    }

    public PetLifeStage getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(PetLifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }
}
