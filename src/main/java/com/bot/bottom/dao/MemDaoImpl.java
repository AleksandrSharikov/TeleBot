package com.bot.bottom.dao;

import com.bot.bottom.model.Mem;
import com.bot.bottom.repository.MemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemDaoImpl implements MemDao{
    private final MemRepository memRepository;

    public MemDaoImpl(MemRepository memRepository) {
        this.memRepository = memRepository;
    }

    @Override
    public Mem save(Mem mem){
        if(memRepository.findById(mem.getName()).isEmpty()){
        memRepository.save(mem);} else {
            String name = mem.getName();
            int i = 1;
            while(!memRepository.findById(name + "_" + i).isEmpty()){
                i++;
            }
            mem.setName(name + "_" + i);
            memRepository.save(mem);
        }
        return mem;
    }

    @Override
    public List<Mem> findAll() {
        return memRepository.findAll();
    }

    @Override
    public Optional<Mem> findByName(String name) {
        return memRepository.findById(name);
    }

    @Override
    public List<Mem> findByKeyword(String keyword) {
        return memRepository.getAllAddresses(keyword);
    }

    @Override
    public void deleteAll() {
        memRepository.deleteAll();
    }

}
