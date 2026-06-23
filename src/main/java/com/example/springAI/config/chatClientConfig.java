package com.example.springAI.config;

import com.example.springAI.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class chatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8);
        return chatClientBuilder
                .defaultOptions(options)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),
                        new TokenUsageAuditAdvisor()))
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help\s
                        employees with questions related to HR policies, such as\s
                        leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics,\s
                        kindly inform them that you can only assist with queries related to\s
                        HR policies.
                        """)
                .defaultUser("How can you help me ?")
                .build();
    }

}
