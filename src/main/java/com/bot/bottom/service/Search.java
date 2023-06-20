package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.model.Mem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class Search {
    private final MemDao memDao;
    private final DictionaryService dictionaryService;

    public Search(MemDao memDao, DictionaryService dictionaryService) {
        this.memDao = memDao;
        this.dictionaryService = dictionaryService;
    }

    public List<String> search(String toFind){
        toFind = toFind.toLowerCase();
        List<String> answer;
        log.info("Looking for "+ toFind);
        if(toFind.equals("all")){
            return memDao.findAll().stream().
                    map(Mem::getAddress).toList();
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
}
