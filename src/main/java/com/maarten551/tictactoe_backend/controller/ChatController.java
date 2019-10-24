package com.maarten551.tictactoe_backend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.maarten551.tictactoe_backend.logic.PlayerContainer;
import com.maarten551.tictactoe_backend.model.Player;

@Controller
public class ChatController {
    private PlayerContainer playerContainer;

    @Autowired
    public ChatController(PlayerContainer playerContainer) {
        this.playerContainer = playerContainer;
    }

    @MessageMapping({"/send/message"})
    @SendTo("/chat")
    public String onReceiveMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        Player currentPlayer = playerContainer.getPlayerByHeader(headerAccessor);
        String dateString = new SimpleDateFormat("HH:mm:ss").format(new Date());

        return String.format("%s - %s: %s", dateString, currentPlayer.getUsername(), message);
    }
}
