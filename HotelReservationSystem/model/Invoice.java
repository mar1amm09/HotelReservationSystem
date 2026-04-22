package HotelReservationSystem.model;

import HotelReservationSystem.enums.paymentMethod;
import HotelReservationSystem.enums.ReservationType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Invoice {
    private double totalAmount;
    protected List<paymentMethod> paymentMethods;
    private LocalDate paymentDate;
    private  ReservationType type;
    public Invoice(double totalAmount, List<paymentMethod> paymentMethods, LocalDate paymentDate,ReservationType type){
        this.paymentMethods = paymentMethods;
        this.paymentDate = paymentDate;
             this.type= type;
        if (type ==ReservationType.ALL_INCLUSIVE){
            this.totalAmount = totalAmount*1.15;}
        else if (type == ReservationType.FULL_BOARD){
            this.totalAmount = totalAmount*1.10;}
        else if (type == ReservationType.HALF_BOARD){
            this.totalAmount = totalAmount*1.05;}
        else {this.totalAmount = totalAmount;}
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
