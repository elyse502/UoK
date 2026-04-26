/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appjava.Systems;

/**
 *
 * @author idtda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockManagementSystem extends JFrame {

    // Form fields
    private JTextField itemNameInput, itemCodeInput, quantityInput, unitPriceInput, supplierInput, searchInput;
    private JComboBox<String> categorySelector, transactionTypeSelector;
    private JButton addItemBtn, stockInBtn, stockOutBtn, deleteBtn, exportBtn, searchBtn, clearSearchBtn;
    private JLabel totalItemsLabel, totalValueLabel, lowStockLabel;

    // Tables
    private DefaultTableModel inventoryModel, transactionModel;
    private JTable inventoryTable, transactionTable;

    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final String[] CATEGORIES = {"Electronics", "Stationery", "Furniture", "IT Equipment", "Consumables", "Other"};

    public StockManagementSystem() {
        setTitle("Stock Management System");
        setSize(1400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(5, 5));
        main.setBackground(new Color(45, 45, 48));
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setContentPane(main);

        main.add(buildTitlePanel(), BorderLayout.NORTH);

        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(), buildRightPanel());
        centerSplit.setDividerLocation(320);
        centerSplit.setOpaque(false);
        main.add(centerSplit, BorderLayout.CENTER);

        main.add(buildSummaryPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildTitlePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));

        JLabel lbl = new JLabel("STOCK MANAGEMENT SYSTEM");
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(new Color(0, 153, 255));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(60, 60, 63));
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 153, 255)),
                "Item Details", 0, 0,
                new Font("Arial", Font.BOLD, 12), new Color(0, 153, 255)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        itemCodeInput    = new JTextField();
        itemNameInput    = new JTextField();
        categorySelector = new JComboBox<>(CATEGORIES);
        quantityInput    = new JTextField();
        unitPriceInput   = new JTextField();
        supplierInput    = new JTextField();

        String[][] fields = {
            {"Item Code:", null},
            {"Item Name:", null},
            {"Category:", "combo"},
            {"Quantity:", null},
            {"Unit Price (RWF):", null},
            {"Supplier:", null}
        };

        JComponent[] inputs = {itemCodeInput, itemNameInput, categorySelector,
                               quantityInput, unitPriceInput, supplierInput};

        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(fields[i][0]);
            lbl.setForeground(Color.LIGHT_GRAY);
            lbl.setFont(new Font("Arial", Font.PLAIN, 11));
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1.0;
            styleInput(inputs[i]);
            p.add(inputs[i], gbc);
        }

        // Transaction type
        gbc.gridx = 0; gbc.gridy = fields.length; gbc.weightx = 0;
        JLabel txLbl = new JLabel("Transaction:");
        txLbl.setForeground(Color.LIGHT_GRAY);
        txLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(txLbl, gbc);

        transactionTypeSelector = new JComboBox<>(new String[]{"Stock In", "Stock Out"});
        styleInput(transactionTypeSelector);
        gbc.gridx = 1; gbc.weightx = 1.0;
        p.add(transactionTypeSelector, gbc);

        // Buttons
        addItemBtn  = makeButton("Add New Item",   new Color(0, 128, 0));
        stockInBtn  = makeButton("Stock In",        new Color(0, 102, 204));
        stockOutBtn = makeButton("Stock Out",       new Color(180, 80, 0));
        deleteBtn   = makeButton("Delete Item",     new Color(160, 30, 30));
        exportBtn   = makeButton("Export Report",   new Color(80, 80, 80));

        addItemBtn.addActionListener(e  -> addNewItem());
        stockInBtn.addActionListener(e  -> adjustStock(true));
        stockOutBtn.addActionListener(e -> adjustStock(false));
        deleteBtn.addActionListener(e   -> deleteItem());
        exportBtn.addActionListener(e   -> exportReport());

        int btnRow = fields.length + 1;
        JButton[] btns = {addItemBtn, stockInBtn, stockOutBtn, deleteBtn, exportBtn};
        for (int i = 0; i < btns.length; i++) {
            gbc.gridx = 0; gbc.gridy = btnRow + i;
            gbc.gridwidth = 2; gbc.weightx = 1.0;
            p.add(btns[i], gbc);
            gbc.gridwidth = 1;
        }

        // Filler
        gbc.gridx = 0; gbc.gridy = btnRow + btns.length;
        gbc.gridwidth = 2; gbc.weighty = 1.0;
        p.add(new JPanel() {{ setOpaque(false); }}, gbc);

        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);
        searchInput = new JTextField(20);
        styleInput(searchInput);
        searchBtn = makeButton("Search", new Color(0, 102, 204));
        clearSearchBtn = makeButton("Clear", new Color(80, 80, 80));
        searchBtn.addActionListener(e -> searchInventory());
        clearSearchBtn.addActionListener(e -> clearSearch());
        searchPanel.add(new JLabel("Search:") {{ setForeground(Color.LIGHT_GRAY); }});
        searchPanel.add(searchInput);
        searchPanel.add(searchBtn);
        searchPanel.add(clearSearchBtn);
        p.add(searchPanel, BorderLayout.NORTH);

        // Tabbed tables
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(new Color(60, 60, 63));
        tabs.setForeground(Color.LIGHT_GRAY);
        tabs.addTab("Inventory", buildInventoryTablePanel());
        tabs.addTab("Transaction Log", buildTransactionTablePanel());
        p.add(tabs, BorderLayout.CENTER);

        return p;
    }

    private JScrollPane buildInventoryTablePanel() {
        String[] cols = {"Code", "Item Name", "Category", "Quantity", "Unit Price (RWF)", "Total Value (RWF)", "Supplier", "Status"};
        inventoryModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        inventoryTable = new JTable(inventoryModel);
        styleTable(inventoryTable);

        // Color low-stock rows
        inventoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    try {
                        int qty = Integer.parseInt(t.getValueAt(row, 3).toString());
                        c.setBackground(qty <= LOW_STOCK_THRESHOLD
                                ? new Color(80, 30, 30) : new Color(50, 50, 53));
                    } catch (Exception ex) {
                        c.setBackground(new Color(50, 50, 53));
                    }
                    c.setForeground(Color.LIGHT_GRAY);
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(inventoryTable);
        sp.getViewport().setBackground(new Color(50, 50, 53));
        return sp;
    }

    private JScrollPane buildTransactionTablePanel() {
        String[] cols = {"Date/Time", "Item Code", "Item Name", "Type", "Quantity", "Unit Price", "Total", "Remarks"};
        transactionModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        transactionTable = new JTable(transactionModel);
        styleTable(transactionTable);

        JScrollPane sp = new JScrollPane(transactionTable);
        sp.getViewport().setBackground(new Color(50, 50, 53));
        return sp;
    }

    private JPanel buildSummaryPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        p.setBackground(new Color(35, 35, 38));

        totalItemsLabel = makeSummaryLabel("Total Items: 0");
        totalValueLabel = makeSummaryLabel("Total Stock Value: 0 RWF");
        lowStockLabel   = makeSummaryLabel("Low Stock Alerts: 0");
        lowStockLabel.setForeground(new Color(255, 100, 100));

        p.add(totalItemsLabel);
        p.add(new JSeparator(JSeparator.VERTICAL) {{ setPreferredSize(new Dimension(1, 20)); }});
        p.add(totalValueLabel);
        p.add(new JSeparator(JSeparator.VERTICAL) {{ setPreferredSize(new Dimension(1, 20)); }});
        p.add(lowStockLabel);
        return p;
    }

    // ─── Business logic ───────────────────────────────────────────────────────

    private void addNewItem() {
        if (!validateInputs(true)) return;

        String code    = itemCodeInput.getText().trim();
        String name    = itemNameInput.getText().trim();
        String cat     = (String) categorySelector.getSelectedItem();
        String supplier = supplierInput.getText().trim();

        // Check for duplicate code
        for (int i = 0; i < inventoryModel.getRowCount(); i++) {
            if (inventoryModel.getValueAt(i, 0).toString().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this, "Item code already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            int    qty   = Integer.parseInt(quantityInput.getText().trim());
            double price = Double.parseDouble(unitPriceInput.getText().trim());
            double total = qty * price;
            String status = qty <= LOW_STOCK_THRESHOLD ? "LOW STOCK" : "In Stock";

            inventoryModel.addRow(new Object[]{code, name, cat, qty,
                    String.format("%.0f", price),
                    String.format("%.0f", total),
                    supplier, status});

            logTransaction("Stock In (New)", code, name, qty, price, "Initial stock entry");
            updateSummary();
            clearForm();
        } catch (NumberFormatException ex) {
            showInputError();
        }
    }

    private void adjustStock(boolean isStockIn) {
        int row = inventoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item from the inventory table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (quantityInput.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the quantity.", "Missing Quantity", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int adjQty = Integer.parseInt(quantityInput.getText().trim());
            int curQty = Integer.parseInt(inventoryModel.getValueAt(row, 3).toString());
            double price = Double.parseDouble(inventoryModel.getValueAt(row, 4).toString());

            if (!isStockIn && adjQty > curQty) {
                JOptionPane.showMessageDialog(this, "Cannot remove more stock than available.\nAvailable: " + curQty, "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newQty = isStockIn ? curQty + adjQty : curQty - adjQty;
            double newTotal = newQty * price;
            String status = newQty <= LOW_STOCK_THRESHOLD ? "LOW STOCK" : "In Stock";

            inventoryModel.setValueAt(newQty, row, 3);
            inventoryModel.setValueAt(String.format("%.0f", newTotal), row, 5);
            inventoryModel.setValueAt(status, row, 7);

            String code = inventoryModel.getValueAt(row, 0).toString();
            String name = inventoryModel.getValueAt(row, 1).toString();
            String type = isStockIn ? "Stock In" : "Stock Out";
            logTransaction(type, code, name, adjQty, price, "Manual adjustment");

            if (!isStockIn && newQty <= LOW_STOCK_THRESHOLD) {
                JOptionPane.showMessageDialog(this,
                        "Warning: " + name + " is running low!\nRemaining quantity: " + newQty,
                        "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
            }

            updateSummary();
            quantityInput.setText("");
        } catch (NumberFormatException ex) {
            showInputError();
        }
    }

    private void deleteItem() {
        int row = inventoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = inventoryModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete \"" + name + "\" from inventory?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            inventoryModel.removeRow(row);
            updateSummary();
        }
    }

    private void searchInventory() {
        String query = searchInput.getText().trim().toLowerCase();
        if (query.isEmpty()) { clearSearch(); return; }

        for (int row = 0; row < inventoryModel.getRowCount(); row++) {
            boolean match = false;
            for (int col = 0; col < inventoryModel.getColumnCount(); col++) {
                if (inventoryModel.getValueAt(row, col).toString().toLowerCase().contains(query)) {
                    match = true;
                    break;
                }
            }
            inventoryTable.getSelectionModel().setSelectionInterval(row, row);
            if (match) { inventoryTable.scrollRectToVisible(inventoryTable.getCellRect(row, 0, true)); break; }
        }
    }

    private void clearSearch() {
        searchInput.setText("");
        inventoryTable.clearSelection();
    }

    private void logTransaction(String type, String code, String name, int qty, double price, String remarks) {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double total = qty * price;
        transactionModel.insertRow(0, new Object[]{
                dateTime, code, name, type, qty,
                String.format("%.0f", price),
                String.format("%.0f", total),
                remarks
        });
    }

    private void updateSummary() {
        int totalItems = inventoryModel.getRowCount();
        double totalValue = 0;
        int lowStockCount = 0;

        for (int i = 0; i < inventoryModel.getRowCount(); i++) {
            try {
                totalValue += Double.parseDouble(inventoryModel.getValueAt(i, 5).toString());
                int qty = Integer.parseInt(inventoryModel.getValueAt(i, 3).toString());
                if (qty <= LOW_STOCK_THRESHOLD) lowStockCount++;
            } catch (Exception ignored) {}
        }

        totalItemsLabel.setText("Total Items: " + totalItems);
        totalValueLabel.setText(String.format("Total Stock Value: %,.0f RWF", totalValue));
        lowStockLabel.setText("Low Stock Alerts: " + lowStockCount);
        lowStockLabel.setForeground(lowStockCount > 0 ? new Color(255, 100, 100) : Color.LIGHT_GRAY);
    }

    private void exportReport() {
        if (inventoryModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No inventory data to export.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("STOCK MANAGEMENT REPORT\n");
        sb.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        sb.append("=".repeat(80)).append("\n\n");

        sb.append(String.format("%-10s %-25s %-15s %8s %15s %15s%n",
                "Code", "Item Name", "Category", "Qty", "Unit Price", "Total Value"));
        sb.append("-".repeat(90)).append("\n");

        double grandTotal = 0;
        for (int i = 0; i < inventoryModel.getRowCount(); i++) {
            double total = Double.parseDouble(inventoryModel.getValueAt(i, 5).toString());
            grandTotal += total;
            sb.append(String.format("%-10s %-25s %-15s %8s %15s %15s  %s%n",
                    inventoryModel.getValueAt(i, 0),
                    inventoryModel.getValueAt(i, 1),
                    inventoryModel.getValueAt(i, 2),
                    inventoryModel.getValueAt(i, 3),
                    inventoryModel.getValueAt(i, 4),
                    inventoryModel.getValueAt(i, 5),
                    inventoryModel.getValueAt(i, 7)));
        }

        sb.append("=".repeat(90)).append("\n");
        sb.append(String.format("TOTAL STOCK VALUE: %,.0f RWF%n", grandTotal));

        try {
            File tmp = File.createTempFile("StockReport_", ".txt");
            tmp.deleteOnExit();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
                bw.write(sb.toString());
            }

            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            if (os.contains("win"))      pb = new ProcessBuilder("notepad.exe", tmp.getAbsolutePath());
            else if (os.contains("mac")) pb = new ProcessBuilder("open", "-e", tmp.getAbsolutePath());
            else                         pb = new ProcessBuilder(findLinuxEditor(), tmp.getAbsolutePath());
            pb.start();

            JOptionPane.showMessageDialog(this, "Report exported: " + tmp.getName(), "Exported", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Export failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private boolean validateInputs(boolean requireAll) {
        if (itemCodeInput.getText().trim().isEmpty() ||
            itemNameInput.getText().trim().isEmpty() ||
            quantityInput.getText().trim().isEmpty()  ||
            unitPriceInput.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        itemCodeInput.setText("");
        itemNameInput.setText("");
        quantityInput.setText("");
        unitPriceInput.setText("");
        supplierInput.setText("");
        categorySelector.setSelectedIndex(0);
        transactionTypeSelector.setSelectedIndex(0);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.setBackground(new Color(50, 50, 53));
        table.setForeground(Color.LIGHT_GRAY);
        table.setGridColor(new Color(70, 70, 73));
        table.getTableHeader().setBackground(new Color(0, 80, 160));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionBackground(new Color(0, 102, 204));
        table.setSelectionForeground(Color.WHITE);
    }

    private void styleInput(JComponent c) {
        c.setBackground(new Color(50, 50, 53));
        c.setForeground(Color.LIGHT_GRAY);
        if (c instanceof JTextField) ((JTextField) c).setCaretColor(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 85)),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)));
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return btn;
    }

    private JLabel makeSummaryLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        return lbl;
    }

    private void showInputError() {
        JOptionPane.showMessageDialog(this, "Please enter valid numerical values.", "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    private String findLinuxEditor() {
        for (String e : new String[]{"gedit","mousepad","leafpad","xed","kate","nano","xdg-open"}) {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"which", e});
                p.waitFor();
                if (p.exitValue() == 0) return e;
            } catch (Exception ignored) {}
        }
        return "xdg-open";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockManagementSystem().setVisible(true));
    }
}