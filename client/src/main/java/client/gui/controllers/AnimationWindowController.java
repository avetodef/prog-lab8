package client.gui.controllers;

import javafx.event.ActionEvent;


public class AnimationWindowController extends AbstractController {
    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice.fxml");
    }
    public void log_out(ActionEvent actionEvent){
        switchStages(actionEvent, "/client/auth.fxml");
    }
}
