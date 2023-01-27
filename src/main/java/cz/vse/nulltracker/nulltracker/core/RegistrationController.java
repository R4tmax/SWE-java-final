package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class RegistrationController {
    public Button submitButton;
    public TextField nameInput;
    public TextField emailInput;
    public PasswordField passwordFirstInput;
    public PasswordField passwordSecondInput;
    private final Stage stage = Main.getStage();
    public Label infoMessage;

    @FXML
    private void linkToLogin() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("login");
    }

    /**
     * <p>Accepts string values from the given FE fields
     * Accepts the login and validates against the existing DB entries
     * for duplicities. Then, checks that the email given conforms to the basic norms using regex
     * and that the password given is usable and of at least some use.</p>
     *
     * @see cz.vse.nulltracker.nulltracker.database.DatabaseHandler
     * @see #isPassSafe(String)
     */
    public void attemptRegistration() {

        String name = nameInput.getText();
        String login = emailInput.getText();
        String pass = passwordFirstInput.getText();
        String secondPass = passwordSecondInput.getText();

        infoMessage.setStyle("-fx-text-fill: red");
        infoMessage.setVisible(true);

        try {
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.regex("login", login);

            Document entry = collection.find(filter).first();


            if (entry != null) {
                System.out.println("User already exists");
                infoMessage.setText("Uživatel již existuje");
                return;
            }

            if (!login.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                System.out.println("Incorrect email format!");
                infoMessage.setText("nesprávný formát emailu");
                return;
            }

            if (!Objects.equals(pass, secondPass)) {
                System.out.println("Passwords do not match");
                infoMessage.setText("Hesla se neshodují");
                return;
            }

            if (!isPassSafe(pass)) {
                System.out.println("Password is not safe enough");
                System.out.println("Password should contain at least 4 characters, one number and one letter");
                infoMessage.setText("Heslo musí obsahovat minimálně" + "\n" +"4 znaky a obsahovat číslo");
                return;
            }


            Document document = new Document("login", login)
                    .append("password", pass).append("name", name);

            collection.insertOne(document);
            ObjectId objectId = document.getObjectId("_id");

            System.out.println("Created user:" + objectId);

            infoMessage.setStyle("-fx-text-fill: green");
            infoMessage.setText("Uživatel vytvořen");

        } catch (Exception e) {
            System.out.println("DB error" + e);
        }
    }


    /**
     * @see #attemptRegistration()
     *
     * Auxiliary method for the attemptRegistration.
     * Before allowing creation, password is checked for at least basic safety
     * measures
     *
     * @param passToCheck String with the password to be validated
     * @return Boolean representation of the password evaluation, false if criteria is not met, true if password is usable
     */
    private boolean isPassSafe (String passToCheck) {

        boolean hasDigit = false;
        boolean hasLetter = false;

        if (passToCheck.length() <= 4) return false;

        for (char c : passToCheck.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasDigit) return false;

        for (char c : passToCheck.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
        }

        return hasLetter;
    }

}
