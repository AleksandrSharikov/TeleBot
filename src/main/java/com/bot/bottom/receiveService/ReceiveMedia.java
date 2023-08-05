package com.bot.bottom.receiveService;

import com.bot.bottom.config.BotConfig;
import com.bot.bottom.dto.MediaToSave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.api.objects.games.Animation;

import java.util.List;

@Slf4j
@Service
public class ReceiveMedia {
    private final BotConfig botConfig;

    public ReceiveMedia(BotConfig botConfig) {
        this.botConfig = botConfig;
    }


    public MediaToSave receiveMedia(Update update) {
        String address;
        String prefix = botConfig.getMediaPrefix();
        String extension = ".txt";
        GetFile getFile = new GetFile();
        String getID = String.valueOf(update.getUpdateId());
        String doc_name = "0";
        int type = 0;
        if(update.getMessage().hasPhoto()){
            log.info("ReceiveMedia.receiveMedia get photo");
            type = 2;
            extension = ".jpg";

            List<PhotoSize> photoList = update.getMessage().getPhoto();
            getFile.setFileId(photoList.get(photoList.size() - 1).getFileId());

        } else if(update.getMessage().hasVideoNote()){
            log.info("ReceiveMedia.receiveMedia get video note");
            type = 3;
            extension = ".mp4";

            VideoNote videoNote = update.getMessage().getVideoNote();
            getFile.setFileId(videoNote.getFileId());

        }else if(update.getMessage().hasVideo()){
            log.info("ReceiveMedia.receiveMedia get video");
            type = 4;
            extension = ".mp4";

            Video video = update.getMessage().getVideo();
            getFile.setFileId(video.getFileId());

        } else if(update.getMessage().hasAnimation()){
            log.info("ReceiveMedia.receiveMedia get animation");
            type = 5;
            extension = ".mp4";

            Animation animation = update.getMessage().getAnimation();
            getFile.setFileId(animation.getFileId());

        } else {
            log.warn("ReceiveMedia.receiveMedia get something weird");
        }
        address = prefix + getID + "_" + doc_name + extension;

        return new MediaToSave(getFile, address, type);
    }
}
