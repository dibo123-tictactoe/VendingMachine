import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VendingMachineGUI extends JFrame {
    private VendingMachine machine = new VendingMachine();

    // Arrays to hold labels so we can update them after a purchase
    private JLabel[] stockLabels = new JLabel[4];
    
    // Transaction Labels
    private JLabel selectedLbl = new JLabel("None");
    private JLabel priceLbl = new JLabel("₱0");
    private JLabel cashLbl = new JLabel("₱0");
    private JLabel changeLbl = new JLabel("₱0");
    private JLabel messageLbl = new JLabel("Please select an item.", JLabel.CENTER);
    private JTextField moneyInput = new JTextField("0", 5);

    public VendingMachineGUI() {
        setTitle("Vending Machine - Group 3 (Modified)");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        
        JLabel title = new JLabel("VENDING MACHINE - GROUP 3 (MODIFIED)", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel productGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        productGrid.setBorder(BorderFactory.createTitledBorder("SELECT AN ITEM"));
        
       
        productGrid.add(createProductCard(1, "Coke", 25, "coke.png", 0));
        productGrid.add(createProductCard(2, "Chips", 20, "chips.png", 1));
        productGrid.add(createProductCard(3, "Water", 15, "water.png", 2));
        productGrid.add(createProductCard(4, "Juice", 20, "juice.png", 3));

        
        JPanel transPanel = new JPanel(new GridLayout(5, 2, 10, 30));
        transPanel.setBorder(BorderFactory.createTitledBorder("TRANSACTION"));
        transPanel.add(new JLabel("Selected Item:")); transPanel.add(selectedLbl);
        transPanel.add(new JLabel("Price:"));         transPanel.add(priceLbl);
        transPanel.add(new JLabel("Enter Money:"));   transPanel.add(moneyInput);
        transPanel.add(new JLabel("Cash:"));          transPanel.add(cashLbl);
        transPanel.add(new JLabel("Change:"));        transPanel.add(changeLbl);

        
        JPanel coinGrid = new JPanel(new GridLayout(4, 2, 5, 5));
        int[] denominations = {5, 10, 20, 50, 100, 200, 500, 1000};
        for (int val : denominations) {
            JButton b = new JButton(String.valueOf(val));
            try {
                // This adds the pictures to the coins as seen in image_2bc8df.png
                ImageIcon icon = new ImageIcon("coin" + val + ".png");
                Image img = icon.getImage().getScaledInstance(70, 40, Image.SCALE_SMOOTH);
                b.setIcon(new ImageIcon(img));
                b.setHorizontalTextPosition(SwingConstants.RIGHT);
            } catch (Exception e) {} 
            
            b.addActionListener(e -> {
                machine.insertMoney(val);
                cashLbl.setText("₱" + (int)machine.getCash());
                moneyInput.setText(String.valueOf((int)machine.getCash()));
            });
            coinGrid.add(b);
        }
        JPanel coinWrapper = new JPanel(new BorderLayout());
        coinWrapper.setBorder(BorderFactory.createTitledBorder("COINS"));
        coinWrapper.add(coinGrid, BorderLayout.CENTER);

        centerPanel.add(productGrid);
        centerPanel.add(transPanel);
        centerPanel.add(coinWrapper);
        add(centerPanel, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageLbl.setForeground(Color.RED);
        messageLbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buyBtn = new JButton("BUY");
        buyBtn.setBackground(new Color(144, 238, 144));
        
        JButton clearBtn = new JButton("CLEAR");
        clearBtn.setBackground(new Color(255, 239, 161));
        
        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBackground(new Color(255, 182, 193));

        
        buyBtn.addActionListener(e -> {
            String result = machine.purchase();
            messageLbl.setText(result);
            cashLbl.setText("₱" + (int)machine.getCash());
            changeLbl.setText("₱" + (int)machine.getLastChange());
            
            
            updateStockDisplay();
        });

        clearBtn.addActionListener(e -> {
            machine.clear();
            cashLbl.setText("₱0");
            changeLbl.setText("₱0");
            moneyInput.setText("0");
            messageLbl.setText("Cleared.");
        });

        controls.add(buyBtn); controls.add(clearBtn); controls.add(cancelBtn);
        bottomPanel.add(messageLbl, BorderLayout.CENTER);
        bottomPanel.add(controls, BorderLayout.EAST);
        bottomPanel.setPreferredSize(new Dimension(1100, 80));
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    
    private JPanel createProductCard(int id, String name, int price, String imgPath, int index) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        p.setBackground(Color.WHITE);

        JLabel imgL = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            imgL.setIcon(new ImageIcon(img));
        } catch (Exception e) { imgL.setText("No Image"); }
        imgL.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameL = new JLabel(name);
        nameL.setFont(new Font("Arial", Font.BOLD, 16));
        nameL.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceL = new JLabel("₱" + price);
        priceL.setForeground(Color.RED);
        priceL.setFont(new Font("Arial", Font.BOLD, 18));
        priceL.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        stockLabels[index] = new JLabel("Stock: 10");
        stockLabels[index].setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(Box.createVerticalStrut(10));
        p.add(imgL);
        p.add(nameL);
        p.add(priceL);
        p.add(stockLabels[index]);

        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                machine.selectProduct(id);
                selectedLbl.setText(name);
                priceLbl.setText("₱" + price);
            }
        });
        return p;
    }

    
    private void updateStockDisplay() {
        int[] ids = {1, 2, 3, 4};
        for (int i = 0; i < 4; i++) {
            machine.selectProduct(ids[i]);
            if (machine.getSelectedProduct() != null) {
                stockLabels[i].setText("Stock: " + machine.getSelectedProduct().getStock());
            }
        }
    }

    public static void main(String[] args) {
        new VendingMachineGUI();
    }
}