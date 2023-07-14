package com.bot.bottom.receiveService;


import com.bot.bottom.dao.MemDaoImpl;
import com.bot.bottom.dto.MemDTO;
import com.bot.bottom.model.Mem;
import com.bot.bottom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DBRegistrator {
    private final MemDaoImpl memDao;
    private final UserService userService;

    public DBRegistrator(MemDaoImpl memDao, UserService userService) {
        this.memDao = memDao;
        this.userService = userService;
    }

    public Mem register(Update update, String address, int type){
        String name = String.valueOf(update.getUpdateId());
        String keyword;
        String caption;
        Set<String> secondWords = null;
        String[] parts;
        long senderId = update.getMessage().getFrom().getId();
        String sender = update.getMessage().getFrom().getFirstName();
        LocalDateTime time = LocalDateTime.now();
            if(update.getMessage().getCaption() == null){
                keyword = "unsorted";
            } else {
                caption = update.getMessage().getCaption().toLowerCase();
                if (caption.contains("/")) {
                    parts = caption.split("/", 2);
                    name = parts[0].trim();
                    caption = parts[1];
                }
                if(!caption.isEmpty()){
                       parts = caption.split(",");
                       keyword = parts[0].trim();
                       if(parts.length > 1){
                           secondWords = new HashSet<>();
                           Arrays.stream(parts).skip(1).map(String::trim).forEach(secondWords::add);
                       }
                   } else {
                    keyword = "unsorted";
                }
            }
            Mem mem = new Mem(keyword,address,name,secondWords,type,time, sender, senderId);
            mem = memDao.save(mem);
            userService.postMem(mem);
            log.info("Added to DB: " + mem);
        return mem;
    }

    public MemDTO changeKeyWord(Update update){
        String[] keyWords = update.getMessage().getText().toLowerCase().split("/key/");
        String old;

        if(keyWords.length != 2){
            return new MemDTO(null, "Format error");
        }
        if(memDao.findByName(keyWords[0]).isEmpty()){
            return new MemDTO(null, "File " + keyWords[0] + " have not been found");
        }
        old = memDao.findByName(keyWords[0]).get().getKeyWord();
        if(old.equals(keyWords[1])){
            return new MemDTO(null, "Keyword already is " + old);
        }
        memDao.setKeyWord(keyWords[0], keyWords[1]);
        return new MemDTO(memDao.findByName(keyWords[0]).get(),
                "Keyword changed from " + old + " to " + memDao.findByName(keyWords[0]).get().getKeyWord());
    }


    public MemDTO addTag(Update update){
        String[] tag = update.getMessage().getText().toLowerCase().split("/tag/");
        Set<String> old;
        StringBuilder message = new StringBuilder();

        if(tag.length != 2){
            return new MemDTO(null, "Format error");
        }
        if(memDao.findByName(tag[0]).isEmpty()){
            return new MemDTO(null, "File " + tag[0] + " have not been found");
        }
        old = memDao.findByName(tag[0]).get().getSecondWords();
        if(old != null && old.contains(tag[1])){
            return new MemDTO(null, "Tag already presented " + old);
        }
        memDao.addSecondWord (tag[0], tag[1]);
        for (String s : memDao.findByName(tag[0]).get().getSecondWords()){
            message.append('\n');
            message.append(s);
        }

        return new MemDTO(memDao.findByName(tag[0]).get(),
                "Tags list contains: " + message.toString());
    }

}