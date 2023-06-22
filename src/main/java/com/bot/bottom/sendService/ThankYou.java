package com.bot.bottom.sendService;

import com.bot.bottom.model.Mem;
import com.bot.bottom.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ThankYou {

    private final Random random = new Random();
    private final DictionaryService dictionaryService;

    public ThankYou(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
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

    public String makeLabel(Mem mem){
        StringBuilder label = new StringBuilder();
        boolean flagFirst = false;
        label.append("Name : ");
        label.append(mem.getName());
        label.append('\n');
        label.append("Key word : ");
        label.append(mem.getKeyWord());
        label.append(" = (");
        for(String s : dictionaryService.findSynonyms(mem.getKeyWord())){
            if(!flagFirst){
                flagFirst = true;
            } else {
                label.append(", ");}
        label.append(s);
        }
        label.append(')');
        label.append('\n');
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

}
