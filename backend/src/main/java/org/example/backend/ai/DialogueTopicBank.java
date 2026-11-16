package org.example.backend.ai;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;

@Component
public class DialogueTopicBank {

    private static final List<String> TOPICS = List.of(
            "the weather outside",
            "something you noticed in the room",
            "a weird dream you had",
            "your favorite food",
            "a secret you're keeping",
            "how your owner has been acting lately",
            "something you're looking forward to",
            "a complaint nobody asked about",
            "something you're curious about",
            "a memory from when you were smaller",
            "what you'd do with a hundred treats",
            "a philosophical thought",
            "something that quietly annoys you",
            "a question you've been chewing on",
            "how the nap situation is going",
            "the last thing that surprised you"
    );

    private final Random random;
    public DialogueTopicBank(Random random) { this.random = random; }

    public String pick() { return TOPICS.get(random.nextInt(TOPICS.size())); }
}