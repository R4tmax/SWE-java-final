package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Michal Průcha, Martin Kadlec
 * @version Last Refactor on 27.01.2023
 *
 * <p> Controller for the header fxml file</p>
 */
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


    /**
     * Initializes periodic calls to the updateName
     * method.
     */
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> updateName()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Sets the text of the hyperlink to the name of the user
     * take not that there is a slight delay due to
     * A, call to the DB
     * B, periodicity of the Handling timeline
     * Not being in sync.
     */
    public void updateName () {
        userTracker.setText(LoggedUser.LUname);
    }

}