package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Person;
import HotelReservationSystem.model.Receptionist;
import HotelReservationSystem.network.ChatClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChatController {

    @FXML
    private Label userLabel;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private Label messageLabel;

    private ChatClient chatClient;
    private String username;
    private String role;

    @FXML
    public void initialize() {
        Person currentUser = AppData.currentUser;

        if (currentUser == null) {
            showError("No user is logged in.");
            return;
        }

        username = currentUser.getUsername();

        if (currentUser instanceof Guest) {
            role = "Guest";
        } else if (currentUser instanceof Receptionist) {
            role = "Receptionist";
        } else {
            role = "User";
        }

        userLabel.setText("Logged in as: " + role + " - " + username);

        try {
            chatClient = new ChatClient("localhost", 5000, message -> {
                Platform.runLater(() -> {
                    chatArea.appendText(message + "\n");
                });
            });

            chatArea.appendText("Connected to chat server.\n");

        } catch (IOException e) {
            showError("Could not connect to chat server. Please run ChatServer first.");
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            showError("Please type a message first.");
            return;
        }

        if (chatClient == null) {
            showError("Chat server is not connected.");
            return;
        }

        String fullMessage = role + " " + username + ": " + message;

        chatClient.sendMessage(fullMessage);

        chatArea.appendText("Me: " + message + "\n");
        messageField.clear();

        messageLabel.setText("");
    }

    @FXML
    private void goBack() {
        if (chatClient != null) {
            chatClient.close();
        }

        if (AppData.currentUser instanceof Guest) {
            SceneController.switchToGuestDashboard();
        } else if (AppData.currentUser instanceof Receptionist) {
            SceneController.switchToReceptionistDashboard();
        } else {
            SceneController.switchToWelcome();
        }
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }
}