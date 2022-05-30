package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lombok.Setter;
import utils.Route;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InfoController extends AbstractController {

    @FXML
    protected TextField id;
    @FXML
    protected TextField coords;
    @FXML
    protected TextField from;
    @FXML
    protected TextField to;
    @FXML
    protected TextField distance;
    @FXML
    protected TextField date;
    @FXML
    protected TextField username;
    @FXML
    private AnchorPane pane;

    //public int routeId;

    private void requestInfo() {
        int id = routeid;
        Request request = new Request();
        request.setArgs(List.of("routeinfo", String.valueOf(id)));
        readerSender.sendToServer(request);
    }

    @Setter
    private int routeid;

    private void processResponse() {
        id.setText("");
        coords.setText("");
        to.setText("");
        from.setText("");
        distance.setText("");
        date.setText("");
        username.setText("");

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
        if (response.msg.equals("database sleep"))
            readerSender.serverDied();
    }



    @FXML
    private void go_back(ActionEvent event) {
        pane.getScene().getWindow().hide();
    }
}
