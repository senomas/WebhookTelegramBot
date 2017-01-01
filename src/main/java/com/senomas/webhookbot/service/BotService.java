package com.senomas.webhookbot.service;

import com.senomas.webhookbot.model.Topic;

public interface BotService {
	
	void broadcast(Topic topic, String message);

}
