package Supemarketbilling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//  window of the program
public class MyFrame extends JFrame {
    private static final Color CREAM = new Color(250, 240, 220);
    private static final Color ERROR_COLOR = new Color(180, 0, 0);
    private static final Color OK_COLOR = new Color(0, 110, 0);

    private Inventory inventory;
    private Purchase currentPurchase = new Purchase();

    // Stock
    private JTextArea stockArea = new JTextArea();

    // Manage items
    private JTextField nameField = new JTextField(15);
    private JTextField priceField = new JTextField(15);
    private JTextField quantityField = new JTextField(15);
    private JLabel manageStatus = new JLabel(" ");

    // New sale
    private JTextField saleNameField = new JTextField(15);
    private JTextField saleQuantityField = new JTextField(15);
    private JTextArea billArea = new JTextArea();
    private JLabel saleStatus = new JLabel(" ");

    public MyFrame(Inventory inventory) {
        this.inventory = inventory;

        setTitle("My Supermarket");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(CREAM);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Stock", buildStockTab());
        tabs.addTab("Manage items", buildManageTab());
        tabs.addTab("New sale", buildSaleTab());
        // show fresh numbers every time the user opens the Stock tab
        tabs.addChangeListener(e -> showStock());
        add(tabs, BorderLayout.CENTER);

        showStock();
        showBill();
        setVisible(true);
    }

    // building the tabs

