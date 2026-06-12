package com.example.springAI.Contoller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class chatContoller {

    private final ChatClient chatClient;

    public chatContoller(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam ("message") String message) {
        return this.chatClient.prompt(message).call().content();
    }
    }

