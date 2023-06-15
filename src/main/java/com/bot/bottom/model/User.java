package com.bot.bottom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document("Users")
public class User {
    @Id
    private long userId;
    private String name;
    private Set<String> seen;
    private Set<String> posted;
    private List<String> oldNames;

    public User(long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    @PersistenceCreator
    public User(long userId, String name, Set<String> seen, Set<String> posted, List<String> oldNames) {
        super();
        this.userId = userId;
        this.name = name;
        this.seen = seen;
        this.posted = posted;
        this.oldNames = oldNames;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getSeen() {
        return seen;
    }

    public void setSeen(Set<String> seen) {
        this.seen = seen;
    }

    public Set<String> getPosted() {
        return posted;
    }

    public void setPosted(Set<String> posted) {
        this.posted = posted;
    }

    public List<String> getOldNames() {
        return oldNames;
    }

    public void setOldNames(List<String> oldNames) {
        this.oldNames = oldNames;
    }
}
