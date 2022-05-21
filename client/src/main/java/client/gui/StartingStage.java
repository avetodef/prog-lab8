package client.gui;

import client.ClientApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartingStage extends Application implements Runnable {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setResizable(false);
            stage.setMinHeight(400.0);
            stage.setMinWidth(640.0);
            stage.setTitle("лаба 8...");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> Platform.exit());

//            ClientApp client = new ClientApp();
//            client.runClient();

//            ClientApp client = new ClientApp();
//            client.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
//        ClientApp client = new ClientApp();
//        client.runClient();
    }

    @Override
    public void run() {
        launch();
    }


}