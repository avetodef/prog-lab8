package server;

import commands.ACommands;
import commands.CommandSaver;
import dao.DataBaseDAO;
import dao.RouteDAO;
import json.JsonConverter;
import utils.RouteInfo;

import java.io.DataOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveTask;

public class RequestProcessor extends RecursiveTask<String>{

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

        ACommands command = CommandSaver.getCommand(JsonConverter.des(msg).getArgs());
//        RouteInfo info = JsonConverter.des(msg).getInfo();
        //command.setInfo(info);
        command.setUser(JsonConverter.des(msg).getUser());
        //System.out.println("command " + command);
        this.fixedThreadPool.execute(new ResponseSender(dataOutputStream, command.execute(dao, dataBaseDAO)));
        return null;
    }




}
