package server;

import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;

public class ListGameResponse {
    private GameData[] games;

    public GameData[] getGameList() {
        return games;
    }

    public void setGameList(GameData[] games) {
        this.games = games;
    }
}
