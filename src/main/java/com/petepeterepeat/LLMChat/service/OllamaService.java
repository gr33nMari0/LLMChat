package com.petepeterepeat.LLMChat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petepeterepeat.LLMChat.model.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {
    private final String API_URL = "http://localhost:11434/api";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OllamaService() {
        this.webClient = WebClient.create(API_URL);
        this.objectMapper = new ObjectMapper();
    }

    public Flux<ApiResponse> sendPrompt(String model, String prompt) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);

        return sendRequest(requestBody);
    }

    public Flux<ApiResponse> sendPrompt(String model, String prompt, List<Integer> context) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("context", context);

        return sendRequest(requestBody);
    }

    private Flux<ApiResponse> sendRequest(Object requestBody) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(requestBody);

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/generate").build())
                    .body(BodyInserters.fromValue(jsonPayload))
                    .exchange()
                    .flatMapMany(clientResponse -> clientResponse.bodyToFlux(ApiResponse.class));
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return Flux.error(e);
        }
    }
}
