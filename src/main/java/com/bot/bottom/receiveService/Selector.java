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
        log.info("Selector get message:   " + update + '\n');

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
            return 2;
        }
        if (update.getMessage().hasVideo()) {
            log.info("Selector detect video and set 4");
            return 2;
        }
        if (update.getMessage().hasAnimation()) {
            log.info("Selector detect animation and set 5");
            return 2;
        }
        if (update.getMessage().hasDocument() && update.getMessage().getDocument().getMimeType().equals("application/json")){
            log.info("Selector detect JSON and set 30");
            return 30;
        }
        if (update.getMessage().hasText()) {
            if (update.getMessage().getText().equalsIgnoreCase("/help/")) {
                log.info("Selector detect help request and return 0");
                return 0;
            }
            if (update.getMessage().getText().contains("=")) {
                log.info("Selector find = and returns 11");
                return 11;
            }
            if (update.getMessage().getText().contains("/key/")) {
                log.info("Selector find /key/ and returns 12");
                return 12;
            }
            if (update.getMessage().getText().contains("/tag/")) {
                log.info("Selector find /tag/ and returns 13");
                return 13;
            }
            if (update.getMessage().getText().contains("Delete/")) {
                log.info("Selector find Delete/ and returns 15");
                return 15;
            }
                        // 16 returns in the top
            if (update.getMessage().getText().equalsIgnoreCase("Здрасте")) {
                return 21;
            }
                        // 30 receive DB
            if (update.getMessage().getText().equalsIgnoreCase("/ExportDB/")) {
                log.info("Selector find /ExportDB/ and returns 35");
                return 35;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/tags/")){
                log.info("Selector find /tags/ and returns 36");
                return 36;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/returnMap/")){
                log.info("Selector find /returnMap/ and returns 37");
                return 37;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/fileContent/")){
                log.info("Selector find /fileContent/ and returns 38");
                return 38;
            }
            if (update.getMessage().getText().equalsIgnoreCase("/clearFiles/")){
                log.info("Selector find /clearFiles/ and returns 39");
                return 39;
            }
            // 40 Clear file  flag
            if (update.getMessage().getText().equalsIgnoreCase("/resetBases/")){
                log.info("Selector find /resetBases/ and returns 40");
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

