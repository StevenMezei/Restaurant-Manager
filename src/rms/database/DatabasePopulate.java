package rms.database;

import rms.controller.Restaurant;
import rms.exceptions.InvalidInputException;

public class DatabasePopulate {
    private final Restaurant restaurant;

    public DatabasePopulate(Restaurant restaurant) {
        this.restaurant = restaurant;
        start();
    }

    private void start() {
        insertOwners();
        insertSuppliers();
        insertIngredients();
        insertMenuItems();
        insertOrders();
        insertCustomers();
        insertShifts();
        insertEmployees();
        insertChefs();
        insertWaiters();
        insertTables();
        insertMadeWith();
        insertOrderandMenu();
        insertWorks();
        insertCooks();
    }

    // TODO
    private void insertOwners() {
        restaurant.insertOwner("admin", "admin", "admin");
        restaurant.insertOwner("owner1", "james", "abcdef");
        restaurant.insertOwner("owner2", "john", "password");
        restaurant.insertOwner("owner3", "jack", "123");
        restaurant.insertOwner("owner4", "jules", "password123");
        restaurant.insertOwner("owner5", "jill", "mybirthday");
    }

    private void insertSuppliers() {
        restaurant.insertSupplier("supplier1", "111 a street", "111111", "owner1");
        restaurant.insertSupplier("supplier2", "222 b street", "222222", "owner1");
        restaurant.insertSupplier("supplier3", "333 c street", "333333", "owner2");
        restaurant.insertSupplier("supplier4", "444 d street", "444444", "owner4");
        restaurant.insertSupplier("supplier5", "555 e street", "555555", "owner4");
    }

    private void insertIngredients() {
        restaurant.insertIngredient("beef", 25, "freezer", "supplier1", 20.5, 10, "meat");
        restaurant.insertIngredient("cheese", 30, "fridge", "supplier2", 10, 5, "dairy");
        restaurant.insertIngredient("ravioli", 50, "larder", "supplier3", 3.5, 15, "pasta");
        restaurant.insertIngredient("pork", 25, "freezer", "supplier1", 8.9, 10, "meat");
        restaurant.insertIngredient("tomato", 5, "fridge", "supplier2", 4.99, 20, "fruit");
        restaurant.insertIngredient("ketchup", 40, "larder", "supplier2", 5, 8, "condiment");
    }

