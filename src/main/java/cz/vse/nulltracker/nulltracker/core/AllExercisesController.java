package cz.vse.nulltracker.nulltracker.core;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class AllExercisesController {

    public VBox mainListOfExercises;
    public HBox category;
    public Text categoryName;
    public ListView exercisesOfCategory;
    public VBox descriptionRegion;
    public Text descriptionTitle;
    public Text descriptionText;


    public void initialize() throws IOException {
        String content = Files.readString(Path.of("src/main/resources/cz/vse/nulltracker/nulltracker/core/exercises2.json"));
        JSONObject obj = new JSONObject(content);
        System.out.println(obj);

    }

}
