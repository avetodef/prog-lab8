package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class UsernamePasswordAuthController extends AbstractController{

    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "startingScene.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void goFurther(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "actionChoice.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
