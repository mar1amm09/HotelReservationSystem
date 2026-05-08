package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.enums.ReservationType;
import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Reservation;
import HotelReservationSystem.model.Room;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class ReservationController {

    @FXML
    private Label roomLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private DatePicker checkInPicker;

    @FXML
    private DatePicker checkOutPicker;

    @FXML
    private ComboBox<String> reservationTypeCombo;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label messageLabel;

    private Room selectedRoom;

    @FXML
    public void initialize() {
        selectedRoom = AppData.selectedRoom;

        setupDatePicker(checkInPicker);
        setupDatePicker(checkOutPicker);

        for (ReservationType type : ReservationType.values()) {
            reservationTypeCombo.getItems().add(formatReservationType(type));
        }
        reservationTypeCombo.setOnAction(event -> updateTotalPrice());

        checkInPicker.setOnAction(event -> updateTotalPrice());
        checkOutPicker.setOnAction(event -> updateTotalPrice());

        if (selectedRoom == null) {
            showError("No room selected. Please go back and choose a room.");
            return;
        }

        roomLabel.setText(String.valueOf(selectedRoom.getRoomId()));
        typeLabel.setText(selectedRoom.getRoomType().getRoomTypeName());
        priceLabel.setText(selectedRoom.getRoomPrice() + " EGP");
    }

    private ReservationType getSelectedReservationType() {
        String selectedText = reservationTypeCombo.getValue();

        if (selectedText == null) {
            return null;
        }

        for (ReservationType type : ReservationType.values()) {
            String displayName = type.toString().replace("_", " ");

            if (selectedText.toUpperCase().startsWith(displayName)) {
                return type;
            }
        }

        return null;
    }

    private String formatReservationType(ReservationType type) {
        double extraPrice = AppData.getReservationTypeExtraPrice(type);

        String displayName = type.toString().replace("_", " ");

        return displayName + " (+" + extraPrice + " EGP/night)";
    }

    @FXML
    private void confirmReservation() {
        if (!(AppData.currentUser instanceof Guest)) {
            showError("Only guests can make reservations.");
            return;
        }

        if (selectedRoom == null) {
            showError("Please select a room first.");
            return;
        }

        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();
        ReservationType reservationType = getSelectedReservationType();

        if (checkIn == null || checkOut == null || reservationType == null) {
            showError("Please choose check-in date, check-out date, and reservation type.");
            return;
        }

        if (checkIn.isBefore(LocalDate.now())) {
            showError("Check-in date cannot be before today.");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            showError("Check-out date must be after check-in date.");
            return;
        }

        if (!selectedRoom.getAvailability()) {
            showError("This room is no longer available.");
            return;
        }

        Guest guest = (Guest) AppData.currentUser;

        Reservation reservation = new Reservation(
                guest,
                selectedRoom,
                checkIn,
                checkOut,
                Reservation.Status.PENDING,
                reservationType
        );

        boolean reserved = AppData.database.makeReservation(reservation);

        if (reserved) {
            AppData.reservations.add(reservation);

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            double extraPrice = AppData.getReservationTypeExtraPrice(reservationType);
            double total = nights * (selectedRoom.getRoomPrice() + extraPrice);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reservation Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Reservation created successfully!\n\n" +
                            "Room: " + selectedRoom.getRoomId() + "\n" +
                            "Nights: " + nights + "\n" +
                            "Total: " + total + " EGP\n" +
                            "Status: PENDING"
            );
            alert.showAndWait();

            AppData.selectedRoom = null;
            SceneController.switchToGuestDashboard();
        } else {
            showError("Reservation failed. Room may not be available.");
        }
    }

    private void updateTotalPrice() {
        if (selectedRoom == null) {
            totalPriceLabel.setText("0 EGP");
            return;
        }

        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();

        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            totalPriceLabel.setText("0 EGP");
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        double extraPrice = AppData.getReservationTypeExtraPrice(getSelectedReservationType());

        double total = nights * (selectedRoom.getRoomPrice() + extraPrice);
        totalPriceLabel.setText(total + " EGP");
    }

    private void setupDatePicker(DatePicker picker) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        picker.setPromptText("dd/MM/yyyy");

        picker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date == null) {
                    return "";
                }
                return formatter.format(date);
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }

                try {
                    return LocalDate.parse(text.trim(), formatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });
    }

    @FXML
    private void goBack() {
        SceneController.switchToRoomBrowsing();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }
}