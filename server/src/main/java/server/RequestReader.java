package server;

import dao.DataBaseDAO;
import dao.RouteDAO;

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
     public String call(){
        try{
            String requestJson;
            StringBuilder builder = new StringBuilder();

            int byteRead;

            while ((byteRead = socketInputStream.read()) != -1) {
                if (byteRead == 0) break;
                builder.append((char) byteRead);
            }
            requestJson = builder.toString();
            //System.out.println("request json " + requestJson);
            this.forkJoinPool.invoke(new RequestProcessor(requestJson, routeDAO, dataBaseDAO, fixedThreadPool, dataOutputStream));
            return "executed";
        }

        catch (SocketException e) {
            System.out.println("клиент лег поспать. жди.");
            while(true){}

        } catch (IOException e) {
            return ("server razuchilsya chitat... wot pochemy: " + e.getMessage());

        } catch (NullPointerException e) {
           return ("stalo pusto v dushe i v request'e: " + e.getMessage());
        }
    }
}
