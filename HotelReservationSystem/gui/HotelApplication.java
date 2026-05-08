package HotelReservationSystem.gui;

import HotelReservationSystem.gui.controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

public class HotelApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneController.setStage(primaryStage);
        SceneController.switchToWelcome();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
