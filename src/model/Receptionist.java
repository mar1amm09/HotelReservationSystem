package model;

import database.HotelDatabase;
import enums.ROLE;
import enums.paymentMethod;

import java.util.Date;
import java.util.List;

public class Receptionist extends Staff{
    public Receptionist(String username, String password, Date dateOfBirth, ROLE role, int workingHours){
        super(username, password,dateOfBirth,role,workingHours);
    }
    public void manageCheckIn(HotelDatabase dataBase, Reservation r, Date checkIn){
        dataBase.setCheckInDate(r,checkIn);
    }
    public void manageCheckOut(HotelDatabase dataBase, Reservation r, Date checkOut){
        dataBase.setCheckOutDate(r,checkOut);
    }
    public void createInvoice(HotelDatabase dataBase, Reservation r, List<paymentMethod> paymentMethods, Date paymentDate){
        dataBase.createInvoice(r, paymentMethods,paymentDate);
}
