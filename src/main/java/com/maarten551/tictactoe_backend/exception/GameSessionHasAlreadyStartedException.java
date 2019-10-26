package com.maarten551.tictactoe_backend.exception;

public class GameSessionHasAlreadyStartedException extends Exception {
    public GameSessionHasAlreadyStartedException(String message) {
        super(message);
    }
}
