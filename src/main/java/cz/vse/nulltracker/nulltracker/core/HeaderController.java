package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

    public void updateName () {
        userTracker.setText(LoggedUser.LUname);
    }

}