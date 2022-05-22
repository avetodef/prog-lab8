package client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class TableWindowController extends AbstractController {
    public void goBack(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice.fxml");
    }

    public void goToRedactorMode(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/redactorMode.fxml");
    }

    @FXML
    private Text username;

    @FXML
    private void add_element (){
        popUpWindow("/client/add_element.fxml");
    }


}
