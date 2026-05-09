package HotelReservationSystem.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchToWelcome() {
        loadScene("/HotelReservationSystem/gui/views/Welcome.fxml",
                "Hotel Reservation System");
    }
    public static void switchToRegister() {
        loadScene("/HotelReservationSystem/gui/views/Register.fxml",
                "Hotel Reservation System - Register");
    }
    public static void switchToLogin() {
        loadScene("/HotelReservationSystem/gui/views/Login.fxml",
                "Hotel Reservation System - Login");
    }
    public static void switchToGuestDashboard() {
        loadScene("/HotelReservationSystem/gui/views/GuestDashboard.fxml",
                "Hotel Reservation System - Guest Dashboard");
    }
    public static void switchToRoomBrowsing() {
        loadScene("/HotelReservationSystem/gui/views/RoomBrowsing.fxml",
                "Hotel Reservation System - Browse Rooms");
    }
    public static void switchToReservation() {
        loadScene("/HotelReservationSystem/gui/views/Reservation.fxml",
                "Hotel Reservation System - Make Reservation");
    }
    public static void switchToMyReservations() {
        loadScene("/HotelReservationSystem/gui/views/MyReservations.fxml",
                "Hotel Reservation System - My Reservations");
    }
    public static void switchToCheckout() {
        loadScene("/HotelReservationSystem/gui/views/Checkout.fxml",
                "Hotel Reservation System - Checkout");
    }
    public static void switchToAdminDashboard() {
        loadScene("/HotelReservationSystem/gui/views/AdminDashboard.fxml",
                "Hotel Reservation System - Admin Dashboard");
    }
    public static void switchToAdminRooms() {
        loadScene("/HotelReservationSystem/gui/views/AdminRooms.fxml",
                "Hotel Reservation System - All Rooms");
    }
    public static void switchToManageRooms() {
        loadScene("/HotelReservationSystem/gui/views/ManageRooms.fxml",
                "Hotel Reservation System - Manage Rooms");
    }
    public static void switchToAdminGuests() {
        loadScene("/HotelReservationSystem/gui/views/AdminGuests.fxml",
                "Hotel Reservation System - All Guests");
    }
    public static void switchToAdminReservations() {
        loadScene("/HotelReservationSystem/gui/views/AdminReservations.fxml",
                "Hotel Reservation System - All Reservations");
    }
    public static void switchToManageRoomTypes() {
        loadScene("/HotelReservationSystem/gui/views/ManageRoomTypes.fxml",
                "Hotel Reservation System - Manage Room Types");
    }
    public static void switchToManageAmenities() {
        loadScene("/HotelReservationSystem/gui/views/ManageAmenities.fxml",
                "Hotel Reservation System - Manage Amenities");
    }
    public static void switchToReceptionistDashboard() {
        loadScene("/HotelReservationSystem/gui/views/ReceptionistDashboard.fxml",
                "Hotel Reservation System - Receptionist Dashboard");
    }
    public static void switchToReceptionistCheckInOut() {
        loadScene("/HotelReservationSystem/gui/views/ReceptionistCheckInOut.fxml",
                "Hotel Reservation System - Check-in / Check-out");
    }
    public static void switchToReceptionistRooms() {
        loadScene("/HotelReservationSystem/gui/views/ReceptionistRooms.fxml",
                "Hotel Reservation System - Receptionist Rooms");
    }
    public static void switchToReceptionistReservations() {
        loadScene("/HotelReservationSystem/gui/views/ReceptionistReservations.fxml",
                "Hotel Reservation System - Receptionist Reservations");
    }
    public static void switchToChat() {
        loadScene("/HotelReservationSystem/gui/views/Chat.fxml",
                "Hotel Reservation System - Live Chat");
    }
    private static void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), 900, 600);

            scene.getStylesheets().add(
                    SceneController.class
                            .getResource("/HotelReservationSystem/gui/styles/style.css")
                            .toExternalForm()
            );

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
