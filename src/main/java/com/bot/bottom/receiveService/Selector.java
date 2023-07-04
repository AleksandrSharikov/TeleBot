package com.bot.bottom.receiveService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class Selector {

    private long deleteFlag;
    private long clearFlag;
    private long baseResetFlag;

    public int select(Update update) {
        log.info("Selector get message " + update);

        if (deleteFlag == update.getMessage().getChatId()) {
            return 16;
        }
        if (clearFlag == update.getMessage().getChatId()){
            return 40;
        }
        if (baseResetFlag == update.getMessage().getChatId()){
            return 42;
        }
        if (update.getMessage().hasPhoto()) {
            log.info("Selector detect photo and set 2");
            return 2;
        }
        if (update.getMessage().hasVideoNote()) {
            log.info("Selector detect video note and set 3");
            return 3;
        }
        if (update.getMessage().hasVideo()) {
            log.info("Selector detect video and set 4");
            return 4;
        }
        if (update.getMessage().hasAnimation()) {
            log.info("Selector detect animation and set 5");
            return 5;
        }
        if (update.getMessage().hasDocument() && update.getMessage().getDocument().getMimeType().equals("application/json")){
            log.info("Selector detect JSON and set 30");
            return 30;
        }
        if (update.getMessage().hasText()) {
            if (update.getMessage().getText().equalsIgnoreCase("/help/")) {
                return 0;
            }
            if (update.getMessage().getText().contains("=")) {
                return 11;
            }
            if (update.getMessage().getText().contains("/key/")) {
                return 12;
            }
            if (update.getMessage().getText().contains("/tag/")) {
                return 13;
            }
            if (update.getMessage().getText().contains("Delete/")) {
                return 15;
            }
                        // 16 returns in the top
            if (update.getMessage().getText().equalsIgnoreCase("Здрасте")) {
                return 21;
            }
                        // 30 receive DB
            if (update.getMessage().getText().equalsIgnoreCase("/ExportDB/")) {
                return 35;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/returnMap/")){
                return 37;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/fileContent/")){
                return 38;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/clearFiles/")){
                return 39;
            }
            // 40 Clear file  flag
            if (update.getMessage().getText().equalsIgnoreCase("/resetBases/")){
                return 41;
            }
            // 42 Reset bases



            return 20;
        }
        return 0;
    }

    public void setDeleteFlag(long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setClearFlag(long clearFlag) { this.clearFlag = clearFlag; }

    public void setBaseResetFlag(long baseResetFlag) {this.baseResetFlag = baseResetFlag; }
}

