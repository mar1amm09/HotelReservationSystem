package model;

import enums.paymentMethod;

import java.util.Date;
import java.util.List;

public class Invoice {
    private double totalAmount;
    protected List<paymentMethod> paymentMethods;
    private Date paymentDate;
    public Invoice(double totalAmount, List<paymentMethod> paymentMethods, Date paymentDate){
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
    public Date getPaymentDate(){
        return paymentDate;
    }
}
