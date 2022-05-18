package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class CommandWindowController extends AbstractController{
    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "actionChoice.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
