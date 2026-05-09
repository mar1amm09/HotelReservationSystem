package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Receptionist;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class ReceptionistDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        if (AppData.currentUser instanceof Receptionist) {
            Receptionist receptionist = (Receptionist) AppData.currentUser;
            welcomeLabel.setText("Welcome, " + receptionist.getUsername());
        } else {
            welcomeLabel.setText("No receptionist is currently logged in.");
        }
    }

    @FXML
    private void viewReservations() {
        SceneController.switchToReceptionistReservations();
    }

    @FXML
    private void manageCheckInOut() {
        SceneController.switchToReceptionistCheckInOut();
    }

    @FXML
    private void viewRooms() {
        SceneController.switchToReceptionistRooms();
    }

    @FXML
    private void goToChat() {
        SceneController.switchToChat();
    }

    @FXML
    private void handleLogout() {
        AppData.currentUser = null;
        SceneController.switchToWelcome();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}