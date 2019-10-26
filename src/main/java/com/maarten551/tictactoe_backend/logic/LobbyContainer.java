package com.maarten551.tictactoe_backend.logic;

import com.maarten551.tictactoe_backend.exception.*;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LobbyContainer {
    public static final int MINIMUM_AMOUNT_OF_PLAYERS_TO_START_GAME = 2;
    private final GameSessionContainer gameSessionContainer;
    private ArrayList<Lobby> activeLobbies;

    public LobbyContainer(GameSessionContainer gameSessionContainer) {
        this.gameSessionContainer = gameSessionContainer;

        this.activeLobbies = new ArrayList<>();
    }

    public Lobby createNewLobby(Player leader, String lobbyName) {
        Lobby createdLobby = new Lobby(leader);
        createdLobby.gameSession = this.gameSessionContainer.createGameSession(createdLobby);

        createdLobby.name = lobbyName;

        this.activeLobbies.add(createdLobby);

        return createdLobby;
    }

    public List<Lobby> getAllWaitingLobbies() {
        return this.activeLobbies.stream().filter(lobby -> !lobby.gameSession.isActive)
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

    public Lobby getLobbyById(UUID lobbyId) throws LobbyDoesNotExistException {
        Optional<Lobby> relatedLobby = this.activeLobbies.stream().filter(lobby -> lobby.id.equals(lobbyId)).findFirst();
        if (!relatedLobby.isPresent()) {
            throw new LobbyDoesNotExistException(String.format("Lobby with name '%s' does not exist", lobbyId));
        }

        return relatedLobby.get();
    }

    public void startGameInLobby(Lobby lobby, Player isStartedByPlayer) throws PlayerIsNotLeaderOfGameException, GameSessionHasAlreadyStartedException, NotEnoughPlayersInLobbyToStartException {
        if (lobby.leader != isStartedByPlayer) {
            throw new PlayerIsNotLeaderOfGameException("You're not the leader of the lobby");
        } else if (lobby.players.size() < MINIMUM_AMOUNT_OF_PLAYERS_TO_START_GAME) {
            throw new NotEnoughPlayersInLobbyToStartException(String.format("You need atleast %d players to start the game", MINIMUM_AMOUNT_OF_PLAYERS_TO_START_GAME));
        }

        this.gameSessionContainer.startGameSession(lobby);
    }
}
