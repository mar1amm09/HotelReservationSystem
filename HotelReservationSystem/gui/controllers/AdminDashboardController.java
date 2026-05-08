package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Admin;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        if (AppData.currentUser instanceof Admin) {
            Admin admin = (Admin) AppData.currentUser;
            welcomeLabel.setText("Welcome, " + admin.getUsername());
        } else {
            welcomeLabel.setText("No admin is currently logged in.");
        }
    }

    @FXML
    private void viewGuests() {
        SceneController.switchToAdminGuests();
    }

    @FXML
    private void viewRooms() {
        SceneController.switchToAdminRooms();
    }

    @FXML
    private void viewReservations() {
        SceneController.switchToAdminReservations();
    }

    @FXML
    private void manageRooms() {
        SceneController.switchToManageRooms();
    }

    @FXML
    private void manageRoomTypes() {
        SceneController.switchToManageRoomTypes();
    }

    @FXML
    private void manageAmenities() {
        SceneController.switchToManageAmenities();
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