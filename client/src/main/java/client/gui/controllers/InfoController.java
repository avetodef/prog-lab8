package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    @FXML
    private void go_back(ActionEvent event) {
        pane.getScene().getWindow().hide();
    }
}
