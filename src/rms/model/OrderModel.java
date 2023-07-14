package rms.model;

public class OrderModel {

    public final int orderID;

    public final String paymentForm;

    public OrderModel(int orderID, String paymentForm) {
        this.orderID = orderID;
        this.paymentForm = paymentForm;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getPaymentForm() {
        return paymentForm;
    }
}
