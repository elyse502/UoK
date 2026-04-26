package Assignments.pos;

/**
 *
 * @author Elysée NIYIBIZI
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import java.io.*;
import java.util.concurrent.atomic.*;

// =====================================================================
//  SMARTMART — SUPERMARKET POS & INVENTORY SYSTEM
//  Advanced Java Programming - UoK
//  Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert
//
//  Advanced Java concepts demonstrated:
//   - OOP: Inheritance, Encapsulation, Polymorphism, Abstraction
//   - Generics & Collections (List, Map, HashMap)
//   - Java Streams & Lambda expressions
//   - Custom component rendering (paintComponent, gradients, charts)
//   - Inner classes, Anonymous classes, Functional interfaces
//   - Exception handling
//   - File I/O (receipt export)
//   - Enum types
//   - Observer / Event pattern
//   - Multithreading (SwingTimer for live clock)
//   - CardLayout, GridBagLayout, custom table renderers
// =====================================================================

public class SupermarketPOS {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}

// ======================================================================
//  THEME — all dark-mode colours in one place
// ======================================================================
class Theme {
    static final Color BG_DARK      = new Color(30,  30,  30);
    static final Color BG_PANEL     = new Color(37,  37,  38);
    static final Color BG_CARD      = new Color(45,  45,  48);
    static final Color BG_INPUT     = new Color(60,  60,  64);
    static final Color BG_ROW_ALT   = new Color(50,  50,  54);
    static final Color BORDER       = new Color(68,  68,  72);

    static final Color ACCENT       = new Color(0,   122, 204); // VS-Code blue
    static final Color ACCENT_HOVER = new Color(0,   150, 240);
    static final Color GREEN        = new Color(78,  201, 176);
    static final Color ORANGE       = new Color(255, 165,  0);
    static final Color RED          = new Color(220,  80,  80);
    static final Color YELLOW       = new Color(220, 200,  80);
    static final Color PURPLE       = new Color(180, 100, 240);

    static final Color TEXT_PRIMARY  = new Color(212, 212, 212);
    static final Color TEXT_SECONDARY= new Color(140, 140, 140);
    static final Color TEXT_HEADER   = new Color(255, 255, 255);

    static final Font FONT_TITLE  = new Font("Arial", Font.BOLD,  22);
    static final Font FONT_HEADER = new Font("Arial", Font.BOLD,  14);
    static final Font FONT_BODY   = new Font("Arial", Font.PLAIN, 12);
    static final Font FONT_MONO   = new Font("Monospaced", Font.PLAIN, 12);
    static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);
    static final Font FONT_BIG    = new Font("Arial", Font.BOLD,  28);

    /** Style a JButton as a filled accent button */
    static JButton accentButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT); b.setForeground(TEXT_HEADER);
        b.setFont(FONT_HEADER); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(9, 20, 9, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ b.setBackground(ACCENT_HOVER); }
            public void mouseExited(MouseEvent e) { b.setBackground(ACCENT); }
        });
        return b;
    }

    /** Style a JButton as a coloured action button */
    static JButton colorButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color); b.setForeground(TEXT_HEADER);
        b.setFont(FONT_BODY); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(7, 16, 7, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Style a JTextField for dark mode */
    static JTextField darkField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_INPUT); f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    /** Dark panel helper */
    static JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_PANEL);
        return p;
    }

    /** Card panel (slightly lighter) */
    static JPanel cardPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_CARD);
        return p;
    }
}

// ======================================================================
//  ENUMS
// ======================================================================
enum Category { GROCERIES, BEVERAGES, DAIRY, BAKERY, MEAT, PRODUCE,
                HOUSEHOLD, ELECTRONICS, PERSONAL_CARE }

enum PaymentMethod { CASH, CARD, MOBILE_MONEY }

// ======================================================================
//  DATA MODELS
// ======================================================================
class Product {
    private static final AtomicInteger counter = new AtomicInteger(1000);
    private final int id;
    private String name;
    private String barcode;
    private Category category;
    private double price;
    private int stock;
    private int lowStockThreshold;
    private int totalSold;

    public Product(String name, String barcode, Category cat,
                   double price, int stock, int threshold) {
        this.id   = counter.getAndIncrement();
        this.name = name; this.barcode = barcode;
        this.category = cat; this.price = price;
        this.stock = stock; this.lowStockThreshold = threshold;
        this.totalSold = 0;
    }

    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getBarcode()  { return barcode; }
    public Category getCategory(){ return category; }
    public double getPrice()    { return price; }
    public int getStock()       { return stock; }
    public int getLowThreshold(){ return lowStockThreshold; }
    public int getTotalSold()   { return totalSold; }
    public boolean isLowStock() { return stock <= lowStockThreshold; }

    public void setName(String n)    { this.name = n; }
    public void setPrice(double p)   { this.price = p; }
    public void setStock(int s)      { this.stock = s; }

    public void sell(int qty) {
        if (qty > stock) throw new IllegalArgumentException("Insufficient stock!");
        stock     -= qty;
        totalSold += qty;
    }
    public void restock(int qty) { stock += qty; }
}

class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product p, int qty) {
        this.product  = p;
        this.quantity = qty;
    }
    public Product getProduct()    { return product; }
    public int getQuantity()       { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }
    public double getSubtotal()    { return product.getPrice() * quantity; }
}

class Sale {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final List<CartItem> items;
    private final double subtotal;
    private final double tax;
    private final double total;
    private final double amountPaid;
    private final double change;
    private final PaymentMethod method;
    private final String cashier;
    private final String timestamp;

    public Sale(List<CartItem> items, double paid, PaymentMethod method,
                String cashier) {
        this.id        = counter.getAndIncrement();
        this.items     = new ArrayList<>(items);
        this.subtotal  = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.tax       = subtotal * 0.18;          // 18% VAT
        this.total     = subtotal + tax;
        this.amountPaid= paid;
        this.change    = paid - total;
        this.method    = method;
        this.cashier   = cashier;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public int getId()            { return id; }
    public List<CartItem> getItems(){ return items; }
    public double getSubtotal()   { return subtotal; }
    public double getTax()        { return tax; }
    public double getTotal()      { return total; }
    public double getAmountPaid() { return amountPaid; }
    public double getChange()     { return change; }
    public PaymentMethod getMethod(){ return method; }
    public String getCashier()    { return cashier; }
    public String getTimestamp()  { return timestamp; }
    public String getReceiptNo()  { return String.format("RCP-%04d", id); }
}

class Staff {
    private final String username, password, fullName, role;
    public Staff(String u, String p, String fn, String r){
        username=u; password=p; fullName=fn; role=r;
    }
    public boolean authenticate(String u, String p){
        return username.equals(u) && password.equals(p);
    }
    public String getFullName() { return fullName; }
    public String getRole()     { return role; }
    public String getUsername() { return username; }
}

// ======================================================================
//  APP STATE (shared singleton-style store)
// ======================================================================
class Store {
    static final String NAME    = "SmartMart Supermarket";
    static final String ADDRESS = "KG 7 Ave, Kigali, Rwanda";
    static final String TEL     = "+250 788 100 200";

