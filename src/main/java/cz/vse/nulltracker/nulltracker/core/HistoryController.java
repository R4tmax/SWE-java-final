package cz.vse.nulltracker.nulltracker.core;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HistoryController {



    public void initialize () {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> updateCollection()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateCollection() {
    }

    public void printCollection() {

    }
}
