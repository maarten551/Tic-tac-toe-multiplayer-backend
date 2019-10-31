package com.maarten551.tictactoe_backend.exception;

public class WrongPlayerTryingToExecuteAnActionException extends Exception {
    public WrongPlayerTryingToExecuteAnActionException(String message) {
        super(message);
    }
}
