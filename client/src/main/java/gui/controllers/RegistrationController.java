package gui.controllers;

import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    private Text warning_text = new Text();

    @FXML
    public String getTextFromUsernameField(){
        return username_field.getText().trim();
    }

    @FXML
    public String getTextFromPasswordField(){
        System.out.println(password_field.getText());
        return password_field.getText().trim();
    }

    @FXML
    public void displayWarning(String s){
        warning_text.setText(s);
    }


    public User submitUser(){
        return new User(username_field.getText().trim(), password_field.getText().trim());
    }

    public void goFurther(ActionEvent actionEvent) throws IOException {
        switchStages(actionEvent, "actionChoice.fxml");
    }


    public void goBack(ActionEvent actionEvent){
        try{
            switchStages(actionEvent, "auth.fxml");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
