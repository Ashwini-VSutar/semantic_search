package com.search.semantic.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.search.semantic.model.request.AIChatRequest;
import com.search.semantic.model.request.FAQ;

@Service
public class InternalSearchService {

	private static final double SIMILARITY_THRESHOLD = 0.8;
	private DataLoaderService dataLoaderService;
	private final LLMChatService llmChatService;

	public InternalSearchService(LLMChatService llmChatService, DataLoaderService dataLoaderService) {
		this.llmChatService = llmChatService;
		this.dataLoaderService = dataLoaderService;
	}

	public String searchFAQUsingInternalSearch(AIChatRequest request) throws JsonProcessingException {
        String result = "";
		List<Double> embeddingForPrompt = llmChatService.getEmbeddings(request.query());
		List<FAQ> faqList = dataLoaderService.getFAQEmbeedings();

		List<List<Double>> faqEmbeddings = new ArrayList<>();
		for (FAQ faq : faqList) {
			faqEmbeddings.add(faq.getEmbedding());
		}

		List<Integer> mostSimilarIndices = findMostSimilarEmbeddings(embeddingForPrompt, faqEmbeddings, 1);

		List<FAQ> topFAQs = new ArrayList<>();
		for (int index : mostSimilarIndices) {
			FAQ faq = new FAQ();
			FAQ faqresult = faqList.get(index);
			faq.setQuestion(faqresult.getQuestion());
			faq.setAnswer(faqresult.getAnswer());
			topFAQs.add(faq);
		}
		
		if(!CollectionUtils.isEmpty(topFAQs)) {
			result = topFAQs.get(0).getAnswer();
		}
		return result;
		//return llmChatService.getLLMResposeOnFAQResult(request.query(), result);
	}

	private List<Integer> findMostSimilarEmbeddings(List<Double> embeddingForPrompt, List<List<Double>> faqEmbeddings,
			int topResults) {
		List<Double> similarities = new ArrayList<>();

		for (List<Double> faqEmbedding : faqEmbeddings) {
			double similarity = cosineSimilarity(embeddingForPrompt, faqEmbedding);
			similarities.add(similarity);
		}

		List<Integer> mostSimilarIndices = new ArrayList<>();
		for (int i = 0; i < faqEmbeddings.size(); i++) {

			// Only consider indices with similarity above the threshold
			if (similarities.get(i) >= SIMILARITY_THRESHOLD)
				mostSimilarIndices.add(i);
		}

		mostSimilarIndices.sort(Comparator.comparingDouble(similarities::get).reversed());

		return mostSimilarIndices.subList(0, Math.min(topResults, mostSimilarIndices.size()));
	}

	public static double cosineSimilarity(List<Double> vectorX, List<Double> vectorY) {
		if (vectorX == null || vectorY == null) {
			throw new RuntimeException("Vectors must not be null");
		}
		if (vectorX.size() != vectorY.size()) {
			throw new IllegalArgumentException("Vectors lengths must be equal");
		}

		double dotProduct = dotProduct(vectorX, vectorY);
		double normX = norm(vectorX);
		double normY = norm(vectorY);

		if (normX == 0 || normY == 0) {
			throw new IllegalArgumentException("Vectors cannot have zero norm");
		}

		return dotProduct / (Math.sqrt(normX) * Math.sqrt(normY));
	}

	public static double dotProduct(List<Double> vectorX, List<Double> vectorY) {
		if (vectorX.size() != vectorY.size()) {
			throw new IllegalArgumentException("Vectors lengths must be equal");
		}

		double result = 0;
		for (int i = 0; i < vectorX.size(); ++i) {
			result += vectorX.get(i) * vectorY.get(i);
		}

		return result;
	}

	public static double norm(List<Double> vector) {
		return dotProduct(vector, vector);
	}

}
