package server;

import commands.ACommands;
import commands.CommandSaver;
import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import json.JsonConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveTask;

public class RequestProcessor extends RecursiveTask<String> {

    private final String msg;
    private final RouteDAO dao;
    private final DataBaseDAO dataBaseDAO;
    private ExecutorService fixedThreadPool;
    private DataOutputStream dataOutputStream;

    public RequestProcessor(String msg, RouteDAO dao, DataBaseDAO dataBaseDAO, ExecutorService fixedThreadPool, DataOutputStream dataOutputStream) {
        this.msg = msg;
        this.dao = dao;
        this.dataBaseDAO = dataBaseDAO;
        this.fixedThreadPool = fixedThreadPool;
        this.dataOutputStream = dataOutputStream;
    }

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected String compute() {

        //User reserveUser = null;
        Request request = JsonConverter.des(msg);
        User newUser = request.getUser();
        try {
            if (request.getArgs().contains("authorization")) {
                newUser = notAFirstTime(dataBaseDAO, request, dataOutputStream);
                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
                request.setUser(newUser);
                //reserveUser = newUser;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (request.getArgs().contains("registration")) {
            newUser = aNewUser(msg);
            newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
            request.setUser(newUser);
            //reserveUser = newUser;
        }
//        System.out.println("61");
        //newUser = JsonConverter.des(msg).getUser();
//        System.out.println("63 " + newUser);
        newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
//        System.out.println("65");
        request.setUser(newUser);

        System.out.println("REQUEST PROCESSOR REQUEST: " + request);
        if (!request.getArgs().contains("registration") && !request.getArgs().contains("authorization")) {
            System.out.println("processing a command...");
            ACommands command = CommandSaver.getCommand(request.getArgs());
//        RouteInfo info = JsonConverter.des(msg).getInfo();
            //command.setInfo(info);

            command.setUser(request.getUser());
            command.setInfo(request.getInfo());
            //System.out.println("command " + command);
            this.fixedThreadPool.execute(new ResponseSender(dataOutputStream, command.execute(dao, dataBaseDAO)));
        }
        return null;
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


}
