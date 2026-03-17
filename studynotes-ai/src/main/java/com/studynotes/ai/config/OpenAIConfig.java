package com.studynotes.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}