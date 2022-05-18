package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class TableWindowController extends AbstractController{
    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "actionChoice.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void goToRedactorMode(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "redactorMode.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
