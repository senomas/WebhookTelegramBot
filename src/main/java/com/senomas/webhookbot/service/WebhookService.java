package com.senomas.webhookbot.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface WebhookService {
	
	Long process(String hook, JsonNode payload);

}
