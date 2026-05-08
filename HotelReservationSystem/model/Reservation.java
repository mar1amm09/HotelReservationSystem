package HotelReservationSystem.model;

import HotelReservationSystem.enums.ReservationType;
import java.time.LocalDate;

public class Reservation {
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationType type;
    public enum Status{
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
    private Status status;
    //constructor 
    public Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, Status status,ReservationType type){
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.type = type;
    }
    //getters
    public Guest getGuest(){
        return guest;
    }
    public Room getRoom(){
        return room;
    }
    public LocalDate getCheckIn(){
        return checkIn;
    }
    public LocalDate getCheckOut(){
        return checkOut;
    }
    public Status getStatus(){
        return status;
    }
    public ReservationType getType() {
        return type;
    }
    //setter
    public void setStatus(Status status){
        this.status = status;
    }
    public void updateReservationStatus(Reservation r, Reservation.Status status) {
        r.setStatus(status);
    }
    public void setCheckIn(LocalDate checkIn){
        this.checkIn = checkIn;
    }
    public void setCheckOut(LocalDate checkOut){
        this.checkOut = checkOut;
    }
        public void setType(ReservationType type) {
        this.type = type;
    }
}
