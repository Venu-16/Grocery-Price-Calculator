import java.util.*;

public class GroceryPriceCalculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DatabaseManager db = new DatabaseManager();

    // shoppingList: productId -> quantity
    private static final Map<Integer, Integer> shoppingList = new HashMap<>();
    private static double budget = 0;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Available Products ---");
            db.getAllProducts().forEach(System.out::println);

            printMainMenu();
            int choice = safeReadInt();

            switch (choice) {
                case 1 -> updateSchemaMenu();
                case 2 -> shoppingMenu();
                case 3 -> { System.out.println("Exiting program... "); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // ---------- Helper Input Methods ----------
    private static int safeReadInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        if (value < 0) {
            System.out.println("Value cannot be negative. Defaulting to 0.");
            value = 0;
        }
        return value;
    }

    private static double safeReadDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print(" Invalid input. Enter a valid number: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        if (value < 0) {
            System.out.println(" Value cannot be negative. Defaulting to 0.");
            value = 0;
        }
        return value;
    }

    private static String safeReadString(String prompt) {
        scanner.nextLine(); // consume newline
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(" Input cannot be empty!");
            }
        } while (input.isEmpty());
        return input;
    }

    // ---------- Menu Printers ----------
    private static void printMainMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Update Schema");
        System.out.println("2. Price Calculator / Shopping List");
        System.out.println("3. Exit Program");
        System.out.print("Choose option: ");
    }

    private static void printUpdateSchemaMenu() {
        System.out.println("\n--- Update Schema Menu ---");
        System.out.println("1. Add Product");
        System.out.println("2. Delete Product");
        System.out.println("3. Update Product Price");
        System.out.println("4. Search Product");
        System.out.println("5. Back to Main Menu");
        System.out.println("6. Exit Program");
        System.out.print("Choose option: ");
    }

    private static void printShoppingMenu() {
        System.out.println("\n--- Shopping List Menu ---");
        System.out.println("1. Show Product Table");
        System.out.println("2. Add Item to List");
        System.out.println("3. Delete Item from List");
        System.out.println("4. Update Quantity");
        System.out.println("5. View Shopping List & Total");
        System.out.println("6. Back to Main Menu");
        System.out.println("7. Exit Program");
        System.out.print("Choose option: ");
    }

    // ----------- Update Schema Menu -----------
    private static void updateSchemaMenu() {
        while (true) {
            printUpdateSchemaMenu();
            int choice = safeReadInt();

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> deleteProduct();
                case 3 -> updateProduct();
                case 4 -> searchProduct();
                case 5 -> { return; } // Back to Main Menu
                case 6 -> { System.out.println("Exiting program... "); System.exit(0); }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void addProduct() {
        String name = safeReadString("Enter product name: ");
        String category = safeReadString("Enter category: ");
        System.out.print("Enter price: ");
        double price = safeReadDouble();

        if (price <= 0) {
            System.out.println(" Price must be greater than 0.");
            return;
        }

        db.addProduct(new Product(name, category, price));
        System.out.println(" Product added.");
    }

    private static void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        int id = safeReadInt();
        Product p = db.findById(id);
        if (p == null) {
            System.out.println("❌ Product not found.");
            return;
        }
        db.deleteProduct(id);
        System.out.println(" Product deleted.");
    }

    private static void updateProduct() {
        System.out.print("Enter product ID to update: ");
        int id = safeReadInt();
        Product p = db.findById(id);
        if (p == null) {
            System.out.println(" Product not found.");
            return;
        }
        System.out.println("Current: " + p);
        System.out.print("Enter new price: ");
        double price = safeReadDouble();
        if (price <= 0) {
            System.out.println(" Price must be greater than 0.");
            return;
        }
        db.updateProduct(id, price);
        System.out.println("✅ Product updated.");
    }

    private static void searchProduct() {
        String name = safeReadString("Enter product name to search: ");
        Product p = db.findByName(name);
        System.out.println(p != null ? "Found: " + p : " Product not found.");
    }

    // ----------- Shopping / Price Calculator Menu -----------
    private static void shoppingMenu() {
        System.out.print("Enter your budget (0 if none): ");
        budget = safeReadDouble();

        while (true) {
            printShoppingMenu();
            int choice = safeReadInt();

            switch (choice) {
                case 1 -> db.getAllProducts().forEach(System.out::println);
                case 2 -> addItemToList();
                case 3 -> removeItemFromList();
                case 4 -> updateItemQuantity();
                case 5 -> viewShoppingList();
                case 6 -> { return; } // Back to Main Menu
                case 7 -> { System.out.println("Exiting program... "); System.exit(0); }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void addItemToList() {
        System.out.print("Enter product ID to add: ");
        int id = safeReadInt();
        Product p = db.findById(id);
        if (p == null) { 
            System.out.println("❌ Product not found."); 
            return; 
        }

        System.out.print("Enter quantity: ");
        int qty = safeReadInt();
        if (qty <= 0) {
            System.out.println("❌ Quantity must be greater than 0.");
            return;
        }

        double cost = p.getPrice() * qty;
        if (budget > 0 && (getTotalCost() + cost) > budget) {
            System.out.println(" Adding this item will exceed your budget!");
        }
        else{
        shoppingList.put(id, shoppingList.getOrDefault(id, 0) + qty);
        System.out.println(" Added to shopping list.");
        }
    }

    private static void removeItemFromList() {
        System.out.print("Enter product ID to remove: ");
        int id = safeReadInt();
        if (!shoppingList.containsKey(id)) { 
            System.out.println(" Not in shopping list."); 
            return; 
        }
        shoppingList.remove(id);
        System.out.println(" Removed from shopping list.");
    }

    private static void updateItemQuantity() {
        System.out.print("Enter product ID to update quantity: ");
        int id = safeReadInt();
        if (!shoppingList.containsKey(id)) { 
            System.out.println(" Not in shopping list."); 
            return; 
        }

        System.out.print("Enter new quantity: ");
        int qty = safeReadInt();
        if (qty <= 0) {
            System.out.println(" Quantity must be greater than 0.");
            return;
        }

        shoppingList.put(id, qty);
        System.out.println(" Quantity updated.");
    }

    private static void viewShoppingList() {
        double total = 0;
        System.out.println("\n--- Shopping List ---");
        if (shoppingList.isEmpty()) {
            System.out.println("Empty");
            return;
        }

        for (Map.Entry<Integer, Integer> entry : shoppingList.entrySet()) {
            int id = entry.getKey();
            int qty = entry.getValue();
            Product p = db.findById(id);
            if (p == null) continue;
            double cost = p.getPrice() * qty;
            total += cost;
            System.out.printf("ID %d | %-15s | Qty: %d | Price: $%.2f | Subtotal: $%.2f%n",
                    id, p.getName(), qty, p.getPrice(), cost);
        }

        System.out.printf("Total: $%.2f%n", total);

        if (budget > 0) {
            System.out.printf("Budget: $%.2f | Remaining: $%.2f%n", budget, budget - total);
            if (total > budget) {
                System.out.println(" Warning: Over budget!");
            }
        }
    }

    private static double getTotalCost() {
        double total = 0;
        for (Map.Entry<Integer, Integer> entry : shoppingList.entrySet()) {
            Product p = db.findById(entry.getKey());
            if (p != null) {
                total += p.getPrice() * entry.getValue();
            }
        }
        return total;
    }
}
