package com.bot.bottom.compillers;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.model.Mem;
import com.bot.bottom.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class MessageCompiller {

    private final Random random = new Random();
    private final DictionaryService dictionaryService;
    private final MemDao memDao;

    public MessageCompiller(DictionaryService dictionaryService, MemDao memDao) {
        this.dictionaryService = dictionaryService;
        this.memDao = memDao;
    }


    public String sayThankYou(Mem mem){
        int cs = 0;
        StringBuilder thank = new StringBuilder();
        thank.append(mem.getSender());
        thank.append(", спасибо, понял, принял обработал\n");
        thank.append("Сохранил ");
        if(mem.getType() == 2){
            thank.append(" твою картинку c \n");
        }  else if(mem.getType() == 4){
            thank.append(" твоё видео c \n");
        } else if(mem.getType() == 5){
            thank.append(" твою гифку с\n");
        } else {
            thank.append(" что бы это ни было c \n");
        }
        thank.append("названием " + mem.getName() + '\n');
        thank.append("ключевым словом: " + mem.getKeyWord() + '\n');
        if(mem.getSecondWords() != null) {
            thank.append("и другими словами: ");
            for (String sw : mem.getSecondWords()) {
                thank.append(sw + ", ");
            }
            thank.replace(thank.length() - 2, thank.length() - 1, "\n");
          //  thank.append('\n');
        }
        cs = random.nextInt(3);
        if(cs == 0) {
            thank.append("Пиши исчё!");
        }
        if(cs == 1) {
            thank.append("Смешно");
        }
        if(cs == 2) {
            thank.append("Наверное смешно. Я не знаю - я бот");
        }
        return thank.toString();
    }

    public String setPhotoToString(Set<String> files, String prefix){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(String s : files){
            sb.append(s);
            sb.append(" -> ");
            sb.append(memDao.findDyAddress(prefix + s) == null
                    ? "not in the base" : memDao.findDyAddress(prefix + s).getName());
            sb.append('\n');
            count++;
        }
        sb.append("Total amount of files: ");
        sb.append(count);
        return sb.toString();
    }

    public String makeLabel(Mem mem){
        StringBuilder label = new StringBuilder();
        boolean flagFirst = false;
        label.append("Name : ");
        label.append(mem.getName());
        label.append('\n');
        label.append("Key word : ");
        label.append(mem.getKeyWord());
        addSynonyms(label, mem.getKeyWord());
        label.append("Other associations : ");
        for (String sw : mem.getSecondWords()) {
            if(!flagFirst){
                flagFirst = true;
            } else {
            label.append(", ");}
            label.append(sw);
        }
        flagFirst = false;
        label.append('\n');
        label.append("Posted by : ");
        label.append(mem.getSender());
        label.append('\n');
        label.append("In : ");
        label.append(mem.getSaveTime());
        label.append('\n');

        return label.toString();
    }
    
    public String makeMap(Map<String, List<String>> map){
        StringBuilder answer = new StringBuilder();
        int count = 0;
        for(Map.Entry<String,List<String>> entry : map.entrySet()){
            answer.append(entry.getKey());
            addSynonyms(answer, entry.getKey());
            for(String name : entry.getValue()){
                answer.append("          ");
                answer.append(name);
                answer.append('\n');
                count++;
            }

        }
        answer.append("Total amount of names: ");
        answer.append(count);
        return answer.toString();
    }
    private void addSynonyms(StringBuilder sb, String word){
        if(dictionaryService.findSynonyms(word) != null) {
            boolean flagFirst = false;
            sb.append(" = (");
            for (String s : dictionaryService.findSynonyms(word)) {
                if (!flagFirst) {
                    flagFirst = true;
                } else {
                    sb.append(", ");
                }
                sb.append(s);
            }
            sb.append(')');
        }
        sb.append('\n');
    }

}
