package com.studynotes.ai.ai;

import com.studynotes.ai.config.OpenAIConfig;
import com.studynotes.ai.model.GroqResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIClient {

    private final WebClient webClient;

    public OpenAIClient(OpenAIConfig config) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader("Authorization", "Bearer " + config.getApiKey())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String generateNotes(String textAndPrompt) {

        Map<String, Object> request = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", textAndPrompt
                        )
                )
        );

        GroqResponse response = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqResponse.class)
                .block();

        return response.choices.get(0).message.content;
    }
}