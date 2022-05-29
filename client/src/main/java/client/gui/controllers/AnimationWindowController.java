package client.gui.controllers;

import client.gui.StartingStage;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import json.ColorConverter;
import lombok.SneakyThrows;
import org.postgresql.replication.fluent.CommonOptions;
import utils.animation.Route;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;


public class AnimationWindowController extends AbstractController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane pane;

    public void drawFloppa(Route route) {
        Path path = createPath(route);


        Color color = ColorConverter.color(route.getColor());

        Animation animation = createPathAnimation(path, Duration.seconds(10), color);
        animation.play();
    }

    private Path createPath(Route route) {
        Path path = new Path();
        path.getElements().addAll
                (new MoveTo(route.getFromX() + 579, -route.getFromY() + 300),
                        new LineTo(route.getToX() + 579, -route.getToX() + 300));
        return path;
    }


    private Path drawPath(Route route) {
        Path path = new Path();
        path.setStroke(ColorConverter.transparentColor(route.getColor()));
        path.setStrokeWidth(10);
        path.setOnMouseClicked(e -> {
            sendIdToInfo(route);
            popUpWindow("/client/info.fxml");

        });
        path.getElements().addAll
                (new MoveTo(route.getFromX() + 630, -route.getFromY() + 350),
                        new LineTo(route.getToX() + 630, -route.getToX() + 350));
        return path;
    }

    private void sendIdToInfo(Route route) {
        try {
            FXMLLoader loader = new FXMLLoader(StartingStage.class.getResource("/client/info.fxml"));
            loader.load();
            InfoController info = loader.getController();
            info.routeId = route.getId();
            System.out.println("INFO ROUTE ID: " + info.routeId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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


//    private void moveFloppa(double fromX, long fromY, int toX, float toY) {
//        TranslateTransition translate = new TranslateTransition();
//        //translate.setNode(floppa);
//        translate.setDuration(Duration.seconds(10));
//        translate.setCycleCount(TranslateTransition.INDEFINITE);
//        translate.setFromX(fromX);
//        translate.setFromY(fromY);
//        translate.setByX(toX);
//        translate.setByY(toY);
//        translate.interpolatorProperty();
//        translate.setAutoReverse(true);
//        translate.play();
//    }

    @FXML
    public void go_back(ActionEvent event) {
        switchStages(event, "/client/actionChoice.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initializing animation scene...");
        requestRoutes();
        System.out.println("requesting data from server...");
        ArrayList<Route> routelist = processServerResponse();
        System.out.println("getting routelist...");
        for (Route route : routelist) {
            drawFloppa(route);
        }
        System.out.println("paths are drawn");
        drawRoutes(routelist);

    }

    private void drawRoutes(ArrayList<Route> routelist) {
        for (Route route : routelist) {
            pane.getChildren().add(drawPath(route));
        }
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

    private ArrayList<Route> processServerResponse() {

        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        if (!response.status.equals(Status.OK)) {
            draw = false;
        }

        return response.routeList;
    }

    private static class Location {
        double x;
        double y;
    }

}
