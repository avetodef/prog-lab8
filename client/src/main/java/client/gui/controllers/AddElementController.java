package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utils.RouteInfo;

import java.util.List;


public class AddElementController extends AbstractController {

    //ReaderSender readerSender = new ReaderSender(new AuthController().socketChannel);
    boolean send = true;
    private void sendDataToServer() {
        Request request = new Request();
        request.setArgs(List.of("add"));
        RouteInfo inf = info();
        request.setInfo(inf);
        readerSender.sendToServer(request);
        System.out.println("sending data to server... " + request);
    }

    private void processServerResponse() {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");

        label.setText(response.msg);
    }

    @FXML
    @Override
    public void submit(ActionEvent actionEvent) {
        if (send) {
            sendDataToServer();
            processServerResponse();
        } else
            label.setText("Неверный ввод. Попробуй снова");
//        processServerResponse();

    }

    private RouteInfo info() {
        name_warning.setText("");
        x_warning.setText("");
        y_warning.setText("");
        from_x_warning.setText("");
        from_y_warning.setText("");
        from_name_warning.setText("");
        to_x_warning.setText("");
        to_y_warning.setText("");
        to_name_warning.setText("");
        distance_warning.setText("");

        RouteInfo out = new RouteInfo();

        send = true;
        try {
            out.name = name_field.getText().trim();
            if (out.name.isEmpty()) {
                name_warning.setText("название не может быть пустым");
                send = false;
            }
        } catch (RuntimeException e) {
            name_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.x = Double.parseDouble(coord_x_field.getText().trim());

        } catch (RuntimeException e) {
            x_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.y = Double.parseDouble(coord_y_field.getText().trim());
            if (out.y < -210) {
                y_warning.setText("не может быть меньше -210");
                send = false;
            }
        } catch (RuntimeException e) {
            y_warning.setText("неправильный тип данных");
            send = false;
        }
        try {
            out.fromX = Double.parseDouble(from_x_field.getText().trim());
        } catch (RuntimeException e) {
            from_name_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.fromY = Long.parseLong(from_y_field.getText().trim());
        } catch (RuntimeException e) {
            from_y_warning.setText("неправильный тип данных");
            send = false;
        }
        try {
            out.nameFrom = from_name_field.getText().trim();
            if (out.nameFrom.isEmpty()) {
                from_x_warning.setText("название не может быть пустым");
                send = false;
            }
        } catch (RuntimeException e) {
            from_x_warning.setText("введите строку");
            send = false;
        }

        try {
            out.toX = Integer.parseInt(to_x_field.getText().trim());
        } catch (RuntimeException e) {
            to_x_warning.setText("неправильный тип данных");
            send = false;
        }
        try {
            out.toY = Float.parseFloat(to_y_field.getText().trim());
        } catch (RuntimeException e) {
            to_y_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.nameTo = to_name_field.getText().trim();
        } catch (RuntimeException e) {
            to_name_warning.setText("неправильный тип данных");
            send = false;
        }

        try {
            out.distance = Integer.parseInt(distance_field.getText().trim());
            if (out.distance < 0 || out.distance < 1) {
                distance_warning.setText("длина маршрута должна быть больше 1");
                send = false;
            }
        } catch (RuntimeException e) {
            distance_warning.setText("неправильный тип данных");
            send = false;
        }
//        name_warning.setText(null);
//        x_warning.setText(null);
//        y_warning.setText(null);
//        from_x_warning.setText(null);
//        from_y_warning.setText(null);
//        from_name_warning.setText(null);
//        to_x_warning.setText(null);
//        to_y_warning.setText(null);
//        to_name_warning.setText(null);
//        distance_warning.setText(null);
        return out;
    }

    public void go_back(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice");
    }

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
}
