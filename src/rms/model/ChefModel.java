package rms.model;
import rms.model.EmployeeModel;


public class ChefModel extends EmployeeModel{
    public final String seniority;

    public ChefModel(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName, String seniority) {
        super(employeeID, name, wageRate, phoneNum, emergencyContact, ownerName);
        this.seniority = seniority;
    }

    public String getSeniority() {
        return seniority;
    }
}
