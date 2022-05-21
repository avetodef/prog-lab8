package server;

import dao.DataBaseDAO;
import dao.RouteDAO;
import exceptions.ExitException;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import json.JsonConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;


public class RequestReader implements Callable<String> {

    private final InputStream socketInputStream;
    private final ForkJoinPool forkJoinPool;
    private RouteDAO routeDAO;
    private DataBaseDAO dataBaseDAO;
    private ExecutorService fixedThreadPool;
    private DataOutputStream dataOutputStream;

    public RequestReader(InputStream socketInputStream, ForkJoinPool forkJoinPool, RouteDAO routeDAO, DataBaseDAO dataBaseDAO, ExecutorService fixedThreadPool, DataOutputStream dataOutputStream) {
        this.socketInputStream = socketInputStream;
        this.forkJoinPool = forkJoinPool;
        this.routeDAO = routeDAO;
        this.dataBaseDAO = dataBaseDAO;
        this.fixedThreadPool = fixedThreadPool;
        this.dataOutputStream = dataOutputStream;

    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */

    @Override
    public String call() {
        User newUser;
        try {

            String requestJson = read();
            Request request = JsonConverter.des(requestJson);

            if (request.getArgs().contains("authorization")) {
                newUser = notAFirstTime(dataBaseDAO, request, dataOutputStream);
                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
                request.setUser(newUser);
            }

            if (request.getArgs().contains("registration")) {
                newUser = aNewUser(requestJson);
                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
                request.setUser(newUser);
            }

            newUser = JsonConverter.des(requestJson).getUser();
            newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
            request.setUser(newUser);

            this.forkJoinPool.invoke(new RequestProcessor(requestJson, routeDAO, dataBaseDAO, fixedThreadPool, dataOutputStream));

            return "executed";
        } catch (NullPointerException e) {
            return ("stalo pusto v dushe i v request'e: " + e.getMessage());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "executed";
    }


    private User aNewUser(String requestJson) {
        try {
            if (dataBaseDAO.checkUsername(JsonConverter.des(requestJson).getUser().getUsername())) {
                Response authErrorResponse = new Response("имя занято", Status.USERNAME_ERROR);
                dataOutputStream.writeUTF(JsonConverter.serResponse(authErrorResponse));
            } else {
                User user = JsonConverter.des(requestJson).getUser();
                dataBaseDAO.insertUser(user);
                Response successAuth = new Response("auth complete.", Status.OK);
                dataOutputStream.writeUTF(JsonConverter.serResponse(successAuth));
                return user;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new User();
    }

    public User notAFirstTime(DataBaseDAO dbDAO, Request request, DataOutputStream dataOutputStream) throws IOException {
        User user;
        if (dbDAO.checkUsername(request.getUser().getUsername())) {

            if (!dbDAO.checkPassword(request.getUser().getPassword())) {
                Response error = new Response("пароль неверный", Status.PASSWORD_ERROR);
                dataOutputStream.writeUTF(JsonConverter.serResponse(error));

            } else {
                int id = dbDAO.getUserID(request.getUser().getUsername());
                user = new User(request.getUser().getUsername(), request.getUser().getPassword(), id);
                Response ok = new Response("authorized. good job", Status.OK);
                dataOutputStream.writeUTF(JsonConverter.serResponse(ok));
                return user;
            }
        } else {
            Response error = new Response("нет такого имени пользователя", Status.USERNAME_ERROR);
            dataOutputStream.writeUTF(JsonConverter.serResponse(error));

        }
        return new User();
    }

    private String read() {
        try {
            StringBuilder builder = new StringBuilder();

            int byteRead;

            while ((byteRead = socketInputStream.read()) != -1) {

                if (byteRead == 0) break;

                builder.append((char) byteRead);

            }

            return builder.toString();
        } catch (IOException e) { //TODO mb socket exception все так
            System.err.println("client die. server kill? {yes/no}");
            Scanner sc = new Scanner(System.in);
            String answer;

            while (!(answer = sc.nextLine()).equals("no")) {
                switch (answer) {
                    case "":
                        break;
                    case "yes":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("скажи пожалуйста.... yes или no");
                }
            }
            System.out.println("жди...");

        }
        return null;
    }
}
