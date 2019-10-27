package com.maarten551.tictactoe_backend.logic;

import com.maarten551.tictactoe_backend.exception.*;
import com.maarten551.tictactoe_backend.model.FieldCell;
import com.maarten551.tictactoe_backend.model.GameSession;
import com.maarten551.tictactoe_backend.model.Lobby;
import com.maarten551.tictactoe_backend.model.Player;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class GameSessionContainer {
    private final int neededAmountOfFieldCellsInARow = 3;
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
        gameSession.field = this.generateField(lobby);
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

    public void executeMove(Lobby lobby, Player player, int x, int y) throws FieldIsAlreadySetException, GameSessionEndsInATieException, GameSessionWonByPlayerException {
        GameSession gameSession = this.gameSessionByLobbyId.get(lobby.id);
        FieldCell fieldCell = gameSession.field[x][y];

        if (fieldCell.selectedByPlayer != null) {
            throw new FieldIsAlreadySetException(String.format("This field is already set by the player '%s'", fieldCell.selectedByPlayer.getUsername()));
        }

        fieldCell.selectedByPlayer = player;
        fieldCell.colorOfPlayer = gameSession.playerColors.get(player);
        gameSession.turnCounter++;

        if (this.checkIfPlayerHasWon(gameSession, player)) {
            throw new GameSessionWonByPlayerException(String.format("Game won by player '%s', congrats!", player.getUsername()));
        }

        if (gameSession.turnCounter == gameSession.field.length * gameSession.field.length) {
            throw new GameSessionEndsInATieException("All the fields are filled, it's a tie!");
        }
    }

    private boolean checkIfPlayerHasWon(GameSession gameSession, Player player) {
        int[][] strategies = new int[4][2];
        strategies[0] = new int[]{1, 0};
        strategies[1] = new int[]{0, 1};
        strategies[2] = new int[]{1, 1};
        strategies[3] = new int[]{-1, 1};

        for (int x = 0; x < gameSession.field.length; x++) {
            for (int y = 0; y < gameSession.field[x].length; y++) {
                for (int[] strategy : strategies) {
                    boolean hasWon = this.checkIfPlayerHasWonOnFieldCellAndStrategy(gameSession, x, y, strategy, player);
                    if (hasWon) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean checkIfPlayerHasWonOnFieldCellAndStrategy(GameSession gameSession, int x, int y, int[] strategy, Player player) {
        return IntStream.range(0, this.neededAmountOfFieldCellsInARow).allMatch(cellSequenceIndex -> {
            int sequenceX = x + (strategy[0] * cellSequenceIndex);
            int sequenceY = y + (strategy[1] * cellSequenceIndex);

            if (sequenceX < 0 || sequenceY < 0) {
                return false;
            }

            if (sequenceX >= gameSession.field.length || sequenceY >= gameSession.field.length) {
                return false;
            }

            return gameSession.field[sequenceX][sequenceY].selectedByPlayer == player;
        });
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

    private FieldCell[][] generateField(Lobby lobby) {
        // The field gets bigger as long more players join the lobby
        // Default is 3 fields by 3 fields when there 2 players, for every player extra, the dimension is +2
        int dimension = 3 + ((lobby.players.size() - 2) * 2);

        FieldCell[][] field = new FieldCell[dimension][];
        for (int i = 0; i < field.length; i++) {
            field[i] = new FieldCell[dimension];
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new FieldCell();
            }
        }

        return field;
    }

    public void removeGameSession(Lobby lobby) {
        this.gameSessionByLobbyId.remove(lobby.id);
    }
}
