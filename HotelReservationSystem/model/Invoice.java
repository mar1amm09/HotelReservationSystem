package HotelReservationSystem.model;

import HotelReservationSystem.enums.paymentMethod;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Invoice {
    private double totalAmount;
    protected List<paymentMethod> paymentMethods;
    private LocalDate paymentDate;
    public Invoice(double totalAmount, List<paymentMethod> paymentMethods, LocalDate paymentDate){
        this.totalAmount = totalAmount;
        this.paymentMethods = paymentMethods;
        this.paymentDate = paymentDate;
    }
    //getters
    public double getTotalAmount(){
        return totalAmount;
    }
    public List<paymentMethod> getPaymentMethods(){
        return paymentMethods;
    }
    public LocalDate getPaymentDate(){
        return paymentDate;
    }
}
