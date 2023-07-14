package rms.model;

public class SupplierModel {
    public final String name;

    public final String address;

    public final String phoneNum;

    public final String ownerName;

    public SupplierModel(String name, String address, String phoneNum, String ownerName) {
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.ownerName = ownerName;
    }

    public String getName(){return name;}

    public String getAddress(){return address;}

    public String getPhoneNum(){return phoneNum;}

    public String getOwnerName(){return ownerName;}
    // TODO
}
