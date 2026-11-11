package org.example.backend.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!offline")
public class GeminiDialogueGenerator implements AiDialogueGenerator {

    private final ChatClient chatClient;
    private final AiDialoguePromptBuilder prompts;

    public GeminiDialogueGenerator(ChatClient chatClient, AiDialoguePromptBuilder prompts) {
        this.chatClient = chatClient;
        this.prompts = prompts;
    }

    @Override
    public String generate(AiDialogueContext context) {
        return chatClient.prompt()
                .system(prompts.system(context))
                .user(prompts.user(context))
                .call()
                .content();
    }
}