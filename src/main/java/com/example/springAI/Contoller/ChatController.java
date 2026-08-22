package com.example.springAI.Contoller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;
    @Value("classpath:/promptTemplates/pb.st")
    Resource pbTemplate;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam ("message") String message) {
        return chatClient.prompt(message).call().content();
    }

    @GetMapping("/chat/prompt-stuffing")
    public String  promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt().system(pbTemplate)
                .user(message)
                .call().content();
    }
    }


