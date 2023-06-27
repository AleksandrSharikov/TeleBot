package com.bot.bottom.service;

import com.bot.bottom.dao.UserDao;
import com.bot.bottom.model.Mem;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UserService {
    private final UserDao userDao;
    private long userId;
    private String name;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void getUserInfo(Update update) {
        name = update.getMessage().getFrom().getUserName() == null ?
                update.getMessage().getFrom().getFirstName() :
                update.getMessage().getFrom().getUserName();
        userId = update.getMessage().getFrom().getId();
        userDao.updateUser(userId, name);
    }

    public void postMem(Mem mem) {
        userDao.addPosted(userId, mem.getName());
    }


}
