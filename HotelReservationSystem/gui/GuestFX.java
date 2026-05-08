package HotelReservationSystem.gui;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.enums.Gender;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;

public class GuestFX extends Application {
    private HotelDatabase database = new HotelDatabase();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel System - JavaFX");

        // 1. تصميم الشبكة (GridPane)
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // 2. العناصر (Controls)
        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        TextField balanceField = new TextField("1000");
        ComboBox<Gender> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Gender.values()); // استخدام الـ Enum بتاعك

        Button registerBtn = new Button("Register & Login");
        registerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // 3. إضافة العناصر للشبكة
        grid.add(new Label("Username:"), 0, 0); grid.add(userField, 1, 0);
        grid.add(new Label("Password:"), 0, 1); grid.add(passField, 1, 1);
        grid.add(new Label("Balance:"), 0, 2); grid.add(balanceField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3); grid.add(genderCombo, 1, 3);
        grid.add(registerBtn, 1, 4);

        // 4. برمجة زرار التسجيل (Action)
        registerBtn.setOnAction(e -> {
            try {
                String user = userField.getText();
                String pass = passField.getText();
                double bal = Double.parseDouble(balanceField.getText());

                // استخدام الـ Constructor بتاعك
                Guest guest = new Guest(user, pass, LocalDate.of(2000, 1, 1), bal, "Cairo", genderCombo.getValue(), "WiFi");
                database.register(guest);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome " + user + "!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input!");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
