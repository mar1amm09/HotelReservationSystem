package model;

import java.util.Date;

public class Reservation {
    private Guest guest;
    private Room room;
    private Date checkIn;
    private Date checkOut;
    public enum Status{
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
    private Status status;
    //constructor
    public Reservation(Guest guest, Room room, Date checkIn, Date checkOut, Status status){
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }
    //getters
    public Guest getGuest(){
        return guest;
    }
    public Room getRoom(){
        return room;
    }
    public Date getCheckIn(){
        return checkIn;
    }
    public Date getCheckOut(){
        return checkOut;
    }
    public Status getStatus(){
        return status;
    }
    //setter
    public void setStatus(Status status){
        this.status = status;
    }
    public void updateReservationStatus(Reservation r, Reservation.Status status) {
        r.setStatus(status);
    }
    public void setCheckIn(Date checkIn){
        this.checkIn = checkIn;
    }
    public void setCheckOut(Date checkOut){
        this.checkOut = checkOut;
    }
}
