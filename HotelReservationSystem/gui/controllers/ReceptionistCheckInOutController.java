package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Reservation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReceptionistCheckInOutController {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, String> guestColumn;

    @FXML
    private TableColumn<Reservation, Integer> roomIdColumn;

    @FXML
    private TableColumn<Reservation, String> roomTypeColumn;

    @FXML
    private TableColumn<Reservation, String> checkInColumn;

    @FXML
    private TableColumn<Reservation, String> checkOutColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private TableColumn<Reservation, String> availabilityColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadReservations();
    }

    private void setupTable() {
        guestColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGuest().getUsername()));

        roomIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getRoom().getRoomId()).asObject());

        roomTypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoom().getRoomType().getRoomTypeName()));

        checkInColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckIn())));

        checkOutColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckOut())));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStatus())));

        availabilityColumn.setCellValueFactory(data -> {
            if (data.getValue().getRoom().getAvailability()) {
                return new SimpleStringProperty("Available");
            } else {
                return new SimpleStringProperty("Not Available");
            }
        });
    }

    @FXML
    private void loadReservations() {
        ObservableList<Reservation> reservations =
                FXCollections.observableArrayList(AppData.reservations);

        reservationsTable.setItems(reservations);

        if (reservations.isEmpty()) {
            showError("No reservations found.");
        } else {
            showSuccess("Showing " + reservations.size() + " reservation(s).");
        }
    }

    @FXML
    private void checkInSelected() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showError("Please select a reservation first.");
            return;
        }

        if (selectedReservation.getStatus() != Reservation.Status.PENDING) {
            showError("Only pending reservations can be checked in.");
            return;
        }

        selectedReservation.setStatus(Reservation.Status.CONFIRMED);
        selectedReservation.getRoom().setRoomAvailability(false);

        reservationsTable.refresh();
        showSuccess("Guest checked in successfully.");
    }

    @FXML
    private void checkOutSelected() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showError("Please select a reservation first.");
            return;
        }

        if (selectedReservation.getStatus() == Reservation.Status.CANCELLED) {
            showError("Cancelled reservations cannot be checked out.");
            return;
        }

        if (selectedReservation.getStatus() == Reservation.Status.PENDING) {
            showError("Guest must be checked in before check-out.");
            return;
        }

        if (selectedReservation.getStatus() == Reservation.Status.COMPLETED) {
            showError("This reservation is already completed.");
            return;
        }
        if (!AppData.paidReservations.contains(selectedReservation)) {
            showError("Guest must pay the invoice before check-out.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Check-out Guest");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to check out guest "
                + selectedReservation.getGuest().getUsername()
                + " from room "
                + selectedReservation.getRoom().getRoomId()
                + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            selectedReservation.setStatus(Reservation.Status.COMPLETED);
            selectedReservation.getRoom().setRoomAvailability(true);

            reservationsTable.refresh();
            showSuccess("Guest checked out successfully. Room is now available.");
        }
    }

    @FXML
    private void goBack() {
        SceneController.switchToReceptionistDashboard();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }
}
