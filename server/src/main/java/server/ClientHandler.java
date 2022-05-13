package server;

import commands.AutoUpdate;
import console.ConsoleOutputer;
import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ConsoleOutputer output = new ConsoleOutputer();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private final Lock locker = new ReentrantLock();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        DataBaseDAO dbDAO = new DataBaseDAO();
        RouteDAO dao = dbDAO.getDAO();
        long initTime = System.currentTimeMillis();


        InputStream socketInputStream;
        OutputStream socketOutputStream;
        DataOutputStream dataOutputStream;

        Response errorResponse = new Response(null, Status.SERVER_ERROR);

        try {
            socketInputStream = clientSocket.getInputStream();
            socketOutputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(socketOutputStream);

            while (true) {

                try {
                    output.printWhite("готов принимать запросы от клиента");

                    locker.lock();
                    dao = this.fixedThreadPool.submit(new AutoUpdate()).get();
                    String clientMessage = this.fixedThreadPool.submit(new RequestReader(socketInputStream, forkJoinPool, dao, dbDAO, fixedThreadPool, dataOutputStream, clientSocket)).get();
                    locker.unlock();

                } catch (NullPointerException e) {
                    errorResponse.setMsg("Введённой вами команды не существует. Попробуйте ввести другую команду.");

                    this.fixedThreadPool.execute(new ResponseSender(dataOutputStream, errorResponse));

                } catch (NoSuchElementException e) {
                    //throw new ExitException("кнтрл д момент...");
                } catch (Exception e) {
                    System.out.println(" ");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

