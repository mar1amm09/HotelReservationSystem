package HotelReservationSystem.gui.controllers;

import HotelReservationSystem.enums.Gender;
import HotelReservationSystem.enums.ROLE;
import HotelReservationSystem.gui.AppData;
import HotelReservationSystem.model.Admin;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Receptionist;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;


public class RegisterController {

    private static final String ADMIN_SECRET_CODE = "ADMIN2026";
    private static final String RECEPTIONIST_SECRET_CODE = "STAFF2026";

    @FXML
    private ComboBox<String> accountTypeCombo;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private HBox dobBox;

    @FXML
    private ComboBox<Integer> dayCombo;

    @FXML
    private ComboBox<Integer> monthCombo;

    @FXML
    private ComboBox<Integer> yearCombo;

    @FXML
    private Label genderLabel;

    @FXML
    private ComboBox<String> genderCombo;

    @FXML
    private TextField addressField;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField workingHoursField;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label dobLabel;

    @FXML
    private PasswordField staffCodeField;

    @FXML
    public void initialize() {
        accountTypeCombo.getItems().addAll("Guest", "Admin", "Receptionist");
        genderCombo.getItems().addAll("MALE", "FEMALE");

        setupDateOfBirthDropdowns();

        showBasicFields(false);
        showGuestFields(false);
        showStaffFields(false);
    }

    private void setupDateOfBirthDropdowns() {
        dayCombo.getItems().clear();
        monthCombo.getItems().clear();
        yearCombo.getItems().clear();

        for (int day = 1; day <= 31; day++) {
            dayCombo.getItems().add(day);
        }

        for (int month = 1; month <= 12; month++) {
            monthCombo.getItems().add(month);
        }

        int currentYear = LocalDate.now().getYear();

        for (int year = currentYear - 100; year <= currentYear; year++) {
            yearCombo.getItems().add(year);
        }
    }

    private LocalDate getDateOfBirth() {
        Integer day = dayCombo.getValue();
        Integer month = monthCombo.getValue();
        Integer year = yearCombo.getValue();

        if (day == null || month == null || year == null) {
            return null;
        }

        try {
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    private void handleAccountTypeChange() {
        String type = accountTypeCombo.getValue();

        if (type == null) {
            showBasicFields(false);
            showGuestFields(false);
            showStaffFields(false);
            return;
        }

        showBasicFields(true);

        if (type.equals("Guest")) {
            showGuestFields(true);
            showStaffFields(false);
        } else {
            showGuestFields(false);
            showStaffFields(true);
        }

        messageLabel.setText("");
    }

    @FXML
    private void handleRegister() {
        String type = accountTypeCombo.getValue();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        LocalDate dob = getDateOfBirth();

        if (type == null || username.isEmpty() || password.isEmpty() || dob == null) {
            showError("Please fill account type, username, password, and date of birth.");
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate minimumAllowedDate = today.minusYears(18);

        if (dob.isAfter(today)) {
            showError("Date of birth cannot be in the future.");
            return;
        }

        if (dob.isAfter(minimumAllowedDate)) {
            showError("You must be at least 18 years old to register.");
            return;
        }

        if (password.length() < 4) {
            showError("Password must be at least 4 characters.");
            return;
        }

        if (AppData.usernameExists(username)) {
            showError("Username already exists. Choose another username.");
            return;
        }

        try {
            if (type.equals("Guest")) {
                registerGuest(username, password, dob);
            } else if (type.equals("Admin")) {
                registerAdmin(username, password, dob);
            } else if (type.equals("Receptionist")) {
                registerReceptionist(username, password, dob);
            }
        } catch (NumberFormatException e) {
            showError("Balance and working hours must be valid numbers.");
        }
    }

    private void registerGuest(String username, String password, LocalDate dob) {
        String genderText = genderCombo.getValue();
        String address = addressField.getText().trim();
        String balanceText = balanceField.getText().trim();

        if (genderText == null || address.isEmpty() || balanceText.isEmpty()) {
            showError("Please fill all guest fields.");
            return;
        }

        double balance = Double.parseDouble(balanceText);

        if (balance < 0) {
            showError("Balance cannot be negative.");
            return;
        }

        Gender gender = Gender.valueOf(genderText);

        String roomPreference = "No preference";

        Guest guest = new Guest(username, password, dob, balance, address, gender, roomPreference);

        boolean registered = AppData.database.register(guest);

        if (registered) {
            AppData.guests.add(guest);
            showSuccess("Guest registered successfully. You can login now.");
            clearFields();
        } else {
            showError("Registration failed.");
        }
    }

    private void registerAdmin(String username, String password, LocalDate dob) {
        String hoursText = workingHoursField.getText().trim();
        String staffCode = staffCodeField.getText().trim();

        if (hoursText.isEmpty()) {
            showError("Please enter working hours.");
            return;
        }
        if (staffCode.isEmpty()) {
            showError("Please enter the admin secret code.");
            return;
        }

        if (!staffCode.equals(ADMIN_SECRET_CODE)) {
            showError("Invalid admin secret code.");
            return;
        }
        int workingHours = Integer.parseInt(hoursText);

        if (workingHours < 0) {
            showError("Working hours cannot be negative.");
            return;
        }

        Admin admin = new Admin(username, password, dob, ROLE.ADMIN, workingHours);
        AppData.admins.add(admin);

        showSuccess("Admin registered successfully. You can login now.");
        clearFields();
    }

    private void registerReceptionist(String username, String password, LocalDate dob) {
        String hoursText = workingHoursField.getText().trim();
        String staffCode = staffCodeField.getText().trim();

        if (hoursText.isEmpty()) {
            showError("Please enter working hours.");
            return;
        }
        if (staffCode.isEmpty()) {
            showError("Please enter the receptionist secret code.");
            return;
        }

        if (!staffCode.equals(RECEPTIONIST_SECRET_CODE)) {
            showError("Invalid receptionist secret code.");
            return;
        }
        int workingHours = Integer.parseInt(hoursText);

        if (workingHours < 0) {
            showError("Working hours cannot be negative.");
            return;
        }

        Receptionist receptionist = new Receptionist(username, password, dob, ROLE.RECEPTIONIST, workingHours);
        AppData.receptionists.add(receptionist);

        showSuccess("Receptionist registered successfully. You can login now.");
        clearFields();
    }

    @FXML
    private void goBack() {
        SceneController.switchToWelcome();
    }

    private void showBasicFields(boolean visible) {
        setVisibleManaged(usernameField, visible);
        setVisibleManaged(passwordField, visible);
        setVisibleManaged(dobLabel, visible);
        setVisibleManaged(dobBox, visible);
        setVisibleManaged(registerButton, visible);
    }

    private void showGuestFields(boolean visible) {
        setVisibleManaged(genderLabel, visible);
        setVisibleManaged(genderCombo, visible);
        setVisibleManaged(addressField, visible);
        setVisibleManaged(balanceField, visible);
    }

    private void showStaffFields(boolean visible) {
        setVisibleManaged(workingHoursField, visible);
        setVisibleManaged(staffCodeField, visible);
    }

    private void setVisibleManaged(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        dayCombo.setValue(null);
        monthCombo.setValue(null);
        yearCombo.setValue(null);
        genderCombo.setValue(null);
        addressField.clear();
        balanceField.clear();
        workingHoursField.clear();
        staffCodeField.clear();
        accountTypeCombo.setValue(null);

        showBasicFields(false);
        showGuestFields(false);
        showStaffFields(false);
    }
}