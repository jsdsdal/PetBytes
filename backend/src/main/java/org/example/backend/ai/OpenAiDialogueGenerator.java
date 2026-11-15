package org.example.backend.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
@Service
@Primary
public class OpenAiDialogueGenerator implements AiDialogueGenerator {

    private static final Logger log = LoggerFactory.getLogger(OpenAiDialogueGenerator.class);

    private final RestClient restClient;
    private final AiDialoguePromptBuilder prompts;
    private final String model;

    public OpenAiDialogueGenerator(
            RestClient.Builder restClientBuilder,
            AiDialoguePromptBuilder prompts,
            @Value("${llm.base-url}") String baseUrl,
            @Value("${llm.api-key}") String apiKey,
            @Value("${llm.model}") String model) {
        log.info("OpenAiDialogueGenerator ACTIVE — baseUrl={}, model={}, apiKeyPresent={}",
                baseUrl, model, apiKey != null && !apiKey.isBlank());
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.prompts = prompts;
        this.model = model;
    }

    @Override
    public String generate(AiDialogueContext context) {
        var request = new ChatRequest(
                model,
                List.of(
                        new Message("system", prompts.system(context)),
                        new Message("user", prompts.user(context))
                )
        );

        try {
            ChatResponse response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            if (response != null && !response.choices().isEmpty()) {
                return response.choices().get(0).message().content();
            }
        } catch (Exception e) {
            log.error("Groq call failed (model={})", model, e);
        }
        return null; // viser cannedialogue i stedet
    }

    // min openai dto
    record ChatRequest(String model, List<Message> messages) {}
    record Message(String role, String content) {}
    record ChatResponse(List<Choice> choices) {}
    record Choice(Message message) {}
}