package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartingStage extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setResizable(false);
            stage.setMinHeight(400.0);
            stage.setMinWidth(650.0);
            stage.setTitle("лаба 8...");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> Platform.exit());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
