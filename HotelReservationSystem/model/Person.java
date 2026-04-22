package HotelReservationSystem.model;

import java.time.LocalDate;
import java.util.Date;

public class Person {
    protected String username;
    protected String password;
    protected LocalDate dateOfBirth;
    //constructor
    public Person (String username, String password, LocalDate dateOfBirth){
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }
    //getters
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public LocalDate getDate(){
        return dateOfBirth;
    }
    //setters
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }
}
