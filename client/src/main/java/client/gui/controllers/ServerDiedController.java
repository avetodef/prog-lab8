package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDiedController extends AbstractController {
    @FXML
    private Button update;
    @FXML
    private Label label;
    @FXML
    private AnchorPane pane;

    @FXML
    private void reconnect() {
//        long limit = 5000;
//        LocalDateTime time = LocalDateTime.now();
        //readerSender.reconnect();
        //readerSender.read();
        //synchronized (socketChannel) {

//            try {
//                readerSender.reconnect();
//            } catch (Exception e) {
//                System.out.println(e + " " + e.getMessage());
//                //e.printStackTrace();
//            }
        Lock lock = new ReentrantLock();
        synchronized (readerSender.socketChannel) {
        try {
            readerSender.socketChannel.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {

                //lock.lock();
                //lock.lockInterruptibly();
                readerSender.socketChannel.connect(new InetSocketAddress("localhost", 6666));
                if (readerSender.socketChannel.isConnected()) {
                    System.out.println("connected! ура блять наконец тщ");

                } else
                    System.out.println("not yet connected...");
                //readerSender.socketChannel.notifyAll();
                //lock.unlock();
            }

            catch (IOException  e) {
                //exception.printStackTrace();
                System.out.println("cannot connect right now... try again later ... свеча роза");
                System.out.println(e + " " + e.getMessage());
                //e.printStackTrace();
            }
        }
//            if (!readerSender.socketChannel.isConnected()) {
//                //time = LocalDateTime.now();
//                label.setText("жди...");
//            } else {
//                //if (time.getSecond() == limit)
//                pane.getScene().getWindow().hide();
//            }
        //}


//        try{
//            System.out.println("creating socket channel...");
//            SocketChannel channel = SocketChannel.open();
//            channel.configureBlocking(true);
//            System.out.println("connecting...");
//            channel.connect(new InetSocketAddress("localhost", serverPort));
//            if(channel.isConnectionPending()) {
//                channel.finishConnect();
//                readerSender.setSocketChannel(channel);
//            }
//            if(channel.isConnected())
//                pane.getScene().getWindow().hide();
//        }
//        catch (IOException e){
//            System.out.println("SERVER DIED CONTR " + e.getMessage());
//        }

//        try{
//            socketChannel = SocketChannel.open();
//            socketChannel.configureBlocking(true);
//            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
//            if (socketChannel.isConnectionPending())
//                try {
//                    socketChannel.finishConnect();
//                    label.setText("Сервер ожил");
//                } catch (IOException e) {
//                    label.setText("Сервер все еще мертв");
//                    System.out.println("no connection to server");
//                }
//            if(socketChannel.isConnected()) {
//                readerSender.setSocketChannel(socketChannel);
//                pane.getScene().getWindow().hide();
//            }
//        }
//        catch (IOException e){
//            System.out.println(e.getMessage());
//        }


    }
}
