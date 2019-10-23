package com.maarten551.tictactoe_backend.controller;

import com.maarten551.tictactoe_backend.logic.LobbyContainer;
import com.maarten551.tictactoe_backend.model.Lobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LobbyController {
    private LobbyContainer lobbyContainer;

    @Autowired
    public LobbyController(LobbyContainer lobbyContainer) {
        this.lobbyContainer = lobbyContainer;
    }

    @SubscribeMapping("/lobbies")
    public List<Lobby> onLobbyOverview() {
        return this.lobbyContainer.getAllWaitingLobbies();
    }
}
