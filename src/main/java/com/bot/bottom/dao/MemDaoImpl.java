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
            while(memRepository.findById(name + "_" + i).isPresent()){
                i++;
            }
            mem.setName(name + "_" + i);
            memRepository.save(mem);
        }
        return mem;
    }

    @Override
    public void importDB(List<Mem> db) {
        memRepository.saveAll(db);
    }

    @Override
    public void setKeyWord(String name, String keyWord) {
        memRepository.setKeyword(name,keyWord);
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
    public List<Mem> findByKeyword(String keyword) { return memRepository.getAllAddresses(keyword); }

    @Override
    public List<Mem> findBySecondWord(String word) {
        return memRepository.getAllAddressesBySecondWords(word);
    }

    @Override
    public Mem findDyAddress(String address) {
        return memRepository.findMemByAddress(address);
    }

    @Override
    public void delete(Mem mem) {
        memRepository.delete(mem);
    }

    @Override
    public void deleteAll() {
        memRepository.deleteAll();
    }

}
