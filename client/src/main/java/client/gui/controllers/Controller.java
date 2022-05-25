package client.gui.controllers;

import javafx.application.Application;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application {
    private static double SCENE_WIDTH = 400;
    private static double SCENE_HEIGHT = 260;

    Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = new Pane();
        Path path = createPath();
        canvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);

        root.getChildren().addAll(path, canvas);

        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();

        Animation animation = createPathAnimation(path, Duration.seconds(5));
        animation.play();
    }

    private Path createPath() {

        Path path = new Path();

        path.setStroke(Color.RED);
        path.setStrokeWidth(10);

        path.getElements().addAll
                (new MoveTo(20, 20),
//                        new CubicCurveTo(380, 0, 380, 120, 200, 120),
//                        new CubicCurveTo(0, 120, 0, 240, 380, 240),
                        new LineTo(100, 100));

        return path;
    }

    private Animation createPathAnimation(Path path, Duration duration) {

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
                gc.setStroke(Color.BLUE);
                gc.setFill(Color.YELLOW);
                gc.setLineWidth(4);
                gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });

        return pathTransition;
    }

    public static class Location {
        double x;
        double y;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//package client.gui.controllers;
//import javafx.animation.RotateTransition;
//import javafx.animation.TranslateTransition;
//import javafx.application.Platform;
//import javafx.concurrent.Task;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ProgressBar;
//import javafx.scene.layout.Pane;
//import javafx.scene.shape.LineTo;
//import javafx.scene.shape.MoveTo;
//import javafx.scene.shape.Path;
//import javafx.util.Duration;
//
//public class Controller {
//    @FXML
//    private Pane idPane;
//    @FXML
//    private Label label;
//    @FXML
//    private Button buttonStart;
//    @FXML
//    private Button buttonStop;
//
//    private Task<Void> task;
//
//    @FXML
//    private void initialize() {
//        startDraw(null);
//    }
//
//    private Task<Void> createTask() {
//        return new Task<Void>() {
//            @Override
//            protected Void call() {
//                try {
//                    draw();
//                } catch (Exception ex) {
//                    updateMessage(ex.getMessage());
//                }
//                return null;
//            }
//
//            private void draw() {
//                float step = 0.2f;
//                float mash = 20;
//                Platform.runLater(() -> idPane.getChildren().clear());
//                for (float i = 0; i < 430 / mash; i += step) {
//                    if (isCancelled()) {
//                        return;
//                    }
//                    Platform.runLater(new Runnable() {
//                        float i;
//
//                        @Override
//                        public void run() {
////                            MoveTo moveTo = new MoveTo(i * mash + 100, Math.sin(i) * mash + 100); //+10 во втором параметре конструктора делают длиннее или короче линию рисуночка
////                            LineTo lineTo = new LineTo(((i + step) * mash) + 101, Math.sin(i + step) * mash + 100);
//                            MoveTo moveTo = new MoveTo(i * mash + 70, i * mash + 80); //+10 во втором параметре конструктора делают длиннее или короче линию рисуночка
//                            LineTo lineTo = new LineTo(((i + step) * mash) + 70, (i + step) * mash + 80);
//
//                            Path path = new Path(moveTo, lineTo);
//                            idPane.getChildren().add(path);
//                            TranslateTransition translate = new TranslateTransition();
//                            translate.setNode(path);
//                            translate.setDuration(Duration.seconds(1));
//                            //translate.setCycleCount(TranslateTransition.INDEFINITE);
//                            translate.setByX(20);
//                            translate.setByY(20);
//                            translate.interpolatorProperty();
//                            translate.setAutoReverse(true);
//                            translate.play();
////                            RotateTransition rotate = new RotateTransition();
////                            rotate.setNode(path);
////                            rotate.setDuration(Duration.seconds(2));
////                            rotate.setCycleCount(RotateTransition.INDEFINITE);    //развлекуха
////                            rotate.interpolatorProperty();
////                            rotate.setByAngle(360);
////                            rotate.play();
////                            updateProgress(i, 430 / mash);
//                        }
//
//                        Runnable param(float i) {
//                            this.i = i;
//                            return this;
//                        }
//                    }.param(i));
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException interrupted) {
//                        if (isCancelled()) {
//                            updateMessage("Рисование прервано");
//                            return;
//                        }
//                    }
//                }
//                updateMessage("Рисование завершено");
//            }
//
//            @Override
//            protected void updateMessage(String message) {
//                System.out.println(message);
//                super.updateMessage(message);
//            }
//        };
//    }
//
//    public void startDraw(ActionEvent event) {
//        if (task != null && task.isRunning()) {
//            task.cancel();
//        }
//
//        task = createTask();
//        Thread thread = new Thread(task);
//        thread.setDaemon(true);
//        thread.start();
//
//        label.textProperty().bind(task.messageProperty());
//
//        buttonStart.disableProperty().bind(task.runningProperty());
//        buttonStop.disableProperty().bind(task.runningProperty().not());
//    }
//
//    public void cancelDraw(ActionEvent event) {
//        if (task != null)
//            task.cancel();
//    }
//}
