package com.microserviceshack2;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by danielvaughan on 07/09/2014.
 */
public class WordChecker {

    private static final WordChecker INSTANCE = new WordChecker();

    public static final String WORD_LIST = "/word-list.txt";
    private BloomFilter<String> wordFilter;
    private Set<String> allWords;

    public static WordChecker getInstance() { return INSTANCE; }

    private WordChecker() {
        loadWords();
    }

    public boolean isInvalid(String word) {
        String cleanedWord = getCleanedWord(word);
        return !wordFilter.mightContain(cleanedWord);
    }

    private String getCleanedWord(String word) {
        return word.toUpperCase();
    }

    public boolean isValid(String word) {
        if (!isInvalid(word)) {
            String cleanedWord = getCleanedWord(word);
            return allWords.contains(cleanedWord);
        }
        return false;
    }

    private void setupWord(String word) {
        wordFilter.put(word);
        allWords.add(word);
    }

    int countWordsInFile() {
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(WORD_LIST));
        LineNumberReader lnr = new LineNumberReader(reader);
        try {

            lnr.skip(Long.MAX_VALUE);
            return lnr.getLineNumber()+1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                lnr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadWords() {
        wordFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), countWordsInFile(), 0.01);
        allWords = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(this.getClass().getResource(WORD_LIST).toURI()), Charset.defaultCharset())) {
            stream.forEach(this::setupWord);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    Map<String, Integer> getSubStrings(String string) {
        Map<String, Integer> subStrings = new LinkedHashMap<>();
        for (int from = 0; from < string.length(); from++) {
            for (int to = from + 1; to <= string.length(); to++) {
                subStrings.put(string.substring(from, to), from);
            }
        }
        return subStrings;
    }

    public Map<String, Integer> getValidWords(String string) {
        Map<String, Integer> validWords = new LinkedHashMap<>();
        Map<String, Integer> candidateWordsMap = getSubStrings(string);
        candidateWordsMap.keySet().stream().filter(candidateWord -> isValid(candidateWord)).forEach(candidateWord -> {
            validWords.put(candidateWord, candidateWordsMap.get(candidateWord));
        });
        return validWords;
    }
}
