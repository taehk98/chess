package Client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final Client client;
    private String state = "[LOGGED_OUT]";

    public Repl (String serverUrl) {
        client = new Client(serverUrl);
    }

    public void run () {
        System.out.println(WHITE_KING + "Welcome to 240 chess. Type Help to get started." + WHITE_KING);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                var tokens = result.toLowerCase().split(" ");
                if(tokens[0].equals("Logged") && tokens[1].equals("in")) {
                    state = "[LOGGED_IN]";
                }
                if(tokens[0].equals("Logged") && tokens[1].equals("out")) {
                    state = "[LOGGED_OUT]";
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
        System.out.print("\n" + state + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
