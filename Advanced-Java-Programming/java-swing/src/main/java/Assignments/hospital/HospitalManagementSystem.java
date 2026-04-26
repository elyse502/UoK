package Assignments.hospital;

/**
 *
 * @author Elysée NIYIBIZI
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.io.*;

// =====================================================================
//  HOSPITAL MANAGEMENT SYSTEM
//  Advanced Java Programming - UoK
//  Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert
//  Features: Login, Patient CRUD, Doctor Management, Appointment
//            Scheduling, Billing, Medical Records, Dashboard Stats
// =====================================================================

public class HospitalManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}

// ========================= DATA MODELS =========================

class Patient {
    private static int idCounter = 1000;
    private int id;
    private String name, gender, phone, bloodGroup, address;
    private int age;
    private String admissionDate;
    private String status; // "Admitted" or "Discharged"
    private double totalBill;

    public Patient(String name, int age, String gender, String phone,
                   String bloodGroup, String address) {
        this.id = ++idCounter;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.admissionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.status = "Admitted";
        this.totalBill = 0.0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAddress() { return address; }
    public String getAdmissionDate() { return admissionDate; }
    public String getStatus() { return status; }
    public double getTotalBill() { return totalBill; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { this.status = status; }
    public void addToBill(double amount) { this.totalBill += amount; }
}

class Doctor {
    private int id;
    private String name, specialization, phone;
    private boolean available;

    public Doctor(int id, String name, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.available = true;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

class Appointment {
    private static int apptCounter = 100;
    private int id;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String date, time, reason, status;

    public Appointment(int patientId, String patientName, int doctorId,
                       String doctorName, String date, String time, String reason) {
        this.id = ++apptCounter;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = "Scheduled";
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class MedicalRecord {
    private static int recCounter = 500;
    private int id;
    private int patientId;
    private String patientName, doctorName, diagnosis, prescription, date, notes;

    public MedicalRecord(int patientId, String patientName, String doctorName,
                         String diagnosis, String prescription, String notes) {
        this.id = ++recCounter;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }
}

// ========================= DATA STORE =========================

class HospitalData {
    static List<Patient> patients = new ArrayList<>();
    static List<Doctor> doctors = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static List<MedicalRecord> records = new ArrayList<>();

    static {
        // Sample Doctors
        doctors.add(new Doctor(1, "Dr. Amina Uwimana",    "Cardiology",       "+250 788 001 001"));
        doctors.add(new Doctor(2, "Dr. Jean Pierre Nkusi","Pediatrics",        "+250 788 001 002"));
        doctors.add(new Doctor(3, "Dr. Grace Mukamana",   "General Medicine",  "+250 788 001 003"));
        doctors.add(new Doctor(4, "Dr. Eric Habimana",    "Surgery",           "+250 788 001 004"));
        doctors.add(new Doctor(5, "Dr. Alice Ingabire",   "Dermatology",       "+250 788 001 005"));

        // Sample Patients
        Patient p1 = new Patient("Mugisha Bosco",    34, "Male",   "+250 788 111 111", "A+",  "Kigali, Gasabo");
        Patient p2 = new Patient("Uwase Claudine",   28, "Female", "+250 788 222 222", "B-",  "Kigali, Kicukiro");
        Patient p3 = new Patient("Ntwari Emmanuel",  45, "Male",   "+250 788 333 333", "O+",  "Huye");
        Patient p4 = new Patient("Ingabire Diane",   19, "Female", "+250 788 444 444", "AB+", "Musanze");
        Patient p5 = new Patient("Habimana Patrick", 60, "Male",   "+250 788 555 555", "A-",  "Rubavu");
        p3.setStatus("Discharged");
        p5.addToBill(25000);
        patients.add(p1); patients.add(p2); patients.add(p3);
        patients.add(p4); patients.add(p5);

        // Sample Appointments
        appointments.add(new Appointment(p1.getId(), p1.getName(), 1, "Dr. Amina Uwimana",
                "2025-05-10", "09:00", "Chest Pain"));
        appointments.add(new Appointment(p2.getId(), p2.getName(), 3, "Dr. Grace Mukamana",
                "2025-05-11", "10:30", "Fever and Headache"));
        appointments.add(new Appointment(p4.getId(), p4.getName(), 5, "Dr. Alice Ingabire",
                "2025-05-12", "14:00", "Skin Rash"));

        // Sample Medical Records
        records.add(new MedicalRecord(p3.getId(), p3.getName(), "Dr. Grace Mukamana",
                "Malaria", "Coartem 4 tablets x3 days", "Advise rest and fluids"));
        records.add(new MedicalRecord(p5.getId(), p5.getName(), "Dr. Amina Uwimana",
                "Hypertension", "Amlodipine 5mg daily", "Monitor BP weekly"));
    }
}

// ========================= LOGIN SCREEN =========================

class LoginScreen extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private static final Color PRIMARY = new Color(0, 102, 153);
    private static final Color ACCENT  = new Color(0, 180, 216);

    public LoginScreen() {
        setTitle("Hospital Management System - Login");
        setSize(420, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PRIMARY);

        // --- TOP LOGO AREA ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(PRIMARY);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel icon = new JLabel("\uD83C\uDFE5", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("KIGALI CENTRAL HOSPITAL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Hospital Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(new Color(180, 220, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(icon);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(4));
        topPanel.add(subtitle);

        // --- FORM AREA ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel loginLabel = new JLabel("Sign In to Your Account");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginLabel.setForeground(PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(loginLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lUser = new JLabel("Username:");
        lUser.setFont(new Font("Arial", Font.BOLD, 11));
        lUser.setForeground(new Color(60, 60, 60));
        formPanel.add(lUser, gbc);

        gbc.gridy = 2;
        txtUser = new JTextField();
        txtUser.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        formPanel.add(txtUser, gbc);

        gbc.gridy = 3;
        JLabel lPass = new JLabel("Password:");
        lPass.setFont(new Font("Arial", Font.BOLD, 11));
        lPass.setForeground(new Color(60, 60, 60));
        formPanel.add(lPass, gbc);

        gbc.gridy = 4;
        txtPass = new JPasswordField();
        txtPass.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        formPanel.add(txtPass, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(16, 0, 8, 0);
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> doLogin());
        formPanel.add(btnLogin, gbc);

        JLabel hint = new JLabel("Demo credentials:  admin / admin123", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.ITALIC, 10));
        hint.setForeground(Color.GRAY);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(hint, gbc);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(formPanel, BorderLayout.CENTER);
        add(root);

        getRootPane().setDefaultButton(btnLogin);
        setVisible(true);
    }

    private void doLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        if (user.equals("admin") && pass.equals("admin123")) {
            dispose();
            new MainDashboard();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.\nHint: admin / admin123",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// ========================= MAIN DASHBOARD =========================

class MainDashboard extends JFrame {
    static final Color PRIMARY = new Color(0, 102, 153);
    static final Color SIDEBAR = new Color(15, 52, 96);
    static final Color ACCENT  = new Color(0, 180, 216);
    static final Color WHITE   = Color.WHITE;
    static final Color BG      = new Color(240, 245, 250);

    private JPanel contentArea;
    private CardLayout cardLayout;

    // FIX: keep references to every panel so navigate() can refresh them
    private DashboardPanel     dashboardPanel;
    private PatientPanel       patientPanel;
    private DoctorPanel        doctorPanel;
    private AppointmentPanel   appointmentPanel;
    private MedicalRecordPanel recordPanel;
    private BillingPanel       billingPanel;

    public MainDashboard() {
        setTitle("Hospital Management System - Kigali Central Hospital");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);

        // FIX: build panels BEFORE the sidebar so the panel references exist
        //      when the sidebar buttons call navigate()
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(BG);

        dashboardPanel   = new DashboardPanel(this);
        patientPanel     = new PatientPanel();
        doctorPanel      = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
        recordPanel      = new MedicalRecordPanel();
        billingPanel     = new BillingPanel();

        contentArea.add(dashboardPanel,   "DASHBOARD");
        contentArea.add(patientPanel,     "PATIENTS");
        contentArea.add(doctorPanel,      "DOCTORS");
        contentArea.add(appointmentPanel, "APPOINTMENTS");
        contentArea.add(recordPanel,      "RECORDS");
        contentArea.add(billingPanel,     "BILLING");

        add(buildSidebar(), BorderLayout.WEST);
        add(contentArea,    BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(PRIMARY);
        top.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("\uD83C\uDFE5  KIGALI CENTRAL HOSPITAL  \u2014  Management System");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setForeground(WHITE);

        JLabel date = new JLabel(new SimpleDateFormat("EEEE, dd MMM yyyy").format(new Date()));
        date.setFont(new Font("Arial", Font.PLAIN, 12));
        date.setForeground(new Color(180, 220, 255));

        top.add(title, BorderLayout.WEST);
        top.add(date,  BorderLayout.EAST);
        return top;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel menu = new JLabel("  NAVIGATION");
        menu.setFont(new Font("Arial", Font.BOLD, 10));
        menu.setForeground(new Color(100, 140, 180));
        menu.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(menu);
        sidebar.add(Box.createVerticalStrut(10));

        String[][] items = {
            {"\uD83C\uDFE0", "DASHBOARD"},
            {"\uD83D\uDC64", "PATIENTS"},
            {"\uD83D\uDC68\u200D\u2695\uFE0F", "DOCTORS"},
            {"\uD83D\uDCC5", "APPOINTMENTS"},
            {"\uD83D\uDCCB", "RECORDS"},
            {"\uD83D\uDCB0", "BILLING"}
        };

        for (String[] item : items) {
            sidebar.add(makeSidebarButton(item[0] + "  " + item[1], item[1]));
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSidebarButton("\uD83D\uDEAA  LOGOUT", "LOGOUT"));
        return sidebar;
    }

    private JButton makeSidebarButton(String text, String card) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(190, 42));
        btn.setPreferredSize(new Dimension(190, 42));
        btn.setBackground(SIDEBAR);
        btn.setForeground(new Color(190, 210, 230));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 18, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(30, 80, 140)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(SIDEBAR); }
        });
        // FIX: all nav goes through navigate() so every panel refreshes on switch
        btn.addActionListener(e -> navigate(card));
        return btn;
    }

    /**
     * FIX: Central navigation method.
     * Refreshes the target panel from the live HospitalData lists BEFORE
     * showing it, so data added anywhere is immediately visible everywhere —
     * no logout/login required.
     */
    void navigate(String card) {
        switch (card) {
            case "LOGOUT":
                int c = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) { dispose(); new LoginScreen(); }
                return;
            case "DASHBOARD":    dashboardPanel.refresh();        break;
            case "PATIENTS":     patientPanel.refreshTable();     break;
            case "DOCTORS":      doctorPanel.refreshTable();      break;
            case "APPOINTMENTS": appointmentPanel.refreshTable(); break;
            case "RECORDS":      recordPanel.refreshTable();      break;
            case "BILLING":      billingPanel.refreshTable();     break;
        }
        cardLayout.show(contentArea, card);
    }

    // Called by dashboard quick-action cards
    void showPanel(String name) { navigate(name); }
}

