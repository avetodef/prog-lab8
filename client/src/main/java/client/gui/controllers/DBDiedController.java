package client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DBDiedController extends AbstractController {
    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private AnchorPane pane;

    @FXML
    private void exit() {
        pane.getScene().getWindow().hide();
    }

    @Override
    protected void localize() {

    }
}
