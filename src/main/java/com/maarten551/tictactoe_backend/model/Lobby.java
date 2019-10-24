package com.maarten551.tictactoe_backend.model;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Lobby {
	public UUID id;
	public String name;
	public Player leader;
	public Set<Player> players;
	public Map<Player, Color> playerColors;

	public GameSession gameSession;

    public Lobby(Player leader, GameSession gameSession) {
        this.id = UUID.randomUUID();

        this.gameSession = gameSession;
        this.leader = leader;

        this.players = new HashSet<>();
        this.players.add(leader);
    }
}
