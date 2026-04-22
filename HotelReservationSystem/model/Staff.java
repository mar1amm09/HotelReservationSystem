package HotelReservationSystem.model;

import HotelReservationSystem.enums.ROLE;

import java.time.LocalDate;

public abstract class Staff extends Person{
    protected ROLE role;
    protected int workingHours;
    //constructor
    public Staff(String username, String password, LocalDate dateOfBirth, ROLE role, int workingHours){
        super(username, password, dateOfBirth);
        this.role = role;
        this.workingHours = workingHours;
    }
    //getters
    public ROLE getRole(){
        return role;
    }
    public int getWorkingHours(){
        return workingHours;
    }
    //setters
    public void setWorkingHours(int workingHours){
        this.workingHours = workingHours;
    }
}
