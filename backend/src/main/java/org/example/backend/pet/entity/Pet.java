package org.example.backend.pet.entity;

import jakarta.persistence.*;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.user.User;
import java.time.Instant;

@Entity
@Table(name = "pets")
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private PetStats stats;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PetSpecies species;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PetPersonality personality;

    @Column(nullable = false)
    private boolean variant;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PetBond petBond;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PetLifeStage lifeStage;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PetStatus status;

    @Column(nullable = false)
    private Instant bornAt;

    @Column(nullable = false)
    private Instant lastSimulatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    protected Pet() {} // JPA

    private Pet(String name, PetSpecies species, PetPersonality personality,
                boolean variant, User owner, Instant now) {
        this.name = name;
        this.species = species;
        this.personality = personality;
        this.variant = variant;
        this.owner = owner;
        this.stats = PetStats.initial();
        this.petBond = PetBond.STRANGER;
        this.lifeStage = PetLifeStage.KID;
        this.status = PetStatus.ACTIVE;
        this.bornAt = now;
        this.lastSimulatedAt = now;
    }

    public static Pet adopt(String name, PetSpecies species, PetPersonality personality,
                            boolean variant, User owner, Instant now) {
        return new Pet(name, species, personality, variant, owner, now);
    }

    // --- behaviour ---

    public void rename(String newName) { this.name = newName; }

    public void feed(int amount) {
        stats.addHunger(-amount);
        stats.addHappiness(2);
    }

    public void play(int amount) {
        stats.addHappiness(amount);
        stats.addEnergy(-amount / 2);
        stats.addHunger(amount / 4);
    }

    public void sleep(int amount) {
        stats.addEnergy(amount);
        stats.addHunger(amount / 3);
    }

    public void markSimulated(Instant now) { this.lastSimulatedAt = now; }

    public void advanceLifeStage(PetLifeStage next) { this.lifeStage = next; }

    public void shelve() { this.status = PetStatus.INACTIVE; }

    public void reactivate(Instant now) {
        this.status = PetStatus.ACTIVE;
        this.lastSimulatedAt = now;      // frozen time doesn't count
    }

    public boolean isActive() { return status == PetStatus.ACTIVE; }

    // --- getters ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public PetStats getStats() { return stats; }
    public PetSpecies getSpecies() { return species; }
    public PetPersonality getPersonality() { return personality; }
    public boolean isVariant() { return variant; }
    public PetBond getPetBond() { return petBond; }
    public PetLifeStage getLifeStage() { return lifeStage; }
    public PetStatus getStatus() { return status; }
    public Instant getBornAt() { return bornAt; }
    public Instant getLastSimulatedAt() { return lastSimulatedAt; }
    public User getOwner() { return owner; }

    // --- setters ---

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStats(PetStats stats) { this.stats = stats; }
    public void setSpecies(PetSpecies species) { this.species = species; }
    public void setPersonality(PetPersonality personality) { this.personality = personality; }
    public void setVariant(boolean variant) { this.variant = variant; }
    public void setPetBond(PetBond petBond) { this.petBond = petBond; }
    public void setLifeStage(PetLifeStage lifeStage) { this.lifeStage = lifeStage; }
    public void setStatus(PetStatus status) { this.status = status; }
    public void setBornAt(Instant bornAt) { this.bornAt = bornAt; }
    public void setLastSimulatedAt(Instant lastSimulatedAt) { this.lastSimulatedAt = lastSimulatedAt; }
    public void setOwner(User owner) { this.owner = owner; }

}