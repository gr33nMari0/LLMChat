//package com.petepeterepeat.LLMChat.service;
//
//import com.petepeterepeat.LLMChat.model.ApiResponse;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class OllamaService {
//    private final String API_URL = "http://localhost:11434/api/generate";
//
//    public ApiResponse sendPrompt(String model, String prompt) {
//        RestTemplate restTemplate = new RestTemplate();
//        String jsonPayload = String.format("{\"model\": \"%s\",\"prompt\": \"%s\"}", model, prompt);
//        return restTemplate.postForObject(API_URL, jsonPayload, ApiResponse.class);
//    }
//}


package com.petepeterepeat.LLMChat.service;

import com.petepeterepeat.LLMChat.model.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OllamaService {
    private final String API_URL = "http://localhost:11434/api/generate";
    private final WebClient webClient;

    public OllamaService() {
        this.webClient = WebClient.create(API_URL);
    }

    public Mono<ApiResponse> sendPrompt(String model, String prompt) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/generate").build())
                .body(BodyInserters.fromValue(String.format("{\"model\": \"%s\",\"prompt\": \"%s\"}", model, prompt)))
                .retrieve()
                .bodyToMono(ApiResponse.class);
    }
}
