package org.example.backend.pet.entity;

import jakarta.persistence.*;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.user.User;

import java.time.Instant;
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
    private Instant bornAt;

    @Column(nullable = false)
    private Instant lastSimulatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetSpecies species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetPersonality personality;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetLifeStage lifeStage;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User owner;

    // ny constructor
//    public Pet(String name, PetSpecies species, PetPersonality personality, User owner) {
//        this.name = name;
//        this.species = species;
//        this.personality = personality;
//        this.stats = PetStats.initial();
//        this.owner = owner;
//        this.bornAt = LocalDateTime.now();
//        this.lastSimulatedAt = this.bornAt;
//        this.lifeStage = PetLifeStage.KID;
//    }

    // ny getter
    // public User getOwner() { return owner; }

    protected Pet() {} // JPA vil gerne have en tom constructor

    public Pet(String name, PetSpecies species,
              PetPersonality personality) {

        this.name = name;
        this.species = species;
        this.personality = personality;
        this.stats = PetStats.initial();

        this.bornAt = Instant.now();
        this.lastSimulatedAt = this.bornAt;

        this.lifeStage = PetLifeStage.KID;
    }

    public void updateLastSimulatedAt(Instant timestamp) {
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

    public void setStats(PetStats stats) {
        this.stats = stats;
    }

    public Instant getBornAt() {
        return bornAt;
    }

    public void setBornAt(Instant bornAt) {
        this.bornAt = bornAt;
    }

    public Instant getLastSimulatedAt() {
        return lastSimulatedAt;
    }

    public void setLastSimulatedAt(Instant lastSimulatedAt) {
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
