package com.maarten551.tictactoe_backend.exception;

public class PlayerIsNotLeaderOfGameException extends Exception {
    public PlayerIsNotLeaderOfGameException(String message) {
        super(message);
    }
}
