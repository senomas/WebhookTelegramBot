package com.senomas.webhookbot.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

	Subscriber findByTopicAndChatId(Topic topic, Long chatId);

	List<Subscriber> findByTopic(Topic topic);

	List<Subscriber> findByChatId(Long chatId);

	@Modifying
	@Transactional
	@RestResource(exported = false)
	@Query("delete from Subscriber s where s.topic.id = ?1 and s.chatId = ?2")
	void deleteByTopicAndChatId(Long topicId, Long chatId);

	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(Subscriber subscriber);
}
