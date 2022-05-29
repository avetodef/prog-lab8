package commands;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;

public class GifRzhaka extends Application implements Runnable{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(createScene());
        stage.setTitle("Animated GIF Demo");
        stage.setWidth(400);
        stage.setHeight(300);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();
    }

    private Scene createScene() {
        try {
            ImageIcon imageIcon = new ImageIcon("sad-monkey.gif");
            JLabel imageLabel = new JLabel(imageIcon);
            SwingNode swingNode = new SwingNode();
            swingNode.setContent(imageLabel);
            BorderPane borderPane = new BorderPane(swingNode);
            return new Scene(borderPane);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        launch();
    }
}