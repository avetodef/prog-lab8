package client.gui.controllers;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class AnimationWindowController extends AbstractController implements Initializable {
    @FXML
    private ImageView floppa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        //translate (moving)
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(floppa);
        translate.setDuration(Duration.seconds(1));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(250);
        translate.setByY(200);
        translate.interpolatorProperty();
        translate.setAutoReverse(true);
        translate.play();

        //rotate (moving)
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(floppa);
        rotate.setDuration(Duration.seconds(2));
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.interpolatorProperty();
        rotate.setByAngle(360);
        rotate.play();
    }



}
