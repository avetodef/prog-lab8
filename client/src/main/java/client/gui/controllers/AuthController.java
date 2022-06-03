package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import parsing.PasswordHandler;

import java.net.URL;
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
    @FXML
    private Button submit_button;


    @FXML
    private void sign_up(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/registration.fxml");
    }

    private User getUserFromAuthWindow() {
        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
        return new User(username, PasswordHandler.encode(password));
    }


    private void sendDataToServer(User user) {
        try {
            List<String> arguments = List.of("authorization");
            Request userRequest = new Request(arguments, null, user);
            readerSender.setUser(user);
            readerSender.sendToServer(userRequest);
            System.out.println("sending data to server... ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processServerResponse(ActionEvent actionEvent) {

        try {
            Response response = readerSender.read();
            if (response != null) {
                System.out.println(response.status + " [" + response.msg + "]");
                if (response.status.equals(Status.OK)) {

                    switchStages(actionEvent, "/client/actionChoice.fxml");
                } else {
                    if (response.status.equals(Status.PASSWORD_ERROR)) {
                        //password_warning_text.setText(response.msg);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                        alert.setTitle(response.msg);
                        alert.show();
                    }
                    if (response.status.equals(Status.USERNAME_ERROR)) {
                        String title = "тебе букетик через интернетик";
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                        alert.setTitle(title);
                        alert.show();
                    }
                    if (response.msg.equals("database sleep"))
                        readerSender.dbDied();
                }
            } else
                System.out.println("NULL SERVER RESPONSE ");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void submit(ActionEvent actionEvent) {

        username_warning_text.setText("");
        password_warning_text.setText("");

        User user = getUserFromAuthWindow();
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()
                && !user.getPassword().equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) { //если что страшилка это то как кодирует пустое поле сша 256
            sendDataToServer(user);

            processServerResponse(actionEvent);

            readerSender.setUser(user);

        } else {
            if (user.getUsername().isEmpty()) {
                String title = "тебе букетик через интернетик";
                String msg = "пустое имя";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(title);
                alert.show();
            }
            if (password_field.getText().isEmpty()) {
                String title = "тебе букетик через интернетик";
                String msg = "пустой пароль";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(title);
                alert.show();
            }
        }
    }


    @FXML
    private ChoiceBox<String> languageChoice;

    private final String[] availableLanguages = {"Русский", "Slovenščina",
            "Український", "Español (República Dominicana)"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.setValue("Choose your language");
        languageChoice.getItems().addAll(availableLanguages);
    }

}
