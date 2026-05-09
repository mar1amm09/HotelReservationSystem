package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Reservation;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.temporal.ChronoUnit;

public class MyReservationsController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> roomIdColumn;

    @FXML
    private TableColumn<Reservation, String> roomTypeColumn;

    @FXML
    private TableColumn<Reservation, String> checkInColumn;

    @FXML
    private TableColumn<Reservation, String> checkOutColumn;

    @FXML
    private TableColumn<Reservation, Integer> nightsColumn;

    @FXML
    private TableColumn<Reservation, Double> totalColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadReservations();
    }

    private void setupTable() {
        roomIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getRoom().getRoomId()).asObject());

        roomTypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoom().getRoomType().getRoomTypeName()));

        checkInColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckIn())));

        checkOutColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckOut())));

        nightsColumn.setCellValueFactory(data -> {
            long nights = ChronoUnit.DAYS.between(
                    data.getValue().getCheckIn(),
                    data.getValue().getCheckOut()
            );

            return new SimpleIntegerProperty((int) nights).asObject();
        });

        totalColumn.setCellValueFactory(data -> {
            long nights = ChronoUnit.DAYS.between(
                    data.getValue().getCheckIn(),
                    data.getValue().getCheckOut()
            );

            double total = AppData.calculateReservationTotal(data.getValue());

            return new SimpleDoubleProperty(total).asObject();
        });

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStatus())));
    }

    @FXML
    private void loadReservations() {
        ObservableList<Reservation> guestReservations = FXCollections.observableArrayList();

        if (!(AppData.currentUser instanceof Guest)) {
            showError("No guest is logged in.");
            return;
        }

        Guest currentGuest = (Guest) AppData.currentUser;

        for (Reservation reservation : AppData.reservations) {
            if (reservation.getGuest().equals(currentGuest)) {
                guestReservations.add(reservation);
            }
        }

        reservationTable.setItems(guestReservations);

        if (guestReservations.isEmpty()) {
            showError("You have no reservations yet.");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Showing " + guestReservations.size() + " reservation(s).");
        }
    }

    @FXML
    private void cancelSelectedReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showError("Please select a reservation first.");
            return;
        }

        if (selectedReservation.getStatus() == Reservation.Status.CANCELLED) {
            showError("This reservation is already cancelled.");
            return;
        }

        if (selectedReservation.getStatus() == Reservation.Status.COMPLETED) {
            showError("Completed reservations cannot be cancelled.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Reservation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to cancel this reservation?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            selectedReservation.setStatus(Reservation.Status.CANCELLED);
            selectedReservation.getRoom().setRoomAvailability(true);

            reservationTable.refresh();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Reservation cancelled successfully.");
        }
    }

    @FXML
    private void goBack() {
        SceneController.switchToGuestDashboard();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }
}
