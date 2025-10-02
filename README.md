# GroceryPriceCalculator

## Overview
The Grocery Cart Calculator is a **console-based Java application** that helps users manage a grocery product database, create a shopping list, and calculate total costs based on a budget. It uses **SQLite** for persistent storage of product data.

**Key Features:**
- Add, delete, update, and search products (CRUD operations)
- Manage shopping list using Product IDs
- Calculate total cost and compare with a user-defined budget
- Exit option available in all menus for convenience
- Product data is persistent via SQLite database

---

## Project Structure
```
GroceryCartCalcutor/
│
├── Product.java
├── DatabaseManager.java
├── GroceryPriceCalculator.java
├── sqlite-jdbc-3.50.3.0.jar
└── grocery.db   (auto-created on first run)
```

**Files:**
- `Product.java` → Defines the Product class (name, category, price)
- `DatabaseManager.java` → Handles SQLite database operations (CRUD)
- `GroceryPriceCalculator.java` → Main program with menus, shopping list, budget management
- `sqlite-jdbc-3.50.3.0.jar` → JDBC driver for SQLite
- `grocery.db` → SQLite database storing product data

---

## How to Run

### 1️⃣ Compile
Open a terminal/command prompt in the project folder:

**Linux / macOS**
```bash
javac -cp ".:sqlite-jdbc-3.50.3.0.jar" Product.java DatabaseManager.java GroceryPricePredictor.java
```

**Windows**
```bash
javac -cp ".;sqlite-jdbc-3.50.3.0.jar" Product.java DatabaseManager.java GroceryPriceCalculator.java
```

### 2️⃣ Run
**Linux / macOS**
```bash
java -cp ".:sqlite-jdbc-3.50.3.0.jar" GroceryPriceCalculator
```

**Windows**
```bash
java -cp ".;sqlite-jdbc-3.50.3.0.jar" GroceryPriceCalculator
```

- The program automatically creates `grocery.db` if it doesn't exist.
- Default products are loaded if the database is empty.
- Run this command each time you want to start the program.

---

## Program Navigation

### Main Menu
```
1. Update Schema
2. Price Calculator / Shopping List
3. Exit Program
```
- **Update Schema:** Manage products in the database  
- **Price Calculator:** Add/remove items to shopping list, calculate totals, check budget  
- **Exit Program:** Quit the application  

### Update Schema Menu
```
1. Add Product
2. Delete Product
3. Update Product Price
4. Search Product
5. Back to Main Menu
6. Exit Program
```

### Price Calculator / Shopping List Menu
```
1. Show Product Table
2. Add Item to List
3. Delete Item from List
4. Update Quantity
5. View Shopping List & Total
6. Back to Main Menu
7. Exit Program
```

- Shopping list operations are **based on Product IDs**  
- Option 5 displays total cost and alerts if over budget  
- Options 6 & 7 allow navigation back or exit  

---

## Notes
- Product IDs are used to manage the shopping list  
- Budget warning is displayed if total exceeds the entered budget  
- `.class` files remain after compilation; recompile only if code changes  
- Product data persists between runs via `grocery.db`  
- Shopping list is reset on each run (can be enhanced to persist)