    static List<Product>  inventory = new ArrayList<>();
    static List<Sale>     sales     = new ArrayList<>();
    static List<Staff>    staff     = new ArrayList<>();
    static Staff          currentStaff;
    static List<CartItem> cart      = new ArrayList<>();

    static {
        // ---- Staff ----
        staff.add(new Staff("admin",   "admin123",   "Admin User",       "Manager"));
        staff.add(new Staff("cashier1","cash123",    "Mugisha Bosco",    "Cashier"));
        staff.add(new Staff("cashier2","cash456",    "Uwase Claudine",   "Cashier"));

        // ---- Products ----
        Object[][] p = {
        // name,                barcode,     category,              price,  stock, lowThreshold
        {"Indomie Noodles",     "BAR-001", Category.GROCERIES,      350,    120,  20},
        {"Cooking Oil 1L",      "BAR-002", Category.GROCERIES,     2200,     45,  10},
        {"Sugar 1kg",           "BAR-003", Category.GROCERIES,      900,     60,  15},
        {"Salt 500g",           "BAR-004", Category.GROCERIES,      300,     80,  20},
        {"Rice 1kg",            "BAR-005", Category.GROCERIES,     1100,     90,  20},
        {"Coca-Cola 500ml",     "BAR-006", Category.BEVERAGES,      500,    200,  30},
        {"Fanta Orange 500ml",  "BAR-007", Category.BEVERAGES,      500,    180,  30},
        {"Primus Beer 500ml",   "BAR-008", Category.BEVERAGES,      800,    150,  25},
        {"Ikivuguto 500ml",     "BAR-009", Category.DAIRY,          600,     50,  10},
        {"Fresh Milk 1L",       "BAR-010", Category.DAIRY,         1000,     40,  10},
        {"Cheddar Cheese 200g", "BAR-011", Category.DAIRY,         2800,     15,   5},
        {"Bread Loaf",          "BAR-012", Category.BAKERY,         900,     30,   8},
        {"Croissant",           "BAR-013", Category.BAKERY,         600,     20,   5},
        {"Beef 500g",           "BAR-014", Category.MEAT,          3500,     25,   5},
        {"Chicken 1kg",         "BAR-015", Category.MEAT,          4200,     20,   5},
        {"Tomatoes 1kg",        "BAR-016", Category.PRODUCE,        500,     70,  15},
        {"Onions 1kg",          "BAR-017", Category.PRODUCE,        400,     80,  20},
        {"Bananas (bunch)",     "BAR-018", Category.PRODUCE,        800,     40,  10},
        {"Omo Detergent 500g",  "BAR-019", Category.HOUSEHOLD,     1200,     35,   8},
        {"Toilet Paper x6",     "BAR-020", Category.HOUSEHOLD,     2000,     60,  12},
        {"Colgate Toothpaste",  "BAR-021", Category.PERSONAL_CARE, 1500,     40,  10},
        {"Vaseline 250ml",      "BAR-022", Category.PERSONAL_CARE, 1800,     30,   8},
        {"Phone Charger",       "BAR-023", Category.ELECTRONICS,   5500,     12,   3},
        {"AA Batteries x4",     "BAR-024", Category.ELECTRONICS,   1200,     55,  15},
        };
        for (Object[] row : p) {
            inventory.add(new Product(
                (String)  row[0], (String) row[1], (Category) row[2],
                ((Number) row[3]).doubleValue(),
                ((Number) row[4]).intValue(),
                ((Number) row[5]).intValue()));
        }

        // ---- Seed some past sales ----
        seedSales();
    }

    private static void seedSales() {
        Random rnd = new Random(42);
        String[] cashiers = {"Mugisha Bosco","Uwase Claudine","Admin User"};
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < 18; i++) {
            List<CartItem> items = new ArrayList<>();
            int numItems = 1 + rnd.nextInt(5);
            for (int j = 0; j < numItems; j++) {
                Product pr = inventory.get(rnd.nextInt(inventory.size()));
                int qty = 1 + rnd.nextInt(3);
                if (pr.getStock() >= qty) {
                    pr.sell(qty);
                    items.add(new CartItem(pr, qty));
                }
            }
            if (!items.isEmpty()) {
                double sub = items.stream().mapToDouble(CartItem::getSubtotal).sum();
                double tot = sub * 1.18;
                sales.add(new Sale(items, Math.ceil(tot / 100) * 100,
                    methods[rnd.nextInt(methods.length)],
                    cashiers[rnd.nextInt(cashiers.length)]));
            }
        }
    }

    static Optional<Product> findByBarcode(String bc) {
        return inventory.stream().filter(p -> p.getBarcode().equalsIgnoreCase(bc.trim())).findFirst();
    }
    static Optional<Product> findByName(String q) {
        String lq = q.toLowerCase();
        return inventory.stream().filter(p -> p.getName().toLowerCase().contains(lq)).findFirst();
    }

    static void addToCart(Product p, int qty) {
        for (CartItem ci : cart) {
            if (ci.getProduct().getId() == p.getId()) {
                ci.setQuantity(ci.getQuantity() + qty); return;
            }
        }
        cart.add(new CartItem(p, qty));
    }

    static void clearCart() { cart.clear(); }

    static double cartSubtotal() {
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    static Sale checkout(double paid, PaymentMethod method) {
        for (CartItem ci : cart) ci.getProduct().sell(ci.getQuantity());
        Sale sale = new Sale(cart, paid, method, currentStaff.getFullName());
        sales.add(sale);
        clearCart();
        return sale;
    }

    static double totalRevenue() {
        return sales.stream().mapToDouble(Sale::getTotal).sum();
    }

    static long lowStockCount() {
        return inventory.stream().filter(Product::isLowStock).count();
    }
}

// ======================================================================
//  CUSTOM DARK TABLE
// ======================================================================
class DarkTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable t, Object v,
            boolean sel, boolean focus, int row, int col) {
        super.getTableCellRendererComponent(t, v, sel, focus, row, col);
        setForeground(Theme.TEXT_PRIMARY);
        setFont(Theme.FONT_BODY);
        setBackground(sel ? Theme.ACCENT
                          : (row % 2 == 0 ? Theme.BG_CARD : Theme.BG_ROW_ALT));
        setBorder(new EmptyBorder(4, 8, 4, 8));
        return this;
    }
}

class DarkTable extends JTable {
    public DarkTable(DefaultTableModel m) {
        super(m);
        setBackground(Theme.BG_CARD);
        setForeground(Theme.TEXT_PRIMARY);
        setFont(Theme.FONT_BODY);
        setRowHeight(28);
        setGridColor(Theme.BORDER);
        setSelectionBackground(Theme.ACCENT);
        setSelectionForeground(Color.WHITE);
        setShowGrid(true);
        setFillsViewportHeight(true);
        setDefaultRenderer(Object.class, new DarkTableRenderer());
        getTableHeader().setBackground(Theme.BG_PANEL);
        getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        getTableHeader().setFont(Theme.FONT_SMALL);
        getTableHeader().setBorder(new MatteBorder(0,0,1,0,Theme.BORDER));
        getTableHeader().setReorderingAllowed(false);
    }
    static JScrollPane scrolled(DarkTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBackground(Theme.BG_CARD);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(new LineBorder(Theme.BORDER,1));
        sp.getVerticalScrollBar().setUnitIncrement(12);
        return sp;
    }
}

