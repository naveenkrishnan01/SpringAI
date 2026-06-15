package com.example.springAI.Contoller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MultiModelController {

    private final ChatClient ollamaChatModel;
    private final ChatClient openAiChatModel;

    public MultiModelController(@Qualifier("ollamaChatClient") ChatClient ollamaChatModel,
                                @Qualifier("openAiChatClient") ChatClient openAiChatModel) {
        this.ollamaChatModel = ollamaChatModel;
        this.openAiChatModel = openAiChatModel;
    }

    @RequestMapping("/ollama")
    public String getOllama(@RequestParam("message") String message) {
         return ollamaChatModel.prompt(message).call().content();
    }

    @RequestMapping("/openai")
    public String getOpenAi(@RequestParam("message") String message) {
        return openAiChatModel.prompt(message).call().content();
    }
}
