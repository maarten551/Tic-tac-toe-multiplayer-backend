package com.maarten551.tictactoe_backend.model.action.websocket.lobby;

import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.action.websocket.WebsocketAction;

public class CreateLobby implements WebsocketAction<Lobby> {
	@Override
	public String getType() {
		return "createLobby";
	}

	@Override
	public Lobby getValue() {
		throw new UnsupportedOperationException("TODO: implement method getValue() --> Object");
	}
}
