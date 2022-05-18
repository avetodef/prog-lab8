package server;

import commands.AutoUpdate;
import console.ConsoleOutputer;
import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import json.JsonConverter;
import json.PasswordHandler;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private final ConsoleOutputer output = new ConsoleOutputer();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private final Lock locker = new ReentrantLock();
    private int id;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {

        DataBaseDAO dbDAO = new DataBaseDAO();
//        RouteDAO dao = dbDAO.getDAO();

        InputStream socketInputStream;
        OutputStream socketOutputStream;
        DataOutputStream dataOutputStream;
        InputStream inputStream;

        Response errorResponse = new Response(null, Status.SERVER_ERROR);

        try {
            socketInputStream = clientSocket.getInputStream();
            socketOutputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(socketOutputStream);
            inputStream = new DataInputStream(socketInputStream);
            RouteDAO dao = dbDAO.getDAO();

            while (true) {

                try {
                    //output.printWhite("готов принимать запросы от клиента");
//                    System.out.println("is auth " + isAuth +" "+ LocalDateTime.now());
                    //else {

                    locker.lock();

                    dao = this.fixedThreadPool.submit(new AutoUpdate()).get();
                    String clientMessage = this.fixedThreadPool.submit(new RequestReader(socketInputStream, forkJoinPool, dao, dbDAO, fixedThreadPool, dataOutputStream, clientSocket)).get();
                    locker.unlock();

                    //}
                    //auth(inputStream, dbDAO, dataOutputStream, socketInputStream);

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

    private void auth(InputStream inputStream, DataBaseDAO dbDAO, DataOutputStream dataOutputStream, InputStream socketInputStream) {

        try {

            String requestJson;
            StringBuilder builder = new StringBuilder();
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                if (byteRead == 0) break;
                builder.append((char) byteRead);
            }
            dbDAO = new DataBaseDAO();
            RouteDAO dao = dbDAO.getDAO();

            requestJson = builder.toString();

            Request request = JsonConverter.des(requestJson);

            if (request.getArgs().contains("new user")) {
                if (request.getArgs().contains("y"))
                    aNewUser(dbDAO, request, dataOutputStream);
                else
                    notAFirstTime(dbDAO, request, dataOutputStream);
            } else {

                //locker.lock();
                dao = this.fixedThreadPool.submit(new AutoUpdate()).get();
                String clientMessage = this.fixedThreadPool.submit(new RequestReader(socketInputStream, forkJoinPool, dao, dbDAO, fixedThreadPool, dataOutputStream, clientSocket)).get();
                //locker.unlock();
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void aNewUser(DataBaseDAO dbDAO, Request request, DataOutputStream dataOutputStream) throws IOException {
        if (dbDAO.checkUsername(request.getUser().getUsername())) {
            Response authErrorResponse = new Response("username already in use", Status.USER_EBLAN_ERROR);
            dataOutputStream.writeUTF(JsonConverter.serResponse(authErrorResponse));

        } else {
            User user = request.getUser();
            user.setId(id);
            dbDAO.insertUser(user);
            Response successAuth = new Response("auth complete. your id: " + dbDAO.getUserID(user.getUsername()), Status.OK);
            dataOutputStream.writeUTF(JsonConverter.serResponse(successAuth));

        }
    }

    public void notAFirstTime(DataBaseDAO dbDAO, Request request, DataOutputStream dataOutputStream) throws IOException {
        if (dbDAO.checkUsername(request.getUser().getUsername())) {

            if (!dbDAO.checkPassword(request.getUser().getPassword())) {
                Response error = new Response("password and username dont match", Status.USER_EBLAN_ERROR);
                dataOutputStream.writeUTF(JsonConverter.serResponse(error));

            } else {
                id = dbDAO.getUserID(request.getUser().getUsername());
                Response ok = new Response("authorized. good job", Status.OK);
                dataOutputStream.writeUTF(JsonConverter.serResponse(ok));
            }
        } else {
            Response error = new Response("no such username", Status.USER_EBLAN_ERROR);
            dataOutputStream.writeUTF(JsonConverter.serResponse(error));

        }
    }

}

