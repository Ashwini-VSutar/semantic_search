package com.search.semantic.model.request;

import java.util.List;

public class FAQ {
	String question;
	String answer;
	List<Double> embedding;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public List<Double>  getEmbedding() {
		return embedding;
	}
	public void setEmbedding(List<Double> embedding) {
		this.embedding = embedding;
	}
}
