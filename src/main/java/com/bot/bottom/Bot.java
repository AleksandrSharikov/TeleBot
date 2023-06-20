package com.bot.bottom;

import com.bot.bottom.config.BotConfig;
import com.bot.bottom.dto.MemDTO;
import com.bot.bottom.receiveService.DBRegistrator;
import com.bot.bottom.receiveService.Selector;
import com.bot.bottom.sendService.MediaCompiller;
import com.bot.bottom.sendService.ThankYou;
import com.bot.bottom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final Selector selector;
    private final MediaCompiller mediaCompiller;
    private final Search search;
    private final DBRegistrator dbRegistrator;
    private final ThankYou thankYou;
    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final FileService fileService;
    private long chatId;
    private final String doc_name = "0";
    private String addresses;
    private final String prefix = "./data/userDoc/";

    public Bot(BotConfig botConfig, Selector selector, MediaCompiller mediaCompiller, Search search, DBRegistrator dbRegistrator, ThankYou thankYou, UserService userService, DictionaryService dictionaryService, FileService fileService) {
        this.botConfig = botConfig;
        this.selector = selector;
        this.mediaCompiller = mediaCompiller;
        this.search = search;
        this.dbRegistrator = dbRegistrator;
        this.thankYou = thankYou;
        this.userService = userService;
        this.dictionaryService = dictionaryService;
        this.fileService = fileService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        int way;
        chatId = update.getMessage().getChatId();
        userService.getUserInfo(update);


        way = selector.select(update);
        switch (way) {
            case 1 -> game();
            case 2 -> receivedPhoto(update);
            case 3 -> receiveVideoNote(update);
            case 4 -> receiveVideo(update);
            case 5 -> receiveAnimation(update);
            case 11 -> dictionary(update);
            case 12 -> changeKeyword(update);
            case 15 -> askDeleteFile(update);
            case 16 -> deleteFile(update);
            case 20 -> returnMems(update);
            case 21 -> sendMessage("Забор покрасьте!");
        }
    }


    private void dictionary(Update update) {
        sendMessage(dictionaryService.inputWordProcessor(update));
    }

    private void receivedPhoto(Update update) {
        log.info("File received by photo processor");
        List<PhotoSize> photoList = update.getMessage().getPhoto();
        GetFile getFile = new GetFile();

        getFile.setFileId(photoList.get(photoList.size() - 1).getFileId());
        String getID = String.valueOf(update.getUpdateId());

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            addresses = prefix + getID + "_" + doc_name + ".jpg";
            downloadFile(file, new File(addresses));
            sendMessage(thankYou.sayThankYou(dbRegistrator.register(update, addresses, 2)));
            log.info("File " + addresses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    private void receiveVideoNote(Update update) {
        log.info("File received by video note processor");
        //  List<PhotoSize> photoList = update.getMessage().getPhoto();
        VideoNote videoNote = update.getMessage().getVideoNote();
        GetFile getFile = new GetFile();

        getFile.setFileId(videoNote.getFileId());
        String getID = String.valueOf(update.getUpdateId());

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            addresses = prefix + getID + "_" + doc_name + ".mp4";
            downloadFile(file, new File(addresses));
            sendMessage(thankYou.sayThankYou(dbRegistrator.register(update, addresses, 3)));
            log.info("File " + addresses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    private void receiveVideo(Update update) {
        log.info("File received by video processor");
        Video video = update.getMessage().getVideo();
        GetFile getFile = new GetFile();

        getFile.setFileId(video.getFileId());
        String getID = String.valueOf(update.getUpdateId());

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            addresses = prefix + getID + "_" + doc_name + ".mp4";
            downloadFile(file, new File(addresses));
            sendMessage(thankYou.sayThankYou(dbRegistrator.register(update, addresses, 4)));
            log.info("File " + addresses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    private void receiveAnimation(Update update) {
        log.info("File received by video processor");
        Animation animation = update.getMessage().getAnimation();
        GetFile getFile = new GetFile();

        getFile.setFileId(animation.getFileId());
        String getID = String.valueOf(update.getUpdateId());

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            addresses = prefix + getID + "_" + doc_name + ".mp4";
            downloadFile(file, new File(addresses));
            sendMessage(thankYou.sayThankYou(dbRegistrator.register(update, addresses, 5)));
            log.info("File " + addresses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    private void returnMems(Update update) {
        List<String> mems = search.search(update.getMessage().getText());
        if (mems.size() > 1) {
            sendMedia(mediaCompiller.addressToMedia(mems));
        }
        if (mems.size() == 1) {
            sendOne(mems.get(0));
        }
        if (mems.isEmpty()) {
            sendMessage("nothing has been found");
        }
    }

    private void sendOne(String address) {
        sendOne(address, null);
    }

    private void sendOne(String address, String caprion) {                  // bad style hardcode chatId. change!
        String extension = FilenameUtils.getExtension(address);
        if (extension.equals("jpg")) {
            sendPhoto(chatId, address, caprion);
        } else if (extension.equals("mp4")) {
            sendVideo(chatId, address, caprion);
        } else {
            sendMessage("error in sendOne");
        }
    }
    private void game() { }
    private void sendMessage(MemDTO memDTO) {
        if (memDTO.mem() == null) {
            sendMessage(memDTO.comment());
        } else {
            sendOne(memDTO.mem().getAddress(), memDTO.comment());
        }
    }

    private void sendMessage(String textToSend) {
        sendMessage(chatId, textToSend);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown in sendMessage");
            log.error(e.getMessage());
        }
    }

    private void sendMedia(List<InputMedia> media) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setMedias(media);
        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by sendMedia");
            log.error(e.getMessage());
        }
    }

    private void sendPhoto(String address) {
        sendPhoto(chatId, address);
    }

    private void sendPhoto(long chatId, String address) {
        sendPhoto(chatId, address, null);
    }

    private void sendPhoto(Long chatId, String address, String caption) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new File(address)));
            sendPhoto.setCaption(caption);
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by sendPhoto");
            log.error(e.getMessage());
        }
    }

    private void sendVideo(String address) {
        sendVideo(chatId, address);
    }

    private void sendVideo(long chatId, String address) {
        sendVideo(chatId, address, null);
    }

    private void sendVideo(Long chatId, String address, String caption) {
        try {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            sendVideo.setVideo(new InputFile(new File(address)));
            sendVideo.setCaption(caption);
            execute(sendVideo);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by sendVideo");
            log.error(e.getMessage());
        }
    }

    private void askDeleteFile(Update update) {
        sendMessage(fileService.askDeleteFile(update));
    }
    private void deleteFile(Update update) {
        sendMessage(fileService.deleteFile(update));
    }
    private void changeKeyword(Update update) {
        sendMessage(dbRegistrator.changeKeyWord(update));
    }


    @Override
    public String getBotUsername() { return botConfig.getBotName(); }
    @Override
    public String getBotToken() { return botConfig.getToken();}
}