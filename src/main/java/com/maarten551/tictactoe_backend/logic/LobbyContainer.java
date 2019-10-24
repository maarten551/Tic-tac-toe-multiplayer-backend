package com.maarten551.tictactoe_backend.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.maarten551.tictactoe_backend.model.GameSession;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;

@Service
public class LobbyContainer {
    private ArrayList<Lobby> activeLobbies;

    public LobbyContainer() {
        this.activeLobbies = new ArrayList<>();
    }

    public Lobby createNewLobby(Player leader, String lobbyName) {
        Lobby createdLobby = new Lobby(leader, new GameSession());
        createdLobby.name = lobbyName;

        this.activeLobbies.add(createdLobby);

        return createdLobby;
    }

    public List<Lobby> getAllWaitingLobbies() {
        return this.activeLobbies.stream().filter(lobby -> !lobby.gameSession.isActive())
                .collect(Collectors.toList());
    }

    public Optional<Lobby> getLobbyByPlayer(Player player) {
        Optional<Lobby> lobbyByLeader = this.activeLobbies.stream().filter(lobby -> lobby.leader == player).findFirst();
        if (lobbyByLeader.isPresent()) {
            return lobbyByLeader;
        }

        return this.activeLobbies.stream().filter(lobby -> lobby.players.contains(player)).findFirst();
    }
}
