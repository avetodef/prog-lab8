package server;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
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
        try {
            String requestJson = read();
            System.out.println("REQUEST " + requestJson);

            this.forkJoinPool.invoke(new RequestProcessor(requestJson, routeDAO, dataBaseDAO, fixedThreadPool, dataOutputStream));

            return "executed";
        } catch (NullPointerException e) {
            return ("stalo pusto v dushe i v request'e: " + e.getMessage());
        }
    }




    private String read() {
        System.out.println("read method invoked...");
        try {

            StringBuilder builder = new StringBuilder();

            int byteRead;
            System.out.println("builder and byteRead created... ");
            while ((byteRead = socketInputStream.read()) != -1) {

                if (byteRead == 0) break;
                builder.append((char) byteRead);
            }
            System.out.println("reading ended");

            return builder.toString();

        } catch (IOException e) {
            System.err.println("client died");
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
