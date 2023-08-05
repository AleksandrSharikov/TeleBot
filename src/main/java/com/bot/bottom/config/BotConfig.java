package com.bot.bottom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String token;
    @Value("${file.prefix}")
    private String prefix;
    @Value("${media.prefix}")
    private String mediaPrefix;
    private final String version = "2.0 compiled 05.08.2023 REST_MS";

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
    public String getVersion(){return version;}
}