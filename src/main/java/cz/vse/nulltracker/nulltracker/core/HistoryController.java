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

        allUserLogs.forEach(log -> {
            HBox workout = new HBox();
            String date = log.get("KCAL").toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
            System.out.println(date);


//            workout.setSpacing(10);
//            workout.getChildren().add(new Text(log.get("timestamp").toString()));
//            workout.getChildren().add(new Text(log.get("workoutName").toString()));
//            workout.getChildren().add(new Text(log.get("workoutDuration").toString()));
//            workout.getChildren().add(new Text(log.get("workoutCalories").toString()));
//            listOfWorkouts.getChildren().add(workout);

//            workout.getChildren().add(date);
            listOfWorkouts.getChildren().add(workout);


        });
    }

     public void refreshHistory() {
        updateCollection();
        printCollection();
    }

}
