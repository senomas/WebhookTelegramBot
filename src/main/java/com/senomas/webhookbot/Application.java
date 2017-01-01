package com.senomas.webhookbot;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

import com.senomas.boot.security.SenomasEncryptor;

@Configuration
@SpringBootApplication
@ComponentScan({ "com.senomas.boot", "com.senomas.webhookbot", "com.senomas.common.loggerfilter" })
@EnableScheduling
public class Application {
	private final static Logger log = LoggerFactory.getLogger(Application.class);

	@Bean(name="jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        return new StringEncryptor() {
			
			@Override
			public String encrypt(String message) {
				return SenomasEncryptor.encrypt(message);
			}
			
			@Override
			public String decrypt(String encryptedMessage) {
				return SenomasEncryptor.decrypt(encryptedMessage);
			}
		};
    }
	
	public static void main(String[] args) {
		try {
			log.info("CHANGE-LOG:\n{}",
					IOUtils.toString(Application.class.getResourceAsStream("/META-INF/changelog.txt"), "UTF-8"));
		} catch (IOException e) {
			log.warn("Error reading changelog {}", e.getMessage(), e);
		}
		String px[] = System.getProperty("spring.profiles.active", "").split(",");
		if (px.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0, il = px.length; i < il; i++) {
				if (i > 0)
					sb.append(',');
				sb.append(px[i]);
				if ("dev".equals(px[i])) {
					sb.append(",dev-").append(System.getProperty("user.name"));
				}
			}
			System.setProperty("spring.profiles.active", sb.toString());
		}
		ApiContextInitializer.init();

		SpringApplication.run(Application.class, args);
	}

}
