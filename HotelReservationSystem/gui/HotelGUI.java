package HotelReservationSystem.gui;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.model.*;
import HotelReservationSystem.enums.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class HotelGUI extends Application {
    private HotelDatabase database = new HotelDatabase();
    private Guest currentGuest;
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Aura Hotel System");
        showLoginScreen();
    }

    public void showLoginScreen() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f4f4f4;");

        Label title = new Label("Hotel Login");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        TextField userField = new TextField();
        userField.setPromptText("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        loginBtn.setMinWidth(100);
        Button goToRegisterBtn = new Button("Create New Account");

        loginBtn.setOnAction(e -> {
            currentGuest = database.login(userField.getText(), passField.getText());
            if (currentGuest != null) {
                showGuestDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid Username or Password.");
            }
        });

        goToRegisterBtn.setOnAction(e -> showRegisterScreen());

        layout.getChildren().addAll(title, userField, passField, loginBtn, goToRegisterBtn);
        window.setScene(new Scene(layout, 400, 450));
        window.show();
    }

    public void showRegisterScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); grid.setVgap(15);
        grid.setPadding(new Insets(20));

        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        TextField balanceField = new TextField();
        ComboBox<Gender> genderBox = new ComboBox<>();
        genderBox.getItems().addAll(Gender.values());

        Button submitBtn = new Button("Register");
        Button backBtn = new Button("Back");

        grid.add(new Label("Username:"), 0, 0); grid.add(userField, 1, 0);
        grid.add(new Label("Password:"), 0, 1); grid.add(passField, 1, 1);
        grid.add(new Label("Initial Balance:"), 0, 2); grid.add(balanceField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3); grid.add(genderBox, 1, 3);
        grid.add(submitBtn, 1, 4); grid.add(backBtn, 0, 4);

        submitBtn.setOnAction(e -> {
            try {
                double bal = Double.parseDouble(balanceField.getText());
                Guest g = new Guest(userField.getText(), passField.getText(), LocalDate.now(), bal, "Address", genderBox.getValue(), "None");
                if (database.register(g)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!");
                    showLoginScreen();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Username already exists!");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid data.");
            }
        });

        backBtn.setOnAction(e -> showLoginScreen());
        window.setScene(new Scene(grid, 400, 450));
    }

    public void showGuestDashboard() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Label welcome = new Label("Welcome, " + currentGuest.getUsername());
        welcome.setStyle("-fx-font-size: 18px;");

        Button viewRooms = new Button("View Available Rooms");
        Button bookTrans = new Button("Book Transportation");
        Button logout = new Button("Logout");

        viewRooms.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Rooms", "Feature coming soon!"));
        logout.setOnAction(e -> showLoginScreen());

        layout.getChildren().addAll(welcome, viewRooms, bookTrans, logout);
        window.setScene(new Scene(layout, 400, 400));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}