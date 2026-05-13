import java.util.ArrayList;

public class VendingMachine {
    private ArrayList<Product> products = new ArrayList<>();
    private double cash = 0;
    private double lastChange = 0;
    private Product selectedProduct = null;

    public VendingMachine() {
        
        products.add(new Product(1, "Coke", 25.0, 5));
        products.add(new Product(2, "Chips", 20.0, 5));
        products.add(new Product(3, "Water", 15.0, 5));
        products.add(new Product(4, "Juice", 20.0, 5));
    }

    public void selectProduct(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                this.selectedProduct = p;
                break;
            }
        }
    }

    public void insertMoney(double amount) {
        this.cash += amount;
    }

    public String purchase() {
        if (selectedProduct == null) return "Select an item.";
        if (selectedProduct.getStock() <= 0) return "Out of stock.";
        if (cash < selectedProduct.getPrice()) return "Insufficient funds.";

        selectedProduct.reduceStock();
        lastChange = cash - selectedProduct.getPrice();
        cash = 0;
        return "Purchased " + selectedProduct.getName();
    }

    public void clear() {
        this.cash = 0;
        this.selectedProduct = null;
        this.lastChange = 0;
    }

    public double getCash() { return cash; }
    public double getLastChange() { return lastChange; }
    public Product getSelectedProduct() { return selectedProduct; }
}