// ========================= DASHBOARD PANEL =========================

class DashboardPanel extends JPanel {
    private MainDashboard parent;
    private JLabel lblPatients, lblDoctors, lblAppts, lblRecords;

    public DashboardPanel(MainDashboard parent) {
        this.parent = parent;
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JLabel header = new JLabel("Dashboard Overview");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);
        add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 16, 16));
        cards.setBackground(MainDashboard.BG);

        lblPatients = new JLabel();
        lblDoctors  = new JLabel();
        lblAppts    = new JLabel();
        lblRecords  = new JLabel();

        cards.add(statCard("Total Patients",  lblPatients, new Color(0, 150, 136),  "\uD83D\uDC64"));
        cards.add(statCard("Total Doctors",   lblDoctors,  new Color(63, 81, 181),  "\uD83D\uDC68\u200D\u2695\uFE0F"));
        cards.add(statCard("Appointments",    lblAppts,    new Color(255, 152, 0),  "\uD83D\uDCC5"));
        cards.add(statCard("Medical Records", lblRecords,  new Color(233, 30, 99),  "\uD83D\uDCCB"));
        cards.add(quickActionCard("Register Patient", "Click to add new patient", "\uD83D\uDC64", "PATIENTS"));
        cards.add(quickActionCard("Book Appointment", "Click to schedule",        "\uD83D\uDCC5", "APPOINTMENTS"));

        add(cards, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(MainDashboard.BG);
        JLabel info = new JLabel("  System:  Kigali Central Hospital HMS  |  Module: Advanced Java Programming  |  UoK");
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        footer.add(info, BorderLayout.WEST);
        add(footer, BorderLayout.SOUTH);

        refresh();
    }

    void refresh() {
        lblPatients.setText(String.valueOf(HospitalData.patients.size()));
        lblDoctors.setText(String.valueOf(HospitalData.doctors.size()));
        lblAppts.setText(String.valueOf(HospitalData.appointments.size()));
        lblRecords.setText(String.valueOf(HospitalData.records.size()));
    }

    private JPanel statCard(String title, JLabel numLabel, Color color, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel emojiLabel = new JLabel(emoji, SwingConstants.RIGHT);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        numLabel.setFont(new Font("Arial", Font.BOLD, 36));
        numLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.WHITE);
        left.add(numLabel,   BorderLayout.CENTER);
        left.add(titleLabel, BorderLayout.SOUTH);

        card.add(left,       BorderLayout.WEST);
        card.add(emojiLabel, BorderLayout.EAST);
        return card;
    }

    private JPanel quickActionCard(String title, String subtitle, String emoji, String target) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(MainDashboard.PRIMARY);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MainDashboard.ACCENT, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(emoji + "  " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        subtitleLabel.setForeground(new Color(180, 220, 255));

        JPanel inner = new JPanel(new BorderLayout(0, 6));
        inner.setBackground(MainDashboard.PRIMARY);
        inner.add(titleLabel,    BorderLayout.CENTER);
        inner.add(subtitleLabel, BorderLayout.SOUTH);

        card.add(inner, BorderLayout.WEST);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { parent.showPanel(target); }
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(0, 130, 180));
                inner.setBackground(new Color(0, 130, 180));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(MainDashboard.PRIMARY);
                inner.setBackground(MainDashboard.PRIMARY);
            }
        });
        return card;
    }
}

