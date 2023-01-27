package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HeaderController {
    public Hyperlink allExercisesLink;
    private final Stage stage = Main.getStage();
    public Hyperlink userTracker;

    @FXML
    private void linkTo(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource() ;
        String data = (String) node.getUserData();
        Main main = (Main) stage.getUserData();
        main.navigateTo(data);
    }

    public void linkToHome(MouseEvent mouseEvent) {
        Main main = (Main) stage.getUserData();
        main.navigateTo("dashboard");
    }


    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateName();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void updateName () {
        userTracker.setText(LoggedUser.LUname);
    }

}