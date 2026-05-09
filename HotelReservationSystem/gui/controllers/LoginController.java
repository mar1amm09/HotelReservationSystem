package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Admin;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Person;
import HotelReservationSystem.model.Receptionist;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        Person user = AppData.login(username, password);

        if (user == null) {
            showError("Invalid username or password.");
            return;
        }

        if (user instanceof Guest) {
            AppData.currentUser = user;
            SceneController.switchToGuestDashboard();

        } else if (user instanceof Admin) {
            AppData.currentUser = user;
            SceneController.switchToAdminDashboard();

        } else if (user instanceof Receptionist) {
            AppData.currentUser = user;
            SceneController.switchToReceptionistDashboard();
        }
    }

    @FXML
    private void goBack() {
        SceneController.switchToWelcome();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