// ========================= PATIENT PANEL =========================

class PatientPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public PatientPanel() {
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setBackground(MainDashboard.BG);

        JLabel header = new JLabel("Patient Management");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);

        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5, 8, 5, 8)));

        JButton btnSearch = makeBtn("\uD83D\uDD0D Search",   new Color(63, 81, 181));
        JButton btnShowAll= makeBtn("\u21A9 Show All",       new Color(90, 90, 90));
        JButton btnAdd    = makeBtn("\u2795 New Patient",    new Color(0, 150, 136));
        JButton btnDisch  = makeBtn("\u2705 Discharge",      new Color(255, 152, 0));
        JButton btnDel    = makeBtn("\uD83D\uDDD1 Delete",   new Color(211, 47, 47));

        // FIX: search filters the live list, not just current table rows
        btnSearch.addActionListener(e -> applySearch());
        // FIX: Show All reloads full list and clears the search box
        btnShowAll.addActionListener(e -> { searchField.setText(""); refreshTable(); });
        btnAdd.addActionListener(e -> addPatient());
        btnDisch.addActionListener(e -> dischargePatient());
        btnDel.addActionListener(e -> deletePatient());

        // Live-filter as user types
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applySearch(); }
        });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.setBackground(MainDashboard.BG);
        btns.add(new JLabel("Search:"));
        btns.add(searchField);
        btns.add(btnSearch);
        btns.add(btnShowAll);
        btns.add(btnAdd);
        btns.add(btnDisch);
        btns.add(btnDel);

        top.add(header, BorderLayout.WEST);
        top.add(btns,   BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Age", "Gender", "Phone", "Blood", "Admission", "Status", "Bill (FRW)"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    // FIX: public — called by MainDashboard.navigate() every time PATIENTS is clicked
    void refreshTable() {
        searchField.setText(""); // reset search box so full list shows
        loadRows(HospitalData.patients);
    }

    // FIX: filter always runs on the live HospitalData list
    private void applySearch() {
        String q = searchField.getText().trim().toLowerCase();
        if (q.isEmpty()) { loadRows(HospitalData.patients); return; }
        List<Patient> result = new ArrayList<>();
        for (Patient p : HospitalData.patients) {
            if (p.getName().toLowerCase().contains(q)
             || String.valueOf(p.getId()).contains(q)
             || p.getPhone().contains(q)
             || p.getBloodGroup().toLowerCase().contains(q)
             || p.getStatus().toLowerCase().contains(q)) {
                result.add(p);
            }
        }
        loadRows(result);
    }

    private void loadRows(List<Patient> list) {
        model.setRowCount(0);
        for (Patient p : list) {
            model.addRow(new Object[]{
                p.getId(), p.getName(), p.getAge(), p.getGender(),
                p.getPhone(), p.getBloodGroup(), p.getAdmissionDate(),
                p.getStatus(), String.format("%,.2f", p.getTotalBill())
            });
        }
    }

    private void addPatient() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Register New Patient", true);
        dlg.setSize(400, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 10));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField fName  = new JTextField();
        JTextField fAge   = new JTextField();
        JComboBox<String> fGender = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField fPhone = new JTextField();
        JComboBox<String> fBlood  = new JComboBox<>(
                new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-"});
        JTextField fAddr  = new JTextField();

        addField(form, "Full Name:",   fName);
        addField(form, "Age:",         fAge);
        addField(form, "Gender:",      fGender);
        addField(form, "Phone:",       fPhone);
        addField(form, "Blood Group:", fBlood);
        addField(form, "Address:",     fAddr);

        JButton save = makeBtn("Save Patient", new Color(0, 150, 136));
        save.addActionListener(e -> {
            try {
                String name = fName.getText().trim();
                int age = Integer.parseInt(fAge.getText().trim());
                String gender = (String) fGender.getSelectedItem();
                String phone  = fPhone.getText().trim();
                String blood  = (String) fBlood.getSelectedItem();
                String addr   = fAddr.getText().trim();
                if (name.isEmpty() || phone.isEmpty())
                    throw new Exception("Name and Phone are required.");

                Patient p = new Patient(name, age, gender, phone, blood, addr);
                HospitalData.patients.add(p);
                refreshTable(); // FIX: new patient appears immediately
                dlg.dispose();
                JOptionPane.showMessageDialog(this,
                        "\u2705 Patient registered with ID: " + p.getId());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Age must be a number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(save);

        dlg.add(form,     BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient first."); return; }
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete patient ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            HospitalData.patients.removeIf(p -> p.getId() == id);
            refreshTable();
        }
    }

    private void dischargePatient() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient first."); return; }
        int id = (int) model.getValueAt(row, 0);
        for (Patient p : HospitalData.patients) {
            if (p.getId() == id) {
                if (p.getStatus().equals("Discharged")) {
                    JOptionPane.showMessageDialog(this, "Patient is already discharged.");
                    return;
                }
                p.setStatus("Discharged");
                break;
            }
        }
        refreshTable();
        JOptionPane.showMessageDialog(this, "\u2705 Patient discharged successfully.");
    }

    private void addField(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(lbl);
        panel.add(field);
    }

    static void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.getTableHeader().setBackground(MainDashboard.PRIMARY);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(180, 220, 255));
        t.setGridColor(new Color(220, 220, 220));
        t.setShowGrid(true);
    }

    static JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}

