package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;


public class ActionChoiceController extends AbstractController implements Initializable {
    @FXML
    private Label things;
    @FXML
    private ChoiceBox<String> languageChoice;
    @FXML
    private Button logOut;
    @FXML
    private Button animation;
    @FXML
    private Button checkTable;
    @FXML
    private AnchorPane panes;
    @FXML
    private Text text;
    @FXML
    private ImageView avatar;


    @Override
    protected void localize() {
        things.setText(observableResourse.getString("things"));
        logOut.setText(observableResourse.getString("logOut"));
        animation.setText(observableResourse.getString("animation"));
        checkTable.setText(observableResourse.getString("checkTable"));
        languageChoice.setValue(observableResourse.getString("languageChoice"));
        text.setText(observableResourse.getString("text") + readerSender.user.getUsername());

    }

    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        things.textProperty().bind(observableResourse.getStringBinding("things"));
        logOut.textProperty().bind(observableResourse.getStringBinding("logOut"));
        animation.textProperty().bind(observableResourse.getStringBinding("animation"));
        checkTable.textProperty().bind(observableResourse.getStringBinding("checkTable"));
        text.textProperty().bind(observableResourse.getStringBinding("text"));

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

    @FXML
    private void hover() {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(0.5);
        avatar.setEffect(adjust);
    }

    @FXML
    private void unhover() {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(0);
        avatar.setEffect(adjust);
    }


    @FXML
    private void trolling() {
        popUpWindow("/client/trolling.fxml");
    }

    @FXML
    private void switchToAnimationWindow(ActionEvent actionEvent) throws IOException {
        //switchStages(actionEvent, "/client/animation.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/animation.fxml"));
        Parent root = loader.load();
        AnimationWindowController anim = loader.getController();
        anim.initLang(observableResourse);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        panes.getScene().getWindow().hide();
        stage.show();
    }

    @FXML
    private void switchToTableWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/tableWindow.fxml"));
        Parent root = loader.load();
        TableWindowController res = loader.getController();
        res.initLang(observableResourse);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        panes.getScene().getWindow().hide();
        stage.show();
        //switchStages(actionEvent, "/client/tableWindow.fxml");
        //popUpWindow("/client/tableWindow.fxml");
    }

    @FXML
    private void log_out(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/auth.fxml"));
            Parent root = loader.load();
            AuthController auth = loader.getController();
            auth.initLang(observableResourse);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            panes.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //switchStages(actionEvent, "/client/auth.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //username.setText("ты зашел как " + readerSender.user.getUsername());
        languageChoice.setValue("Choose your language");
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));
        //text.setText(observableResourse.getString("text") + readerSender.user.getUsername());
    }
}
