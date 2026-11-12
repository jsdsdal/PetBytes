package org.example.backend.pet;

import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetLifeStage;
import org.example.backend.pet.entity.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByIdAndOwnerId(Long id, Long ownerId);
    List<Pet> findByOwnerIdOrderByBornAtDesc(Long ownerId);
    boolean existsByOwnerIdAndStatus(Long ownerId, PetStatus status);
}