// ======================================================================
//  STAT CARD WIDGET (dashboard)
// ======================================================================
class StatCard extends JPanel {
    private final JLabel valueLabel;

    public StatCard(String title, String value, Color accent) {
        setLayout(new BorderLayout(0, 8));
        setBackground(Theme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 1, true),
            new EmptyBorder(18, 20, 18, 20)));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(Theme.FONT_SMALL);
        titleLbl.setForeground(Theme.TEXT_SECONDARY);

        valueLabel = new JLabel(value);
        valueLabel.setFont(Theme.FONT_BIG);
        valueLabel.setForeground(accent);

        JPanel colorBar = new JPanel();
        colorBar.setBackground(accent);
        colorBar.setPreferredSize(new Dimension(4, 0));

        add(colorBar,  BorderLayout.WEST);
        add(titleLbl,  BorderLayout.NORTH);
        add(valueLabel,BorderLayout.CENTER);
    }

    public void setValue(String v) { valueLabel.setText(v); }
}

// ======================================================================
//  SIMPLE BAR CHART (Sales by Category)
// ======================================================================
class BarChart extends JPanel {
    private Map<String, Double> data = new LinkedHashMap<>();
    private static final Color[] COLORS = {
        Theme.ACCENT, Theme.GREEN, Theme.ORANGE,
        Theme.PURPLE, Theme.YELLOW, Theme.RED,
        new Color(100,200,255), new Color(255,150,200)
    };

    public BarChart() {
        setBackground(Theme.BG_CARD);
        setPreferredSize(new Dimension(400, 220));
    }

    public void setData(Map<String, Double> d) { this.data = d; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data.isEmpty()) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int padL=50, padR=20, padT=20, padB=40;
        int w = getWidth()-padL-padR;
        int h = getHeight()-padT-padB;

        double maxVal = data.values().stream().mapToDouble(d->d).max().orElse(1);

        int n    = data.size();
        int barW = Math.max(8, w / n - 8);
        int gap  = (w - barW * n) / (n + 1);

        List<String> keys = new ArrayList<>(data.keySet());
        for (int i = 0; i < n; i++) {
            double val = data.get(keys.get(i));
            int barH = (int)((val / maxVal) * h);
            int x    = padL + gap + i * (barW + gap);
            int y    = padT + (h - barH);

            Color c  = COLORS[i % COLORS.length];
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 180));
            g2.fillRoundRect(x, y, barW, barH, 4, 4);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, barW, barH, 4, 4);

            // Value label on top
            String valStr = String.format("%,.0f", val);
            g2.setFont(new Font("Arial", Font.BOLD, 9));
            g2.setColor(Theme.TEXT_PRIMARY);
            int tw = g2.getFontMetrics().stringWidth(valStr);
            g2.drawString(valStr, x + (barW - tw)/2, y - 3);

            // Category label bottom
            String key = keys.get(i);
            if (key.length() > 7) key = key.substring(0,7);
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            g2.setColor(Theme.TEXT_SECONDARY);
            int kw = g2.getFontMetrics().stringWidth(key);
            g2.drawString(key, x + (barW - kw)/2, padT + h + 14);
        }

        // Y-axis line
        g2.setColor(Theme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(padL, padT, padL, padT + h);
        g2.drawLine(padL, padT + h, padL + w, padT + h);

        // Y-axis labels
        g2.setFont(new Font("Arial", Font.PLAIN, 9));
        g2.setColor(Theme.TEXT_SECONDARY);
        for (int i = 0; i <= 4; i++) {
            int yy = padT + h - (int)(i * h / 4.0);
            double val = maxVal * i / 4;
            String lbl = String.format("%,.0f", val);
            int lw = g2.getFontMetrics().stringWidth(lbl);
            g2.drawString(lbl, padL - lw - 4, yy + 4);
            g2.setColor(new Color(68,68,72,80));
            g2.drawLine(padL, yy, padL + w, yy);
            g2.setColor(Theme.TEXT_SECONDARY);
        }
        g2.dispose();
    }
}

// ======================================================================
//  LOGIN SCREEN
// ======================================================================
class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("SmartMart POS — Login");
        setSize(480, 540); setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(20,20,30),
                                              getWidth(),getHeight(),new Color(30,40,60)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        root.setBorder(new EmptyBorder(40,50,40,50));

        // Branding
        JPanel top = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("SmartMart", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 44));
        logo.setForeground(Theme.ACCENT);
        logo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Point of Sale & Inventory System", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(Theme.TEXT_SECONDARY);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JLabel mod = new JLabel("Advanced Java Programming  •  UoK", SwingConstants.CENTER);
        mod.setFont(new Font("Arial", Font.ITALIC, 11));
        mod.setForeground(new Color(80,120,180));
        mod.setAlignmentX(CENTER_ALIGNMENT);

        top.add(logo); top.add(Box.createVerticalStrut(6));
        top.add(sub);  top.add(Box.createVerticalStrut(4)); top.add(mod);

        // Card
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(Theme.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(28,30,28,30));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(7,0,7,0); gc.gridwidth = 1;

        JLabel hdr = new JLabel("Staff Login");
        hdr.setFont(Theme.FONT_TITLE); hdr.setForeground(Theme.TEXT_HEADER);
        gc.gridx=0; gc.gridy=0; card.add(hdr, gc);

        JLabel lu = label("Username:"); gc.gridy=1; card.add(lu, gc);
        JTextField fUser = Theme.darkField(20); gc.gridy=2; card.add(fUser, gc);

        JLabel lp = label("Password:"); gc.gridy=3; card.add(lp, gc);
        JPasswordField fPass = new JPasswordField(20);
        fPass.setBackground(Theme.BG_INPUT); fPass.setForeground(Theme.TEXT_PRIMARY);
        fPass.setCaretColor(Theme.TEXT_PRIMARY); fPass.setFont(Theme.FONT_BODY);
        fPass.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(6,10,6,10)));
        gc.gridy=4; card.add(fPass, gc);

        gc.gridy=5; gc.insets=new Insets(18,0,6,0);
        JButton btnLogin = Theme.accentButton("LOGIN  >>");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(btnLogin, gc);

        gc.gridy=6; gc.insets=new Insets(4,0,0,0);
        JLabel hint = new JLabel("admin / admin123   or   cashier1 / cash123",
                SwingConstants.CENTER);
        hint.setFont(Theme.FONT_SMALL); hint.setForeground(Theme.TEXT_SECONDARY);
        card.add(hint, gc);

        JPanel cardWrap = new JPanel(new BorderLayout());
        cardWrap.setOpaque(false);
        cardWrap.setBorder(new EmptyBorder(28,0,0,0));
        cardWrap.add(card);

        root.add(top,      BorderLayout.NORTH);
        root.add(cardWrap, BorderLayout.CENTER);
        add(root);

        ActionListener doLogin = e -> {
            String u = fUser.getText().trim();
            String p = new String(fPass.getPassword()).trim();
            for (Staff s : Store.staff) {
                if (s.authenticate(u, p)) {
                    Store.currentStaff = s;
                    dispose();
                    new MainWindow();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this,
                "Invalid credentials. Try admin/admin123",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        };
        btnLogin.addActionListener(doLogin);
        fPass.addActionListener(doLogin);
        getRootPane().setDefaultButton(btnLogin);
        setVisible(true);
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t); l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_SECONDARY); return l;
    }
}

