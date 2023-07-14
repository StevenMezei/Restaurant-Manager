package rms.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import rms.exceptions.InvalidInputException;
import rms.model.*;
import rms.util.PrintablePreparedStatement;

import javax.swing.table.DefaultTableModel;

// Code adapted from https://github.students.cs.ubc.ca/CPSC304/CPSC304_Java_Project

public class DatabaseConnectionHandler {
    // REMEMBER TO TUNNEL WITH
    // ssh -l username -L localhost:1522:dbhost.students.cs.ubc.ca:1522 remote.students.cs.ubc.ca

    // Use this version of the ORACLE_URL if you are running the code off of the server
    //	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // login to Oracle
    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // clean and create all tables
    public void databaseSetup() {
        dropAllTablesIfExists();
        createTables();
    }

    // Create tables for the database respecting order for foreign keys
    private void createTables() {
        createMenuItemTable();
        createOwner2Table();
        createOwner1Table();
        createSupplierTable();
        createIngredient2Table();
        createIngredient1Table();
        createOrderTable();
        createCustomerTable();
        createShift1Table();
        createShift2Table();
        createShift3Table();
        createEmployeeTable();
        createChefTable();
        createWaiterTable();
        createTableTable();
        createMadeWithTable();
        createOrderAndMenuTable();
        createWorksTable();
        createCooksTable();
    }

