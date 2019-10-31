package com.maarten551.tictactoe_backend.exception;

public class PlayerNotInLobbyException extends Exception {
    public PlayerNotInLobbyException(String message) {
        super(message);
    }
}
