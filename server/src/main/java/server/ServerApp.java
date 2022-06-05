package server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import console.ConsoleOutputer;
import interaction.Response;
import interaction.Status;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class ServerApp {

    ConsoleOutputer output = new ConsoleOutputer();

    protected void mainServerLoop() throws IOException {

        Response errorResponse = new Response();
        errorResponse.setStatus(Status.SERVER_ERROR);


        try {

            int port = 666;
            output.printPurple("Ожидаю подключение клиента");

            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (true) {

                try {

                    Socket client = serverSocket.accept();
                    output.printPurple("Клиент подключился ");
                    ClientHandler clientHandler = new ClientHandler(client);
                    output.printWhite("creating a new client handler...");
                    new Thread(clientHandler).start();
                    output.printYellow("new thread started...");

                } catch (SocketException e) {
                    System.err.println("клиент упал. подожди немного");
                }

            }

        } catch (IllegalArgumentException e) {
            System.err.println("ну и зачем менять номер порта");
            System.err.println("исправляй а потом запускай");
        } catch (IOException e) {
            System.out.println("IO troubles " + e.getMessage());
        }
    }


}
