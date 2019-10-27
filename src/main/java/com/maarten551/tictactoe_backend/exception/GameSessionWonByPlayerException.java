package com.maarten551.tictactoe_backend.exception;

public class GameSessionWonByPlayerException extends Exception {
    public GameSessionWonByPlayerException(String message) {
        super(message);
    }
}
