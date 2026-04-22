package HotelReservationSystem.model;

import java.util.List;

public class Room {
    private int roomId;
    private RoomType roomType;
    private double roomPrice;
    private boolean available;
    private int roomFloor;
    private List<Amenity> roomAmenities;

    //constructor
    public Room(int roomId, RoomType roomType, double roomPrice, boolean available, int roomFloor, List <Amenity> roomAmenities){
        this.roomId = roomId;
        this. roomType = roomType;
        this. roomPrice = roomPrice;
        this.available = available;
        this.roomFloor = roomFloor;
        this.roomAmenities = roomAmenities;
    }
    //getters
    public int getRoomId(){
        return roomId;
    }
    public double getRoomPrice(){
        return roomPrice;
    }
    public boolean getAvailability(){
        return available;
    }
    public int getRoomFloor(){
        return roomFloor;
    }
    public List<Amenity> getRoomAmenities(){
        return roomAmenities;
    }
    // setters
    public void setRoomId(int roomId){
        this.roomId = roomId;
    }
    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }
    public void setRoomAvailability(boolean available){
        this.available = available;
    }
    public void setRoomFloor(int roomFloor){
        this.roomFloor = roomFloor;
    }
    public void setRoomAmenities(List<Amenity> roomAmenities){
        this.roomAmenities = roomAmenities;
    }
}
