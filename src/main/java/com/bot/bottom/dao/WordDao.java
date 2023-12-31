package com.bot.bottom.dao;

import com.bot.bottom.model.Word;

import java.util.List;
import java.util.Set;

public interface WordDao {
    void importDB(List<Word> db);
    List<Word> findAll();
    void addWord(Word word);
    void addWord(String word);
    void addSynonyms(String word, Set<String> synonym);
    void addWordsLike(String word, Set<String> wordLike);

    String makeSynonym(String word1, String word2);
    void makeWordLike(String word, String wordLike);
    Word findWord(String word);
    Set<String> findSynonyms(String word);
    void deleteAll();

}
