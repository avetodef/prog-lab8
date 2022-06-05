package client.gui;

import client.ClientApp;
import client.ReaderSender;
import client.gui.controllers.AuthController;
import client.gui.tool.ObservableResourse;
import console.ConsoleOutputer;
import interaction.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;


public class StartingStage extends Application implements Runnable {
    private static ObservableResourse observableResourse;
    static ClientApp client;
    public static final String BUNDLE = "bundle.gui";
    private ConsoleOutputer consoleOutputer;
    static User user;

    /*@Override
    public void initial(){
        observableResourse = new ObservableResourse();
        observableResourse.setResources(ResourceBundle.getBundle(BUNDLE));
        consoleOutputer = new ConsoleOutputer();
        try{
            user = new User()
        }
    }*/

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/client/auth.fxml"));
            Parent authLoader = fxmlLoader.load();
            Scene scene = new Scene(authLoader);
            AuthController authController = fxmlLoader.getController();
            authController.initLang(observableResourse);

            stage.setResizable(false);
            stage.setMinHeight(400.0);
            stage.setMinWidth(640.0);
            stage.setTitle("лаба 8...");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        observableResourse = new ObservableResourse();
        observableResourse.setResources(ResourceBundle.getBundle(BUNDLE));
        ReaderSender.setResourceFactory(observableResourse);
        launch(args);
    }

    @Override
    public void run() {
        launch();
    }


}
