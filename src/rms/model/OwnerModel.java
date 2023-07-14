package rms.model;

public class OwnerModel {
    public final String accountName;
    public final String name;
    public final String password;

    public OwnerModel(String accountName, String name, String password) {
        this.accountName = accountName;
        this.name = name;
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
