package client.gui.controllers;

import client.ReaderSender;
import client.gui.StartingStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public abstract class AbstractController {

    public static SocketChannel socketChannel;
    private static final int serverPort = 6666;

    private static void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                System.out.println("connection established");
            } catch (IOException e) {
                System.out.println("no connection to server");
            }
        }
    }

    static {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            System.out.println("AbstractController.static initializer");
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            System.out.println("AbstractController.static initializer)))))))");
            if (socketChannel.isConnected())
                System.out.println("connection established ABSTR CONTR");

        } catch (IOException e) {
            connect(socketChannel);
        }
    }

    public void submit(ActionEvent actionEvent) {
    }

    public static ReaderSender readerSender = new ReaderSender(socketChannel);

    public void popUpWindow(String path) {
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

    public static void switchStages(javafx.event.ActionEvent actionEvent, String switchingPoint) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(switchingPoint));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InfoController getInfoController(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(path));
            fxmlLoader.load();
            return fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthController getAuthController(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(path));
            fxmlLoader.load();
            return fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
