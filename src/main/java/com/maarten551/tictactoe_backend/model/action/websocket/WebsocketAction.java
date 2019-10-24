package com.maarten551.tictactoe_backend.model.action.websocket;

/**
 * @author BOBBM01
 */
public interface WebsocketAction<T> {
	String getType();

	T getValue();
}
