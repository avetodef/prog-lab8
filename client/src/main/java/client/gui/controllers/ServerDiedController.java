package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ServerDiedController extends AbstractController {
    @FXML
    private Button update;
    @FXML
    private Label label;
    @FXML
    private AnchorPane pane;

    @FXML
    private void reconnect() {

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

        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            if (socketChannel.isConnectionPending())
                try {
                    socketChannel.finishConnect();
                    label.setText("Сервер ожил");
                } catch (IOException e) {
                    label.setText("Сервер все еще мертв");
                    System.out.println("no connection to server");
                }
            if(socketChannel.isConnected()) {
                readerSender.setSocketChannel(socketChannel);
                pane.getScene().getWindow().hide();
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }


    }
}
