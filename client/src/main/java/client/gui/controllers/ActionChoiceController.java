package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class ActionChoiceController extends AbstractController implements Initializable {
    @FXML
    private Text username;

    @FXML
    private void switchToAnimationWindow(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/animation.fxml");
    }

    @FXML
    private void switchToTableWindow(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/tableWindow.fxml");
    }

    @FXML
    private void log_out(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/auth.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("ты зашел как " + readerSender.user.getUsername());
    }
}
