package com.bot.bottom.service;

import com.bot.bottom.dao.WordDao;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DictionaryService {
    private final WordDao wordDao;

    public DictionaryService(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    public String inputWordProcessor(Update update){
        String message = update.getMessage().getText();
        String answer = "not written in inputWordProcessor yet";
        if (message.contains("=")){
            answer = newSynonyms(message);
        }
        return answer;
    }

    private String newSynonyms(String message){
        System.out.println(message);
        StringBuilder answer;
        String[] words = message.split("=");
        System.out.println(words.length);

        if(words.length > 2){
            return "Только одно слово за раз \n ХХХ = УУУ \n или ХХХ = УУУ, ЙЙЙ, ....";
        }
        if (words[1].contains(",")){
            Set<String> synonyms = Arrays.stream(words[1].split(","))
                    .map(String::toLowerCase).map(String::trim).collect(Collectors.toSet());
            wordDao.addSynonyms(words[0].toLowerCase().trim(), synonyms);
        } else {
            System.out.println("in else");
            return "added " + wordDao.makeSynonym(words[0].trim().toLowerCase(), words[1].trim().toLowerCase());
        }
        answer = new StringBuilder();
        answer.append("added ");
        answer.append(words[0].trim().toLowerCase());
        answer.append(" = \n");
        for (String syn : wordDao.findWord(words[0].trim().toLowerCase()).getSynonyms()){
            System.out.println(syn);
            answer.append(syn);
            answer.append('\n');
        }
        return answer.toString();
    }
}
