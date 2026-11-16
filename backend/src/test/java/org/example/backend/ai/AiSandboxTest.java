package org.example.backend.ai;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AiSandboxTest {

    @Autowired
    private AiDialogueGenerator generator;

    @Test
    void testDialogue() {
        AiDialogueContext context = new AiDialogueContext(
                "Sparky",
                PetSpecies.ELECTRIC_RAT,
                PetPersonality.ENERGETIC,
                1.5, // dialogueVerbosity
                "Speaks in all caps. Chaotic and excitable.",
                10, // hunger
                90, // happiness
                20, // energy
                List.of("The pet took a nap.", "The pet ate food."),
                List.of("HI HI HI!! WHAT ARE WE DOING?!", "I AM A ZAPPY BOI!")
        );


        for (int i = 0; i < 5; i++) {
            System.out.println("OUTPUT " + i + ": " + generator.generate(context));
        }
    }
}