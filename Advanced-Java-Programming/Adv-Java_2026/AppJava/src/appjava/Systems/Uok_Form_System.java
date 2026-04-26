/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*

Database Query

CREATE DATABASE IF NOT EXISTS Uok_db;

USE Uok_db;

CREATE TABLE IF NOT EXISTS students (
    student_id    INT          NOT NULL AUTO_INCREMENT,
    full_name     VARCHAR(150) NOT NULL,
    gender        VARCHAR(10)  NOT NULL,
    password      VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL,
    mobile_number VARCHAR(15)  NOT NULL,
    email_address VARCHAR(150) NOT NULL,
    area          VARCHAR(100) NOT NULL,
    province      VARCHAR(100) NOT NULL,
    nationality   VARCHAR(100) NOT NULL,
    registered_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id)
);

*/
package appjava.Systems;

/**
 *
 * @author STUDENTS
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Uok_Form_System extends JFrame {

    private JTextField txtFullName, txtMobile, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cmbDay, cmbMonth, cmbYear;
    private JComboBox<String> cmbArea, cmbProvince, cmbNationality;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
    private JButton btnSubmit, btnClear;

    private static final Color PURPLE_DARK  = new Color(75, 0, 130);
    private static final Color PURPLE_MED   = new Color(102, 51, 153);
    private static final Color PURPLE_LIGHT = new Color(180, 140, 220);
    private static final Color PURPLE_BG    = new Color(245, 235, 255);

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/Uok_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public Uok_Form_System() {
        setTitle("University of Kigali - Student Registration");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.add(buildHeader(),  BorderLayout.NORTH);
        main.add(buildForm(),    BorderLayout.CENTER);
        main.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(main);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 12));
        p.setBackground(PURPLE_DARK);

        JLabel icon = new JLabel("U");
        icon.setPreferredSize(new Dimension(44, 44));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setFont(new Font("Arial", Font.BOLD, 20));
        icon.setForeground(Color.WHITE);
        icon.setOpaque(true);
        icon.setBackground(PURPLE_MED);

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);

        JLabel t1 = new JLabel("UNIVERSITY OF KIGALI");
        t1.setFont(new Font("Arial", Font.BOLD, 20));
        t1.setForeground(Color.WHITE);

        JLabel t2 = new JLabel("STUDENT REGISTRATION SYSTEM · MYSQL DATABASE");
        t2.setFont(new Font("Arial", Font.PLAIN, 11));
        t2.setForeground(new Color(200, 180, 230));

        text.add(t1);
        text.add(t2);
        p.add(icon);
        p.add(text);
        return p;
    }

    private JScrollPane buildForm() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));

        JPanel banner = new JPanel(new GridLayout(2, 1));
        banner.setBackground(PURPLE_DARK);
        banner.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JLabel b1 = new JLabel("  Student Registration Form");
        b1.setFont(new Font("Arial", Font.BOLD, 15));
        b1.setForeground(Color.WHITE);

        JLabel b2 = new JLabel("  Fields marked * are mandatory · Data stored in MySQL");
        b2.setFont(new Font("Arial", Font.PLAIN, 11));
        b2.setForeground(new Color(200, 180, 230));

        banner.add(b1);
        banner.add(b2);
        p.add(banner);
        p.add(Box.createVerticalStrut(16));

        // Row 1: Full Name | Gender
        JPanel r1 = row();
        txtFullName = field("Enter full name");
        JPanel gp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        gp.setOpaque(false);
        rbMale   = radio("Male");
        rbFemale = radio("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);
        gp.add(rbMale);
        gp.add(rbFemale);
        r1.add(labeled("Full Name *", txtFullName));
        r1.add(labeled("Gender *", gp));
        p.add(r1);
        p.add(Box.createVerticalStrut(12));

        // Row 2: Password | Confirm Password
        JPanel r2 = row();
        txtPassword        = pfield("Min 6 characters");
        txtConfirmPassword = pfield("Re-enter password");
        r2.add(labeled("Password *", txtPassword));
        r2.add(labeled("Confirm Password *", txtConfirmPassword));
        p.add(r2);
        p.add(Box.createVerticalStrut(12));

        // Row 3: Date of Birth
        String[] days = new String[32]; days[0] = "DD";
        for (int i = 1; i <= 31; i++) days[i] = String.valueOf(i);
        cmbDay = combo(days);

        String[] months = {"Month","January","February","March","April","May","June",
                           "July","August","September","October","November","December"};
        cmbMonth = combo(months);
        cmbMonth.setPreferredSize(new Dimension(200, 36));

        String[] years = new String[81]; years[0] = "YYYY";
        for (int i = 1; i <= 80; i++) years[i] = String.valueOf(2024 - i + 1);
        cmbYear = combo(years);
        cmbYear.setPreferredSize(new Dimension(100, 36));

        JPanel dob = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        dob.setOpaque(false);
        dob.add(cmbDay);
        dob.add(cmbMonth);
        dob.add(cmbYear);

        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        r3.setOpaque(false);
        r3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        r3.add(labeled("Date of Birth *", dob));
        p.add(r3);
        p.add(Box.createVerticalStrut(12));

        // Row 4: Mobile | Email
        JPanel r4 = row();
        txtMobile = field("10-digit e.g. 0788123456");
        txtEmail  = field("e.g. student@uni.ac.rw");
        r4.add(labeled("Mobile Number *", txtMobile));
        r4.add(labeled("E-mail Address *", txtEmail));
        p.add(r4);
        p.add(Box.createVerticalStrut(12));

        // Row 5: Area | Province
        JPanel r5 = row();
        cmbArea     = combo(new String[]{"Select area","Nyarugenge","Gasabo","Kicukiro",
                                         "Huye","Musanze","Rubavu","Nyagatare","Muhanga"});
        cmbProvince = combo(new String[]{"Select province","Kigali City","Northern Province",
                                          "Southern Province","Eastern Province","Western Province"});
        r5.add(labeled("Area *", cmbArea));
        r5.add(labeled("State / Province *", cmbProvince));
        p.add(r5);
        p.add(Box.createVerticalStrut(12));

        // Row 6: Nationality
        JPanel r6 = row();
        cmbNationality = combo(new String[]{"Select nationality","Rwandan","Ugandan","Kenyan",
                                             "Tanzanian","Burundian","Congolese","Ethiopian","Other"});
        r6.add(labeled("Nationality *", cmbNationality));
        r6.add(new JPanel());
        p.add(r6);
        p.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bp.setOpaque(false);
        bp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnClear  = btn("  Clear",  false);
        btnSubmit = btn("  Submit", true);
        btnClear .addActionListener(e -> clearForm());
        btnSubmit.addActionListener(e -> submitForm());
        bp.add(btnClear);
        bp.add(btnSubmit);
        p.add(bp);

        return new JScrollPane(p);
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setBackground(PURPLE_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));
        JLabel l = new JLabel("University of Kigali · Student Registration System · Java Swing + MySQL (JDBC)");
        l.setFont(new Font("Arial", Font.PLAIN, 11));
        l.setForeground(new Color(200, 180, 230));
        p.add(l);
        return p;
    }

    private JPanel row() {
        JPanel p = new JPanel(new GridLayout(1, 2, 24, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        return p;
    }

    private JTextField field(String hint) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setBackground(PURPLE_BG);
        tf.setForeground(new Color(60, 0, 100));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        tf.putClientProperty("JTextField.placeholderText", hint);
        return tf;
    }

    private JPasswordField pfield(String hint) {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Arial", Font.PLAIN, 13));
        pf.setBackground(PURPLE_BG);
        pf.setForeground(new Color(60, 0, 100));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        pf.putClientProperty("JTextField.placeholderText", hint);
        return pf;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Arial", Font.PLAIN, 13));
        cb.setBackground(PURPLE_BG);
        cb.setForeground(new Color(60, 0, 100));
        cb.setPreferredSize(new Dimension(160, 36));
        return cb;
    }

    private JRadioButton radio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Arial", Font.PLAIN, 13));
        rb.setForeground(PURPLE_DARK);
        rb.setOpaque(false);
        return rb;
    }

    private JButton btn(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(120, 38));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        if (primary) {
            b.setBackground(PURPLE_DARK);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 2));
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(PURPLE_DARK);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createLineBorder(PURPLE_DARK, 2));
        }
        return b;
    }

    private JPanel labeled(String label, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setForeground(PURPLE_DARK);
        p.add(l, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void clearForm() {
        txtFullName.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
        genderGroup.clearSelection();
        cmbDay.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);
        cmbArea.setSelectedIndex(0);
        cmbProvince.setSelectedIndex(0);
        cmbNationality.setSelectedIndex(0);
    }

    private void submitForm() {
        String fullName    = txtFullName.getText().trim();
        String password    = new String(txtPassword.getPassword());
        String confirm     = new String(txtConfirmPassword.getPassword());
        String mobile      = txtMobile.getText().trim();
        String email       = txtEmail.getText().trim();
        String gender      = rbMale.isSelected() ? "Male" : rbFemale.isSelected() ? "Female" : "";
        String day         = (String) cmbDay.getSelectedItem();
        String month       = (String) cmbMonth.getSelectedItem();
        String year        = (String) cmbYear.getSelectedItem();
        String area        = (String) cmbArea.getSelectedItem();
        String province    = (String) cmbProvince.getSelectedItem();
        String nationality = (String) cmbNationality.getSelectedItem();

        if (fullName.isEmpty())                           { err("Full Name is required.");             return; }
        if (gender.isEmpty())                             { err("Please select a gender.");            return; }
        if (password.length() < 6)                        { err("Password must be at least 6 chars."); return; }
        if (!password.equals(confirm))                    { err("Passwords do not match.");            return; }
        if (day.equals("DD")||month.equals("Month")||year.equals("YYYY"))
                                                          { err("Please complete Date of Birth.");     return; }
        if (!mobile.matches("\\d{10}"))                   { err("Mobile must be 10 digits.");          return; }
        if (!email.contains("@") || !email.contains(".")) { err("Enter a valid email address.");       return; }
        if (area.equals("Select area"))                   { err("Please select an area.");             return; }
        if (province.equals("Select province"))           { err("Please select a province.");          return; }
        if (nationality.equals("Select nationality"))     { err("Please select a nationality.");       return; }

        String dob = year + "-"
                   + String.format("%02d", cmbMonth.getSelectedIndex()) + "-"
                   + String.format("%02d", Integer.parseInt(day));

        String sql = "INSERT INTO students "
                   + "(full_name, gender, password, date_of_birth, mobile_number, "
                   + " email_address, area, province, nationality) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, gender);
            ps.setString(3, password);
            ps.setString(4, dob);
            ps.setString(5, mobile);
            ps.setString(6, email);
            ps.setString(7, area);
            ps.setString(8, province);
            ps.setString(9, nationality);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student registered successfully!\nWelcome, " + fullName + "!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();

        } catch (SQLException ex) {
            err("Database error:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!\nAdd mysql-connector-j.jar to your classpath.",
                "Driver Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(Uok_Form_System::new);
    }
}