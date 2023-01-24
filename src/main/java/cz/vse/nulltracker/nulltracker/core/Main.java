package cz.vse.nulltracker.nulltracker.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

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
        stage = primaryStage;

        login_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_view.fxml"))), 1200, 800);
        registration_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("registration_view.fxml"))));
        dashboard_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard_view.fxml"))));
        allExercises_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("allExercises_view.fxml"))));

        stage.setUserData(this);
        stage.setScene(login_scene);
        stage.setTitle("Null Tracker");
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public void navigateTo(String desiredLocation) {
        switch (desiredLocation) {
            case "allExercises" -> stage.setScene(allExercises_scene);
            case "dashboard" -> stage.setScene(dashboard_scene);
            case "login" -> stage.setScene(login_scene);
            case "registration" -> stage.setScene(registration_scene);
//            case "community" -> stage.setScene(community_scene);

            default -> System.out.println("No such link");
        }
    }
}
