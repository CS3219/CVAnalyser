package com.parser;

import java.util.ArrayList;

public class parsedObject {

	private String type;
	private ArrayList<String> words;
	
	public parsedObject() {
		words = new ArrayList<String>();
	}
	
	public String getType() {
		return type;
	}
	
	public ArrayList<String> getWords() {
		return words;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
}
