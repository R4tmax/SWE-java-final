module cz.vse.nulltracker.nulltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    opens cz.vse.nulltracker.nulltracker.core to javafx.fxml;
    opens cz.vse.nulltracker.nulltracker.ui to javafx.fxml;
    exports cz.vse.nulltracker.nulltracker.core;
    exports cz.vse.nulltracker.nulltracker.ui;

}