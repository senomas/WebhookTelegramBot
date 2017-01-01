package com.senomas.webhookbot.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senomas.webhookbot.model.Message;
import com.senomas.webhookbot.model.MessageRepository;
import com.senomas.webhookbot.model.Topic;
import com.senomas.webhookbot.model.TopicRepository;

@Component("transmission")
public class WebhookTransmission implements WebhookService {

	@Autowired
	MessageRepository repo;

	@Autowired
	TopicRepository topicRepo;

	@Autowired
	BotService bot;

	@Override
	public Long process(String hook, JsonNode payload) {
		try {
			ObjectMapper om = new ObjectMapper();
			
			Message msg = new Message();
			msg.setHookType("transmission");
			msg.setHook(hook);
			msg.setData(om.writeValueAsString(payload));
			msg.setTimestamp(new Date());
			msg = repo.save(msg);

			String topicUrl = "https://senomas.com/transmission/"+hook;
			Topic topic = topicRepo.findByUrl(topicUrl);
			if (topic == null) {
				topic = new Topic();
				topic.setUrl(topicUrl);
				topic.setSecret(UUID.randomUUID().toString());
				topic.setTimestamp(new Date());

				topicRepo.save(topic);
			}

			bot.broadcast(topic, "Torrent finished "+hook+" "+payload.get("TR_TORRENT_NAME").asText());

			return msg.getId();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
