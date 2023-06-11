package com.bot.bottom.config;

import com.bot.bottom.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {
    private final com.bot.bottom.Bot bot;
    @Autowired
    public BotInitializer(Bot bot) {
        this.bot = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init()throws TelegramApiException {
        log.info("void init starts");
        TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            botApi.registerBot(bot);
            log.info("bot registered");
        } catch (TelegramApiException e){
            log.error("TelegramApiException thrown in initializer");
            log.error(e.getMessage());
        }
    }
}
