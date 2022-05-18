package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class ActionChoiceController extends AbstractController{

    public void switchToCommandWindow(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "commandWindow.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void switchToAnimationWindow(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "animationWindow.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void switchToTableWindow(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "tableWindow.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