// ======================================================================
//  MAIN WINDOW  (tab-based)
// ======================================================================
class MainWindow extends JFrame {
    private final JTabbedPane tabs;
    private DashboardPanel  dashboard;
    private POSPanel        pos;
    private InventoryPanel  inventory;
    private SalesPanel      salesHistory;
    private final JLabel    clockLabel = new JLabel();
    private final JLabel    userLabel  = new JLabel();

    public MainWindow() {
        setTitle("SmartMart POS — " + Store.currentStaff.getFullName());
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 640));
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);

        // Styled tab pane
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(Theme.BG_PANEL);
        tabs.setForeground(Theme.TEXT_PRIMARY);
        tabs.setFont(Theme.FONT_HEADER);

        dashboard    = new DashboardPanel(this);
        pos          = new POSPanel(this);
        inventory    = new InventoryPanel(this);
        salesHistory = new SalesPanel();

        tabs.addTab("  Dashboard  ", dashboard);
        tabs.addTab("  Cashier POS  ", pos);
        tabs.addTab("  Inventory  ", inventory);
        tabs.addTab("  Sales History  ", salesHistory);

        // Refresh panels on tab switch
        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 0) dashboard.refresh();
            if (idx == 2) inventory.refresh();
            if (idx == 3) salesHistory.refresh();
        });

        add(tabs, BorderLayout.CENTER);

        // Live clock
        javax.swing.Timer clock = new javax.swing.Timer(1000, e -> {
            clockLabel.setText(
                new SimpleDateFormat("EEE dd MMM yyyy   HH:mm:ss").format(new Date()));
        });
        clock.start();
        clockLabel.setText(
            new SimpleDateFormat("EEE dd MMM yyyy   HH:mm:ss").format(new Date()));

        setVisible(true);
    }

    private JPanel buildTopBar() {
        JPanel bar = Theme.darkPanel(new BorderLayout(16,0));
        bar.setBackground(Theme.BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0,0,1,0,Theme.BORDER),
            new EmptyBorder(10,20,10,20)));

        JLabel storeName = new JLabel("SmartMart POS");
        storeName.setFont(new Font("Arial", Font.BOLD, 18));
        storeName.setForeground(Theme.ACCENT);

        clockLabel.setFont(Theme.FONT_SMALL);
        clockLabel.setForeground(Theme.TEXT_SECONDARY);

        userLabel.setText(Store.currentStaff.getFullName() +
                          "  [" + Store.currentStaff.getRole() + "]");
        userLabel.setFont(Theme.FONT_BODY);
        userLabel.setForeground(Theme.GREEN);

        JButton btnLogout = Theme.colorButton("Logout", Theme.RED);
        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Logout?","Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) { dispose(); new LoginScreen(); }
        });

        JPanel right = Theme.darkPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        right.add(clockLabel); right.add(userLabel); right.add(btnLogout);

        bar.add(storeName, BorderLayout.WEST);
        bar.add(right,     BorderLayout.EAST);
        return bar;
    }

    void refreshAll() {
        dashboard.refresh();
        inventory.refresh();
        salesHistory.refresh();
    }
}

// ======================================================================
//  DASHBOARD PANEL
// ======================================================================
class DashboardPanel extends JPanel {
    private final MainWindow parent;
    private StatCard cardRevenue, cardSales, cardProducts, cardLowStock;
    private final BarChart chart = new BarChart();
    private final DefaultTableModel topModel;
    private final DarkTable topTable;

    public DashboardPanel(MainWindow p) {
        this.parent = p;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(16,16));
        setBorder(new EmptyBorder(20,20,20,20));

        // ---- Stat Cards ----
        cardRevenue  = new StatCard("Total Revenue (FRW)", "0",     Theme.GREEN);
        cardSales    = new StatCard("Completed Sales",     "0",     Theme.ACCENT);
        cardProducts = new StatCard("Products in Stock",   "0",     Theme.YELLOW);
        cardLowStock = new StatCard("Low Stock Alerts",    "0",     Theme.RED);

        JPanel statsRow = new JPanel(new GridLayout(1,4,14,0));
        statsRow.setBackground(Theme.BG_DARK);
        statsRow.add(cardRevenue); statsRow.add(cardSales);
        statsRow.add(cardProducts); statsRow.add(cardLowStock);

        // ---- Chart ----
        JPanel chartCard = Theme.cardPanel(new BorderLayout(0,8));
        chartCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(14,14,14,14)));
        JLabel chartTitle = new JLabel("Revenue by Category (FRW)");
        chartTitle.setFont(Theme.FONT_HEADER); chartTitle.setForeground(Theme.TEXT_PRIMARY);
        chartCard.add(chartTitle, BorderLayout.NORTH);
        chartCard.add(chart,      BorderLayout.CENTER);

        // ---- Top Products Table ----
        topModel = new DefaultTableModel(
            new String[]{"Rank","Product","Category","Qty Sold","Revenue (FRW)"}, 0) {
            public boolean isCellEditable(int r,int c){ return false; }
        };
        topTable = new DarkTable(topModel);
        JPanel topCard = Theme.cardPanel(new BorderLayout(0,8));
        topCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(14,14,14,14)));
        JLabel topTitle = new JLabel("Top Selling Products");
        topTitle.setFont(Theme.FONT_HEADER); topTitle.setForeground(Theme.TEXT_PRIMARY);
        topCard.add(topTitle,                    BorderLayout.NORTH);
        topCard.add(DarkTable.scrolled(topTable),BorderLayout.CENTER);

        // ---- Bottom split ----
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chartCard, topCard);
        split.setDividerLocation(480);
        split.setBackground(Theme.BG_DARK);
        split.setBorder(null);
        split.setDividerSize(6);

        add(statsRow, BorderLayout.NORTH);
        add(split,    BorderLayout.CENTER);

        refresh();
    }

    void refresh() {
        DecimalFormat df = new DecimalFormat("#,###");
        cardRevenue.setValue("FRW " + df.format(Store.totalRevenue()));
        cardSales.setValue(String.valueOf(Store.sales.size()));
        cardProducts.setValue(String.valueOf(Store.inventory.size()));
        cardLowStock.setValue(String.valueOf(Store.lowStockCount()));

        // Revenue by category
        Map<String, Double> rev = new LinkedHashMap<>();
        for (Category cat : Category.values()) {
            double total = Store.sales.stream()
                .flatMap(s -> s.getItems().stream())
                .filter(ci -> ci.getProduct().getCategory() == cat)
                .mapToDouble(CartItem::getSubtotal).sum();
            if (total > 0) rev.put(cat.name().substring(0,
                    Math.min(cat.name().length(), 8)), total);
        }
        chart.setData(rev);

        // Top 8 products by qty sold
        topModel.setRowCount(0);
        Store.inventory.stream()
            .filter(pr -> pr.getTotalSold() > 0)
            .sorted((a,b) -> b.getTotalSold() - a.getTotalSold())
            .limit(8)
            .forEach(pr -> {
                int rank = topModel.getRowCount() + 1;
                topModel.addRow(new Object[]{
                    rank, pr.getName(), pr.getCategory().name(),
                    pr.getTotalSold(),
                    df.format(pr.getTotalSold() * pr.getPrice())
                });
            });
    }
}

