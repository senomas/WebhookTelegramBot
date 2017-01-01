package com.senomas.webhookbot.service;

import org.springframework.stereotype.Component;

import com.senomas.boot.security.domain.SecurityUser;
import com.senomas.boot.security.service.AuthUserService;

@Component
public class AuthUserServiceImpl implements AuthUserService {

	@Override
	public SecurityUser findByLogin(String username) {
		return null;
	}

	@Override
	public SecurityUser save(SecurityUser user) {
		throw new RuntimeException("NOT SUPPORTED");
	}

}
