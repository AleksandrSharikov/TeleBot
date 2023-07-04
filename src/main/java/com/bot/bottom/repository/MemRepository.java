package com.bot.bottom.repository;

import com.bot.bottom.model.Mem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemRepository extends MongoRepository<Mem, String> {
    @Query(value = "{'keyWord' : ?0}")
    List<Mem> getAllAddresses(String keyWord);

    @Query("{secondWords : ?0}")
    List<Mem> getAllAddressesBySecondWords(String secondWord);
    @Query("{'name' : ?0}")
    @Update("{$set :  {'keyWord' :  ?1}}")
    void setKeyword(String name, String keyWord);
    Mem findMemByAddress(String address);

    @Query("{'name' : ?0}")
    @Update("{ '$push' : { 'secondWords' : ?1 } }")
    void addSecondWord(String name, String secondWord);
}
