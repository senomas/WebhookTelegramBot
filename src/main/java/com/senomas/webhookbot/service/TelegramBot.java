package com.senomas.webhookbot.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import com.senomas.webhookbot.model.Subscriber;
import com.senomas.webhookbot.model.SubscriberRepository;
import com.senomas.webhookbot.model.Topic;
import com.senomas.webhookbot.model.TopicRepository;

@Component
public class TelegramBot extends TelegramLongPollingBot implements BotService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	Environment env;

	@Autowired
	TopicRepository topicRepo;

	@Autowired
	SubscriberRepository subscriberRepo;

	String token;

	String userName;

	String state = null;
	String stateUser = null;

	Map<String, Object> map = new HashMap<>();

	@PostConstruct
	public void init() throws TelegramApiRequestException {
		log.info("Starting");

		userName = env.getProperty("bot.username");
		token = env.getProperty("bot.token");

		TelegramBotsApi botsApi = new TelegramBotsApi();

		botsApi.registerBot(this);
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public String getBotUsername() {
		return userName;
	}

	public void onUpdateReceived(Update update) {
		// log.info("UPDATE RECEIVE");
		// log.info("UPDATE RECEIVE hasCallbackQuery: " +
		// update.hasCallbackQuery());
		// log.info("UPDATE RECEIVE hasChannelPost: " +
		// update.hasChannelPost());
		// log.info("UPDATE RECEIVE hasChosenInlineQuery: " +
		// update.hasChosenInlineQuery());
		// log.info("UPDATE RECEIVE hasEditedChannelPost: " +
		// update.hasEditedChannelPost());
		// log.info("UPDATE RECEIVE hasEditedMessage: " +
		// update.hasEditedMessage());
		// log.info("UPDATE RECEIVE hasInlineQuery: " +
		// update.hasInlineQuery());
		// log.info("UPDATE RECEIVE hasMessage: " + update.hasMessage());
		if (update.hasMessage()) {
			Message message = update.getMessage();
			Long chatId = message.getChatId();
			// log.info("MESSAGE RECEIVE getChatId: " + chatId);
			// log.info("MESSAGE RECEIVE hasDocument: " +
			// message.hasDocument());
			// log.info("MESSAGE RECEIVE hasEntities: " +
			// message.hasEntities());
			// log.info("MESSAGE RECEIVE hasLocation: " +
			// message.hasLocation());
			// log.info("MESSAGE RECEIVE hasPhoto: " + message.hasPhoto());
			// log.info("MESSAGE RECEIVE hasText: " + message.hasText());
			if (update.getMessage().hasText()) {
				String mtxt = message.getText().trim();
				String muser = message.getFrom().getUserName();
				if (mtxt.endsWith("@SenomasBot")) mtxt = mtxt.substring(0, mtxt.length()-11).trim();
				log.info("MESSAGE RECEIVE text: [" + mtxt + "] from ["+muser+"] ["+message.getChat().isGroupChat()+"]");
				// cmds.put("subscribe",
				// Pattern.compile("/subscribe\\s+([^\\s]+)\\s+([^\\s]+)\\s*"));
				// cmds.put("unsub",
				// Pattern.compile("/unsub\\s+([^\\s]+)\\s*"));
				// cmds.put("list", Pattern.compile("/list\\s*"));
				// cmds.put("topic", Pattern.compile("/topic\\s*"));

				SendMessage sm = null;
				if ("/topic".equals(mtxt)) {
					StringBuilder sb = new StringBuilder();
					for (Topic topic : topicRepo.findAll()) {
						if (sb.length() > 0)
							sb.append('\n');
						sb.append(topic.getUrl());
					}
					sb.insert(0, "Topics:\n");
					sm = new SendMessage().setChatId(message.getChatId()).setText(sb.toString());
				} else if ("/list".equals(mtxt)) {
					StringBuilder sb = new StringBuilder();
					for (Subscriber sub : subscriberRepo.findByChatId(chatId)) {
						if (sb.length() > 0)
							sb.append('\n');
						sb.append(sub.getTopic().getUrl());
					}
					sb.insert(0, "Subscribed topics:\n");
					sm = new SendMessage().setChatId(message.getChatId()).setText(sb.toString());
				} else if ("/newtopic".equals(mtxt)) {
					map.clear();
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter the topic url:");
					state = "/newtopic/1";
					stateUser = muser;
				} else if ("/newtopic/1".equals(state) && muser.equals(stateUser)) {
					String topicUrl = mtxt;
					Topic topic = topicRepo.findByUrl(topicUrl);
					if (topic == null) {
						topic = new Topic();
						topic.setUrl(topicUrl);
						topic.setSecret(UUID.randomUUID().toString());
						topic.setTimestamp(new Date());
					} else {
						topic.setSecret(UUID.randomUUID().toString());
					}
					topicRepo.save(topic);
					sm = new SendMessage().setChatId(message.getChatId()).setText("Topic created");
					state = null;
					map.clear();
				} else if ("/deltopic".equals(mtxt)) {
					map.clear();
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter the topic url:");
					state = "/deltopic/1";
					stateUser = muser;
				} else if ("/deltopic/1".equals(state) && muser.equals(stateUser)) {
					map.put("topicUrl", mtxt);
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter secret text:");
					state = "/deltopic/2";
				} else if ("/deltopic/2".equals(state) && muser.equals(stateUser)) {
					String topicUrl = (String) map.get("topicUrl");
					String secret = mtxt;
					Topic topic = topicRepo.findByUrl(topicUrl);
					if (topic != null) {
						if (secret.equals(topic.getSecret())) {
							List<Subscriber> subs = subscriberRepo.findByTopic(topic);
							subscriberRepo.delete(subs);
							topicRepo.delete(topic);
							sm = new SendMessage().setChatId(message.getChatId()).setText("Topic deleted");
						} else {
							sm = new SendMessage().setChatId(message.getChatId())
									.setText("Invalid secret '" + secret + "'");
						}
					} else {
						sm = new SendMessage().setChatId(message.getChatId()).setText("Topic not found");
					}
					state = null;
					map.clear();
				} else if ("/subscribe".equals(mtxt)) {
					map.clear();
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter the topic url:");
					state = "/subscribe/1";
					stateUser = muser;
				} else if ("/subscribe/1".equals(state) && muser.equals(stateUser)) {
					map.put("topicUrl", mtxt);
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter secret text:");
					state = "/subscribe/2";
				} else if ("/subscribe/2".equals(state) && muser.equals(stateUser)) {
					state = null;
					String topicUrl = (String) map.get("topicUrl");
					String secret = mtxt;
					map.clear();
					Topic topic = topicRepo.findByUrl(topicUrl);
					if (topic != null) {
						if (secret.equals(topic.getSecret())) {
							Subscriber sub = subscriberRepo.findByTopicAndChatId(topic, chatId);
							if (sub == null) {
								sub = new Subscriber();
								sub.setTopic(topic);
								sub.setChatId(chatId);
								sub.setTimestamp(new Date());

								subscriberRepo.save(sub);
							}

							topic.setSecret(UUID.randomUUID().toString());
							topicRepo.save(topic);

							sm = new SendMessage().setChatId(message.getChatId()).setText("Subscribed to " + topicUrl);
						} else {
							sm = new SendMessage().setChatId(message.getChatId())
									.setText("Invalid secret '" + secret + "'");
						}
					} else {
						sm = new SendMessage().setChatId(message.getChatId()).setText("Invalid url '" + topicUrl + "'");
					}
				} else if ("/unsub".equals(mtxt)) {
					map.clear();
					sm = new SendMessage().setChatId(message.getChatId()).setText("Please enter the topic url:");
					state = "/unsub/1";
					stateUser = muser;
				} else if ("/unsub/1".equals(state) && muser.equals(stateUser)) {
					String topicUrl = mtxt;

					Topic topic = topicRepo.findByUrl(topicUrl);
					if (topic != null) {
						subscriberRepo.deleteByTopicAndChatId(topic.getId(), chatId);

						sm = new SendMessage().setChatId(message.getChatId()).setText("Unsubscribe from " + topicUrl);
					} else {
						sm = new SendMessage().setChatId(message.getChatId()).setText("Invalid url '" + topicUrl + "'");
					}
				}
				if (sm == null) {
					sm = new SendMessage().setChatId(message.getChatId()).setText("Unknown: " + mtxt);
				}

				try {
					sendMessage(sm);
				} catch (TelegramApiException e) {
					log.warn(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void broadcast(Topic topic, String message) {
		Set<Long> remove = new HashSet<>();
		for (Subscriber sub : subscriberRepo.findByTopic(topic)) {
			long chatId = sub.getChatId();
			SendMessage sm = new SendMessage().setChatId(chatId).setText(message);
			try {
				sendMessage(sm);
			} catch (TelegramApiRequestException e) {
				if (e.getErrorCode() == 403) {
					log.warn("REMOVE CHAT-ID " + chatId);
					remove.add(chatId);
				} else {
					log.warn(e.getMessage(), e);
				}
			} catch (TelegramApiException e) {
				log.warn(e.getMessage(), e);
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		for (Long chatId : remove) {
			subscriberRepo.deleteByTopicAndChatId(topic.getId(), chatId);
		}
	}
}
