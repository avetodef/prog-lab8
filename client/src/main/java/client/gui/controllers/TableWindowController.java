package client.gui.controllers;

import javafx.event.ActionEvent;


public class TableWindowController extends AbstractController {
    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "actionChoice.fxml");
    }

    public void goToRedactorMode(ActionEvent actionEvent) {
        switchStages(actionEvent, "redactorMode.fxml");
    }
}
