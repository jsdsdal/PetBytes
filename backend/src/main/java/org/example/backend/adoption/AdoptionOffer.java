package org.example.backend.adoption;

import jakarta.persistence.*;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.user.User;

import java.time.Instant;

@Entity
@Table(name = "adoption_offers", indexes = @Index(columnList = "user_id, status"))
public class AdoptionOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) @Column(nullable = false) private PetSpecies species;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PetPersonality personality;
    @Column(nullable = false) private boolean variant;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OfferStatus status = OfferStatus.PENDING;
    @Column(nullable = false) private Instant createdAt;
    private Instant resolvedAt;

    protected AdoptionOffer() {}

    public AdoptionOffer(User user, PetSpecies species, PetPersonality personality,
                         boolean variant, Instant now) {
        this.user = user; this.species = species; this.personality = personality;
        this.variant = variant; this.createdAt = now;
    }

    public void accept(Instant now) { resolve(OfferStatus.ACCEPTED, now); }
    public void decline(Instant now) { resolve(OfferStatus.DECLINED, now); }

    private void resolve(OfferStatus newStatus, Instant now) {
        this.status = newStatus;
        this.resolvedAt = now;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public PetSpecies getSpecies() {
        return species;
    }

    public PetPersonality getPersonality() {
        return personality;
    }

    public boolean isVariant() {
        return variant;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSpecies(PetSpecies species) {
        this.species = species;
    }

    public void setPersonality(PetPersonality personality) {
        this.personality = personality;
    }

    public void setVariant(boolean variant) {
        this.variant = variant;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}