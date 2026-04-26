package com.userapp.views;

import com.userapp.controllers.UserController;
import com.userapp.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserManagementUI extends JFrame {
    private UserController userController;
    
 // UI Components
    private JTextField searchData;
    private JTextField fname;
    private JTextField email;
    private JTextField phone;
    private JTextField country;
    private JTable table;
    private JButton submitBtn;
    private JButton updateBtn;
    private JButton deleteBtn;
    private JButton searchBtn;
    private JButton resetBtn;

    public UserManagementUI() {
        initComponents();
        userController = new UserController();
        loadData();
    }

    private void initComponents() {
        // Main Frame Setup
        setTitle("User Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);  // Slightly larger for better spacing
        setLocationRelativeTo(null);
        
        // Create input components
        createTextFields();
        createButtons();
        
        // Table with scroll pane
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        
        // Set layout with padding
        setLayout(new BorderLayout(15, 15));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create and configure panels
        JPanel searchPanel = createSearchPanel();
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to frame
        add(searchPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        // Set action listeners
        setActionListeners();
    }

    private void createTextFields() {
        searchData = createStyledTextField(20);
        fname = createStyledTextField(20);
        email = createStyledTextField(20);
        phone = createStyledTextField(20);
        country = createStyledTextField(20);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    private void createButtons() {
        submitBtn = createStyledButton("Submit", new Color(76, 175, 80));
        updateBtn = createStyledButton("Update", new Color(33, 150, 243));
        deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        searchBtn = createStyledButton("Search", new Color(255, 152, 0));
        resetBtn = createStyledButton("Reset", new Color(158, 158, 158));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search User"));
        
        JLabel searchLabel = new JLabel("Search (ID/Name):");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        panel.add(searchLabel);
        panel.add(searchData);
        panel.add(searchBtn);
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 12));
        panel.setBorder(BorderFactory.createTitledBorder("User Details"));
        
        addFormField(panel, "Full Name:", fname);
        addFormField(panel, "Email:", email);
        addFormField(panel, "Phone:", phone);
        addFormField(panel, "Country:", country);
        
        return panel;
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label);
        panel.add(textField);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        panel.add(submitBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(resetBtn);
        
        return panel;
    }

    private void setActionListeners() {
        submitBtn.addActionListener(this::submitActionPerformed);
        updateBtn.addActionListener(this::updateActionPerformed);
        deleteBtn.addActionListener(this::deleteActionPerformed);
        searchBtn.addActionListener(this::searchbtnActionPerformed);
        resetBtn.addActionListener(this::resetActionPerformed);
    }
    
    private void loadData() {
        DefaultTableModel model = userController.loadUserData();
        table.setModel(model);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    searchData.setText(model.getValueAt(row, 0).toString());
                    fname.setText(model.getValueAt(row, 1).toString());
                    email.setText(model.getValueAt(row, 2).toString());
                    phone.setText(model.getValueAt(row, 3).toString());
                    country.setText(model.getValueAt(row, 4).toString());
                }
            }
        });
    }
    
    private void submitActionPerformed(ActionEvent evt) {                                       
        String fn = fname.getText().trim();
        String user_email = email.getText().trim();
        String phone_number = phone.getText().trim();
        String user_country = country.getText().trim();

        // Validate inputs
        if (fn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name Required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (user_email.isEmpty() || !user_email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Valid Email Required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userController.checkEmailExists(user_email)) {
            JOptionPane.showMessageDialog(this, "This Email is already registered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (phone_number.isEmpty() || !phone_number.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Valid Phone Number Required (7-15 digits)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userController.checkPhoneExists(phone_number)) {
            JOptionPane.showMessageDialog(this, "This Phone Number is already registered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user_country.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Country Required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setFullName(fn);
        user.setEmail(user_email);
        user.setPhone(phone_number);
        user.setCountry(user_country);

        if (userController.createUser(user)) {
            JOptionPane.showMessageDialog(this, "User Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateActionPerformed(ActionEvent evt) {
        String searchText = searchData.getText().trim();  // searchText is a String representing the user ID
        String fn = fname.getText().trim();
        String user_email = email.getText().trim();
        String phone_number = phone.getText().trim();
        String user_country = country.getText().trim();

        // Check if search field is filled
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID Required for Update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate all fields are filled
        if (fn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user_email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!user_email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid Email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email is used by another user
        if (userController.checkEmailExistsForOtherUser(user_email, searchText)) {  // searchText is passed as a String
            JOptionPane.showMessageDialog(this, "This Email is already registered to another user.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone_number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone Number is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate phone number format (digits only, 7-15 digits)
        if (!phone_number.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Phone Number must be numeric and between 7 to 15 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if phone number is used by another user
        if (userController.checkPhoneExistsForOtherUser(phone_number, searchText)) {  // searchText is passed as a String
            JOptionPane.showMessageDialog(this, "This Phone Number is already registered to another user.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user_country.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Country is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user object for update
        User user = new User();
        user.setFullName(fn);
        user.setEmail(user_email);
        user.setPhone(phone_number);
        user.setCountry(user_country);

        // Perform update
        if (userController.updateUser(user, searchText)) {
            JOptionPane.showMessageDialog(this, "User Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed. User not found or error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void deleteActionPerformed(ActionEvent evt) {                                       
        String searchText = searchData.getText();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID or Name Required for Deletion", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userController.deleteUser(searchText)) {
            JOptionPane.showMessageDialog(this, "User Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "No user found with this ID or name", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchbtnActionPerformed(ActionEvent evt) {                                          
        String searchText = searchData.getText();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID or Name Required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userController.searchUser(searchText);
        if (user != null) {
            fname.setText(user.getFullName());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            country.setText(user.getCountry());
        } else {
            JOptionPane.showMessageDialog(this, "No user found with this ID or name", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resetActionPerformed(ActionEvent evt) {                                      
        resetFields();
    }
    
    private void resetFields() {
        searchData.setText("");
        fname.setText("");
        email.setText("");
        phone.setText("");
        country.setText("");
    }
    
    public static void main(String[] args) {
        // Set system look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // Create and show the UI
        SwingUtilities.invokeLater(() -> {
            UserManagementUI ui = new UserManagementUI();
            ui.setVisible(true);
        });
    }
}