package client.gui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class Main extends Application {
    //public void start(Stage stage) {
    //Drawing the shape
//        MoveTo moveTo = new MoveTo(108, 71); //начальная точка
//        LineTo line1 = new LineTo(371, 161); // они все так сгруппированны что если одного двигать то подвинется и другой
//        LineTo line2 = new LineTo(126, 232);
//        LineTo line3 = new LineTo(232, 52);
//        LineTo line4 = new LineTo(269, 250);
//        LineTo line5 = new LineTo(108, 71); //конечная точка
//        //Creating a Path
//        Path path = new Path(moveTo, line1, line2, line3, line4, line5);
//        //Preparing the Stage object
//        Group root = new Group(path);
//        Scene scene = new Scene(root, 600, 300);
//        stage.setTitle("Drawing an arc through a path");
//        stage.setScene(scene);/*  w w w    .d    em   o  2   s  . c   o m */
//        stage.show();


    //}

    //    public static void main(String args[]) {
//        launch(args);
//    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/client/sample.fxml"));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}