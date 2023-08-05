package com.bot.bottom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;
    @Value("${file.prefix}")
    String prefix;

    @Value("${media.prefix}")
    String mediaPrefix;

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMediaPrefix() {
        return mediaPrefix;
    }
}