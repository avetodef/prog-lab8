package gui.controllers;

import gui.StartingStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements Initializable {

    @FXML
    protected ChoiceBox<String> languageChoice;

    private final String[] availableLanguages = {"Русский", "Slovenščina",
            "Український", "Español (República Dominicana)"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.setValue("Choose your language");
        languageChoice.getItems().addAll(availableLanguages);
    }

    public void exit(ActionEvent actionEvent){
        try {
            switchStages(actionEvent,"auth.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    void switchStages(javafx.event.ActionEvent actionEvent, String switchingPoint) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(switchingPoint));
        stage = (Stage)( (Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