    private JPanel buildStockTab() {
        stockArea.setEditable(false);
        stockArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> showStock());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREAM);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("ITEMS IN STOCK"), BorderLayout.NORTH);
        panel.add(new JScrollPane(stockArea), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildManageTab() {
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBackground(CREAM);
        form.add(new JLabel("Item name:"));
        form.add(nameField);
        form.add(new JLabel("Unit price:"));
        form.add(priceField);
        form.add(new JLabel("Quantity:"));
        form.add(quantityField);

        JButton addButton = new JButton("Add new item");
        JButton priceButton = new JButton("Change price");
        JButton increaseButton = new JButton("Increase quantity");
        JButton decreaseButton = new JButton("Decrease quantity");
        JButton checkButton = new JButton("Check item");
        JButton removeButton = new JButton("Remove item");

        addButton.addActionListener(e -> addNewItem());
        priceButton.addActionListener(e -> changePrice());
        increaseButton.addActionListener(e -> changeQuantity(true));
        decreaseButton.addActionListener(e -> changeQuantity(false));
        checkButton.addActionListener(e -> checkItem());
        removeButton.addActionListener(e -> removeItem());

        JPanel buttons = new JPanel(new GridLayout(2, 3, 8, 8));
        buttons.setBackground(CREAM);
        buttons.add(addButton);
        buttons.add(priceButton);
        buttons.add(checkButton);
        buttons.add(increaseButton);
        buttons.add(decreaseButton);
        buttons.add(removeButton);

        JLabel hint = new JLabel("<html>Add new item: name + price + quantity<br>"
                + "Change price: name + price &nbsp;|&nbsp; Increase/Decrease: name + quantity<br>"
                + "Check / Remove: name only</html>");

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setBackground(CREAM);
        top.add(hint, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CREAM);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        manageStatus.setVerticalAlignment(JLabel.TOP);
        panel.add(top, BorderLayout.NORTH);
        panel.add(manageStatus, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSaleTab() {
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.setBackground(CREAM);
        form.add(new JLabel("Item name:"));
        form.add(saleNameField);
        form.add(new JLabel("Quantity:"));
        form.add(saleQuantityField);

        JButton addButton = new JButton("Add to bill");
        JButton completeButton = new JButton("Complete sale");
        JButton cancelButton = new JButton("Cancel sale");
        addButton.addActionListener(e -> addToBill());
        completeButton.addActionListener(e -> completeSale());
        cancelButton.addActionListener(e -> cancelSale());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBackground(CREAM);
        buttons.add(addButton);
        buttons.add(completeButton);
        buttons.add(cancelButton);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setBackground(CREAM);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        billArea.setEditable(false);
        billArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CREAM);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(billArea), BorderLayout.CENTER);
        panel.add(saleStatus, BorderLayout.SOUTH);
        return panel;
    }

    // Manage items actions

    private void addNewItem() {
        try {
            String name = nameField.getText().trim();
            int price = readNumber(priceField, "Unit price");
            int quantity = readNumber(quantityField, "Quantity");
            inventory.add(new Stock(name, price, quantity));
            showStock();
            say(manageStatus, name + " added successfully", false);
            nameField.setText("");
            priceField.setText("");
            quantityField.setText("");
        } catch (InvalidInput e) {
            say(manageStatus, e.getMessage(), true);
        }
    }

    private void changePrice() {
        try {
            Stock item = inventory.findOrThrow(nameField.getText());
            item.setItemUnitPrice(readNumber(priceField, "Unit price"));
            showStock();
            say(manageStatus, "Price of " + item.getItemName() + " is now " + item.getItemUnitPrice(), false);
        } catch (InvalidInput e) {
            say(manageStatus, e.getMessage(), true);
        }
    }

    private void changeQuantity(boolean increase) {
        try {
            Stock item = inventory.findOrThrow(nameField.getText());
            int amount = readNumber(quantityField, "Quantity");
            if (increase) {
                item.restock(amount);
            } else {
                item.sell(amount);
            }
            showStock();
            say(manageStatus, item.getItemName() + " quantity is now " + item.getItemQuantity(), false);
        } catch (InvalidInput e) {
            say(manageStatus, e.getMessage(), true);
        }
    }

    private void checkItem() {
        try {
            Stock item = inventory.findOrThrow(nameField.getText());
            say(manageStatus, item.getItemName() + ": quantity " + item.getItemQuantity()
                    + ", unit price " + item.getItemUnitPrice(), false);
        } catch (InvalidInput e) {
            say(manageStatus, e.getMessage(), true);
        }
    }

    private void removeItem() {
        try {
            Stock item = inventory.findOrThrow(nameField.getText());
            if (currentPurchase.containsItem(item)) {
                throw new InvalidInput("That item is on the current bill. Complete or cancel the sale first");
            }
            inventory.remove(item.getItemName());
            showStock();
            say(manageStatus, item.getItemName() + " removed", false);
        } catch (InvalidInput e) {
            say(manageStatus, e.getMessage(), true);
        }
    }

    // New sale actions

    private void addToBill() {
        try {
            Stock item = inventory.findOrThrow(saleNameField.getText());
            int quantity = readNumber(saleQuantityField, "Quantity");
            currentPurchase.addItem(item, quantity);
            showBill();
            say(saleStatus, quantity + " x " + item.getItemName() + " added to the bill", false);
            saleNameField.setText("");
            saleQuantityField.setText("");
        } catch (InvalidInput e) {
            say(saleStatus, e.getMessage(), true);
        }
    }

    private void completeSale() {
        try {
            currentPurchase.complete();
            String receipt = currentPurchase.getBill();
            currentPurchase = new Purchase(); // fresh bill for the next customer
            billArea.setText(receipt + "\nThank you. Please come again.\n");
            showStock();
            say(saleStatus, "Sale completed", false);
        } catch (InvalidInput e) {
            say(saleStatus, e.getMessage(), true);
        }
    }

    private void cancelSale() {
        currentPurchase = new Purchase();
        showBill();
        say(saleStatus, "Sale cancelled. Stock was not changed", false);
    }

    // small helpers

    private void showStock() {
        stockArea.setText(inventory.describeAll());
    }

    private void showBill() {
        billArea.setText(currentPurchase.getBill());
    }

    // shows a message in a status label (red for errors, green for success)
    private void say(JLabel label, String message, boolean isError) {
        label.setForeground(isError ? ERROR_COLOR : OK_COLOR);
        label.setText(message);
    }

    // reads a whole number from a text field, with a clear error if it is not one
    private int readNumber(JTextField field, String fieldName) throws InvalidInput {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            throw new InvalidInput(fieldName + " is required");
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new InvalidInput(fieldName + " must be a whole number");
        }
    }
}
