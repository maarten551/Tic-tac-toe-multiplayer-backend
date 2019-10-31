package com.maarten551.tictactoe_backend.configuration.eventlistener;

import com.maarten551.tictactoe_backend.controller.LobbyController;
import com.maarten551.tictactoe_backend.exception.PlayerNotInLobbyException;
import com.maarten551.tictactoe_backend.logic.LobbyContainer;
import com.maarten551.tictactoe_backend.logic.PlayerContainer;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SessionDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final SimpMessagingTemplate template;
    private final PlayerContainer playerContainer;
    private final LobbyContainer lobbyContainer;

    @Autowired
    public SessionDisconnectEventListener(PlayerContainer playerContainer, LobbyContainer lobbyContainer, SimpMessagingTemplate template) {
        this.playerContainer = playerContainer;
        this.lobbyContainer = lobbyContainer;
        this.template = template;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Player playerBySessionId = playerContainer.getPlayerBySessionId(sessionDisconnectEvent.getSessionId());

        if (this.lobbyContainer.getLobbyByPlayer(playerBySessionId).isPresent()) {
            try {
                this.lobbyContainer.removePlayerFromLobby(playerBySessionId);
                // Update all lobby information
                this.template.convertAndSend(LobbyController.LOBBY_OVERVIEW_ENDPOINT, this.lobbyContainer.getAllWaitingLobbies());
            } catch (PlayerNotInLobbyException e) {
                // It doesn't really matter when this exception comes along, we just to remove the player from the lobby
                // Also, it shouldn't come here because of the if check
                e.printStackTrace();
            }
        }

        // Actually remove the player from the application
        playerContainer.removePlayerFromApplicationAndCleanup(sessionDisconnectEvent.getSessionId());
    }
}
