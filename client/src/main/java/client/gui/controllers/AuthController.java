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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.ResourceBundle;


public class AuthController extends AbstractController implements Initializable {

    public void sign_up(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/registration.fxml");
    }

    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Text username_warning_text;
    @FXML
    private Text password_warning_text;


    public User getUserFromAuthWindow(ActionEvent actionEvent) {

        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
        System.out.println("USER: " + new User (username, password)
                + " IS_PASSWORD_EMPTY " + username_field.getText().isEmpty());

        return new User(username, PasswordHandler.encode(password) );
    }

    public SocketChannel socketChannel;


    int serverPort = 6666;

    {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            connect(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ReaderSender readerSender = new ReaderSender(socketChannel);

    public void sendDataToServer(User user) {
        try {
            List<String> arguments = List.of("authorization");
            Request userRequest = new Request(arguments, user);
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
        if(!user.getUsername().isEmpty() && !user.getPassword().isEmpty()
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
        }
    }


    public void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                System.out.println("connection established");
            } catch (IOException e) {
                System.out.println("no connection to server");
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