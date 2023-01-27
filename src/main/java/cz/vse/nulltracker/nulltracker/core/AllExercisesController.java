package cz.vse.nulltracker.nulltracker.core;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.google.gson.Gson;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public class AllExercisesController {

    public VBox mainListOfExercises;
    public HBox category;
    public Text categoryName;
    public ListView exercisesOfCategory;
    public VBox descriptionRegion;
    public Text descriptionTitle;
    public Text descriptionText;


    public void initialize() throws IOException {
        String jsonString = "{name: John, age: 30, car: {factory: Ford, model: Mustang}}";

        String content = Files.readString(Path.of("src/main/resources/cz/vse/nulltracker/nulltracker/core/exercises2.json"));
        JSONObject obj = new JSONObject(content);
        System.out.println(obj);

    }

}
