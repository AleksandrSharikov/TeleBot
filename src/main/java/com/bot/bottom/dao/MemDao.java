package com.bot.bottom.dao;

import com.bot.bottom.model.Mem;

import java.util.List;
import java.util.Optional;

public interface MemDao {
    void save(Mem mem);
    List<Mem> findAll();
    Optional<Mem> findByName(String name);

    List<Mem> findByKeyword(String keyword);

    void deleteAll();

}
