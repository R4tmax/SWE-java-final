package cz.vse.nulltracker.nulltracker.core;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public Button submitButton;
    public TextField emailInput;
    public PasswordField passwordInput;
    private final Stage stage = Main.getStage();
    @FXML
    private void linkToRegistration() {
        Main main = (Main) stage.getUserData();
        main.navigateTo("registration");
    }
}
