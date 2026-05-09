package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminGuestsController {

    @FXML
    private TableView<Guest> guestsTable;

    @FXML
    private TableColumn<Guest, String> usernameColumn;

    @FXML
    private TableColumn<Guest, String> dobColumn;

    @FXML
    private TableColumn<Guest, String> genderColumn;

    @FXML
    private TableColumn<Guest, String> addressColumn;

    @FXML
    private TableColumn<Guest, Double> balanceColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadGuests();
    }

    private void setupTable() {
        usernameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsername()));

        dobColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getDate())));

        genderColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getGender())));

        addressColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAddress()));

        balanceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().balance()).asObject());
    }

    @FXML
    private void loadGuests() {
        ObservableList<Guest> guests = FXCollections.observableArrayList(AppData.guests);

        guestsTable.setItems(guests);

        if (guests.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("No registered guests found.");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Showing " + guests.size() + " guest(s).");
        }
    }

    @FXML
    private void goBack() {
        SceneController.switchToAdminDashboard();
    }
}
