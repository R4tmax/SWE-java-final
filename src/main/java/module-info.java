module cz.vse.nulltracker.nulltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;


    opens cz.vse.nulltracker.nulltracker to javafx.fxml;
    exports cz.vse.nulltracker.nulltracker;
}