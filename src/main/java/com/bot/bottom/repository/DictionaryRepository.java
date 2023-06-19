package com.bot.bottom.repository;

import com.bot.bottom.model.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DictionaryRepository extends MongoRepository<Word, String> {

    @Query("{word : ?0}")
    @Update("{$push : { synonyms : {$each : ?1} }}")
    void addSynonyms(String word, Set<String> synonyms);

    @Query("{word : ?0}")
    @Update("{$push : { wordsLike : {$each : ?1 } }}")
    void addWordsLike(String word, Set<String> wordsLike);
    @Query("{word : ?0}")
    @Update("{'$push' : { 'synonyms' : ?1 }}")
    void addSynonym(String word, String synonym);

    @Query("{word : ?0}")
    @Update("{$push : { wordsLike : ?1 }}")
    void addWordLike(String word, String wordLike);

}
