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

import javax.lang.model.util.AbstractAnnotationValueVisitor6;
import java.io.IOException;
import java.net.ConnectException;
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

//    protected void connect(SocketChannel client) {
//        if (client.isConnectionPending()) {
//            try {
//                client.finishConnect();
//                System.out.println("connection established");
//            } catch (IOException e) {
//                System.out.println("no connection to server");
//            }
//        }
//    }

    {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            if (socketChannel.isConnected())
                System.out.println("connection established");
            //connect(socketChannel);
        }
//        catch (ConnectException e){
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/server_die.fxml"));
//                Scene scene = new Scene(fxmlLoader.load());
//                Stage stage = new Stage();
//                stage.setScene(scene);
//                stage.setResizable(false);
//                stage.show();
//
//            }
//            catch (IOException e1){
//                System.out.println(e1.getMessage() );
//            }
//        }
        catch (IOException e) {
            //e.printStackTrace();
            System.out.println("abstrc 72 " + e.getMessage());
        }
    }

    public void submit(ActionEvent actionEvent){}

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
