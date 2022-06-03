package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Coordinates;
import utils.Location;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * controller of table scene
 */
public class TableWindowController extends AbstractController implements Initializable {
    @FXML
    private Label author;
    @FXML
    private TableView<Route> table;
    @FXML
    private Label label;
    @FXML
    private TextField search_by_id;
    @FXML
    private TextField search_by_name;
    @FXML
    private TextField search_by_username;

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


    TableColumn<Route, String> user = new TableColumn<>("username");

    /**
     * main method, initialization of the table
     *
     * @param url            -url
     * @param resourceBundle -resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        author.setText("ты зашел как " + readerSender.user.getUsername());
        putTableTogether();
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
    protected void initializeTable() {
        requestRoutes();
        setTableSize();

        Response response = readerSender.read();
//        System.out.println(response.routeArrayList);
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

    private void putTableTogether() {
        coords.getColumns().addAll(x, y);
        location_from.getColumns().addAll(from_x, from_y, from_name);
        location_to.getColumns().addAll(to_x, to_y, to_name);


        table.getColumns().addAll(id, name, coords, location_from, location_to,
                distance, date, user);
    }

    /**
     * method that puts data collected from an ArrayList<Route> to the table
     *
     * @param routeArrayList - the ArrayList where data is taken from
     * @return
     */
    private void putDataInTheTable(ArrayList<Route> routeArrayList) {
        System.out.println("putting data in the table...");


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

        user.setCellValueFactory(callback -> new SimpleStringProperty(callback.getValue().getUser().getUsername()));

        ObservableList<Route> routes = FXCollections.observableList(routeArrayList);

        table.setItems(routes);

        search(routes);

    }

    /**
     * method that gets an observable list of routes and returns filtered list
     *
     * @param routes observable list of routes
     */
    private void search(ObservableList<Route> routes) {
        FilteredList<Route> filteredRoutes = new FilteredList<>(routes, b -> true);
        search_by_id.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRoutes.setPredicate(route -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String filter = newValue.toLowerCase();
                return String.valueOf(route.getId()).equals(filter);
            });
        });

        search_by_name.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRoutes.setPredicate(route -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String filter = newValue.toLowerCase();

                return route.getName().contains(filter);
            });
        });

        search_by_username.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRoutes.setPredicate(route -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String filter = newValue.toLowerCase();

                return (route.getUser().getUsername().contains(filter));
            });
        });

        SortedList<Route> sortedRoutes = new SortedList<>(filteredRoutes);
        sortedRoutes.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedRoutes);

    }

    /**
     * method that corrects the location
     *
     * @param routes          - arraylist of routes (with incorrect locations)
     * @param animationRoutes - list of animation routes (with correct locations)
     * @return arraylist with correct routes
     */
    private ArrayList<Route> correctLocations(ArrayList<Route> routes, ArrayList<AnimationRoute> animationRoutes) {
        for (int i = 0; i < routes.size(); i++) {
            routes.get(i).getFrom().setX(animationRoutes.get(i).getFromX());
            routes.get(i).getFrom().setY(animationRoutes.get(i).getFromY());
            routes.get(i).getTo().setX(animationRoutes.get(i).getToX());
            routes.get(i).getTo().setY(animationRoutes.get(i).getToY());
        }
        return routes;
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
        user.setPrefWidth(100);
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

    @FXML
    private void update_element() throws IOException {
        if (Objects.equals(readerSender.user.getUsername(), table.getSelectionModel().getSelectedItem().getUser().getUsername())) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/update.fxml"));
            Parent root = loader.load();
            UpdateController controller = loader.getController();
            controller.setId(table.getSelectionModel().getSelectedItem().getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            String msg = "нет прав на редактирование элемента";
            String title = "тебе букетик через интернетик";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setTitle(title);

        }
    }

    private void createAlert() {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
////        alert.setTitle("ты еблан?");
//        ButtonType type = new ButtonType("ok", ButtonBar.ButtonData.OK_DONE);
//        alert.setContentText("пустое имя");
//        alert.getDialogPane().getButtonTypes().add(type);

        //Creating a dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //Setting the title
        alert.setTitle("Alert");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        alert.setContentText("This is a confirmmation alert");
        //Adding buttons to the dialog pane
        alert.getDialogPane().getButtonTypes().add(type);
        //Setting the label
        Text txt = new Text("Click the button to show the dialog");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        txt.setFont(font);
        //Creating a button
        Button button = new Button("Show Dialog");
        //Showing the dialog on clicking the button
        button.setOnAction(e -> {
            alert.showAndWait();
        });
        //Creating a vbox to hold the button and the label
        HBox pane = new HBox(15);
        //Setting the space between the nodes of a HBox pane
        pane.setPadding(new Insets(50, 150, 50, 60));
        pane.getChildren().addAll(txt, button);
        //Creating a scene object
        Stage stage = new Stage();
        Scene scene = new Scene(new Group(pane), 595, 300, Color.BEIGE);
        stage.setTitle("Alert");
        stage.setScene(scene);
        stage.show();


    }

}