// ======================================================================
//  POS / CASHIER PANEL
// ======================================================================
class POSPanel extends JPanel {
    private final MainWindow parent;

    // Cart table
    private final DefaultTableModel cartModel;
    private final DarkTable cartTable;

    // Totals
    private final JLabel lblSubtotal = valLabel("FRW 0");
    private final JLabel lblTax      = valLabel("FRW 0");
    private final JLabel lblTotal    = valLabel("FRW 0");

    // Input
    private final JTextField scanField;
    private final JTextField qtyField;
    private final JTextField paidField;
    private final JComboBox<PaymentMethod> methodBox;

    // Product search results
    private final DefaultTableModel searchModel;
    private final DarkTable searchTable;

    public POSPanel(MainWindow p) {
        this.parent = p;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(14,0));
        setBorder(new EmptyBorder(16,16,16,16));

        // ======================== LEFT: PRODUCT SEARCH ========================
        JPanel leftPanel = Theme.cardPanel(new BorderLayout(0,10));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(14,14,14,14)));
        leftPanel.setPreferredSize(new Dimension(360,0));

        JLabel searchTitle = new JLabel("Product Search / Scan");
        searchTitle.setFont(Theme.FONT_HEADER); searchTitle.setForeground(Theme.TEXT_PRIMARY);

        // Scan bar
        JPanel scanRow = Theme.cardPanel(new BorderLayout(8,0));
        JLabel scanLbl = new JLabel("Barcode / Name:");
        scanLbl.setFont(Theme.FONT_SMALL); scanLbl.setForeground(Theme.TEXT_SECONDARY);
        scanField = Theme.darkField(16);
        scanField.setToolTipText("Enter barcode (e.g. BAR-001) or product name");
        JButton btnScan = Theme.accentButton("ADD");

        JPanel qtyRow = Theme.cardPanel(new BorderLayout(8,0));
        JLabel qtyLbl = new JLabel("Quantity:");
        qtyLbl.setFont(Theme.FONT_SMALL); qtyLbl.setForeground(Theme.TEXT_SECONDARY);
        qtyField = Theme.darkField(5); qtyField.setText("1");

        JPanel inputGrid = Theme.cardPanel(new GridLayout(2,2,8,8));
        inputGrid.add(scanLbl); inputGrid.add(qtyLbl);
        inputGrid.add(scanField); inputGrid.add(qtyField);

        searchModel = new DefaultTableModel(
                new String[]{"Barcode","Name","Price","Stock"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        searchTable = new DarkTable(searchModel);
        loadSearchAll();

        // Double-click product to add
        searchTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount() == 2) addFromSearch();
            }
        });

        JPanel scanBtn = Theme.cardPanel(new BorderLayout(8,0));
        scanBtn.add(inputGrid, BorderLayout.CENTER);
        scanBtn.add(btnScan,   BorderLayout.EAST);
        btnScan.addActionListener(ev -> addToCart());
        scanField.addActionListener(ev -> addToCart());

        leftPanel.add(searchTitle,                   BorderLayout.NORTH);
        leftPanel.add(scanBtn,                       BorderLayout.CENTER);
        leftPanel.add(DarkTable.scrolled(searchTable),BorderLayout.SOUTH);
        leftPanel.add(buildSearchBar(),              BorderLayout.AFTER_LAST_LINE);

        // ======================== CENTRE: CART ========================
        JPanel centrePanel = Theme.cardPanel(new BorderLayout(0,10));
        centrePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(14,14,14,14)));

        JLabel cartTitle = new JLabel("Current Transaction");
        cartTitle.setFont(Theme.FONT_HEADER); cartTitle.setForeground(Theme.TEXT_PRIMARY);

        cartModel = new DefaultTableModel(
                new String[]{"#","Product","Price","Qty","Subtotal"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        cartTable = new DarkTable(cartModel);

        // Buttons row
        JButton btnRemove  = Theme.colorButton("Remove Item", Theme.RED);
        JButton btnClear   = Theme.colorButton("Clear Cart",  Theme.ORANGE);
        JButton btnQtyEdit = Theme.colorButton("Edit Qty",    Theme.YELLOW);
        btnRemove.addActionListener(e  -> removeFromCart());
        btnClear.addActionListener(e   -> clearCart());
        btnQtyEdit.addActionListener(e -> editQty());

        JPanel cartBtns = Theme.cardPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        cartBtns.add(btnRemove); cartBtns.add(btnQtyEdit); cartBtns.add(btnClear);

        centrePanel.add(cartTitle,                    BorderLayout.NORTH);
        centrePanel.add(DarkTable.scrolled(cartTable),BorderLayout.CENTER);
        centrePanel.add(cartBtns,                     BorderLayout.SOUTH);

        // ======================== RIGHT: PAYMENT ========================
        JPanel rightPanel = Theme.cardPanel(new BorderLayout(0,14));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER,1,true), new EmptyBorder(14,14,14,14)));
        rightPanel.setPreferredSize(new Dimension(260,0));

        JLabel payTitle = new JLabel("Payment");
        payTitle.setFont(Theme.FONT_HEADER); payTitle.setForeground(Theme.TEXT_PRIMARY);

        JPanel totals = Theme.cardPanel(new GridLayout(3,2,8,12));
        totals.add(totLbl("Subtotal:")); totals.add(lblSubtotal);
        totals.add(totLbl("VAT (18%):")); totals.add(lblTax);
        totals.add(totLbl("TOTAL:"));

        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setForeground(Theme.GREEN);
        totals.add(lblTotal);

        JPanel payMethod = Theme.cardPanel(new BorderLayout(8,0));
        JLabel pmLbl = new JLabel("Payment Method:");
        pmLbl.setFont(Theme.FONT_SMALL); pmLbl.setForeground(Theme.TEXT_SECONDARY);
        methodBox = new JComboBox<>(PaymentMethod.values());
        methodBox.setBackground(Theme.BG_INPUT);
        methodBox.setForeground(Theme.TEXT_PRIMARY);
        methodBox.setFont(Theme.FONT_BODY);
        payMethod.add(pmLbl,    BorderLayout.NORTH);
        payMethod.add(methodBox,BorderLayout.CENTER);

        JPanel amtPaid = Theme.cardPanel(new BorderLayout(8,4));
        JLabel apLbl = new JLabel("Amount Paid:");
        apLbl.setFont(Theme.FONT_SMALL); apLbl.setForeground(Theme.TEXT_SECONDARY);
        paidField = Theme.darkField(12);
        amtPaid.add(apLbl,    BorderLayout.NORTH);
        amtPaid.add(paidField,BorderLayout.CENTER);

        JButton btnCheckout = Theme.colorButton("  CHECKOUT  ", Theme.GREEN);
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 16));
        btnCheckout.setBorder(new EmptyBorder(14,10,14,10));
        btnCheckout.addActionListener(e -> checkout());

        JPanel formPad = Theme.cardPanel(new BorderLayout(0,12));
        formPad.add(payMethod, BorderLayout.NORTH);
        formPad.add(amtPaid,   BorderLayout.CENTER);

        rightPanel.add(payTitle,   BorderLayout.NORTH);
        rightPanel.add(totals,     BorderLayout.CENTER);
        rightPanel.add(formPad,    BorderLayout.SOUTH);

        JPanel checkoutWrap = Theme.cardPanel(new BorderLayout());
        checkoutWrap.setBorder(new EmptyBorder(14,0,0,0));
        checkoutWrap.add(btnCheckout, BorderLayout.CENTER);
        rightPanel.add(checkoutWrap,  BorderLayout.AFTER_LAST_LINE);

        add(leftPanel,  BorderLayout.WEST);
        add(centrePanel,BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel buildSearchBar() {
        JPanel p = Theme.cardPanel(new BorderLayout(8,0));
        p.setBorder(new EmptyBorder(8,0,0,0));
        JTextField liveSearch = Theme.darkField(16);
        liveSearch.setToolTipText("Live search product list...");
        JLabel lbl = new JLabel("Filter list:");
        lbl.setFont(Theme.FONT_SMALL); lbl.setForeground(Theme.TEXT_SECONDARY);
        liveSearch.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){ filterSearch(liveSearch.getText()); }
        });
        p.add(lbl,        BorderLayout.WEST);
        p.add(liveSearch, BorderLayout.CENTER);
        return p;
    }

    private void loadSearchAll() {
        searchModel.setRowCount(0);
        for (Product pr : Store.inventory) {
            searchModel.addRow(new Object[]{
                pr.getBarcode(), pr.getName(),
                String.format("FRW %,.0f", pr.getPrice()), pr.getStock()
            });
        }
    }

    private void filterSearch(String q) {
        searchModel.setRowCount(0);
        for (Product pr : Store.inventory) {
            if (pr.getName().toLowerCase().contains(q.toLowerCase()) ||
                pr.getBarcode().toLowerCase().contains(q.toLowerCase())) {
                searchModel.addRow(new Object[]{
                    pr.getBarcode(), pr.getName(),
                    String.format("FRW %,.0f", pr.getPrice()), pr.getStock()
                });
            }
        }
    }

    private void addFromSearch() {
        int row = searchTable.getSelectedRow();
        if (row < 0) return;
        String bc = (String) searchModel.getValueAt(row, 0);
        scanField.setText(bc);
        addToCart();
    }

    private void addToCart() {
        String query = scanField.getText().trim();
        if (query.isEmpty()) return;
        int qty;
        try { qty = Integer.parseInt(qtyField.getText().trim()); }
        catch (NumberFormatException e) { qty = 1; }
        if (qty <= 0) qty = 1;

        Optional<Product> found = Store.findByBarcode(query);
        if (found.isEmpty()) found = Store.findByName(query);

        if (found.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Product not found: " + query, "Not Found",
                JOptionPane.WARNING_MESSAGE); return;
        }
        Product pr = found.get();
        if (pr.getStock() < qty) {
            JOptionPane.showMessageDialog(this,
                "Only " + pr.getStock() + " units in stock!", "Low Stock",
                JOptionPane.WARNING_MESSAGE); return;
        }
        Store.addToCart(pr, qty);
        scanField.setText(""); qtyField.setText("1");
        refreshCart();
    }

    private void refreshCart() {
        cartModel.setRowCount(0);
        int i = 1;
        for (CartItem ci : Store.cart) {
            cartModel.addRow(new Object[]{
                i++, ci.getProduct().getName(),
                String.format("FRW %,.0f", ci.getProduct().getPrice()),
                ci.getQuantity(),
                String.format("FRW %,.0f", ci.getSubtotal())
            });
        }
        double sub  = Store.cartSubtotal();
        double tax  = sub * 0.18;
        double tot  = sub + tax;
        DecimalFormat df = new DecimalFormat("#,###.##");
        lblSubtotal.setText("FRW " + df.format(sub));
        lblTax.setText("FRW " + df.format(tax));
        lblTotal.setText("FRW " + df.format(tot));
        paidField.setText(String.format("%.0f", Math.ceil(tot / 100) * 100));
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select an item."); return; }
        Store.cart.remove(row); refreshCart();
    }

    private void clearCart() {
        if (Store.cart.isEmpty()) return;
        int c = JOptionPane.showConfirmDialog(this,"Clear all items?","Confirm",
                JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) { Store.clearCart(); refreshCart(); }
    }

    private void editQty() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select an item."); return; }
        CartItem ci = Store.cart.get(row);
        String input = JOptionPane.showInputDialog(this,
            "New quantity for " + ci.getProduct().getName() + ":",
            ci.getQuantity());
        if (input == null) return;
        try {
            int q = Integer.parseInt(input.trim());
            if (q <= 0) { Store.cart.remove(row); }
            else if (q > ci.getProduct().getStock()) {
                JOptionPane.showMessageDialog(this,
                    "Max stock: " + ci.getProduct().getStock());
            } else { ci.setQuantity(q); }
            refreshCart();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
        }
    }

    private void checkout() {
        if (Store.cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Cart is empty!","Error",
                    JOptionPane.WARNING_MESSAGE); return;
        }
        double total = Store.cartSubtotal() * 1.18;
        double paid;
        try { paid = Double.parseDouble(paidField.getText().trim().replace(",","")); }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"Enter valid payment amount.");return;
        }
        if (paid < total) {
            JOptionPane.showMessageDialog(this,
                String.format("Insufficient payment!\nTotal: FRW %,.2f\nPaid: FRW %,.2f",
                    total, paid)); return;
        }
        PaymentMethod method = (PaymentMethod) methodBox.getSelectedItem();
        Sale sale = Store.checkout(paid, method);
        showReceipt(sale);
        refreshCart();
        loadSearchAll();
        parent.refreshAll();
    }

    private void showReceipt(Sale sale) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         ").append(Store.NAME).append("\n");
        sb.append("    ").append(Store.ADDRESS).append("\n");
        sb.append("    Tel: ").append(Store.TEL).append("\n");
        sb.append("========================================\n");
        sb.append("Receipt No : ").append(sale.getReceiptNo()).append("\n");
        sb.append("Date/Time  : ").append(sale.getTimestamp()).append("\n");
        sb.append("Cashier    : ").append(sale.getCashier()).append("\n");
        sb.append("Payment    : ").append(sale.getMethod()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-20s %5s %10s%n","PRODUCT","QTY","SUBTOTAL"));
        sb.append("----------------------------------------\n");
        for (CartItem ci : sale.getItems()) {
            sb.append(String.format("%-20s %5d %10s%n",
                truncate(ci.getProduct().getName(),20), ci.getQuantity(),
                "FRW "+df.format(ci.getSubtotal())));
        }
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-26s %10s%n","Subtotal:",  "FRW "+df.format(sale.getSubtotal())));
        sb.append(String.format("%-26s %10s%n","VAT (18%):", "FRW "+df.format(sale.getTax())));
        sb.append(String.format("%-26s %10s%n","TOTAL:",     "FRW "+df.format(sale.getTotal())));
        sb.append(String.format("%-26s %10s%n","Amount Paid:","FRW "+df.format(sale.getAmountPaid())));
        sb.append(String.format("%-26s %10s%n","Change:",    "FRW "+df.format(sale.getChange())));
        sb.append("========================================\n");
        sb.append("    Thank you for shopping at SmartMart!\n");
        sb.append("========================================\n");

        String receiptText = sb.toString();

        JTextArea area = new JTextArea(receiptText);
        area.setFont(Theme.FONT_MONO);
        area.setBackground(Theme.BG_CARD);
        area.setForeground(Theme.TEXT_PRIMARY);
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(460, 400));
        sp.getViewport().setBackground(Theme.BG_CARD);

        int choice = JOptionPane.showOptionDialog(this, sp,
            "Receipt — " + sale.getReceiptNo(),
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            new String[]{"Save to File","Close"}, "Close");

        if (choice == 0) saveReceipt(sale.getReceiptNo(), receiptText);
    }

    private void saveReceipt(String name, String text) {
        try {
            String fname = name + ".txt";
            FileWriter fw = new FileWriter(fname);
            fw.write(text); fw.close();
            JOptionPane.showMessageDialog(this,"Receipt saved as: "+fname);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,"Could not save: "+ex.getMessage());
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0,max-1)+"~";
    }
    private static JLabel valLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(Theme.FONT_HEADER);
        l.setForeground(Theme.TEXT_PRIMARY); return l;
    }
    private static JLabel totLbl(String t) {
        JLabel l = new JLabel(t); l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_SECONDARY); return l;
    }
}

