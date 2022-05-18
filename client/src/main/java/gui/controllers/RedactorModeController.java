package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class RedactorModeController extends AbstractController {
    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "tableWindow.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