// ========================= DOCTOR PANEL =========================

class DoctorPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public DoctorPanel() {
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JLabel header = new JLabel("Doctor Directory");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Specialization", "Phone", "Availability"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PatientPanel.styleTable(table);

        JButton toggleBtn = PatientPanel.makeBtn(
                "\uD83D\uDD04 Toggle Availability", new Color(255, 152, 0));
        toggleBtn.addActionListener(e -> toggleAvailability());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(MainDashboard.BG);
        btns.add(toggleBtn);

        add(btns,                       BorderLayout.SOUTH);
        add(new JScrollPane(table),     BorderLayout.CENTER);

        refreshTable();
    }

    // FIX: public — called by MainDashboard.navigate()
    void refreshTable() {
        model.setRowCount(0);
        for (Doctor d : HospitalData.doctors) {
            model.addRow(new Object[]{
                d.getId(), d.getName(), d.getSpecialization(), d.getPhone(),
                d.isAvailable() ? "\u2705 Available" : "\u274C Busy"
            });
        }
    }

    private void toggleAvailability() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a doctor first."); return; }
        int id = (int) model.getValueAt(row, 0);
        for (Doctor d : HospitalData.doctors) {
            if (d.getId() == id) {
                d.setAvailable(!d.isAvailable());
                model.setValueAt(d.isAvailable() ? "\u2705 Available" : "\u274C Busy", row, 4);
                break;
            }
        }
    }
}

