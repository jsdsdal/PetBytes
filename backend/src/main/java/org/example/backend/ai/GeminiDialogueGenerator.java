package org.example.backend.ai;

import org.example.backend.ai.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class GeminiDialogueGenerator implements AiDialogueGenerator {

    private static final Logger log = LoggerFactory.getLogger(GeminiDialogueGenerator.class);
    private final RestClient restClient;
    private final AiDialoguePromptBuilder prompts;
    private final GeminiProperties props;

    public GeminiDialogueGenerator(
            RestClient.Builder restClientBuilder,
            AiDialoguePromptBuilder prompts,
            GeminiProperties props) {

        boolean keyPresent = props.apiKey() != null && !props.apiKey().isBlank();
        log.info("GeminiDialogueGenerator ACTIVE — baseUrl={}, model={}, apiKeyPresent={}",
                props.baseUrl(), props.model(), keyPresent);

        this.restClient = restClientBuilder.baseUrl(props.baseUrl()).build();
        this.prompts = prompts;
        this.props = props;
    }

    @Override
    public String generate(AiDialogueContext context) {
        if (props.apiKey() == null || props.apiKey().isBlank()) {
            log.error("Gemini API key is missing or blank! Check your GEMINI_API_KEY environment variable.");
            return null;
        }

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiContent("user", prompts.user(context))),
                new GeminiSystemInstruction(prompts.system(context))
        );

        try {
            log.info("Calling Gemini API (model={})...", props.model());
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
                    log.info("Gemini successfully generated a monologue.");
                    return candidate.content().parts().get(0).text();
                }
            }
            log.warn("Gemini returned a response, but candidates/parts were empty.");
        } catch (HttpClientErrorException e) {
            log.error("Gemini API HTTP Error: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Failed to fetch dialogue from Gemini API via RestClient", e);
        }
        return null;
    }
}