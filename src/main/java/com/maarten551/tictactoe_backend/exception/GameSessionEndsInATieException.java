package com.maarten551.tictactoe_backend.exception;

public class GameSessionEndsInATieException extends Exception {
    public GameSessionEndsInATieException(String message) {
        super(message);
    }
}
