package client.gui;

import client.gui.controllers.AnimationWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Animation extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/animation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AnimationWindowController controller = fxmlLoader.getController();

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

            System.out.println(controller.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
