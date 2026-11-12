package Supemarketbilling;

//sales
public class Purchase {
    private static final int MAX_BUYS = 10;// maximum number of items on can buy
    private static int purchaseNumber = 0; // shared counter for bill numbers

    private String purchaseID = "(pending)";
    private final Stock[] itemsBought = new Stock[MAX_BUYS];
    private final int[] quantityBought = new int[MAX_BUYS];
    private int numberOfItemsBought = 0;
    private boolean completed = false;

    public int getNumberOfItemsBought() {
        return numberOfItemsBought;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Checks if this item is already on the bill
    public boolean containsItem(Stock item) {
        return findLine(item) >= 0;
    }

    // position of the item on the bill, or -1 if it is not there
    private int findLine(Stock item) {
        for (int i = 0; i < numberOfItemsBought; i++) {
            if (itemsBought[i] == item) {
                return i;
            }
        }
        return -1;
    }

    // adds an item to the bill.
    public void addItem(Stock item, int quantity) throws InvalidInput {
        if (completed) {
            throw new InvalidInput("This sale is already completed");
        }
        if (quantity <= 0) {
            throw new InvalidInput("Quantity must be greater than 0");
        }
        int line = findLine(item);
        int alreadyOnBill = 0;
        if (line >= 0) {
            alreadyOnBill = quantityBought[line];
        }
        if (!item.hasEnough(alreadyOnBill + quantity)) {
            int stillAvailable = item.getItemQuantity() - alreadyOnBill;
            throw new InvalidInput("Not enough " + item.getItemName() + " in stock (" + stillAvailable + " available)");
        }
        if (line >= 0) {
            quantityBought[line] += quantity; // same item again, add to its line
        } else {
            if (numberOfItemsBought >= itemsBought.length) {
                throw new InvalidInput("A bill can have at most " + MAX_BUYS + " different items");
            }
            itemsBought[numberOfItemsBought] = item;
            quantityBought[numberOfItemsBought] = quantity;
            numberOfItemsBought++;
        }
    }

    // total cost of everything on the bill
    public int getTotal() {
        int total = 0;
        for (int i = 0; i < numberOfItemsBought; i++) {
            total += itemsBought[i].calculatePrice(quantityBought[i]);
        }
        return total;
    }

    // finishes the sale
    public void complete() throws InvalidInput {
        if (completed) {
            throw new InvalidInput("This sale is already completed");
        }
        if (numberOfItemsBought == 0) {
            throw new InvalidInput("The bill is empty");
        }
        for (int i = 0; i < numberOfItemsBought; i++) {
            if (!itemsBought[i].hasEnough(quantityBought[i])) {
                throw new InvalidInput("Not enough " + itemsBought[i].getItemName() + " in stock");
            }
        }
        for (int i = 0; i < numberOfItemsBought; i++) {
            itemsBought[i].sell(quantityBought[i]);
        }
        purchaseNumber++;
        purchaseID = String.valueOf(purchaseNumber);
        completed = true;
    }

    // the bill , each item on one line
    public String getBill() {
        String bill = "MY SUPERMARKET\n";
        bill += "Bill for purchase: " + purchaseID + "\n\n";
        if (numberOfItemsBought == 0) {
            bill += "(no items yet)\n";
        }
        for (int i = 0; i < numberOfItemsBought; i++) {
            Stock item = itemsBought[i];
            bill += (i + 1) + ". " + item.getItemName()
                    + "|  x " + quantityBought[i]
                    + "  @ " + item.getItemUnitPrice()
                    + "  = " + item.calculatePrice(quantityBought[i]) + "\n";
        }
        bill += "\nTotal price: " + getTotal() + "\n";
        return bill;
    }
}
