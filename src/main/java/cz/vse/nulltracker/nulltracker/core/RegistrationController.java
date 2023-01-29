package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

/**
 * @author Martin Kadlec, Michal Průcha, Michal Nguyen, Maxmilián Staněk
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

    /**
     * Redirects user to the Login screen.
     */
    @FXML
    private void linkToLogin() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("login");
    }

    /**
     * Redirects user to the Dashboard screen of the application
     */
    @FXML
    private void linkToDashboard() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("dashboard");
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
                infoMessage.setText("Uživatel již existuje.");
                return;
            }

            if (!login.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                infoMessage.setText("Nesprávný formát emailu.");
                return;
            }

            if (!Objects.equals(pass, secondPass)) {
                infoMessage.setText("Hesla se neshodují.");
                return;
            }

            if (!isPassSafe(pass)) {
                infoMessage.setText("Heslo by mělo obsahovat alespoň" + "\n" +"4 znaky, jedno písmeno a jednu číslici.");
                return;
            }


            Document document = new Document("login", login)
                    .append("password", pass).append("name", name);

            collection.insertOne(document);
            ObjectId documentId = document.getObjectId("_id");

            infoMessage.setStyle("-fx-text-fill: green");
            infoMessage.setText("Uživatel vytvořen!");
            LoggedUser.saveUserData(document.getString("name"),document.getString("login"),documentId);
            linkToDashboard();

        } catch (Exception e) {
            System.out.println("DB error" + e);
            showDBErrorMessage();
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
    protected boolean isPassSafe (String passToCheck) {

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


    /**
     * Creates a modal window warning user of an error.
     * In case of registration, it is only called to notify the user of
     * unexpected DB error.
     */
    private void showDBErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba při přihlášení");
        alert.setHeaderText("Chyba při přihlášení");
        alert.setContentText("Chyba při komunikaci s databází!");
        alert.showAndWait();
    }

}
