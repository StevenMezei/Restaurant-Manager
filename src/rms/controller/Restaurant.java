package rms.controller;

import rms.database.DatabaseConnectionHandler;
import rms.database.DatabasePopulate;
import rms.delegates.MenuWindowDelegate;
import rms.exceptions.ConnectionFailureException;
import rms.exceptions.InvalidInputException;
import rms.model.*;

import rms.ui.ErrorWindow;
import rms.ui.MenuWindow;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.function.Supplier;

public class Restaurant implements MenuWindowDelegate {
    // set to true to clean and repopulate tables
    private final boolean DATABASE_RESET = false;

    private DatabaseConnectionHandler dbHandler = null;
    private ErrorWindow errorWindow;
    private MenuWindow menuWindow = null;

    public Restaurant() { dbHandler = new DatabaseConnectionHandler(); }

    private void start() throws ConnectionFailureException {

        // Attempt login
        if (!dbHandler.login("ora_jackt32", "a38017240")) {
            throw new ConnectionFailureException();
        }

        // Clean and repopulate tables
        if (DATABASE_RESET) {
            dbHandler.databaseSetup();
            new DatabasePopulate(this);
        }


        menuWindow = new MenuWindow();
        menuWindow.showFrame(this);
    }

    public void insertMenuItem(String name, double price, int prepTime) throws InvalidInputException {
        MenuItemModel menuItem = new MenuItemModel(name, price, prepTime);
        dbHandler.insertMenuItem(menuItem);
    }

    private void updateMenuItem(String name, double price) {
        dbHandler.updateMenuItemPrice(price, name);
    }

    public void insertOwner(String username, String name, String pw) {
        dbHandler.insertOwner(new OwnerModel(username, name, pw));
    }

    private void printMenuItemInfo() {
        MenuItemModel[] menuItems;
        menuItems = dbHandler.getMenuItem();
        for (MenuItemModel mi : menuItems) {
            System.out.println(mi.toString());
        }
    }


    public void insertChef(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName, String seniority){
        dbHandler.insertChef(new ChefModel(employeeID,name, wageRate, phoneNum, emergencyContact, ownerName, seniority));
    }

    public void insertWaiter(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName, String SIRcertificate){
        dbHandler.insertWaiter(new WaiterModel(employeeID,name,wageRate,phoneNum,emergencyContact,ownerName,SIRcertificate));
    }


    public void insertEmployee(int employeeID, String name, double wageRate, String phoneNum, String emergencyContact, String ownerName){
        dbHandler.insertEmployee(new EmployeeModel(employeeID,name,wageRate,phoneNum,emergencyContact,ownerName));
    }

    private void deleteEmployee(int employeeID){
        dbHandler.deleteEmployee(employeeID);
    }

    public void insertShift(int shiftID, int breakTime, String startTime, String endTime, int duration){
        dbHandler.insertShift(new ShiftModel(shiftID,breakTime,startTime,endTime,duration));
    }

    private void deleteShift(int shiftID){
        dbHandler.deleteShift(shiftID);
    }

    public void insertTable(int tableNum, int seats, int indoorOutdoor, int waiterID){
        dbHandler.insertTable(new TableModel(tableNum,seats,indoorOutdoor,waiterID));
    }

    private void deleteTable(int tableNum){
        dbHandler.deleteTable(tableNum);
    }

    public void insertOrder(int orderID, String paymentForm){
        dbHandler.insertOrder(new OrderModel(orderID,paymentForm));
    }

    private void deleteOrder(int orderID) {
        dbHandler.deleteOrder(orderID);
    }

    public void insertCustomer(String name, int age, int orderID){
        dbHandler.insertCustomer(new CustomerModel(name, age, orderID));
    }

    private void deleteCustomer(String name, int orderID){
        dbHandler.deleteCustomer(name, orderID);
    }

    // should put supplier on top of ingredient
    public void insertIngredient(String name, int shelfLife, String storageLocation, String supplierName, double price, int orderQuantity, String foodType){
        dbHandler.insertIngredient(new IngredientModel(name, shelfLife, storageLocation, supplierName, price, orderQuantity, foodType));
    }

    private void deleteIngredient(String name){
        dbHandler.deleteIngredient(name);
    }

    public void insertSupplier(String name, String address, String phoneNum, String ownerName){
        dbHandler.insertSupplier(new SupplierModel(name, address, phoneNum, ownerName));
    }

    public void insertMadeWith(String ingredient, String menuItem){
        dbHandler.insertMadeWith(ingredient, menuItem);
    }

