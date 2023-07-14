package com.bot.bottom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bot.bottom","resources","java"})
public class BottomBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BottomBotApplication.class, args);
    }

}
