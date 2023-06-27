package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.dao.UserDao;
import com.bot.bottom.dao.WordDao;
import com.bot.bottom.model.Mem;
import com.bot.bottom.model.User;
import com.bot.bottom.model.Word;
import com.bot.bottom.receiveService.Selector;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
//import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//import org.json.simple.parser.JSONParser;


//import org.telegram.telegrambots.api.methods.GetFile;

@Slf4j
@Service
public class BasesService {
    private final MemDao memDao;
    private final UserDao userDao;
    private final WordDao wordDao;
    private final FileService fileService;
    private final Selector selector;

    private final String writePrefix = "./data/dbLog/";
    private String add = "";

    public BasesService(MemDao memDao, UserDao userDao, WordDao wordDao, FileService fileService, Selector selector) {
        this.memDao = memDao;
        this.userDao = userDao;
        this.wordDao = wordDao;
        this.fileService = fileService;
        this.selector = selector;
    }

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Instant instant = Instant.parse(json.toString().substring(1,25));
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }).registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
       // @Override
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }
    }).setPrettyPrinting().create();

    Type memListType = new TypeToken<ArrayList<Mem>>(){}.getType();
    Type dictionaryType = new TypeToken<ArrayList<Word>>(){}.getType();
    Type userListType = new TypeToken<ArrayList<User>>(){}.getType();

    // Import bases _______________________________________________________________________________

    public String importBase(String address){
        String answer = "Something went wrong";


            try (FileReader reader = new FileReader(address)){
                if(address.contains("mem")) {
                    List<Mem> memBase = gson.fromJson(reader, memListType);
                    memDao.importDB(memBase);
                }
                if(address.contains("dictionary")) {
                    List<Word> dictionary = gson.fromJson(reader, dictionaryType);
                    wordDao.importDB(dictionary);
                }
                if(address.contains("users")) {
                    List<User> users = gson.fromJson(reader, userListType);
                    userDao.importDB(users);
                }
                answer = "Base updated";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {

                log.info("File " + address + " deleted = " + fileService.removeFile(address));
            }
        return answer;
    }

    // Export bases ____________________________________________________________________________________

    public List<String> exportBD(){
        List<String> answer = new ArrayList<>();
        String date = LocalDate.now().toString();

        String writeAddress = writePrefix + date + add;

        List<Mem> memBase = memDao.findAll();
        List<Word> dictionary = wordDao.findAll();
        List<User> userBase = userDao.findAll();
        log.info("Bases loged. User base: " + userBase.size() + "records. Dictionary: " + dictionary.size() +
                "records. Mems: " + memBase.size() + " records.");

        if(!memBase.isEmpty()) {
            try (FileWriter writer = new FileWriter(writeAddress + "_mem.json")) {
                gson.toJson(memBase, writer);

            } catch (IOException e) {
                log.warn("Exception in memBaseWriter");
                throw new RuntimeException(e);
            }
            answer.add(writeAddress + "_mem.json");
        }


        if(!dictionary.isEmpty()) {
            try (FileWriter writer = new FileWriter(writeAddress + "_dictionary.json")) {
                gson.toJson(dictionary, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            answer.add(writeAddress + "_dictionary.json");
        }

        if(!userBase.isEmpty()) {
            try (FileWriter writer = new FileWriter(writeAddress + "_user.json")) {
                gson.toJson(userBase, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            answer.add(writeAddress + "_user.json");
        }

        return answer;
    }

    // Reset bases  ________________________________________________________________________

    public String askResetBases(Update update){
        selector.setBaseResetFlag(update.getMessage().getChatId());
        return "What base do you want to reset?";
    }

    public String resetBases(Update update) {
        selector.setBaseResetFlag(0);
        long count = 0;
        String text = update.getMessage().getText();
        if(text.equalsIgnoreCase("Mems")){
            add = "reset_" + update.getUpdateId();
            exportBD();
            memDao.deleteAll();
            add = "";
            return "Mem base is empty now";
        }
        if(text.equalsIgnoreCase("Dictionary")){
            add = "reset_" + update.getUpdateId();
            exportBD();
            wordDao.deleteAll();
            add = "";
            return "Dictionary is empty now";
        }
        if(text.equalsIgnoreCase("Users")){
            add = "reset_user_" + update.getUpdateId();
            exportBD();
            userDao.deleteAll();
            add = "";
            return "User base is empty now";
        }
        if(text.equalsIgnoreCase("All")){
            add = "reset_all_" + update.getUpdateId();
            exportBD();
            wordDao.deleteAll();
            userDao.deleteAll();
            memDao.deleteAll();
            add = "";
            return "Bases reset";
        }

        return "Bases reset canceled";

    }




}
