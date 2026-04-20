package HotelReservationSystem.model;
import java.time.LocalDate;
public class Hotel{
    public static int validateIntegerInput(String input){
        if (input == null||input.isempty()){
            return -1;
        }
        for (char c : input.toCharArray()){
            if (!Character.isDigit(c)) {
                return -1;
            }
        }
    }
    public static boolean areDatesValid(LocalDate checkIn, LocalDate checkOut){
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return !checkIn.isAfter(checkOut);
    }
    public static boolean isGuestValid(Guest guest){
        return guest != null;
    }
    public static boolean isRoomAvailable(Room room){
        if (room == null){
            return false;
        }
        return room.getAvailability();
    }
    public static boolean validatePayment(double amount, double expectedAmount){
        if (amount <= 0){
            System.out.println ("Payment amount must be greater than zero. ");
            return false;
        }
        if (amount < expectedAmount){
            System.out.println ("Insufficient Payment, expected to pay: " + expectedAmount + " Received: " + amount) ;
            return false;
        }
        System.out.println("Payment Successful");
        return true;
    }
}