    private void insertMenuItems() {
        try {
            restaurant.insertMenuItem("menuitem1", 10.99, 10);
            restaurant.insertMenuItem("menuitem2", 14.99, 20);
            restaurant.insertMenuItem("menuitem3", 18.99, 18);
            restaurant.insertMenuItem("menuitem4", 4.99, 4);
            restaurant.insertMenuItem("menuitem5", 8.99, 8);
        } catch (InvalidInputException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void insertOrders() {
        restaurant.insertOrder(1, "visa");
        restaurant.insertOrder(2, "cash");
        restaurant.insertOrder(3, "debit");
        restaurant.insertOrder(4, "visa");
        restaurant.insertOrder(5, "visa");
    }

    private void insertCustomers() {
        restaurant.insertCustomer("Manny", 19, 1);
        restaurant.insertCustomer("Matthew", 20, 1);
        restaurant.insertCustomer("Muncy", 35, 2);
        restaurant.insertCustomer("Morbius", 87, 3);
        restaurant.insertCustomer("Matilda", 38, 4);
        restaurant.insertCustomer("Maxim", 5, 5);
    }

    private void insertShifts() {
        restaurant.insertShift(1, 15, "6:00", "8:00", 120);
        restaurant.insertShift(2, 15, "8:00", "11:00", 180);
        restaurant.insertShift(3, 30, "10:00", "14:00", 240);
        restaurant.insertShift(4, 30, "14:00", "20:00", 300);
        restaurant.insertShift(5, 15, "9:00", "15:00", 360);
    }

    private void insertEmployees() {
        restaurant.insertEmployee(1, "Sally", 16.50, "778-111-2403", "778-111-9804", "james");
        restaurant.insertEmployee(2, "Tim", 17.50, "778-222-2403", "778-222-9804", "john");
        restaurant.insertEmployee(3, "Tom", 16.50, "778-333-2403", "778-333-9804", "jack");
        restaurant.insertEmployee(4, "Ally", 17.50, "778-444-2403", "778-444-9804", "john");
        restaurant.insertEmployee(5, "Jim", 16.50, "778-555-2403", "778-555-9804", "jack");

        // Chefs
        restaurant.insertEmployee(6, "Brenda", 20.50, "778-666-2403", "778-666-9804", "jules");
        restaurant.insertEmployee(7, "Sonya", 19.50, "778-777-2403", "778-777-9804", "jill");
        restaurant.insertEmployee(8, "David", 19.50, "778-888-2403", "778-888-9804", "jules");
        restaurant.insertEmployee(9, "Mia", 18.50, "778-999-2403", "778-999-9804", "jill");
        restaurant.insertEmployee(10, "Adam", 18.50, "778-000-2403", "778-000-9804", "jill");

        // Waiters
        restaurant.insertEmployee(11, "Ella", 16.50, "778-666-5913", "778-666-9804", "john");
        restaurant.insertEmployee(12, "Belle", 16.50, "778-777-5913", "778-777-9804", "jill");
        restaurant.insertEmployee(13, "Tiana", 16.50, "778-888-5913", "778-888-9804", "jules");
        restaurant.insertEmployee(14, "Ariel", 16.50, "778-999-5913", "778-999-9804", "james");
        restaurant.insertEmployee(15, "Aurora", 16.50, "778-000-5913", "778-000-9804", "jack");
    }


    private void insertChefs(){
        restaurant.insertChef(6, "Brenda", 20.50, "778-666-2403", "778-666-9804", "jules", "Main chef");
        restaurant.insertChef(7, "Sonya", 19.50, "778-777-2403", "778-777-9804", "jill", "Main chef");
        restaurant.insertChef(8, "David", 19.50, "778-888-2403", "778-888-9804", "jules", "Sous chef");
        restaurant.insertChef(9, "Mia", 18.50, "778-999-2403", "778-999-9804", "jill", "Sous chef");
        restaurant.insertChef(10, "Adam", 18.50, "778-000-2403", "778-000-9804", "jill", "Line cook");
    }

    private void insertWaiters(){
        restaurant.insertWaiter(11, "Ella", 16.50, "778-666-2403", "778-666-9804", "john", "Yes");
        restaurant.insertWaiter(12, "Belle", 16.50, "778-777-2403", "778-777-9804", "jill", "Yes");
        restaurant.insertWaiter(13, "Tiana", 16.50, "778-888-2403", "778-888-9804", "jules", "Yes");
        restaurant.insertWaiter(14, "Ariel", 16.50, "778-999-2403", "778-999-9804", "james", "No");
        restaurant.insertWaiter(15, "Aurora", 16.50, "778-000-2403", "778-000-9804", "jack", "No");
    }

    private void insertTables(){
        restaurant.insertTable(1,2,0,11);
        restaurant.insertTable(2,2,0,12);
        restaurant.insertTable(3,4,0,13);
        restaurant.insertTable(4,4,1,14);
        restaurant.insertTable(5,8,1,15);
    }

    private void insertMadeWith(){
        restaurant.insertMadeWith("beef", "menuitem1");
        restaurant.insertMadeWith("tomato", "menuitem1");
        restaurant.insertMadeWith("cheese", "menuitem2");
        restaurant.insertMadeWith("pork", "menuitem2");
        restaurant.insertMadeWith("ravioli", "menuitem3");

        restaurant.insertMadeWith("cheese", "menuitem1");
        restaurant.insertMadeWith("ravioli", "menuitem1");
        restaurant.insertMadeWith("pork", "menuitem1");
        restaurant.insertMadeWith("ketchup", "menuitem1");
    }


    private void insertOrderandMenu(){
        restaurant.insertOrderandMenu("menuitem1",1);
        restaurant.insertOrderandMenu("menuitem2",2);
        restaurant.insertOrderandMenu("menuitem3",3);
        restaurant.insertOrderandMenu("menuitem4",4);
        restaurant.insertOrderandMenu("menuitem5",5);
    }

    private void insertWorks(){
        restaurant.insertWorks(6,2);
        restaurant.insertWorks(7,2);
        restaurant.insertWorks(8,2);
        restaurant.insertWorks(9,2);
        restaurant.insertWorks(10,2);
    }

    private void insertCooks(){
        restaurant.insertCooks("menuitem1", 6);
        restaurant.insertCooks("menuitem2", 7);
        restaurant.insertCooks("menuitem3", 8);
        restaurant.insertCooks("menuitem4", 9);
        restaurant.insertCooks("menuitem5", 10);
    }



}
