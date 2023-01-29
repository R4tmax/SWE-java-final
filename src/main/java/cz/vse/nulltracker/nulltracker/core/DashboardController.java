package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.*;
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
    private final Stage stage = Main.getStage();
    public void toNewWorkoutPage(ActionEvent actionEvent) {
        Main main = (Main) stage.getUserData();
        main.navigateTo("newWorkout");
    }

    private static final MongoCollection<Document> userLogsCollection = database.getCollection("logs");
    private static final MongoCollection<Document> usersCollection = database.getCollection("users");

    public void calculateUserMinutes () {
        // Define the BSON query to find the documents belonging to the logged in user
        // and whose "timestamp" is within the last 7 days
        Bson filter = and(eq("belongsTo", LoggedUser.LUID),
                gte("date", new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)));

        // Use the "find" method of the userLogsCollection to retrieve the filtered documents
        List<Document> filteredUserLogs = userLogsCollection.find(filter).sort(Sorts.descending("timestamp")).into(new ArrayList<>());

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
        // Create the pipeline
        List<Bson> pipeline = Arrays.asList(
                Aggregates.lookup("logs", "login", "belongsTo", "logs"),
                Aggregates.unwind("$logs"),
                Aggregates.group("$login", sum("KCAL", "$logs.KCAL"))
        );

// Execute the aggregate operation
        AggregateIterable<Document> result = usersCollection.aggregate(pipeline);

// Loop through the result to get the sum of KCAL for each user
        for (Document doc : result) {
            System.out.println("User: " + doc.get("_id") + " Total KCAL: " + doc.get("KCAL"));
        }

    }

}
