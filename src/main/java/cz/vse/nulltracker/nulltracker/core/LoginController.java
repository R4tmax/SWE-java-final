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

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

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
                    linkToDashboard();
                }
            }

        } catch (Exception e) {
            System.out.println("DB error" + e);
        }

    }


}
