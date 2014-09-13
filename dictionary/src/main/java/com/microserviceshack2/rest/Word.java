package com.microserviceshack2.rest;

public class Word {
	private String word;
	private int index;
	private int start;
	private Direction direction;
	
	public Word(String word, int index, int start, Direction direction) {
		super();
		this.word = word;
		this.index = index;
		this.start = start;
		this.direction = direction;
	}
}
