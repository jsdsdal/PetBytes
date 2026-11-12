package org.example.backend.adoption;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class SpeciesSelectionService {
    private static final int HOME = 70, OTHER = 10;
    private final WeightedPicker picker;

    public SpeciesSelectionService(WeightedPicker picker) {
        this.picker = picker;
    }

    public PetSpecies select(PetPersonality personality) {
        return picker.pick(weightsFor(personality));
    }

    private static Map<PetSpecies, Integer> weightsFor(PetPersonality personality) {
        Map<PetSpecies, Integer> weights = new EnumMap<>(PetSpecies.class);
        for (PetSpecies s : PetSpecies.values()) {
            weights.put(s, s.getPersonality() == personality ? HOME : OTHER);
        }
        return weights;
    }
}