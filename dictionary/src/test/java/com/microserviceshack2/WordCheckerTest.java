package com.microserviceshack2;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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
    }

}
