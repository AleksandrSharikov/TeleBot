package com.bot.bottom.service;

import com.bot.bottom.compillers.MessageCompiller;
import com.bot.bottom.dao.MemDao;
import com.bot.bottom.model.Mem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Search {
    private final MemDao memDao;
    private final DictionaryService dictionaryService;
    private final MessageCompiller messageCompiller;

    public Search(MemDao memDao, DictionaryService dictionaryService, MessageCompiller messageCompiller) {
        this.memDao = memDao;
        this.dictionaryService = dictionaryService;
        this.messageCompiller = messageCompiller;
    }

    public Mem findMemByAddress(String address){
        return memDao.findDyAddress(address);
    }

    public List<String> search(String toFind){
        toFind = toFind.toLowerCase();
        List<String> answer;
        log.info("Looking for "+ toFind);
        if(toFind.equals("/all/")){
            return memDao.findAll().stream().
                    map(Mem::getAddress).toList();
        }
        if(toFind.matches("^name/.*")){
        answer = new ArrayList<>();
        toFind = toFind.substring(5).trim();
        log.info("Detected name search. Looking for {}", toFind);
            if (memDao.findByName(toFind).isPresent()){
                answer.add(memDao.findByName(toFind).get().getAddress());
            }
            return answer;
        } else {
            answer = new ArrayList<>();
            if (memDao.findByName(toFind).isPresent()){
                answer.add(memDao.findByName(toFind).get().getAddress());
            }
            if (!memDao.findByKeyword(toFind).isEmpty()){
                memDao.findByKeyword(toFind).stream().map(Mem::getAddress)
                        .forEach(answer::add);
            }
            if (dictionaryService.findSynonyms(toFind) != null){
                for(String synonym : dictionaryService.findSynonyms(toFind)){
                    if (!memDao.findByKeyword(synonym).isEmpty()){
                        memDao.findByKeyword(synonym).stream()
                                .map(Mem::getAddress)
                                .forEach(answer::add);
                    }
                }
            }
            if (!memDao.findBySecondWord(toFind).isEmpty()){
                memDao.findBySecondWord(toFind).stream().map(Mem::getAddress)
                        .forEach(answer::add);
            }
            return answer;       // not tested 07022023
        }
    }

    public List<String>  printMap(){
        return messageCompiller.makeMap(compileMap());
    }

    private Map<String,List<String>> compileMap() {
        List<Mem> allMems = memDao.findAll();
        Map<String, List<String>>  wordName = new HashMap<>();
        List<String> listOfOne;
        boolean foundFlag;
        String name;
        String keyWord;
        for(Mem mem : allMems) {
            name = mem.getName();
            keyWord = mem.getKeyWord();
            foundFlag = false;
            if(wordName.containsKey(keyWord)){
                wordName.get(keyWord).add(name);
                foundFlag = true;
            } else if(dictionaryService.findSynonyms(keyWord) != null) {
                for(String synonym : dictionaryService.findSynonyms(keyWord)){
                    if(!foundFlag && wordName.containsKey(synonym)){
                        wordName.get(synonym).add(name);
                        foundFlag = true;
                    }
                }
            }
            if (!foundFlag){
                listOfOne = new ArrayList<>();
                listOfOne.add(name);
                wordName.put(keyWord, listOfOne);
            }
        }
        return wordName;
    }
}
