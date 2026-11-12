package Supemarketbilling;

// The Inventory owns the array of Stock objects and manages it:
public class Inventory {
    private static final int MAX_ITEMS = 20;// maximum number of items

    private final Stock[] items = new Stock[MAX_ITEMS];
    private int numberOfItems = 0; // how many slots are filled

    // checks if inventory is full
    public boolean isFull() {
        return numberOfItems >= items.length;
    }

    // gets number of items
    public int getNumberOfItems() {
        return numberOfItems;
    }

    // gets specific item
    public Stock getItemAt(int index) {
        return items[index];
    }

    // adds a new item, refusing duplicates and prevents adding if stock is full
    public void add(Stock item) throws InvalidInput {
        if (isFull()) {
            throw new InvalidInput("Stock is full (maximum " + MAX_ITEMS + " different items)");
        }
        if (find(item.getItemName()) != null) {
            throw new InvalidInput("Item already exists in stock");
        }
        items[numberOfItems] = item;
        numberOfItems++;
    }

    // returns the item with that name, or null when item not found
    public Stock find(String name) {
        for (int i = 0; i < numberOfItems; i++) {
            if (items[i].hasName(name)) {
                return items[i];
            }
        }
        return null;
    }

    // find with clear error for null values
    public Stock findOrThrow(String name) throws InvalidInput {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInput("Item name is required");
        }
        Stock item = find(name);
        if (item == null) {
            throw new InvalidInput("Item not found: " + name.trim());
        }
        return item;
    }

    // removes an item and removes the gap
    public void remove(String name) throws InvalidInput {
        Stock item = findOrThrow(name);
        int position = 0;
        while (items[position] != item) {
            position++;
        }
        for (int i = position; i < numberOfItems - 1; i++) {
            items[i] = items[i + 1];
        }
        items[numberOfItems - 1] = null;
        numberOfItems--;
    }

    // lists items in stock
    public String describeAll() {
        if (numberOfItems == 0) {
            return "No items in stock.";
        }
        String text = "";
        for (int i = 0; i < numberOfItems; i++) {
            text += (i + 1) + ". " + items[i].describe() + "\n";
        }
        return text;
    }
}
