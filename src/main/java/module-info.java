module cz.vse.nulltracker.nulltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires com.google.gson;
    requires org.json;

    opens cz.vse.nulltracker.nulltracker.core to javafx.fxml;
    opens cz.vse.nulltracker.nulltracker.database to com.google.gson;
    exports cz.vse.nulltracker.nulltracker.core;
    exports cz.vse.nulltracker.nulltracker.database;
}