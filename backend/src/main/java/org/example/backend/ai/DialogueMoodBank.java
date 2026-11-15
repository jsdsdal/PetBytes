package org.example.backend.ai;

import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class DialogueMoodBank {

    private static final List<String> MOODS = List.of(
            "playful",
            "pensive",
            "grumpy",
            "affectionate",
            "restless",
            "sleepy",
            "adventurous",
            "melancholy",
            "mischievous",
            "content"
       );

    private final Random random;
    public DialogueMoodBank(Random random) { this.random = random; }

    public String pick() { return MOODS.get(random.nextInt(MOODS.size())); }
}
