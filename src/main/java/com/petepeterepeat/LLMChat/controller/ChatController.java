package com.petepeterepeat.LLMChat.controller;

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
import reactor.core.publisher.Mono;

@Controller
public class ChatController {
    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/")
    public String chat(Model model) {
        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String prompt, Model model) {
        Flux<ApiResponse> responses = ollamaService.sendPrompt("mistral:latest", prompt);

        // Store the Flux in the model to be accessed in JavaScript
        model.addAttribute("responses", responses);
        return "chat";
    }

    // SSE endpoint to stream responses
    @GetMapping(value = "/streamResponses", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ApiResponse> streamResponses(@RequestParam String prompt) {
        return ollamaService.sendPrompt("mistral:latest", prompt);
    }
}
