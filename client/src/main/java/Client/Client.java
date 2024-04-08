package Client;

import Client.websocket.NotificationHandler;
import Client.websocket.WebSocketFacade;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.UserData;
import RequestResponses.CreateGameResponse;
import RequestResponses.JoinRequest;
import RequestResponses.ListGameResponse;
import RequestResponses.RegisterRes;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.server.ExportException;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private final WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
    private String username = null;
    private String password = null;
    private int gameID = 0;
    private String playerColor = null;
    private AuthData auth = null;
    private boolean isWhite = false;
    private boolean isBlack = false;
    private boolean isObserver = false;

    public Client(String serverUrl , NotificationHandler notificationHandler) throws DeploymentException, IOException, URISyntaxException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, notificationHandler);
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
                case "redraw" -> redraw();
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "move" -> move(params);
                case "leave" -> leave();
                case "help" -> help();
                default -> error();
            };
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

    public boolean getIsWhite() {
        return isWhite;
    }
    public String redraw() throws IOException {
        if(isBlack || isObserver || isWhite){
            return "redraw";
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "Failed to redraw. Not in a game");
        }

    }

    public String highlight(String...params) throws IOException {
        assertSignedIn();
        if (params.length == 1 && isBlack || isObserver || isWhite){
            return "highlight " + params[0];
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "Failed to see valid moves. Check your input");
        }
    }
    public String resign() throws IOException {
        if(isBlack || isObserver || isWhite) {
            try {
                ws.resignGame(auth.authToken(), gameID);

            } catch (Exception e) {
                return "Resign Error";
            }
            return "You resigned";
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "Not in a game");
        }
    }
    public String move(String... params) throws IOException {
        if ((isWhite || isBlack) && !isObserver){
            ChessPosition startPosition;
            ChessPosition endPosition;
            ChessMove move;
            try{
                if(params.length == 2){
                    char a = params[0].charAt(0);
                    String i = params[0].substring(1,2);
                    int startCol = params[0].charAt(0) - 'a';
                    int startRow = Integer.parseInt(params[0].substring(1,2));
                    startPosition = new ChessPosition(startRow , startCol + 1);
                    int endCol = params[1].charAt(0) - 'a';
                    int endRow = Integer.parseInt(params[1].substring(1,2));
                    endPosition = new ChessPosition(endRow , endCol + 1);
                    move = new ChessMove(startPosition, endPosition, null);
                    ws.makeGameMove(auth.authToken(), gameID, move);

                    return String.format("%s is trying to move ", username);
                }else{
                    //error of command
                    return "Wrong";
                }
            }catch (Exception e){
                return "Error";
            }
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "You're an observer.");
        }

    }

    public String leave() throws IOException {
        if (isWhite || isBlack || isObserver){
            try{
                ws.leaveGame(auth.authToken(), gameID);
                if(isWhite){
                    isWhite = false;
                }else{
                    isBlack = false;
                }
                if(isObserver){
                    isObserver = false;
                }
                return String.format("%s left the chessGame.", username);
            }catch (Exception e){
                throw new IOException(SET_TEXT_COLOR_RED+ "Failed to leave the game");
            }
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "You're not in a game");
        }

    }
    public String logIn(String... params) throws IOException {
        if (params.length == 2 && state != State.SIGNEDIN) {
            username = params[0];
            password = params[1];
            UserData user = new UserData(username, password, null);

            auth = server.getUser(user);
            state = State.SIGNEDIN;
            return String.format("Logged in as %s", username);
        }
        throw new IOException(SET_TEXT_COLOR_RED+ "Login Error. Check your input or logout first");
    }

    public String register(String... params) throws IOException  {
        if (params.length == 3 && state != State.SIGNEDIN){
            try{
                username = params[0];
                UserData user = new UserData(params[0], params[1], params[2]);
                auth = server.addUser(user);
                state = State.SIGNEDIN;
                return String.format("Logged in as %s", params[0]);
            }catch (Exception ex){
                throw new IOException(SET_TEXT_COLOR_RED+ "Registration Error. Please check your input");
            }

        }
        throw new IOException(SET_TEXT_COLOR_RED+ "Registration Error. Please check your input");
    }

    public String logOut() throws IOException {
        assertSignedIn();
        if (!isWhite && !isBlack && !isObserver){
            try {
                server.deleteUser(auth);
                state = State.SIGNEDOUT;
                auth = null;
                return String.format("%s is signed out", username);
            }catch (Exception ex) {
                throw new IOException(SET_TEXT_COLOR_RED+ "Logout Error. Please check your input");
            }
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "Logout Error. Please leave the game first");
        }

    }

    public String createGame(String... params) throws IOException {
        assertSignedIn();
        if(params.length == 1 && !isWhite && !isBlack && !isObserver) {
            try{
                String gameName = params[0];
                GameData game = new GameData(0, null, null, gameName, null);
                CreateGameResponse res = server.createGame(auth, game);
                return String.format("You created a chessGame with ID: %d", res.getGameID());
            }catch (Exception ex) {
                throw new IOException(SET_TEXT_COLOR_RED+ "Error. CreateGame Wrong input");
            }
        }else {
            throw new IOException(SET_TEXT_COLOR_RED+ "Error. CreateGame Wrong input or already in a game");
        }
    }

    public String listGames() throws IOException {
        assertSignedIn();
        if (!isWhite && !isBlack && !isObserver) {
            ListGameResponse list=server.listGames(auth);

            return list.getGamesString();
        }else{
            throw new IOException(SET_TEXT_COLOR_RED+ "You are in a game. Leave the game first");
        }
    }

    public String joinGame(String... params) throws IOException {
        assertSignedIn();
        if(!isWhite && !isBlack && !isObserver){
            try{
                gameID = Integer.parseInt(params[0]);
                playerColor = params.length == 1 ? null : params[1];
                JoinRequest req = new JoinRequest(playerColor, gameID);
                RegisterRes res = server.joinGame(auth, req);
                if(res.getMessage() != null){
                    return res.getMessage();
                }else{
                    if(params.length == 1){
                        isObserver = true;
                    }else if(params.length == 2) {
                        if(playerColor.equalsIgnoreCase("white")){
                            isWhite = true;
                        }else if(playerColor.equalsIgnoreCase("black")) {
                            isBlack = true;
                        }
                    }
                    String color = "";
                    if (isWhite && !isBlack && !isObserver){
                        color = "White";
                        ws.joinGameAsPlayer(auth.authToken(), gameID, color);
                    }else if (!isWhite && isBlack && !isObserver){
                        color = "Black";
                        ws.joinGameAsPlayer(auth.authToken(), gameID, color);
                    }else if (isObserver) {
                        color = "an Observer";
                        ws.joinGameAsObserver(auth.authToken(), gameID);
                    }
                    return String.format("You connected to the chessGame with ID %d as '%s.'", gameID, color);
                }
            }catch(Exception ex){
                throw new IOException("Failed to join the chessGame. Check your input");
            }
        }else {
            throw new IOException("Failed to join the chessGame. Leave the game first");
        }


    }

    public String clear() throws IOException {
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
        }else if (state == State.SIGNEDIN && !isObserver && !isBlack && !isWhite){
            return """
                create <NAME> - a chessGame
                list - games
                join <ID> [WHITE|BLACK|<emtpy>] - a chessGame
                observe <ID> - a chessGame
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
        }else if (state == State.SIGNEDIN && !isObserver && (isWhite || isBlack)) {
            return """
                redraw - the board
                leave - the chessGame
                move <beforeMove> <afterMove> - a piece
                resign <ID> - a chessGame
                highlight <position> - legal moves
                help - with possible commands
                """;
        }else if (state == State.SIGNEDIN && isObserver) {
            return """
                redraw - the board
                leave - the chessGame
                help - with possible commands
                """;
        }
        return "Error: printing help()";
    }

    public String error() {
        StringBuilder sb = new StringBuilder();
        sb.append(SET_TEXT_COLOR_RED + "Command Error. Please type it again" + "\n" + SET_TEXT_COLOR_BLUE).append(help());
        return sb.toString();
    }

    private void assertSignedIn() throws IOException {
        if (state == State.SIGNEDOUT) {
            throw new IOException("You must sign in first");
        }
    }
}
