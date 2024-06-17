package com.search.semantic.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.search.semantic.model.response.Product;

@Service
public class LLMChatService {
	private final EmbeddingClient embeddingClient;
	@Value("${spring.ai.vectorstore.pgvector.topk}")
	private int topk;
	@Value("${spring.ai.openai.chat.options.temperature}")
	private double similarityThreshold;
	
	private final ChatClient chatClient;
	private final VectorStore vectorStore;
	private static final String template = """
			You're assisting about Solar Panel Installation

			Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
			If unsure, simply state that you don't know.

			DOCUMENTS:
			{documents}

			""";

	public LLMChatService(EmbeddingClient embeddingClient, ChatClient chatClient, VectorStore vectorStore) {
		this.embeddingClient = embeddingClient;
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	public List<Double> getEmbeddings(String query) {
		return embeddingClient.embed(query);
	}

	public List<Product> getLLMResposeOnFAQResult(String request) throws JsonProcessingException {

        // Querying the VectorStore using natural language looking for the information about info asked.
        List<Document> listOfSimilarDocuments = this.vectorStore.similaritySearch(SearchRequest.query(request).withTopK(topk).withSimilarityThreshold(similarityThreshold));
        String documents = listOfSimilarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        // Constructing the systemMessage to indicate the AI model to use the passed information
        // to answer the question.
       // Message systemMessage = new SystemPromptTemplate(template).createMessage(Map.of("documents", documents));
       // UserMessage userMessage = new UserMessage(request);
       // Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        // Prompt prompt = new Prompt(userMessage);
       // ChatResponse aiResponse = chatClient.call(prompt);
       // Generation generation = aiResponse.getResult();
       // return (generation != null) ? generation.getOutput().getContent() : "";
    
        Reader reader = new StringReader(documents);
        CsvToBean<Product> products = new CsvToBeanBuilder<Product>(reader).withType(Product.class).withIgnoreLeadingWhiteSpace(true).build();
        List<Product> list =  products.parse();
        return list;
	}

}
