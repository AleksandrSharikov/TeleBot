package com.bot.bottom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class Selector {
    public int select(Update update){
        log.info("Selector get message " + update);

        if (update.getMessage().hasPhoto()){
            log.info("Selector detect photo and set 2");
            return 2;
        }
        if (update.getMessage().hasVideoNote()){
            log.info("Selector detect video note and set 3");
            return 3;
        }
        if (update.getMessage().hasVideo()){
            log.info("Selector detect video and set 4");
            return 4;
        }
        if (update.getMessage().hasAnimation()){
            log.info("Selector detect animation and set 5");
            return 5;
        }
        if (update.getMessage().hasText()){
            if (update.getMessage().getText().equalsIgnoreCase("Здрасте")){
                return 21;
            }
            return 20;
        }
        return 0;
    }
}
