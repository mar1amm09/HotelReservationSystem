package HotelReservationSystem.gui.controllers;

import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    private void goToRegister() {
        SceneController.switchToRegister();
    }

    @FXML
    private void goToLogin() {
        SceneController.switchToLogin();
    }
}