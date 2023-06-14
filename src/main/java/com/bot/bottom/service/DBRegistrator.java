package com.bot.bottom.service;


import com.bot.bottom.dao.MemDaoImpl;
import com.bot.bottom.model.Mem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Slf4j
@Service
public class DBRegistrator {
    private final MemDaoImpl memDao;

    public DBRegistrator(MemDaoImpl memDao) {
        this.memDao = memDao;
    }

    public Mem register(Update update, String address, int type){
        String name;
        String keyword;
        String caption;
        String[] parts;
        LocalDateTime time = LocalDateTime.now();
            if(update.getMessage().getCaption() == null){
                name = String.valueOf(update.getUpdateId());
                keyword = "unsorted";
            } else {
                caption = update.getMessage().getCaption();
                if (caption.charAt(0) == '"'){
                   parts = caption.split("\"");
                   name = parts[0];
                   keyword = parts.length > 1 ? parts[1] : "unsorted";
                } else {
                    name = String.valueOf(update.getUpdateId());
                    keyword = caption;
                }
            }
            Mem mem = new Mem(keyword,address,name,null,type,time);
            memDao.save(mem);
            log.info("Added to DB: " + mem.toString());
        return mem;
    }

}