// ======================================================================
//  INVENTORY PANEL
// ======================================================================
class InventoryPanel extends JPanel {
    private final MainWindow parent;
    private final DefaultTableModel model;
    private final DarkTable table;
    private final JTextField searchField;

    public InventoryPanel(MainWindow p) {
        this.parent = p;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0,14));
        setBorder(new EmptyBorder(16,16,16,16));

        // Top bar
        JLabel title = new JLabel("Inventory Management");
        title.setFont(Theme.FONT_TITLE); title.setForeground(Theme.TEXT_PRIMARY);

        searchField = Theme.darkField(20);
        searchField.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){ refresh(); }
        });

        JButton btnRestock = Theme.accentButton("Restock");
        JButton btnAdd     = Theme.colorButton("Add Product",    Theme.GREEN);
        JButton btnEdit    = Theme.colorButton("Edit Price",     Theme.YELLOW);
        JButton btnDelete  = Theme.colorButton("Delete",         Theme.RED);
        JButton btnLow     = Theme.colorButton("Show Low Stock", Theme.ORANGE);

        btnRestock.addActionListener(e -> restock());
        btnAdd.addActionListener(e     -> addProduct());
        btnEdit.addActionListener(e    -> editPrice());
        btnDelete.addActionListener(e  -> deleteProduct());
        btnLow.addActionListener(e     -> showLowStock());

        JPanel topRow = Theme.darkPanel(new BorderLayout(12,0));
        JPanel topLeft = Theme.darkPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        topLeft.add(title);
        JPanel topRight = Theme.darkPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        topRight.add(new JLabel("Search: ") {{
            setFont(Theme.FONT_SMALL); setForeground(Theme.TEXT_SECONDARY); }});
        topRight.add(searchField);
        topRight.add(btnLow); topRight.add(btnAdd);
        topRight.add(btnEdit); topRight.add(btnRestock); topRight.add(btnDelete);
        topRow.add(topLeft,  BorderLayout.WEST);
        topRow.add(topRight, BorderLayout.EAST);

        // Table
        model = new DefaultTableModel(
                new String[]{"ID","Barcode","Product","Category","Price (FRW)",
                             "Stock","Low Threshold","Status"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new DarkTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String status = (String) model.getValueAt(row, 7);
                if (!isRowSelected(row)) {
                    c.setBackground("LOW STOCK".equals(status) ?
                        new Color(80,30,30) : Theme.BG_CARD);
                }
                return c;
            }
        };

        add(topRow,                     BorderLayout.NORTH);
        add(DarkTable.scrolled(table),  BorderLayout.CENTER);
        refresh();
    }

    void refresh() {
        String q = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###.##");
        for (Product pr : Store.inventory) {
            if (!q.isEmpty() &&
                !pr.getName().toLowerCase().contains(q) &&
                !pr.getBarcode().toLowerCase().contains(q) &&
                !pr.getCategory().name().toLowerCase().contains(q)) continue;
            model.addRow(new Object[]{
                pr.getId(), pr.getBarcode(), pr.getName(),
                pr.getCategory().name(), df.format(pr.getPrice()),
                pr.getStock(), pr.getLowThreshold(),
                pr.isLowStock() ? "LOW STOCK" : "OK"
            });
        }
    }

    private Product selectedProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select a product."); return null; }
        int id = (int) model.getValueAt(row, 0);
        return Store.inventory.stream().filter(p->p.getId()==id).findFirst().orElse(null);
    }

    private void restock() {
        Product pr = selectedProduct(); if (pr == null) return;
        String in = JOptionPane.showInputDialog(this,
            "Units to add for: " + pr.getName(), "10");
        if (in == null) return;
        try {
            int q = Integer.parseInt(in.trim());
            if (q <= 0) throw new NumberFormatException();
            pr.restock(q); refresh();
            JOptionPane.showMessageDialog(this,
                "Restocked " + pr.getName() + " by " + q + " units.\nNew stock: "+pr.getStock());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"Enter a valid positive number.");
        }
    }

    private void addProduct() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Add New Product", true);
        dlg.setSize(400, 420); dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_CARD);
        dlg.setLayout(new BorderLayout());

        JPanel form = Theme.cardPanel(new GridLayout(8,2,8,10));
        form.setBorder(new EmptyBorder(20,20,10,20));

        JTextField fName  = Theme.darkField(15);
        JTextField fBC    = Theme.darkField(10);
        JComboBox<Category> fCat = new JComboBox<>(Category.values());
        fCat.setBackground(Theme.BG_INPUT); fCat.setForeground(Theme.TEXT_PRIMARY);
        JTextField fPrice = Theme.darkField(10);
        JTextField fStock = Theme.darkField(10);
        JTextField fThres = Theme.darkField(10);

        formRow(form,"Name:",         fName);
        formRow(form,"Barcode:",       fBC);
        formRow(form,"Category:",      fCat);
        formRow(form,"Price (FRW):",   fPrice);
        formRow(form,"Stock Qty:",     fStock);
        formRow(form,"Low Threshold:", fThres);

        JButton save = Theme.accentButton("Save Product");
        save.addActionListener(e -> {
            try {
                String name  = fName.getText().trim();
                String bc    = fBC.getText().trim();
                Category cat = (Category) fCat.getSelectedItem();
                double price = Double.parseDouble(fPrice.getText().trim());
                int stock    = Integer.parseInt(fStock.getText().trim());
                int thr      = Integer.parseInt(fThres.getText().trim());
                if (name.isEmpty()||bc.isEmpty()) throw new Exception("Fill all fields.");
                Store.inventory.add(new Product(name,bc,cat,price,stock,thr));
                refresh(); dlg.dispose();
                JOptionPane.showMessageDialog(this,"Product added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg,"Price/Stock must be numbers.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, ex.getMessage());
            }
        });
        JPanel bp = Theme.cardPanel(new FlowLayout(FlowLayout.CENTER));
        bp.add(save);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(bp,   BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void editPrice() {
        Product pr = selectedProduct(); if (pr == null) return;
        String in = JOptionPane.showInputDialog(this,
            "New price for " + pr.getName() + " (FRW):",
            String.format("%.0f", pr.getPrice()));
        if (in == null) return;
        try {
            double p = Double.parseDouble(in.trim());
            if (p <= 0) throw new NumberFormatException();
            pr.setPrice(p); refresh();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,"Enter a valid positive price.");
        }
    }

    private void deleteProduct() {
        Product pr = selectedProduct(); if (pr == null) return;
        int c = JOptionPane.showConfirmDialog(this,
            "Delete '" + pr.getName() + "'?","Confirm",JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            Store.inventory.removeIf(p -> p.getId() == pr.getId()); refresh();
        }
    }

    private void showLowStock() {
        searchField.setText("");
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###.##");
        Store.inventory.stream().filter(Product::isLowStock).forEach(pr ->
            model.addRow(new Object[]{
                pr.getId(), pr.getBarcode(), pr.getName(),
                pr.getCategory().name(), df.format(pr.getPrice()),
                pr.getStock(), pr.getLowThreshold(), "LOW STOCK"
            }));
        JOptionPane.showMessageDialog(this,
            Store.lowStockCount() + " product(s) are low on stock (highlighted in red).",
            "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
    }

    private void formRow(JPanel p, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_SMALL); l.setForeground(Theme.TEXT_SECONDARY);
        p.add(l); p.add(field);
    }
}

