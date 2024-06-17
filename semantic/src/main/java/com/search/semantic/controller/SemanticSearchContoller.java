package com.search.semantic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.search.semantic.model.request.AIChatRequest;
import com.search.semantic.model.response.Product;
import com.search.semantic.services.InternalSearchService;
import com.search.semantic.services.LLMChatService;

@RestController
@RequestMapping("api/semantic")
public class SemanticSearchContoller {

	private InternalSearchService internalSearchService;
	private LLMChatService llmChatService;

	public SemanticSearchContoller(InternalSearchService internalSearchService, LLMChatService llmchatService) {
		this.internalSearchService = internalSearchService;
		this.llmChatService = llmchatService;
	}

	@PostMapping("/search")
	public List<Product> getSearchResults(@RequestBody AIChatRequest request) throws JsonProcessingException {
		return llmChatService.getLLMResposeOnFAQResult(request.query());
	}

}
