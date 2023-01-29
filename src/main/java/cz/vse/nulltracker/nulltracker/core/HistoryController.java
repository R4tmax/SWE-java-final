package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import cz.vse.nulltracker.nulltracker.database.LoggedUser;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

public class HistoryController {


    private final MongoCollection<Document> logsCollection = database.getCollection("logs");
    private List<Document> allUserLogs;


    public void initialize () {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> updateCollection()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        updateCollection();
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

        for (Document log: allUserLogs) {
            for (Map.Entry<String, Object> entry : log.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
