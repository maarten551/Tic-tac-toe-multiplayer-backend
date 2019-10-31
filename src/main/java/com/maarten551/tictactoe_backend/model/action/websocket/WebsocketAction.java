package com.maarten551.tictactoe_backend.model.action.websocket;

public interface WebsocketAction<T> {
	String getType();

	T getValue();
}
