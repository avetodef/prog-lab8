package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import utils.RouteInfo;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class AddElementController extends AbstractController implements Initializable {

    @Override
    protected void localize() {

//        name_warning.setText("");
//        x_warning.setText("");
//        y_warning.setText("");
//        from_x_warning.setText("");
//        from_y_warning.setText("");
//        from_name_warning.setText("");
//        to_x_warning.setText("");
//        to_y_warning.setText("");
//        to_name_warning.setText("");
//   distance_warning.setText("");

        addElement.setText(observableResourse.getString("addElement"));
        add.setText(observableResourse.getString("add"));
        back.setText(observableResourse.getString("back"));

    }


    @FXML
    private Button AddButton;

    @FXML
    private Button UpdateButton;

    @FXML
    Button DeleteButton;
    private boolean send;

    private void sendDataToServer(RouteInfo info) {
        Request request = new Request();
        request.setArgs(List.of("add"));
        request.setInfo(info);
        readerSender.sendToServer(request);
        System.out.println("sending data to server... ");
    }

    private void processServerResponse() {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        label.setText(response.msg);
        if (response.status.equals(Status.OK)) {
            pane.getScene().getWindow().hide();
        }
        if (response.msg.equals("database sleep"))
            readerSender.dbDied();
    }

    @FXML
    @Override
    public void submit(ActionEvent actionEvent) {
        RouteInfo inf = info();
        if (send) {
            sendDataToServer(inf);
            processServerResponse();
        } else
        //label.setText("Неверный ввод");
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, observableResourse.getString("InputExc"), ButtonType.OK);
            alert.show();
//            add.setText(observableResourse.getString("add"));
//            System.out.println("AddElementController.submit");
        }
    }

    private RouteInfo info() {
//        name_warning.setText("");
//        x_warning.setText("");
//        y_warning.setText("");
//        from_x_warning.setText("");
//        from_y_warning.setText("");
//        from_name_warning.setText("");
//        to_x_warning.setText("");
//        to_y_warning.setText("");
//        to_name_warning.setText("");
//        distance_warning.setText("");

        RouteInfo out = new RouteInfo();

        send = true;

        try {
            out.name = name_field.getText().trim();
            if (out.name.isEmpty()) {
//                name_warning.textProperty().bind(observableResourse.getStringBinding("name_warning"));
//                name_warning.setText(observableResourse.getString("name_warning"));
                send = false;
            }
        } catch (RuntimeException e) {
            //name_warning.setText(observableResourse.getString("name_warning"));
            send = false;
        }

        try {
            out.x = Double.parseDouble(coord_x_field.getText().trim());

        } catch (RuntimeException e) {
            //x_warning.setText(observableResourse.getString("x_warning"));
            send = false;
        }

        try {
            out.y = Double.parseDouble(coord_y_field.getText().trim());
            if (out.y < -210) {
                //y_warning.setText(observableResourse.getString("y_warning"));
                send = false;
            }
        } catch (RuntimeException e) {
            //y_warning.setText(observableResourse.getString("y_warning"));
            send = false;
        }
        try {
            out.fromX = Double.parseDouble(from_x_field.getText().trim());
        } catch (RuntimeException e) {
            //from_name_warning.setText(observableResourse.getString("from_name_warning"));
            send = false;
        }

        try {
            out.fromY = Long.parseLong(from_y_field.getText().trim());
        } catch (RuntimeException e) {
            //from_y_warning.setText(observableResourse.getString("from_y_warning"));
            send = false;
        }
        try {
            out.nameFrom = from_name_field.getText().trim();
            if (out.nameFrom.isEmpty()) {
                //from_x_warning.setText(observableResourse.getString("from_x_warning"));
                send = false;
            }
        } catch (RuntimeException e) {
            // from_x_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.toX = Integer.parseInt(to_x_field.getText().trim());
        } catch (RuntimeException e) {
            //to_x_warning.setText(observableResourse.getString("to_x_warning"));
            send = false;
        }
        try {
            out.toY = Float.parseFloat(to_y_field.getText().trim());
        } catch (RuntimeException e) {
            //to_y_warning.setText(observableResourse.getString("to_y_warning"));
            send = false;
        }

        try {
            out.nameTo = to_name_field.getText().trim();
        } catch (RuntimeException e) {
            //to_name_warning.setText(observableResourse.getString("to_name_warning"));
            send = false;
        }

        try {
            out.distance = Integer.parseInt(distance_field.getText().trim());
            if (out.distance < 0 || out.distance < 1) {
                // distance_warning.setText(observableResourse.getString("distance_warning"));
                send = false;
            }
        } catch (RuntimeException e) {
            // distance_warning.setText(observableResourse.getString("distance_warning"));
            send = false;
        }
        return out;
    }

    @FXML
    private void go_back(ActionEvent actionEvent) {
        pane.getScene().getWindow().hide();
    }


    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        addElement.textProperty().bind(observableResourse.getStringBinding("addElement"));
        add.textProperty().bind(observableResourse.getStringBinding("add"));
        back.textProperty().bind(observableResourse.getStringBinding("back"));

