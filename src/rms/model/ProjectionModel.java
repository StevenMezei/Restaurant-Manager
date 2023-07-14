package rms.model;

public class ProjectionModel{

    public final int employeeID;

    public final String name;

    public final String phoneNum;

    public ProjectionModel(int employeeID, String name, String phoneNum) {
        this.employeeID = employeeID;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }



}