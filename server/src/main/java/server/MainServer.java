package server;

import java.io.IOException;

public class MainServer {

    public static void main(String[] args) {
        ServerApp server = new ServerApp();
        try {
            server.mainServerLoop();
        } catch (IOException e) {
            System.out.println(" io moment");
        }
    }

}
