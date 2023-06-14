package com.bot.bottom.test;

import com.bot.bottom.repository.MemRepository;
import com.bot.bottom.model.Mem;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class FillTables implements ApplicationRunner {
    private final MemRepository memRepository;

    public FillTables(MemRepository memRepository) {
        this.memRepository = memRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestTable();

    }
    void createTestTable() {
        System.out.println("Data creation started...");

        memRepository.save(new Mem("таблица", "data/userDoc/831553627_notTitle.jpg",
                "видимость"));

        memRepository.save(new Mem("таблица", "data/userDoc/831553628_notTitle.jpg",
                "угол"));

        memRepository.save(new Mem("портрет", "data/userDoc/831553626_notTitle.jpg",
                "рыло"));

        memRepository.save(new Mem("видео", "data/userDoc/831553650_notTitle.mp4",
                "кружок"));

        System.out.println("Data creation complete...");
    }
}
