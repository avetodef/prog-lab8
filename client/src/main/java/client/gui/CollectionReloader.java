package client.gui;

import client.gui.controllers.AbstractController;
import client.gui.controllers.AnimationWindowController;
import client.gui.controllers.InfoController;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.ColorConverter;
import utils.Route;
import utils.animation.AnimationRoute;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class CollectionReloader extends AbstractController implements Runnable {

    @Override
    public void run() {
        System.out.println("running...");
        while (true) {
            try {
                System.out.println("прошло 5 секунд...");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        try {
//            System.out.println("initializing animation scene...");
//
//            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/client/animation.fxml"));
//            loader1.load();
//            AnimationWindowController animation = loader1.getController();
//            requestRoutes();
//
//            ArrayList<AnimationRoute> routelist = getAnimationRoutes();
//            for (AnimationRoute animationRoute : routelist) {
//                drawFloppa(animationRoute, animation.canvas);
//            }
//            drawRoutes(routelist, animation.pane);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//        try {
//            Thread.sleep(15000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

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

    private ArrayList<AnimationRoute> getAnimationRoutes() {

        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        if (!response.status.equals(Status.OK)) {
            if (response.msg.equals("database sleep"))
                readerSender.serverDied();
        }
        return response.animationRouteList;
    }

    private ArrayDeque<Route> getCollection() {
        Response response = readerSender.read();
        System.out.println(response.status + " [" + response.msg + "]");
        if (!response.status.equals(Status.OK)) {
            if (response.msg.equals("database sleep"))
                readerSender.serverDied();
        }
        return (ArrayDeque<Route>) response.collection;
    }

    public void drawFloppa(AnimationRoute animationRoute, Canvas canvas) {
        Path path = createPath(animationRoute);

        Color color = ColorConverter.color(animationRoute.getColor());

        Animation animation = createPathAnimation(path, Duration.seconds(10), color, canvas);
        animation.play();
    }

    private Path createPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 579, -animationRoute.getFromY() + 300),
                        new LineTo(animationRoute.getToX() + 579, -animationRoute.getToX() + 300));
        return path;
    }

    private Path drawPath(AnimationRoute animationRoute) {
        Path path = new Path();
        path.setStroke(ColorConverter.transparentColor(animationRoute.getColor()));
        path.setStrokeWidth(10);
        path.setOnMouseClicked(e -> sendIdToInfo(animationRoute));
        path.getElements().addAll
                (new MoveTo(animationRoute.getFromX() + 630, -animationRoute.getFromY() + 350),
                        new LineTo(animationRoute.getToX() + 630, -animationRoute.getToX() + 350));
        return path;
    }

    private void drawRoutes(ArrayList<AnimationRoute> routelist, AnchorPane pane) {
        for (AnimationRoute animationRoute : routelist) {
            pane.getChildren().add(drawPath(animationRoute));
        }
    }

//    private InfoController getInfoController(){
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/r.fxml"));
//            Parent root = loader.load();
//            return loader.getController();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("я дурак");
//        return null;
//    }

    private void sendIdToInfo(AnimationRoute animationRoute) {
        try {
            askRoute(animationRoute);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/r.fxml"));
            Parent root = loader.load();
            InfoController controller = loader.getController();

            //utils.Route r = buidRoute();
            //Route r = buildRoute(animationRoute.getId());

            Route r = buildRoute();
            System.out.println("ROUTE " + r);

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

    private Animation createPathAnimation(Path path, Duration duration, Color color, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            Loc oldLocation = null;

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
                    oldLocation = new Loc();
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

    private static class Loc {
        double x;
        double y;
    }
}