// ======================================================================
//  SALES HISTORY PANEL
// ======================================================================
class SalesPanel extends JPanel {
    private final DefaultTableModel model;
    private final DarkTable table;

    public SalesPanel() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0,14));
        setBorder(new EmptyBorder(16,16,16,16));

        JLabel title = new JLabel("Sales History");
        title.setFont(Theme.FONT_TITLE); title.setForeground(Theme.TEXT_PRIMARY);

        JButton btnView = Theme.accentButton("View Receipt");
        JButton btnExport = Theme.colorButton("Export CSV", Theme.GREEN);
        btnView.addActionListener(e -> viewSale());
        btnExport.addActionListener(e -> exportCSV());

        JPanel topRow = Theme.darkPanel(new BorderLayout());
        JPanel buttons = Theme.darkPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        buttons.add(btnExport); buttons.add(btnView);
        topRow.add(title,  BorderLayout.WEST);
        topRow.add(buttons,BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{"Receipt","Date/Time","Cashier","Items",
                             "Subtotal","VAT","Total","Paid","Change","Method"},0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table = new DarkTable(model);

        // Summary bar
        JPanel summaryBar = Theme.cardPanel(new FlowLayout(FlowLayout.LEFT,20,8));
        summaryBar.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER));
        JLabel sumTitle = new JLabel("Session Summary:");
        sumTitle.setFont(Theme.FONT_SMALL); sumTitle.setForeground(Theme.TEXT_SECONDARY);
        summaryBar.add(sumTitle);

        add(topRow,                    BorderLayout.NORTH);
        add(DarkTable.scrolled(table), BorderLayout.CENTER);
        add(summaryBar,                BorderLayout.SOUTH);

        refresh();
    }

    void refresh() {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###.##");
        for (Sale s : Store.sales) {
            model.addRow(new Object[]{
                s.getReceiptNo(), s.getTimestamp(), s.getCashier(),
                s.getItems().size(),
                "FRW "+df.format(s.getSubtotal()),
                "FRW "+df.format(s.getTax()),
                "FRW "+df.format(s.getTotal()),
                "FRW "+df.format(s.getAmountPaid()),
                "FRW "+df.format(s.getChange()),
                s.getMethod()
            });
        }
    }

    private void viewSale() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select a sale."); return; }
        Sale sale = Store.sales.get(Store.sales.size()-1 - (Store.sales.size()-1-row));
        // Find sale by receipt number
        String rno = (String) model.getValueAt(row, 0);
        Sale found = Store.sales.stream()
            .filter(s -> s.getReceiptNo().equals(rno)).findFirst().orElse(null);
        if (found == null) return;

        DecimalFormat df = new DecimalFormat("#,###.##");
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt: ").append(found.getReceiptNo())
          .append("   Date: ").append(found.getTimestamp()).append("\n\n");
        for (CartItem ci : found.getItems()) {
            sb.append(String.format("  %-22s x%-3d  FRW %,10.0f%n",
                ci.getProduct().getName(), ci.getQuantity(), ci.getSubtotal()));
        }
        sb.append("\n  Subtotal : FRW ").append(df.format(found.getSubtotal()))
          .append("\n  VAT 18%  : FRW ").append(df.format(found.getTax()))
          .append("\n  TOTAL    : FRW ").append(df.format(found.getTotal()))
          .append("\n  Paid     : FRW ").append(df.format(found.getAmountPaid()))
          .append("\n  Change   : FRW ").append(df.format(found.getChange()));

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(Theme.FONT_MONO);
        area.setBackground(Theme.BG_CARD);
        area.setForeground(Theme.TEXT_PRIMARY);
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(440,300));
        sp.getViewport().setBackground(Theme.BG_CARD);
        JOptionPane.showMessageDialog(this, sp,
            "Sale Details — "+found.getReceiptNo(), JOptionPane.PLAIN_MESSAGE);
    }

    private void exportCSV() {
        try {
            String fname = "sales_export_"
                + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".csv";
            FileWriter fw = new FileWriter(fname);
            fw.write("Receipt,DateTime,Cashier,Items,Subtotal,VAT,Total,Paid,Change,Method\n");
            for (Sale s : Store.sales) {
                fw.write(String.format("%s,%s,%s,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%s%n",
                    s.getReceiptNo(), s.getTimestamp(), s.getCashier(),
                    s.getItems().size(), s.getSubtotal(), s.getTax(),
                    s.getTotal(), s.getAmountPaid(), s.getChange(), s.getMethod()));
            }
            fw.close();
            JOptionPane.showMessageDialog(this,"Sales exported to: "+fname);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,"Export failed: "+ex.getMessage());
        }
    }
}