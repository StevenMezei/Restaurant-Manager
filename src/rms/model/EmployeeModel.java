package rms.model;

public class EmployeeModel {
    public final int employeeID;

    public final String name;

    public final double wageRate;

    public final String phoneNum;

    public final String emergencyContact;

    public final String ownerName;

    public EmployeeModel(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName) {
        this.employeeID = employeeID;
        this.name = name;
        this.wageRate = wageRate;
        this.phoneNum = phoneNum;
        this.emergencyContact = emergencyContact;
        this.ownerName = ownerName;
    }
    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public double getWageRate() {
        return wageRate;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getOwnerName() {
        return ownerName;
    }


}
