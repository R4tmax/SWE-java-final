package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

//TODO:DOCS
//TODO:remap the JSON
//TODO:Handle the Labels
//TODO:Save the activity

public class NewWorkoutController {
    public ComboBox<Exercise> activitySelector;
    public DatePicker timestampCalendar;
    public Label attribute1;
    public Label attribute2;
    public Label attribute3;
    public Label attribute4;

    public void initialize () throws FileNotFoundException {

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
                    parameters.add(entry.getValue().getAsString());
                }
                allExercises.add(new Exercise(name, description, parameters));
            });
        });

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

                if (iterator < control) {
                    attribute1.setText(newValue.getParameters() != null ? newValue.getParameters().get(0) : "");
                    iterator++;
                }

                if (iterator < control) {
                    attribute2.setText(newValue.getParameters() != null ? newValue.getParameters().get(1) : "");
                    iterator++;
                }

                if (iterator < control) {
                    attribute3.setText(newValue.getParameters() != null ? newValue.getParameters().get(2) : "");
                    iterator++;
                }

                if (iterator < control) {
                    attribute4.setText(newValue.getParameters() != null ? newValue.getParameters().get(3) : "");
                }
            }

        });

    }
}
