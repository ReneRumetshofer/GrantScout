package at.fhtw.grantscout.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAIClient openAIClient(@Value("${app.openai.apiKey}") String apiKey) {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }
}
