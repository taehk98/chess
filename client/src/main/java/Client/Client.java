package Client;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.CreateGameResponse;
import server.JoinRequest;
import server.ListGameResponse;
import server.RegisterRes;

import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String username = null;
    private String password = null;
    private int gameID = 0;
    private String playerColor = null;
    private AuthData auth = null;
    private boolean isWhite = false;
    private boolean isBlack = false;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> logIn(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                case "logout" -> logOut();
                case "list" -> listGames();
                case "join", "observe" -> joinGame(params);
                case "create" -> createGame(params);
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String logIn(String... params) throws DataAccessException {
        if (params.length >= 1) {
            username = params[0];
            password = params[1];
            UserData user = new UserData(username, password, null);

            auth = server.getUser(user);
            state = State.SIGNEDIN;
            return String.format("Logged in as %s", username);
        }
        throw new DataAccessException("client Login Error");
    }

    public String register(String... params) throws DataAccessException {
        if (params.length >= 1){
            username = params[0];
            UserData user = new UserData(params[0], params[1], params[2]);
            auth = server.addUser(user);
            state = State.SIGNEDIN;
            return String.format("Logged in as %s", params[0]);
        }
        throw new DataAccessException("client Register Error");
    }

    public String logOut() throws DataAccessException {
        assertSignedIn();
        server.deleteUser(auth);
        state = State.SIGNEDOUT;
        auth = null;
        return String.format("%s is signed out", username);
    }

    public String createGame(String... params) throws DataAccessException {
        assertSignedIn();
        if(params.length == 1) {
            String gameName = params[0];
            GameData game = new GameData(0, null, null, gameName, null);
            CreateGameResponse res = server.createGame(auth, game);
            return String.format("You created a game with ID: %d", res.getGameID());
        }else {
            throw new DataAccessException("CreateGame Wrong input");
        }
    }

    public String listGames() throws DataAccessException {
        assertSignedIn();
        ListGameResponse list = server.listGames(auth);

        return list.getGamesString();
    }

    public String joinGame(String... params) throws DataAccessException {
        assertSignedIn();
        if(params.length == 1){
            gameID = Integer.parseInt(params[0]);
            playerColor = null;
        }
        else if (params.length == 2){
            gameID = Integer.parseInt(params[0]);
            playerColor = params[1];
            if(playerColor.equalsIgnoreCase("white")){
                isWhite = true;
            }else{
                isBlack = true;
            }
        }
        else {
            throw new DataAccessException("JoinGame Wrong input");
        }
        JoinRequest req = new JoinRequest(playerColor, gameID);
        RegisterRes res = server.joinGame(auth, req);
        if(res.getMessage() != null){
            return res.getMessage();
        }else{
            return String.format("You joined a game as ID: %d", gameID);
        }
    }

    public String clear() throws DataAccessException {
        server.clear();
        state = State.SIGNEDOUT;
        auth = null;
        return "cleared everything";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<emtpy>] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws DataAccessException {
        if (state == State.SIGNEDOUT) {
            throw new DataAccessException("You must sign in");
        }
    }
}
