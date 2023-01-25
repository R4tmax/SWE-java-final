package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

/**
 * @author Martin Kadlec, Michal Průcha
 * @version Last refactor 25.1
 * <p>
 * Controller for the login_view FXML file.
 * It is the first screen opened upon program launch.</p>
 *
 * @see cz.vse.nulltracker.nulltracker.database.DatabaseHandler
 */
public class LoginController {
    public Button submitButton;
    public TextField emailInput;
    public PasswordField passwordInput;
    private final Stage stage = Main.getStage();
    @FXML
    private void linkToRegistration() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("registration");
    }

    @FXML
    private void linkToDashboard() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("dashboard");
    }

    /**
     * Accepts text values from the related fields
     * and interprets them against the contents of the "users"
     * collection. Call to the DB is made via DatabaseHandler client
     *
     * @see cz.vse.nulltracker.nulltracker.database.DatabaseHandler
     */
    public void attemptLogin() {

        String login = emailInput.getText();
        String pass = passwordInput.getText();


        try  {
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.regex("login", login);

            Document entry = collection.find(filter).first();

            if (entry == null) {
                System.out.println("Incorrect login");
            } else {
                if(!Objects.equals(entry.getString("password"), pass)) {
                    System.out.println("Incorrect password");
                } else {
                    System.out.println("Login successful!");
                    LoggedUser.saveUserData(entry.getString("name"),entry.getString("login"),entry.getObjectId("_id"));
                    linkToDashboard();
                }
            }

        } catch (Exception e) {
            System.out.println("DB error" + e);
        }

    }


}
