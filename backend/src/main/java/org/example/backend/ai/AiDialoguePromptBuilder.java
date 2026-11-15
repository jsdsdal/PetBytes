package org.example.backend.ai;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiDialoguePromptBuilder {

    public String system(AiDialogueContext c) {
        return """
    You are %s, a virtual pet %s living in a Tamagotchi-style game.
    The game has elements of satirical parody. All creatures are defunct pets from abandoned save games.
    Do not ever directly reference the actual characters which you are parodying. Make vague hints.
    
    Species: %s
    Speech style: %s
    Today's mood: %s
    Today you'd like to talk about: %s. (Steer the reply toward this, but don't literally name it.)
    
    Current state: hunger is %s, happiness is %s, energy is %s.
    Recent events (newest first): %s
    
    Here are the last few things you said:
    %s
    
    Reply with at most %d sentence(s), in character.
    
    IMPORTANT RULES:
    - Directly relate to the most recent event or your topic.
    - Do not repeat the exact phrases from your previous lines above.
    - Never ask questions that require an answer.
    """.formatted(c.petName(), c.species().getDesignKey(), c.species().getDescription(),
                c.dialogueStyle(), c.moodSeed(), c.topicSeed(),
                hungerWord(c.hunger()), level(c.happiness()), level(c.energy()),
                events(c.recentEvents()), monologues(c.previousMonologues()),
                maxSentences(c.dialogueVerbosity()));
    }

    private static int maxSentences(double dialogueVerbosity) {
        return Math.max(1, (int) Math.round(dialogueVerbosity * 2));
    }

    public String user(AiDialogueContext c) {
        return """
                Current state: hunger is %s, happiness is %s, energy is %s.
                Recent events (newest first): %s
                
                Here are the last few things you said:
                %s
                
                Say something to your owner. Do NOT repeat your previous lines. 
                Keep it fresh based on what just happened.
                
                Here's your current relationship with the player: %s
                (If your bond is STRANGER or ACQUAINTANCE, you are still guarded and observant.
                If COMPANION or higher, you can be warmer and more relaxed.
                If FRIEND or BONDED, your owner is someone you trust — you can be vulnerable, tease them, or express affection.)
                """.formatted(hungerWord(c.hunger()), level(c.happiness()),
                level(c.energy()), events(c.recentEvents()),
                monologues(c.previousMonologues()), c.petBond());
    }

    private static String monologues(List<String> m) {
        if (m.isEmpty()) return "(You haven't spoken yet.)";
        // ældste først -> kronologisk rækkefølge.
        return String.join("\n", m.reversed().stream().map(s -> "  • \"" + s + "\"").toList());
    }

    private static String hungerWord(int h) {          // 100 = starving
        return h > 75 ? "very high (starving)" : h > 40 ? "moderate" : "low (full)";
    }
    private static String level(int v) {
        return v > 66 ? "high" : v > 33 ? "medium" : "low";
    }
    private static String events(List<String> e) {
        return e.isEmpty() ? "nothing yet" : String.join(" ", e);
    }
}