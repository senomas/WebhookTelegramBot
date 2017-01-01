package com.senomas.webhookbot.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

public interface TopicRepository extends JpaRepository<Topic, Long> {
	
	Topic findByUrl(String url);

	@Modifying
	@Transactional
	@RestResource(exported = false)
	@Query("delete from Topic t where t.url = ?1")
	void deleteByUrl(String url);

	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(Topic gogs);
}
