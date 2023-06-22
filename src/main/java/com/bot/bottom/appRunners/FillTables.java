package com.bot.bottom.appRunners;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.dao.UserDao;
import com.bot.bottom.model.Mem;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class FillTables implements ApplicationRunner {
    private final MemDao memDao;
    private final UserDao userDao;

    public FillTables(MemDao memDao, UserDao userDao) {
        this.memDao = memDao;

        this.userDao = userDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // memDao.deleteAll();
      //  userDao.deleteAll();
      //  createTestTable();
    }
    void createTestTable() {
        System.out.println("Data creation started...");

        memDao.save(new Mem("таблица", "data/userDoc/831553627_notTitle.jpg", "видимость"));

        memDao.save(new Mem("таблица", "data/userDoc/831553628_notTitle.jpg","угол"));

        memDao.save(new Mem("портрет", "data/userDoc/831553626_notTitle.jpg","рыло"));

        memDao.save(new Mem("видео", "data/userDoc/831553650_notTitle.mp4","кружок"));

        System.out.println("Data creation complete...");
    }

}