    public void insertOrderandMenu (String menuItem, int orderID){
        dbHandler.insertOrderandMenu(menuItem,orderID);
    }

    public void insertWorks (int employeeID, int shiftID){
        dbHandler.insertWorks(employeeID,shiftID);
    }

    public void insertCooks (String name, int employeeID){
        dbHandler.insertCooks(name, employeeID);
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        try {
            restaurant.start();
        } catch (ConnectionFailureException e) {
            restaurant.displayError("Connection to Oracle failed");
        }
    }

    public void deleteSupplier(String name) {
        if (dbHandler.doesSupplierExist(name)) {
            dbHandler.deleteSupplier(name);
            menuWindow.handleGoodDelete();
        } else {
            displayError("Supplier does not exist");
        }
    }
    public void ingredientFromSupplier(String name, String str_price){
        try{
            if(!Double.isNaN(Double.parseDouble(str_price))){
                double price = Double.parseDouble(str_price);
                menuWindow.handleGoodIngredientFromSupp();
                DefaultTableModel dtm = dbHandler.ingredientFromSupplier(name, price);
                menuWindow.displayTable2(dtm);
            }
            else{
                displayError("Price is not a number");
            }
        } catch(NumberFormatException e) {
            displayError("Price is not in correct format");
        }

    }

    public void updateEmployee(String employeeID, String wageRate){

        try{
            int intEmployeeID = Integer.parseInt(employeeID);
            double doubleWR = Double.parseDouble(wageRate);
            dbHandler.updateEmployee(intEmployeeID,doubleWR);
            menuWindow.handleGoodUpdate();
        }
        catch (NumberFormatException e){
            displayError("EmployeeID or Wage Rate is not in correct format");

        }
        catch (InvalidInputException e){
            displayError("Employee does not exist");

        }


    }

    public void storageLocation(String count) {
        try {
            int count_i = Integer.parseInt(count);
            menuWindow.handleGoodStorageLocation();
            DefaultTableModel dtm = dbHandler.storageLocation(count_i);
            menuWindow.displayTable2(dtm);
        } catch (NumberFormatException e) {
            displayError("Please enter correct number format");
        }
    }

    public void menuItemsAllIngredients(){
        menuWindow.handleGoodAllIngredients();
        DefaultTableModel dtm = dbHandler.itemsAllIngredients();
        menuWindow.displayTable2(dtm);

    }

    @Override
    public void viewPaymentMethodsCount() {
        DefaultTableModel dtm = dbHandler.getPaymentMethodsCount();
        menuWindow.displayTable2(dtm);
    }

    @Override
    public void viewSuppliers() {
        SupplierModel[] suppliers = dbHandler.getSuppliers();
        menuWindow.displayTable(suppliers);
    }

    @Override
    public void insertMenuItemStrings(String name, String price, String prepTime, String ingredients) {
        try {
            double doublePrice = Double.parseDouble(price);
            int intPrepTime = Integer.parseInt(prepTime);
            dbHandler.insertMenuItem(new MenuItemModel(name, doublePrice, intPrepTime));
            menuWindow.handleGoodInsert();
            // TODO parse ingredients and add them to madewith table

        } catch (InvalidInputException e) {
            displayError("Item with that name already exists!");
        } catch (NumberFormatException e) {
            displayError("Please enter correct number formats");
        }
    }

    @Override
    public void viewFastItems(String prepTime) {
        try {
            int intPrepTime = Integer.parseInt(prepTime);
            menuWindow.handleGoodViewFastItems();
            MenuItemModel[] fastItems = dbHandler.viewFastItems(intPrepTime);
            menuWindow.displayTable(fastItems);
        } catch (NumberFormatException e) {
            displayError("Please enter a number");
        }
    }

    @Override
    public void viewEmployees() {
        EmployeeModel[] employees = dbHandler.getEmployees();
        menuWindow.displayTable(employees);
    }

    public void projectEmployees(){
        ProjectionModel[] employees = dbHandler.projectEmployees();
        menuWindow.displayTable(employees);
    }

    public void nestedChefs(){
        DefaultTableModel chefs = dbHandler.nestedChef();
        menuWindow.displayTable2(chefs);
    }

    // Creates an error popup with given text
    public void displayError(String text) {
        new ErrorWindow(text);
    }

    @Override
    public void login(String username, String password) {
        try {
            OwnerModel res = dbHandler.getOwner(username);
            if (res.getAccountName().equals(username) && res.getPassword().equals(password)) {
                menuWindow.handleGoodLogin();
            } else {
                displayError("Bad password");
            }
        } catch (InvalidInputException e) {
            displayError("Invalid username");
        }

    }
}
