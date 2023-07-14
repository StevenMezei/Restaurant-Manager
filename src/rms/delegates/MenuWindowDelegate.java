package rms.delegates;

public interface MenuWindowDelegate {
    void login(String username, String password);
    void updateEmployee(String employeeID, String wageRate);
    void deleteSupplier(String name);
    void insertMenuItemStrings(String name, String price, String prepTime, String ingredients);
    void viewFastItems(String prepTime);
    void ingredientFromSupplier(String name, String price);
    void viewEmployees();
    void projectEmployees();
    void nestedChefs();
    void storageLocation(String count);
    void menuItemsAllIngredients();
    void viewPaymentMethodsCount();
    void viewSuppliers();
}
