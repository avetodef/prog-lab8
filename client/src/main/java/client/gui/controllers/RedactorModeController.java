package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RedactorModeController extends AbstractController {
    @FXML
    private Label label;
    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/tableWindow.fxml");
    }

}
