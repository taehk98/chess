package server;

import model.GameData;

import java.util.ArrayList;

public class ListGameResponse {
    private GameData[] games;

    public void setGameList(GameData[] games) {
        this.games = games;
    }
}
