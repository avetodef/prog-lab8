package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class StartingStageController extends AbstractController {
    @FXML
    protected Label label;


    public void switchToRegistration(ActionEvent actionEvent){
        try {
            switchStages(actionEvent, "registration.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToIdAuth(ActionEvent actionEvent){
        try {
            switchStages(actionEvent, "idAuth.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToUnPassw(ActionEvent actionEvent){
        try {
            switchStages(actionEvent, "usernamePasswordAuth.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
