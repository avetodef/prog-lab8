package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import utils.RouteInfo;

import java.io.IOException;
import java.util.List;

public class UpdateController extends AbstractController {
    /**
     * a buch of @FXML injections
     */

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
    private void go_back(ActionEvent actionEvent) {
        pane.getScene().getWindow().hide();
    }

    private boolean send = true;
    @Setter
    protected int id;

    @FXML
    public void submit(ActionEvent actionEvent) {
        RouteInfo inf = info();
        if (send) {
            sendDataToServer(inf);
            processServerResponse();

        } else
            label.setText("Неверный ввод. Попробуй снова");
    }

    private void sendDataToServer(RouteInfo info) {
        Request request = new Request();
        request.setArgs(List.of("update_by_id", String.valueOf(id)));
        request.setInfo(info);
        readerSender.sendToServer(request);
        System.out.println("sending data to server... " + request);
    }

    private void processServerResponse() {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        label.setText(response.msg);
        if (response.status.equals(Status.OK)) {
            pane.getScene().getWindow().hide();
            try {
                loadTable();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        if (response.msg.equals("database sleep"))
            readerSender.dbDied();

    }

    private void loadTable() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/tableWindow.fxml"));
        Parent root = loader.load();
        TableWindowController controller = loader.getController();
        controller.initializeTable();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to get data from a bunch of fields
     *
     * @return - object of RouteInfo
     */
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
                name_warning.setText("неправильный тип данных");
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
                y_warning.setText("неправильный тип данных");
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
                from_x_warning.setText("неправильный тип данных");
                send = false;
            }
        } catch (RuntimeException e) {
            from_x_warning.setText("неправильный тип данных");
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
                distance_warning.setText("неправильный тип данных");
                send = false;
            }
        } catch (RuntimeException e) {
            distance_warning.setText("неправильный тип данных");
            send = false;
        }
        return out;
    }


    @Override
    protected void localize() {

    }

}
