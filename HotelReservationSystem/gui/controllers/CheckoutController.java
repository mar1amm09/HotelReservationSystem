package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.enums.paymentMethod;
import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Invoice;
import HotelReservationSystem.model.Reservation;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CheckoutController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TableView<Reservation> checkoutTable;

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
    private Label selectedTotalLabel;

    @FXML
    private CheckBox cashCheckBox;

    @FXML
    private CheckBox creditCardCheckBox;

    @FXML
    private CheckBox onlineCheckBox;

    @FXML
    private Label messageLabel;

    private Guest currentGuest;

    @FXML
    public void initialize() {
        if (AppData.currentUser instanceof Guest) {
            currentGuest = (Guest) AppData.currentUser;
        } else {
            showError("No guest is logged in.");
            return;
        }

        setupTable();
        loadReservations();

        checkoutTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateSelectedTotal(newValue)
        );
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
            long nights = getNights(data.getValue());
            return new SimpleIntegerProperty((int) nights).asObject();
        });

        totalColumn.setCellValueFactory(data -> {
            double total = getTotal(data.getValue());
            return new SimpleDoubleProperty(total).asObject();
        });

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStatus())));
    }

    @FXML
    private void loadReservations() {
        ObservableList<Reservation> unpaidReservations = FXCollections.observableArrayList();

        for (Reservation reservation : AppData.reservations) {
            if (reservation.getGuest().equals(currentGuest)
                    && reservation.getStatus() == Reservation.Status.CONFIRMED
                    && !AppData.paidReservations.contains(reservation)) {
                unpaidReservations.add(reservation);
            }
        }

        checkoutTable.setItems(unpaidReservations);
        updateBalanceLabel();
        selectedTotalLabel.setText("0 EGP");

        if (unpaidReservations.isEmpty()) {
            showError("You have no confirmed unpaid reservations. You can pay only after receptionist check-in.");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Select a reservation to pay.");
        }
    }

    @FXML
    private void confirmPayment() {
        Reservation selectedReservation = checkoutTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showError("Please select a reservation first.");
            return;
        }
        if (selectedReservation.getStatus() != Reservation.Status.CONFIRMED) {
            showError("You can only pay after receptionist check-in.");
            return;
        }

        if (AppData.paidReservations.contains(selectedReservation)) {
            showError("This reservation is already paid.");
            return;
        }

        ArrayList<paymentMethod> methods = getSelectedPaymentMethods();

        if (methods.isEmpty()) {
            showError("Please choose at least one payment method.");
            return;
        }

        double total = getTotal(selectedReservation);

        if (currentGuest.balance() < total) {
            showError("Insufficient balance. Your balance is " + currentGuest.balance() + " EGP.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Payment");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Pay " + total + " EGP for Room "
                + selectedReservation.getRoom().getRoomId() + "?");

        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        currentGuest.setBalance(currentGuest.balance() - total);


        LocalDate paymentDate = LocalDate.now();

        Invoice invoice = new Invoice(total, methods, paymentDate, selectedReservation.getType());
        AppData.invoices.add(invoice);
        AppData.paidReservations.add(selectedReservation);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Payment Successful");
        successAlert.setHeaderText(null);
        successAlert.setContentText(
                "Payment completed successfully!\n\n" +
                        "Total: " + total + " EGP\n" +
                        "Payment Date: " + paymentDate + "\n" +
                        "New Balance: " + currentGuest.balance() + " EGP"
        );
        successAlert.showAndWait();

        clearPaymentMethods();
        loadReservations();
    }

    private ArrayList<paymentMethod> getSelectedPaymentMethods() {
        ArrayList<paymentMethod> methods = new ArrayList<>();

        if (cashCheckBox.isSelected()) {
            methods.add(paymentMethod.CASH);
        }

        if (creditCardCheckBox.isSelected()) {
            methods.add(paymentMethod.CREDIT_CARD);
        }

        if (onlineCheckBox.isSelected()) {
            methods.add(paymentMethod.ONLINE);
        }

        return methods;
    }

    private void updateSelectedTotal(Reservation reservation) {
        if (reservation == null) {
            selectedTotalLabel.setText("0 EGP");
            return;
        }

        selectedTotalLabel.setText(getTotal(reservation) + " EGP");
    }

    private long getNights(Reservation reservation) {
        return ChronoUnit.DAYS.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );
    }

    private double getTotal(Reservation reservation) {
        return AppData.calculateReservationTotal(reservation);
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Current Balance: " + currentGuest.balance() + " EGP");
    }

    private void clearPaymentMethods() {
        cashCheckBox.setSelected(false);
        creditCardCheckBox.setSelected(false);
        onlineCheckBox.setSelected(false);
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