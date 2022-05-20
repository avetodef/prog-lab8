package gui.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class AuthController extends AbstractController{

    public void goFurther(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "actionChoice.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void signUp(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "registration.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
