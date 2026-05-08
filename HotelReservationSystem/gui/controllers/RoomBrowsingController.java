package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Amenity;
import HotelReservationSystem.model.Room;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RoomBrowsingController {

    @FXML
    private ComboBox<String> typeFilterCombo;

    @FXML
    private TextField maxPriceField;

    @FXML
    private TextField amenityField;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> roomIdColumn;

    @FXML
    private TableColumn<Room, String> typeColumn;

    @FXML
    private TableColumn<Room, Integer> floorColumn;

    @FXML
    private TableColumn<Room, Double> priceColumn;

    @FXML
    private TableColumn<Room, String> amenitiesColumn;

    @FXML
    private TableColumn<Room, String> statusColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        loadAvailableRooms();
    }

    private void setupTable() {
        roomIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getRoomId()).asObject());

        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomType().getRoomTypeName()));

        floorColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getRoomFloor()).asObject());

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getRoomPrice()).asObject());

        amenitiesColumn.setCellValueFactory(data ->
                new SimpleStringProperty(getAmenitiesText(data.getValue())));

        statusColumn.setCellValueFactory(data -> {
            if (data.getValue().getAvailability()) {
                return new SimpleStringProperty("Available");
            } else {
                return new SimpleStringProperty("Not Available");
            }
        });
    }

    private void setupFilters() {
        typeFilterCombo.getItems().add("All Types");

        for (Room room : AppData.rooms) {
            String typeName = room.getRoomType().getRoomTypeName();

            if (!typeFilterCombo.getItems().contains(typeName)) {
                typeFilterCombo.getItems().add(typeName);
            }
        }

        typeFilterCombo.setValue("All Types");
    }

    private void loadAvailableRooms() {
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();

        for (Room room : AppData.rooms) {
            if (room.getAvailability()) {
                availableRooms.add(room);
            }
        }

        roomTable.setItems(availableRooms);
        messageLabel.setText("Showing available rooms only.");
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void applyFilter() {
        String selectedType = typeFilterCombo.getValue();
        String maxPriceText = maxPriceField.getText().trim();
        String amenityText = amenityField.getText().trim().toLowerCase();

        double maxPrice = Double.MAX_VALUE;

        if (!maxPriceText.isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceText);

                if (maxPrice < 0) {
                    showError("Max price cannot be negative.");
                    return;
                }

            } catch (NumberFormatException e) {
                showError("Max price must be a valid number.");
                return;
            }
        }

        ObservableList<Room> filteredRooms = FXCollections.observableArrayList();

        for (Room room : AppData.rooms) {

            if (!room.getAvailability()) {
                continue;
            }

            if (selectedType != null
                    && !selectedType.equals("All Types")
                    && !room.getRoomType().getRoomTypeName().equals(selectedType)) {
                continue;
            }

            if (room.getRoomPrice() > maxPrice) {
                continue;
            }

            if (!amenityText.isEmpty()
                    && !getAmenitiesText(room).toLowerCase().contains(amenityText)) {
                continue;
            }

            filteredRooms.add(room);
        }

        roomTable.setItems(filteredRooms);

        if (filteredRooms.isEmpty()) {
            showError("No rooms match your filter.");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Found " + filteredRooms.size() + " matching room(s).");
        }
    }

    @FXML
    private void resetFilter() {
        typeFilterCombo.setValue("All Types");
        maxPriceField.clear();
        amenityField.clear();
        loadAvailableRooms();
    }

    @FXML
    private void goToMakeReservation() {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();

        if (selectedRoom == null) {
            showError("Please select a room first.");
            return;
        }

        AppData.selectedRoom = selectedRoom;
        SceneController.switchToReservation();
    }

    @FXML
    private void goBack() {
        SceneController.switchToGuestDashboard();
    }

    private String getAmenitiesText(Room room) {
        String text = "";

        for (Amenity amenity : room.getRoomAmenities()) {
            text += amenity.getAmenity() + ", ";
        }

        if (text.endsWith(", ")) {
            text = text.substring(0, text.length() - 2);
        }

        return text;
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }
}