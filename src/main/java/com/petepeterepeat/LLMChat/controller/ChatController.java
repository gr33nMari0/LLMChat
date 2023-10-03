//package com.petepeterepeat.LLMChat.controller;
//
//import com.petepeterepeat.LLMChat.model.ApiResponse;
//import com.petepeterepeat.LLMChat.service.OllamaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class ChatController {
//    @Autowired
//    private OllamaService ollamaService;
//
//    @GetMapping("/")
//    public String chat(Model model) {
//        return "chat";
//    }
//
//    @PostMapping("/send")
//    public String sendMessage(@RequestParam String prompt, Model model) {
//        ApiResponse response = ollamaService.sendPrompt("mistral:latest", prompt);
//        model.addAttribute("response", response.getResponse());
//        return "chat";
//    }
//}


package com.petepeterepeat.LLMChat.controller;

import com.petepeterepeat.LLMChat.model.ApiResponse;
import com.petepeterepeat.LLMChat.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Mono<ApiResponse> response = ollamaService.sendPrompt("mistral:latest", prompt);
        model.addAttribute("response", response.block().getResponse());  // Note: blocking here, in reality, you'd want more reactive flow
        return "chat";
    }
}
