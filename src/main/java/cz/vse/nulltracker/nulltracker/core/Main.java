package cz.vse.nulltracker.nulltracker.core;

import cz.vse.nulltracker.nulltracker.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private Stage stage;
    private Scene login_scene;
    private Scene registration_scene;
    private Scene dashboard_scene;

    public static void main(String[] args) {
        //DatabaseHandler.DBtestInit();
        //DatabaseHandler.DBinsertionTest();
        //DatabaseHandler.DBdocumentReadTest();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        login_scene = new Scene(FXMLLoader.load(getClass().getResource("login_view.fxml")), 1200, 800);
        registration_scene = new Scene(FXMLLoader.load(getClass().getResource("registration_view.fxml")));
        dashboard_scene = new Scene(FXMLLoader.load(getClass().getResource("dashboard_view.fxml")));

        stage.setScene(login_scene);
        stage.setTitle("Null Tracker");
        stage.show();
    }

    public void navigateToRegister() {
        stage.setScene(registration_scene);
    }

    public void navigateToLogin() {
        stage.setScene(login_scene);
    }

    public void navigateToDashboard() {
        stage.setScene(dashboard_scene);
    }
}
