package com.bot.bottom.dao;

import com.bot.bottom.model.User;

import java.util.List;

public interface UserDao {
    void importDB(List<User> db);
    List<User> findAll();
    void addPosted(long userId, String post);
    void addSeen(long userId, List<String> seen);
    User updateUser(long id, String name);
    User findUserById(long id);
    void deleteAll();

}
