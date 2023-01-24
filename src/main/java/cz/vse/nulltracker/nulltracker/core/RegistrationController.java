package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

        ConnectionString connectionString = new ConnectionString("mongodb+srv://martin_dev:wahB7g4jjP2CCJ7@nulltrackerdev.nxwgnwc.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database;

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            database = mongoClient.getDatabase("NullTracerkerDevDB");
            MongoCollection<Document> collection = database.getCollection("users");

            Bson filter = Filters.regex("login", login);

            Document entry = collection.find(filter).first();

            if (entry != null) {
                System.out.println("User already exists");
                return;
            }

            if (!Objects.equals(pass, secondPass)) {
                System.out.println("Passwords do not match");
                return;
            }


            Document document = new Document("login", login)
                    .append("password", pass).append("name", name);

            collection.insertOne(document);
            ObjectId objectId = document.getObjectId("_id");

            System.out.println("Created user:" + objectId);

        }
    }


}
