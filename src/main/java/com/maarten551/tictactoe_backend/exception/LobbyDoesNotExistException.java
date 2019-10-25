package com.maarten551.tictactoe_backend.exception;

public class LobbyDoesNotExistException extends Exception {
    public LobbyDoesNotExistException(String message) {
        super(message);
    }
}
