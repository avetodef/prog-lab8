package server;

import commands.AutoUpdate;
import commands.Show;
import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private final Lock locker = new ReentrantLock();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {

        try {

            InputStream socketInputStream;
            OutputStream socketOutputStream;
            DataOutputStream dataOutputStream;

            Response errorResponse = new Response(null, Status.SERVER_ERROR);

            socketInputStream = clientSocket.getInputStream();
            socketOutputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(socketOutputStream);

            DataBaseDAO dbDAO = new DataBaseDAO(dataOutputStream);
            RouteDAO dao = dbDAO.getDAO();

            Show show = new Show();
            //show.execute(dao, dbDAO);

            while (true) {

                try {

                    locker.lock();
                    dao = this.fixedThreadPool.submit(new AutoUpdate()).get();
                    System.out.println("new dao created...");
                    String clientMessage = this.fixedThreadPool.submit(new RequestReader(socketInputStream, forkJoinPool, dao, dbDAO, fixedThreadPool, dataOutputStream)).get();
                    System.out.println(clientMessage);
                    locker.unlock();

                } catch (NullPointerException e) {
                    errorResponse.setMsg("Введённой вами команды не существует. Попробуйте ввести другую команду.");

                    this.fixedThreadPool.execute(new ResponseSender(dataOutputStream, errorResponse));

                } catch (NoSuchElementException e) {
                    //throw new ExitException("кнтрл д момент...");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}