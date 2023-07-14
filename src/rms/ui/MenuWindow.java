package rms.ui;

import rms.delegates.MenuWindowDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.Stack;

public class MenuWindow extends JFrame implements ActionListener {

    private JPanel cards;
    private JPanel viewTableCard;

    private JPasswordField passwordField;
    private JTextField usernameField;
    private JTextField supplierField;
    private JTextField joinSupplierField;
    private JTextField joinPriceField;
    private JTextField storagePriceField;
    private JTextField menuNameField;
    private JTextField menuPriceField;
    private JTextField menuPrepField;
    private JTextField menuIngredientField;
    private JTextField menuViewMaxPrepTimeField;
    private JTextField employeeIDField;
    private JTextField wageField;

    private Stack<String> cardRecord;

    private MenuWindowDelegate delegate;

    public MenuWindow() {
        super("Restaurant Management Software");
        // initializes record to keep track of visited cards
        cardRecord = new Stack<>();
    }

    // initializes and displays main interface
    public void showFrame(MenuWindowDelegate delegate) {

        this.delegate = delegate;

        // initializes panels
        JPanel loginCard = createLoginPanel();
        JPanel homeCard = createHomePanel();
        JPanel employeeCard = createEmployeePanel();
        JPanel menuCard = createMenuItemPanel();
        JPanel supplierCard = createSupplierPanel();
        JPanel ordersCard = createOrderPanel();
        JPanel ingredientFromSuppCard = createIngredientFromSupplier();
        JPanel deleteCard = createDeletePanel();
        JPanel insertCard = createInsertPanel();
        JPanel maxPrepTimeCard = createViewFastItemsPanel();
        JPanel storageCard = createStoragePanel();
        JPanel updateEmployeeCard = createUpdateEmployee();

        viewTableCard = createTableViewPanel();

        // add panels to card layout
        cards = new JPanel(new CardLayout());
        cards.add(loginCard, "Login");
        cards.add(homeCard, "Home");
        cards.add(employeeCard, "Employee");
        cards.add(menuCard, "Menu");
        cards.add(ordersCard, "Orders");
        cards.add(supplierCard, "Supplier");
        cards.add(ingredientFromSuppCard, "Ingredient from supplier");
        cards.add(deleteCard, "Delete");
        cards.add(insertCard, "Insert");
        cards.add(maxPrepTimeCard, "View fast items");
        cards.add(storageCard, "Storage");
        cards.add(updateEmployeeCard, "Update wage rate");
        cards.add(viewTableCard, "Table");

        this.setContentPane(cards);

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);

    }

    // returns a JPanel for login GUI
    private JPanel createLoginPanel() {
        final int TEXT_FIELD_WIDTH = 10;

        JPanel loginPanel = new JPanel();

        JLabel usernameLabel = new JLabel("Enter username: ");
        JLabel passwordLabel = new JLabel("Enter password: ");

        usernameField = new JTextField(TEXT_FIELD_WIDTH);
        passwordField = new JPasswordField(TEXT_FIELD_WIDTH);

        JButton loginButton = new JButton("Login");

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        loginPanel.setLayout(gb);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(usernameLabel, c);
        loginPanel.add(usernameLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        loginPanel.add(usernameField);

        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        loginPanel.add(passwordLabel);

        // place the password field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        loginPanel.add(passwordField);

        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        loginPanel.add(loginButton);

        loginButton.addActionListener(this);

        return loginPanel;
    }

    // returns a JPanel for the menu GUI
    private JPanel createHomePanel() {
        JPanel menuPanel = new JPanel();

        // create buttons and add them to panel
        JButton manageButton = new JButton("Menu");
        manageButton.addActionListener(this);
        menuPanel.add(manageButton);

        JButton viewButton = new JButton("Employee");
        viewButton.addActionListener(this);
        menuPanel.add(viewButton);

        JButton insertButton = new JButton("Supplier");
        insertButton.addActionListener(this);
        menuPanel.add(insertButton);

        JButton orderButton = new JButton("Orders");
        orderButton.addActionListener(this);
        menuPanel.add(orderButton);

        JButton logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(this);
        menuPanel.add(logOutButton);

        return menuPanel;
    }

    // returns a JPanel for management GUI
    private JPanel createEmployeePanel() {
        JPanel employeePanel = new JPanel();

        // create buttons and add them to panel
        JButton viewButton = new JButton("View employees");
        viewButton.addActionListener(this);
        employeePanel.add(viewButton);

        JButton updateButton = new JButton("Update wage rate");
        updateButton.addActionListener(this);
        employeePanel.add(updateButton);

        JButton quickviewButton = new JButton("Quick overview");
        quickviewButton.addActionListener(this);
        employeePanel.add(quickviewButton);

        JButton chefsButton = new JButton("Chefs");
        chefsButton.addActionListener(this);
        employeePanel.add(chefsButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        employeePanel.add(backButton);

        return employeePanel;
    }

    // returns a JPanel for management GUI
    private JPanel createMenuItemPanel() {
        JPanel menuItemPanel = new JPanel();

        // create buttons and add them to panel

        JButton insertButton = new JButton("New menu item");
        insertButton.addActionListener(this);
        menuItemPanel.add(insertButton);

        JButton viewButton = new JButton("View fast foods");
        viewButton.addActionListener(this);
        menuItemPanel.add(viewButton);

        JButton allItems = new JButton("All Ingredients");
        allItems.addActionListener(this);
        menuItemPanel.add(allItems);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        menuItemPanel.add(backButton);

        return menuItemPanel;
    }

    // returns a JPanel for management GUI
    private JPanel createSupplierPanel() {
        JPanel supplierPanel = new JPanel();

        JButton viewAllButton = new JButton("View suppliers");
        viewAllButton.addActionListener(this);
        supplierPanel.add(viewAllButton);

        // create buttons and add them to panel
        JButton deleteButton = new JButton("Remove supplier");
        deleteButton.addActionListener(this);
        supplierPanel.add(deleteButton);

        // Get Ingredient from Supplier
        JButton ingredientButton = new JButton("Search Ingredient");
        ingredientButton.addActionListener(this);
        supplierPanel.add(ingredientButton);

        JButton storageLocationButton = new JButton("Storage Location");
        storageLocationButton.addActionListener(this);
        supplierPanel.add(storageLocationButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        supplierPanel.add(backButton);

        return supplierPanel;
    }

    // returns a JPanel for supplier deletion GUI
    private JPanel createDeletePanel(){
        JPanel deletePanel = new JPanel();

        JLabel supplierLabel = new JLabel("Enter supplier name: ");
        JButton deleteSupplier = new JButton("Delete Supplier");

        supplierField = new JTextField(10);
        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        deletePanel.setLayout(gb);
        deletePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(supplierLabel, c);
        deletePanel.add(supplierLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(supplierField, c);
        deletePanel.add(supplierField);

        // place the delete supplier button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(deleteSupplier, c);

        deleteSupplier.addActionListener(this);
        deletePanel.add(deleteSupplier);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        deletePanel.add(backButton);
        return deletePanel;
    }

    private JPanel createStoragePanel(){
        JPanel storagePanel = new JPanel();

        JLabel storageLabel = new JLabel("Minimum Items per storage Location: ");

        JButton findMaxPrice = new JButton("Find Max Price");
        storagePriceField = new JTextField(10);
        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        storagePanel.setLayout(gb);
        storagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(storageLabel, c);
        storagePanel.add(storageLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(storagePriceField, c);
        storagePanel.add(storagePriceField);

        // place the delete supplier button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(findMaxPrice, c);

        findMaxPrice.addActionListener(this);
        storagePanel.add(findMaxPrice);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        storagePanel.add(backButton);
        return storagePanel;
    }

    private JPanel createIngredientFromSupplier() {

        JPanel joinPanel = new JPanel();

        JLabel supplierLabel = new JLabel("Enter supplier name here: ");
        JLabel priceLabel = new JLabel("Enter max price here: ");

        joinSupplierField = new JTextField(10);
        joinPriceField = new JTextField(10);

        JButton ingredientButton = new JButton("Get Ingredients");

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        joinPanel.setLayout(gb);
        joinPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the supplier label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(supplierLabel, c);
        joinPanel.add(supplierLabel);

        // place the text field for the supplier
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(joinSupplierField, c);
        joinPanel.add(joinSupplierField);

        // place price label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(priceLabel, c);
        joinPanel.add(priceLabel);

        // place the price field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(joinPriceField, c);
        joinPanel.add(joinPriceField);

        // place the get ingredient button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(ingredientButton, c);
        joinPanel.add(ingredientButton);

        ingredientButton.addActionListener(this);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        joinPanel.add(backButton);
        return joinPanel;
    }
    // returns a JPanel for menu item insert GUI
    private JPanel createInsertPanel() {
        final int TEXT_FIELD_WIDTH = 10;

        JPanel insertPanel = new JPanel();

        JLabel nameLabel = new JLabel("Enter menu item name: ");
        JLabel priceLabel = new JLabel("Enter price: ");
        JLabel prepTimeLabel = new JLabel("Enter preparation time: ");
        JLabel ingredientLabel = new JLabel("Enter ingredients: ");

        menuNameField = new JTextField(TEXT_FIELD_WIDTH);
        menuPriceField = new JTextField(TEXT_FIELD_WIDTH);
        menuPrepField = new JTextField(TEXT_FIELD_WIDTH);
        menuIngredientField = new JTextField(TEXT_FIELD_WIDTH);

        JButton insertButton = new JButton("insert new menu item");
        JButton backButton = new JButton("back");

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        insertPanel.setLayout(gb);
        insertPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the name label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(nameLabel, c);
        insertPanel.add(nameLabel);

        // place the text field for the name
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(menuNameField, c);
        insertPanel.add(menuNameField);

        // place price label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(priceLabel, c);
        insertPanel.add(priceLabel);

        // place the price field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(menuPriceField, c);
        insertPanel.add(menuPriceField);

        // place prepTime label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(prepTimeLabel, c);
        insertPanel.add(prepTimeLabel);

        // place the prepTime field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(menuPrepField, c);
        insertPanel.add(menuPrepField);

        // place ingredient label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(ingredientLabel, c);
        insertPanel.add(ingredientLabel);

        // place the ingredient field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(menuIngredientField, c);
        insertPanel.add(menuIngredientField);

        // place the insert button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(insertButton, c);
        insertPanel.add(insertButton);

        insertButton.addActionListener(this);

        // place the back button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(backButton, c);
        insertPanel.add(backButton);

        backButton.addActionListener(this);

        return insertPanel;
    }

    // returns a JPanel to view menu items with USER INPUT prep time GUI
    private JPanel createViewFastItemsPanel() {
        JPanel viewFastItemsPanel = new JPanel();

        JLabel maxPrepTimeLabel = new JLabel("Enter maximum prep time: ");
        JButton searchButton = new JButton("Search Fast Items");
        JButton backButton = new JButton("Back");

        menuViewMaxPrepTimeField = new JTextField(10);
        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        viewFastItemsPanel.setLayout(gb);
        viewFastItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the prepTime label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(maxPrepTimeLabel, c);
        viewFastItemsPanel.add(maxPrepTimeLabel);

        // place the text field for the prepTime
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(menuViewMaxPrepTimeField, c);
        viewFastItemsPanel.add(menuViewMaxPrepTimeField);

        // place the search button
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 10, 0);
        gb.setConstraints(searchButton, c);
        c.anchor = GridBagConstraints.CENTER;

        searchButton.addActionListener(this);
        viewFastItemsPanel.add(searchButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 0, 10, 10);
        gb.setConstraints(backButton, c);

        backButton.addActionListener(this);
        viewFastItemsPanel.add(backButton);

        return viewFastItemsPanel;
    }

    private JPanel createTableViewPanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        tablePanel.add(backButton);
        return tablePanel;
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel();

        JButton viewPaymentMethodCountButton = new JButton("View payment methods count");
        viewPaymentMethodCountButton.addActionListener(this);
        orderPanel.add(viewPaymentMethodCountButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(this);
        orderPanel.add(backButton);

        return orderPanel;
    }

    // returns a JPanel for updating employees' wage rate GUI
    private JPanel createUpdateEmployee() {
        final int TEXT_FIELD_WIDTH = 10;

        JPanel updateEmployeePanel = new JPanel();

        JLabel employeeIDLabel = new JLabel("Enter employee ID: ");
        JLabel wageLabel = new JLabel("Enter new wage rate: ");

        employeeIDField = new JTextField(TEXT_FIELD_WIDTH);
        wageField = new JTextField(TEXT_FIELD_WIDTH);

        JButton updateButton = new JButton("Update Employee");
        JButton backButton = new JButton("back");

        // layout components using GridBag
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        updateEmployeePanel.setLayout(gb);
        updateEmployeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the ID label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(employeeIDLabel, c);
        updateEmployeePanel.add(employeeIDLabel);

        // place the text field for the ID
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(employeeIDField, c);
        updateEmployeePanel.add(employeeIDField);

        // place price label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(wageLabel, c);
        updateEmployeePanel.add(wageLabel);

        // place the price field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(wageField, c);
        updateEmployeePanel.add(wageField);


        // place the insert button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(updateButton, c);
        updateEmployeePanel.add(updateButton);

        updateButton.addActionListener(this);

        // place the back button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(backButton, c);
        updateEmployeePanel.add(backButton);

        backButton.addActionListener(this);

        return updateEmployeePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        CardLayout cl = (CardLayout)(cards.getLayout());

        switch (button.getText().toLowerCase()) {
            // LOGIN PAGE
            case "login" -> handleLogin();
            // HOME PAGE
            case "menu" -> {
                cardRecord.push("Home");
                cl.show(cards, "Menu");
            }
            case "employee" -> {
                cardRecord.push("Home");
                cl.show(cards, "Employee");
            }
            case "supplier" -> {
                cardRecord.push("Home");
                cl.show(cards, "Supplier");
            }
            case "orders" -> {
                cardRecord.push("Home");
                cl.show(cards, "Orders");
            }
            // ORDERS PAGE
            case "view payment methods count" -> {
                handleViewPaymentMethodsCount();
            }
            // MENU PAGE
            case "new menu item" -> {
                cardRecord.push("Menu");
                cl.show(cards, "Insert");
            }
            case "view fast foods" -> {
                cardRecord.push("Menu");
                cl.show(cards, "View fast items");
            }
            // SUPPLIER PAGE
            case "view suppliers" -> {
                handleViewSuppliers();
            }
            case "remove supplier" -> {
                cardRecord.push("Supplier");
                cl.show(cards, "Delete");
            }
            // FULL EMPLOYEE VIEW PAGE
            case "view employees" -> handleViewEmployees();

            // UPDATE WAGE RATE FOR EMPLOYEE
            case "update wage rate" -> {
                cardRecord.push("Employee");
                cl.show(cards,"Update wage rate");
            }
            case "update employee" -> {
                handleUpdateEmployee();
            }

            // PROJECTION: OVERVIEW FOR EMPLOYEE
            case "quick overview" -> {
                handleProjectEmployee();
            }

            //NESTED AGGREGATION WITH GROUP BY
            case "chefs" -> {
                handleNestedChefs();
            }

            // NEW MENU ITEM PAGE
            case "insert new menu item" -> handleInsertMenuItem();

            // SEARCH FAST FOOD PAGE
            case "search fast items" -> handleFastItemSearch();

            // SEARCH INGREDIENTS FROM SUPPLIER
            case "search ingredient" -> {
                cardRecord.push("Supplier");
                cl.show(cards, "Ingredient from supplier");
            }

            case "get ingredients" -> ingredientFromSupplier();

            case "find max price" -> storageLocation();

            case "storage location" -> {
                cardRecord.push("Supplier");
                cl.show(cards,"Storage");
            }
            case "all ingredients" -> itemsAllIngredients();
            // BACK BUTTONS
            case "back", "log out" -> cl.show(cards, cardRecord.pop());

            // DELETE SUPPLIER PAGE
            case "delete supplier" -> {
                handleDeleteSupplier();
            }
            default -> {
            }
        }
    }

    private void handleViewSuppliers() {
        delegate.viewSuppliers();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Supplier");
        cl.show(cards, "Table");
    }

    private void handleViewPaymentMethodsCount() {
        delegate.viewPaymentMethodsCount();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Orders");
        cl.show(cards, "Table");
    }

    private void handleViewEmployees() {
        delegate.viewEmployees();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Employee");
        cl.show(cards, "Table");
    }

    private void handleProjectEmployee(){
        delegate.projectEmployees();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Employee");
        cl.show(cards, "Table");
    }

    private void handleNestedChefs(){
        delegate.nestedChefs();
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Employee");
        cl.show(cards, "Table");
    }

    private void handleFastItemSearch() {
        delegate.viewFastItems(menuViewMaxPrepTimeField.getText());
    }

    // attempts to insert a new menu item, errors handled in restaurant class
    private void handleInsertMenuItem() {
        String name = menuNameField.getText();
        String ingredients = menuIngredientField.getText();
        String price = menuPriceField.getText();
        String prepTime = menuPrepField.getText();
        // passed as strings, input validation handled in Restaurant class
        delegate.insertMenuItemStrings(name, price, prepTime, ingredients);
    }

    public void handleDeleteSupplier(){
        String name = supplierField.getText();
        delegate.deleteSupplier(name);
    }

    public void handleGoodDelete(){
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardRecord.pop());
        displayMessage("Deleted supplier!");

        // clear supplier after delete
        supplierField.setText("");
    }

    public void handleUpdateEmployee(){
        String employeeID = employeeIDField.getText();
        String wageRate = wageField.getText();
        delegate.updateEmployee(employeeID,wageRate);
    }

    public void handleGoodUpdate(){
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardRecord.pop());
        displayMessage("Updated wage rate!");

        //clear text fields
        employeeIDField.setText("");
        wageField.setText("");
    }

    public void ingredientFromSupplier() {
        String supplierName = joinSupplierField.getText();
        String price = joinPriceField.getText();
        delegate.ingredientFromSupplier(supplierName, price);
    }

    public void storageLocation(){
        String minimumPrice = storagePriceField.getText();
        delegate.storageLocation(minimumPrice);
    }

    public void itemsAllIngredients(){
        delegate.menuItemsAllIngredients();
    }

    public void handleGoodStorageLocation(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Storage");
        cl.show(cards, "Table");
        storagePriceField.setText("");
    }
    public void handleGoodIngredientFromSupp(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Ingredient from supplier");
        cl.show(cards, "Table");
        joinSupplierField.setText("");
        joinPriceField.setText("");
    }
    public void handleGoodAllIngredients(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("Menu");
        cl.show(cards, "Table");
    }

    // handles login, logic handled by delegate
    private void handleLogin() {
        delegate.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
    }

    // handles a successful login
    public void handleGoodLogin() {
        CardLayout cl = (CardLayout)(cards.getLayout());
        // add current card to card record
        cardRecord.push("Login");
        // display next card
        cl.show(cards, "Home");

        // clear name + pw after login
        usernameField.setText("");
        passwordField.setText("");
    }

    // handles a successful insertion
    public void handleGoodInsert() {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, cardRecord.pop());
        displayMessage("Inserted item!");
        // clears menu fields
        menuNameField.setText("");
        menuPriceField.setText("");
        menuPrepField.setText("");
        menuIngredientField.setText("");
}

    // create popup dialog with given string
    private void displayMessage(String text) {
        new ErrorWindow(text);
    }

    // handles a successful view fast items
    public void handleGoodViewFastItems() {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cardRecord.push("View fast items");
        cl.show(cards, "Table");
        menuViewMaxPrepTimeField.setText("");
    }

    // displays given array in a JTable
    public <T> void displayTable(T[] array) {
        JTable table;
        if (array.length > 0) {
            Object o = array[0];
            // get fields from array's class
            Field[] fields = o.getClass().getDeclaredFields();
            // place all field names in column name array
            String[] columnNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                columnNames[i] = fields[i].getName();
            }
            // place array data in 2 dimension array
            Object[][] data = new Object[array.length][columnNames.length];
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < columnNames.length; j++) {
                    try {
                        data[i][j] = fields[j].get(array[i]);
                    } catch (Exception e) {
                        displayMessage("Something went wrong!");
                    }
                }
            }
            // create JTable and populate it
            table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            // remove scrollPane if exists
            Component[] componentList = viewTableCard.getComponents();
            for(Component c : componentList){
                if(c instanceof JScrollPane){
                    viewTableCard.remove(c);
                }
            }

            viewTableCard.add(scrollPane);
        } else {
            displayMessage("No results match your query");
        }
    }

    public void displayTable2(DefaultTableModel dtm){
        // create JTable and populate it
        JTable table;
        table = new JTable(dtm);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // remove scrollPane if exists
        Component[] componentList = viewTableCard.getComponents();
        for(Component c : componentList){
            if(c instanceof JScrollPane){
                viewTableCard.remove(c);
            }
        }
        viewTableCard.add(scrollPane);

    }


}
