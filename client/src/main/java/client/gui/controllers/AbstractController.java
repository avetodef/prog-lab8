package client.gui.controllers;

import client.gui.StartingStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {

//    @FXML
//    protected ChoiceBox<String> languageChoice;
//
//    private final String[] availableLanguages = {"Русский", "Slovenščina",
//            "Український", "Español (República Dominicana)"};
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        languageChoice.setValue("Choose your language");
//        languageChoice.getItems().addAll(availableLanguages);
//    }

    public void exit(ActionEvent actionEvent) {
        switchStages(actionEvent, "auth.fxml");
    }

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    void switchStages(javafx.event.ActionEvent actionEvent, String switchingPoint) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(switchingPoint));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
