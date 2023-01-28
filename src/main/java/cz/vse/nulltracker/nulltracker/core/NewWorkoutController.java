package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import cz.vse.nulltracker.nulltracker.database.LoggedActivity;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.UnaryOperator;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;


/**
 * @author Martin Kadlec
 * @version Last refactor on 28.01.2023
 *
 * <p>
 *     Controller for the newWorkout FXML.
 *     Handles the necessary UI operations to limit the User input
 *     only to expected values.
 *     Parses the core data from the exercises.json file.
 *     Handles the rest dynamically.
 * </p>
 *
 * @see LoggedUser
 * @see LoggedActivity
 * @see cz.vse.nulltracker.nulltracker.database.DatabaseHandler
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
    public VBox summaryVBox;
    public Text kcalText;
    public Text dateText;

    Random random = new Random();
    double kcalValue;
    Locale locale = new Locale("cs", "CZ");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE DD.MM.YYYY", locale);

    /**
     * Programmatically sets all the JFX elements into their desired states.
     * Loads the exercises into required format.
     * Specifies the selection behaviors of UI elements.
     *
     * @throws FileNotFoundException If resource does not exist
     */
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

        timestampCalendar.setValue(LocalDate.now());
        timestampCalendar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                timestampCalendar.setValue(newValue);
                dateText.setText(dateFormat.format(Date.from(timestampCalendar.getValue().atStartOfDay(ZoneOffset.UTC).toInstant())));
            }
        });
        dateText.setText(dateFormat.format(Date.from(timestampCalendar.getValue().atStartOfDay(ZoneOffset.UTC).toInstant())));



    }


    /**
     * Disables all text fields.
     */
    private void disableInactiveFields() {
        attribute1Field.setDisable(true);
        attribute2Field.setDisable(true);
        attribute3Field.setDisable(true);
        attribute4Field.setDisable(true);
    }

    /**
     * Clears all text prompts from the fields.
     */
    private void cleanUpPrompts() {
        attribute1Field.setPromptText("");
        attribute2Field.setPromptText("");
        attribute3Field.setPromptText("");
        attribute4Field.setPromptText("");
    }

    /**
     * Cleans all text field labels
     */
    private void cleanUpLabels () {
        attribute1.setText("");
        attribute2.setText("");
        attribute3.setText("");
        attribute4.setText("");
    }


    /**
     * Clears all data currently in fields
     * this is mostly done to prevent incorrect data transmissions.
     */
    private void cleanUpFields() {
        attribute1Field.clear();
        attribute2Field.clear();
        attribute3Field.clear();
        attribute4Field.clear();
    }

    /**
     * Appends the data currently in fields in the temporary
     * array list for further manipulation.
     * Clears UI elements after saving,
     * recalculates KCAL values.
     */
    public void appendLog() {
        Map<String,Double> placeholderMap = new HashMap<>();

        if (!attribute1Field.isDisable()) placeholderMap.put(attribute1.getText(), Double.valueOf(attribute1Field.getText()));
        if (!attribute2Field.isDisable()) placeholderMap.put(attribute2.getText(), Double.valueOf(attribute2Field.getText()));
        if (!attribute3Field.isDisable()) placeholderMap.put(attribute3.getText(), Double.valueOf(attribute3Field.getText()));
        if (!attribute4Field.isDisable()) placeholderMap.put(attribute4.getText(), Double.valueOf(attribute4Field.getText()));
        activityLog.add(new LoggedActivity(activitySelector.getValue().getName(),placeholderMap));


        cleanUpFields();
        cleanUpLabels();
        cleanUpPrompts();
        activitySelector.getSelectionModel().clearSelection();
        refreshSummaryVBox();
        recalculateKcal();
    }

    private void recalculateKcal() {
        kcalValue = activityLog.size() * 0.67 * Math.abs(random.nextInt(50));
        kcalValue = Math.round(kcalValue);
        kcalText.setText("Celkem " + kcalValue + " kcal");
    }

    /**
     * Drops all currently saved data.
     */
    public void dropLog() {
        timestampCalendar.getEditor().clear();
        activityLog.clear();
        activitySelector.getSelectionModel().clearSelection();
        disableInactiveFields();
        cleanUpLabels();
        cleanUpPrompts();
        cleanUpFields();
        refreshSummaryVBox();
        recalculateKcal();
    }

    /**
     * Attempts to save cached data to the DB.
     * Checks if log is not empty and if it has timestamp.
     * Iterates over each element in the array and creates appropriate JSON
     * DB counterpart.
     * Batches them together into single document, stamped with user object ID and stamp.
     * Clears buffers and UI upon creation of the log.
     * Check wiki for further DB info.
     *
     */
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
        activityLog.clear();
        refreshSummaryVBox();
        recalculateKcal();
    }

    /**
     * Warns user of incorrect DB behavior.
     *
     * @param message Text to be displayed
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba při logování!");
        alert.setHeaderText("Chyba při logování!");
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void refreshSummaryVBox(){
        summaryVBox.getChildren().clear();
        activityLog.forEach(loggedActivity -> {
            HBox loggedActivityHBox = new HBox();
            loggedActivityHBox.setSpacing(20);
            loggedActivityHBox.setAlignment(Pos.CENTER_LEFT);

            ImageView crossIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/icon_cross_black.png")), 8, 8, true, true));
            crossIcon.setOnMouseClicked(event -> {
                activityLog.remove(loggedActivity);
                refreshSummaryVBox();
                recalculateKcal();
            });
            crossIcon.setCursor(Cursor.HAND);
            loggedActivityHBox.getChildren().add(crossIcon);

            Text activityName = new Text(loggedActivity.getName());
            activityName.getStyleClass().add("bold");
            loggedActivityHBox.getChildren().add(activityName);

            loggedActivity.getParameters().forEach((key, value) -> {
                Text parameter = new Text(value + " " + key);
                parameter.getStyleClass().add("opacity-50");
                loggedActivityHBox.getChildren().add(parameter);
            });

            summaryVBox.getChildren().add(loggedActivityHBox);
        });
        kcalText.setText("Celkem " + kcalValue + " kcal");
    }


}
