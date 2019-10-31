package com.maarten551.tictactoe_backend.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.maarten551.tictactoe_backend.model.Player;

@Service
public class PlayerContainer {
	private Map<String, Player> playersBySessionId;

	public PlayerContainer() {
		this.playersBySessionId = new HashMap<>();
	}

	/**
	 * Finds player by sessionId, if no player is found, create the player.
	 */
	public Player getPlayerByHeader(SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor.getSessionId();
		if (this.playersBySessionId.containsKey(sessionId)) {
			return this.playersBySessionId.get(sessionId);
		}

		Player newPlayer = new Player(sessionId);
		this.playersBySessionId.put(sessionId, newPlayer);

		return newPlayer;
	}

	public List<Player> getAllPlayers() {
		return new ArrayList<>(this.playersBySessionId.values());
	}
}
