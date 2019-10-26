package com.maarten551.tictactoe_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maarten551.tictactoe_backend.model.serializer.PlayerColorSerializer;

import java.awt.*;
import java.util.Map;

public class GameSession {
    @JsonSerialize(using = PlayerColorSerializer.class)
    public Map<Player, Color> playerColors;
    public boolean isActive = false;
    public int turnCounter = 0;
    public String currentPlayingPlayerBySessionId;
    @JsonIgnore
    private Lobby lobby;

    public GameSession(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }
}
