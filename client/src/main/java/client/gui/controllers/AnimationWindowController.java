package client.gui.controllers;

import client.gui.CollectionReloader;
import interaction.Request;
import interaction.Response;
import interaction.Status;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.ColorConverter;
import lombok.Getter;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;


public class AnimationWindowController extends AbstractController implements Initializable {

    @FXML
    public Canvas canvas;

    @FXML
    public AnchorPane pane;

    //private final Response response = readerSender.read();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        animate();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        animate();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();

    }

    private void animate() {
        System.out.println("initializing animation...");
        requestRoutes();
        ArrayList<AnimationRoute> routelist = processServerResponse();
        for (AnimationRoute animationRoute : routelist) {
            drawFloppa(animationRoute);
        }
        drawRoutes(routelist);
        clearCanvas();
    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


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


    private void sendIdToInfo(AnimationRoute animationRoute) {
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

            controller.distance.setEditable(false);
            controller.to.setEditable(false);
            controller.from.setEditable(false);
            controller.coords.setEditable(false);
            controller.id.setEditable(false);
            controller.date.setEditable(false);
            controller.username.setEditable(false);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void askRoute(AnimationRoute animationRoute) {
        Request request = new Request();
        request.setArgs(List.of("routeinfo", String.valueOf(animationRoute.getId())));
        readerSender.sendToServer(request);
    }

    private Route buildRoute() {
        Response response = readerSender.read();
        System.out.println(response.route);
        return response.route;
    }

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

    public void drawFloppa(AnimationRoute animationRoute) {
        Path path = createPath(animationRoute);

        Color color = ColorConverter.color(animationRoute.getColor());
//        System.out.println(color);

        Animation animation = createPathAnimation(path, Duration.seconds(10), color);
        animation.play();
    }

    private Path createPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 579, -animationRoute.getFromY() + 300),
                        new LineTo(animationRoute.getToX() + 579, -animationRoute.getToY() + 300));
        return path;
    }


    private Path drawPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.setStroke(ColorConverter.transparentColor(animationRoute.getColor()));
        path.setStrokeWidth(10);
        path.setOnMouseClicked(e -> sendIdToInfo(animationRoute));
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 630, -animationRoute.getFromY() + 350),
                        new LineTo(animationRoute.getToX() + 630, -animationRoute.getToY() + 350));
        return path;
    }


    private utils.Route buildRoute(int id) {
        Response response = readerSender.read();
        for (Route route : response.collection) {
            if (route.getId() == id) {
                System.err.println("ROUTE FOUND");
                return route;
            }
        }
        System.out.println("ya yeban");
        return null;
    }

    @FXML
    public void go_back(ActionEvent event) {
        switchStages(event, "/client/actionChoice.fxml");
    }


    private void drawRoutes(ArrayList<AnimationRoute> routelist) {
        for (AnimationRoute animationRoute : routelist) {
            pane.getChildren().add(drawPath(animationRoute));
        }
    }

    private static class Location {
        double x;
        double y;
    }

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
}