package com.maarten551.tictactoe_backend.exception;

public class FieldIsAlreadySetException extends Exception {
    public FieldIsAlreadySetException(String message) {
        super(message);
    }
}
