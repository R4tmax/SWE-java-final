package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

/**
 * @author Martin Kadlec, Michal Průcha
 * @version Last refactor on 25.1
 *
 * <p>Controller for the registration_view FXML file.
 * Handles user creation in tandem with the DB.
 * Take note that all data calls are made against the
 * MongoDB remote system.</p>
 */
public class DashboardController {
    private ArrayList<Exercise> allExercises = new ArrayList<>();
    private final Stage stage = Main.getStage();
    public void toNewWorkoutPage(ActionEvent actionEvent) {
        Main main = (Main) stage.getUserData();
        main.navigateTo("newWorkout");
    }

    private static final MongoCollection<Document> userLogsCollection = database.getCollection("logs");
    //private static final MongoCollection<Document> usersCollection = database.getCollection("users");

    public void initialize () throws FileNotFoundException {
        //read and parse the contents of the JSON
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
    }

    public void calculateUserMinutes () {
        // Define the BSON query to find the documents belonging to the logged in user
        // and whose "timestamp" is within the last 7 days
        Bson filter = and(eq("belongsTo", LoggedUser.LUID),
                gte("date", new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)));

        // Use the "find" method of the userLogsCollection to retrieve the filtered documents
        List<Document> filteredUserLogs = userLogsCollection.find(filter).sort(descending("timestamp")).into(new ArrayList<>());

        // Initialize a variable to keep the sum of all "time" values
        double totalTime = 0;

        // Loop through each document, extract its "activities" field,
        // and sum the "time" values of each activity
        for (Document userLog : filteredUserLogs) {
            Document activities = (Document) userLog.get("activities");
            for (Map.Entry<String, Object> activity : activities.entrySet()) {
                Document activityDetails = (Document) activity.getValue();
                totalTime += activityDetails.getDouble("time");
            }
        }

        // Print the sum of all "time" values
        System.out.println("Total time: " + totalTime);
    }

    public void getKcalsForUsers () {

        List<Bson> pipeline = Arrays.asList(
                lookup("users", "belongsTo", "_id", "user"),
                unwind("$user"),
                group("$user.name", sum("KCAL", "$KCAL")),
                sort(descending("Total KCAL")),
                project(new Document("_id", 0).append("User Name", "$_id").append("Total KCAL", "$KCAL"))
        );

        AggregateIterable<Document> result = userLogsCollection.aggregate(pipeline);

        for (Document document : result) {
            String userName = (String) document.get("User Name");
            Double totalKCAL = (Double) document.get("Total KCAL");
            System.out.println("User: " + userName + " Total KCAL: " + totalKCAL);
        }

    }

    public void recommendNextExercise () {
        Random random = new Random();
        int selector = random.nextInt(allExercises.size());
        String activityName = allExercises.get(selector).getName();
        String activityDesc = allExercises.get(selector).getDescription();

    }

}
