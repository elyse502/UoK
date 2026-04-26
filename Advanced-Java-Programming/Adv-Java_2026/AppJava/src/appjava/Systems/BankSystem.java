package appjava.Systems;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BankSystem {

    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new LoginDialog();
        });
    }
}

/**
 * Login Dialog for authenticating users
 */
class LoginDialog extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public LoginDialog() {
        // Initialize login window
        setTitle("Banking System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set background color to green
        getContentPane().setBackground(new Color(0, 128, 0));
        
        // Use a panel with GridBagLayout for better control
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 128, 0));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create and add the title label
        JLabel lblTitle = new JLabel("Bank of Kigali (BK)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);
        
        // Create and add username label and field
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.ORANGE);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblUsername, gbc);
        
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtUsername, gbc);
        
        // Create and add password label and field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.ORANGE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPassword, gbc);
        
        // Create and add login button
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(0, 0, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 10));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);
        
        // Add action listener to login button
        btnLogin.addActionListener(e -> checkCredentials());
        
        // Add panel to frame
        add(panel);
        
        // Display the window
        setVisible(true);
    }
    
    private void checkCredentials() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.equals("Danny") && password.equals("Danny")) {
            dispose(); // Close login window
            new BankSystemGUI(); // Open main application
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password.", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Main Banking System GUI class
 */
class BankSystemGUI extends JFrame {
    private String accountHolder;
    private double balance;
    private List<String> transactions;
    
    private JTextField txtAccountHolder;
    private JTextField txtDeposit;
    private JTextField txtWithdraw;
    private JTextArea txtAreaOutput;
    private JButton btnCreateAccount;
    private JButton btnDeposit;
    private JButton btnWithdraw;
    private JButton btnCheckBalance;
    private JButton btnSaveTransactions;
    
    public BankSystemGUI() {
        // Initialize attributes
        accountHolder = "";
        balance = 0.0;
        transactions = new ArrayList<>();
        
        // Set up the main window
        setTitle("Bank System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 128, 0));
        
        // Use BorderLayout for the main frame
        setLayout(new BorderLayout());
        
        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(0, 128, 0));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create and add title label
        JLabel lblTitle = new JLabel("Welcome to the Bank of Kigali (BK)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(lblTitle, gbc);
        
        // Create and add account holder label and field
        JLabel lblAccountHolder = new JLabel("Account Holder:");
        lblAccountHolder.setForeground(Color.ORANGE);
        lblAccountHolder.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(lblAccountHolder, gbc);
        
        txtAccountHolder = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(txtAccountHolder, gbc);
        
        // Create and add deposit label and field
        JLabel lblDeposit = new JLabel("Deposit Amount:");
        lblDeposit.setForeground(Color.ORANGE);
        lblDeposit.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblDeposit, gbc);
        
        txtDeposit = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(txtDeposit, gbc);
        
        // Create and add withdraw label and field
        JLabel lblWithdraw = new JLabel("Withdraw Amount:");
        lblWithdraw.setForeground(Color.ORANGE);
        lblWithdraw.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblWithdraw, gbc);
        
        txtWithdraw = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(txtWithdraw, gbc);
        
        // Create buttons
        btnCreateAccount = createButton("Create Account");
        btnDeposit = createButton("Deposit");
        btnWithdraw = createButton("Withdraw");
        btnCheckBalance = createButton("Check Balance");
        btnSaveTransactions = createButton("Save Transactions");
        
        // Add action listeners to buttons
        btnCreateAccount.addActionListener(e -> createAccount());
        btnDeposit.addActionListener(e -> deposit());
        btnWithdraw.addActionListener(e -> withdraw());
        btnCheckBalance.addActionListener(e -> checkBalance());
        btnSaveTransactions.addActionListener(e -> saveTransactions());
        
        // Add buttons to panel
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(btnCreateAccount, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(btnDeposit, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(btnWithdraw, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        inputPanel.add(btnCheckBalance, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        inputPanel.add(btnSaveTransactions, gbc);
        
        // Create output text area
        txtAreaOutput = new JTextArea();
        txtAreaOutput.setEditable(false);
        txtAreaOutput.setLineWrap(true);
        txtAreaOutput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtAreaOutput);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        
        // Set auto-scroll policy
        DefaultCaret caret = (DefaultCaret) txtAreaOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        // Add initial text to output area
        txtAreaOutput.append(String.format("%-20s | %-10s\n", "Transactions", "Amount"));
        txtAreaOutput.append("---------------------------------\n");
        
        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Display the window
        pack();
        setVisible(true);
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        return button;
    }
    
    private void createAccount() {
        accountHolder = txtAccountHolder.getText().trim();
        if (!accountHolder.isEmpty()) {
            balance = 0.0;
            txtAreaOutput.append(String.format("%-20s | %s\n", "Account Created", accountHolder));
            JOptionPane.showMessageDialog(this, 
                "Account created for " + accountHolder + ".",
                "Account Created", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please enter an account holder name.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deposit() {
        try {
            double amount = Double.parseDouble(txtDeposit.getText().trim());
            if (amount > 0 && !accountHolder.isEmpty()) {
                balance += amount;
                String transaction = String.format("%-20s FRW: %.2f", "Deposit", amount);
                transactions.add(transaction);
                txtAreaOutput.append(String.format("%-20s | FRW: %.2f\n", "Deposit", amount));
                JOptionPane.showMessageDialog(this, 
                    "Deposited FRW " + new DecimalFormat("#0.00").format(amount) + ".",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Deposit amount must be positive and account must be created.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid deposit amount.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void withdraw() {
        try {
            double amount = Double.parseDouble(txtWithdraw.getText().trim());
            if (amount > 0 && amount <= balance && !accountHolder.isEmpty()) {
                balance -= amount;
                String transaction = String.format("%-20s FRW: %.2f", "Withdraw", amount);
                transactions.add(transaction);
                txtAreaOutput.append(String.format("%-20s | FRW: %.2f\n", "Withdraw", amount));
                JOptionPane.showMessageDialog(this, 
                    "Withdrew FRW " + new DecimalFormat("#0.00").format(amount) + ".",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient funds or invalid amount, and account must be created.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid withdrawal amount.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkBalance() {
        if (!accountHolder.isEmpty()) {
            txtAreaOutput.append(String.format("%-20s | FRW: %.2f\n", "Balance", balance));
            JOptionPane.showMessageDialog(this, 
                "Current Balance: FRW " + new DecimalFormat("#0.00").format(balance) + ".",
                "Balance", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please create an account first.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveTransactions() {
        if (accountHolder.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please create an account first.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transactions");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Add .txt extension if not present
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write account information
                writer.write("Account Holder: " + accountHolder + "\n");
                writer.write("Current Balance: FRW " + new DecimalFormat("#0.00").format(balance) + "\n");
                writer.write("\nTransactions:\n");
                
                // Write transactions
                for (String transaction : transactions) {
                    writer.write(transaction + "\n");
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Transactions saved successfully to:\n" + fileToSave.getAbsolutePath(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving transactions: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Save cancelled.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}