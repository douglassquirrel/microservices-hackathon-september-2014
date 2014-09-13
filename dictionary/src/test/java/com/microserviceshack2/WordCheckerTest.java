package com.microserviceshack2;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class WordCheckerTest {

    private WordChecker wordChecker;

    @Before
    public void setup()
    {
        wordChecker = new WordChecker();
    }

    @Test
    public void check_cat_is_a_valid_word_in_any_case()
    {
        assertFalse(wordChecker.isInvalid("cat"));
        assertFalse(wordChecker.isInvalid("Cat"));
        assertFalse(wordChecker.isInvalid("cAt"));
        assertFalse(wordChecker.isInvalid("CAT"));
    }

    @Test
    public void check_ccc_is_not_a_valid_word()
    {

        assertTrue(wordChecker.isInvalid("ccc"));
    }

    @Test
    public void check_word_is_a_valid_word_in_any_case()
    {
        assertFalse(wordChecker.isInvalid("dog"));
        assertTrue(wordChecker.isInvalid("doggiex"));
        assertFalse(wordChecker.isInvalid("doggy"));
        assertFalse(wordChecker.isInvalid("monkey"));
        assertFalse(wordChecker.isInvalid("bonk"));
        assertTrue(wordChecker.isValid("zzz"));
    }

    @Test
    public void return_list_of_valid_word_in_string()
    {
        Map<String, Integer> validWords = wordChecker.getValidWords("fishpies");
        assertFalse(validWords.isEmpty());
        outputValidWords(validWords);
    }

    private void outputValidWords(Map<String, Integer> validWords) {
        for (String word : validWords.keySet())
        {
            System.out.println(word + " (" + validWords.get(word) + ")");
        }
    }

    @Test
    public void return_all_sub_strings()
    {
        assertEquals(6, wordChecker.getSubStrings("cat").size());
    }

    @Test
    public void return_line_in_file()
    {
        assertEquals(178691, wordChecker.countWordsInFile());
    }
}
