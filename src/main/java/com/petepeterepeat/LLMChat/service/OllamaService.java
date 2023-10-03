package com.petepeterepeat.LLMChat.service;

import com.petepeterepeat.LLMChat.model.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class OllamaService {
    private final String API_URL = "http://localhost:11434/api";
    private final WebClient webClient;

    public OllamaService() {
        this.webClient = WebClient.create(API_URL);
    }

    public Flux<ApiResponse> sendPrompt(String model, String prompt) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/generate").build())
                .body(BodyInserters.fromValue(String.format("{\"model\": \"%s\",\"prompt\": \"%s\"}", model, prompt)))
                .retrieve()
                .bodyToFlux(ApiResponse.class);
    }
}
