package client.gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class AnimationWindowController extends AbstractController {
    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "actionChoice.fxml");
    }
}
