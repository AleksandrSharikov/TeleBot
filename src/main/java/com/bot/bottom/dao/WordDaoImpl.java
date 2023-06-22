package com.bot.bottom.dao;

import com.bot.bottom.model.Word;
import com.bot.bottom.repository.DictionaryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WordDaoImpl implements WordDao {
    private final DictionaryRepository dictionaryRepository;

    public WordDaoImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public void importDB(List<Word> db) {
        dictionaryRepository.saveAll(db);
    }

    @Override
    public List<Word> findAll() {
        return dictionaryRepository.findAll();
    }

    @Override
    public void addWord(Word word) {
        if(dictionaryRepository.findById(word.getWord()).isPresent()){
            if(word.getSynonyms() != null){
                addSynonyms(word.getWord(), word.getSynonyms());
            }
            if(word.getWordsLike() != null){
                addWordsLike(word.getWord(), word.getWordsLike());
            }
        } else {
            dictionaryRepository.save(word);
        }

    }

    @Override
    public void addWord(String word) {
        addWord(new Word(word, null, null));
    }

    @Override
    public void addSynonyms(String word, Set<String> synonyms) {
            if(dictionaryRepository.findById(word).isEmpty()){
                dictionaryRepository.save(new Word(word,null,null));
            }
            dictionaryRepository.addSynonyms(word, synonyms);
            for(String synonym : synonyms){
                  checkSynonym(word,synonym);
            }
    }

    @Override
    public void addWordsLike(String word, Set<String> wordLike) {
            dictionaryRepository.addWordsLike(word, wordLike);
    }

    @Override
    public String makeSynonym(String word, String synonym) {
        StringBuilder answer = new StringBuilder();
        if (dictionaryRepository.findById(word).isEmpty()) {
            dictionaryRepository.save(new Word(word, null, null));
        }
        if (dictionaryRepository.findById(word).get().getSynonyms() != null 
                && dictionaryRepository.findById(word).get().getSynonyms().contains(synonym)){
            return "always has been";
        }
            dictionaryRepository.addSynonym(word, synonym);
           checkSynonym(word,synonym);
           answer.append(word);
           answer.append(" = \n");
           for (String syn : dictionaryRepository.findById(word).get().getSynonyms()){
               answer.append(syn);
               answer.append('\n');
           }
           return answer.toString();
    }

    @Override
    public void makeWordLike(String word, String wordLike) {

    }

    @Override
    public Word findWord(String word) {
        return dictionaryRepository.findById(word).orElse(null);
    }

    @Override
    public Set<String> findSynonyms(String word) {
        if(dictionaryRepository.findById(word).isPresent()){
            return dictionaryRepository.findById(word).get().getSynonyms();
        }
        return null;
    }

    @Override
    public void deleteAll() { dictionaryRepository.deleteAll(); }

    private void checkSynonym(String word, String synonym){
        if (dictionaryRepository.findById(synonym).isPresent()) {
            if (dictionaryRepository.findById(synonym).get().getSynonyms() == null
                    || !dictionaryRepository.findById(synonym).get().getSynonyms().contains(word)) {
                dictionaryRepository.addSynonym(synonym, word);
            }
        } else {
            Set<String> setOfOne = new HashSet<>();
            setOfOne.add(word);
            dictionaryRepository.save(new Word(synonym, setOfOne, null));
        }
    }


}
