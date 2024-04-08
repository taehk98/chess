import Client.Repl;
import Client.ServerFacade;
import chess.*;
//import server.Server;
import ui.ChessBoardPrinter;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
//    private static Server server;
    static ServerFacade facade;
    public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException {
//        server = new Server();
//        var port = server.run(0);
        var port = 49155;
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        facade.clear();

        new Repl(serverUrl).run();
    }
}