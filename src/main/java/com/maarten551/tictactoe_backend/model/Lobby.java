package com.maarten551.tictactoe_backend.model;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Lobby {
    private UUID id;
    private Player leader;
    private HashSet<Player> players;
    private HashMap<Player, Color> playerColors;

    private GameSession gameSession;

    public Lobby(Player leader, GameSession gameSession) {
        this.id = UUID.randomUUID();

        this.gameSession = gameSession;
        this.leader = leader;

        this.players = new HashSet<>();
        this.players.add(leader);
    }

    public UUID getId() {
        return id;
    }

    public Player getLeader() {
        return leader;
    }

    public HashSet<Player> getPlayers() {
        return players;
    }

    public HashMap<Player, Color> getPlayerColors() {
        return playerColors;
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}
