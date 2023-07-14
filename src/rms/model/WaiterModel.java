package rms.model;
import rms.model.EmployeeModel;

public class WaiterModel extends EmployeeModel{

    public final String SIRcertificate;


    public WaiterModel(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName, String SIRcertificate) {
        super(employeeID, name, wageRate, phoneNum, emergencyContact, ownerName);
        this.SIRcertificate = SIRcertificate;
    }

    public String getSIRcertificate() {
        return SIRcertificate;
    }

}
