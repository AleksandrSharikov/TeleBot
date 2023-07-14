package com.bot.bottom.compillers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.media.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MediaCompiller {

    private InputMedia media;
    private String extension;
    private String name;
    public List<InputMedia> addressToMedia(List<String> addresses){
        List<InputMedia> medias = new ArrayList<>();
        log.info("addresses contains " + addresses.size() + " records");
        for(String address : addresses){
            extension = FilenameUtils.getExtension(address);
            name = FilenameUtils.getName(address);
            media = switch (extension) {
                case "jpg" -> new InputMediaPhoto();
                case "mp4" -> new InputMediaVideo();
            //    case "gif" -> new InputMediaAnimation();
                default -> new InputMediaDocument();
            };
                media.setMedia(new File(address), name);
                medias.add(media);
        }
        log.info("media contains " + medias.size() + " records");
        return medias;
    }
    public List<List<InputMedia>> divideList(List<InputMedia> inList){
        List<List<InputMedia>> answer = new ArrayList<>();


        int counter = 0;
        int totalCounter = 0;

        List<InputMedia> page = new ArrayList<>();
        int size = inList.size();
        for (InputMedia mediaToSend : inList) {

            page.add(mediaToSend);

            if (counter++ == 8 || (totalCounter == size - 1)) {
                if (counter == 1) {
                    page.add(inList.get(0));
                }
                answer.add(page);
                counter = 0;
                page = new ArrayList<>();
            }
            totalCounter++;
        }

        return answer;
    }


}
