package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

/**
 * @author Martin Kadlec, Michal Průcha, Michal Ngyuen, Maximilián Staněk
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

    /**
     * Redirects user to the registration screen.
     */
    @FXML
    private void linkToRegistration() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("registration");
    }

    /**
     * Redirects user to the login screen
     */
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


        if (login.isEmpty() || pass.isEmpty()) {
            showErrorMessage("Vyplňte všechna požadovaná pole.");
            return;
        }


        try  {
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.eq("login", login);

            Document entry = collection.find(filter).first();

            if (entry == null) {
                passwordInput.clear();
                showErrorMessage("Zadejte prosím správné přihlašovací údaje.");
            } else {
                if(!Objects.equals(entry.getString("password"), pass)) {
                    passwordInput.clear();
                    showErrorMessage("Zadejte prosím správné přihlašovací údaje.");
                } else {
                    LoggedUser.saveUserData(entry.getString("name"),entry.getString("login"),entry.getObjectId("_id"));
                    linkToDashboard();
                }
            }

        } catch (Exception e) {
            System.out.println("DB error" + e);
            showErrorMessage("DB communication Error!");
        }

    }

    /**
     * Warns user of incorrect login behavior.
     *
     * @param message Message to be displayed to the user
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba při přihlášení!");
        alert.setHeaderText("Chyba při přihlášení!");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
