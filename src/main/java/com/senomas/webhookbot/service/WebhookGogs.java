package com.senomas.webhookbot.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.senomas.webhookbot.model.Message;
import com.senomas.webhookbot.model.MessageRepository;
import com.senomas.webhookbot.model.Topic;
import com.senomas.webhookbot.model.TopicRepository;

@Component("gogs")
public class WebhookGogs implements WebhookService {

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
			
			String hook = payload.get("repository").get("name").asText();

			Message msg = new Message();
			msg.setHookType("gogs");
			msg.setHook(hook);
			msg.setData(om.writeValueAsString(payload));
			msg.setTimestamp(new Date());
			msg = repo.save(msg);

			String topicUrl = payload.get("repository").get("html_url").asText();

			Topic topic = topicRepo.findByUrl(topicUrl);
			if (topic == null) {
				topic = new Topic();
				topic.setUrl(topicUrl);
				topic.setSecret(UUID.randomUUID().toString());
				topic.setTimestamp(new Date());

				topicRepo.save(topic);
			}

			StringBuilder sb = new StringBuilder();
			sb.append(payload.get("pusher").get("username").asText()).append(" push to ")
					.append(payload.get("repository").get("clone_url").asText());

			for (JsonNode com : (ArrayNode) payload.get("commits")) {
				sb.append("\n\n").append(com.get("timestamp").asText()).append(' ')
						.append(com.get("committer").get("name").asText()).append(' ').append(com.get("id").asText())
						.append(' ').append(com.get("message").asText());
			}
			bot.broadcast(topic, sb.toString());

			return msg.getId();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
