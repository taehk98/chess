import Client.Repl;
import Client.ServerFacade;
import chess.*;
import dataAccess.DataAccessException;
import server.Server;
import ui.ChessBoardPrinter;

public class Main {
    private static Server server;
    static ServerFacade facade;
    public static void main(String[] args) throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        facade.clear();

        new Repl(serverUrl).run();
//        ChessBoardPrinter printer = new ChessBoardPrinter();
//        printer.printChessBoard(new ChessGame(), true);
//        printer.printChessBoard(new ChessGame(), false);
    }
}