// ========================= APPOINTMENT PANEL =========================

class AppointmentPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public AppointmentPanel() {
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(MainDashboard.BG);

        JLabel header = new JLabel("Appointment Scheduling");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);

        JButton btnNew      = PatientPanel.makeBtn("\uD83D\uDCC5 Book Appointment", new Color(0, 150, 136));
        JButton btnCancel   = PatientPanel.makeBtn("\u274C Cancel",                 new Color(211, 47, 47));
        JButton btnComplete = PatientPanel.makeBtn("\u2705 Mark Completed",         new Color(63, 81, 181));

        btnNew.addActionListener(e -> bookAppointment());
        btnCancel.addActionListener(e -> cancelAppointment());
        btnComplete.addActionListener(e -> completeAppointment());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.setBackground(MainDashboard.BG);
        btns.add(btnNew); btns.add(btnComplete); btns.add(btnCancel);

        top.add(header, BorderLayout.WEST);
        top.add(btns,   BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Appt ID", "Patient", "Doctor", "Date", "Time", "Reason", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PatientPanel.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    // FIX: public — called by MainDashboard.navigate()
    void refreshTable() {
        model.setRowCount(0);
        for (Appointment a : HospitalData.appointments) {
            model.addRow(new Object[]{
                a.getId(), a.getPatientName(), a.getDoctorName(),
                a.getDate(), a.getTime(), a.getReason(), a.getStatus()
            });
        }
    }

    private void bookAppointment() {
        if (HospitalData.patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients registered yet."); return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Book Appointment", true);
        dlg.setSize(400, 350);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        // FIX: dropdowns built from live list at dialog-open time
        String[] pNames = HospitalData.patients.stream()
                .map(p -> p.getId() + " - " + p.getName()).toArray(String[]::new);
        String[] dNames = HospitalData.doctors.stream()
                .map(d -> d.getId() + " - " + d.getName()).toArray(String[]::new);

        JComboBox<String> cPatient = new JComboBox<>(pNames);
        JComboBox<String> cDoctor  = new JComboBox<>(dNames);
        JTextField fDate   = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        JTextField fTime   = new JTextField("09:00");
        JTextField fReason = new JTextField();

        form.add(new JLabel("Patient:"));          form.add(cPatient);
        form.add(new JLabel("Doctor:"));           form.add(cDoctor);
        form.add(new JLabel("Date (yyyy-MM-dd):")); form.add(fDate);
        form.add(new JLabel("Time (HH:mm):"));     form.add(fTime);
        form.add(new JLabel("Reason:"));           form.add(fReason);

        JButton save = PatientPanel.makeBtn("Book", new Color(0, 150, 136));
        save.addActionListener(e -> {
            int pIdx = cPatient.getSelectedIndex();
            int dIdx = cDoctor.getSelectedIndex();
            Patient p = HospitalData.patients.get(pIdx);
            Doctor d  = HospitalData.doctors.get(dIdx);
            String reason = fReason.getText().trim();
            if (reason.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Enter a reason."); return; }
            Appointment a = new Appointment(p.getId(), p.getName(), d.getId(), d.getName(),
                    fDate.getText().trim(), fTime.getText().trim(), reason);
            HospitalData.appointments.add(a);
            refreshTable(); // FIX: update table right away
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "\u2705 Appointment booked! ID: " + a.getId());
        });

        JPanel btnPnl = new JPanel();
        btnPnl.setBackground(Color.WHITE);
        btnPnl.add(save);
        dlg.add(form,   BorderLayout.CENTER);
        dlg.add(btnPnl, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void cancelAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment."); return; }
        int id = (int) model.getValueAt(row, 0);
        for (Appointment a : HospitalData.appointments)
            if (a.getId() == id) { a.setStatus("Cancelled"); break; }
        refreshTable();
    }

    private void completeAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment."); return; }
        int id = (int) model.getValueAt(row, 0);
        for (Appointment a : HospitalData.appointments)
            if (a.getId() == id) { a.setStatus("Completed"); break; }
        refreshTable();
    }
}

