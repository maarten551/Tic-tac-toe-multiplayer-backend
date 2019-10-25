package com.maarten551.tictactoe_backend.controller;

import java.util.List;
import java.util.Optional;

import com.maarten551.tictactoe_backend.exception.LobbyDoesNotExistException;
import com.maarten551.tictactoe_backend.exception.PlayerNotInLobbyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.maarten551.tictactoe_backend.exception.PlayerAlreadyInLobbyException;
import com.maarten551.tictactoe_backend.logic.LobbyContainer;
import com.maarten551.tictactoe_backend.logic.PlayerContainer;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;

@Controller
public class LobbyController {
	public static final String LOBBY_OVERVIEW_ENDPOINT = "/lobbies";

	private LobbyContainer lobbyContainer;
	private PlayerContainer playerContainer;

	@Autowired
	public LobbyController(LobbyContainer lobbyContainer, PlayerContainer playerContainer) {
		this.lobbyContainer = lobbyContainer;
		this.playerContainer = playerContainer;
	}

	@SubscribeMapping(LOBBY_OVERVIEW_ENDPOINT)
	public List<Lobby> onLobbyOverview() {
		return this.lobbyContainer.getAllWaitingLobbies();
	}

	@MessageMapping({ "/send/lobbies/create" })
	@SendTo(LOBBY_OVERVIEW_ENDPOINT)
	public List<Lobby> onLobbyCreate(@Payload String lobbyName, SimpMessageHeaderAccessor headerAccessor)
			throws PlayerAlreadyInLobbyException {
        Player player = this.playerContainer.getPlayerBySessionId(headerAccessor.getSessionId());

		// Check if the player is already in a lobby
		Optional<Lobby> lobbyByPlayer = this.lobbyContainer.getLobbyByPlayer(player);
		if (lobbyByPlayer.isPresent()) {
			throw new PlayerAlreadyInLobbyException(player, lobbyByPlayer.get());
		}

		this.lobbyContainer.createNewLobby(player, lobbyName);

		return this.lobbyContainer.getAllWaitingLobbies();
	}

    @MessageMapping({"/send/lobbies/leave-current"})
    @SendTo(LOBBY_OVERVIEW_ENDPOINT)
    public List<Lobby> onLeaveCurrentLobby(SimpMessageHeaderAccessor headerAccessor) throws PlayerNotInLobbyException {
        this.lobbyContainer.removePlayerFromLobby(this.playerContainer.getPlayerBySessionId(headerAccessor.getSessionId()));

        return this.lobbyContainer.getAllWaitingLobbies();
    }

    @MessageMapping({"/send/lobbies/join"})
    @SendTo(LOBBY_OVERVIEW_ENDPOINT)
    public List<Lobby> onJoinLobby(@Payload String lobbyId, SimpMessageHeaderAccessor headerAccessor) throws LobbyDoesNotExistException, PlayerAlreadyInLobbyException {
        Player player = this.playerContainer.getPlayerBySessionId(headerAccessor.getSessionId());
        Lobby lobby = this.lobbyContainer.getLobbyById(lobbyId);

        if (this.lobbyContainer.getLobbyByPlayer(player).isPresent()) {
            throw new PlayerAlreadyInLobbyException(player, lobby);
        }

        lobby.players.add(player);

        return this.lobbyContainer.getAllWaitingLobbies();
    }

	@MessageExceptionHandler
    @SendToUser("/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
    }
}
