package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Objects;

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

        if (login.isEmpty() || pass.isEmpty()) {
            showErrorMessage("Vyplňte všechna požadovaná pole.");
            return;
        }

        ConnectionString connectionString = new ConnectionString("mongodb+srv://martin_dev:wahB7g4jjP2CCJ7@nulltrackerdev.nxwgnwc.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database;

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            database = mongoClient.getDatabase("NullTracerkerDevDB");
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.regex("login", login);

            Document entry = collection.find(filter).first();

            if (entry == null) {
                passwordInput.clear();
                showErrorMessage("Zadejte prosím správné přihlašovací údaje.");
            } else {
                if(!Objects.equals(entry.getString("password"), pass)) {
                    passwordInput.clear();
                    showErrorMessage("Zadejte prosím správné přihlašovací údaje.");
                } else {
                    linkToDashboard();
                }
            }

        }

    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba při přihlášení");
        alert.setHeaderText("Chyba při přihlášení");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
