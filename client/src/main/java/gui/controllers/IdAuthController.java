package gui.controllers;

import dao.DataBaseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class IdAuthController extends AbstractController {

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
