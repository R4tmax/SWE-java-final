package cz.vse.nulltracker.nulltracker.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage;
    private Scene login_scene;
    private Scene registration_scene;
    private Scene dashboard_scene;
    private Scene allExercises_scene;

    public static void main(String[] args) {
        //DatabaseHandler.DBtestInit();
        //DatabaseHandler.DBinsertionTest();
        //DatabaseHandler.DBdocumentReadTest();
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        stage = primaryStage;

        login_scene = new Scene(FXMLLoader.load(getClass().getResource("login_view.fxml")), 1200, 800);
        registration_scene = new Scene(FXMLLoader.load(getClass().getResource("registration_view.fxml")));
        dashboard_scene = new Scene(FXMLLoader.load(getClass().getResource("dashboard_view.fxml")));
        allExercises_scene = new Scene(FXMLLoader.load(getClass().getResource("allExercises_view.fxml")));

        stage.setUserData(this);
        stage.setScene(dashboard_scene);
        stage.setTitle("Null Tracker");
        stage.show();
    }

    public static Stage getStage() {
        return stage;
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

    public void navigateToAllExercises() {
        stage.setScene(allExercises_scene);
    }
}
