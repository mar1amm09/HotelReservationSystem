package HotelReservationSystem.gui;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.ROLE;
import HotelReservationSystem.model.Admin;
import HotelReservationSystem.model.Guest;
import HotelReservationSystem.model.Person;
import HotelReservationSystem.model.Receptionist;
import HotelReservationSystem.model.Room;
import HotelReservationSystem.model.RoomType;
import HotelReservationSystem.model.Amenity;
import HotelReservationSystem.model.Reservation;
import HotelReservationSystem.model.Invoice;
import HotelReservationSystem.enums.ReservationType;
import HotelReservationSystem.model.Reservation;

import java.util.Arrays;

import java.time.LocalDate;
import java.util.ArrayList;

public class AppData {

    public static final HotelDatabase database = new HotelDatabase();

    public static Person currentUser;
    public static final ArrayList<Guest> guests = new ArrayList<>();
    public static Room selectedRoom;
    public static Reservation selectedReservation;

    public static final ArrayList<Reservation> reservations = new ArrayList<>();
    public static final ArrayList<Invoice> invoices = new ArrayList<>();
    public static final ArrayList<Reservation> paidReservations = new ArrayList<>();

    public static final ArrayList<Room> rooms = new ArrayList<>();
    public static final ArrayList<RoomType> roomTypes = new ArrayList<>();
    public static final ArrayList<Amenity> amenities = new ArrayList<>();
    public static final ArrayList<Admin> admins = new ArrayList<>();
    public static final ArrayList<Receptionist> receptionists = new ArrayList<>();

    static {
        admins.add(new Admin("admin", "1234",
                LocalDate.of(1990, 1, 1),
                ROLE.ADMIN,
                8));

        receptionists.add(new Receptionist("reception", "1234",
                LocalDate.of(1995, 1, 1),
                ROLE.RECEPTIONIST,
                8));

        Amenity wifi = new Amenity("WiFi");
        Amenity tv = new Amenity("TV");
        Amenity minibar = new Amenity("Mini-bar");
        Amenity seaView = new Amenity("Sea View");
        amenities.add(wifi);
        amenities.add(tv);
        amenities.add(minibar);
        amenities.add(seaView);

        RoomType single = new RoomType("Single", 1, 800);
        RoomType doubleRoom = new RoomType("Double", 2, 1300);
        RoomType suite = new RoomType("Suite", 4, 2500);

        roomTypes.add(single);
        roomTypes.add(doubleRoom);
        roomTypes.add(suite);

        rooms.add(new Room(101, single, 900, true, 1, Arrays.asList(wifi, tv)));
        rooms.add(new Room(102, single, 850, true, 1, Arrays.asList(wifi)));
        rooms.add(new Room(201, doubleRoom, 1300, true, 2, Arrays.asList(wifi, tv, minibar)));
        rooms.add(new Room(202, doubleRoom, 1800, false, 2, Arrays.asList(tv, minibar)));
        rooms.add(new Room(301, suite, 2900, true, 3, Arrays.asList(wifi, tv, minibar, seaView)));
        rooms.add(new Room(302, suite, 2700, true, 3, Arrays.asList(wifi, seaView)));

        for (Room room : rooms) {
            database.addRoom(room);
        }
    }

    public static boolean usernameExists(String username) {

        if (database.doesUsernameExist(username)) {
            return true;
        }

        for (Admin admin : admins) {
            if (admin.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        for (Receptionist receptionist : receptionists) {
            if (receptionist.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public static double getReservationTypeExtraPrice(ReservationType type) {
        if (type == null) {
            return 0;
        }

        String typeName = type.toString().toLowerCase();

        if (typeName.contains("breakfast")) {
            return 300;
        } else if (typeName.contains("half")) {
            return 700;
        } else if (typeName.contains("full")) {
            return 1200;
        } else if (typeName.contains("all")) {
            return 1500;
        }

        return 0;
    }

    public static double calculateReservationTotal(Reservation reservation) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                reservation.getCheckIn(),
                reservation.getCheckOut()
        );

        double roomPrice = reservation.getRoom().getRoomPrice();
        double extraPrice = getReservationTypeExtraPrice(reservation.getType());

        return nights * (roomPrice + extraPrice);
    }

    public static Person login(String username, String password) {

        Guest guest = database.login(username, password);

        if (guest != null) {
            return guest;
        }

        for (Admin admin : admins) {
            if (admin.getUsername().equals(username)
                    && admin.getPassword().equals(password)) {
                return admin;
            }
        }

        for (Receptionist receptionist : receptionists) {
            if (receptionist.getUsername().equals(username)
                    && receptionist.getPassword().equals(password)) {
                return receptionist;
            }
        }

        return null;
    }
}