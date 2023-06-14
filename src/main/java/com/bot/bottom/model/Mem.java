package com.bot.bottom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("Mems")
public class Mem {
    private String keyWord;
    private String address;

    @Id
    private String name;
    private List<String> secondWords;
    private int type;
    private LocalDateTime saveTime;

    @PersistenceCreator
    public Mem(String keyWord, String address, String name, List<String> secondWords, int type, LocalDateTime saveTime) {
       // super();
        this.keyWord = keyWord;
        this.address = address;
        this.name = name;
        this.secondWords = secondWords;
        this.type = type;
        this.saveTime = saveTime;
    }

    public Mem(String keyWord, String address, String name) {
        this.keyWord = keyWord;
        this.address = address;
        this.name = name;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(LocalDateTime saveTime) {
        this.saveTime = saveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSecondWords() {
        return secondWords;
    }

    public void setSecondWords(List<String> secondWords) {
        this.secondWords = secondWords;
    }

    @Override
    public String toString() {
        return "Mem{" +
                "keyWord='" + keyWord + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", secondWords=" + secondWords +
                ", type=" + type +
                ", saveTime=" + saveTime +
                '}';
    }
}
