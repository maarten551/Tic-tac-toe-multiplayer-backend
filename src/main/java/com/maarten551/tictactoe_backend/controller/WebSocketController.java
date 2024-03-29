package com.maarten551.tictactoe_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping({"/send/message"})
    public void onReceiveMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        String username = "<Unknown>";
        if (Objects.requireNonNull(headerAccessor.getSessionAttributes()).containsKey(NameController.NAME_IN_SESSION_KEY)) {
            username = (String)Objects.requireNonNull(headerAccessor.getSessionAttributes()).get(NameController.NAME_IN_SESSION_KEY);
        }

        String dateString = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String payload = String.format("%s - %s: %s", dateString, username, message);

        this.template.convertAndSend("/chat", payload);
    }
}
