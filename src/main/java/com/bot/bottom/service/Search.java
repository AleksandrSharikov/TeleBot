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

    public Search(MemDao memDao) {
        this.memDao = memDao;
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
                memDao.findByKeyword(toFind).stream().limit(8).map(Mem::getAddress)
                        .forEach(answer::add);
            }
        }
        return answer;
    }
}
