package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class DBDiedController extends AbstractController implements Initializable {
    @FXML
    private ChoiceBox<String> languageChoice;
    @FXML
    private Button button;
    @FXML
    private Label sleep;
    @FXML
    private AnchorPane pane;

    @FXML
    private void exit() {
        pane.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    @Override
    protected void localize() {
        sleep.setText(observableResourse.getString("sleep"));
        button.setText(observableResourse.getString("button"));

    }

    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        sleep.textProperty().bind(observableResourse.getStringBinding("sleep"));
        button.textProperty().bind(observableResourse.getStringBinding("button"));
    }

    public void initLang(ObservableResourse observableResourse) {
        this.observableResourse = observableResourse;
        for (String localName : localeMap.keySet()) {
            if (localeMap.get(localName).equals(observableResourse.getResources().getLocale()))
                languageChoice.getSelectionModel().select(localName);
        }
        if (languageChoice.getSelectionModel().getSelectedItem().isEmpty()) {
            if (localeMap.containsValue(Locale.getDefault()))
                languageChoice.getSelectionModel().select(MapUtils.getKeyByValue(localeMap, Locale.getDefault()));
            else languageChoice.getSelectionModel().selectFirst();
        }
        languageChoice.setOnAction((event) -> {
            Locale loc = localeMap.get(languageChoice.getValue());
            observableResourse.setResources(ResourceBundle.getBundle(BUNDLE, loc));
            System.out.println("AbstractController.initLang");

        });
        bindGuiLanguage();
    }

}
