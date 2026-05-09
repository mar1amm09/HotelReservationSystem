package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

public class GuestDashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label dobLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label messageLabel;

    private Guest currentGuest;

    @FXML
    private void goToChat() {
        SceneController.switchToChat();
    }

    @FXML
    public void initialize() {
        if (AppData.currentUser instanceof Guest) {
            currentGuest = (Guest) AppData.currentUser;
            loadGuestData();
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("No guest is currently logged in.");
        }
    }

    private void loadGuestData() {
        usernameLabel.setText(currentGuest.getUsername());
        dobLabel.setText(String.valueOf(currentGuest.getDate()));
        genderLabel.setText(String.valueOf(currentGuest.getGender()));
        addressLabel.setText(currentGuest.getAddress());
        balanceLabel.setText(currentGuest.balance() + " EGP");
    }

    @FXML
    private void goToBrowseRooms() {
        SceneController.switchToRoomBrowsing();
    }

    @FXML
    private void goToReservations() {
        SceneController.switchToMyReservations();
    }

    @FXML
    private void goToCheckout() {
        SceneController.switchToCheckout();
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
    @FXML
    private void updateBalance() {
        if (currentGuest == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("No guest is currently logged in.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Balance");
        dialog.setHeaderText("Add money to your balance");
        dialog.setContentText("Enter amount:");

        dialog.showAndWait().ifPresent(amountText -> {
            try {
                double amount = Double.parseDouble(amountText.trim());

                if (amount <= 0) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Amount must be greater than zero.");
                    return;
                }

                double newBalance = currentGuest.balance() + amount;
                currentGuest.setBalance(newBalance);

                balanceLabel.setText(newBalance + " EGP");

                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Balance updated successfully.");

            } catch (NumberFormatException e) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Amount must be a valid number.");
            }
        });
    }
}