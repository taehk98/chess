package Client;

import Client.websocket.NotificationHandler;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.ChessBoardPrinter;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final Client client;
    private ChessGame game;
    private String state = "[LOGGED_OUT]";

    public Repl (String serverUrl) throws DeploymentException, IOException, URISyntaxException {
        client = new Client(serverUrl, this);
    }

    public void run () {
        System.out.println("\n"+ WHITE_KING + "Welcome to 240 chess. Type Help to get started." + WHITE_KING + "\n");
        System.out.print(SET_TEXT_COLOR_BLUE + client.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                String[] parts = result.split(" ");
                if(parts.length > 3) {
                    if (parts[0].equals("Logged") && parts[1].equals("in")) {
                        state = "[LOGGED_IN]";
                    }
                    if (parts[2].equals("signed") && parts[3].equals("out")) {
                        state = "[LOGGED_OUT]";
                    }
                }else if (result.equals("redraw")){
                    ChessBoardPrinter printer = new ChessBoardPrinter();
                    printer.printChessBoard(game, client.getIsWhite(), false, new ChessPosition(1, 1));
                    result = "Redraw the board";
                }else if (parts[0].equals("highlight")){
                    ChessBoardPrinter printer = new ChessBoardPrinter();
                    if (parts.length == 2) {
                        String charString = parts[1];
                        char ch = charString.toCharArray()[0];
                        printer.printChessBoard(game, client.getIsWhite(), true,
                                new ChessPosition(Integer.parseInt("" + charString.toCharArray()[1]), (ch - 'a' + 1)));
                    }
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e){
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print(SET_TEXT_COLOR_WHITE + "\n" + state + " >>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(NotificationMessage notification) {
        System.out.println("\n" + SET_TEXT_COLOR_GREEN + notification.message);
        printPrompt();
    }

    @Override
    public void error(ErrorMessage errorMessage) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + errorMessage.errorMessage);
        printPrompt();
    }

    @Override
    public void loadGame(LoadGameMessage loadGameMessage) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + "Loading Game....");
        ChessBoardPrinter printer = new ChessBoardPrinter();
        printer.printChessBoard(loadGameMessage.game, client.getIsWhite(), false, new ChessPosition(1, 1));

        this.game= loadGameMessage.game;
        System.out.println(SET_BG_COLOR_DARK_GREY);
        printPrompt();
    }
}
