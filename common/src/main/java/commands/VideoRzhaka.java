package commands;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class VideoRzhaka extends Application implements Runnable  {
    public void start(Stage primaryStage) {
        try {
            String workingDir = System.getProperty("user.dir");
            final File f = new File(workingDir, "goodmorningAlex.mp4");

            final Media m = new Media(f.toURI().toString());
            final MediaPlayer mp = new MediaPlayer(m);
            final MediaView mv = new MediaView(mp);

            final DoubleProperty width = mv.fitWidthProperty();
            final DoubleProperty height = mv.fitHeightProperty();

            width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

            mv.setPreserveRatio(true);

            StackPane root = new StackPane();
            root.getChildren().add(mv);

            final Scene scene = new Scene(root, 500, 740);
            scene.setFill(Color.BLACK);

            primaryStage.setScene(scene);
            //primaryStage.setTitle("Full Screen Video Player");
            //primaryStage.setFullScreen(true);
            primaryStage.show();

            mp.play();

        } catch (Exception e){
            System.out.println(" ");
        }
    }

    @Override
    public void run() {
        launch();
    }

}