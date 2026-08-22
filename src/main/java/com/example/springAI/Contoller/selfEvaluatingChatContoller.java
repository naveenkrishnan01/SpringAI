package com.example.springAI.Contoller;

import com.example.springAI.exception.InvalidAnswerException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/api")
public class selfEvaluatingChatContoller {

    private final ChatClient chatClient;
    @Value("classpath:/promptTemplates/pb.st")
    Resource pbTemplate;
    private final FactCheckingEvaluator factCheckingEvaluator;

    public selfEvaluatingChatContoller(ChatClient.Builder chatClientBuilder,
                                       @Value("classpath:/promptTemplates/factCheck.st") Resource factCheckTemplate) throws IOException {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(chatClientBuilder)
                .evaluationPrompt(factCheckTemplate.getContentAsString(Charset.defaultCharset())).build();
    }


    @GetMapping("/chat/selfEvaluatingChatPrompt")
    public String chatPrompt(@RequestParam("message") String message) {
        return chatClient
                .prompt().system(pbTemplate)
                .user(message)
                .call().content();
    }

    @Retryable(retryFor = InvalidAnswerException.class, maxAttempts = 3)
    @GetMapping("/evaluate/chat")
    public String chat(@RequestParam("message") String message) {
        String aiResponse = chatClient.prompt().user(message)
                .call().content();
        validateAnswer(message, aiResponse);
        return aiResponse;
    }

    private void validateAnswer(String question, String answer) {
        EvaluationRequest evaluationRequest =
                new EvaluationRequest(question, List.of(), answer);
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            throw new InvalidAnswerException(question, answer);
        }
    }
}
