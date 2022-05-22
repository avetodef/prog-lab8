package client.gui.controllers;

import client.ReaderSender;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import json.PasswordHandler;

import java.net.URL;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.ResourceBundle;


public class AuthController extends AbstractController implements Initializable {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Text username_warning_text;
    @FXML
    private Text password_warning_text;

    public void sign_up(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/registration.fxml");
    }


    public User getUserFromAuthWindow() {
        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
        return new User(username, PasswordHandler.encode(password));
    }

    ReaderSender readerSender = new ReaderSender(socketChannel);

    public void sendDataToServer(User user) {
        try {
            client.register(selector, SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        System.out.println(key.isWritable());
        if (key.isWritable()) {
            try {
                List<String> arguments = List.of("authorization");
                Request userRequest = new Request(arguments, null, user);
                readerSender.setUser(user);
                readerSender.sendToServer(userRequest);
                System.out.println("sending data to server... " + userRequest);
                client.register(selector, SelectionKey.OP_READ);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processServerResponse(ActionEvent actionEvent) {
        if (key.isReadable()) {
            Response response = readerSender.read();
            System.out.println(response.status + " [" + response.msg + "]");

            if (response.status.equals(Status.OK)) {

                switchStages(actionEvent, "/client/actionChoice.fxml");
            } else {
                if (response.status.equals(Status.PASSWORD_ERROR)) {
                    password_warning_text.setText(response.msg);
                }
                if (response.status.equals(Status.USERNAME_ERROR))
                    username_warning_text.setText(response.msg);
            }

//            try {
//                client.register(selector, SelectionKey.OP_);
//            } catch (ClosedChannelException e) {
//                e.printStackTrace();
//            }
        }
    }


    @FXML
    private void submit(ActionEvent actionEvent) {
        username_warning_text.setText("");
        password_warning_text.setText("");

        User user = getUserFromAuthWindow();
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()
                && !user.getPassword().equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) { //если что страшилка это то как кодирует пустое поле сша 256
            sendDataToServer(user);

            processServerResponse(actionEvent);
            System.out.println("setting user...");
            readerSender.setUser(user);
            try {
                client.register(selector, SelectionKey.OP_WRITE);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }

        } else {
            if (user.getUsername().isEmpty()) {
                System.out.println("NAME IS EMPTY");
                username_warning_text.setText("пустое имя");
            }
            if (password_field.getText().isEmpty()) {
                System.out.println("PASSWORD IS EMPTY");
                password_warning_text.setText("пустой пароль");
            }
        }
    }


    @FXML
    protected ChoiceBox<String> languageChoice;

    private final String[] availableLanguages = {"Русский", "Slovenščina",
            "Український", "Español (República Dominicana)"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.setValue("Choose your language");
        languageChoice.getItems().addAll(availableLanguages);

    }

}
