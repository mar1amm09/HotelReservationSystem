package HotelReservationSystem.model;

public class RoomType {
    private String name;
    private int maxCapacity;
    private double price;
    //constructor
    public RoomType(String name, int maxCapacity, double price){
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.price = price;
    }
    //getters
    public String getRoomTypeName(){
        return name;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public double getPrice() {
        return price;
    }
    //setters
    public void setPrice(double price){
        this.price = price;
    }
}
