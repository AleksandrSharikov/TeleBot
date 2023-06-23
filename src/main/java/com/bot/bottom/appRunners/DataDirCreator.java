package com.bot.bottom.appRunners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DataDirCreator implements ApplicationRunner {

private final List<String> addresses = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) {
        addresses.add("./data/");
        addresses.add("./data/photoBase/");
        addresses.add("./data/dbLog/");

        for (String address : addresses) {
            File dirToCheck = new File(address);
            if (!dirToCheck.exists()){
                if( dirToCheck.mkdirs()){
                log.info("Directory " + address + " created");
                } else {
                    log.warn(" Can't create directory " + address);
                }
            } else {
                log.info("Directory " + address + " existed");
            }

        }
    }
}