package com.bot.bottom.repository;

import com.bot.bottom.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    @Query(value = "{userId: ?0} push {posted: ?1}")
    void updateUserPostedById(long id, String posted);

    @Query(value = "{userId: ?0} push {oldNames: name} update {name: ?2}")
    void changeName(long id, String newName);
}
