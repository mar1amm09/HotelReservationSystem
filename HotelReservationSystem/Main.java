package HotelReservationSystem.model;

import HotelReservationSystem.enums.ReservationType;
import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.Gender;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HotelDatabase database = new HotelDatabase();

        System.out.println("=== Guest Registration ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter year of birth: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter month of birth (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter day of birth: ");
        int day = Integer.parseInt(scanner.nextLine());

        LocalDate dob = LocalDate.of(year, month, day);

        System.out.print("Enter balance: ");
        double balance = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter gender (MALE / FEMALE): ");
        Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Enter room preference: ");
        String roomPreferences = scanner.nextLine();

        Guest guest = new Guest(username, password, dob, balance, address, gender, roomPreferences);

        guest.login(database, username, password);

        // === LOGIN ===
        System.out.println("\n=== Guest Login ===");

        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        if (!guest.login(database, loginUser, loginPass)) {
            System.out.println("Login failed!");
            return;
        }

        System.out.println("Login successful!");

        // Store reservation outside switch
        Reservation currentReservation = null;

        boolean running = true;

        while (running) {
            System.out.println("\n=== Guest Actions ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View My Reservations");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    guest.viewAvailableRooms(database);
                    break;

                case 2:
                    try {
                        System.out.print("Enter check-in date (YYYY-MM-DD): ");
                        LocalDate checkIn = LocalDate.parse(scanner.nextLine());

                        System.out.print("Enter check-out date (YYYY-MM-DD): ");
                        LocalDate checkOut = LocalDate.parse(scanner.nextLine());

                        System.out.println("Enter the reservation type");
                        ReservationType type= ReservationType.valueOf(scanner.nextLine());
                        
                        Amenity a1 = new Amenity("WiFi");
                        Amenity a2 = new Amenity("Air Conditioning");

                        List<Amenity> amenities = new ArrayList<>();
                        amenities.add(a1);
                        amenities.add(a2);
                        RoomType roomtype = new RoomType("Single", 5, 500.00);
                        Room room = new Room(101, roomtype, 100.0, true, 4, amenities);

                        currentReservation = new Reservation(guest, room, checkIn, checkOut, Reservation.Status.PENDING);
                        guest.makeReservation(database, currentReservation);

                        System.out.println("Reservation created successfully!");

                    } catch (Exception e) {
                        System.out.println("Invalid input. Try again.");
                    }
                    break;

                case 3:
                    if (currentReservation != null) {
                        guest.cancelReservation(database, currentReservation);
                        currentReservation = null;
                        System.out.println("Reservation cancelled successfully.");
                    } else {
                        System.out.println("No reservation found.");
                    }
                    break;

                case 4:
                    guest.viewReservations(database, guest);
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
