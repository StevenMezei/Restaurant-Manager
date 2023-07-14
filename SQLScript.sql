
CREATE TABLE Owner1 (
	accountName CHAR(20) PRIMARY KEY,
	name CHAR(10),
    FOREIGN KEY (name) REFERENCES Owner2(name) ON DELETE CASCADE);

CREATE TABLE Owner2 (
	name CHAR(10) PRIMARY KEY,
	password CHAR(20));
	
CREATE TABLE Supplier (
    name CHAR(20) PRIMARY KEY
    address CHAR(40) UNIQUE, 
    phoneNum CHAR(10) UNIQUE,
    ownerName CHAR(20), 
    FOREIGN KEY (ownerName) REFERENCES Owner1(accountName) ON DELETE CASCADE);

CREATE TABLE Ingredient1 (
	name CHAR(25) PRIMARY KEY,
	shelfLife INTEGER,
	supplierName CHAR(20),
	price REAL,
	orderQuantity integer,
	foodType CHAR(20),
	FOREIGN KEY (supplierName) REFERENCES Supplier(name),
    FOREIGN KEY (foodType) REFERENCES Ingredient2(foodType));

CREATE TABLE Ingredient2 (
    foodType CHAR(20) PRIMARY KEY,
    storageLocation CHAR(20));

CREATE TABLE Customer (
    name CHAR(30),
    age INTEGER,
    orderID INTEGER,
    PRIMARY KEY(name, orderID),
    FOREIGN KEY (orderID) REFERENCES Orders ON DELETE CASCADE);
	
CREATE TABLE Menu_Items (
	menuItemName CHAR(30) PRIMARY KEY,
	price REAL,
	preparationTime INTEGER);

CREATE TABLE Orders (
	orderID INTEGER PRIMARY KEY,
	paymentForm CHAR(20)); 

CREATE TABLE Shift1 (
	duration INTEGER PRIMARY KEY,
	breakTime INTEGER);

CREATE TABLE Shift2 (
	startTime CHAR(20),
	endTime CHAR(20),
	duration INTEGER,
	PRIMARY KEY (startTime, endTime),
	FOREIGN KEY(duration) REFERENCES Shift1 ON DELETE CASCADE);
	
CREATE TABLE Shift3 (
	shiftID INTEGER,
	startTime CHAR(20),
	endTime CHAR (20),
	PRIMARY KEY (shiftID),
	FOREIGN KEY (startTime, endTime) REFERENCES Shift2 ON DELETE CASCADE);

CREATE TABLE Employee (
	employeeID INTEGER,
	name CHAR(30),
	wageRate REAL,
	phone# CHAR (20) UNIQUE,
	emergencyContact CHAR (30),
	ownerName CHAR (10),
	PRIMARY KEY (employeeID),
	FOREIGN KEY (ownerName) REFERENCES Owner2 ON DELETE CASCADE);

CREATE TABLE Chef (
	employeeID INTEGER,
	seniority CHAR(15),
	PRIMARY KEY (employeeID),
	FOREIGN KEY (employeeID) REFERENCES Employee ON DELETE CASCADE);


CREATE TABLE Waiter (
	employeeID INTEGER,
	SIRcertificate CHAR(20),
	PRIMARY KEY (employeeID),
	FOREIGN KEY (employeeID) REFERENCES Employee ON DELETE CASCADE);
	
CREATE TABLE Table (
	table# INTEGER PRIMARY KEY,
	numSeats INTEGER,
	indoor BOOLEAN,
	waiterID INTEGER,
	FOREIGN KEY (waiterID) REFERENCES Waiter(employeeID) ON UPDATE CASCADE);
	
CREATE TABLE MadeWith (
	ingredientName CHAR(25),
	menuItemName CHAR(25),
	PRIMARY KEY(ingredientName, menuItemName),
	FOREIGN KEY (ingredientName) REFERENCES Ingredient2(name),
	FOREIGN KEY (menuItemName) REFERENCES Menu_Items);

CREATE TABLE OrderAndMenu (
	menuItemName CHAR(25),
	orderID INTEGER,
	PRIMARY KEY(menuItemName, orderID),
	FOREIGN KEY (menuItemName) REFERENCES Menu_Items,
	FOREIGN KEY (orderID) REFERENCES Orders(orderID));

