package com.senomas.webhookbot.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.senomas.boot.security.LoginRequest;
import com.senomas.boot.security.domain.AuthToken;
import com.senomas.boot.security.domain.SecurityUser;
import com.senomas.boot.security.service.AuthenticationService;

@Controller
public class AuthenticationServiceImpl implements AuthenticationService {
	@Override
	public SecurityUser getUser() {
		return null;
	}

	@Override
	public AuthToken login(HttpServletRequest arg0, LoginRequest arg1) {
		return null;
	}

	@Override
	public SecurityUser logout() {
		return null;
	}

	@Override
	public SecurityUser logout(String arg0) {
		return null;
	}

	@Override
	public AuthToken refresh(HttpServletRequest arg0, String arg1, String arg2) {
		return null;
	}
	
}
