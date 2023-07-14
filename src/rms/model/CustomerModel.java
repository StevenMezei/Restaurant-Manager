package rms.model;

public class CustomerModel {
    public final String name;

    public final int age;

    public final int orderID;

    public CustomerModel(String name, int age, int orderID) {
        this.name = name;
        this.age = age;
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getOrderID() {
        return orderID;
    }
}
