package cz.vse.nulltracker.nulltracker.core;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * @author Martin Kadlec, Michal Průcha
 * @version Last refactor on 25.1
 *
 * <p>Controller for the registration_view FXML file.
 * Handles user creation in tandem with the DB.
 * Take note that all data calls are made against the
 * MongoDB remote system.</p>
 */
public class DashboardController {
    private final Stage stage = Main.getStage();
    public void toNewWorkoutPage(ActionEvent actionEvent) {
        Main main = (Main) stage.getUserData();
        main.navigateTo("newWorkout");
    }
}
