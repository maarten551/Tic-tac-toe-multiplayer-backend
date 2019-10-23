package com.maarten551.tictactoe_backend.logic;

import com.maarten551.tictactoe_backend.model.GameSession;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LobbyContainer {
    private ArrayList<Lobby> activeLobbies;

    public LobbyContainer() {
        this.activeLobbies = new ArrayList<>();

        // TODO: remove this, is mock data for now
        Player leaderPlayer = new Player("This is a mock leader sessionID");
        Player normalPlayer = new Player("This is a mock player sessionID");

        leaderPlayer.setUsername("Leader username");
        normalPlayer.setUsername("Player username");

        this.activeLobbies.add(
                new Lobby(
                        leaderPlayer,
                        new GameSession()
                )
        );
        this.activeLobbies.get(0).getPlayers().add(normalPlayer);
    }

    public List<Lobby> getAllWaitingLobbies() {
        return this.activeLobbies.stream()
                .filter(lobby -> !lobby.getGameSession().isActive())
                .collect(Collectors.toList());
    }
}
