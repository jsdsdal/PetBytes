package org.example.backend.ai;

import org.example.backend.ai.dto.GeminiContent;
import org.example.backend.ai.dto.GeminiRequest;
import org.example.backend.ai.dto.GeminiResponse;
import org.example.backend.ai.dto.GeminiSystemInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Primary
@ConditionalOnProperty(name = "dialogue.provider", havingValue = "gemini")
public class GeminiDialogueGenerator implements AiDialogueGenerator {

    private static final Logger log = LoggerFactory.getLogger(GeminiDialogueGenerator.class);
    private final RestClient restClient;
    private final AiDialoguePromptBuilder prompts;
    private final GeminiProperties props;

    public GeminiDialogueGenerator(
            RestClient.Builder restClientBuilder,
            AiDialoguePromptBuilder prompts,
            GeminiProperties props) {
        this.restClient = restClientBuilder.baseUrl(props.baseUrl()).build();
        this.prompts = prompts;
        this.props = props;
    }

    @Override
    public String generate(AiDialogueContext context) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiContent("user", prompts.user(context))),
                new GeminiSystemInstruction(prompts.system(context))
        );

        try {
            GeminiResponse response = restClient.post()
                    .uri("/v1beta/models/{model}:generateContent", props.model())
                    .header("X-goog-api-key", props.apiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                var candidate = response.candidates().get(0);
                if (candidate.content() != null && candidate.content().parts() != null && !candidate.content().parts().isEmpty()) {
                    return candidate.content().parts().get(0).text();
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch dialogue from Gemini API via RestClient", e);
        }

        return null;
    }
}