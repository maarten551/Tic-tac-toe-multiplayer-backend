package com.maarten551.tictactoe_backend.exception;

public class NotEnoughPlayersInLobbyToStartException extends Exception {
    public NotEnoughPlayersInLobbyToStartException(String message) {
        super(message);
    }
}
