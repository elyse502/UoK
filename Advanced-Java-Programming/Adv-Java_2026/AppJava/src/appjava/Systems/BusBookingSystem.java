package appjava.Systems;
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

public class BusBookingSystem extends JFrame {

    // ── State ──────────────────────────────────────────────────────────────────
    private int remainingSeats = 20;
    private int bookedTickets  = 0;
    private final ArrayList<Ticket> ticketList = new ArrayList<>();

    // ── Form inputs ────────────────────────────────────────────────────────────
    private JTextField nameInput, phoneInput, priceInput, codeInput;
    private JComboBox<String> statusInput, routeInput;
    private JSpinner dateInput, timeInput;

    // ── Display components ─────────────────────────────────────────────────────
    private JLabel remainingSeatsLabel;
    private JLabel availableTicketsValue;
    private JLabel bookedTicketsValue;
    private JTable ticketTable;
    private DefaultTableModel tableModel;

    // ══════════════════════════════════════════════════════════════════════════
    public BusBookingSystem() {
        setTitle("RITCO COMPANY TICKET BOOKING");
        setSize(1200, 800);
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
        main.add(buildTablePanel(),  BorderLayout.CENTER);
        main.add(buildRightPanel(),  BorderLayout.EAST);
    }

    // ── Title ──────────────────────────────────────────────────────────────────
    private JPanel buildTitlePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        JLabel lbl = new JLabel("RITCO COMPANY BOOKING TICKET");
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.BLUE);
        p.add(lbl);
        return p;
    }

    // ── Right panel: cards + form ──────────────────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        p.add(buildCardsPanel(), BorderLayout.NORTH);
        p.add(buildFormPanel(),  BorderLayout.CENTER);
        return p;
    }

    // ── Info cards ─────────────────────────────────────────────────────────────
    private JPanel buildCardsPanel() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        cards.setOpaque(false);

        // Available Buses card (static)
        cards.add(makeStaticCard("Available Buses", "5"));

        // Available Tickets card (dynamic label)
        availableTicketsValue = new JLabel(String.valueOf(remainingSeats));
        availableTicketsValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        availableTicketsValue.setForeground(Color.WHITE);
        availableTicketsValue.setFont(new Font("Arial", Font.BOLD, 16));
        cards.add(makeDynamicCard("Available Tickets", availableTicketsValue));

        // Booked Tickets card (dynamic label)
        bookedTicketsValue = new JLabel(String.valueOf(bookedTickets));
        bookedTicketsValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookedTicketsValue.setForeground(Color.WHITE);
        bookedTicketsValue.setFont(new Font("Arial", Font.BOLD, 16));
        cards.add(makeDynamicCard("Booked Tickets", bookedTicketsValue));

        remainingSeatsLabel = new JLabel("Remaining Seats: " + remainingSeats,
                                         SwingConstants.CENTER);
        remainingSeatsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        remainingSeatsLabel.setForeground(Color.BLUE);

        wrap.add(cards,               BorderLayout.CENTER);
        wrap.add(remainingSeatsLabel, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel makeStaticCard(String title, String value) {
        return makeDynamicCard(title, labelFor(value));
    }

    private JLabel labelFor(String value) {
        JLabel l = new JLabel(value);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 16));
        return l;
    }

    private JPanel makeDynamicCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setBackground(Color.BLUE);
        card.setPreferredSize(new Dimension(90, 60));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 10));

        card.add(titleLbl);
        card.add(valueLabel);
        return card;
    }

    // ── Booking form ───────────────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridLayout(9, 2, 5, 5));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameInput  = new JTextField();
        phoneInput = new JTextField();
        priceInput = new JTextField();
        codeInput  = new JTextField();
        codeInput.setEditable(false);

        statusInput = new JComboBox<>(new String[]{"Paid", "Pending"});
        routeInput  = new JComboBox<>(new String[]{
            "Kigali - Muhanga", "Muhanga - Kigali",
            "Kigali - Musanze", "Musanze - Kigali",
            "Kigali - Rubavu",  "Rubavu - Kigali"
        });

        dateInput = new JSpinner(new SpinnerDateModel());
        dateInput.setEditor(new JSpinner.DateEditor(dateInput, "yyyy-MM-dd"));
        dateInput.setValue(new Date());

        timeInput = new JSpinner(new SpinnerDateModel());
        timeInput.setEditor(new JSpinner.DateEditor(timeInput, "HH:mm"));
        timeInput.setValue(new Date());

        generateTicketCode();

        JButton bookBtn = new JButton("Book Ticket");
        bookBtn.setBackground(Color.BLUE);
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Arial", Font.BOLD, 12));
        bookBtn.setFocusPainted(false);
        bookBtn.addActionListener(e -> bookTicket());

        p.add(new JLabel("Name:"));          p.add(nameInput);
        p.add(new JLabel("Phone Number:"));  p.add(phoneInput);
        p.add(new JLabel("Price (RWF):"));   p.add(priceInput);
        p.add(new JLabel("Status:"));        p.add(statusInput);
        p.add(new JLabel("Route:"));         p.add(routeInput);
        p.add(new JLabel("Date:"));          p.add(dateInput);
        p.add(new JLabel("Time:"));          p.add(timeInput);
        p.add(new JLabel("Ticket Code:"));   p.add(codeInput);
        p.add(new JPanel());                 p.add(bookBtn);

        return p;
    }

    // ── Table + Print button ───────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        // Table model
        String[] cols = {"Ticket Code","Name","Phone","Price","Status","Route","Date","Time"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        ticketTable = new JTable(tableModel);
        ticketTable.setFillsViewportHeight(true);
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketTable.setRowHeight(25);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] widths = {100, 150, 120, 80, 80, 150, 100, 80};
        for (int i = 0; i < widths.length; i++)
            ticketTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                showTicketDetails(ticketTable.getSelectedRow());
        });

        JTableHeader header = ticketTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.BLUE);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(ticketTable);
        scroll.setPreferredSize(new Dimension(700, 430));

        // Title
        JLabel title = new JLabel("BOOKED TICKETS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.BLUE);

        // Print button
        JButton printBtn = new JButton("Print Ticket  (Notepad)");
        printBtn.setBackground(new Color(0, 128, 0));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFont(new Font("Arial", Font.BOLD, 13));
        printBtn.setFocusPainted(false);
        printBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        printBtn.addActionListener(e -> printTicket());

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnBar.setOpaque(false);
        btnBar.add(printBtn);

        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnBar, BorderLayout.SOUTH);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BOOKING LOGIC
    // ══════════════════════════════════════════════════════════════════════════
    private void bookTicket() {
        String name  = nameInput.getText().trim();
        String phone = phoneInput.getText().trim();
        String price = priceInput.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in Name, Phone and Price.",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (remainingSeats <= 0) {
            JOptionPane.showMessageDialog(this,
                "All seats are fully booked.",
                "No Seats Available", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) statusInput.getSelectedItem();
        String route  = (String) routeInput.getSelectedItem();
        String date   = new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue());
        String time   = new SimpleDateFormat("HH:mm").format(timeInput.getValue());
        String code   = codeInput.getText();

        Ticket t = new Ticket(code, name, phone, price, status, route, date, time);
        ticketList.add(t);

        remainingSeats--;
        bookedTickets++;

        // Refresh table
        tableModel.addRow(new Object[]{
            t.code, t.name, t.phone, t.price,
            t.status, t.route, t.date, t.time
        });

        // Refresh card labels (no component traversal needed)
        availableTicketsValue.setText(String.valueOf(remainingSeats));
        bookedTicketsValue.setText(String.valueOf(bookedTickets));
        remainingSeatsLabel.setText("Remaining Seats: " + remainingSeats);

        clearForm();
        JOptionPane.showMessageDialog(this,
            "Ticket booked successfully!\nCode: " + code,
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        nameInput.setText("");
        phoneInput.setText("");
        priceInput.setText("");
        dateInput.setValue(new Date());
        timeInput.setValue(new Date());
        statusInput.setSelectedIndex(0);
        routeInput.setSelectedIndex(0);
        generateTicketCode();
    }

    private void generateTicketCode() {
        codeInput.setText("TKT-" + (new Random().nextInt(9000) + 1000));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SHOW TICKET DETAILS DIALOG
    // ══════════════════════════════════════════════════════════════════════════
    private void showTicketDetails(int row) {
        if (row < 0 || row >= ticketList.size()) return;
        Ticket t = ticketList.get(row);

        JPanel header = new JPanel();
        header.setBackground(Color.BLUE);
        JLabel hLbl = new JLabel("RITCO COMPANY", SwingConstants.CENTER);
        hLbl.setFont(new Font("Arial", Font.BOLD, 18));
        hLbl.setForeground(Color.WHITE);
        header.add(hLbl);

        JPanel info = new JPanel(new GridLayout(10, 2, 5, 5));
        info.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addDetailRow(info, "Company Name:", "RITCO");
        addDetailRow(info, "Email:",        "ritco@gmail.com");
        addDetailRow(info, "Tel:",          "+250 078000000");
        addDetailRow(info, "Route:",        t.route);
        addDetailRow(info, "Date:",         t.date);
        addDetailRow(info, "Time:",         t.time);
        addDetailRow(info, "Ticket Code:",  t.code);
        addDetailRow(info, "Passenger:",    t.name);
        addDetailRow(info, "Price:",        t.price + " RWF");
        addDetailRow(info, "Cashier:",      "Mukamana Aline");

        JPanel footer = new JPanel();
        footer.setBackground(Color.BLUE);
        JLabel fLbl = new JLabel("THANK YOU FOR BOOKING WITH US!", SwingConstants.CENTER);
        fLbl.setFont(new Font("Arial", Font.BOLD, 13));
        fLbl.setForeground(Color.WHITE);
        footer.add(fLbl);

        JPanel dialog = new JPanel(new BorderLayout());
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(info,   BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, dialog,
            "Ticket Details — " + t.code, JOptionPane.PLAIN_MESSAGE);
    }

    private void addDetailRow(JPanel p, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(l);
        p.add(v);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRINT TICKET → NOTEPAD
    // ══════════════════════════════════════════════════════════════════════════
    private void printTicket() {
        int row = ticketTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please click a ticket row in the table first,\nthen press Print.",
                "No Ticket Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Ticket t = ticketList.get(row);
        String content = formatTicket(t);

        try {
            File tmp = File.createTempFile("RITCO_" + t.code + "_", ".txt");
            tmp.deleteOnExit();

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"))) {
                bw.write(content);
            }

            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            if (os.contains("win")) {
                pb = new ProcessBuilder("notepad.exe", tmp.getAbsolutePath());
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", "-e", tmp.getAbsolutePath());
            } else {
                // Try common Linux text editors
                String editor = findLinuxEditor();
                pb = new ProcessBuilder(editor, tmp.getAbsolutePath());
            }
            pb.start();

            JOptionPane.showMessageDialog(this,
                "Ticket sent to Notepad!\nFile: " + tmp.getName(),
                "Printed", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Could not open Notepad:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String findLinuxEditor() {
        String[] editors = {"gedit","mousepad","leafpad","xed","kate","nano","xdg-open"};
        for (String e : editors) {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"which", e});
                p.waitFor();
                if (p.exitValue() == 0) return e;
            } catch (Exception ignored) {}
        }
        return "xdg-open";
    }

    private String formatTicket(Ticket t) {
        String D = "================================================";
        String H = "################################################";
        String nl = "\r\n";   // Windows Notepad needs CRLF
        return  H + nl
              + "           RITCO COMPANY                       " + nl
              + "         BUS TICKET RECEIPT                    " + nl
              + H + nl + nl
              + "  Company     : RITCO                          " + nl
              + "  Email       : ritco@gmail.com                " + nl
              + "  Tel         : +250 078000000                 " + nl
              + "  Country     : RWANDA                         " + nl + nl
              + D + nl
              + "  TICKET DETAILS                               " + nl
              + D + nl
              + "  Ticket Code : " + t.code   + nl
              + "  Passenger   : " + t.name   + nl
              + "  Phone       : " + t.phone  + nl
              + "  Route       : " + t.route  + nl
              + "  Date        : " + t.date   + nl
              + "  Time        : " + t.time   + nl
              + "  Price       : " + t.price  + " RWF" + nl
              + "  Status      : " + t.status + nl
              + "  Cashier     : Mukamana Aline                 " + nl
              + D + nl + nl
              + "  Printed on  : "
              + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + nl + nl
              + "     THANK YOU FOR BOOKING WITH RITCO!         " + nl
              + "       Have a safe and pleasant journey!       " + nl
              + H + nl;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TICKET DATA CLASS
    // ══════════════════════════════════════════════════════════════════════════
    private static class Ticket {
        final String code, name, phone, price, status, route, date, time;

        Ticket(String code, String name, String phone, String price,
               String status, String route, String date, String time) {
            this.code   = code;
            this.name   = name;
            this.phone  = phone;
            this.price  = price;
            this.status = status;
            this.route  = route;
            this.date   = date;
            this.time   = time;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BusBookingSystem().setVisible(true));
    }
}