package server;

import dao.DataBaseDAO;
import dao.RouteDAO;
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
    private Socket clientSocket;

    public RequestReader(InputStream socketInputStream, ForkJoinPool forkJoinPool, RouteDAO routeDAO, DataBaseDAO dataBaseDAO, ExecutorService fixedThreadPool, DataOutputStream dataOutputStream, Socket clientSocket) {
        this.socketInputStream = socketInputStream;
        this.forkJoinPool = forkJoinPool;
        this.routeDAO = routeDAO;
        this.dataBaseDAO = dataBaseDAO;
        this.fixedThreadPool = fixedThreadPool;
        this.dataOutputStream = dataOutputStream;
        this.clientSocket = clientSocket;
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
            String requestJson;
            StringBuilder builder = new StringBuilder();

            int byteRead;

            while ((byteRead = socketInputStream.read()) != -1) {

                if (byteRead == 0) break;

                builder.append((char) byteRead);

            }

            requestJson = builder.toString();

            if (JsonConverter.des(requestJson).getArgs().contains("new user")) {
                if (JsonConverter.des(requestJson).getArgs().contains("y")) {
                    newUser = aNewUser(requestJson);
                    JsonConverter.des(requestJson).getUser().setId(dataBaseDAO.getUserID(newUser.getUsername()));
                } else {
                    newUser = notAFirstTime(dataBaseDAO, JsonConverter.des(requestJson), dataOutputStream);
                    JsonConverter.des(requestJson).getUser().setId(dataBaseDAO.getUserID(newUser.getUsername()));
                }

            } else {

                newUser = JsonConverter.des(requestJson).getUser();
                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));

                this.forkJoinPool.invoke(new RequestProcessor(requestJson, routeDAO, dataBaseDAO, fixedThreadPool, dataOutputStream));
            }
            return "executed";
        } catch (SocketException e) {
            System.out.println("клиент лег поспать. жди.");
            while (true) {
            }

        } catch (IOException e) {
            return ("server razuchilsya chitat... wot pochemy: " + e.getMessage());

        } catch (NullPointerException e) {
            return ("stalo pusto v dushe i v request'e: " + e.getMessage());
        }
    }

    private User aNewUser(String requestJson) {
        try {
            if (dataBaseDAO.checkUsername(JsonConverter.des(requestJson).getUser().getUsername())) {
                Response authErrorResponse = new Response("username already in use", Status.USER_EBLAN_ERROR);
                dataOutputStream.writeUTF(JsonConverter.serResponse(authErrorResponse));

            } else {
                User user = JsonConverter.des(requestJson).getUser();

                dataBaseDAO.insertUser(user);
                Response successAuth = new Response("auth complete. your id: " + dataBaseDAO.getUserID(user.getUsername()), Status.OK);
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
                Response error = new Response("password and username dont match", Status.USER_EBLAN_ERROR);
                dataOutputStream.writeUTF(JsonConverter.serResponse(error));

            } else {
                int id = dbDAO.getUserID(request.getUser().getUsername());
                user = new User(request.getUser().getUsername(), request.getUser().getPassword(), id);
                Response ok = new Response("authorized. good job", Status.OK);
                dataOutputStream.writeUTF(JsonConverter.serResponse(ok));
                return user;
            }
        } else {
            Response error = new Response("no such username", Status.USER_EBLAN_ERROR);
            dataOutputStream.writeUTF(JsonConverter.serResponse(error));

        }
        return new User();
    }
}
