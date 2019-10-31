package com.maarten551.tictactoe_backend.exception;

import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;

public class PlayerAlreadyInLobbyException extends Exception {
	public PlayerAlreadyInLobbyException(Player player, Lobby lobby) {
		super(String.format("Player '%s' is already in lobby '%s'", player.getUsername(), lobby.name));
	}
}
