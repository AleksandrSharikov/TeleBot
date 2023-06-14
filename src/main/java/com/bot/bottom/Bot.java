package com.bot.bottom;

import com.bot.bottom.config.BotConfig;
import com.bot.bottom.service.DBRegistrator;
import com.bot.bottom.service.MediaCompiller;
import com.bot.bottom.service.Search;
import com.bot.bottom.service.Selector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
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
    private boolean gameFlag;
    private long chatId;
    private String inText;
    private String name;

    private String adreses;

    private final String prefix = "./data/userDoc/";

    public Bot(BotConfig botConfig, Selector selector, MediaCompiller mediaCompiller, Search search, DBRegistrator dbRegistrator) {
        this.botConfig = botConfig;
        this.selector = selector;
        this.mediaCompiller = mediaCompiller;
        this.search = search;
        this.dbRegistrator = dbRegistrator;
    }

    @Override
    public void onUpdateReceived(Update update) {
        int way = 0;
        chatId = update.getMessage().getChatId();
        inText = update.getMessage().getText() == null ?
                "no_text" : update.getMessage().getText();
        name = update.getMessage().getFrom().getFirstName();



        way = selector.select(update);
        switch (way){
            case 1 -> game();
            case 2 -> receivedPhoto(update);
            case 3 -> receiveVideoNote(update);
            case 4 -> receiveVideo(update);
            case 5 -> receiveAnimation(update);
            case 20 -> returnMems(update);

        }
    }

    private void receivedPhoto(Update update) {
        log.info("File received by photo processor");
        List<PhotoSize> photoList = update.getMessage().getPhoto();
        GetFile getFile = new GetFile();

        getFile.setFileId(photoList.get(photoList.size() - 1).getFileId());
        String  getID = String.valueOf(update.getUpdateId());
        String doc_name = update.getMessage().getCaption() == null ? "notTitle" : update.getMessage().getCaption();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            adreses = prefix +getID+"_"+doc_name+ ".jpg";
            downloadFile(file, new File(adreses));
            dbRegistrator.register(update,adreses,2);
            log.info("File " + adreses + "Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }

    }

    private void receiveVideoNote(Update update){
        log.info("File received by video note processor");
      //  List<PhotoSize> photoList = update.getMessage().getPhoto();
        VideoNote videoNote = update.getMessage().getVideoNote();
        GetFile getFile = new GetFile();

        getFile.setFileId(videoNote.getFileId());
        String  getID = String.valueOf(update.getUpdateId());
        String doc_name = update.getMessage().getCaption() == null ? "notTitle" : update.getMessage().getCaption();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            adreses = prefix+getID+"_"+doc_name+ ".mp4";
            downloadFile(file, new File(adreses));
            dbRegistrator.register(update, adreses, 3);
            log.info("File " + adreses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    private void receiveVideo(Update update){
        log.info("File received by video processor");
        Video video = update.getMessage().getVideo();
        GetFile getFile = new GetFile();

        getFile.setFileId(video.getFileId());
        String  getID = String.valueOf(update.getUpdateId());
        String doc_name = update.getMessage().getCaption() == null ? "notTitle" : update.getMessage().getCaption();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            adreses = prefix+getID+"_"+doc_name+ ".mp4";
            downloadFile(file, new File(adreses));
            dbRegistrator.register(update, adreses, 4);
            log.info("File " + adreses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

   private void receiveAnimation(Update update){
        log.info("File received by video processor");
        Animation animation = update.getMessage().getAnimation();
        GetFile getFile = new GetFile();

        getFile.setFileId(animation.getFileId());
        String  getID = String.valueOf(update.getUpdateId());
        String doc_name = update.getMessage().getCaption() == null ? "notTitle" : update.getMessage().getCaption();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            adreses = prefix+getID+"_"+doc_name+ ".gif";
            downloadFile(file, new File(adreses));
            dbRegistrator.register(update, adreses, 4);
            log.info("File " + adreses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }



    private void returnMems(Update update){
        sendMedia(mediaCompiller.addressToMedia(search.search(update.getMessage().getText())));
    }

    private void game() {
    }

    private int choose() {
        if(inText.equals("Загадай цифру")){
            return 1;
        }
        return 0;
    }


    private void sendMessage(String textToSend){
        sendMessage(chatId, textToSend);
    }
    private void sendMessage(Long chatId, String textToSend){
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

    private void sendMedia(List<InputMedia> media){
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setMedias(media);
        try {
            execute(sendMediaGroup);
        }
        catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by sendMedia");
            log.error(e.getMessage());
        }
    }

    private void sendPhoto(Long chatId, String name) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new File("src/main/resources/ANS.JPG")));


            execute(sendPhoto);
        }
       catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by sendPhoto");
            log.error(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();

    }

}