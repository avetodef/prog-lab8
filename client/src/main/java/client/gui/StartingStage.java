package client.gui;

import client.gui.controllers.AbstractController;
import client.gui.controllers.AuthController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class StartingStage extends Application implements Runnable {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AbstractController controller = fxmlLoader.getController();

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER)
                        controller.submit(new ActionEvent());
                }
            });

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
        launch(args);
    }

    @Override
    public void run() {
        launch();
    }


}
