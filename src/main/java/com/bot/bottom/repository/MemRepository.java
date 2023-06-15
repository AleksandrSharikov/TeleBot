package com.bot.bottom.repository;

import com.bot.bottom.model.Mem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemRepository extends MongoRepository<Mem, String> {
    @Query(value = "{keyWord :?0}")
    List<Mem> getAllAddresses(String keyWord);
}
