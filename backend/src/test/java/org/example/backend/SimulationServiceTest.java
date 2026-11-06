package org.example.backend;
//
//import org.example.backend.personality.pet.PersonalityProfileRegistry;
//import org.example.backend.personality.pet.PetPersonality;
//import org.example.backend.personality.pet.profiles.EnergeticProfileProvider;
//import org.example.backend.pet.entity.Pet;
//import org.example.backend.pet.entity.PetSpecies;
//import org.junit.jupiter.api.Test;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import java.util.Random;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
class SimulationServiceTest {
//
//    private static final Instant T0 = Instant.parse("2026-11-06T08:00:00Z");
//
//    private final SimulationService service = new SimulationService(
//            new PersonalityProfileRegistry(List.of(
//                    new EnergeticProfileProvider(),
//                   // new ContemplativeProfileProvider())),
//            new Random(42));                    // seeded, so repeatable
//
//    @Test
//    void rambunctiousPetGetsHungrierFasterThanContemplativeOne() {
//        Pet wild = new Pet("A", PetSpecies.CAT, PetPersonality.ENERGETIC, T0);
//        Pet calm = new Pet("B", PetSpecies.CAT, PetPersonality.CONTEMPLATIVE, T0);
//
//        service.catchUp(wild, T0.plus(Duration.ofHours(6)));
//        service.catchUp(calm, T0.plus(Duration.ofHours(6)));
//
//        assertThat(wild.getStats().getHungerLevel())
//                .isGreaterThan(calm.getStats().getHungerLevel());
//    }
}