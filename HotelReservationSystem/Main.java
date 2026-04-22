package HotelReservationSystem.model;
import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.Gender;
import java.time.LocalDate;
import java.util.Scanner;
public class Main {
    public static void main  (String[] args){
        Scanner scanner = new Scanner(System.in);
        HotelDatabase database = new HotelDatabase();
        System.out.println("   Guest Registration   ");
        System.out.println("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.println("\nEnter password: ");
        String password = scanner.nextLine();
        System.out.print("\nEnter year of birth: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("\nEnter month of birth (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.print("\nEnter day of birth: ");
        int day = Integer.parseInt(scanner.nextLine());
        LocalDate dob = LocalDate.of(year, month, day);
        System.out.println("\nEnter balance: ");
        double balance = Double.parseDouble(scanner.nextLine());
        System.out.println("\nEnter address: ");
        String address = scanner.nextLine();
        System.out.println("\nEnter gender (MALE / FEMALE): ");
        Gender gender = Gender.valueOf(scanner.nextLine());
        System.out.println("\nEnter room preference: ");
        String roomPreferences = scanner.nextLine();
        Guest guest = new Guest(username, password, dob, balance, address, gender,roomPreferences);
        guest.login(database, username, password);
        System.out.println("\n   Guest Login   ");
        System.out.print("\nEnter username: ");
        String loginUser = scanner.nextLine();
        System.out.print("\nEnter password: ");
        String loginPass = scanner.nextLine();
        guest.login(database, loginUser, loginPass);
        System.out.println("\n=== Guest Actions ===");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Make Reservation");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View My Reservations");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                guest.viewAvailableRooms(database);
                break;
            case 2:
                System.out.println("Enter check in date: ");
                LocalDate checkIn = LocalDate.parse(scanner.nextLine());
                System.out.println("Enter check out date: ");
                LocalDate checkOut = LocalDate.parse(scanner.nextLine());
                Room room1 = new Room(  );
                Reservation newReservation = new Reservation(guest, room1, checkIn, checkOut);
                guest.makeReservation(database, newReservation);
                System.out.println("Reservation created successfully!");
                break;
            case 3:
                if (newReservation != null) {
                    guest.cancelReservation(database, newReservation);
                    System.out.println("Reservation cancelled successfully.");
                } else {
                    System.out.println("No reservation found to cancel.");
                }
                break;
            case 4:
                guest.viewReservations(database, guest);
                break;
            default:
                System.out.println("   Invalid choice   ");
        }
    }
}


