public class Product {
    private int id;
    private String name;
    private String category;
    private double price;

    public Product(int id, String name, String category, double price) {
        this.id = id;
        this.name = name.toLowerCase();
        this.category = category.toLowerCase();
        this.price = price;
    }

    public Product(String name, String category, double price) {
        this(-1, name, category, price);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return id + " | " + name + " | " + category + " | $" + price;
    }
}
