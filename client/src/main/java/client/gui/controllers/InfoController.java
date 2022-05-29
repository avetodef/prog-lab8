package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import utils.Route;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InfoController extends AbstractController implements Initializable {

    @FXML
    private Text id;
    @FXML
    private Text coords;
    @FXML
    private Text from;
    @FXML
    private Text to;
    @FXML
    private Text distance;
    @FXML
    private Text date;
    @FXML
    private Text username;


    private void requestInfo() {
        Request request = new Request();
        request.setArgs(List.of("routeinfo"));
        readerSender.sendToServer(request);
    }

    private void processResponse() {
        Response response = readerSender.read();

        System.out.println(response.status + " [" + response.msg + "]");
        if (response.status.equals(Status.OK)) {
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
