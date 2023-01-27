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
                allExercises.add(new Exercise(name, description, exerciseObject.get("parameters").getAsJsonObject().asMap()));
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

        activitySelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // update the labels with the information from the selected exercise
                attribute1.setText(newValue.getName());
                attribute2.setText("Description: " + newValue.getDescription());
                //attribute3.setText("Parameters: " + newValue.getParameters());
                //attribute4.setText("Other information: " + newValue.getOtherInformation());
            }
        });

    }




}
