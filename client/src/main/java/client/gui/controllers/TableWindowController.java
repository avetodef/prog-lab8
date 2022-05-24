package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class TableWindowController extends AbstractController implements Initializable {


    @FXML
    public void go_back(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice.fxml");
    }

    @FXML
    public void log_out(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/auth.fxml");
    }

    @FXML
    private Text username;


    @FXML
    private void add_element() {
        popUpWindow("/client/add_element.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(readerSender.user.getUsername());
    }
}
