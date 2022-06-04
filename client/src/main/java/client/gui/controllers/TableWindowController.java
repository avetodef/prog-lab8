package client.gui.controllers;

import client.gui.tool.MapUtils;
import client.gui.tool.ObservableResourse;
import interaction.Request;
import interaction.Response;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Coordinates;
import utils.Location;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    private Label labelTable;
    @FXML
    private TextField search_by_id;
    @FXML
    private TextField search_by_name;
    @FXML
    private TextField search_by_username;
    @FXML
    private Button logOut;
    @FXML
    private Button back;
    @FXML
    private ChoiceBox<String> languageChoice;
    @FXML
    private AnchorPane panes;

    private Map<String, Locale> localeMap;

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

        localeMap = new HashMap<>();
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Slovenščina", new Locale("sl", "SL"));
        localeMap.put("Український", new Locale("uk", "UK"));
        localeMap.put("Español (República Dominicana)", new Locale("es", "ES"));
        languageChoice.setItems(FXCollections.observableArrayList(localeMap.keySet()));

        Thread thread = new Thread(() -> {
            Runnable updater = this::initializeTable;

            while (true) {
                try {
                    Thread.sleep(10000);
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
        System.out.println("sending a request to server...");
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
        //date.setCellValueFactory(c -> (ObservableValue<ZonedDateTime>) new SimpleDateFormat(c.getValue().getCreationDate()));

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
        date.setPrefWidth(75);
        user.setPrefWidth(100);
    }

    /**
     * methods for buttons clicked
     *
     * @param actionEvent click on back button
     */
    @FXML
    public void go_back(ActionEvent actionEvent) {
        //switchStages(actionEvent, "/client/actionChoice.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/actionChoice.fxml"));
            Parent root = loader.load();
            ActionChoiceController auth = loader.getController();
            auth.initLang(observableResourse);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            panes.getScene().getWindow().hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void log_out(ActionEvent actionEvent) {
        //switchStages(actionEvent, "/client/auth.fxml");
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
    }

    @FXML
    private void add_element() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/add_element.fxml"));
        try {
            Parent root = loader.load();
            AddElementController controller = loader.getController();
            controller.initLang(observableResourse);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @FXML
    private void delete_element() {
        if (readerSender.user.getUsername().equals(table.getSelectionModel().getSelectedItem().getUser().getUsername())) {
            table.getItems().removeAll(table.getSelectionModel().getSelectedItem());

            Request request = new Request();
            request.setArgs(List.of("remove_by_id", String.valueOf(table.getSelectionModel().getSelectedItem().getId())));
            readerSender.sendToServer(request);

        } else {
            System.out.println("user eblan");
            String msg = "нет прав на удаление элемента";
            String title = "тебе букетик через интернетик";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setTitle(title);
            alert.show();
        }
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
            stage.showAndWait();

            panes.getScene().getWindow().hide();

            initializeTable();
        } else {
            String msg = "нет прав на редактирование элемента";
            String title = "тебе букетик через интернетик";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.setTitle(title);
            alert.show();
        }
    }


    public void bindGuiLanguage() {
        observableResourse.setResources(ResourceBundle.getBundle
                (BUNDLE, localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        labelTable.textProperty().bind(observableResourse.getStringBinding("labelTable"));
        logOut.textProperty().bind(observableResourse.getStringBinding("logOut"));
        back.textProperty().bind(observableResourse.getStringBinding("back"));
        search_by_id.promptTextProperty().bind(observableResourse.getStringBinding("search_by_id"));
        search_by_name.promptTextProperty().bind(observableResourse.getStringBinding("search_by_name"));
        search_by_username.promptTextProperty().bind(observableResourse.getStringBinding("search_by_username"));

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


    @Override
    protected void localize() {
        logOut.setText(observableResourse.getString("logOut"));
        back.setText(observableResourse.getString("back"));
        labelTable.setText(observableResourse.getString("labelTable"));
        search_by_id.setPromptText(observableResourse.getString("search_by_id"));
        search_by_name.setPromptText(observableResourse.getString("search_by_name"));
        search_by_username.setPromptText(observableResourse.getString("search_by_username"));
        languageChoice.setValue(observableResourse.getString("languageChoice"));
    }
}
