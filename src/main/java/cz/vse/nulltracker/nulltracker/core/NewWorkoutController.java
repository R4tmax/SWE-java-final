package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import cz.vse.nulltracker.nulltracker.database.LoggedActivity;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.scene.control.*;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.UnaryOperator;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

//TODO:DOCS
//TODO:Save the activity

/**
 * @author Martin Kadlec
 * @version Last refactor on 28.01.2023
 *
 *
 */
public class NewWorkoutController {
    public ComboBox<Exercise> activitySelector;
    public DatePicker timestampCalendar;
    public Label attribute1;
    public Label attribute2;
    public Label attribute3;
    public Label attribute4;
    public TextField attribute1Field;
    public TextField attribute2Field;
    public TextField attribute3Field;
    public TextField attribute4Field;
    public Button buttonSaveLog;
    public Button buttonDropLog;
    public Button buttonAppendLong;

    public ArrayList<LoggedActivity> activityLog = new ArrayList<>();

    Random random = new Random();
    double kcalValue;

    public void initialize () throws FileNotFoundException {

        //format the textFields
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        attribute1Field.setTextFormatter(new TextFormatter<>(filter));
        attribute2Field.setTextFormatter(new TextFormatter<>(filter));
        attribute3Field.setTextFormatter(new TextFormatter<>(filter));
        attribute4Field.setTextFormatter(new TextFormatter<>(filter));


        //initial UI clear (done in order to avoid FXML manipulation)
        cleanUpPrompts();
        cleanUpLabels();
        disableInactiveFields();

        //read and parse the contents of the JSON
        ArrayList<Exercise> allExercises = new ArrayList<>();
        File input = new File("src/main/resources/cz/vse/nulltracker/nulltracker/core/exercises.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonArray categories = fileObject.get("workouts").getAsJsonArray();

        categories.forEach(category -> {
            JsonObject categoryObject = category.getAsJsonObject();
            categoryObject.get("exercises").getAsJsonArray().forEach(exercise -> {
                JsonObject exerciseObject = exercise.getAsJsonObject();
                String name = exerciseObject.get("name").getAsString();
                String description = exerciseObject.get("description").getAsString();
                ArrayList<String> parameters = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : exerciseObject.get("parameters").getAsJsonObject().entrySet()) {
                    parameters.add(entry.getKey());
                }
                ArrayList<String> prompts = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : exerciseObject.get("parameters").getAsJsonObject().entrySet()) {
                    prompts.add(entry.getValue().getAsString());
                }
                allExercises.add(new Exercise(name, description, parameters,prompts));
            });
        });

        //process the data into the ComboBox
        activitySelector.getItems().addAll(allExercises);
        activitySelector.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    cleanUpFields();
                    setText(item.getName());
                }
            }
        });

        // update the labels with the information from the selected exercise
        activitySelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int iterator = 0;
                int control = newValue.getParameters().size();
                cleanUpLabels();
                cleanUpPrompts();
                disableInactiveFields();

                if (iterator < control) {
                    attribute1.setText(newValue.getParameters() != null ? newValue.getParameters().get(0) : "");
                    attribute1Field.setPromptText(newValue.getPrompts() != null ? newValue.getPrompts().get(0) : "");
                    attribute1Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute2.setText(newValue.getParameters() != null ? newValue.getParameters().get(1) : "");
                    attribute2Field.setPromptText(newValue.getPrompts() != null ? newValue.getPrompts().get(1) : "");
                    attribute2Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute3.setText(newValue.getParameters() != null ? newValue.getParameters().get(2) : "");
                    attribute3Field.setPromptText(newValue.getPrompts() != null ? newValue.getPrompts().get(2) : "");
                    attribute3Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute4.setText(newValue.getParameters() != null ? newValue.getParameters().get(3) : "");
                    attribute4Field.setPromptText(newValue.getPrompts() != null ? newValue.getPrompts().get(3) : "");
                    attribute4Field.setDisable(false);
                }
            }

        });
    }


    private void disableInactiveFields() {
        attribute1Field.setDisable(true);
        attribute2Field.setDisable(true);
        attribute3Field.setDisable(true);
        attribute4Field.setDisable(true);
    }

    private void cleanUpPrompts() {
        attribute1Field.setPromptText("");
        attribute2Field.setPromptText("");
        attribute3Field.setPromptText("");
        attribute4Field.setPromptText("");
    }

    private void cleanUpLabels () {
        attribute1.setText("");
        attribute2.setText("");
        attribute3.setText("");
        attribute4.setText("");
    }


    private void cleanUpFields() {
        attribute1Field.clear();
        attribute2Field.clear();
        attribute3Field.clear();
        attribute4Field.clear();
    }

    public void appendLog() {
        Map<String,Double> placeholderMap = new HashMap<>();

        if (!attribute1Field.isDisable()) placeholderMap.put(attribute1.getText(), Double.valueOf(attribute1Field.getText()));
        if (!attribute2Field.isDisable()) placeholderMap.put(attribute2.getText(), Double.valueOf(attribute2Field.getText()));
        if (!attribute3Field.isDisable()) placeholderMap.put(attribute3.getText(), Double.valueOf(attribute3Field.getText()));
        if (!attribute4Field.isDisable()) placeholderMap.put(attribute4.getText(), Double.valueOf(attribute4Field.getText()));
        activityLog.add(new LoggedActivity(activitySelector.getValue().getName(),placeholderMap));
        kcalValue = activityLog.size() * 0.67  * random.nextInt(5);

        cleanUpFields();
        cleanUpLabels();
        cleanUpPrompts();
        activitySelector.getSelectionModel().clearSelection();
    }

    public void dropLog() {
        timestampCalendar.getEditor().clear();
        activityLog.clear();
        activitySelector.getSelectionModel().clearSelection();
        disableInactiveFields();
        cleanUpLabels();
        cleanUpPrompts();
        cleanUpFields();
    }

    public void saveLog() {
        MongoCollection<Document> collection = database.getCollection("logs");
        ObjectId userId = LoggedUser.LUID;

        if (timestampCalendar.getValue() == null) {
            showErrorMessage("Není upřesněno datum!");
            return;
        }

        LocalDate date = timestampCalendar.getValue();
        Instant instant = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        BsonTimestamp bsonTimestamp = new BsonTimestamp((int) instant.getEpochSecond(), 0);

        Document logDocument = new Document("belongsTo",userId).append("timestamp",bsonTimestamp);

        if (activityLog.size() == 0) {
            showErrorMessage("Záznam je prádzný!");
            return;
        }

        for (LoggedActivity arrayElement: activityLog) {
            String activityName = arrayElement.getName();
            for (Map.Entry<String,Double> entry : arrayElement.getParameters().entrySet()) {
                String key = entry.getKey();
                Double value = entry.getValue();

                logDocument.append(key+activityName,value);
            }
        }

        logDocument.append("KCAL",kcalValue);

        try {
            collection.insertOne(logDocument);
        } catch (Exception e) {
            System.out.println("DB error" + e);
        }


        //TODO: redirect to log history/dashboard
        System.out.println("Log creation successful!");

        timestampCalendar.getEditor().clear();
        activitySelector.getSelectionModel().clearSelection();
        disableInactiveFields();
        cleanUpLabels();
        cleanUpPrompts();
        cleanUpFields();

    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba při logování!");
        alert.setHeaderText("Chyba při logování!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
