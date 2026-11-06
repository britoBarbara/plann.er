package br.com.barbara.planner.infra.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY") != null ?System.getenv("OPENAI_API_KEY") : "demo")
                .modelName("gpto-mini-")
                .build();
    }
}
