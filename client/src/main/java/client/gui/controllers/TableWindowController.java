package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import utils.Coordinates;
import utils.Location;
import utils.Route;
import utils.animation.AnimationRoute;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * controller of table scene
 */
public class TableWindowController extends AbstractController implements Initializable {
    @FXML
    private Text username;
    @FXML
    private TableView<Route> table;
    @FXML
    private Label label;

    TableColumn<Route, Integer> id = new TableColumn<>("id");
    TableColumn<Route, String> name = new TableColumn<>("name");

    TableColumn<Route, Coordinates> coords = new TableColumn<>("coordinates");
    TableColumn<Route, Number> x = new TableColumn<>("x");
    TableColumn<Route, Number> y = new TableColumn<>("y");

    TableColumn<Route, Location> location_from = new TableColumn<>("location(from)");
    TableColumn<Route, Number> from_x = new TableColumn<>("from_x");
    TableColumn<Route, Number> from_y = new TableColumn<>("from_y");
    TableColumn<Route, String> from_name = new TableColumn<>("from_name");

    TableColumn<Route, utils.loc.Location> location_to = new TableColumn<>("location(to)");
    TableColumn<Route, Number> to_x = new TableColumn<>("to_x");
    TableColumn<Route, Number> to_y = new TableColumn<>("to_y");
    TableColumn<Route, String> to_name = new TableColumn<>("to_name");

    TableColumn<Route, Integer> distance = new TableColumn<>("distance");
    TableColumn<Route, ZonedDateTime> date = new TableColumn<>("creation date");


    TableColumn<Route, String> author = new TableColumn<>("username");

    /**
     * main method, initialization of the table
     *
     * @param url            -url
     * @param resourceBundle -resource bundle
     */
    //TODO автообновление как в анимации
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("ты зашел как " + readerSender.user.getUsername());
        initializeTable();
        Thread thread = new Thread(() -> {
            Runnable updater = this::initializeTable;

            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * method to initialize the table
     */
    private void initializeTable() {
        requestRoutes();
        setTableSize();

        Response response = readerSender.read();

        ArrayList<Route> routeArrayList = correctLocations(response.routeArrayList, response.animationRouteList);

        putDataInTheTable(routeArrayList);
    }

    /**
     * method that sends a request to the server
     */
    private void requestRoutes() {
        Request request = new Request();
        request.setArgs(List.of("get_all"));
        readerSender.sendToServer(request);
        System.out.println("sending a request to server..." + request);
    }

    /**
     * method that puts data collected from an ArrayList<Route> to the table
     *
     * @param routeArrayList - the ArrayList where data is taken from
     */
    private void putDataInTheTable(ArrayList<Route> routeArrayList) {
        System.out.println("putting data in the table...");
        coords.getColumns().addAll(x, y);
        location_from.getColumns().addAll(from_x, from_y, from_name);
        location_to.getColumns().addAll(to_x, to_y, to_name);


        table.getColumns().addAll(id, name, coords, location_from, location_to,
                distance, date, author);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        x.setCellValueFactory(callback -> new SimpleDoubleProperty(callback.getValue().getCoordinates().getCoorX()));
        y.setCellValueFactory(callback -> new SimpleDoubleProperty(callback.getValue().getCoordinates().getCoorY()));

        from_x.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getFrom().getFromX()));
        from_y.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getFrom().getFromY()));
        from_name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFrom().getName()));

        to_x.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTo().getToX()));
        to_y.setCellValueFactory(c -> new SimpleFloatProperty(c.getValue().getTo().getToY()));
        to_name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTo().getName()));

        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        date.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        author.setCellValueFactory(callback -> new SimpleStringProperty(callback.getValue().getUser().getUsername()));
        table.setItems(FXCollections.observableList(routeArrayList));
    }

    /**
     * method that corrects the location
     *
     * @param routes          - arraylist of routes (with incorrect locations)
     * @param animationRoutes - list of animation routes (with correct locations)
     * @return arraylist with correct routes
     */
    private ArrayList<Route> correctLocations(ArrayList<Route> routes, ArrayList<AnimationRoute> animationRoutes) {
        System.out.println("correcting locations...");
        ArrayList<Route> output = new ArrayList<>();
        for (Route route : routes) {
            for (AnimationRoute aniRoute : animationRoutes) {
                route.getFrom().setX(aniRoute.getFromX());
                route.getFrom().setY(aniRoute.getFromY());
                route.getTo().setX(aniRoute.getToX());
                route.getTo().setY(aniRoute.getToY());

            }
            output.add(route);
        }
        return output;
    }

    /**
     * method that sets the table size
     */
    private void setTableSize() {
        id.setPrefWidth(75);
        name.setPrefWidth(75);
        coords.setPrefWidth(150);
        x.setPrefWidth(75);
        y.setPrefWidth(75);
        location_from.setPrefWidth(300);
        from_x.setPrefWidth(100);
        from_y.setPrefWidth(100);
        from_name.setPrefWidth(100);
        location_to.setPrefWidth(300);
        to_x.setPrefWidth(100);
        to_y.setPrefWidth(100);
        to_name.setPrefWidth(100);
        distance.setPrefWidth(100);
        date.setPrefWidth(150);
        author.setPrefWidth(100);
    }

    /**
     * methods for buttons clicked
     *
     * @param actionEvent - click on back button
     */
    @FXML
    public void go_back(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/actionChoice.fxml");
    }

    @FXML
    public void log_out(ActionEvent actionEvent) {
        switchStages(actionEvent, "/client/auth.fxml");
    }

    @FXML
    private void add_element() {
        popUpWindow("/client/add_element.fxml");
    }

    @FXML
    private void delete_element() {
        if (readerSender.user == table.getSelectionModel().getSelectedItem().getUser())
            table.getItems().removeAll(table.getSelectionModel().getSelectedItem());
        else
            label.setText("нет прав на удаление элемента");
    }

}
