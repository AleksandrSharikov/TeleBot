package com.bot.bottom.repository;

import com.bot.bottom.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    @Query("{ 'userId' : ?0 }")
    @Update("{ '$push' : { 'posted' : ?1 } }")
    void updateUserPostedById(long id, String posted);

    @Query("{userId: ?0 }")
    @Update("{ '$push' : {'oldNames' :  $name}, '$set' : {'name' : ?1 } }")
    void changeName(long id, String newName);

    @Query("{userId: ?0 }")
    @Update("{ '$push' : {'oldNames' : ?1}, '$set' : {'name' : ?2 } }")
    void changeName(long id, String oldName, String newName);


}
