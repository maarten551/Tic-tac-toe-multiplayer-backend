package com.maarten551.tictactoe_backend.logic;

import com.maarten551.tictactoe_backend.exception.GameSessionHasAlreadyStartedException;
import com.maarten551.tictactoe_backend.exception.GameSessionNotFoundException;
import com.maarten551.tictactoe_backend.model.GameSession;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class GameSessionContainer {
    // Use these colors for the players, if the group is bigger, generate random colors
    private final Color[] preferablePlayerColors = new Color[]{
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            Color.BLACK
    };
    private Map<UUID, GameSession> gameSessionByLobbyId;

    public GameSessionContainer() {
        this.gameSessionByLobbyId = new HashMap<>();
    }

    public GameSession createGameSession(Lobby lobby) {
        GameSession gameSession = new GameSession(lobby);

        this.gameSessionByLobbyId.put(lobby.id, gameSession);

        return gameSession;
    }

    public void startGameSession(Lobby lobby) throws GameSessionHasAlreadyStartedException {
        GameSession gameSession = this.gameSessionByLobbyId.get(lobby.id);
        if (gameSession == null) {
            return;
        }

        if (gameSession.isActive) {
            throw new GameSessionHasAlreadyStartedException("Lobby has already started the game");
        }

        gameSession.playerColors = this.generateColorsForLobby(lobby);
        gameSession.isActive = true;
    }

    public Player determinePlayingPlayerInSession(UUID lobbyId) throws GameSessionNotFoundException {
        GameSession gameSession = this.gameSessionByLobbyId.get(lobbyId);
        if (gameSession == null) {
            throw new GameSessionNotFoundException(String.format("No game session found with lobby id '%s'", lobbyId.toString()));
        }

        List<Player> lobbyPlayer = gameSession.getLobby().players;
        return lobbyPlayer.get(gameSession.turnCounter % lobbyPlayer.size());
    }

    private Map<Player, Color> generateColorsForLobby(Lobby lobby) {
        Map<Player, Color> playerColors = new HashMap<>();

        for (int i = 0; i < lobby.players.size(); i++) {
            Color playerColor;
            if (i < this.preferablePlayerColors.length) {
                playerColor = this.preferablePlayerColors[i];
            } else {
                playerColor = this.generateRandomColor();
            }

            playerColors.put(lobby.players.get(i), playerColor);
        }

        return playerColors;
    }

    /**
     * Stolen from https://stackoverflow.com/questions/4246351/creating-random-colour-in-java/4246418
     *
     * @return a pleasant palet color (according to a stackoverflow topic)
     */
    private Color generateRandomColor() {
        Random random = new Random();

        final float hue = random.nextFloat();

        // Saturation between 0.1 and 0.3
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;

        return Color.getHSBColor(hue, saturation, luminance);
    }
}
