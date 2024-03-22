package Client;

import chess.ChessGame;
import ui.ChessBoardPrinter;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final Client client;
    private String state = "[LOGGED_OUT]";

    public Repl (String serverUrl) {
        client = new Client(serverUrl);
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
                    if (parts[1].equals("connected")) {
                        boolean isWhite = parts[9].equalsIgnoreCase("white")
                                || parts[9].equalsIgnoreCase("an");
                        ChessBoardPrinter printer = new ChessBoardPrinter();
                        printer.printChessBoard(new ChessGame(), isWhite);
                    }
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print(SET_TEXT_COLOR_WHITE + "\n" + state + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
