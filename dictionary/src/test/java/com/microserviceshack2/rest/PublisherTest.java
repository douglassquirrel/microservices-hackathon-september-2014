package com.microserviceshack2.rest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PublisherTest {

	@Test
	public void testPostTopicWithBody() {
		//given
		String url = "http://54.76.117.95:8080";
		String path = "topics";
		String topicName = "words_found";
		

		List<Word> words = new ArrayList<Word>();
		words.add(new Word("testWord1", 1, 2, Direction.V));
		words.add(new Word("testWord2", 5, 1, Direction.H));
		
		String id = "1";
		WordsFound wordsFound = new WordsFound(words, id);
		//when
		int response = new Connection().postTopicWithBody(url, path,topicName, wordsFound);
		
		//then
		assertEquals(202, response);
	}

}
