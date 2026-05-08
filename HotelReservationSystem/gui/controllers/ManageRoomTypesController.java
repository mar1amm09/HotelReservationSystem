package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Room;
import HotelReservationSystem.model.RoomType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManageRoomTypesController {

    @FXML
    private TextField typeNameField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField priceField;

    @FXML
    private TableView<RoomType> roomTypesTable;

    @FXML
    private TableColumn<RoomType, String> nameColumn;

    @FXML
    private TableColumn<RoomType, Integer> capacityColumn;

    @FXML
    private TableColumn<RoomType, Double> priceColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadRoomTypes();

        roomTypesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldType, selectedType) -> fillFields(selectedType)
        );
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomTypeName()));

        capacityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getMaxCapacity()).asObject());

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
    }

    private void loadRoomTypes() {
        ObservableList<RoomType> types =
                FXCollections.observableArrayList(AppData.roomTypes);

        roomTypesTable.setItems(types);

        showSuccess("Showing " + types.size() + " room type(s).");
    }

    @FXML
    private void addRoomType() {
        try {
            String name = typeNameField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (name.isEmpty()) {
                showError("Room type name cannot be empty.");
                return;
            }

            if (capacity <= 0 || price < 0) {
                showError("Capacity must be positive and price cannot be negative.");
                return;
            }

            if (roomTypeExists(name, null)) {
                showError("This room type already exists.");
                return;
            }

            RoomType roomType = new RoomType(name, capacity, price);
            AppData.roomTypes.add(roomType);

            loadRoomTypes();
            clearFields();
            showSuccess("Room type added successfully.");

        } catch (NumberFormatException e) {
            showError("Capacity and price must be valid numbers.");
        }
    }

    @FXML
    private void updateRoomType() {
        RoomType selectedType = roomTypesTable.getSelectionModel().getSelectedItem();

        if (selectedType == null) {
            showError("Please select a room type to update.");
            return;
        }

        try {
            String oldName = selectedType.getRoomTypeName();

            String newName = typeNameField.getText().trim();
            int newCapacity = Integer.parseInt(capacityField.getText().trim());
            double newPrice = Double.parseDouble(priceField.getText().trim());

            if (newName.isEmpty()) {
                showError("Room type name cannot be empty.");
                return;
            }

            if (newCapacity <= 0 || newPrice < 0) {
                showError("Capacity must be positive and price cannot be negative.");
                return;
            }

            if (roomTypeExists(newName, selectedType)) {
                showError("Another room type already has this name.");
                return;
            }

            RoomType updatedType = new RoomType(newName, newCapacity, newPrice);

            int index = AppData.roomTypes.indexOf(selectedType);
            AppData.roomTypes.set(index, updatedType);

            for (Room room : AppData.rooms) {
                if (room.getRoomType() == selectedType ||
                        room.getRoomType().getRoomTypeName().equalsIgnoreCase(oldName)) {
                    room.setRoomType(updatedType);
                }
            }

            loadRoomTypes();
            clearFields();
            showSuccess("Room type updated successfully.");

        } catch (NumberFormatException e) {
            showError("Capacity and price must be valid numbers.");
        }
    }

    @FXML
    private void deleteRoomType() {
        RoomType selectedType = roomTypesTable.getSelectionModel().getSelectedItem();

        if (selectedType == null) {
            showError("Please select a room type to delete.");
            return;
        }

        if (isRoomTypeUsed(selectedType)) {
            showError("Cannot delete this room type because some rooms are using it.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Room Type");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete " +
                selectedType.getRoomTypeName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            AppData.roomTypes.remove(selectedType);

            loadRoomTypes();
            clearFields();
            showSuccess("Room type deleted successfully.");
        }
    }

    private boolean roomTypeExists(String name, RoomType selectedType) {
        for (RoomType type : AppData.roomTypes) {
            if (type.getRoomTypeName().equalsIgnoreCase(name) && type != selectedType) {
                return true;
            }
        }

        return false;
    }

    private boolean isRoomTypeUsed(RoomType selectedType) {
        for (Room room : AppData.rooms) {
            if (room.getRoomType().getRoomTypeName()
                    .equalsIgnoreCase(selectedType.getRoomTypeName())) {
                return true;
            }
        }

        return false;
    }

    private void fillFields(RoomType type) {
        if (type == null) {
            return;
        }

        typeNameField.setText(type.getRoomTypeName());
        capacityField.setText(String.valueOf(type.getMaxCapacity()));
        priceField.setText(String.valueOf(type.getPrice()));
    }

    @FXML
    private void clearFields() {
        typeNameField.clear();
        capacityField.clear();
        priceField.clear();
        roomTypesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void goBack() {
        SceneController.switchToAdminDashboard();
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