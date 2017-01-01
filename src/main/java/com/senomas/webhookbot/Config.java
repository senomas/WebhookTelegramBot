package com.senomas.webhookbot;

public class Config {

	String username;
	
	String token;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Config [username=" + username + ", token=" + token + "]";
	}
}
