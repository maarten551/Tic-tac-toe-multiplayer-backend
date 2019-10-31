package com.maarten551.tictactoe_backend.model;

import java.util.*;

public class Lobby {
    public UUID id;
    public String name;
    public Player leader;
    public List<Player> players;
    public GameSession gameSession;

    public Lobby(Player leader) {
        this.id = UUID.randomUUID();

        this.leader = leader;

        this.players = new ArrayList<>();
        this.players.add(leader);
    }
}
