package com.bot.bottom;

import com.bot.bottom.config.BotConfig;
import com.bot.bottom.dto.MemDTO;
import com.bot.bottom.receiveService.DBRegistrator;
import com.bot.bottom.receiveService.Selector;
import com.bot.bottom.compillers.MediaCompiller;
import com.bot.bottom.compillers.MessageCompiller;
import com.bot.bottom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
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
    private final MessageCompiller messageCompiller;
    private final Search search;
    private final DBRegistrator dbRegistrator;
    private final MessageCompiller thankYou;
    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final FileService fileService;
    private final BasesService basesService;
    private long chatId;
    private final String doc_name = "0";
    private String addresses;
    private final String prefix = "./data/photoBase/";

    public Bot(BotConfig botConfig, Selector selector, MediaCompiller mediaCompiller, MessageCompiller messageCompiller,
               Search search, DBRegistrator dbRegistrator, MessageCompiller thankYou, UserService userService,
               DictionaryService dictionaryService, FileService fileService, BasesService basesService) {
        this.botConfig = botConfig;
        this.selector = selector;
        this.mediaCompiller = mediaCompiller;
        this.messageCompiller = messageCompiller;
        this.search = search;
        this.dbRegistrator = dbRegistrator;
        this.thankYou = thankYou;
        this.userService = userService;
        this.dictionaryService = dictionaryService;
        this.fileService = fileService;
        this.basesService = basesService;
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
            case 30 -> receiveDB(update);
            case 35 -> sendDB(update);
            case 37 -> returnMap(update);
            case 38 -> photoFiles(update);
            case 39 -> askClearFiles(update);
            case 40 -> clearFiles(update);
            case 41 -> askResetBases(update);
            case 42 -> resetBases(update);
        }
    }




// 2 receive photo_________________________________________________________

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

//  3 receive video note________________________________________
    private void receiveVideoNote(Update update) {
        log.info("File received by video note processor");
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

    // 4 receive video________________________________________________________

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

    // 5 receive animation_______________________________________________________________

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

    // 11 Working with dictionary_______________________________________________________
    private void dictionary(Update update) {
        sendMessage(dictionaryService.inputWordProcessor(update));
    }

    // 12 change keyWord________________________________________________________________

    private void changeKeyword(Update update) {
        sendMessage(dbRegistrator.changeKeyWord(update));
    }

    // 15 ask delete file _______________________________________________________
    private void askDeleteFile(Update update) {
        sendMessage(fileService.askDeleteFile(update));
    }
    // 16 delete file_____________________________________________________________
    private void deleteFile(Update update) {
        sendMessage(fileService.deleteFile(update));
    }

    // 20 return mems _______________________________________________________________
    private void returnMems(Update update) {
        List<String> mems = search.search(update.getMessage().getText());
        if (mems.size() > 1) {
            sendMedia(mediaCompiller.addressToMedia(mems));
        }
        if (mems.size() == 1) {
            String caption = null;
            if (update.getMessage().getText().matches("^name/.*")) {
                caption = thankYou.makeLabel(search.findMemByAddress(mems.get(0)));
            }
            sendOne(mems.get(0), caption);
        }
        if (mems.isEmpty()) {
            sendMessage("nothing has been found");
        }
    }

    //30 receive DB____________________________________________________________

    private void receiveDB(Update update) {
        Document document = update.getMessage().getDocument();
        GetFile getFile = new GetFile();

        getFile.setFileId(document.getFileId());
        String baseName = update.getMessage().getDocument().getFileName().toLowerCase();

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            addresses = prefix + baseName;
            downloadFile(file, new File(addresses));
            sendMessage("File has been gotten");
            sendMessage(basesService.importBase(addresses));
            log.info("File " + addresses + " Saved");
        } catch (TelegramApiException e) {
            log.error("TelegramApiException thrown by receivePhoto");
            log.error(e.getMessage());
        }
    }

    // 35 Export DB______________________________________________

    private void sendDB(Update update) {
        List<String> addressList = basesService.exportBD();
        for(String address : addressList) {
            try {
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(new InputFile(new File(address)));
                // sendDocument.setCaption(caption);
                execute(sendDocument);
            } catch (TelegramApiException e) {
                log.error("TelegramApiException thrown by sendDB");
                log.error(e.getMessage());
            }
        }
    }
    // 37 Return map____________________________________________________________________
    private void returnMap(Update update) { sendMessage(search.printMap());  }
    // 38 Send file list__________________________________________________________________
    private void photoFiles(Update update) {
        sendMessage(messageCompiller.setPhotoToString(fileService.photoList(prefix),prefix));
    }
    // 39 Ask cleat files___________________________________________________________________________
    private void askClearFiles(Update update){
        sendMessage(fileService.askClearFiles(update));
    }

    // 40 Clear files__________________________________________________________________________
    private void clearFiles(Update update){
        sendMessage(fileService.clearFile(update, prefix));
    }

    // 41 Ask reset bases _______________________________________________________________________
    private void askResetBases(Update update) {
        sendMessage(basesService.askResetBases(update));
    }

    // 42 Reset bases __________________________________________________________________________
    private void resetBases(Update update) {
        sendMessage(basesService.resetBases(update));
    }



// Send One media___________________________________________________________________________________

    private void sendOne(String address) {
        sendOne(address, null);
    }
    private void sendOne(String address, String caption) {                  // bad style hardcode chatId. change!
        String extension = FilenameUtils.getExtension(address);
        if (extension.equals("jpg")) {
            sendPhoto(chatId, address, caption);
        } else if (extension.equals("mp4")) {
            sendVideo(chatId, address, caption);
        } else {
            sendMessage("error in sendOne");
        }
    }

    // Send several media________________________________________________________________________
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



    // Send message_________________________________________________________________________
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

// Send photo or Video _____________________________________________________________________

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



    private void game() { }

    // Technical ________________________________________________________________________
    @Override
    public String getBotUsername() { return botConfig.getBotName(); }
    @Override
    public String getBotToken() { return botConfig.getToken();}


}