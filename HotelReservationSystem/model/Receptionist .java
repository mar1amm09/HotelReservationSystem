package HotelReservationSystem.model;

import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.ROLE;
import HotelReservationSystem.enums.paymentMethod;

import java.time.LocalDate;
import java.util.List;

public class Receptionist extends Staff{
    public Receptionist(String username, String password, LocalDate dateOfBirth, ROLE role, int workingHours){
        super(username, password,dateOfBirth,role,workingHours);
    }
    public void manageCheckIn(HotelDatabase dataBase, Reservation r, LocalDate checkIn){
        dataBase.setCheckInDate(r,checkIn);
    }
    public void manageCheckOut(HotelDatabase dataBase, Reservation r, LocalDate checkOut){
        dataBase.setCheckOutDate(r,checkOut);
    }
    public void createInvoice(HotelDatabase dataBase, double total, List<paymentMethod> paymentMethods, LocalDate paymentDate){
        dataBase.createInvoice(total, paymentMethods,paymentDate);}
}
