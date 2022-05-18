package gui.controllers;

import dao.DataBaseDAO;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.PasswordHandler;

import java.io.IOException;

public class RegistrationController extends AbstractController{
    @FXML
    private TextField username_field;
    @FXML
    private TextField password_field;
    @FXML
    private Label label;
    @FXML
    private Button submit;
    @FXML
    private Text warning_text;


    public void newUser(ActionEvent actionEvent){

        String name = username_field.getText().trim();
        String pssw = password_field.getText().trim();

        if (pssw.isEmpty()||name.isEmpty()){
            warning_text.setText("имя или пароль не могут быть пустыми");
        }
        //TODO проверку с бд перенести на сервер
        else {
            try {
                switchStages(actionEvent, "actionChoice.fxml");
            } catch (IOException e) {
                label.setText(e.getMessage());
            }
        }
    }

    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "startingScene.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
