package client.gui.controllers;

import interaction.Request;
import interaction.Response;
import interaction.Status;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.ColorConverter;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class AnimationWindowController extends AbstractController implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane pane;
    @FXML
    private Text username;
    @FXML
    private Rectangle userColour;

    @FXML
    public void go_back(ActionEvent event) {
        switchStages(event, "/client/actionChoice.fxml");
    }


    /**
     * initialization of the scene
     *
     * @param url            - url
     * @param resourceBundle - resourse bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("вы вошли как " + readerSender.user.getUsername());
        drawAxis();
        animate();
        Thread thread = new Thread(() -> {
            Runnable updater = this::animate;

            while (true) {
                try {
                    Thread.sleep(20000);
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
     * method to send request to server
     */
    private void requestRoutes() {
        try {
            List<String> arguments = List.of("animation");
            Request userRequest = new Request(arguments, null, readerSender.user);
            readerSender.sendToServer(userRequest);
            System.out.println("sending data to server... " + userRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean draw = true;

    /**
     * method to get an ArrayList of special type of routes (AnimationRoute)
     *
     * @return ArrayList of animationRoutes
     */
    private ArrayList<AnimationRoute> processServerResponse() {

        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        if (!response.status.equals(Status.OK)) {
            draw = false;
            if (response.msg.equals("database sleep"))
                readerSender.serverDied();
        }
        return response.animationRouteList;
    }


    /**
     * method to initialize a pop-up window with info about route
     *
     * @param animationRoute - animation route
     */
    private void initializeRouteInfoScene(AnimationRoute animationRoute) {
        try {
            askRoute(animationRoute);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/r.fxml"));
            Parent root = loader.load();
            InfoController controller = loader.getController();
            Route r = buildRoute();

            controller.id.setText(String.valueOf(r.getId()));
            controller.username.setText(r.getUser().getUsername());
            controller.coords.setText(String.valueOf(r.getCoordinates()));
            controller.from.setText(getPrettyFrom(animationRoute, r));
            controller.to.setText(getPrettyTo(animationRoute, r));

            controller.date.setText(r.getCreationDate());

            controller.distance.setText(String.valueOf(r.getDistance()));

            for (TextField textField : Arrays.asList(controller.distance, controller.to, controller.from, controller.coords, controller.id, controller.date, controller.username)) {
                textField.setEditable(false);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * method that sends a request for server to get a specific route
     *
     * @param animationRoute- animation route
     */
    private void askRoute(AnimationRoute animationRoute) {
        Request request = new Request();
        request.setArgs(List.of("routeinfo", String.valueOf(animationRoute.getId())));
        readerSender.sendToServer(request);
    }

    /**
     * method which collects data from server and draws routes
     */
    private void animate() {
        System.out.println("initializing animation...");
        requestRoutes();
        ArrayList<AnimationRoute> routelist = processServerResponse();
        for (AnimationRoute animationRoute : routelist) {
            drawRoutes(animationRoute);
        }
        drawRoutes(routelist);
        clearCanvas();
    }

    /**
     * methos that build route from server's response
     *
     * @return Route
     */
    private Route buildRoute() {
        Response response = readerSender.read();
        System.out.println(response.route);
        return response.route;
    }

    /**
     * two methods to get a pretty view of location(from) and location(to)
     *
     * @param route - animation route
     * @param r     - ulits route
     * @return pretty strings
     */
    private String getPrettyFrom(AnimationRoute route, Route r) {
        return "x=" + route.getFromX() +
                ", y=" + route.getFromY() +
                ", name='" + r.getFrom().getName();
    }

    private String getPrettyTo(AnimationRoute route, Route r) {
        return "x=" + route.getToX() +
                ", y=" + route.getToY() +
                ", name='" + r.getTo().getName();
    }

    /**
     * method to draw route
     *
     * @param animationRoute - animation route
     */
    public void drawRoutes(AnimationRoute animationRoute) {
        Path path = createPath(animationRoute);
        Color color = ColorConverter.color(animationRoute.getColor());
        userColour.setFill(ColorConverter.color(animationRoute.getColor()));
        Animation animation = createPathAnimation(path, Duration.seconds(10), color);
        animation.play();
    }


    /**
     * method to create Path
     *
     * @param animationRoute - animation route
     * @return path
     */
    private Path createPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 500, -animationRoute.getFromY() + 300),
                        new LineTo(animationRoute.getToX() + 500, -animationRoute.getToY() + 300));
        return path;
    }


    /**
     * methos to draw transparent path
     *
     * @param animationRoute - animation route
     * @return path
     */
    private Path drawPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.setStroke(ColorConverter.transparentColor(animationRoute.getColor()));
        path.setStrokeWidth(10);
        path.setOnMouseClicked(e -> initializeRouteInfoScene(animationRoute));
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 515, -animationRoute.getFromY() + 360),
                        new LineTo(animationRoute.getToX() + 515, -animationRoute.getToY() + 360));
        return path;
    }


    /**
     * old method to build route from Dequeue<Route>
     * @param event
     */
//    private utils.Route buildRoute(int id) {
//        Response response = readerSender.read();
//        for (Route route : response.collection) {
//            if (route.getId() == id) {
//                System.err.println("ROUTE FOUND");
//                return route;
//            }
//        }
//        System.out.println("ya yeban");
//        return null;
//    }


    /**
     * method to draw all routes
     *
     * @param routelist list of animation routes
     */
    private void drawRoutes(ArrayList<AnimationRoute> routelist) {
        for (AnimationRoute animationRoute : routelist) {
            pane.getChildren().add(drawPath(animationRoute));
        }
    }

    /**
     * methos to create path animation
     *
     * @param path     path
     * @param duration time of animation
     * @param color    - color of the route
     * @return path animation
     */

    private Animation createPathAnimation(Path path, Duration duration, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            Location oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                // skip starting at 0/0
                if (oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if (oldLocation == null) {
                    oldLocation = new Location();
                    oldLocation.x = x;
                    oldLocation.y = y;
                    return;
                }

                // draw line
                gc.setStroke(color);
                //gc.setFill(Color.YELLOW);
                gc.setLineWidth(10);
                gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });
        return pathTransition;
    }

    private static class Location {
        double x;
        double y;
    }

    /**
     * method to clear canvas
     */
    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * method to draw axis
     */
    private void drawAxis() {
        Line x = new Line(500, 60, 500, 600);
        Line y = new Line(0, 300, 1000, 300);
        x.setStrokeWidth(2);
        y.setStrokeWidth(2);
        x.setStroke(Color.GRAY);
        y.setStroke(Color.GRAY);
        pane.getChildren().addAll(x, y);
    }
}