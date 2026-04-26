package StudentRegistration;

import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class Employee extends JFrame {

    private JTextField Txtcompany, Txtemployee;
    private JComboBox<String> gender, industry, maxsalary, minsalary, post;
    private JButton jButton1;

    public Employee() {
        initComponents();
    }

    private void initComponents() {
        // Colors
        Color bgColor = new Color(245, 252, 255);
        Color headerColor = new Color(0, 102, 153);
        Color labelColor = new Color(0, 51, 102);
        Color buttonColor = new Color(0, 153, 153);

        // Frame settings
        setTitle("Kigali Employee Data Entry");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);

        // Layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);

        // Title label
        JLabel jLabel1 = new JLabel("KIGALI EMPLOYEE DATA ENTRY FORM");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        jLabel1.setForeground(headerColor);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 20, 10);
        c.gridx = 0;
        c.gridy = 0;
        add(jLabel1, c);

        // Field labels and components
        String[] labels = {
            "Company Name:", "Industry:", "Employee Names:", "Gender:",
            "Post:", "Min Salary:", "Max Salary:"
        };

        Component[] fields = new Component[labels.length];

        Txtcompany = new JTextField(20);
        industry = new JComboBox<>(new String[]{"--Select--", "Farming", "IT", "Education", "Business", "Public Administration", "Politics", "Health"});
        Txtemployee = new JTextField(20);
        gender = new JComboBox<>(new String[]{"--Select--", "Male", "Female"});
        post = new JComboBox<>(new String[]{"--Select--", "CEO", "Senior Manager", "Manager", "Human Resources", "Strategic Director", "Director", "Worker Staff"});
        minsalary = new JComboBox<>(new String[]{"--Select--", "less than 100000 rwf", "less than 200000 rwf", "less than 300000 rwf", "less than 400000 rwf", "less than 500000 rwf"});
        maxsalary = new JComboBox<>(new String[]{"--Select--", "above 100000", "above 200000", "above 300000", "above 400000", "above 500000", "above 1000000"});

        fields[0] = Txtcompany;
        fields[1] = industry;
        fields[2] = Txtemployee;
        fields[3] = gender;
        fields[4] = post;
        fields[5] = minsalary;
        fields[6] = maxsalary;

        // Adding labels and fields to the form
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setForeground(labelColor);

            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = i + 1;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(10, 10, 10, 10);
            add(lbl, c);

            c.gridx = 1;
            c.anchor = GridBagConstraints.LINE_START;
            add(fields[i], c);
        }

        // Save button
        jButton1 = new JButton("SAVE DATA");
        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton1.setBackground(buttonColor);
        jButton1.setForeground(Color.WHITE);
        jButton1.addActionListener(evt -> saveEmployeeData());
        c.gridx = 0;
        c.gridy = labels.length + 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(25, 10, 20, 10);
        add(jButton1, c);
    }

    private void saveEmployeeData() {
        // Collect values from form
        String companyName = Txtcompany.getText().trim();
        String Industry = industry.getSelectedItem().toString();
        String employeeName = Txtemployee.getText().trim();
        String employeeGender = gender.getSelectedItem().toString();
        String Post = post.getSelectedItem().toString();
        String minSalary = minsalary.getSelectedItem().toString();
        String maxSalary = maxsalary.getSelectedItem().toString();

        // Validation checks
        if (companyName.isEmpty() || Industry.equals("--Select--") || employeeName.isEmpty()
                || employeeGender.equals("--Select--") || Post.equals("--Select--")
                || minSalary.equals("--Select--") || maxSalary.equals("--Select--")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Database Connection & Data Saving
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Netbeansloginsituation?zeroDateTimeBehavior=convertToNull", "root", "");
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO kigaliemployeesdata (company_name, industry, employee_name, employee_gender, post, min_salary, max_salary) VALUES (?,?,?,?,?,?,?)")) {

            pst.setString(1, companyName);
            pst.setString(2, Industry);
            pst.setString(3, employeeName);
            pst.setString(4, employeeGender);
            pst.setString(5, Post);
            pst.setString(6, minSalary);
            pst.setString(7, maxSalary);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Employee data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Reset fields
            Txtcompany.setText("");
            industry.setSelectedIndex(0);
            Txtemployee.setText("");
            gender.setSelectedIndex(0);
            post.setSelectedIndex(0);
            minsalary.setSelectedIndex(0);
            maxsalary.setSelectedIndex(0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Employee().setVisible(true));
    }
}
