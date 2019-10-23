package com.maarten551.tictactoe_backend.model;

public class Player {
    private String sessionId;
    private String username;

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
