package HotelReservationSystem.model;
import HotelReservationSystem.database.HotelDatabase;
import HotelReservationSystem.enums.Gender;

import java.time.LocalDate;

public class Guest extends Person{
    private double balance;
    private String address;
    private Gender gender;
    private String roomPreferences;
    //constructor
    public Guest(String username, String password, LocalDate dateOfBirth, double balance, String address, Gender gender, String roomPreferences){
        super(username, password, dateOfBirth);
        this.balance = balance;
        this.address= address;
        this.gender = gender;
        this.roomPreferences = roomPreferences;
    }
    //getters
    public double balance(){
        return balance;
    }
    public String getAddress(){
        return address;
    }
    public Gender getGender(){
        return gender;
    }
    public String getRoomPreferences(){
        return roomPreferences;
    }
    //setters
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setGender(Gender gender){
        this.gender = gender;
    }
    public void setRoomPreferences(String roomPreferences){
        this.roomPreferences = roomPreferences;
    }
    //methods
    public void register (HotelDatabase dataBase, Guest g){
        dataBase.register(g);
    }
    public boolean login (HotelDatabase dataBase, String username, String password)
    {
        dataBase.login(username, password);
        return true;
    }
    public void viewAvailableRooms(HotelDatabase dataBase){
        dataBase.viewAvailableRooms();
    }
    public void makeReservation(HotelDatabase dataBase, Reservation r){
        dataBase.makeReservation(r);
    }
    public void cancelReservation(HotelDatabase dataBase, Reservation r){
        dataBase.cancelReservation(r);
    }
    public void viewReservations(HotelDatabase dataBase,Guest g){
        dataBase.viewReservations(g);
    }
}