    // Drops all tables in current user, disregarding foreign key constraints
    private void dropAllTablesIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                dropTable(rs.getString(1));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // drops a table using its name
    private void dropTable(String tableName) {
        try {
            String query = "DROP TABLE " + tableName + " CASCADE CONSTRAINTS";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    /* SQL QUERIES */

    // OWNER QUERIES

    // insert a new owner into owner1 and owner2 tables
    public void insertOwner(OwnerModel model) {
        insertOwner2(model);
        insertOwner1(model);
    }

    // helper method for inserting new owner
    private void insertOwner1(OwnerModel model) {
        try {
            String query = "INSERT INTO Owner1 VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getAccountName());
            ps.setString(2, model.getName());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // helper method for inserting new owner
    private void insertOwner2(OwnerModel model) {
        try {
            String query = "INSERT INTO Owner2 VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getName());
            ps.setString(2, model.getPassword());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // delete owner using accountName
    public void deleteOwner(String accountName) throws InvalidInputException {
        OwnerModel model = getOwner(accountName);
        deleteFromOwner2(model);
        deleteFromOwner1(model);
    }

    // returns an Owner from accountName
    public OwnerModel getOwner(String accountName) throws InvalidInputException{
        String accName = accountName;
        String name = "";
        String pw = "";

        try {
            String query = "SELECT * FROM Owner1 WHERE owner1_accountName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int res = rs.getRow();

            if(res == 0){
                throw new InvalidInputException();
            }

            if (rs.getString(1).equals(accountName)) {
                pw = getPasswordFromName(rs.getString(2));
            } else {
                throw new InvalidInputException();
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return new OwnerModel(accName, name, pw);
    }


    // helper method to delete owner from owner2 table
    private void deleteFromOwner2(OwnerModel model) {
        try {
            String query = "DELETE FROM Owner2 WHERE owner2_name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // helper method to delete owner from owner1 table
    private void deleteFromOwner1(OwnerModel model) {
        try {
            String query = "DELETE FROM Owner1 WHERE owner1_accountName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getAccountName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // MENU ITEM QUERIES

    public void insertMenuItem(MenuItemModel model) throws InvalidInputException {
        try {
            String query = "INSERT INTO MenuItem VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getItemName());
            ps.setDouble(2, model.getPrice());
            ps.setInt(3, model.getPreparationTime());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            throw new InvalidInputException();
        }
    }


    // update the price of a menu item
    public void updateMenuItemPrice(double price, String name) {
        try {
            String query = "UPDATE MenuItem SET menuitem_price = ? WHERE menuitem_name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDouble(1, price);
            ps.setString(2, name);
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    // returns all menu items (SELECT * FROM) into an array
    public MenuItemModel[] getMenuItem() {
        ArrayList<MenuItemModel> result = new ArrayList<>();

        try {
            String query = "SELECT * FROM MenuItem";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                MenuItemModel model = new MenuItemModel(rs.getString("menuitem_name"),
                        rs.getDouble("menuitem_price"),
                        rs.getInt("menuitem_preparationTime"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new MenuItemModel[result.size()]);
    }


    // returns matching password from given name if not found then null
    public String getPasswordFromName(String name) {
        try {
            String res;
            String query = "SELECT owner2_password FROM Owner2 WHERE owner2_name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getString(1);
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    /* --Employee-- */

    // insert employee
    public void insertEmployee(EmployeeModel model) {
        try {
            String query = "INSERT INTO Employee VALUES (?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getEmployeeID());
            ps.setString(2,model.getName());
            ps.setDouble(3,model.getWageRate());
            ps.setString(4,model.getPhoneNum());
            ps.setString(5,model.getEmergencyContact());
            ps.setString(6,model.getOwnerName());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    //delete Employee via employeeID
    public void deleteEmployee(int employeeID){

        //check if they are in chef or waiter to also delete
        checkChef(employeeID);
        checkWaiter(employeeID);

        try{
            String query = "DELETE FROM Employee WHERE employeeID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, employeeID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    // update Employees

    public void updateEmployee(int employeeID, double wageRate) throws InvalidInputException{
        try {
            String query = "UPDATE Employee SET wageRate = ? WHERE employeeID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setDouble(1, wageRate);
            ps.setInt(2, employeeID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public SupplierModel[] getSuppliers() {
        ArrayList<SupplierModel> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM Supplier";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                SupplierModel model = new SupplierModel(rs.getString("supplier_name"),
                        rs.getString("supplier_address"),
                        rs.getString("supplier_phone#"),
                        rs.getString("supplier_ownerName"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new SupplierModel[result.size()]);
    }

    // get all Employees
    public EmployeeModel[] getEmployees() {
        ArrayList<EmployeeModel> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM Employee";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                EmployeeModel model = new EmployeeModel(rs.getInt("employeeID"),
                        rs.getString("name"),
                        rs.getDouble("wageRate"),
                        rs.getString("phoneNum"),
                        rs.getString("emergencyContact"),
                        rs.getString("ownerName"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new EmployeeModel[result.size()]);
    }

    // PROJECTION: quick overview of the employees, gives ID, name, and phoneNum
    public ProjectionModel[] projectEmployees() {
        ArrayList<ProjectionModel> result = new ArrayList<>();
        try {
            String query = "SELECT employeeID, name, phoneNum FROM Employee";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ProjectionModel model = new ProjectionModel(rs.getInt("employeeID"),
                        rs.getString("name"),
                        rs.getString("phoneNum"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result.toArray(new ProjectionModel[result.size()]);
    }

    //helper function to check if they are in chef or waiter to also delete
    public void checkChef(int employeeID){
        boolean found  = false;

        try {
            String query = "SELECT employeeID FROM Chef";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                if (rs.getInt(1) == employeeID); {
                    deleteChef(employeeID);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                return;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        
    }

    public void checkWaiter(int employeeID){
        boolean found  = false;

        try {
            String query = "SELECT employeeID FROM Waiter";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == employeeID); {
                    deleteWaiter(employeeID);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

    }

    /* --Chef-- */

    // insert Chef
    public void insertChef(ChefModel model) {
        try {
            String query = "INSERT INTO Chef VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getEmployeeID());
            ps.setString(2,model.getSeniority());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteChef(int employeeID){
        try{
            String query = "DELETE FROM Chef WHERE employeeID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, employeeID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Find average wage of all Chefs who work at 8:00
    public DefaultTableModel nestedChef (){

        try{
            String query = "SELECT c.seniority, avg (e.wageRate) FROM Chef c, Employee e " +
                    "WHERE c.employeeID = e.employeeID AND " +
                    "c.employeeID IN (SELECT w.employeeID FROM Works w, Shift3 s " +
                    "WHERE  w.shiftID = s.shiftID AND s.startTime = '8:00') " +
                    "GROUP BY c.seniority";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            return buildTableModel(rs);
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return null;
    }

    /* --Waiter-- */

    // insert Waiter
    public void insertWaiter(WaiterModel model) {
        try {
            String query = "INSERT INTO Waiter VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getEmployeeID());
            ps.setString(2,model.getSIRcertificate());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteWaiter(int employeeID){
        try{
            String query = "DELETE FROM Waiter WHERE employeeID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, employeeID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    /* --Shift-- */

    //insert shift
    public void insertShift(ShiftModel model) {
        insertShift1(model);
        insertShift2(model);
        insertShift3(model);
    }

    //insert for shift1
    public void insertShift1(ShiftModel model) {
        try {
            String query = "INSERT INTO Shift1 VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1,model.getDuration());
            ps.setInt(2,model.getBreakTime());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    //insert for shift2
    public void insertShift2(ShiftModel model) {
        try {
            String query = "INSERT INTO Shift2 VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getStartTime());
            ps.setString(2,model.getEndTime());
            ps.setInt(3,model.getDuration());


            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    //insert for shift3
    public void insertShift3(ShiftModel model) {
        try {
            String query = "INSERT INTO Shift3 VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getShiftID());
            ps.setString(2, model.getStartTime());
            ps.setString(3,model.getEndTime());


            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // delete shift based on shiftID
    public void deleteShift(int shiftID){
        String startTime = findStartTime(shiftID);
        String endTime = findEndTime(shiftID);
        int duration = findDuration(startTime,endTime);

        deleteShift1(duration);
        deleteShift2(startTime,endTime);
        deleteShift3(shiftID);
    }

    //helper functions for finding keys of shift1 and shift2

    // returns null if not found
    public String findStartTime(int shiftID){
        try {
            String res;
            String query = "SELECT startTime FROM Shift3 WHERE shiftID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, shiftID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getString(1);
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // returns null if not found
    public String findEndTime(int shiftID){
        try {
            String res;
            String query = "SELECT endTime FROM Shift3 WHERE shiftID = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, shiftID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getString(1);
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // returns -1 if not found
    public int findDuration (String startTime, String endTime){
        try {
            int res;
            String query = "SELECT duration FROM Shift2 WHERE startTime = ? AND endTime = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, startTime);
            ps.setString(2,endTime);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getInt(1);
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return -1;
    }


    public void deleteShift1(int duration){
        try{
            String query = "DELETE FROM Shift1 WHERE duration = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, duration);

            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteShift2(String startTime, String endTime){
        try{
            String query = "DELETE FROM Shift2 WHERE startTime = ? AND endTime = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, startTime);
            ps.setString(2,endTime);

            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }


    public void deleteShift3(int shiftID){
        try{
            String query = "DELETE FROM Shift3 WHERE shiftID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, shiftID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    /* --Table-- */


    // insert table
    public void insertTable(TableModel model) {
        try {
            String query = "INSERT INTO Tables VALUES (?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getTableNum());
            ps.setInt(2,model.getSeats());
            ps.setInt(3,model.isIndoorOutdoor());
            ps.setInt(4,model.getWaiterID());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // delete table from table num, if table num not found, error message
    public void deleteTable (int tableNum){
        try{
            String query = "DELETE FROM Tables WHERE tableNum = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, tableNum);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }


    public void insertCustomer(CustomerModel model) {
        try {
            String query = "INSERT INTO Customer VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getName());
            ps.setInt(2, model.getAge());
            ps.setInt(3, model.getOrderID());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteCustomer(String name, int orderID) {
        try{
            String query = "DELETE FROM Customer WHERE name = ? AND orderID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ps.setInt(2, orderID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertOrder(OrderModel model) {
        try {
            String query = "INSERT INTO Orders VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getOrderID());
            ps.setString(2, model.getPaymentForm());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteOrder(int orderID) {
        try{
            String query = "DELETE FROM Orders WHERE orderID = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, orderID);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertIngredient(IngredientModel model) {
        insertIngredient2(model);
        insertIngredient1(model);
    }
    public void insertIngredient1(IngredientModel model) {
        try {
            String query = "INSERT INTO Ingredient1 VALUES (?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getName());
            ps.setInt(2, model.getShelfLife());
            ps.setString(3, model.getSupplierName());
            ps.setDouble(4, model.getPrice());
            ps.setInt(5, model.getOrderQuantity());
            ps.setString(6, model.getFoodType());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertIngredient2(IngredientModel model) {
        try {
            String query = "INSERT INTO Ingredient2 VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getFoodType());
            ps.setString(2, model.getStorageLocation());

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteIngredient(String name){
        String foodType = findFoodType(name);

        deleteIngredient1(name);
        deleteIngredient2(foodType);
    }

    public void deleteIngredient1(String name) {
        try{
            String query = "DELETE FROM Ingredient1 WHERE name = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteIngredient2(String foodType) {
        try{
            String query = "DELETE FROM Ingredient2 WHERE foodType = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, foodType);
            int rows = ps.executeUpdate();

            if (rows == 0){
                // did not delete
                throw new InvalidInputException();
            } else{
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch(InvalidInputException e){
            System.out.println("Table does not exist");
            rollbackConnection();
        }
        catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public String findFoodType(String name){
        try {
            String res;
            String query = "SELECT foodType FROM Ingredient2 WHERE name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getString(1);
            rs.close();
            ps.close();
            return res;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public void insertSupplier(SupplierModel model) {
        try {
            String query = "INSERT INTO Supplier VALUES (?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getName());
            ps.setString(2, model.getAddress());
            ps.setString(3, model.getPhoneNum());
            ps.setString(4, model.getOwnerName());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteSupplier(String name) {
        try {
            String query = "DELETE FROM Supplier WHERE supplier_name = ? ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                // did not delete
                throw new InvalidInputException();
            } else {
                connection.commit();
                ps.close();
            }
        }
        // catch if the table doesn't exist
        catch (InvalidInputException e) {
            System.out.println("Table does not exist");
            rollbackConnection();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }



    // Insert Statements for Relationships
    public void insertMadeWith(String ingredient, String menuitem) {
        try {
            String query = "INSERT INTO MadeWith VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, ingredient);
            ps.setString(2, menuitem);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertOrderandMenu(String menuItem, int orderID){
        try {
            String query = "INSERT INTO OrderAndMenu VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, menuItem);
            ps.setInt(2, orderID);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertWorks(int employeeID , int shiftID){
        try {
            String query = "INSERT INTO Works VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, employeeID);
            ps.setInt(2, shiftID);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertCooks(String name , int employeeID){
        try {
            String query = "INSERT INTO Cooks VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ps.setInt(2, employeeID);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
    public boolean doesSupplierExist(String name){
        try{
            int res;
            String query = "SELECT supplier_name FROM Supplier WHERE supplier_name = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getRow();
            rs.close();
            ps.close();
            return res>0;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
            return false;
        }
    }

    public MenuItemModel[] viewFastItems(int prepTime) {
        ArrayList<MenuItemModel> res = new ArrayList<>();
        try {
            String query = "SELECT * FROM MenuItem WHERE menuitem_preparationTime <= ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, prepTime);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                res.add(new MenuItemModel(rs.getString("menuitem_name"),
                        rs.getDouble("menuitem_price"),
                        rs.getInt("menuitem_preparationTime")));
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return res.toArray(new MenuItemModel[res.size()]);

    }

    // TABLE CREATION HELPERS

    private void createMenuItemTable() {
        try {
            String query = """
                    CREATE TABLE MenuItem(
                        menuitem_name varchar2(30) PRIMARY KEY,
                        menuitem_price REAL,
                        menuitem_preparationTime INTEGER)""";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createOwner1Table() {

        try {
            String query = """
                    CREATE TABLE Owner1 (
                    	owner1_accountName varchar2(20) PRIMARY KEY,
                    	owner1_name varchar2(10),
                        FOREIGN KEY (owner1_name) REFERENCES Owner2(owner2_name)
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createOwner2Table() {

        try {
            String query = """
                    CREATE TABLE Owner2 (
                    	owner2_name varchar2(10) PRIMARY KEY,
                    	owner2_password varchar2(20))
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createSupplierTable() {

        try {
            String query = """
                CREATE TABLE Supplier(
                    supplier_name varchar2(20) PRIMARY KEY,
                    supplier_address varchar2(40) UNIQUE,
                    supplier_phone# varchar2(10) UNIQUE,
                    supplier_ownerName varchar2(20),
                    FOREIGN KEY (supplier_ownerName) REFERENCES Owner1(owner1_accountName)
                    ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createIngredient1Table() {

        try {
            String query = """
                CREATE TABLE Ingredient1(
                    name varchar2(25) PRIMARY KEY,
                    shelfLife INTEGER,
                    supplierName varchar2(20),
                    price REAL,
                    orderQuantity INTEGER,
                    foodType varchar2(20),
                    FOREIGN KEY (supplierName) REFERENCES Supplier(supplier_name) ON DELETE SET NULL,
                    FOREIGN KEY (foodType) REFERENCES Ingredient2(foodType))
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createIngredient2Table() {

        try {
            String query = """
                    CREATE TABLE Ingredient2(
                         foodType varchar2(20) PRIMARY KEY,
                         storageLocation varchar2(20))
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createCustomerTable() {

        try {
            String query = """
                CREATE TABLE Customer(
                    name varchar2(30),
                    age INTEGER,
                    orderID INTEGER,
                    PRIMARY KEY(name, orderID),
                    FOREIGN KEY (orderID) REFERENCES Orders ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createOrderTable() {

        try {
            String query = """
                    CREATE TABLE Orders(
                           orderID INTEGER PRIMARY KEY,
                           paymentForm varchar2(20))
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createShift1Table() {
        try {
            String query = """
                    CREATE TABLE Shift1 (
                    	duration INTEGER PRIMARY KEY,
                    	breakTime INTEGER)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createShift2Table() {
        try {
            String query = """
                    CREATE TABLE Shift2 (
                    	startTime varchar2(20),
                    	endTime varchar2(20),
                    	duration INTEGER,
                    	PRIMARY KEY (startTime, endTime),
                        FOREIGN KEY (duration) REFERENCES Shift1
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createShift3Table() {
        try {
            String query = """
                    CREATE TABLE Shift3 (
                    	shiftID INTEGER,
                        startTime varchar2(20),
                    	endTime varchar2(20),
                    	PRIMARY KEY (shiftID),
                        FOREIGN KEY (startTime, endTime) REFERENCES Shift2
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createEmployeeTable() {
        try {
            String query = """
                    CREATE TABLE Employee (
                    	employeeID INTEGER,
                        name varchar2(30),
                        wageRate REAL,
                    	phoneNum varchar2(20) UNIQUE,
                    	emergencyContact varchar2(30),
                    	ownerName varchar2(20),
                    	PRIMARY KEY (employeeID),
                        FOREIGN KEY (ownerName) REFERENCES Owner2(owner2_name)
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createChefTable() {
        try {
            String query = """
                    CREATE TABLE Chef (
                    	employeeID INTEGER,
                        seniority varchar2(20),
                    	PRIMARY KEY (employeeID),
                        FOREIGN KEY (employeeID) REFERENCES Employee
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createWaiterTable() {
        try {
            String query = """
                    CREATE TABLE Waiter (
                    	employeeID INTEGER,
                        sirCertificate varchar2(20),
                    	PRIMARY KEY (employeeID),
                        FOREIGN KEY (employeeID) REFERENCES Employee
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // this has to be named Tables because it did NOT like the name Table in singular
    /* boolean doesn't exist in Oracle so Number is used, with 1 representing TRUE = indoors,
        0 representing FALSE = outdoors, constraint is added to ensure only 1 or 0 can be entered
    */

    private void createTableTable() {
        try {
            String query = """
                    CREATE TABLE Tables(
                    	tableNum INTEGER,
                    	seats INTEGER,
                        indoorOutdoor NUMBER(1),
                        waiterID INTEGER,
                        CONSTRAINT check_bool CHECK (indoorOutdoor IN (1,0)),
                    	PRIMARY KEY (tableNum),
                        FOREIGN KEY (waiterID) REFERENCES Employee(employeeID)
                        ON DELETE CASCADE)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createMadeWithTable() {
        try {
            String query = """
                CREATE TABLE MadeWith(
                    ingredientName varchar2(25),
                    menuItemName varchar2(25),
                    PRIMARY KEY (ingredientName,menuItemName),
                    FOREIGN KEY (ingredientName) REFERENCES Ingredient1(name),
                    FOREIGN KEY (menuItemName) REFERENCES MenuItem(menuitem_name))
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createOrderAndMenuTable() {
        try {
            String query = """
                CREATE TABLE OrderandMenu(
                    menuItemName varchar2(25),
                    orderID INTEGER,
                    PRIMARY KEY (menuItemName, orderID),
                    FOREIGN KEY (menuItemName) REFERENCES MenuItem(menuitem_name),
                    FOREIGN KEY (orderID) REFERENCES Orders)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createWorksTable() {
        try {
            String query = """
                CREATE TABLE Works(
                    employeeID INTEGER,
                    shiftID INTEGER,
                    PRIMARY KEY (employeeID, shiftID),
                    FOREIGN KEY (employeeID) REFERENCES Employee,
                    FOREIGN KEY (shiftID) REFERENCES Shift3)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void createCooksTable() {
        try {
            String query = """
                CREATE TABLE Cooks(
                    menuItemName varchar2(25),
                    employeeID INTEGER,
                    PRIMARY KEY (menuItemName, employeeID),
                    FOREIGN KEY (menuItemName) REFERENCES MenuItem(menuitem_name),
                    FOREIGN KEY (employeeID) REFERENCES Employee)
                    """;
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

    }


    public DefaultTableModel ingredientFromSupplier (String supplierName, double price) {
        try {
            String query = "SELECT i.name, i.price, s.supplier_address FROM Ingredient1 i, Supplier s WHERE s.supplier_Name = ? AND price <= ? AND i.supplierName = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, supplierName);
            ps.setDouble(2, price);
            ps.setString(3, supplierName);
            ResultSet rs = ps.executeQuery();
            return buildTableModel(rs);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public DefaultTableModel storageLocation(int count){
        try{
            String query = "SELECT DISTINCT i2.storageLocation, MAX(i1.price) FROM Ingredient1 i1, Ingredient2 i2 WHERE i1.foodType = i2.foodType GROUP BY StorageLocation HAVING COUNT(*) >= ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, count);
            ResultSet rs = ps.executeQuery();
            return buildTableModel(rs);
        } catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }


    public DefaultTableModel itemsAllIngredients(){
        try{
            String query = "SELECT m.menuitem_name FROM MenuItem m WHERE NOT EXISTS (SELECT i.name FROM Ingredient1 i MINUS SELECT mw.ingredientName FROM MadeWith mw WHERE mw.menuItemName = m.menuitem_name)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            return buildTableModel(rs);

        } catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException{
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int i = 1; i <= columnCount; i++) {
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }


    public DefaultTableModel getPaymentMethodsCount() {
        DefaultTableModel dtm = null;
        try {
            String query = "SELECT count(paymentForm), paymentForm FROM Orders GROUP BY paymentForm";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();
            dtm = buildTableModel(rs);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return dtm;
    }
}
