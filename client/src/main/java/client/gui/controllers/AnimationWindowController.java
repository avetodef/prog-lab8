package client.gui.controllers;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AnimationWindowController extends AbstractController implements Initializable {
//    @FXML
//    private ImageView floppa;

    @FXML
    private Canvas canvas;

    @FXML
    private Button draw;

    @FXML
    public void draw_route(ActionEvent event) {
//        drawFloppa(0, 0, -100, 200, Color.RED); //TODO так рисует
//        drawFloppa(0, 0, -100, -200, Color.BLUE); //TODO так рисует

    }

    private List<Route> routes = new ArrayList<>();


    public void drawFloppa(Route route) {
        // floppa = new ImageView("/images/floppa.jpg");
        System.out.println("drawing a route...");
        Path path = createPath(route);
        Animation animation = createPathAnimation(path, Duration.seconds(10), route.getColor());
        routes.add(route);
        animation.play();
        //moveFloppa(fromX, fromY , toX, toY);
    }

    private Path createPath(Route route) {
        Path path = new Path();
        path.setStroke(Color.RED);
        path.setStrokeWidth(10);
        path.getElements().addAll
                (new MoveTo(route.getFromX() + 579, -route.getFromY() + 300),
                        new LineTo(route.toX + 579, -route.getToX() + 300));
        return path;
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
                gc.setLineWidth(4);
                gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });

        return pathTransition;
    }


    private void moveFloppa(double fromX, long fromY, int toX, float toY) {
        TranslateTransition translate = new TranslateTransition();
        //translate.setNode(floppa);
        translate.setDuration(Duration.seconds(10));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setFromX(fromX);
        translate.setFromY(fromY);
        translate.setByX(toX);
        translate.setByY(toY);
        translate.interpolatorProperty();
        translate.setAutoReverse(true);
        translate.play();
    }

    @FXML
    public void go_back(ActionEvent event) {
        switchStages(event, "/client/actionChoice.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
        graphicsContext.strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);

        for (Route route : routes) {
            drawFloppa(route);
        }

        //drawFloppa(0, 0, 100, 100, Color.BLUE);
    }

    @AllArgsConstructor
    public static class Route {
        @Getter
        protected String author;
        @Getter
        protected double fromX;
        @Getter
        protected long fromY;
        @Getter
        protected int toX;
        @Getter
        protected float toY;
        @Getter
        protected Color color;

//        public Route(String author, double fromX, long fromY, int toX, float toY) {
//            this.author = author;
//            this.fromX = fromX;
//            this.fromY = fromY;
//            this.toX = toX;
//            this.toY = toY;
//        }

        public Route(double fromX, long fromY, int toX, float toY, Color color) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
            this.color = color;
        }

    }

    public static class Location {
        double x;
        double y;
    }

}
