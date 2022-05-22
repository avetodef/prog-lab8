package client.gui.controllers;

import javafx.event.ActionEvent;


public class ActionChoiceController extends AbstractController {

    public void switchToCommandWindow(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/commandWindow.fxml");
    }
    public void switchToAnimationWindow(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/animationWindow.fxml");
    }
    public void switchToTableWindow(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/tableWindow.fxml");
    }
    public void log_out(ActionEvent actionEvent){
        switchStages(actionEvent, "/client/auth.fxml");
    }

}
