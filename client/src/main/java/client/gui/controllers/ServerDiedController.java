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
    }

    @Override
    protected void localize() {

    }
}
