package client.gui.controllers;

import client.ReaderSender;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.PasswordHandler;

import java.util.List;

public class RegistrationController extends AbstractController {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Text password_warning_text;
    @FXML
    private Text username_warning_text;
    @FXML
    private PasswordField repeat_password_field;


    public User getUserFromAuthWindow(ActionEvent actionEvent) {

        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
//        System.out.println("USER: " + new User (username, password)
//                + " IS_PASSWORD_EMPTY " + username_field.getText().isEmpty());

        return new User(username, PasswordHandler.encode(password) );
    }

    ReaderSender readerSender = new ReaderSender(new AuthController().socketChannel);

    public void sendDataToServer(User user) {
        try {
            List<String> arguments = List.of("registration");
            Request userRequest = new Request(arguments,null, user);
            readerSender.sendToServer(userRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void processServerResponse(ActionEvent actionEvent) {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");

        if (response.status.equals(Status.OK)) {
            switchStages(actionEvent, "/client/actionChoice.fxml");
            System.out.println("AUTHENTICATION WENT SUCCESSFULLY");
        } else {
            if (response.status.equals(Status.PASSWORD_ERROR)) {
                password_warning_text.setText(response.msg);
            }
            if (response.status.equals(Status.USERNAME_ERROR))
                username_warning_text.setText(response.msg);
        }
    }

    @FXML
    private void submit(ActionEvent actionEvent) {
        username_warning_text.setText("");
        password_warning_text.setText("");

        User user = getUserFromAuthWindow(actionEvent);

        boolean areTheSame = password_field.getText().trim().equals(repeat_password_field.getText().trim());
        if(areTheSame && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()
                && !user.getPassword().equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) { //если что страшилка это то как кодирует пустое поле сша 256
            System.out.println("sending data to server... " + user + user.getUsername().isEmpty() + " " + user.getPassword().isEmpty());
            sendDataToServer(user);
            processServerResponse(actionEvent);
        }
        else {
            if (user.getUsername().isEmpty()) {
                System.out.println("NAME IS EMPTY");
                username_warning_text.setText("пустое имя");
            }
            if (password_field.getText().isEmpty()) {
                System.out.println("PASSWORD IS EMPTY");
                password_warning_text.setText("пустой пароль");
            }
            if(!areTheSame){
                System.out.println("PASSWORD AND REPEAT PASSWORD DO NOT MATCH");
                password_warning_text.setText("пароли не совпадают");
            }
        }
    }


    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/auth.fxml");
    }

}
