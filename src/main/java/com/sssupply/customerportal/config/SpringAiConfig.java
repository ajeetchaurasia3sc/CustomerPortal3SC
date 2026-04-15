package com.sssupply.customerportal.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiConfig {

    /**
     * Creates a ChatClient from the auto-configured ChatModel.
     * The model is determined by application.properties:
     *   spring.ai.openai.chat.options.model=gpt-4o-mini
     * Swap to Anthropic or Ollama by changing the spring.ai.* prefix — no code changes needed.
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
