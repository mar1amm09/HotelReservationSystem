package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Amenity;
import HotelReservationSystem.model.Room;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdminRoomsController {

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
        loadRooms();
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

    @FXML
    private void loadRooms() {
        ObservableList<Room> rooms = FXCollections.observableArrayList();

        rooms.addAll(AppData.rooms);

        roomsTable.setItems(rooms);

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Showing " + rooms.size() + " room(s).");
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
    private void goBack() {
        SceneController.switchToAdminDashboard();
    }
}
