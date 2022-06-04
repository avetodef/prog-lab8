package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import parsing.PasswordHandler;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class AuthController extends AbstractController implements Initializable {
    @FXML
    private Label label;
    @FXML
    private Text username;
    @FXML
    private Text password;
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Text username_warning_text;
    @FXML
    private Text password_warning_text;
    @FXML
    private Button submit_button;
    @FXML
    private Button inputButton;
    @FXML
    private Button registrButton;
    @FXML
    private Label inputLabel;
    @FXML
    private Label registrationLabel;

    private Map<String, Locale> localeMap;

    @FXML
    private ChoiceBox<String> languageChoice;
    @FXML
    private AnchorPane pane;

    public static final String BUNDLE = "bundle.gui";


    @FXML
    private void sign_up(ActionEvent actionEvent) {
        //switchStages(actionEvent, "/client/registration.fxml");
        try {
            goToRegistration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableResourse observableResourse;

    @Override
    protected void localize() {
        System.out.println("обзервабл ресурс пустой? " + observableResourse.resourcesProperty().isNull());
        inputButton.setText(observableResourse.getString("InputButton"));
        registrButton.setText(observableResourse.getString("RegistrationButton"));
        username_field.setText(observableResourse.getString("username_field"));
        password_field.setText(observableResourse.getString("password_field"));
        username.setText(observableResourse.getString("username"));
        password.setText(observableResourse.getString("password"));
        username_warning_text.setText(observableResourse.getString("username_warning_text"));
        password_warning_text.setText(observableResourse.getString("password_warning_text"));
        label.setText(observableResourse.getString("label"));
        languageChoice.setValue(observableResourse.getString("languageChoice"));

    }

    private User getUserFromAuthWindow() {
        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
        return new User(username, PasswordHandler.encode(password));
    }


    private void sendDataToServer(User user) {
        try {
            List<String> arguments = List.of("authorization");
            Request userRequest = new Request(arguments, null, user);
            readerSender.setUser(user);
            readerSender.sendToServer(userRequest);
            System.out.println("sending data to server... ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processServerResponse(ActionEvent actionEvent) {

        try {
            Response response = readerSender.read();
            if (response != null) {
                System.out.println(response.status + " [" + response.msg + "]");
                if (response.status.equals(Status.OK)) {

                    switchStages(actionEvent, "/client/actionChoice.fxml");
                } else {
                    if (response.status.equals(Status.PASSWORD_ERROR)) {
                        //password_warning_text.setText(response.msg);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                        alert.setTitle(response.msg);
                        alert.show();
                    }
                    if (response.status.equals(Status.USERNAME_ERROR)) {
                        String title = "тебе букетик через интернетик";
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                        alert.setTitle(title);
                        alert.show();
                    }
                    if (response.msg.equals("database sleep"))
                        readerSender.dbDied();
                }
            } else
                System.out.println("NULL SERVER RESPONSE ");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void submit(ActionEvent actionEvent) {

        username_warning_text.setText("");
        password_warning_text.setText("");

        User user = getUserFromAuthWindow();
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()
                && !user.getPassword().equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) { //если что страшилка это то как кодирует пустое поле сша 256
            sendDataToServer(user);

            processServerResponse(actionEvent);

            readerSender.setUser(user);

        } else {
            if (user.getUsername().isEmpty()) {
                String title = "тебе букетик через интернетик";
                String msg = "пустое имя";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(title);
                alert.show();
            }
            if (password_field.getText().isEmpty()) {
                String title = "тебе букетик через интернетик";
                String msg = "пустой пароль";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(title);
                alert.show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        languageChoice.setValue("Choose your language");
//        languageChoice.getItems().addAll(availableLanguages);

        languageChoice.setValue("Choose your language");
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));

    }

    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        label.textProperty().bind(observableResourse.getStringBinding("label"));
        username.textProperty().bind(observableResourse.getStringBinding("username"));
        password.textProperty().bind(observableResourse.getStringBinding("password"));
        password_field.promptTextProperty().bind(observableResourse.getStringBinding("password_field"));
        username_field.promptTextProperty().bind(observableResourse.getStringBinding("username_field"));
        inputButton.textProperty().bind(observableResourse.getStringBinding("InputButton"));
        registrButton.textProperty().bind(observableResourse.getStringBinding("RegistrationButton"));

    }


    public void initLang(ObservableResourse observableResourse) {
        //System.out.println("AbstractController.initLang");
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

    private void goToRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/registration.fxml"));
        Parent root = loader.load();
        RegistrationController res = loader.getController();
        res.initLang(observableResourse);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        pane.getScene().getWindow().hide();
        stage.show();
    }
}
