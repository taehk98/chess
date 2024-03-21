package Client;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String username = null;
    private String password = null;
    private AuthData auth = null;

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
//                case "logout" -> logOut();
//                case "list" -> listGames();
//                case "join" -> joinGame(params);
//                case "create" -> createGame(params);
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
            UserData user = new UserData(params[0], params[1], params[2]);
            auth = server.addUser(user);
            state = State.SIGNEDIN;
            return String.format("Logged in as %s", params[0]);
        }
        throw new DataAccessException("client Register Error");
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
}
