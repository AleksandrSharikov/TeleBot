package com.bot.bottom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("Dictionary")
public class Word {
    @Id
    private String word;
    private Set<String> synonyms;
    private Set<String> wordsLike;

    public Word(String word, Set<String> synonyms, Set<String> wordsLike) {
        this.word = word;
        this.synonyms = synonyms;
        this.wordsLike = wordsLike;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public Set<String> getWordsLike() {
        return wordsLike;
    }

    public void setWordsLike(Set<String> wordsLike) {
        this.wordsLike = wordsLike;
    }
}
