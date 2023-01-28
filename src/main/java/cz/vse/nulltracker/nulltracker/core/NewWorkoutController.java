package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

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

    public void initialize () throws FileNotFoundException {

        //initial UI clear (done in order to avoid FXML manipulation)
        cleanUpPrompts();
        cleanUpLabels();
        disableInactiveFields(true);

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
                allExercises.add(new Exercise(name, description, parameters));
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
                disableInactiveFields(true);

                if (iterator < control) {
                    attribute1.setText(newValue.getParameters() != null ? newValue.getParameters().get(0) : "");
                    attribute1Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute2.setText(newValue.getParameters() != null ? newValue.getParameters().get(1) : "");
                    attribute2Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute3.setText(newValue.getParameters() != null ? newValue.getParameters().get(2) : "");
                    attribute3Field.setDisable(false);
                    iterator++;
                }

                if (iterator < control) {
                    attribute4.setText(newValue.getParameters() != null ? newValue.getParameters().get(3) : "");
                    attribute4Field.setDisable(false);
                }
            }

        });
    }

    private void disableInactiveFields(boolean b) {
        attribute1Field.setDisable(b);
        attribute2Field.setDisable(b);
        attribute3Field.setDisable(b);
        attribute4Field.setDisable(b);
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

    public void appendLog(MouseEvent mouseEvent) {
    }

    public void dropLog(MouseEvent mouseEvent) {
    }

    public void saveLog(MouseEvent mouseEvent) {
    }
}
