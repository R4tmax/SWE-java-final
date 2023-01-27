package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.*;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;


public class AllExercisesController {

    public VBox mainListOfExercises;

    public VBox descriptionRegion;
    public Text descriptionTitle;
    public Text descriptionText;


    public void initialize() throws FileNotFoundException {


        File input = new File("src/main/resources/cz/vse/nulltracker/nulltracker/core/exercises.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonArray categories = fileObject.get("workouts").getAsJsonArray();

        categories.forEach(category -> {
            JsonObject categoryObject = category.getAsJsonObject();
            ArrayList<Exercise> exercises = new ArrayList<>();
            categoryObject.get("exercises").getAsJsonArray().forEach(exercise -> {
                JsonObject exerciseObject = exercise.getAsJsonObject();
                String name = exerciseObject.get("name").getAsString();
                String description = exerciseObject.get("description").getAsString();
                exercises.add(new Exercise(name, description, exerciseObject.get("parameters").getAsJsonObject().asMap()));
            });

            HBox categoryBox = new HBox();
            categoryBox.setPrefSize(100, 200);

            String categoryName = categoryObject.get("category").getAsString();
            Text categoryTitle = new Text(categoryName);
            categoryTitle.getStyleClass().add("h4");
            categoryTitle.setWrappingWidth(160);

            ListView exercisesOfCategory = new ListView();
            exercisesOfCategory.getItems().addAll(exercises);
            exercisesOfCategory.setMinWidth(160);
            exercisesOfCategory.setCellFactory(param -> new ListCell<Exercise>(){
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

            categoryBox.getChildren().add(categoryTitle);
            categoryBox.getChildren().add(exercisesOfCategory);
            mainListOfExercises.getChildren().add(categoryBox);

            exercisesOfCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                descriptionTitle.setText(((Exercise) newValue).getName());
                descriptionText.setText(((Exercise) newValue).getDescription());
            });

        });
    }

}
