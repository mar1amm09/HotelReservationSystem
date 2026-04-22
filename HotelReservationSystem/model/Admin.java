package HotelReservationSystem.model;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.ROLE;

import java.time.LocalDate;

public class Admin extends Staff{
    public Admin(String username, String password, LocalDate dateOfBirth, ROLE role, int workingHours){
        super(username, password, dateOfBirth, role, workingHours);
    }
    public Guest viewAllGuests(HotelDatabase dataBase){
        return dataBase.viewAllGuests();
    }
    public Room viewAllRooms(HotelDatabase dataBase){
        return dataBase.viewAllRooms();
    }
    public Reservation viewAllReservations(HotelDatabase dataBase){
        return dataBase.viewAllReservations();
    }
    //admin CRUD
    public void addRoomType(HotelDatabase dataBase, RoomType type){
        dataBase.addRoomType(type);
    }
    public RoomType viewAllRoomTypes(HotelDatabase dataBase){
        return dataBase.viewAllRoomTypes();
    }
    public void removeRoomType(HotelDatabase dataBase, RoomType type){
        dataBase.removeRoomType(type);
    }
    //Room
    public void addRoom(HotelDatabase dataBase,Room room){
        dataBase.addRoom(room);
    }
    public void removeRoom(HotelDatabase dataBase, Room room){
        dataBase.removeRoom(room);
    }
    //Amenity
    public Amenity viewAllAmenities(HotelDatabase dataBase){
        return dataBase.viewAllAmenities();
    }
    public void addAmenity(HotelDatabase dataBase, Amenity amenity){
        dataBase.addAmenity(amenity);
    }
    public void removeAmenity(HotelDatabase dataBase, Amenity amenity){
        dataBase.removeAmenity(amenity);
    }

}