// ========================= MEDICAL RECORDS PANEL =========================

class MedicalRecordPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public MedicalRecordPanel() {
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(MainDashboard.BG);

        JLabel header = new JLabel("Medical Records");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);

        JButton btnAdd = PatientPanel.makeBtn("\uD83D\uDCCB Add Record", new Color(233, 30, 99));
        btnAdd.addActionListener(e -> addRecord());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(MainDashboard.BG);
        btns.add(btnAdd);

        top.add(header, BorderLayout.WEST);
        top.add(btns,   BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Record ID", "Patient", "Doctor", "Diagnosis", "Prescription", "Date", "Notes"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PatientPanel.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    // FIX: public — called by MainDashboard.navigate()
    void refreshTable() {
        model.setRowCount(0);
        for (MedicalRecord r : HospitalData.records) {
            model.addRow(new Object[]{
                r.getId(), r.getPatientName(), r.getDoctorName(),
                r.getDiagnosis(), r.getPrescription(), r.getDate(), r.getNotes()
            });
        }
    }

    private void addRecord() {
        if (HospitalData.patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients yet."); return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Add Medical Record", true);
        dlg.setSize(450, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        // FIX: live patient list
        String[] pNames = HospitalData.patients.stream()
                .map(p -> p.getId() + " - " + p.getName()).toArray(String[]::new);
        String[] dNames = HospitalData.doctors.stream()
                .map(Doctor::getName).toArray(String[]::new);

        JComboBox<String> cPatient      = new JComboBox<>(pNames);
        JComboBox<String> cDoctor       = new JComboBox<>(dNames);
        JTextField fDiagnosis     = new JTextField();
        JTextField fPrescription  = new JTextField();
        JTextField fNotes         = new JTextField();

        form.add(new JLabel("Patient:"));      form.add(cPatient);
        form.add(new JLabel("Doctor:"));       form.add(cDoctor);
        form.add(new JLabel("Diagnosis:"));    form.add(fDiagnosis);
        form.add(new JLabel("Prescription:")); form.add(fPrescription);
        form.add(new JLabel("Notes:"));        form.add(fNotes);

        JButton save = PatientPanel.makeBtn("Save Record", new Color(233, 30, 99));
        save.addActionListener(e -> {
            int pIdx  = cPatient.getSelectedIndex();
            Patient p = HospitalData.patients.get(pIdx);
            String doctor = (String) cDoctor.getSelectedItem();
            String diag   = fDiagnosis.getText().trim();
            String presc  = fPrescription.getText().trim();
            String notes  = fNotes.getText().trim();
            if (diag.isEmpty() || presc.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Fill diagnosis & prescription."); return;
            }
            MedicalRecord rec = new MedicalRecord(
                    p.getId(), p.getName(), doctor, diag, presc, notes);
            HospitalData.records.add(rec);
            refreshTable(); // FIX: update immediately
            dlg.dispose();
            JOptionPane.showMessageDialog(this,
                    "\u2705 Medical record saved! ID: " + rec.getId());
        });

        JPanel bp = new JPanel();
        bp.setBackground(Color.WHITE);
        bp.add(save);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(bp,   BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
}

// ========================= BILLING PANEL =========================

class BillingPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private JTextField amountField;

    public BillingPanel() {
        setBackground(MainDashboard.BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(MainDashboard.BG);

        JLabel header = new JLabel("Patient Billing");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(MainDashboard.PRIMARY);

        amountField = new JTextField(10);
        amountField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5, 8, 5, 8)));

        JButton btnAdd     = PatientPanel.makeBtn("\uD83D\uDCB0 Add Charge",   new Color(255, 152, 0));
        JButton btnReceipt = PatientPanel.makeBtn("\uD83D\uDDB8 Print Receipt", new Color(63, 81, 181));
        btnAdd.addActionListener(e -> addCharge());
        btnReceipt.addActionListener(e -> printReceipt());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.setBackground(MainDashboard.BG);
        btns.add(new JLabel("Amount (FRW):")); btns.add(amountField);
        btns.add(btnAdd); btns.add(btnReceipt);

        top.add(header, BorderLayout.WEST);
        top.add(btns,   BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Patient ID", "Name", "Status", "Total Bill (FRW)"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PatientPanel.styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    // FIX: public — called by MainDashboard.navigate()
    void refreshTable() {
        model.setRowCount(0);
        for (Patient p : HospitalData.patients) {
            model.addRow(new Object[]{
                p.getId(), p.getName(), p.getStatus(),
                String.format("%,.2f", p.getTotalBill())
            });
        }
    }

    private void addCharge() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient."); return; }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
            int id = (int) model.getValueAt(row, 0);
            for (Patient p : HospitalData.patients)
                if (p.getId() == id) { p.addToBill(amount); break; }
            amountField.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "\u2705 Charge of FRW " + String.format("%,.2f", amount) + " added.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive amount.");
        }
    }

    private void printReceipt() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient."); return; }
        int id = (int) model.getValueAt(row, 0);
        Patient selected = null;
        for (Patient p : HospitalData.patients)
            if (p.getId() == id) { selected = p; break; }
        if (selected == null) return;

        String receipt =
            "============================================\n" +
            "       KIGALI CENTRAL HOSPITAL\n" +
            "       Patient Billing Receipt\n" +
            "============================================\n" +
            "Patient ID   : " + selected.getId() + "\n" +
            "Name         : " + selected.getName() + "\n" +
            "Age          : " + selected.getAge() + "\n" +
            "Blood Group  : " + selected.getBloodGroup() + "\n" +
            "Admission    : " + selected.getAdmissionDate() + "\n" +
            "Status       : " + selected.getStatus() + "\n" +
            "--------------------------------------------\n" +
            "TOTAL BILL   : FRW " + String.format("%,.2f", selected.getTotalBill()) + "\n" +
            "============================================\n" +
            "Date Issued  : " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "\n" +
            "Prepared by  : HMS System\n";

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, sp,
                "Receipt - " + selected.getName(), JOptionPane.PLAIN_MESSAGE);

        try {
            File f = new File("receipt_" + selected.getId() + ".txt");
            FileWriter fw = new FileWriter(f);
            fw.write(receipt);
            fw.close();
            JOptionPane.showMessageDialog(this, "Receipt saved as: " + f.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save receipt: " + ex.getMessage());
        }
    }
}