package com.search.semantic.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.semantic.model.request.FAQ;

@Service
public class DataLoaderService {

	// @Value("classpath:providedFAQs.json")
	 private Resource faqEmbeddingsResource;
	 
	 private final LLMChatService embeddingClientService;
	 private final ObjectMapper objectMapper;
	 
	 public DataLoaderService(LLMChatService embeddingClientService, ObjectMapper objectMapper) {
		this.embeddingClientService = embeddingClientService;
		this.objectMapper = objectMapper;
	 }
	 
	  private final List<FAQ> faqList = new ArrayList<>();
	  
		/*
		 * @PostConstruct private void init() throws JsonProcessingException {
		 * faqList.addAll(loadFAQsFromJsonFile()); generateEmbeddingsJson(); }
		 */
	 private List<FAQ> loadFAQsFromJsonFile() {

	        try {
	            InputStream inputStream = faqEmbeddingsResource.getInputStream();
	            FAQ[] faqs = objectMapper.readValue(inputStream, FAQ[].class);

	            return List.of(faqs);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ArrayList<>();
	        }
	    }
	 
	 private void generateEmbeddingsJson() throws JsonProcessingException {

	        for (FAQ faq : faqList) {
	            List<Double> embeddingsAsList = embeddingClientService.getEmbeddings(faq.getQuestion());
	            faq.setEmbedding(embeddingsAsList);
	        }
	 }
	 
	 public List<FAQ> getFAQEmbeedings() {
		 return faqList;
		 
	 }
	 
}
