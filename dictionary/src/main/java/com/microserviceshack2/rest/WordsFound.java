package com.microserviceshack2.rest;

import java.util.List;

import com.google.gson.Gson;

public class WordsFound {
	private List<Word> words;
	private String id;
	public WordsFound(List<Word> words, String id) {
		this.words = words;
		this.id = id;
	}

	public String toJson() {
		Gson gson = new Gson();
		 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(this);
		
		return json;
	}
}
