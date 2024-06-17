package com.search.semantic.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration(proxyBeanMethods = false)
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("classpath:products.csv")
    private Resource resource;
	@Value("${spring.ai.vectorstore.pgvector.load}")
	private boolean loadVectorEmbeddings;
	
    @Bean
    TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }

    @Bean
	ApplicationRunner runner(VectorStore vectorStore, JdbcTemplate template, TokenTextSplitter tokenTextSplitter) {
		return args -> {
			
			/*
			 * ExtractedTextFormatter textFormatter = ExtractedTextFormatter.builder()
			 * .withNumberOfBottomTextLinesToDelete(3).
			 * withNumberOfTopPagesToSkipBeforeDelete(1).build(); TikaDocumentReader
			 * documentReader = new TikaDocumentReader(resource, textFormatter);
			 * 
			 * template.update("delete from vector_store");
			 * vectorStore.accept(tokenTextSplitter.apply(documentReader.get()));
			 */
			
			if(loadVectorEmbeddings) {
				List<Document> listOfDocs = new ArrayList<>();
				log.info("Loading file(s) as Documents");
				try (Stream<String> lines = Files.lines(Paths.get(resource.getURI()))) {
					lines.forEach(line -> {
						listOfDocs.add(new Document(line));
					});
				}
				vectorStore.accept(listOfDocs);
				log.info("Loaded document to database.");
			}
		    
		};
		
		
	}
}
