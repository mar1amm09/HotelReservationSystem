package HotelReservationSystem.model;
import HotelReservationSystem.enums.ReservationType;
import HotelReservationSystem.enums.paymentMethod;
import java.util.Date;
import.java.util.List;
public class Invoice {
    private double totalAmount;
    protected List<paymentMethod>
    paymentMethods;
    private Date paymentDate;
    public Invoice(double totalAmount,<List<paymentMethod>paymentMethos,Date paymentDate,ReservationType type)
    {
        this.totalAmount+totalAmount;
        this.paymentMethods=paymentMethods;
        this.paymentDate=paymentDate;
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
    
    public List<paymentMethod getPaymentMethods(){
        return PaymentMethods;
    }
    public Date getPaymentDate(){
        return PaymentDate;

    }
}
