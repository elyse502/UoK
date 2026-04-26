package appjava.Systems;

/**
 * @author Danny Idukundatwese
 * Module: Advanced Java Programming
 * RegNo: 2405001032
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class QuotingSystem extends JFrame {

    private JTextField descriptionInput, unitPriceInput, quantityInput, marginInput, totalPriceInput, exchangeRateInput, tinInput, customerAddressInput;
    private JRadioButton withholdingTaxYes, withholdingTaxNo;
    private JComboBox<String> itemTypeSelector, currencySelector;
    private JButton calculateBtn, exportBtn;
    private DefaultTableModel tableModel;
    private JTable quoteTable;

    private static final String TERMS_AND_CONDITIONS = 
            "Terms and Conditions: \n" +
            "1. Payment should be made against delivery.\n" +
            "2. Prices are valid for 30 days only.\n" +
            "3. Any changes to the order must be made in writing.\n" +
            "4. Cost is VAT Inclusive 18%.";

    public QuotingSystem() {
        setTitle("Quoting System");
        setSize(1550, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(5, 5));
        main.setBackground(Color.GRAY);
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setContentPane(main);

        main.add(buildTitlePanel(),  BorderLayout.NORTH);
        main.add(buildFormPanel(),   BorderLayout.WEST);
        main.add(buildTablePanel(),  BorderLayout.CENTER);
    }

    private JPanel buildTitlePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        JLabel lbl = new JLabel("QUOTING SYSTEM");
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.BLUE);
        p.add(lbl);
        return p;
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridLayout(8, 2, 5, 5));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        descriptionInput = new JTextField();
        unitPriceInput = new JTextField();
        quantityInput = new JTextField();
        marginInput = new JTextField();
        totalPriceInput = new JTextField();
        totalPriceInput.setEditable(false);

        exchangeRateInput = new JTextField("1500.0"); // Default exchange rate

        withholdingTaxYes = new JRadioButton("Yes");
        withholdingTaxNo = new JRadioButton("No", true);
        ButtonGroup withholdingTaxGroup = new ButtonGroup();
        withholdingTaxGroup.add(withholdingTaxYes);
        withholdingTaxGroup.add(withholdingTaxNo);

        itemTypeSelector = new JComboBox<>(new String[]{"Product", "Service"});
        currencySelector = new JComboBox<>(new String[]{"RWF", "USD"});

        calculateBtn = new JButton("Calculate Total Price");
        calculateBtn.setBackground(Color.BLUE);
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFont(new Font("Arial", Font.BOLD, 12));
        calculateBtn.addActionListener(e -> calculatePrice());

        exportBtn = new JButton("Export Quote to Notepad");
        exportBtn.setBackground(Color.BLUE);
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 12));
        exportBtn.addActionListener(e -> exportQuoteToNotepad());

        p.add(new JLabel("Customer TIN:"));         
        p.add(tinInput = new JTextField());
        
        p.add(new JLabel("Customer Address:"));     
        p.add(customerAddressInput = new JTextField());
        
        p.add(new JLabel("Item Description:"));
        p.add(descriptionInput);
        
        p.add(new JLabel("Disti Unit Price (USD):"));
        p.add(unitPriceInput);
        
        p.add(new JLabel("Quantity:"));
        p.add(quantityInput);
        
        p.add(new JLabel("Margin (%):"));
        p.add(marginInput);
        
        p.add(new JLabel("Exchange Rate (USD to RWF):"));
        p.add(exchangeRateInput);
        
        p.add(new JLabel("Type:"));
        p.add(itemTypeSelector);
        
        p.add(new JLabel("Withholding Tax:"));
        p.add(withholdingTaxYes);
        
        p.add(new JLabel(""));
        p.add(withholdingTaxNo);
        
        p.add(new JLabel("Currency:"));
        p.add(currencySelector);
        
        p.add(new JLabel("Total Price:"));
        p.add(totalPriceInput);
        
        p.add(new JPanel());
        p.add(calculateBtn);
        
        p.add(new JPanel());
        p.add(exportBtn);

        return p;
    }

    private JPanel buildTablePanel() {
        String[] cols = {"Customer", "TIN", "Description", "Unit Price", "Quantity", "Margin (%)", "Profit", "Total"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        quoteTable = new JTable(tableModel);
        quoteTable.setFillsViewportHeight(true);
        quoteTable.setFont(new Font("Arial", Font.PLAIN, 14));
        quoteTable.setRowHeight(25);
        quoteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(quoteTable);
        scroll.setPreferredSize(new Dimension(700, 430));

        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }

    private void calculatePrice() {
        try {
            String customer = customerAddressInput.getText().trim();
            String customerTin = tinInput.getText().trim();
            String description = descriptionInput.getText().trim();
            double unitPrice = Double.parseDouble(unitPriceInput.getText().trim());
            int quantity = Integer.parseInt(quantityInput.getText().trim());
            double margin = Double.parseDouble(marginInput.getText().trim());
            double exchangeRate = Double.parseDouble(exchangeRateInput.getText().trim());
            double finalMargin = margin;

            double distiSubTotal = unitPrice * quantity;
            margin /= 100; 
            double customerUnit = unitPrice / (1 - margin);
            double subtotal = customerUnit * quantity;
//            double grossProfit = (margin / 100) * subtotal;
            double grossProfit = subtotal - distiSubTotal;
            
            double total;

            if (currencySelector.getSelectedItem().equals("USD")) {
                total = distiSubTotal + grossProfit;
            } else {
                total = subtotal * exchangeRate;
            }

            if (withholdingTaxYes.isSelected()) {
                customerUnit /= (1 - 0.15);
                subtotal = customerUnit * quantity;
                total = subtotal;
//                total *= 0.85; // Deduct 15% withholding tax
            }

            total = subtotal * 1.18; // Add 18% VAT

            totalPriceInput.setText(String.format("%.2f", total));

            // Add quote to table
            tableModel.addRow(new Object[]{customer, customerTin, description, unitPrice, quantity, finalMargin, String.format("%.2f", grossProfit), String.format("%.2f", total)});
            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numerical values.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearForm() {
        descriptionInput.setText("");
        unitPriceInput.setText("");
        quantityInput.setText("");
        marginInput.setText("");
        totalPriceInput.setText("");
        exchangeRateInput.setText("1.0");
        withholdingTaxYes.setSelected(false);
        withholdingTaxNo.setSelected(true);
        itemTypeSelector.setSelectedIndex(0);
        currencySelector.setSelectedIndex(0);
        tinInput.setText("");
        customerAddressInput.setText("");
    }

    private void exportQuoteToNotepad() {
        int row = quoteTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a quote line to export.",
                    "No Quote Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String quoteDetails = createQuoteString(row);
        try {
            File tmp = File.createTempFile("Quote_", ".txt");
            tmp.deleteOnExit();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
                bw.write(quoteDetails);
            }

            ProcessBuilder pb;
            pb = new ProcessBuilder("notepad.exe", tmp.getAbsolutePath());
            pb.start();

            JOptionPane.showMessageDialog(this,
                    "Quote exported to Notepad!\nFile: " + tmp.getName(),
                    "Exported", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not open Notepad:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String createQuoteString(int row) {
        StringBuilder quote = new StringBuilder();
        quote.append("Customer: ").append(tableModel.getValueAt(row, 0)).append("\n");
        quote.append("TIN: ").append(tableModel.getValueAt(row, 1)).append("\n");
        quote.append("Description: ").append(tableModel.getValueAt(row, 2)).append("\n");
        quote.append("Unit Price: ").append(tableModel.getValueAt(row, 3)).append(" $\n");
        quote.append("Quantity: ").append(tableModel.getValueAt(row, 4)).append("\n");
        quote.append("Margin: ").append(tableModel.getValueAt(row, 5)).append("%\n");
        quote.append("Profit: ").append(tableModel.getValueAt(row, 6)).append(" $\n");
        quote.append("Total Price: ").append(tableModel.getValueAt(row, 7)).append(" $");
        quote.append(TERMS_AND_CONDITIONS);
        return quote.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuotingSystem().setVisible(true));
    }
}
