package org.example.backend.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "dialogue.provider", havingValue = "gemini")
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