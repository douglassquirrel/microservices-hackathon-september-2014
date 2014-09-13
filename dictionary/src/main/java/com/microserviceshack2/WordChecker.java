package com.microserviceshack2;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by danielvaughan on 07/09/2014.
 */
public class WordChecker {

    private BloomFilter<String> words;
    private Set<String> allWords;

    public WordChecker()
    {
        loadWords();
    }

    public boolean isInvalid(String word) {
        String cleanedWord = getCleanedWord(word);
        return !words.mightContain(cleanedWord);
    }

    private String getCleanedWord(String word) {
        return word.toUpperCase();
    }

    public boolean isValid(String word)
    {
        if (!isInvalid(word))
        {
            String cleanedWord = getCleanedWord(word);
            return allWords.contains(cleanedWord);
        }
        return false;
    }

    private void setupWord(String word)
    {
        words.put(word);
        allWords.add(word);
    }

    private void loadWords()
    {
        words = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 178691, 0.01);
        allWords = new HashSet<String>();
        try (Stream<String> stream = Files.lines(Paths.get(this.getClass().getResource("/word-list.txt").toURI()),Charset.defaultCharset())) {
            stream.forEach(this::setupWord);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
