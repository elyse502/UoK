/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package appjava.raw;

/**
 *
 * @author idtda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class QuotingSystem extends JFrame {

    // ── Form inputs ────────────────────────────────────────────────────────────
    private JTextField descriptionInput, unitPriceInput, quantityInput, marginInput, totalPriceInput;
    private JRadioButton withholdingTaxYes, withholdingTaxNo;
    private JComboBox<String> itemTypeSelector;
    private JButton calculateBtn;

    // ── Display components ─────────────────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable quoteTable;

    // ══════════════════════════════════════════════════════════════════════════
    public QuotingSystem() {
        setTitle("Quoting System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BUILD UI
    // ══════════════════════════════════════════════════════════════════════════
    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(5, 5));
        main.setBackground(Color.CYAN);
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setContentPane(main);

        main.add(buildTitlePanel(),  BorderLayout.NORTH);
        main.add(buildFormPanel(),   BorderLayout.WEST);
        main.add(buildTablePanel(),  BorderLayout.CENTER);
    }

    // ── Title ──────────────────────────────────────────────────────────────────
    private JPanel buildTitlePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        JLabel lbl = new JLabel("QUOTING SYSTEM");
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.BLUE);
        p.add(lbl);
        return p;
    }

    // ── Form panel ──────────────────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        descriptionInput = new JTextField();
        unitPriceInput = new JTextField();
        quantityInput = new JTextField();
        marginInput = new JTextField();
        totalPriceInput = new JTextField();
        totalPriceInput.setEditable(false);

        withholdingTaxYes = new JRadioButton("Yes");
        withholdingTaxNo = new JRadioButton("No", true);
        ButtonGroup withholdingTaxGroup = new ButtonGroup();
        withholdingTaxGroup.add(withholdingTaxYes);
        withholdingTaxGroup.add(withholdingTaxNo);

        itemTypeSelector = new JComboBox<>(new String[]{"Product", "Service"});

        calculateBtn = new JButton("Calculate Total Price");
        calculateBtn.setBackground(Color.BLUE);
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFont(new Font("Arial", Font.BOLD, 12));
        calculateBtn.addActionListener(e -> calculatePrice());

        p.add(new JLabel("Item Description:"));  p.add(descriptionInput);
        p.add(new JLabel("Unit Price (RWF):"));  p.add(unitPriceInput);
        p.add(new JLabel("Quantity:"));           p.add(quantityInput);
        p.add(new JLabel("Margin (%):"));         p.add(marginInput);
        p.add(new JLabel("Type:"));                p.add(itemTypeSelector);
        p.add(new JLabel("Withholding Tax:"));    p.add(withholdingTaxYes);
        p.add(new JLabel(""));                      p.add(withholdingTaxNo);
        p.add(new JLabel("Total Price:"));         p.add(totalPriceInput);
        p.add(new JPanel());                       p.add(calculateBtn);

        return p;
    }

    // ── Table ───────────────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        // Table model
        String[] cols = {"Description", "Unit Price", "Quantity", "Margin (%)", "Total"};
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

    // ══════════════════════════════════════════════════════════════════════════
    // CALCULATE TOTAL PRICE LOGIC
    // ══════════════════════════════════════════════════════════════════════════
    private void calculatePrice() {
        try {
            String description = descriptionInput.getText().trim();
            double unitPrice = Double.parseDouble(unitPriceInput.getText().trim());
            int quantity = Integer.parseInt(quantityInput.getText().trim());
            double margin = Double.parseDouble(marginInput.getText().trim());

            double subtotal = unitPrice * quantity;
            double grossProfit = (margin / 100) * subtotal;
            double total = subtotal + grossProfit;

            if (withholdingTaxYes.isSelected()) {
                total *= 0.85; // Deduct 15% withholding tax
            }

            total *= 1.18; // Add 18% VAT

            totalPriceInput.setText(String.format("%.2f RWF", total));

            // Add quote to table
            tableModel.addRow(new Object[]{description, unitPrice, quantity, margin, String.format("%.2f RWF", total)});
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
        withholdingTaxYes.setSelected(false);
        withholdingTaxNo.setSelected(true);
        itemTypeSelector.setSelectedIndex(0);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuotingSystem().setVisible(true));
    }
}