//        if(!send) {
//
//            name_warning.textProperty().bind(observableResourse.getStringBinding("name_warning"));
//            x_warning.textProperty().bind(observableResourse.getStringBinding("x_warning"));
//            y_warning.textProperty().bind(observableResourse.getStringBinding("y_warning"));
//            from_x_warning.textProperty().bind(observableResourse.getStringBinding("from_x_warning"));
//            from_y_warning.textProperty().bind(observableResourse.getStringBinding("from_y_warning"));
//            from_name_warning.textProperty().bind(observableResourse.getStringBinding("from_name_warning"));
//            to_x_warning.textProperty().bind(observableResourse.getStringBinding("to_x_warning"));
//            to_y_warning.textProperty().bind(observableResourse.getStringBinding("to_y_warning"));
//            to_name_warning.textProperty().bind(observableResourse.getStringBinding("to_name_warning"));
        distance_warning.textProperty().bind(observableResourse.getStringBinding("distance_warning"));
//        }
//        else
//        {
//            name_warning.textProperty().bind(null);
//            x_warning.textProperty().bind(null);
//            y_warning.textProperty().bind(null);
//            from_x_warning.textProperty().bind(null);
//            from_y_warning.textProperty().bind(null);
//            from_name_warning.textProperty().bind(null);
//            to_x_warning.textProperty().bind(null);
//            to_y_warning.textProperty().bind(null);
//            to_name_warning.textProperty().bind(null);
//            distance_warning.textProperty().bind(null);
//        }


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
    private Button addElement;
    @FXML
    private Label add;
    @FXML
    private Button back;

    @FXML
    private Pane pane;
    @FXML
    private TextField name_field;
    @FXML
    private TextField coord_x_field;
    @FXML
    private TextField coord_y_field;
    @FXML
    private TextField from_x_field;
    @FXML
    private TextField from_y_field;
    @FXML
    private TextField from_name_field;
    @FXML
    private TextField to_x_field;
    @FXML
    private TextField to_y_field;
    @FXML
    private TextField to_name_field;
    @FXML
    private TextField distance_field;

    @FXML
    private Text name_warning;
    @FXML
    private Text x_warning;
    @FXML
    private Text y_warning;
    @FXML
    private Text from_x_warning;
    @FXML
    private Text from_y_warning;
    @FXML
    private Text from_name_warning;
    @FXML
    private Text to_x_warning;
    @FXML
    private Text to_y_warning;
    @FXML
    private Text to_name_warning;
    @FXML
    private Text distance_warning;
    @FXML
    private Label label;
    @FXML
    private ChoiceBox<String> languageChoice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        name_warning.setText("");
//        x_warning.setText("");
//        y_warning.setText("");
//        from_x_warning.setText("");
//        from_y_warning.setText("");
//        from_name_warning.setText("");
//        to_x_warning.setText("");
//        to_y_warning.setText("");
//        to_name_warning.setText("");
        //distance_warning.setText("");

        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }
}
