package org.example.backend.ai;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CannedDialogueGenerator implements AiDialogueGenerator {

    private static final List<String> LINES = List.of(
            "…",
            "placeholder",// skriver senere
            "uhhh",
            "testingtesting !",
            "computer science is my passion",
            "java in my cup til i see sharp",
            "i wish i had rich parents but instead I'm writing java",
            "nintendo please dont sue i only have two dollar",
            "I slurp mayonnaise straight from the jar.",
            "kill all billionaires :)",
            "googoogaga"
    );

    private final Random random;

    public CannedDialogueGenerator(Random random) {
        this.random = random;
    }

    @Override
    public String generate(AiDialogueContext context) {
        return LINES.get(random.nextInt(LINES.size()));
    }
}