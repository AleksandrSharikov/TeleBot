package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.repository.MemRepository;
import com.bot.bottom.model.Mem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Search {
    private final MemDao memDao;

    public Search(MemDao memDao) {
        this.memDao = memDao;
    }

    public List<String> search(String toFind){
        log.info("Looking for "+ toFind);
        if(toFind.equalsIgnoreCase("all")){


            return memDao.findAll().stream().
                    map(Mem::getAddress).toList();
        }
        return null;
    }
}
