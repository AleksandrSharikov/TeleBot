package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.dto.MemDTO;
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

    public Search(MemDao memDao, DictionaryService dictionaryService) {
        this.memDao = memDao;
        this.dictionaryService = dictionaryService;
    }

    public Mem findMemByAddress(String address){
        return memDao.findDyAddress(address);
    }

    public List<String> search(String toFind){
        toFind = toFind.toLowerCase();
        List<String> answer;
        log.info("Looking for "+ toFind);
        if(toFind.equals("all")){
            return memDao.findAll().stream().
                    map(Mem::getAddress).toList();
        }
        if(toFind.matches("^name/.*")){
        answer = new ArrayList<>();
        toFind = toFind.substring(5).trim();
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
                memDao.findByKeyword(toFind).stream().limit(9).map(Mem::getAddress)
                        .forEach(answer::add);
            }
            if (9 - answer.size() > 1 && dictionaryService.findSynonyms(toFind) != null){
                for(String synonym : dictionaryService.findSynonyms(toFind)){
                    if (!memDao.findByKeyword(synonym).isEmpty()){
                        memDao.findByKeyword(synonym).stream().limit(9).map(Mem::getAddress)
                                .forEach(answer::add);
                    }
                }
            }
            if (9 - answer.size() > 1 && !memDao.findBySecondWord(toFind).isEmpty()){
                memDao.findBySecondWord(toFind).stream().limit(9 - answer.size()).map(Mem::getAddress)
                        .forEach(answer::add);
            }
        }
        return answer;
    }

    public MemDTO compileMap() {
        List<Mem> allMems = memDao.findAll();
        Map<String, List<String>>  wordName = new HashMap<>();
        String name;
        String keyWord;
        for(Mem mem : allMems) {
            name = mem.getName();
            keyWord = mem.getKeyWord();
            if(wordName.keySet().contains(mem.getKeyWord())){
                wordName.merge(keyWord, word, (list,word) -> list.add(mem.getKeyWord()));
            }

        }
    }
}
