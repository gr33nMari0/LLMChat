package com.petepeterepeat.LLMChat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petepeterepeat.LLMChat.model.ApiResponse;
import com.petepeterepeat.LLMChat.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/")
    public String chat(Model model) {
        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String prompt, Model model, String unParsedContext) {
        List<Integer> context = parseContext(unParsedContext);
        Flux<ApiResponse> responses = determineServiceCall(prompt, context);

        // Store the Flux in the model to be accessed in JavaScript
        model.addAttribute("responses", responses);
        return "chat";
    }

    // SSE endpoint to stream responses
    @GetMapping(value = "/streamResponses", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ApiResponse> streamResponses(@RequestParam String prompt, String unParsedContext) {
        List<Integer> context = parseContext(unParsedContext);
        Flux<ApiResponse> responses = determineServiceCall(prompt, context);
        return responses.takeUntil(apiResponse -> apiResponse.isDone());
    }

    private List<Integer> parseContext(String unParsedContext) {
        if (unParsedContext != null && !unParsedContext.isEmpty()) {
            Integer[] contextArray;
            try {
                contextArray = objectMapper.readValue(unParsedContext, Integer[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return Arrays.asList(contextArray);
        }
        return null;
    }

    private Flux<ApiResponse> determineServiceCall(String prompt, List<Integer> context) {
        if (context == null || context.isEmpty()) {
            return ollamaService.sendPrompt("mistral:latest", prompt);
        } else {
            return ollamaService.sendPrompt("mistral:latest", prompt, context);
        }
    }
}
