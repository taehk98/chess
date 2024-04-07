package RequestResponses;

import model.GameData;

import java.util.ArrayList;

public class ListGameResponse {
    private GameData[] games = null;

    public void setGameList(GameData[] games) {
        this.games = games;
    }

    public String getGamesString() {
        if (games == null || games.length == 0) {
            return "No games available.";
        }

        StringBuilder sb = new StringBuilder();
        for (GameData gameData : games) {
            sb.append("Game ID: ").append(gameData.gameID())
                    .append(", White Username: ").append(gameData.whiteUsername())
                    .append(", Black Username: ").append(gameData.blackUsername())
                    .append(", Game Name: ").append(gameData.gameName())
                    .append("\n");
        }
        return sb.toString();
    }
}
