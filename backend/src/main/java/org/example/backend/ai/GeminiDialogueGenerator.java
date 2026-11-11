package org.example.backend.ai;

import org.example.backend.ai.dto.GeminiContent;
import org.example.backend.ai.dto.GeminiRequest;
import org.example.backend.ai.dto.GeminiResponse;
import org.example.backend.ai.dto.GeminiSystemInstruction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Primary
@ConditionalOnProperty(name = "dialogue.provider", havingValue = "gemini")
public class GeminiDialogueGenerator implements AiDialogueGenerator {

    private final WebClient webClient;
    private final AiDialoguePromptBuilder prompts;
    private final GeminiProperties props;

    public GeminiDialogueGenerator(
            WebClient.Builder builder,
            AiDialoguePromptBuilder prompts,
            GeminiProperties props) {
        this.webClient = builder.baseUrl(props.baseUrl()).build();
        this.prompts = prompts;
        this.props = props;
    }

    @Override
    public String generate(AiDialogueContext context) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiContent("user", prompts.user(context))),
                new GeminiSystemInstruction(prompts.system(context))
        );

        GeminiResponse response = webClient.post()
                .uri("/v1beta/models/{model}:generateContent?key={key}",
                        props.model(), props.apiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();

        return response.candidates().get(0).content().parts().get(0).text();
    }
}