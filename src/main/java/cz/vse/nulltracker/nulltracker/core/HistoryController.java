package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

public class HistoryController {


    private static final MongoCollection<Document> logsCollection = database.getCollection("logs");
    private static List<Document> allUserLogs;
    public VBox listOfWorkouts;


    public void initialize () {
        refreshHistory();
    }

    public void updateCollection() {

        if (LoggedUser.LUID == null) return;

        try {
            allUserLogs = logsCollection.find(eq("belongsTo", LoggedUser.LUID)).sort(Sorts.descending("timestamp")).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("DB error" + e);
        }

    }

    public void printCollection() {

        if (allUserLogs == null) return;
        listOfWorkouts.getChildren().clear();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
        allUserLogs.forEach(log -> {
            VBox workoutVBox = new VBox();
            workoutVBox.setSpacing(10);

            // DATE
            Date date = (Date) log.get("date");
            String formattedDate = dateFormat.format(date);
            Text dateText = new Text(formattedDate);
            dateText.getStyleClass().add("h4");
            workoutVBox.getChildren().add(dateText);

            // Activities
            Document activities = (Document) log.get("activities");
            VBox activitiesVBox = new VBox();
            activitiesVBox.setSpacing(10);
            activities.forEach((key, value) -> {
                Text exerciseName = new Text(key);
                exerciseName.getStyleClass().add("p");
                activitiesVBox.getChildren().add(exerciseName);
            });

            workoutVBox.getChildren().add(activitiesVBox);
            listOfWorkouts.getChildren().add(workoutVBox);
        });
    }

     public void refreshHistory() {
        updateCollection();
        printCollection();
    }

}
