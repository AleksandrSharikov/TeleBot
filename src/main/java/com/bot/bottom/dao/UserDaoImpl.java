package com.bot.bottom.dao;

import com.bot.bottom.model.User;
import com.bot.bottom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addPosted(long userId, String post) {
        userRepository.updateUserPostedById(userId, post);
    }

    @Override
    public void addSeen(long userId, List<String> seen) {

    }

    @Override
    public User updateUser(long id, String name) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            user = new User( id, name);
            userRepository.save(user);
            return user;
        }
        if (user.getName().equals(name)) {
            return user;
        }
        userRepository.changeName(id, name);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
