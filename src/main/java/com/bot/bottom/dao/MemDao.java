package com.bot.bottom.dao;

import com.bot.bottom.model.Mem;

import java.util.List;

public interface MemDao {
    void save(Mem mem);
    List<Mem> findAll();
}
