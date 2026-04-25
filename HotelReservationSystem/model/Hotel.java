package HotelReservationSystem.model;
import java.time.LocalDate;
import HotelReservationSystem.exceptions.*;
public abstract class Hotel{
    public static int validateIntegerInput (String input)throws InvalidInputException{
        if (input == null||input.isEmpty()){
            throw new InvalidInputException("Input cannot be empty or null.");
        }
        for (char c : input.toCharArray()){
            if (!Character.isDigit(c)) {
                throw new InvalidInputException("Input should contain only digits.");
            }
        }
        return Integer.parseInt(input);
    }
    public static void areDatesValid  (LocalDate checkIn, LocalDate checkOut)throws InvalidDateException{
        if (checkIn == null || checkOut == null) {
            throw new InvalidDateException("Date cannot be null");
        }
        if (checkIn.isAfter(checkOut)){
            throw new InvalidDateException("Check in date cannot be after check out date.");
        }
    }
    public static void isGuestValid (Guest guest)throws InvalidInputException{
        if (guest == null){
            throw new InvalidInputException("Guest cannot be null");
        }
    }
    public static boolean isRoomAvailable (Room room)throws RoomNotAvailableException{
        if (room == null){
            throw new RoomNotAvailableException("Room selected does not exist");
        }
        return room.getAvailability();
    }
    public static void validatePayment (double amount, double expectedAmount)throws InvalidPaymentException{
        if (amount <= 0){
            throw new InvalidPaymentException("Payment amount must be greater than zero. ");
        }
        if (amount < expectedAmount){
            throw new InvalidPaymentException("Insufficient Payment, expected to pay: " + expectedAmount + " Received: " + amount) ;
        }
    }
}