CREATE TABLE Works (
	employeeID INTEGER,
	shiftID INTEGER,
	PRIMARY KEY(employeeID, shiftID),
	FOREIGN KEY (employeeID) REFERENCES Employee,
	FOREIGN KEY (shiftID) REFERENCES Shift3(shiftID));

CREATE TABLE Cooks (
	menuItemName CHAR(25),
	employeeID INTEGER,
	PRIMARY KEY(menuItemName, employeeID),
	FOREIGN KEY(menuItemName) REFERENCES Menu_Items(menuItemName),
	FOREIGN KEY(employeeID) REFERENCES Employee(employeeID));

INSERT INTO Owner1 VALUES('owner1', 'james');
INSERT INTO Owner1 VALUES('owner2', 'john');
INSERT INTO Owner1 VALUES('owner3', 'jack');
INSERT INTO Owner1 VALUES('owner4', 'jules');
INSERT INTO Owner1 VALUES('owner5', 'jill');

INSERT INTO Owner2 VALUES('james', 'abcdef');
INSERT INTO Owner2 VALUES('john', 'password');
INSERT INTO Owner2 VALUES('jack', '123');
INSERT INTO Owner2 VALUES('jules', 'password123');
INSERT INTO Owner2 VALUES('jill', 'mybirthday');

INSERT INTO Supplier VALUES('supplier1', '111 a street', '1111111', 'owner1');
INSERT INTO Supplier VALUES('supplier2', '222 b street', '2222222', 'owner1');
INSERT INTO Supplier VALUES('supplier3', '333 c street', '3333333', 'owner1');
INSERT INTO Supplier VALUES('supplier4', '444 d street', '4444444', 'owner1');
INSERT INTO Supplier VALUES('supplier5', '555 e street', '5555555', 'owner1');

INSERT INTO Ingredient1 VALUES('beef', 25, 'supplier1', 20.5, 10, 'meat');
INSERT INTO Ingredient1 VALUES('cheese', 30, 'supplier1', 5.6, 5, 'dairy');
INSERT INTO Ingredient1 VALUES('ravioli', 50, 'supplier2', 3.5, 5, 'pasta');
INSERT INTO Ingredient1 VALUES('pork', 25, 'supplier3', 20, 10, 'meat');
INSERT INTO Ingredient1 VALUES('tomato', 5, 'supplier4', 3.2, 20, 'fruit');
INSERT INTO Ingredient1 VALUES('ketchup', 40, 'supplier1', 2.5, 1, 'condiment');

INSERT INTO Ingredient2 VALUES('meat', 'freezer');
INSERT INTO Ingredient2 VALUES('dairy', 'fridge');
INSERT INTO Ingredient2 VALUES('pasta', 'larder');
INSERT INTO Ingredient2 VALUES('fruit', 'fridge');
INSERT INTO Ingredient2 VALUES('condiment', 'larder');

INSERT INTO Menu_Items VALUES('menuItem1', 10.5, 10);
INSERT INTO Menu_Items VALUES('menuItem2', 18, 30);
INSERT INTO Menu_Items VALUES('menuItem3', 9, 10);
INSERT INTO Menu_Items VALUES('menuItem4', 20, 25);
INSERT INTO Menu_Items VALUES('menuItem5', 12.5, 25);

INSERT INTO Orders VALUES(1, 'visa');
INSERT INTO Orders VALUES(2, 'cash');
INSERT INTO Orders VALUES(3, 'debit');
INSERT INTO Orders VALUES(4, 'visa');
INSERT INTO Orders VALUES(5, 'cash');

INSERT INTO Customer VALUES('Manny', 19, 1);
INSERT INTO Customer VALUES('Matthew', 20, 1);
INSERT INTO Customer VALUES('Muncy' 35, 2);
INSERT INTO Customer VALUES('Morbius' 87, 3);
INSERT INTO Customer VALUES('Matilda' 38, 4);
INSERT INTO Customer VALUES('Maxim' 5, 5);

INSERT INTO MadeWith VALUES('beef', 'menuItem1');
INSERT INTO MadeWith VALUES('tomato', 'menuItem1');
INSERT INTO MadeWith VALUES('cheese', 'menuItem2');
INSERT INTO MadeWith VALUES('pork', 'menuItem2');
INSERT INTO MadeWith VALUES('ravioli', 'menuItem3');

