package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

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
