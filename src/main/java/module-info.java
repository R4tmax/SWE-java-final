module cz.vse.nulltracker.nulltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires com.google.gson;
    requires org.json;

    opens cz.vse.nulltracker.nulltracker.core to javafx.fxml;
    exports cz.vse.nulltracker.nulltracker.core;
}