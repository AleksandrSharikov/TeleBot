package com.bot.bottom.sendService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.media.*;

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

    public InputMedia addressToMedia(String address){
        extension = FilenameUtils.getExtension(address);
        name = FilenameUtils.getName(address);
        media = switch (extension) {
            case "jpg" -> new InputMediaPhoto();
            case "mp4" -> new InputMediaVideo();
            //    case "gif" -> new InputMediaAnimation();
            default -> new InputMediaDocument();
        };
        media.setMedia(new File(address), name);
        return media;
    }

}
