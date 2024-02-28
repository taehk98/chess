package server;

public class JoinRequest {
    private String playerColor;
    private int gameID;

    public JoinRequest(String playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

}
