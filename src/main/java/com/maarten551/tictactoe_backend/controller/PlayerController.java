package com.maarten551.tictactoe_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.maarten551.tictactoe_backend.dto.PlayerOverview;
import com.maarten551.tictactoe_backend.logic.PlayerContainer;
import com.maarten551.tictactoe_backend.model.Player;

@Controller
public class PlayerController {
	public static final String PLAYER_OVERVIEW_ENDPOINT = "/players";

	private PlayerContainer playerContainer;

	@Autowired
	public PlayerController(PlayerContainer playerContainer) {
		this.playerContainer = playerContainer;
	}

	@SubscribeMapping(PLAYER_OVERVIEW_ENDPOINT)
	public PlayerOverview onPlayerOverview(SimpMessageHeaderAccessor headerAccessor) {
		PlayerOverview playerOverview = new PlayerOverview();
		playerOverview.sessionId = this.playerContainer.getPlayerByHeader(headerAccessor).getSessionId();
		playerOverview.players = this.playerContainer.getAllPlayers();

		return playerOverview;
	}

	@MessageMapping({ "/send/players/set-username" })
	@SendTo(PLAYER_OVERVIEW_ENDPOINT)
	public PlayerOverview setNameInSession(@Payload String name, SimpMessageHeaderAccessor headerAccessor) {
		if (name.trim().length() == 0) {
			name = Player.DEFAULT_PLAYER_NAME;
		}

		Player playerByHeader = this.playerContainer.getPlayerByHeader(headerAccessor);

		playerByHeader.setUsername(name);

		PlayerOverview playerOverview = new PlayerOverview();
		playerOverview.sessionId = playerByHeader.getSessionId();
		playerOverview.players = this.playerContainer.getAllPlayers();

		return playerOverview;
	}
}
