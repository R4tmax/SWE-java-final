package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        //DatabaseHandler.DBtestInit();
        //DatabaseHandler.DBinsertionTest();
        //DatabaseHandler.DBdocumentReadTest();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("registration_view.fxml"));
        loader.load();
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
        stage.setTitle("Nulltracker");
        stage.show();
    }
}