INSERT INTO OrderAndMenu VALUES('menuItem1', 1);
INSERT INTO OrderAndMenu VALUES('menuItem2', 2);
INSERT INTO OrderAndMenu VALUES('menuItem3', 3);
INSERT INTO OrderAndMenu VALUES('menuItem4', 4);
INSERT INTO OrderAndMenu VALUES('menuItem5', 5);

INSERT INTO Shift1 VALUES (120,10);
INSERT INTO Shift1 VALUES (180,15);
INSERT INTO Shift1 VALUES (240,20);
INSERT INTO Shift1 VALUES (300,25);
INSERT INTO Shift1 VALUES (360,30);

INSERT INTO Shift2 VALUES ('4:00', '6:00', 120);
INSERT INTO Shift2 VALUES ('3:00', '6:00', 180);
INSERT INTO Shift2 VALUES ('10:00', '2:00', 240);
INSERT INTO Shift2 VALUES ('10:00', '3:00', 300);
INSERT INTO Shift2 VALUES ('10:00', '4:00', 360);

INSERT INTO Shift3 VALUES (1,'4:00', '6:00');
INSERT INTO Shift3 VALUES (2,'3:00', '6:00');
INSERT INTO Shift3 VALUES (3,'10:00', '2:00');
INSERT INTO Shift3 VALUES (4,'10:00', '3:00');
INSERT INTO Shift3 VALUES (5,'10:00', '4:00');

INSERT INTO Employee VALUES (1,'Sally', 16.50, '778-235-4950','604-123-4567','james');
INSERT INTO Employee VALUES (2,'Tim', 17.50, '778-213-3749','604-342-4724','john');
INSERT INTO Employee VALUES (3,'Tom', 16.50, '778-573-2916','604-234-5739','jack');
INSERT INTO Employee VALUES (4,'Ally', 17.50, '778-213-3749','604-342-4724','john');
INSERT INTO Employee VALUES (5,'Jim', 16.50, '778-573-2916','604-234-5739','jack');

INSERT INTO Employee VALUES (6,'Brenda', 18.00, '778-018-4720','604-074-7593','jules');
INSERT INTO Employee VALUES (7,'Sonya', 18.50, '778-764-3820','604-548-4839','jill');
INSERT INTO Employee VALUES (8,'David', 18.00, '778-018-4720','604-074-7593','jules');
INSERT INTO Employee VALUES (9,'Mia', 18.50, '778-764-3820','604-548-4839','jill');
INSERT INTO Employee VALUES (10,'Adam', 18.50, '778-764-3820','604-548-4839','jill');

INSERT INTO Chef VALUES (6,'Head Chef');
INSERT INTO Chef VALUES (7,'Sous Chef');
INSERT INTO Chef VALUES (8,'Junior Chef');
INSERT INTO Chef VALUES (9,'Line Cook');
INSERT INTO Chef VALUES (10,'Linecook');

INSERT INTO Waiter VALUES(1, 'Yes');
INSERT INTO Waiter VALUES(2, 'Yes');
INSERT INTO Waiter VALUES(3, 'No');
INSERT INTO Waiter VALUES(4, 'Yes');
INSERT INTO Waiter VALUES(5, 'No');

INSERT INTO Table VALUES(1, 4, TRUE, 3);
INSERT INTO Table VALUES(2, 4, TRUE, 3);
INSERT INTO Table VALUES(3, 8, FALSE, 1);
INSERT INTO Table VALUES(4, 6, FALSE, 4);
INSERT INTO Table VALUES(5, 6, FALSE, 5);

INSERT INTO Works VALUES (2, 1);
INSERT INTO Works VALUES (6, 2);
INSERT INTO Works VALUES (7, 1);
INSERT INTO Works VALUES (4, 4);
INSERT INTO Works VALUES (3, 3);

INSERT INTO Cooks VALUES ('menuitem1', 6);
INSERT INTO Cooks VALUES ('menuitem2', 7);
INSERT INTO Cooks VALUES ('menuitem3', 8);
INSERT INTO Cooks VALUES ('menuitem4', 9);
INSERT INTO Cooks VALUES ('menuitem5', 10);
