package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Reservation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReceptionistReservationsController {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, String> guestColumn;

    @FXML
    private TableColumn<Reservation, Integer> roomIdColumn;

    @FXML
    private TableColumn<Reservation, String> roomTypeColumn;

    @FXML
    private TableColumn<Reservation, String> reservationTypeColumn;

    @FXML
    private TableColumn<Reservation, String> checkInColumn;

    @FXML
    private TableColumn<Reservation, String> checkOutColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private TableColumn<Reservation, String> paymentColumn;

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

        reservationTypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getType())));

        checkInColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckIn())));

        checkOutColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCheckOut())));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStatus())));

        paymentColumn.setCellValueFactory(data -> {
            if (AppData.paidReservations.contains(data.getValue())) {
                return new SimpleStringProperty("Paid");
            } else {
                return new SimpleStringProperty("Unpaid");
            }
        });
    }

    @FXML
    private void loadReservations() {
        ObservableList<Reservation> reservations =
                FXCollections.observableArrayList(AppData.reservations);

        reservationsTable.setItems(reservations);

        if (reservations.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("No reservations found.");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Showing " + reservations.size() + " reservation(s).");
        }
    }

    @FXML
    private void goBack() {
        SceneController.switchToReceptionistDashboard();
    }
}
