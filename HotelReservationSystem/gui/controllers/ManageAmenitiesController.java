package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Amenity;
import HotelReservationSystem.model.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ManageAmenitiesController {

    @FXML
    private TextField amenityNameField;

    @FXML
    private TableView<Amenity> amenitiesTable;

    @FXML
    private TableColumn<Amenity, String> amenityNameColumn;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadAmenities();

        amenitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldAmenity, selectedAmenity) -> fillFields(selectedAmenity)
        );
    }

    private void setupTable() {
        amenityNameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAmenity()));
    }

    private void loadAmenities() {
        ObservableList<Amenity> amenities =
                FXCollections.observableArrayList(AppData.amenities);

        amenitiesTable.setItems(amenities);

        showSuccess("Showing " + amenities.size() + " amenity/amenities.");
    }

    @FXML
    private void addAmenity() {
        String name = amenityNameField.getText().trim();

        if (name.isEmpty()) {
            showError("Amenity name cannot be empty.");
            return;
        }

        if (amenityExists(name, null)) {
            showError("This amenity already exists.");
            return;
        }

        Amenity amenity = new Amenity(name);
        AppData.amenities.add(amenity);

        loadAmenities();
        clearFields();
        showSuccess("Amenity added successfully.");
    }

    @FXML
    private void updateAmenity() {
        Amenity selectedAmenity = amenitiesTable.getSelectionModel().getSelectedItem();

        if (selectedAmenity == null) {
            showError("Please select an amenity to update.");
            return;
        }

        String oldName = selectedAmenity.getAmenity();
        String newName = amenityNameField.getText().trim();

        if (newName.isEmpty()) {
            showError("Amenity name cannot be empty.");
            return;
        }

        if (amenityExists(newName, selectedAmenity)) {
            showError("Another amenity already has this name.");
            return;
        }

        Amenity updatedAmenity = new Amenity(newName);

        int index = AppData.amenities.indexOf(selectedAmenity);
        AppData.amenities.set(index, updatedAmenity);

        updateRoomsUsingAmenity(selectedAmenity, oldName, updatedAmenity);

        loadAmenities();
        clearFields();
        showSuccess("Amenity updated successfully.");
    }

    @FXML
    private void deleteAmenity() {
        Amenity selectedAmenity = amenitiesTable.getSelectionModel().getSelectedItem();

        if (selectedAmenity == null) {
            showError("Please select an amenity to delete.");
            return;
        }

        if (isAmenityUsed(selectedAmenity)) {
            showError("Cannot delete this amenity because some rooms are using it.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Amenity");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete "
                + selectedAmenity.getAmenity() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            AppData.amenities.remove(selectedAmenity);

            loadAmenities();
            clearFields();
            showSuccess("Amenity deleted successfully.");
        }
    }

    private boolean amenityExists(String name, Amenity selectedAmenity) {
        for (Amenity amenity : AppData.amenities) {
            if (amenity.getAmenity().equalsIgnoreCase(name)
                    && amenity != selectedAmenity) {
                return true;
            }
        }

        return false;
    }

    private boolean isAmenityUsed(Amenity selectedAmenity) {
        for (Room room : AppData.rooms) {
            for (Amenity amenity : room.getRoomAmenities()) {
                if (amenity.getAmenity().equalsIgnoreCase(selectedAmenity.getAmenity())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateRoomsUsingAmenity(Amenity oldAmenity, String oldName, Amenity updatedAmenity) {
        for (Room room : AppData.rooms) {
            List<Amenity> roomAmenities = room.getRoomAmenities();

            for (int i = 0; i < roomAmenities.size(); i++) {
                Amenity currentAmenity = roomAmenities.get(i);

                if (currentAmenity == oldAmenity
                        || currentAmenity.getAmenity().equalsIgnoreCase(oldName)) {
                    roomAmenities.set(i, updatedAmenity);
                }
            }
        }
    }

    private void fillFields(Amenity amenity) {
        if (amenity == null) {
            return;
        }

        amenityNameField.setText(amenity.getAmenity());
    }

    @FXML
    private void clearFields() {
        amenityNameField.clear();
        amenitiesTable.getSelectionModel().clearSelection();
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