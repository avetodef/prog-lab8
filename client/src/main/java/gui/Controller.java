package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    protected Label label;
    @FXML
    protected void sayHi(){
        label.setText("hi");
    }
}
