package com.maarten551.tictactoe_backend.model;

public class Player {
	public static final String DEFAULT_PLAYER_NAME = "<Unknown>";

	private String sessionId;
	private String username = DEFAULT_PLAYER_NAME;

	public Player(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
