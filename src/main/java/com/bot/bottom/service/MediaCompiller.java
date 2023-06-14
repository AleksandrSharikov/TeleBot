package com.bot.bottom.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MediaCompiller {

    public List<InputMedia> addressToMedia(List<String> addresses){
        List<InputMedia> medias = new ArrayList<>();
        InputMedia media;
        String extension;
        String name;
        log.info("addresses contains " + addresses.size() + " records");
        for(String address : addresses){
            extension = FilenameUtils.getExtension(address);
            name = FilenameUtils.getName(address);
            media = switch (extension) {
                case "jpg" -> new InputMediaPhoto();
                case "mp4" -> new InputMediaVideo();
                default -> new InputMediaDocument();
            };
                media.setMedia(new File(address), name);
                medias.add(media);
        }
        log.info("media contains " + medias.size() + " records");
        return medias;
    }
}
