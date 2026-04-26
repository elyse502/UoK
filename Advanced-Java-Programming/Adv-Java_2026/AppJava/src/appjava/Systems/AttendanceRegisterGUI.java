package appjava.Systems;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceRegisterGUI {
    private static final String SCHOOL_NAME = "UoK";
    private static final String DEPARTMENT = "Department of Computer Science, SWE";
    private static final String OPTION = "Advanced Java Programming";
    private static final String LECTURER = "Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String CURRENT_DATE = "Date: " + DATE_FORMAT.format(new Date());

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField studentNoField;
    private JTextField dayField;
    private List<Student> students;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AttendanceRegisterGUI window = new AttendanceRegisterGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AttendanceRegisterGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Attendance Register created by Dr. Josbert");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLUE);
        frame.setLayout(new BorderLayout());

        // Main panel to hold all content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLUE);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLUE);
        JLabel titleLabel = new JLabel("ATTENDANCE SOFTWARE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // School Info Panel (placed right after title)
        JPanel schoolInfoPanel = new JPanel();
        schoolInfoPanel.setBackground(Color.BLUE);
        schoolInfoPanel.setLayout(new BoxLayout(schoolInfoPanel, BoxLayout.Y_AXIS));
        
        // Add vertical space after title
        schoolInfoPanel.add(Box.createVerticalStrut(10));
        
        // School information labels
        JLabel schoolLabel = createInfoLabel(SCHOOL_NAME);
        JLabel deptLabel = createInfoLabel(DEPARTMENT);
        JLabel optionLabel = createInfoLabel(OPTION);
        JLabel dateLabel = createInfoLabel(CURRENT_DATE);
        JLabel lecturerLabel = createInfoLabel(LECTURER);
        
        schoolInfoPanel.add(schoolLabel);
        schoolInfoPanel.add(deptLabel);
        schoolInfoPanel.add(optionLabel);
        schoolInfoPanel.add(dateLabel);
        schoolInfoPanel.add(lecturerLabel);
        
        // Add school info to main panel
        mainPanel.add(schoolInfoPanel, BorderLayout.CENTER);

        // Table setup
        String[] columnNames = createColumnNames();
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 4 && columnIndex < 19) {
                    return Integer.class;
                } else if (columnIndex == 19) {
                    return String.class;
                }
                return Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4 && column < 19;
            }
        };
        
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Input Panel
        JPanel inputPanel = createInputPanel();
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Populate with sample data
        initializeSampleData();

        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private String[] createColumnNames() {
        String[] columnNames = new String[]{"No.", "Student No.", "First Name", "Surname"};
        for (int i = 1; i <= 15; i++) {
            columnNames = addToArray(columnNames, "Day " + i);
        }
        columnNames = addToArray(columnNames, "Attendance %");
        return columnNames;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.BLUE);
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        
        // Student Number Field
        JLabel studentNoLabel = new JLabel("Student No.:");
        studentNoLabel.setForeground(Color.WHITE);
        studentNoField = new JTextField();
        
        // Day Field
        JLabel dayLabel = new JLabel("Day:");
        dayLabel.setForeground(Color.WHITE);
        dayField = new JTextField();
        
        inputPanel.add(studentNoLabel);
        inputPanel.add(studentNoField);
        inputPanel.add(dayLabel);
        inputPanel.add(dayField);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLUE);
        
        // Create buttons
        JButton markAttendanceButton = createButton("Mark Attendance", e -> markAttendance());
        JButton addStudentButton = createButton("Add Student", e -> openAddStudentDialog());
        JButton deleteStudentButton = createButton("Delete Student", e -> deleteStudent());
        JButton calculateButton = createButton("Calculate Attendance", e -> calculateAndCheckEligibility());
        JButton printButton = createButton("Print to Notepad", e -> printToNotepad());
        
        // Add buttons to panel
        buttonPanel.add(markAttendanceButton);
        buttonPanel.add(addStudentButton);
        buttonPanel.add(deleteStudentButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(printButton);
        
        inputPanel.add(new JLabel());
        inputPanel.add(buttonPanel);
        
        return inputPanel;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(Color.GREEN);
        button.setForeground(Color.WHITE);
        button.addActionListener(action);
        return button;
    }

    private void initializeSampleData() {
        students = getSampleStudents();
        for (Student student : students) {
            Object[] rowData = new Object[20];
            rowData[0] = student.getNo();
            rowData[1] = student.getStudentNo();
            rowData[2] = student.getFirstName();
            rowData[3] = student.getSurname();
            for (int i = 4; i < 19; i++) {
                rowData[i] = 0;
            }
            rowData[19] = "0.00%";
            tableModel.addRow(rowData);
        }
    }

    private String[] addToArray(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

    private List<Student> getSampleStudents() {
        List<Student> sampleStudents = new ArrayList<>();
        sampleStudents.add(new Student(1, 2111, "Olivier", "KARENZI"));
        sampleStudents.add(new Student(2, 2222, "Jeanne", "UWIRINGIYIMANA"));
        sampleStudents.add(new Student(3, 2333, "Rachel", "MUKESHIMANA"));
        sampleStudents.add(new Student(4, 2444, "Jehovanis", "KIGELI"));
        return sampleStudents;
    }

    private int findStudentRow(int studentNo) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(studentNo)) {
                return i;
            }
        }
        return -1;
    }

    private void markAttendance() {
        try {
            int studentNo = Integer.parseInt(studentNoField.getText());
            int day = Integer.parseInt(dayField.getText());
            
            if (day < 1 || day > 15) {
                JOptionPane.showMessageDialog(frame, "Day must be between 1 and 15.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int row = findStudentRow(studentNo);
            if (row >= 0) {
                tableModel.setValueAt(1, row, 3 + day);
                JOptionPane.showMessageDialog(frame, "Attendance marked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Student No. and Day.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddStudentDialog() {
        JDialog addStudentDialog = new JDialog(frame, "Add New Student", true);
        addStudentDialog.setLayout(new GridLayout(4, 2, 5, 5));
        addStudentDialog.getContentPane().setBackground(Color.BLUE);
        
        JLabel studentNoLabel = new JLabel("Student No.:");
        studentNoLabel.setForeground(Color.WHITE);
        JTextField studentNoEntry = new JTextField();
        
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setForeground(Color.WHITE);
        JTextField firstNameEntry = new JTextField();
        
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setForeground(Color.WHITE);
        JTextField surnameEntry = new JTextField();
        
        JButton addButton = new JButton("Add Student");
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            try {
                int studentNo = Integer.parseInt(studentNoEntry.getText());
                String firstName = firstNameEntry.getText();
                String surname = surnameEntry.getText();
                
                if (findStudentRow(studentNo) == -1) {
                    int newRow = students.size() + 1;
                    students.add(new Student(newRow, studentNo, firstName, surname));
                    
                    Object[] rowData = new Object[20];
                    rowData[0] = newRow;
                    rowData[1] = studentNo;
                    rowData[2] = firstName;
                    rowData[3] = surname;
                    for (int i = 4; i < 19; i++) {
                        rowData[i] = 0;
                    }
                    rowData[19] = "0.00%";
                    tableModel.addRow(rowData);
                    
                    JOptionPane.showMessageDialog(addStudentDialog, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addStudentDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(addStudentDialog, "Student number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addStudentDialog, "Please enter a valid student number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        addStudentDialog.add(studentNoLabel);
        addStudentDialog.add(studentNoEntry);
        addStudentDialog.add(firstNameLabel);
        addStudentDialog.add(firstNameEntry);
        addStudentDialog.add(surnameLabel);
        addStudentDialog.add(surnameEntry);
        addStudentDialog.add(new JLabel());
        addStudentDialog.add(addButton);
        
        addStudentDialog.pack();
        addStudentDialog.setLocationRelativeTo(frame);
        addStudentDialog.setVisible(true);
    }

    private void deleteStudent() {
        try {
            int studentNoToDelete = Integer.parseInt(studentNoField.getText());
            int row = findStudentRow(studentNoToDelete);
            if (row >= 0) {
                tableModel.removeRow(row);
                students.remove(row);
                JOptionPane.showMessageDialog(frame, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid student number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateAttendancePercentage() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int attendanceCount = 0;
            for (int day = 4; day < 19; day++) {
                attendanceCount += (int) tableModel.getValueAt(row, day);
            }
            double percentage = (attendanceCount / 15.0) * 100;
            tableModel.setValueAt(String.format("%.2f%%", percentage), row, 19);
        }
    }

    private void displayExamEligibility() {
        StringBuilder allowedStudents = new StringBuilder("Students allowed to sit for the exam:\n");
        StringBuilder notAllowedStudents = new StringBuilder("Students NOT allowed to sit for the exam:\n");
        boolean hasAllowed = false;
        boolean hasNotAllowed = false;
        
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String percentageStr = (String) tableModel.getValueAt(row, 19);
            double percentage = Double.parseDouble(percentageStr.replace("%", ""));
            
            int studentNo = (int) tableModel.getValueAt(row, 1);
            String firstName = (String) tableModel.getValueAt(row, 2);
            String surname = (String) tableModel.getValueAt(row, 3);
            
            String studentInfo = String.format("Student No: %d (%s %s) • %.2f%%", 
                studentNo, firstName, surname, percentage);
            
            if (percentage >= 80) {
                allowedStudents.append(studentInfo).append("\n");
                hasAllowed = true;
            } else {
                notAllowedStudents.append(studentInfo).append("\n");
                hasNotAllowed = true;
            }
        }
        
        String message = (hasAllowed ? allowedStudents.toString() : "No students are allowed to sit for the exam.\n") + 
                         "\n" + 
                         (hasNotAllowed ? notAllowedStudents.toString() : "All students are allowed to sit for the exam.");
        
        JOptionPane.showMessageDialog(frame, message, "Exam Eligibility", JOptionPane.INFORMATION_MESSAGE);
    }

    private void calculateAndCheckEligibility() {
        calculateAttendancePercentage();
        displayExamEligibility();
    }

    private void printToNotepad() {
        try {
            File file = new File("attendance_report.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            
            // Write header
            writer.write("ATTENDANCE REPORT\n");
            writer.write("================\n\n");
            writer.write(SCHOOL_NAME + "\n");
            writer.write(DEPARTMENT + "\n");
            writer.write(OPTION + "\n");
            writer.write(LECTURER + "\n");
            writer.write(CURRENT_DATE + "\n\n");
            
            // Write column headers
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                writer.write(String.format("%-15s", tableModel.getColumnName(col)));
            }
            writer.write("\n");
            
            // Write table data
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    writer.write(String.format("%-15s", tableModel.getValueAt(row, col).toString()));
                }
                writer.write("\n");
            }
            
            writer.close();
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().edit(file);
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "File saved as: " + file.getAbsolutePath(), 
                    "Print to Notepad", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error creating report: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Student {
        private int no;
        private int studentNo;
        private String firstName;
        private String surname;

        public Student(int no, int studentNo, String firstName, String surname) {
            this.no = no;
            this.studentNo = studentNo;
            this.firstName = firstName;
            this.surname = surname;
        }

        public int getNo() { return no; }
        public int getStudentNo() { return studentNo; }
        public String getFirstName() { return firstName; }
        public String getSurname() { return surname; }
    }
}