package cz.vse.nulltracker.nulltracker.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DatabaseHandler {


    public static void DBtestInit () {

        ConnectionString connectionString = new ConnectionString("mongodb+srv://martin_dev:wahB7g4jjP2CCJ7@nulltrackerdev.nxwgnwc.mongodb.net/?retryWrites=true&w=majority");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoIterable<String> dbs = mongoClient.listDatabaseNames();
            try (MongoCursor<String> cursor = dbs.cursor()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            }
        }
    }

    public static void DBinsertionTest () {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://martin_dev:wahB7g4jjP2CCJ7@nulltrackerdev.nxwgnwc.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            database = mongoClient.getDatabase("NullTracerkerDevDB");
            MongoCollection<Document> collection = database.getCollection("users");

            Document document = new Document("login", "dbtesting1")
                    .append("password", "11115");

            collection.insertOne(document);
            ObjectId objectId = document.getObjectId("_id");


            System.out.println("Created user:" + objectId);
        }



    }

}
