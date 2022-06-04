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
import lombok.SneakyThrows;
import parsing.PasswordHandler;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrationController extends AbstractController implements Initializable {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Text password_warning_text;
    @FXML
    private Text username_warning_text;
    @FXML
    private PasswordField repeat_password_field;
    @FXML
    private Text password;
    @FXML
    private Text username;
    @FXML
    private Text repeat_password;
    @FXML
    private Label create;
    @FXML
    private Button back;
    @FXML
    private Button submit;
    @FXML
    private AnchorPane pane;


    private User getUserFromAuthWindow() {
        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
        return new User(username, PasswordHandler.encode(password));
    }

    private void sendDataToServer(User user) {
        try {
            List<String> arguments = List.of("registration");
            Request userRequest = new Request(arguments, null, user);
            System.out.println("sending data to server... " + userRequest);
            readerSender.setUser(user);
            readerSender.sendToServer(userRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void processServerResponse(ActionEvent actionEvent) {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");

        if (response.status.equals(Status.OK)) {
            //switchStages(actionEvent, "/client/actionChoice.fxml");
            //System.out.println("AUTHENTICATION WENT SUCCESSFULLY");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/actionChoice.fxml"));
            Parent root = loader.load();
            ActionChoiceController res = loader.getController();
            res.initLang(observableResourse);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            pane.getScene().getWindow().hide();
            stage.show();
        } else {
            if (response.status.equals(Status.PASSWORD_ERROR)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                alert.setTitle(response.msg);
                alert.show();
            }
            if (response.status.equals(Status.USERNAME_ERROR)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response.msg, ButtonType.OK);
                alert.setTitle(response.msg);
                alert.show();
            }
            if (response.msg.equals("database sleep"))
                readerSender.dbDied();
        }
    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        username_warning_text.setText("");
        password_warning_text.setText("");

        User user = getUserFromAuthWindow();

        boolean areTheSame = password_field.getText().trim().equals(repeat_password_field.getText().trim());
        if(areTheSame && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()
                && !user.getPassword().equals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")) { //если что страшилка это то как кодирует пустое поле сша 256
            sendDataToServer(user);
            processServerResponse(actionEvent);
        }
        else {
            if (user.getUsername().isEmpty()) {
                //System.out.println("NAME IS EMPTY");
                String msg = "пустое имя";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(msg);
                alert.show();
            }
            if (password_field.getText().isEmpty()) {
                //System.out.println("PASSWORD IS EMPTY");
                String msg = "пустой пароль";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(msg);
                alert.show();
            }
            if (!areTheSame) {
                //System.out.println("PASSWORD AND REPEAT PASSWORD DO NOT MATCH");
                String msg = "пароли не совпадают";
                Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                alert.setTitle(msg);
                alert.show();
            }
        }
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        //switchStages(actionEvent, "/client/auth.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/auth.fxml"));
            Parent root = loader.load();
            AuthController auth = loader.getController();
            auth.initLang(observableResourse);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            pane.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ChoiceBox<String> languageChoice;

    private final String[] availableLanguages = {"Русский", "Slovenščina",
            "Український", "Español (República Dominicana)"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.setValue("Choose your language");

        AbstractController.localeMap = new HashMap<>();
        AbstractController.localeMap.put("Русский", new Locale("ru", "RU"));
        AbstractController.localeMap.put("Slovenščina", new Locale("sl", "SL"));
        AbstractController.localeMap.put("Український", new Locale("uk", "UK"));
        AbstractController.localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        //languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));

    }

    @Override
    protected void localize() {
        System.out.println("обзервабл ресурс пустой? " + observableResourse.resourcesProperty().isNull());
        username_field.setPromptText((observableResourse.getString("username_field")));
        password_field.setPromptText((observableResourse.getString("password_field")));
        repeat_password_field.setPromptText((observableResourse.getString("repeat_password_field")));
        username.setText(observableResourse.getString("username"));
        password.setText(observableResourse.getString("password"));

        repeat_password.setText(observableResourse.getString("repeat_password"));

        username_warning_text.setText(observableResourse.getString("username_warning_text"));
        password_warning_text.setText(observableResourse.getString("password_warning_text"));

        create.setText(observableResourse.getString("create"));
        back.setText(observableResourse.getString("back"));
        submit.setText(observableResourse.getString("submit"));

        languageChoice.setValue(observableResourse.getString("languageChoice"));

    }

    public void initLang(ObservableResourse observableResourse) {
        AbstractController.observableResourse = observableResourse;

        for (String localName : AbstractController.localeMap.keySet()) {

            Locale s = AbstractController.localeMap.get(localName);
            System.out.println(s);
            var res = observableResourse.getResources();
            System.out.println(res);
            if (s.equals(res.getLocale()))
                languageChoice.getSelectionModel().select(localName);

        }
        if (languageChoice.getSelectionModel().getSelectedItem().isEmpty()) {
            if (AbstractController.localeMap.containsValue(Locale.getDefault()))
                languageChoice.getSelectionModel().select(MapUtils.getKeyByValue(AbstractController.localeMap, Locale.getDefault()));
            else languageChoice.getSelectionModel().selectFirst();
        }

        languageChoice.setOnAction((event) -> {
            Locale loc = AbstractController.localeMap.get(languageChoice.getValue());
            observableResourse.setResources(ResourceBundle.getBundle(BUNDLE, loc));


        });
        bindGuiLanguage();
    }

    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));

        username.textProperty().bind(observableResourse.getStringBinding("username"));
        password.textProperty().bind(observableResourse.getStringBinding("password"));
        repeat_password.textProperty().bind(observableResourse.getStringBinding("repeat_password"));

        password_field.promptTextProperty().bind(observableResourse.getStringBinding("password_field"));
        username_field.promptTextProperty().bind(observableResourse.getStringBinding("username_field"));
        repeat_password_field.promptTextProperty().bind(observableResourse.getStringBinding("repeat_password_field"));

        back.textProperty().bind(observableResourse.getStringBinding("back"));
        create.textProperty().bind(observableResourse.getStringBinding("create"));
        submit.textProperty().bind(observableResourse.getStringBinding("submit"));


    }
}
