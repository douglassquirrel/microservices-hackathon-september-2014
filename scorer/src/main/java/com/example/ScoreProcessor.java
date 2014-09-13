package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class ScoreProcessor {

    private ObjectMapper mapper = new ObjectMapper();

    private LetterScorer letterScorer = new LetterScorer();

    public String process(String message) {
        try {
            InputMessage inputMessage = mapper.readValue(message, InputMessage.class);
            if (inputMessage.words.isEmpty()) {
                return null;
            }

            int maxScore = 0;
            List<Map> maxWords = new ArrayList<>();
            for (Map word : inputMessage.words) {
                int score = getScore(getWord(word));

                if (score > maxScore) {
                    maxScore = score;
                    maxWords = new ArrayList<>();
                    maxWords.add(word);

                } else if (score == maxScore) {
                    maxWords.add(word);
                }
            }

            Collections.sort(maxWords, new StringLengthComparator());

            System.out.println(inputMessage);

            OutputMessage outputMessage = new OutputMessage();
            outputMessage.id = inputMessage.id;
            outputMessage.word = maxWords.get(0);
            outputMessage.score = maxScore;

            return mapper.writeValueAsString(outputMessage);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getScore(String word) {
        int score = 0;
        for (char c : word.toUpperCase().toCharArray()) {
            score += letterScorer.score(c);
        }
        return score;
    };

    private static class StringLengthComparator implements Comparator<Map> {

        @Override
        public int compare(Map s1, Map s2) {
            return new Integer(getWord(s1).length()).compareTo(getWord(s2).length());
        }

    }

    private static String getWord(Map map) {
        return (String) map.get("word");
    }

}
