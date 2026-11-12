package Supemarketbilling;

import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        try {
            inventory.add(new Stock("sweets", 5000, 89));
            inventory.add(new Stock("bread", 4500, 70));
            inventory.add(new Stock("sugar", 6000, 90));
        } catch (InvalidInput e) {
            System.out.println("Could not load starting stock: " + e.getMessage());
        }
        // Swing windows should be created on the Swing (event) thread
        SwingUtilities.invokeLater(() -> new MyFrame(inventory));
    }
}
