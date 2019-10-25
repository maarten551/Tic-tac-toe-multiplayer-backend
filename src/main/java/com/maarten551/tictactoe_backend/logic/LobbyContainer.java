package com.maarten551.tictactoe_backend.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.maarten551.tictactoe_backend.exception.LobbyDoesNotExistException;
import com.maarten551.tictactoe_backend.exception.PlayerNotInLobbyException;
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

    public void removePlayerFromLobby(Player player) throws PlayerNotInLobbyException {
        Optional<Lobby> lobbyByPlayer = this.getLobbyByPlayer(player);
        if (!lobbyByPlayer.isPresent()) {
            throw new PlayerNotInLobbyException("You're not in a lobby!");
        }

        Lobby lobby = lobbyByPlayer.get();
        if (lobby.leader == player) {
            this.activeLobbies.remove(lobby);
        } else {
            lobby.players.remove(player);
        }
    }

    public Lobby getLobbyById(String lobbyId) throws LobbyDoesNotExistException {
        Optional<Lobby> relatedLobby = this.activeLobbies.stream().filter(lobby -> lobby.id.toString().equals(lobbyId)).findFirst();
        if (!relatedLobby.isPresent()) {
            throw new LobbyDoesNotExistException(String.format("Lobby with name '%s' does not exist", lobbyId));
        }

        return relatedLobby.get();
    }
}
