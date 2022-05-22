package client.gui.controllers;

import client.ReaderSender;
import client.gui.StartingStage;
import interaction.Request;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public abstract class AbstractController {

    public void exit(ActionEvent actionEvent) {
        switchStages(actionEvent, "auth.fxml");
    }


    public SocketChannel socketChannel;


    int serverPort = 6666;

    protected void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                System.out.println("connection established");
                client.register(selector, SelectionKey.OP_WRITE);
            } catch (IOException e) {
                System.out.println("no connection to server");
            }
        }
    }

    Selector selector;
    {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            //connect(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SelectionKey key;
    public SocketChannel client;
    {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        key = iterator.next();
        iterator.remove();
        client = (SocketChannel) key.channel();
        if (key.isConnectable()) {
            connect(client);
            try {
                client.register(selector, SelectionKey.OP_WRITE);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }

    }


    ReaderSender readerSender = new ReaderSender(socketChannel);

    public void popUpWindow(String path){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(path));
            Scene popupScene = new Scene(fxmlLoader.load());
            Stage popupstage = new Stage();
            popupstage.setScene(popupScene);
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    void switchStages(javafx.event.ActionEvent actionEvent, String switchingPoint) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(switchingPoint));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
