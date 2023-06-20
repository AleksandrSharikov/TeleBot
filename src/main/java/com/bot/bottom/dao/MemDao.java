package com.bot.bottom.dao;

import com.bot.bottom.model.Mem;

import java.util.List;
import java.util.Optional;

public interface MemDao {
    Mem save(Mem mem);
    void setKeyWord(String name, String keyWord);
    List<Mem> findAll();
    Optional<Mem> findByName(String name);
    List<Mem> findByKeyword(String keyword);
    List<Mem> findBySecondWord(String word);
    void delete(Mem mem);
    void deleteAll();


}
