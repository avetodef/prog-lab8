package client.gui.controllers;

import client.ReaderSender;
import client.gui.StartingStage;
import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class AbstractController {
    @FXML
    private Button BackButton;
    @FXML
    private Button ExitButton;

    public static SocketChannel socketChannel;
    private static final int serverPort = 6666;

    private static void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                System.out.println("connection established");
            } catch (IOException e) {
                System.out.println("no connection to server");
            }
        }
    }

    static {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            //System.out.println("AbstractController.static initializer");
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            //System.out.println("AbstractController.static initializer)))))))");
            if (socketChannel.isConnected())
                System.out.println("connection established ABSTR CONTR");

        } catch (IOException e) {
            connect(socketChannel);
        }
    }

    public void submit(ActionEvent actionEvent) {
    }

    public static ReaderSender readerSender = new ReaderSender(socketChannel);

    public void popUpWindow(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(path));
            Scene popupScene = new Scene(fxmlLoader.load());
            Stage popupstage = new Stage();
            popupstage.setScene(popupScene);
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchStages(javafx.event.ActionEvent actionEvent, String switchingPoint) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource(switchingPoint));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static final String BUNDLE = "bundle.gui";

    public static ObservableResourse observableResourse;

    public static Map<String, Locale> localeMap;

    @FXML
    private static ChoiceBox<String> languageChoice;
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
    private Button InputButton;
    @FXML
    private Button RegistrationButton;
    @FXML
    private Label InputLabel;
    @FXML
    private Label RegistrationLabel;


    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.
                getBundle(BUNDLE, localeMap.get(languageChoice.
                        getSelectionModel().getSelectedItem())));
        label.textProperty().bind(observableResourse.getStringBinding("label"));
        username.textProperty().bind(observableResourse.getStringBinding("username"));
        password.textProperty().bind(observableResourse.getStringBinding("password"));
        password_field.textProperty().bind(observableResourse.getStringBinding("password_field"));
        username_field.textProperty().bind(observableResourse.getStringBinding("username_field"));
        InputButton.textProperty().bind(observableResourse.getStringBinding("InputButton"));
        RegistrationButton.textProperty().bind(observableResourse.getStringBinding("RegistrationButton"));

    }

    public void initial() {
        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));

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

        System.out.println("AbstractController.initLang");
        languageChoice.setOnAction((event) -> {
            Locale loc = localeMap.get(languageChoice.getValue());
            observableResourse.setResources(ResourceBundle.getBundle(BUNDLE, loc));
            System.out.println("AbstractController.initLang");

        });
        bindGuiLanguage();


    }

    protected abstract void localize();


}
