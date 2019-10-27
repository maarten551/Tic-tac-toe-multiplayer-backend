package com.maarten551.tictactoe_backend.dto;

import com.maarten551.tictactoe_backend.model.Lobby;

public class SelectedLobbyOverview {
    public Lobby lobby;
    public String gameOverMessage;
    public String gameOverMessageType = "error";
}
