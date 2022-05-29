package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private AnchorPane pane;

    public int routeId;

    private void requestInfo() {
        int id = routeId;
        Request request = new Request();
        request.setArgs(List.of("routeinfo", String.valueOf(id)));
        readerSender.sendToServer(request);
    }

    private void processResponse() {
        Response response = readerSender.read();
        Route route = response.route;
        System.out.println(response.status + " [" + response.msg + "]");
        if (response.status.equals(Status.OK)) {
            id.setText(String.valueOf(route.getId()));
            coords.setText(String.valueOf(route.getCoordinates()));
            to.setText(String.valueOf(route.getTo()));
            from.setText(String.valueOf(route.getFrom()));
            distance.setText(String.valueOf(route.getDistance()));
            date.setText(route.getCreationDate());
            username.setText(route.getUser().getUsername());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setText("");
        coords.setText("");
        to.setText("");
        from.setText("");
        distance.setText("");
        date.setText("");
        username.setText("");

        requestInfo();
        processResponse();
    }


    @FXML
    private void go_back(ActionEvent event) {
        pane.getScene().getWindow().hide();
    }
}
