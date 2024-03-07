package server;

import model.GameData;

import java.util.ArrayList;

public class ListGameResponse {
    private ArrayList<GameData> games;

    public void setGameList(ArrayList<GameData> games) {
        this.games = games;
    }
}
