package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Amenity;
import HotelReservationSystem.model.Room;
import HotelReservationSystem.model.RoomType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ManageRoomsController {

    @FXML
    private TextField roomIdField;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField floorField;

    @FXML
    private VBox amenitiesBox;

    @FXML
    private CheckBox availableCheckBox;

    @FXML
    private TableView<Room> roomsTable;

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
    private TableColumn<Room, String> availabilityColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadRoomTypeOptions();
        loadAmenityOptions();
        loadRooms();

        roomsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRoom, selectedRoom) -> fillFieldsFromSelectedRoom(selectedRoom)
        );
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

        availabilityColumn.setCellValueFactory(data -> {
            if (data.getValue().getAvailability()) {
                return new SimpleStringProperty("Available");
            } else {
                return new SimpleStringProperty("Not Available");
            }
        });
    }

    private void loadRoomTypeOptions() {
        typeCombo.getItems().clear();

        for (RoomType type : AppData.roomTypes) {
            typeCombo.getItems().add(type.getRoomTypeName());
        }

        typeCombo.setOnAction(event -> {
            RoomType selectedType = findRoomTypeByName(typeCombo.getValue());

            if (selectedType != null) {
                capacityField.setText(String.valueOf(selectedType.getMaxCapacity()));

                if (priceField.getText().trim().isEmpty()) {
                    priceField.setText(String.valueOf(selectedType.getPrice()));
                }
            }
        });
    }

    private void loadAmenityOptions() {
        amenitiesBox.getChildren().clear();

        for (Amenity amenity : AppData.amenities) {
            CheckBox checkBox = new CheckBox(amenity.getAmenity());
            checkBox.setUserData(amenity);
            amenitiesBox.getChildren().add(checkBox);
        }
    }

    private void loadRooms() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(AppData.rooms);
        roomsTable.setItems(rooms);
        showSuccess("Showing " + rooms.size() + " room(s).");
    }

    @FXML
    private void addRoom() {
        try {
            int roomId = Integer.parseInt(roomIdField.getText().trim());

            String typeName = typeCombo.getValue();

            if (typeName == null) {
                showError("Please choose a room type.");
                return;
            }

            RoomType roomType = findRoomTypeByName(typeName);

            if (roomType == null) {
                showError("Selected room type does not exist.");
                return;
            }

            double price = Double.parseDouble(priceField.getText().trim());
            int floor = Integer.parseInt(floorField.getText().trim());
            boolean available = availableCheckBox.isSelected();

            if (roomId <= 0 || price < 0 || floor < 0) {
                showError("Room ID, price, and floor must be valid positive values.");
                return;
            }

            if (roomIdExists(roomId, null)) {
                showError("Room ID already exists.");
                return;
            }

            List<Amenity> amenities = getSelectedAmenities();

            Room room = new Room(roomId, roomType, price, available, floor, amenities);

            AppData.rooms.add(room);
            AppData.database.addRoom(room);

            loadRooms();
            clearFields();
            showSuccess("Room added successfully.");

        } catch (NumberFormatException e) {
            showError("Room ID, price, and floor must be numbers.");
        }
    }

    @FXML
    private void updateRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();

        if (selectedRoom == null) {
            showError("Please select a room to update.");
            return;
        }

        try {
            int newRoomId = Integer.parseInt(roomIdField.getText().trim());

            String typeName = typeCombo.getValue();

            if (typeName == null) {
                showError("Please choose a room type.");
                return;
            }

            RoomType updatedType = findRoomTypeByName(typeName);

            if (updatedType == null) {
                showError("Selected room type does not exist.");
                return;
            }

            double price = Double.parseDouble(priceField.getText().trim());
            int floor = Integer.parseInt(floorField.getText().trim());
            boolean available = availableCheckBox.isSelected();

            if (newRoomId <= 0 || price < 0 || floor < 0) {
                showError("Room ID, price, and floor must be valid positive values.");
                return;
            }

            if (roomIdExists(newRoomId, selectedRoom)) {
                showError("Another room already has this Room ID.");
                return;
            }

            selectedRoom.setRoomId(newRoomId);
            selectedRoom.setRoomType(updatedType);
            selectedRoom.setRoomPrice(price);
            selectedRoom.setRoomFloor(floor);
            selectedRoom.setRoomAvailability(available);
            selectedRoom.setRoomAmenities(getSelectedAmenities());

            roomsTable.refresh();
            clearFields();
            showSuccess("Room updated successfully.");

        } catch (NumberFormatException e) {
            showError("Room ID, price, and floor must be numbers.");
        }
    }

    @FXML
    private void deleteRoom() {
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();

        if (selectedRoom == null) {
            showError("Please select a room to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Room");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete room " + selectedRoom.getRoomId() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            AppData.rooms.remove(selectedRoom);
            AppData.database.removeRoom(selectedRoom);

            loadRooms();
            clearFields();
            showSuccess("Room deleted successfully.");
        }
    }

    private void fillFieldsFromSelectedRoom(Room room) {
        if (room == null) {
            return;
        }

        roomIdField.setText(String.valueOf(room.getRoomId()));
        typeCombo.setValue(room.getRoomType().getRoomTypeName());
        capacityField.setText(String.valueOf(room.getRoomType().getMaxCapacity()));
        priceField.setText(String.valueOf(room.getRoomPrice()));
        floorField.setText(String.valueOf(room.getRoomFloor()));
        availableCheckBox.setSelected(room.getAvailability());

        clearAmenitySelections();

        for (Amenity roomAmenity : room.getRoomAmenities()) {
            for (Node node : amenitiesBox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    Amenity amenity = (Amenity) checkBox.getUserData();

                    if (amenity.getAmenity().equalsIgnoreCase(roomAmenity.getAmenity())) {
                        checkBox.setSelected(true);
                    }
                }
            }
        }
    }

    private boolean roomIdExists(int roomId, Room selectedRoom) {
        for (Room room : AppData.rooms) {
            if (room.getRoomId() == roomId && room != selectedRoom) {
                return true;
            }
        }

        return false;
    }

    private RoomType findRoomTypeByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        for (RoomType type : AppData.roomTypes) {
            if (type.getRoomTypeName().equalsIgnoreCase(typeName)) {
                return type;
            }
        }

        return null;
    }

    private Amenity findAmenityByName(String amenityName) {
        if (amenityName == null) {
            return null;
        }

        for (Amenity amenity : AppData.amenities) {
            if (amenity.getAmenity().equalsIgnoreCase(amenityName)) {
                return amenity;
            }
        }

        return null;
    }

    private void clearAmenitySelections() {
        for (Node node : amenitiesBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }

    private List<Amenity> getSelectedAmenities() {
        List<Amenity> selectedAmenities = new ArrayList<>();

        for (Node node : amenitiesBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;

                if (checkBox.isSelected()) {
                    Amenity amenity = (Amenity) checkBox.getUserData();
                    selectedAmenities.add(amenity);
                }
            }
        }

        return selectedAmenities;
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

    @FXML
    private void clearFields() {
        roomIdField.clear();
        typeCombo.setValue(null);
        capacityField.clear();
        priceField.clear();
        floorField.clear();
        clearAmenitySelections();
        availableCheckBox.setSelected(true);
        roomsTable.getSelectionModel().clearSelection();
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