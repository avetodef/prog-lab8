package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ServerDiedController extends AbstractController {
    @FXML
    private Button update;
    @FXML
    private Label label;
    @FXML
    private AnchorPane pane;

    @FXML
    private void reconnect() {

        pane.getScene().getWindow().hide();

//        try {
//            readerSender.socketChannel.connect(new InetSocketAddress("localhost", 6666));
//            if (readerSender.socketChannel.isConnected()) {
//                System.out.println("connected! ура блять наконец тщ");
//
//            } else
//                System.out.println("not yet connected...");
//        } catch (IOException e) {
//            System.out.println("cannot connect right now... try again later ... свеча роза");
//            System.out.println(e + " " + e.getMessage());
//            e.printStackTrace();
//        }

    }
}
