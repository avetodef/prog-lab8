package server;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
//            Request request = JsonConverter.des(requestJson);
//
//            if (request.getArgs().contains("authorization")) {
//                newUser = notAFirstTime(dataBaseDAO, request, dataOutputStream);
//                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
//                request.setUser(newUser);
//            }
//
//            if (request.getArgs().contains("registration")) {
//                newUser = aNewUser(requestJson);
//                newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
//                request.setUser(newUser);
//            }
//
//            newUser = JsonConverter.des(requestJson).getUser();
//            newUser.setId(dataBaseDAO.getUserID(newUser.getUsername()));
//            request.setUser(newUser);

            System.out.println("REQUEST " + requestJson);

            this.forkJoinPool.invoke(new RequestProcessor(requestJson, routeDAO, dataBaseDAO, fixedThreadPool, dataOutputStream));

            return "executed";
        } catch (NullPointerException e) {
            return ("stalo pusto v dushe i v request'e: " + e.getMessage());
        }
//        catch (IOException exception) {
//            exception.printStackTrace();
//        }
        //return "executed";
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
            System.err.println(" client die. server kill? {yes/no} ");
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
