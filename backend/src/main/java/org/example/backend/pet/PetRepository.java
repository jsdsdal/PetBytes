package org.example.backend.pet;

import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetLifeStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerId(Long ownerId);                         // all pets ever
    List<Pet> findByOwnerIdAndLifeStageIn(Long ownerId, List<PetLifeStage> stages);
}
