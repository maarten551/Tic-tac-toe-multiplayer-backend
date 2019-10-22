package com.maarten551.tictactoe_backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class NameController {
    public static final String NAME_IN_SESSION_KEY = "username";

    @MessageMapping({"/send/username"})
    public void setNameInSession(@Payload String name, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put(NAME_IN_SESSION_KEY, name);
    }
}
