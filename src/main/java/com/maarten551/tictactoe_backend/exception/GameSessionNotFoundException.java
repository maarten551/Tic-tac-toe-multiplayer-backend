package com.maarten551.tictactoe_backend.exception;

public class GameSessionNotFoundException extends Exception {
    public GameSessionNotFoundException(String message) {
        super(message);
    }
}
