package com.maarten551.tictactoe_backend.controller;

import com.maarten551.tictactoe_backend.dto.PlayerExecutionMove;
import com.maarten551.tictactoe_backend.dto.SelectedLobbyOverview;
import com.maarten551.tictactoe_backend.exception.*;
import com.maarten551.tictactoe_backend.logic.GameSessionContainer;
import com.maarten551.tictactoe_backend.logic.LobbyContainer;
import com.maarten551.tictactoe_backend.logic.PlayerContainer;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LobbyController {
	public static final String LOBBY_OVERVIEW_ENDPOINT = "/lobbies";
    public static final String LOBBY_JOINED_ENDPOINT = "/lobby/{lobbyId}";

    private final LobbyContainer lobbyContainer;
    private final PlayerContainer playerContainer;
    private final GameSessionContainer gameSessionContainer;
    private final SimpMessagingTemplate template;

	@Autowired
    public LobbyController(LobbyContainer lobbyContainer, PlayerContainer playerContainer, GameSessionContainer gameSessionContainer, SimpMessagingTemplate template) {
		this.lobbyContainer = lobbyContainer;
		this.playerContainer = playerContainer;
        this.gameSessionContainer = gameSessionContainer;
        this.template = template;
	}

	@SubscribeMapping(LOBBY_OVERVIEW_ENDPOINT)
	public List<Lobby> onLobbyOverview() {
		return this.lobbyContainer.getAllWaitingLobbies();
	}

//	@SubscribeMapping(LOBBY_JOINED_ENDPOINT)
//	public Lobby onJoinLobby(@DestinationVariable String lobbyId) throws LobbyDoesNotExistException {
//		return this.lobbyContainer.getLobbyById(UUID.fromString(lobbyId));
//	}

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
        Lobby lobby = this.lobbyContainer.getLobbyById(UUID.fromString(lobbyId));

        if (this.lobbyContainer.getLobbyByPlayer(player).isPresent()) {
            throw new PlayerAlreadyInLobbyException(player, lobby);
        }

        lobby.players.add(player);

        return this.lobbyContainer.getAllWaitingLobbies();
    }

    @MessageMapping({"/send/lobby/{lobbyId}/start-game"})
    @SendTo(LOBBY_OVERVIEW_ENDPOINT)
    public List<Lobby> onLobbyStartGame(@DestinationVariable String lobbyId, SimpMessageHeaderAccessor headerAccessor) throws LobbyDoesNotExistException, GameSessionHasAlreadyStartedException, PlayerIsNotLeaderOfGameException, NotEnoughPlayersInLobbyToStartException, GameSessionNotFoundException {
        UUID lobbyUUID = UUID.fromString(lobbyId);
        Player player = this.playerContainer.getPlayerBySessionId(headerAccessor.getSessionId());
        Lobby lobby = this.lobbyContainer.getLobbyById(lobbyUUID);

        this.lobbyContainer.startGameInLobby(lobby, player);

        SelectedLobbyOverview selectedLobbyOverview = new SelectedLobbyOverview();
        selectedLobbyOverview.lobby = lobby;
        selectedLobbyOverview.lobby.gameSession.currentPlayingPlayerBySessionId = this.gameSessionContainer.determinePlayingPlayerInSession(lobbyUUID).getSessionId();
        template.convertAndSend(LOBBY_JOINED_ENDPOINT.replace("{lobbyId}", lobbyId), selectedLobbyOverview);

        return this.lobbyContainer.getAllWaitingLobbies();
    }

    @MessageMapping({"/send/lobby/{lobbyId}/execute-move"})
    public void onPlayerActionExecution(@DestinationVariable String lobbyId, @Payload PlayerExecutionMove move, SimpMessageHeaderAccessor headerAccessor) throws GameSessionNotFoundException, WrongPlayerTryingToExecuteAnActionException, LobbyDoesNotExistException, FieldIsAlreadySetException {
        UUID lobbyUUID = UUID.fromString(lobbyId);
        Player playerAllowedToMakeAMove = this.gameSessionContainer.determinePlayingPlayerInSession(lobbyUUID);
        if (!playerAllowedToMakeAMove.getSessionId().equals(headerAccessor.getSessionId())) {
            throw new WrongPlayerTryingToExecuteAnActionException("It's not your turn!");
        }

        Lobby lobby = this.lobbyContainer.getLobbyById(lobbyUUID);
        Player player = playerContainer.getPlayerBySessionId(headerAccessor.getSessionId());
        SelectedLobbyOverview selectedLobbyOverview = new SelectedLobbyOverview();

        try {
            this.gameSessionContainer.executeMove(lobby, player, move.x, move.y);
        } catch (GameSessionEndsInATieException e) {
            selectedLobbyOverview.gameOverMessageType = "warning";
            selectedLobbyOverview.gameOverMessage = e.getMessage();
        }

        selectedLobbyOverview.lobby = lobby;
        selectedLobbyOverview.lobby.gameSession.currentPlayingPlayerBySessionId = this.gameSessionContainer.determinePlayingPlayerInSession(lobbyUUID).getSessionId();
        template.convertAndSend(LOBBY_JOINED_ENDPOINT.replace("{lobbyId}", lobbyId), selectedLobbyOverview);

        if (selectedLobbyOverview.gameOverMessage != null && selectedLobbyOverview.gameOverMessage.length() > 0) {
            this.gameSessionContainer.removeGameSession(lobby);
            this.lobbyContainer.removeLobby(lobby);
        }
    }

	@MessageExceptionHandler
    @SendToUser("/errors")
	public String handleException(Throwable exception) {
        exception.printStackTrace();

		return exception.getMessage();
    }
}
