package client.gui.controllers;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class AnimationWindowController extends AbstractController implements Initializable {
    @FXML
    private ImageView floppa;

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double fromX = 10;
        long fromY = 20;
        int toX = 300;
        float toY = 40;
        Path path = createPath(fromX, fromY, toX, toY);
        Animation animation = createPathAnimation(path, Duration.seconds(10), Color.HOTPINK);
        animation.play();

        moveFloppa(fromX, fromY - 5, toX, toY);
    }

    private Path createPath(double fromX, long fromY, int toX, float toY) {

        Path path = new Path();

        path.setStroke(Color.RED);
        path.setStrokeWidth(10);

        path.getElements().addAll
//                (new MoveTo(20, 20),
//                        new LineTo(100,100));
        (new MoveTo(fromX, fromX),
                new LineTo(toX, toY));

        return path;
    }

    private Animation createPathAnimation(Path path, Duration duration, Color color) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            Controller.Location oldLocation = null;

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
                    oldLocation = new Controller.Location();
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
        translate.setNode(floppa);
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


//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
////        //translate (moving)
//        TranslateTransition translate = new TranslateTransition();
//        translate.setNode(floppa);
//        translate.setDuration(Duration.seconds(1));
//        translate.setCycleCount(TranslateTransition.INDEFINITE);
//        translate.setByX(250);
//        translate.setByY(200);
//        translate.interpolatorProperty();
//        translate.setAutoReverse(true);
//        translate.play();
////
////        //rotate (moving)
////        RotateTransition rotate = new RotateTransition();
////        rotate.setNode(floppa);
////        rotate.setDuration(Duration.seconds(2));
////        rotate.setCycleCount(RotateTransition.INDEFINITE);
////        rotate.interpolatorProperty();
////        rotate.setByAngle(360);
////        rotate.play();
//
////        Line line = new Line();
////
////        line.setStartX();
//    }


}
