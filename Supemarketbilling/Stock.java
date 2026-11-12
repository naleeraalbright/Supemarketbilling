package Supemarketbilling;

//items in the supermarket and their details
public class Stock {
    private String itemName;
    private int itemQuantity;
    private int itemUnitPrice;

    // constructor
    public Stock(String itemName, int itemUnitPrice, int itemQuantity) throws InvalidInput {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new InvalidInput("Item name cannot be empty");
        }
        if (itemUnitPrice <= 0) {
            throw new InvalidInput("Unit price must be greater than 0");
        }
        if (itemQuantity < 0) {
            throw new InvalidInput("Quantity cannot be negative");
        }
        this.itemName = itemName.trim();
        this.itemUnitPrice = itemUnitPrice;
        this.itemQuantity = itemQuantity;
    }

    // checks if an item in the stock has the name given as a parameter
    public boolean hasName(String name) {
        return name != null && itemName.equalsIgnoreCase(name.trim());
    }

    // checks if there are enough units
    public boolean hasEnough(int amount) {
        return amount > 0 && amount <= itemQuantity;
    }

    // calculates price
    public int calculatePrice(int amount) {
        return itemUnitPrice * amount;
    }

    // causes quantity changes during sells
    public void sell(int amount) throws InvalidInput {
        if (amount <= 0) {
            throw new InvalidInput("Quantity must be greater than 0");
        }
        if (amount > itemQuantity) {
            throw new InvalidInput("not enough items in stock");
        }
        itemQuantity -= amount;
    }

    // adds stock quantity
    public void restock(int amount) throws InvalidInput {
        if (amount <= 0) {
            throw new InvalidInput("Quantity must be greater than 0");
        }
        itemQuantity += amount;
    }

    // Describes a stock Item
    public String describe() {
        return String.format("%-15s Qty: %-6d Price: %d", itemName, itemQuantity, itemUnitPrice);
    }

    // getters
    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getItemUnitPrice() {
        return itemUnitPrice;
    }

    // setters
    public void setItemUnitPrice(int itemUnitPrice) throws InvalidInput {
        if (itemUnitPrice <= 0) {
            throw new InvalidInput("Unit price must be greater than 0");
        }
        this.itemUnitPrice = itemUnitPrice;
    }
}
