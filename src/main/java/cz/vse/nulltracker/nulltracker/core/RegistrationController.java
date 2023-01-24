package cz.vse.nulltracker.nulltracker.core;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {
    public Button submitButton;
    public TextField nameInput;
    public TextField emailInput;
    public PasswordField passwordFirstInput;
    public PasswordField passwordSecondInput;
    private final Stage stage = Main.getStage();
    @FXML
    private void linkToLogin() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("login");
    }
}
