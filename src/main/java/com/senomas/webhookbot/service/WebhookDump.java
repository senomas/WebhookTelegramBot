package com.senomas.webhookbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.senomas.webhookbot.model.MessageRepository;
import com.senomas.webhookbot.model.TopicRepository;

@Component("dump")
public class WebhookDump implements WebhookService {
	Logger log = LoggerFactory.getLogger(WebhookDump.class);

	@Autowired
	MessageRepository repo;

	@Autowired
	TopicRepository topicRepo;

	@Autowired
	BotService bot;

	@Override
	public Long process(String oriHook, JsonNode payload) {
		try {
			ObjectMapper om = new ObjectMapper();
			om.enable(SerializationFeature.INDENT_OUTPUT);
			
			log.info("DUMP {}\n{}", oriHook, om.writeValueAsString(payload));
			return 0L;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
