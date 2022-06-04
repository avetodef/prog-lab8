package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InfoController extends AbstractController {

    @FXML
    public TextField id;
    @FXML
    public TextField coords;
    @FXML
    public TextField from;
    @FXML
    public TextField to;
    @FXML
    public TextField distance;
    @FXML
    public TextField date;
    @FXML
    public TextField username;
    @FXML
    private AnchorPane pane;

    @FXML
    private void go_back(ActionEvent event) {
        pane.getScene().getWindow().hide();
    }

    @Override
    protected void localize() {

    }
}
