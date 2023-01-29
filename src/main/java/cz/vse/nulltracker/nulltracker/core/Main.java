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
    private Scene newWorkout_scene;
    private Scene history_scene;
    private HistoryController historyController;
    private DashboardController dashboardController;
    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        login_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login_view.fxml"))));
        registration_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("registration_view.fxml"))));
        allExercises_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("allExercises_view.fxml"))));
        newWorkout_scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("newWorkout_view.fxml"))));

        FXMLLoader dashboardLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("dashboard_view.fxml")));
        dashboard_scene = new Scene(dashboardLoader.load());
        dashboardController = dashboardLoader.getController();


        FXMLLoader historyLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("history_view.fxml")));
        history_scene = new Scene(historyLoader.load());
        historyController = historyLoader.getController();

        stage.setUserData(this);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setScene(login_scene);
        stage.setTitle("NullTracker");
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public void navigateTo(String desiredLocation) {

        switch (desiredLocation) {
            case "allExercises" -> stage.setScene(allExercises_scene);
            case "dashboard" -> {
                stage.setScene(dashboard_scene);
                dashboardController.refreshDashboard();
            }
            case "login" -> stage.setScene(login_scene);
            case "registration" -> stage.setScene(registration_scene);
            case "newWorkout" -> stage.setScene(newWorkout_scene);
            case "history" -> {
                stage.setScene(history_scene);
                historyController.refreshHistory();
            }
//            case "community" -> stage.setScene(community_scene);

            default -> System.out.println("No such link");
        }
    }
}
