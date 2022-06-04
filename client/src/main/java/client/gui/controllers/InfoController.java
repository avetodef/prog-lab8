package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class InfoController extends AbstractController implements Initializable {

    @FXML
    public TextField id;
    @FXML
    public TextField coords;
    @FXML
    public TextField from;
    @FXML
    public TextField to;
    @FXML
    public TextField distance;
    @FXML
    public TextField date;
    @FXML
    public TextField username;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button back;
    @FXML
    private ChoiceBox<String> languageChoice;

    @FXML
    private void go_back(ActionEvent event) {
        pane.getScene().getWindow().hide();
    }

    @Override
    protected void localize() {
        back.setText(observableResourse.getString("back"));
        languageChoice.setValue(observableResourse.getString("languageChoice"));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.setValue("Choose your language");
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));
        System.out.println("You choose " + FXCollections.observableArrayList(localeMap.keySet()));
    }

    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        back.textProperty().bind(observableResourse.getStringBinding("back"));
    }

    public void initLang(ObservableResourse observableResourse) {
        System.out.println("AbstractController.initLang");
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

        System.out.println("AbstractController.initLang");
        languageChoice.setOnAction((event) -> {
            Locale loc = localeMap.get(languageChoice.getValue());
            observableResourse.setResources(ResourceBundle.getBundle(BUNDLE, loc));
            System.out.println("AbstractController.initLang");

        });
        bindGuiLanguage();
    }

}
