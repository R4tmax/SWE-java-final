package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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

public class RegistrationController {
    public Button submitButton;
    public TextField nameInput;
    public TextField emailInput;
    public PasswordField passwordFirstInput;
    public PasswordField passwordSecondInput;
    private final Stage stage = Main.getStage();
    @FXML
    private void linkToLogin() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("login");
    }

    public void attemptRegistration() {

        String name = nameInput.getText();
        String login = emailInput.getText();
        String pass = passwordFirstInput.getText();
        String secondPass = passwordSecondInput.getText();


        try {
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.regex("login", login);

            Document entry = collection.find(filter).first();

            if (entry != null) {
                System.out.println("User already exists");
                return;
            }

            if (!login.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                System.out.println("Incorrect email format!");
                return;
            }

            if (!Objects.equals(pass, secondPass)) {
                System.out.println("Passwords do not match");
                return;
            }

            if (!isPassSafe(pass)) {
                System.out.println("Password is not safe enough");
                System.out.println("Password should contain at least 4 characters, one number and one letter");
                return;
            }


            Document document = new Document("login", login)
                    .append("password", pass).append("name", name);

            collection.insertOne(document);
            ObjectId objectId = document.getObjectId("_id");

            System.out.println("Created user:" + objectId);

        } catch (Exception e) {
            System.out.println("DB error" + e);
        }
    }


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
