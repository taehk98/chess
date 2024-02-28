package server;

import model.GameData;

public class ListGameResponse {
    private GameData[] games;

    public void setGameList(GameData[] games) {
        this.games = games;
    }
}
