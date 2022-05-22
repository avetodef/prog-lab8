package client.gui.controllers;

import javafx.event.ActionEvent;

public class CommandWindowController extends AbstractController {

    public void go_back(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice.fxml");
    }
    public void log_out(ActionEvent actionEvent){
        switchStages(actionEvent, "/client/auth.fxml");
    }
    public void show(ActionEvent actionEvent){
        switchStages(actionEvent, "/client/tableWindow.fxml");
    }
    public void add(ActionEvent event){
        switchStages(event, "/client/add_element.fxml");
    }

}
