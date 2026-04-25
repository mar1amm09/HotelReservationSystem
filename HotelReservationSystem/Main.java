package HotelReservationSystem.model;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.Gender;
import HotelReservationSystem.enums.ROLE;
import HotelReservationSystem.enums.ReservationType;
import HotelReservationSystem.enums.paymentMethod;
import HotelReservationSystem.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HotelDatabase database = new HotelDatabase();
        System.out.println("Are you 1.Guest, 2.Admin, or 3.Receptionist");
        int x = scanner.nextInt();
        scanner.nextLine();
        switch (x){
            case 1:{
                System.out.println("GUEST REGISTRATION");

                System.out.print("Please enter username: ");
                String username = scanner.nextLine();

                System.out.print("Please enter password: ");
                String password = scanner.nextLine();

                System.out.print("Enter year of birth: ");
                int year = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                System.out.print("Enter month of birth (1-12): ");
                int month = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                System.out.print("Enter day of birth: ");
                int day = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                LocalDate dob = LocalDate.of(year, month, day);

                System.out.print("Enter balance: ");
                double balance = Double.parseDouble(scanner.nextLine());
                scanner.nextLine();

                System.out.print("Enter address: ");
                String address = scanner.nextLine();

                System.out.print("Enter gender (MALE / FEMALE): ");
                Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

                System.out.print("Enter room preference: ");
                String roomPreferences = scanner.nextLine();

                Guest guest = new Guest(username, password, dob, balance, address, gender, roomPreferences);

                guest.login(database, username, password);

                //LOGIN
                System.out.println("\n GUEST LOGIN");

                System.out.print("Please enter username: ");
                String loginUser = scanner.nextLine();

                System.out.print("Please enter password: ");
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
                    scanner.nextLine();

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
                                try Hotel.areDatesValid(checkIn,checkOut);{
                                } catch (InvalidDateException e){
                                    System.out.println(e.getMessage());
                                }
                                Amenity a1 = new Amenity("WiFi");
                                Amenity a2 = new Amenity("Air Conditioning");

                                List<Amenity> amenities = new ArrayList<>();
                                amenities.add(a1);
                                amenities.add(a2);
                                RoomType roomtype = new RoomType("Single", 5, 500.00);

                                Room room = new Room(101, roomtype, 100.0, true, 4, amenities);
                                System.out.println("Enter the reservation type");
                                Hotel.validateRoomAvailability(room);

                                System.out.println("Enter the reservation type:");
                                ReservationType type = ReservationType.valueOf(scanner.nextLine().toUpperCase());

                                currentReservation = new Reservation( guest, room, checkIn, checkOut,Reservation.Status.PENDING, type);
                                guest.makeReservation(database, currentReservation);

                                System.out.println("Reservation created successfully!");

                                } catch (InvalidDateException | RoomNotAvailableException e) {
                                System.out.println("Error: " + e.getMessage());

                                } catch (Exception e) {
                                System.out.println("Unexpected error: " + e.getMessage());
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
                break;
            }
            case 2: {
                // ===== ADMIN TEST =====
                scanner.nextLine();
                System.out.print("Enter username: ");
                String username1 = scanner.nextLine();

                System.out.print("Enter password: ");
                String password1 = scanner.nextLine();

                System.out.print("Enter year of birth: ");
                int year1 = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                System.out.print("Enter month of birth (1-12): ");
                int month1 = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                System.out.print("Enter day of birth: ");
                int day1 = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                LocalDate dob1 = LocalDate.of(year1, month1, day1);

                System.out.print("Enter working hours: ");
                int wh = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();

                Admin admin = new Admin(username1, password1, dob1, ROLE.ADMIN, wh);

                //let admin choose operation
                System.out.print("What would you like to do? " +
                        "1. Create (Room, RoomType, Amenity)" +
                        "2. View (Guests, Rooms, RoomTypes, Amenities, Reservations" +
                        "3. Delete (Room, RoomType, Amenity");
                int choice = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();
                switch (choice) {
                    case 1: {
                        System.out.print("1.Room" +
                                "2.RoomType" +
                                "3.Amenity ");
                        int choice2 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();
                        switch (choice2) {
                            case 1: {
                                System.out.println("ID?");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("RoomType?");
                                String roomtype = scanner.nextLine();
                                System.out.println("Max capacity of roomtype?");
                                int max = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Price of roomtype?");
                                double price = scanner.nextDouble();
                                scanner.nextLine();
                                RoomType rt = new RoomType(roomtype, max, price);
                                System.out.println("Is this room currently available?");
                                boolean available = scanner.nextBoolean();
                                scanner.nextLine();
                                System.out.println("Room Floor?");
                                int floor = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Room Amenities?" +
                                        "Enter d when done.");
                                String amenity = scanner.nextLine();
                                List<Amenity> amenities = new ArrayList<>();
                                while (!amenity.equals("d")) {
                                    Amenity a = new Amenity(amenity);
                                    amenities.add(a);
                                }
                                Room r1 = new Room(id, rt, rt.getPrice(), available, floor, amenities);
                                admin.addRoom(database, r1);
                                System.out.println("Room Created Successfully!!");
                                break;
                            }
                            case 2: {
                                System.out.println("RoomType name?");
                                String name = scanner.nextLine();
                                System.out.println("Maximum capacity of room?");
                                int max = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Price?");
                                double price = scanner.nextDouble();
                                scanner.nextLine();
                                RoomType rt1 = new RoomType(name, max, price);
                                admin.addRoomType(database, rt1);
                                System.out.println("Room Type added successfully!!");
                                break;
                            }
                            case 3: {
                                System.out.println("Amenity name?");
                                String amenity = scanner.nextLine();
                                Amenity a1 = new Amenity(amenity);
                                admin.addAmenity(database, a1);
                                System.out.println("Amenity added successfully!!");
                                break;
                            }
                        }
                    }
                    case 2: {
                        System.out.print("1.Guests" +
                                "2.Rooms" +
                                "3.RoomTypes" +
                                "4.Amenities" +
                                "5.Reservations");
                        int choice3 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();
                        switch (choice3) {
                            case 1: {
                                admin.viewAllGuests(database);
                                break;
                            }
                            case 2: {
                                admin.viewAllRooms(database);
                                break;
                            }
                            case 3: {
                                admin.viewAllRoomTypes(database);
                                break;
                            }
                            case 4: {
                                admin.viewAllAmenities(database);
                                break;
                            }
                            case 5: {
                                admin.viewAllReservations(database);
                                break;
                            }
                        }
                    }
                    case 3: {
                        System.out.print("1.Room" +
                                "2.RoomType" +
                                "3.Amenity ");
                        int choice4 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();
                        switch (choice4) {
                            case 1: {
                                System.out.println("Enter id of room to delete");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                boolean removed = false;
                                for (Room r : database.rooms) {
                                    if (r.getRoomId() == id) {
                                        admin.removeRoom(database, r);
                                        removed = true;
                                        break;
                                    }
                                }
                                if (!removed) {
                                    System.out.println("Cannot find room");
                                }
                                break;
                            }
                            case 2: {
                                System.out.println("Enter name of roomtype to delete");
                                String name = scanner.nextLine();
                                boolean removed = false;
                                for (RoomType rt2 : database.roomTypes) {
                                    if (rt2.getRoomTypeName().equals(name)) {
                                        admin.removeRoomType(database, rt2);
                                        System.out.println("RoomType removed successfully!!");
                                        removed = true;
                                        break;
                                    }
                                }
                                if (!removed) {
                                    System.out.println("Could not find RoomType");
                                }
                                break;
                            }
                            case 3: {
                                System.out.println("Enter name of amenity to delete");
                                String name = scanner.nextLine();
                                boolean removed = false;
                                for (Amenity a : database.allAmenities) {
                                    if (a.getAmenity().equals(name)) {
                                        admin.removeAmenity(database, a);
                                        System.out.println("Amenity removed successfully!!");
                                        removed = true;
                                        break;
                                    }
                                }
                                if (!removed) {
                                    System.out.println("Cannot find amenity");
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            }
                case 3:{

                        // ===== RECEPTIONIST TEST =====
                    scanner.nextLine();
                        System.out.print("Enter username: ");
                        String username2 = scanner.nextLine();

                        System.out.print("Enter password: ");
                        String password2 = scanner.nextLine();

                        System.out.print("Enter year of birth: ");
                        int year2 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();

                        System.out.print("Enter month of birth (1-12): ");
                        int month2 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();

                        System.out.print("Enter day of birth: ");
                        int day2 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();

                        LocalDate dob2 = LocalDate.of(year2, month2, day2);

                        System.out.print("Enter working hours: ");
                        int wh2 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();
                        Receptionist receptionist = new Receptionist(username2, password2, dob2, ROLE.RECEPTIONIST,wh2);

                        System.out.println("What do you want to do?" +
                                "1.Manage Check In" +
                                "2.Manage Check Out" +
                                "3.Create Invoice");
                        int choice5 = Integer.parseInt(scanner.nextLine());
                        scanner.nextLine();
                        switch (choice5){
                            case 1:{
                                System.out.println("Enter room id of reservation you want to manage");
                                int id2 = scanner.nextInt();
                                scanner.nextLine();
                                boolean found = false;
                                for (Reservation r : database.reservations){
                                    if (r.getRoom().getRoomId() == id2){
                                        System.out.println("Check in date: " + r.getCheckIn());
                                        System.out.println("Do you wish to edit this date? Y/N");
                                        String manage = scanner.nextLine();
                                        if (manage.equals("Y")){
                                            System.out.print("Enter year: ");
                                            int year3 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            System.out.print("Enter month(1-12): ");
                                            int month3 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            System.out.print("Enter day: ");
                                            int day3 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            LocalDate CheckInDate = LocalDate.of(year3, month3, day3);

                                        }
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found){
                                    System.out.println("Reservation not found");
                                }
                            }
                            case 2:{
                                System.out.println("Enter room id of reservation you want to manage");
                                int id3 = scanner.nextInt();
                                scanner.nextLine();
                                boolean found = false;
                                for (Reservation r : database.reservations){
                                    if (r.getRoom().getRoomId() == id3){
                                        System.out.println("Check out date: " + r.getCheckOut());
                                        System.out.println("Do you wish to edit this date? Y/N");
                                        String manage = scanner.nextLine();
                                        if (manage.equals("Y")){
                                            System.out.print("Enter year: ");
                                            int year4 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            System.out.print("Enter month(1-12): ");
                                            int month4 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            System.out.print("Enter day: ");
                                            int day4 = Integer.parseInt(scanner.nextLine());
                                            scanner.nextLine();

                                            LocalDate CheckOutDate = LocalDate.of(year4, month4, day4);

                                        }
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found){
                                    System.out.println("Reservation not found");
                                }
                            }
                            case 3: {
                                System.out.println("Enter total amount");
                                double total = scanner.nextDouble();
                                System.out.println("Enter payment method:" +
                                        "1.Cash" +
                                        "2.Credit" +
                                        "3.Online");
                                int pay = scanner.nextInt();
                                scanner.nextLine();
                                switch (pay) {
                                    case 1: {
                                        paymentMethod method = paymentMethod.CASH;
                                        break;
                                    }
                                    case 2: {
                                        paymentMethod method = paymentMethod.CREDIT_CARD;
                                        break;
                                    }
                                    case 3: {
                                        paymentMethod method = paymentMethod.ONLINE;
                                    }
                                }
                                System.out.println("Do you want to add another payment method? (Y/N)");
                                String choice6 = scanner.nextLine();
                                List<paymentMethod> methods = null;
                                if (choice6.equals("Y")) {
                                    methods = new ArrayList<>();
                                    paymentMethod method1 = paymentMethod.CASH;
                                    paymentMethod method2 = paymentMethod.CREDIT_CARD;
                                    paymentMethod method3 = paymentMethod.ONLINE;
                                    methods.add(method1);
                                    methods.add(method2);
                                    methods.add(method3);
                                }
                                System.out.println("What is the reservation type?" +
                                        "1.Full board" +
                                        "2.Half board" +
                                        "3.All inclusive" +
                                        "4.Bed and breakfast");
                                int type = scanner.nextInt();
                                scanner.nextLine();
                                switch (type) {
                                    case 1: {
                                        ReservationType t2 = ReservationType.FULL_BOARD;
                                        break;
                                    }
                                    case 2: {
                                        ReservationType t2 = ReservationType.HALF_BOARD;
                                        break;
                                    }
                                    case 3: {
                                        ReservationType t2 = ReservationType.ALL_INCLUSIVE;
                                        break;
                                    }
                                    case 4: {
                                        ReservationType t2 = ReservationType.BED_AND_BREAKFAST;
                                    }
                                }
                                Invoice i1 = new Invoice(total, methods, LocalDate.now());
                                System.out.println("Invoice created successfully!");
                            }
                        }
                    }
                    break;
                }
        scanner.close();
        }

    }
