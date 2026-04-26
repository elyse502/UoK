package appjava.systems;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Danny Idukundatwese
 */
public class PersonManagementGUI extends JFrame {

    private Connection connection;
    private PreparedStatement insertStatement, updateStatement, deleteStatement, selectStatement;

    private JTextField firstNameField, middleNameField, lastNameField, emailField, phoneField;

    private JButton insertButton, newButton, updateButton, deleteButton;
    private JButton firstButton, previousButton, nextButton, lastButton;

    private JTable personTable;
    private DefaultTableModel tableModel;

    private int currentRowIndex = -1;

    public PersonManagementGUI() {
        initComponents();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/my_data_base_improved",
                "root",
                ""
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            insertStatement = connection.prepareStatement(
                "INSERT INTO Person_improved (firstName, middleName, lastName, email, phone) VALUES (?, ?, ?, ?, ?)");

            updateStatement = connection.prepareStatement(
                "UPDATE Person_improved SET firstName=?, middleName=?, lastName=?, email=?, phone=? WHERE personId=?");

            deleteStatement = connection.prepareStatement(
                "DELETE FROM Person_improved WHERE personId=?");

            selectStatement = connection.prepareStatement(
                "SELECT personId, firstName, middleName, lastName, email, phone FROM Person_improved");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error preparing SQL statements: " + e.getMessage(), 
                "SQL Error", JOptionPane.ERROR_MESSAGE);
        }

        populateTable();

        newButton.addActionListener(e -> clearFields());
        insertButton.addActionListener(e -> insertPerson());
        updateButton.addActionListener(e -> updatePerson());
        deleteButton.addActionListener(e -> deletePerson());
        firstButton.addActionListener(e -> navigateToFirst());
        previousButton.addActionListener(e -> navigateToPrevious());
        nextButton.addActionListener(e -> navigateToNext());
        lastButton.addActionListener(e -> navigateToLast());

        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = personTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        currentRowIndex = selectedRow;
                        loadPersonData(selectedRow);
                    }
                }
            }
        });
        
        // Add window closing handler to close database connection
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
    }

    private void initComponents() {
        setTitle("Person Management Improved");
        setSize(900, 600);
        setLayout(new BorderLayout());

        firstNameField = new JTextField(15);
        middleNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);

        insertButton = new JButton("Insert");
        newButton = new JButton("New");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        firstButton = new JButton("First");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        lastButton = new JButton("Last");

        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Middle Name", "Last Name", "Email", "Phone"}, 0);
        personTable = new JTable(tableModel);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("First Name:")); formPanel.add(firstNameField);
        formPanel.add(new JLabel("Middle Name:")); formPanel.add(middleNameField);
        formPanel.add(new JLabel("Last Name:")); formPanel.add(lastNameField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 8, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(newButton); buttonPanel.add(insertButton);
        buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        buttonPanel.add(firstButton); buttonPanel.add(previousButton);
        buttonPanel.add(nextButton); buttonPanel.add(lastButton);

        add(new JScrollPane(personTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateTable() {
        try {
            ResultSet resultSet = selectStatement.executeQuery();
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("personId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("middleName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("email"),
                    resultSet.getString("phone")
                });
            }

            currentRowIndex = -1;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        currentRowIndex = -1;
        personTable.clearSelection();
    }

    private void loadPersonData(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            Object middleName = personTable.getValueAt(row, 2);
            Object email = personTable.getValueAt(row, 4);
            Object phone = personTable.getValueAt(row, 5);
            
            firstNameField.setText(personTable.getValueAt(row, 1).toString());
            middleNameField.setText(middleName != null ? middleName.toString() : "");
            lastNameField.setText(personTable.getValueAt(row, 3).toString());
            emailField.setText(email != null ? email.toString() : "");
            phoneField.setText(phone != null ? phone.toString() : "");
        }
    }

    private void insertPerson() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name and Last Name are required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            insertStatement.setString(1, firstName);
            insertStatement.setString(2, middleNameField.getText().trim());
            insertStatement.setString(3, lastName);
            insertStatement.setString(4, emailField.getText().trim());
            insertStatement.setString(5, phoneField.getText().trim());

            insertStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Person added successfully!");

            populateTable();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting record: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePerson() {
        if (currentRowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to update by double-clicking on it.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name and Last Name are required!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int personId = (int) personTable.getValueAt(currentRowIndex, 0);

            updateStatement.setString(1, firstName);
            updateStatement.setString(2, middleNameField.getText().trim());
            updateStatement.setString(3, lastName);
            updateStatement.setString(4, emailField.getText().trim());
            updateStatement.setString(5, phoneField.getText().trim());
            updateStatement.setInt(6, personId);

            updateStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record updated successfully!");
            populateTable();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePerson() {
        int row = personTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this record?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int personId = (int) personTable.getValueAt(row, 0);

            deleteStatement.setInt(1, personId);
            deleteStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record deleted successfully!");
            populateTable();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navigateToFirst() { 
        if (tableModel.getRowCount() > 0) {
            currentRowIndex = 0;
            loadPersonData(currentRowIndex);
            personTable.setRowSelectionInterval(currentRowIndex, currentRowIndex);
        }
    }

    private void navigateToPrevious() { 
        if (currentRowIndex > 0) {
            currentRowIndex--;
            loadPersonData(currentRowIndex);
            personTable.setRowSelectionInterval(currentRowIndex, currentRowIndex);
        }
    }

    private void navigateToNext() { 
        if (currentRowIndex < tableModel.getRowCount() - 1) {
            currentRowIndex++;
            loadPersonData(currentRowIndex);
            personTable.setRowSelectionInterval(currentRowIndex, currentRowIndex);
        }
    }

    private void navigateToLast() { 
        if (tableModel.getRowCount() > 0) {
            currentRowIndex = tableModel.getRowCount() - 1;
            loadPersonData(currentRowIndex);
            personTable.setRowSelectionInterval(currentRowIndex, currentRowIndex);
        }
    }
    
    private void closeConnection() {
        try {
            if (insertStatement != null) insertStatement.close();
            if (updateStatement != null) updateStatement.close();
            if (deleteStatement != null) deleteStatement.close();
            if (selectStatement != null) selectStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersonManagementGUI gui = new PersonManagementGUI();
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setVisible(true);
        });
    }
}