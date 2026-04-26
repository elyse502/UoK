package Assignments.chat;

/**
 *
 * @author Elysée NIYIBIZI
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

// =====================================================================
//  REAL-TIME CHAT SIMULATOR
//  Advanced Java Programming - UoK
//  Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert
//
//  Demonstrates:
//   - Multithreading (ScheduledExecutorService, SwingWorker)
//   - Observer / Event-listener pattern
//   - Custom component painting (paintComponent)
//   - Collections & generics
//   - Inner classes & anonymous classes
//   - Enum types
//   - Polymorphism (Message subtypes)
//   - CardLayout, BoxLayout, custom renderers
// =====================================================================

public class ChatSimulator {
    public static void main(String[] args) {
        // Apply FlatLaf-style hints for crisper rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}

// ========================= EMOJI FONT UTILITY =========================
// Detects the best available emoji-capable font on any OS so that
// emoji characters always render correctly on Windows, Linux, and macOS.

class EmojiFont {
    private static Font cached = null;

    /** Returns the best emoji font available, scaled to the requested size. */
    static Font get(float size) {
        if (cached == null) {
            String[] candidates = {
                "Segoe UI Emoji",    // Windows 8.1+
                "Noto Emoji",        // Linux (apt install fonts-noto-color-emoji)
                "Noto Color Emoji",  // alternative Linux name
                "Apple Color Emoji", // macOS
                "Symbola",           // cross-platform fallback
                "Dialog"             // universal JVM fallback
            };
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            Set<String> available = new HashSet<>(
                    Arrays.asList(ge.getAvailableFontFamilyNames()));
            String chosen = "Dialog";
            for (String c : candidates) {
                if (available.contains(c)) { chosen = c; break; }
            }
            cached = new Font(chosen, Font.PLAIN, 14);
        }
        return cached.deriveFont(size);
    }
}

// ========================= ENUMS =========================

enum MessageType { TEXT, IMAGE_PLACEHOLDER, SYSTEM, TYPING }

enum UserStatus { ONLINE, AWAY, OFFLINE }

// ========================= DATA MODELS =========================

class User {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final String name;
    private final Color avatarColor;
    private UserStatus status;
    private boolean isMe; // the logged-in user

    static final Color[] PALETTE = {
        new Color(255, 87, 34),  new Color(33, 150, 243),
        new Color(76, 175, 80),  new Color(156, 39, 176),
        new Color(255, 193, 7),  new Color(0, 188, 212),
        new Color(233, 30, 99),  new Color(63, 81, 181)
    };

    public User(String name, boolean isMe) {
        this.id   = counter.getAndIncrement();
        this.name = name;
        this.isMe = isMe;
        this.avatarColor = PALETTE[(id - 1) % PALETTE.length];
        this.status = UserStatus.ONLINE;
    }

    public int getId()            { return id; }
    public String getName()       { return name; }
    public Color getAvatarColor() { return avatarColor; }
    public UserStatus getStatus() { return status; }
    public boolean isMe()         { return isMe; }
    public void setStatus(UserStatus s) { this.status = s; }

    public String getInitials() {
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2)
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }
}

class Message {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final User sender;
    private final String text;
    private final MessageType type;
    private final String timestamp;
    private boolean read;

    public Message(User sender, String text, MessageType type) {
        this.id        = counter.getAndIncrement();
        this.sender    = sender;
        this.text      = text;
        this.type      = type;
        this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());
        this.read      = false;
    }

    public int getId()          { return id; }
    public User getSender()     { return sender; }
    public String getText()     { return text; }
    public MessageType getType(){ return type; }
    public String getTimestamp(){ return timestamp; }
    public boolean isRead()     { return read; }
    public void markRead()      { this.read = true; }
}

class ChatRoom {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final int id;
    private final String name;
    private final Color roomColor;
    private final List<Message> messages = new ArrayList<>();
    private final List<User> members     = new ArrayList<>();
    private int unreadCount = 0;

    public ChatRoom(String name, Color color) {
        this.id        = counter.getAndIncrement();
        this.name      = name;
        this.roomColor = color;
    }

    public int getId()               { return id; }
    public String getName()          { return name; }
    public Color getRoomColor()      { return roomColor; }
    public List<Message> getMessages(){ return messages; }
    public List<User> getMembers()   { return members; }
    public int getUnreadCount()      { return unreadCount; }

    public void addMessage(Message m) {
        messages.add(m);
        if (!m.getSender().isMe()) unreadCount++;
    }
    public void clearUnread() { unreadCount = 0; }

    public String getLastMessagePreview() {
        if (messages.isEmpty()) return "No messages yet";
        Message last = messages.get(messages.size() - 1);
        String preview = last.getText();
        return (preview.length() > 32) ? preview.substring(0, 32) + "…" : preview;
    }
    public String getLastMessageTime() {
        if (messages.isEmpty()) return "";
        return messages.get(messages.size() - 1).getTimestamp();
    }
}

// ========================= OBSERVER PATTERN =========================

interface ChatEventListener {
    void onNewMessage(ChatRoom room, Message message);
    void onTypingIndicator(ChatRoom room, User user, boolean isTyping);
    void onUserStatusChange(User user);
}

// ========================= SIMULATED BOT RESPONSES =========================

class BotEngine {
    private static final String[][] RESPONSES = {
        {"hello","hi","hey","morning","evening"},
        {"good morning!", "Hey there! How's it going?", "Hi! Great to see you online!", "Hello! What's up?"},

        {"how are you","how r u","hows it going","you good"},
        {"I'm doing great, thanks for asking! 😊", "All good here! Busy day?", "Feeling fantastic today! 🎉", "Can't complain! How about yourself?"},

        {"java","programming","code","coding"},
        {"Java is such a powerful language! 💻", "OOP in Java is really elegant once you get the hang of it!", "Have you tried multithreading yet? It's mind-blowing! 🤯", "I love how Java handles generics!"},

        {"uok","university","school","class","lecturer","josbert"},
        {"UoK has a great CS department! 🎓", "Advanced Java is one of the best modules! 📚", "Dr. Josbert really knows his stuff! 👨‍🏫", "University life is the best experience! 🏫"},

        {"thanks","thank you","thx","appreciate"},
        {"You're welcome! 😄", "Anytime! Happy to help!", "No problem at all! 🙌", "Always here for you! 💪"},

        {"bye","goodbye","cya","see you","later"},
        {"See you later! 👋", "Take care! Come back soon!", "Goodbye! Have a great day! ☀️", "Catch you later! 😊"},

        {"lol","haha","funny","😂","😄"},
        {"Haha! 😄", "Right?! 😂", "LOL! That's hilarious!", "😂😂 Too funny!"},
    };

    private static final String[] DEFAULT_RESPONSES = {
        "That's interesting! Tell me more 🤔",
        "I see what you mean! 👍",
        "Really? That's cool! 😎",
        "Haha! Good point 😄",
        "I totally agree with you! ✅",
        "Wow, didn't think of it that way! 🌟",
        "You're absolutely right! 💯",
        "That makes sense! 🧠",
    };

    private final Random rand = new Random();

    public String getResponse(String input) {
        String lower = input.toLowerCase();
        for (int i = 0; i < RESPONSES.length; i += 2) {
            for (String keyword : RESPONSES[i]) {
                if (lower.contains(keyword)) {
                    String[] replies = RESPONSES[i + 1];
                    return replies[rand.nextInt(replies.length)];
                }
            }
        }
        return DEFAULT_RESPONSES[rand.nextInt(DEFAULT_RESPONSES.length)];
    }

    public int getTypingDelay() { return 800 + rand.nextInt(1200); }
    public int getReplyDelay()  { return 300 + rand.nextInt(500); }
}

// ========================= SHARED APP STATE =========================

class AppState {
    static User currentUser;
    static final List<User> allUsers   = new ArrayList<>();
    static final List<ChatRoom> rooms  = new ArrayList<>();
    static final List<ChatEventListener> listeners = new ArrayList<>();
    static ChatRoom activeRoom;
    static final BotEngine botEngine   = new BotEngine();
    static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(3);

    static void addListener(ChatEventListener l)    { listeners.add(l); }
    static void removeListener(ChatEventListener l) { listeners.remove(l); }

    static void fireNewMessage(ChatRoom room, Message msg) {
        SwingUtilities.invokeLater(() -> {
            for (ChatEventListener l : listeners) l.onNewMessage(room, msg);
        });
    }

    static void fireTyping(ChatRoom room, User user, boolean isTyping) {
        SwingUtilities.invokeLater(() -> {
            for (ChatEventListener l : listeners) l.onTypingIndicator(room, user, isTyping);
        });
    }

    static void fireStatusChange(User user) {
        SwingUtilities.invokeLater(() -> {
            for (ChatEventListener l : listeners) l.onUserStatusChange(user);
        });
    }

    static void sendMessage(ChatRoom room, User sender, String text) {
        Message msg = new Message(sender, text, MessageType.TEXT);
        room.addMessage(msg);
        fireNewMessage(room, msg);

        // Bot reply only in non-general rooms and not from the current user
        if (!sender.isMe()) return;

        // Pick a bot from the room
        User bot = room.getMembers().stream()
                .filter(u -> !u.isMe())
                .findFirst().orElse(null);
        if (bot == null) return;

        // Show typing indicator, then reply
        scheduler.schedule(() -> {
            fireTyping(room, bot, true);
            scheduler.schedule(() -> {
                fireTyping(room, bot, false);
                String reply = botEngine.getResponse(text);
                Message botMsg = new Message(bot, reply, MessageType.TEXT);
                room.addMessage(botMsg);
                fireNewMessage(room, botMsg);
            }, botEngine.getTypingDelay(), TimeUnit.MILLISECONDS);
        }, botEngine.getReplyDelay(), TimeUnit.MILLISECONDS);
    }

    static void init(String username) {
        currentUser = new User(username, true);
        allUsers.add(currentUser);

        // Create bot users
        String[] botNames = {"Alice Uwimana", "Bob Nkusi", "Carol Ingabire",
                             "David Habimana", "Eve Mukamana"};
        for (String n : botNames) allUsers.add(new User(n, false));

        // Create chat rooms with vibrant colors
        ChatRoom general  = new ChatRoom("# General",     new Color(103, 58, 183));
        ChatRoom java     = new ChatRoom("# Java Dev",    new Color(0, 150, 136));
        ChatRoom random   = new ChatRoom("# Random",      new Color(233, 30, 99));
        ChatRoom uok      = new ChatRoom("# UoK Campus",  new Color(255, 152, 0));
        ChatRoom dm1      = new ChatRoom("@ Alice",       new Color(33, 150, 243));
        ChatRoom dm2      = new ChatRoom("@ Bob",         new Color(76, 175, 80));

        // Add members to rooms
        for (User u : allUsers) { general.getMembers().add(u); java.getMembers().add(u); }
        random.getMembers().add(currentUser); random.getMembers().add(allUsers.get(1));
        uok.getMembers().add(currentUser);    uok.getMembers().add(allUsers.get(2));
        dm1.getMembers().add(currentUser);    dm1.getMembers().add(allUsers.get(1));
        dm2.getMembers().add(currentUser);    dm2.getMembers().add(allUsers.get(2));

        rooms.add(general); rooms.add(java); rooms.add(random);
        rooms.add(uok);     rooms.add(dm1);  rooms.add(dm2);

        // Seed initial messages
        seedMessages(general, allUsers.get(1), allUsers.get(2),
            "Hey everyone! Welcome to the General channel 👋",
            "Glad to be here! Ready to chat 😄",
            "This chat app is amazing! Who built it? 🤩",
            "Someone in Advanced Java class at UoK! 💻");
        seedMessages(java, allUsers.get(2), allUsers.get(3),
            "Has anyone finished the Java assignment? 🤔",
            "Working on it! Multithreading is tricky",
            "I love the Observer pattern, so elegant! ✨",
            "SwingWorker saved my life for background tasks 😅");
        seedMessages(dm1, allUsers.get(1), null,
            "Hey! How's the project going? 👋",
            "Making great progress! Almost done 💪");

        activeRoom = general;

        // Schedule random bot activity to simulate real-time chat
        scheduleRandomActivity();
    }

    private static void seedMessages(ChatRoom room, User u1, User u2, String... texts) {
        for (int i = 0; i < texts.length; i++) {
            User sender = (i % 2 == 0) ? u1 : (u2 != null ? u2 : u1);
            Message m = new Message(sender, texts[i], MessageType.TEXT);
            room.addMessage(m);
        }
    }

    private static void scheduleRandomActivity() {
        Random rand = new Random();
        // Simulate bots sending messages occasionally
        scheduler.scheduleAtFixedRate(() -> {
            if (rooms.isEmpty()) return;
            ChatRoom room = rooms.get(rand.nextInt(rooms.size()));
            List<User> bots = new ArrayList<>();
            for (User u : room.getMembers()) if (!u.isMe()) bots.add(u);
            if (bots.isEmpty()) return;
            User bot = bots.get(rand.nextInt(bots.size()));

            String[] spontaneous = {
                "Anyone online? 👋", "Just finished studying! 📚",
                "Java is awesome! ☕", "Check out this cool feature! 😎",
                "Happy coding everyone! 💻", "Rwanda's tech scene is growing fast! 🇷🇼",
                "Coffee time ☕☕", "Almost weekend! 🎉"
            };
            String text = spontaneous[rand.nextInt(spontaneous.length)];
            Message msg = new Message(bot, text, MessageType.TEXT);
            room.addMessage(msg);
            fireNewMessage(room, msg);
        }, 8, 12, TimeUnit.SECONDS);

        // Simulate random status changes
        scheduler.scheduleAtFixedRate(() -> {
            List<User> bots = new ArrayList<>();
            for (User u : allUsers) if (!u.isMe()) bots.add(u);
            if (bots.isEmpty()) return;
            User bot = bots.get(rand.nextInt(bots.size()));
            UserStatus[] statuses = {UserStatus.ONLINE, UserStatus.AWAY, UserStatus.OFFLINE};
            bot.setStatus(statuses[rand.nextInt(statuses.length)]);
            fireStatusChange(bot);
        }, 5, 10, TimeUnit.SECONDS);
    }
}

// ========================= CUSTOM AVATAR COMPONENT =========================

class AvatarLabel extends JComponent {
    private final User user;
    private final int size;
    private boolean showStatus;

    public AvatarLabel(User user, int size, boolean showStatus) {
        this.user       = user;
        this.size       = size;
        this.showStatus = showStatus;
        setPreferredSize(new Dimension(size + (showStatus ? 4 : 0),
                                       size + (showStatus ? 4 : 0)));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Circle avatar
        g2.setColor(user.getAvatarColor());
        g2.fillOval(0, 0, size, size);

        // Initials
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, size / 3));
        FontMetrics fm = g2.getFontMetrics();
        String initials = user.getInitials();
        int tx = (size - fm.stringWidth(initials)) / 2;
        int ty = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initials, tx, ty);

        // Status dot
        if (showStatus) {
            Color dotColor = switch (user.getStatus()) {
                case ONLINE  -> new Color(76, 175, 80);
                case AWAY    -> new Color(255, 193, 7);
                case OFFLINE -> new Color(158, 158, 158);
            };
            int dotSize = size / 4;
            int dx = size - dotSize + 2;
            int dy = size - dotSize + 2;
            g2.setColor(Color.WHITE);
            g2.fillOval(dx - 1, dy - 1, dotSize + 2, dotSize + 2);
            g2.setColor(dotColor);
            g2.fillOval(dx, dy, dotSize, dotSize);
        }
        g2.dispose();
    }

    public void repaintStatus() { repaint(); }
}

// ========================= ANIMATED TYPING INDICATOR =========================

class TypingIndicator extends JComponent {
    private boolean visible2 = false;
    private float[] phases = {0f, 1f, 2f};
    private javax.swing.Timer animTimer;

    public TypingIndicator() {
        setPreferredSize(new Dimension(40, 20));
        setOpaque(false);
        animTimer = new javax.swing.Timer(120, e -> {
            for (int i = 0; i < phases.length; i++)
                phases[i] = (phases[i] + 0.3f) % 3f;
            repaint();
        });
    }

    public void start() { animTimer.start(); setVisible(true); }
    public void stop()  { animTimer.stop();  setVisible(false); }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible()) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int dotSize = 6;
        int spacing = 10;
        for (int i = 0; i < 3; i++) {
            float brightness = (float)(0.4 + 0.6 * Math.abs(Math.sin(phases[i] * Math.PI / 3)));
            g2.setColor(new Color(0.4f, 0.4f, 0.4f, brightness));
            int x = 4 + i * spacing;
            int y = (getHeight() - dotSize) / 2;
            g2.fillOval(x, y, dotSize, dotSize);
        }
        g2.dispose();
    }
}

// ========================= MESSAGE BUBBLE PANEL =========================

class MessageBubble extends JPanel {
    private static final Color SENT_COLOR     = new Color(103, 58, 183);
    private static final Color RECEIVED_COLOR = new Color(255, 255, 255);
    private static final Color SENT_TEXT      = Color.WHITE;
    private static final Color RECEIVED_TEXT  = new Color(30, 30, 30);

    private final Message message;
    private final boolean isMine;

    public MessageBubble(Message message) {
        this.message = message;
        this.isMine  = message.getSender().isMe();
        setOpaque(false);
        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(4, 12, 4, 12));
        build();
    }

    private void build() {
        // Avatar
        AvatarLabel avatar = new AvatarLabel(message.getSender(), 34, false);

        // Bubble content
        JPanel bubbleContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isMine ? SENT_COLOR : RECEIVED_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                // Drop shadow effect (subtle)
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(2, 2, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bubbleContent.setOpaque(false);
        bubbleContent.setLayout(new BorderLayout(0, 2));
        bubbleContent.setBorder(new EmptyBorder(8, 12, 8, 12));

        // Sender name (only shown for received messages)
        if (!isMine) {
            JLabel nameLabel = new JLabel(message.getSender().getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 11));
            nameLabel.setForeground(message.getSender().getAvatarColor());
            bubbleContent.add(nameLabel, BorderLayout.NORTH);
        }

        // Message text
        JTextArea textArea = new JTextArea(message.getText());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setFont(EmojiFont.get(13f));
        textArea.setForeground(isMine ? SENT_TEXT : RECEIVED_TEXT);
        textArea.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        bubbleContent.add(textArea, BorderLayout.CENTER);

        // Timestamp + read receipt
        JLabel timeLabel = new JLabel(message.getTimestamp() + (isMine ? " ✓✓" : ""));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(isMine ? new Color(200, 180, 255) : new Color(160, 160, 160));
        timeLabel.setHorizontalAlignment(isMine ? SwingConstants.RIGHT : SwingConstants.LEFT);
        bubbleContent.add(timeLabel, BorderLayout.SOUTH);

        // Layout: avatar left for received, avatar right for sent
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        // Max bubble width constraint
        JPanel bubbleWrapper = new JPanel(new BorderLayout());
        bubbleWrapper.setOpaque(false);
        bubbleWrapper.add(bubbleContent, BorderLayout.CENTER);
        bubbleWrapper.setMaximumSize(new Dimension(380, Integer.MAX_VALUE));

        if (isMine) {
            wrapper.add(Box.createHorizontalGlue());
            wrapper.add(bubbleWrapper);
            wrapper.add(Box.createHorizontalStrut(8));
            wrapper.add(avatar);
        } else {
            wrapper.add(avatar);
            wrapper.add(Box.createHorizontalStrut(8));
            wrapper.add(bubbleWrapper);
            wrapper.add(Box.createHorizontalGlue());
        }

        add(wrapper, BorderLayout.CENTER);
    }
}

// ========================= ROOM LIST CELL RENDERER =========================

class RoomListRenderer extends JPanel implements ListCellRenderer<ChatRoom> {
    private JLabel nameLabel    = new JLabel();
    private JLabel previewLabel = new JLabel();
    private JLabel timeLabel    = new JLabel();
    private JLabel badgeLabel   = new JLabel();
    private JPanel colorBar     = new JPanel();

    public RoomListRenderer() {
        setLayout(new BorderLayout(10, 0));
        setBorder(new EmptyBorder(10, 8, 10, 8));

        colorBar.setPreferredSize(new Dimension(4, 0));

        JPanel textPanel = new JPanel(new BorderLayout(0, 3));
        textPanel.setOpaque(false);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        previewLabel.setForeground(new Color(130, 130, 130));

        textPanel.add(nameLabel,    BorderLayout.NORTH);
        textPanel.add(previewLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 4));
        rightPanel.setOpaque(false);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(new Color(150, 150, 150));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        badgeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        badgeLabel.setForeground(Color.WHITE);
        badgeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        badgeLabel.setOpaque(true);
        badgeLabel.setPreferredSize(new Dimension(20, 16));

        rightPanel.add(timeLabel,  BorderLayout.NORTH);
        rightPanel.add(badgeLabel, BorderLayout.SOUTH);

        add(colorBar,   BorderLayout.WEST);
        add(textPanel,  BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ChatRoom> list,
            ChatRoom room, int index, boolean isSelected, boolean hasFocus) {
        nameLabel.setText(room.getName());
        previewLabel.setText(room.getLastMessagePreview());
        timeLabel.setText(room.getLastMessageTime());

        colorBar.setBackground(room.getRoomColor());
        nameLabel.setForeground(isSelected ? Color.WHITE : new Color(30, 30, 30));

        if (room.getUnreadCount() > 0) {
            badgeLabel.setText(String.valueOf(room.getUnreadCount()));
            badgeLabel.setBackground(room.getRoomColor());
            badgeLabel.setBorder(new EmptyBorder(2, 4, 2, 4));
        } else {
            badgeLabel.setText("");
            badgeLabel.setBackground(new Color(0,0,0,0));
        }

        setBackground(isSelected ? room.getRoomColor().darker() : Color.WHITE);
        textPanel_setForeground(isSelected);
        setOpaque(true);
        return this;
    }

    private void textPanel_setForeground(boolean selected) {
        if (selected) previewLabel.setForeground(new Color(220, 220, 255));
        else          previewLabel.setForeground(new Color(130, 130, 130));
    }
}

// ========================= MEMBERS PANEL =========================

class MembersPanel extends JPanel {
    private final JPanel listPanel;

    public MembersPanel() {
        setBackground(new Color(248, 248, 252));
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BorderLayout());
        setBorder(new MatteBorder(0, 1, 0, 0, new Color(220, 220, 230)));

        JLabel header = new JLabel("  MEMBERS");
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setForeground(new Color(100, 100, 120));
        header.setBorder(new EmptyBorder(14, 10, 10, 10));
        header.setBackground(new Color(240, 240, 248));
        header.setOpaque(true);
        add(header, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(248, 248, 252));

        JScrollPane sp = new JScrollPane(listPanel);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(10);
        add(sp, BorderLayout.CENTER);
    }

    public void updateMembers(List<User> members) {
        listPanel.removeAll();
        listPanel.add(Box.createVerticalStrut(6));
        for (User u : members) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setBackground(new Color(248, 248, 252));
            row.setBorder(new EmptyBorder(6, 10, 6, 10));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            AvatarLabel avatar = new AvatarLabel(u, 32, true);
            JLabel name = new JLabel(u.getName());
            name.setFont(new Font("Arial", Font.PLAIN, 12));
            name.setForeground(new Color(40, 40, 40));

            String statusText = switch (u.getStatus()) {
                case ONLINE  -> "Online";
                case AWAY    -> "Away";
                case OFFLINE -> "Offline";
            };
            JLabel status = new JLabel(statusText);
            status.setFont(new Font("Arial", Font.PLAIN, 10));
            status.setForeground(switch (u.getStatus()) {
                case ONLINE  -> new Color(76, 175, 80);
                case AWAY    -> new Color(255, 152, 0);
                case OFFLINE -> new Color(158, 158, 158);
            });

            JPanel textCol = new JPanel(new BorderLayout());
            textCol.setBackground(new Color(248, 248, 252));
            textCol.add(name,   BorderLayout.CENTER);
            textCol.add(status, BorderLayout.SOUTH);

            if (u.isMe()) {
                JLabel youTag = new JLabel("you");
                youTag.setFont(new Font("Arial", Font.ITALIC, 10));
                youTag.setForeground(new Color(180, 180, 200));
                row.add(youTag, BorderLayout.EAST);
            }

            row.add(avatar,  BorderLayout.WEST);
            row.add(textCol, BorderLayout.CENTER);
            listPanel.add(row);
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }
}

// ========================= CHAT AREA PANEL =========================

class ChatAreaPanel extends JPanel implements ChatEventListener {
    private final JPanel  messagesPanel;
    private final JScrollPane scrollPane;
    private final JLabel  roomTitle;
    private final JLabel  roomSubtitle;
    private final JPanel  typingPanel;
    private final JLabel  typingLabel;
    private final TypingIndicator typingDots;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JPanel  headerColorBar;
    private final MembersPanel membersPanel;
    private ChatRoom currentRoom;

    private static final Color SEND_COLOR = new Color(103, 58, 183);

    public ChatAreaPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // ---- HEADER ----
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(220, 220, 230)),
                new EmptyBorder(12, 16, 12, 16)));

        headerColorBar = new JPanel();
        headerColorBar.setPreferredSize(new Dimension(5, 0));

        JPanel titleCol = new JPanel(new BorderLayout(0, 2));
        titleCol.setOpaque(false);
        roomTitle = new JLabel("Select a room");
        roomTitle.setFont(new Font("Arial", Font.BOLD, 16));
        roomTitle.setForeground(new Color(30, 30, 30));
        roomSubtitle = new JLabel("");
        roomSubtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        roomSubtitle.setForeground(new Color(120, 120, 140));
        titleCol.add(roomTitle,    BorderLayout.CENTER);
        titleCol.add(roomSubtitle, BorderLayout.SOUTH);

        header.add(headerColorBar, BorderLayout.WEST);
        header.add(titleCol,       BorderLayout.CENTER);

        // ---- MESSAGES AREA ----
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(245, 245, 250));
        messagesPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // ---- TYPING INDICATOR ----
        typingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        typingPanel.setBackground(new Color(245, 245, 250));
        typingDots = new TypingIndicator();
        typingLabel = new JLabel("");
        typingLabel.setFont(EmojiFont.get(11f));
        typingLabel.setForeground(new Color(120, 120, 140));
        typingPanel.add(typingDots);
        typingPanel.add(typingLabel);
        typingPanel.setVisible(false);

        // ---- INPUT BAR ----
        JPanel inputBar = new JPanel(new BorderLayout(10, 0));
        inputBar.setBackground(Color.WHITE);
        inputBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(220, 220, 230)),
                new EmptyBorder(10, 14, 10, 14)));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 230), 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        inputField.setBackground(new Color(248, 248, 255));

        sendButton = new JButton("Send  ➤");
        sendButton.setBackground(SEND_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(9, 18, 9, 18));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Emoji quick-insert buttons
        JPanel emojiBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        emojiBar.setBackground(Color.WHITE);
        String[] quickEmoji = {"\uD83D\uDE00","\uD83D\uDC4D","\u2764",
                               "\uD83D\uDE02","\uD83D\uDC4F","\uD83D\uDD25"};
        for (String e : quickEmoji) {
            JButton eb = new JButton(e);
            eb.setFont(EmojiFont.get(16f));
            eb.setFocusPainted(false); eb.setBorderPainted(false);
            eb.setContentAreaFilled(false);
            eb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            eb.addActionListener(ev ->
                inputField.setText(inputField.getText() + e));
            emojiBar.add(eb);
        }

        JPanel inputWrapper = new JPanel(new BorderLayout(0, 4));
        inputWrapper.setBackground(Color.WHITE);
        inputWrapper.add(emojiBar,   BorderLayout.NORTH);
        inputWrapper.add(inputField, BorderLayout.CENTER);
        inputBar.add(inputWrapper, BorderLayout.CENTER);
        inputBar.add(sendButton,   BorderLayout.EAST);

        // ---- MEMBERS PANEL ----
        membersPanel = new MembersPanel();

        // ---- ASSEMBLE ----
        JPanel centerCol = new JPanel(new BorderLayout());
        centerCol.add(scrollPane,  BorderLayout.CENTER);
        centerCol.add(typingPanel, BorderLayout.SOUTH);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(centerCol,   BorderLayout.CENTER);
        mainArea.add(membersPanel,BorderLayout.EAST);

        add(header,  BorderLayout.NORTH);
        add(mainArea,BorderLayout.CENTER);
        add(inputBar, BorderLayout.SOUTH);

        // ---- EVENTS ----
        ActionListener sendAction = e -> sendMessage();
        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        AppState.addListener(this);
    }

    public void loadRoom(ChatRoom room) {
        this.currentRoom = room;
        room.clearUnread();

        roomTitle.setText(room.getName());
        roomSubtitle.setText(room.getMembers().size() + " members  •  " +
                room.getMessages().size() + " messages");
        headerColorBar.setBackground(room.getRoomColor());

        messagesPanel.removeAll();
        for (Message m : room.getMessages()) {
            if (m.getType() == MessageType.TEXT)
                messagesPanel.add(new MessageBubble(m));
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
        scrollToBottom();

        membersPanel.updateMembers(room.getMembers());
        inputField.requestFocusInWindow();
    }

    private void sendMessage() {
        if (currentRoom == null) return;
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        inputField.setText("");
        AppState.sendMessage(currentRoom, AppState.currentUser, text);
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    @Override
    public void onNewMessage(ChatRoom room, Message message) {
        if (currentRoom != null && room.getId() == currentRoom.getId()) {
            if (message.getType() == MessageType.TEXT) {
                messagesPanel.add(new MessageBubble(message));
                messagesPanel.revalidate();
                messagesPanel.repaint();
                scrollToBottom();
                roomSubtitle.setText(room.getMembers().size() + " members  •  " +
                        room.getMessages().size() + " messages");
            }
        }
    }

    @Override
    public void onTypingIndicator(ChatRoom room, User user, boolean isTyping) {
        if (currentRoom != null && room.getId() == currentRoom.getId()) {
            if (isTyping) {
                typingLabel.setText(user.getName() + " is typing...");
                typingDots.start();
                typingPanel.setVisible(true);
            } else {
                typingDots.stop();
                typingPanel.setVisible(false);
            }
        }
    }

    @Override
    public void onUserStatusChange(User user) {
        if (currentRoom != null) membersPanel.updateMembers(currentRoom.getMembers());
    }
}

// ========================= MAIN CHAT WINDOW =========================

class ChatWindow extends JFrame implements ChatEventListener {
    private final DefaultListModel<ChatRoom> roomListModel = new DefaultListModel<>();
    private final JList<ChatRoom> roomList;
    private final ChatAreaPanel chatArea;
    private final JLabel userNameLabel;

    public ChatWindow() {
        setTitle("ChatWave — Real-Time Chat Simulator");
        setSize(1100, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 560));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ---- LEFT SIDEBAR ----
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 220, 230)));

        // Sidebar header / branding
        JPanel brand = new JPanel(new BorderLayout(10, 0));
        brand.setBackground(new Color(103, 58, 183));
        brand.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel logo = new JLabel("ChatWave");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Real-Time Simulator");
        tagline.setFont(new Font("Arial", Font.ITALIC, 10));
        tagline.setForeground(new Color(210, 190, 255));

        JPanel logoCol = new JPanel(new BorderLayout());
        logoCol.setOpaque(false);
        logoCol.add(logo,    BorderLayout.CENTER);
        logoCol.add(tagline, BorderLayout.SOUTH);
        brand.add(logoCol, BorderLayout.CENTER);

        // Current user info
        JPanel mePanel = new JPanel(new BorderLayout(8, 0));
        mePanel.setBackground(new Color(240, 235, 255));
        mePanel.setBorder(new EmptyBorder(10, 14, 10, 14));

        AvatarLabel meAvatar = new AvatarLabel(AppState.currentUser, 36, true);
        JPanel meText = new JPanel(new BorderLayout());
        meText.setOpaque(false);
        userNameLabel = new JLabel(AppState.currentUser.getName());
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userNameLabel.setForeground(new Color(60, 40, 120));
        JLabel meStatus = new JLabel("● Online");
        meStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        meStatus.setForeground(new Color(76, 175, 80));
        meText.add(userNameLabel, BorderLayout.CENTER);
        meText.add(meStatus,      BorderLayout.SOUTH);
        mePanel.add(meAvatar, BorderLayout.WEST);
        mePanel.add(meText,   BorderLayout.CENTER);

        // Rooms section label
        JLabel roomsLabel = new JLabel("  CHANNELS & DMs");
        roomsLabel.setFont(new Font("Arial", Font.BOLD, 10));
        roomsLabel.setForeground(new Color(130, 110, 180));
        roomsLabel.setBorder(new EmptyBorder(10, 10, 6, 10));
        roomsLabel.setBackground(new Color(248, 246, 255));
        roomsLabel.setOpaque(true);

        JPanel sidebarTop = new JPanel(new BorderLayout());
        sidebarTop.add(brand,      BorderLayout.NORTH);
        sidebarTop.add(mePanel,    BorderLayout.CENTER);
        sidebarTop.add(roomsLabel, BorderLayout.SOUTH);

        // Room list
        for (ChatRoom r : AppState.rooms) roomListModel.addElement(r);
        roomList = new JList<>(roomListModel);
        roomList.setCellRenderer(new RoomListRenderer());
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setBackground(Color.WHITE);
        roomList.setFixedCellHeight(64);

        JScrollPane roomScroll = new JScrollPane(roomList);
        roomScroll.setBorder(null);
        roomScroll.getVerticalScrollBar().setUnitIncrement(10);

        sidebar.add(sidebarTop,  BorderLayout.NORTH);
        sidebar.add(roomScroll,  BorderLayout.CENTER);

        // ---- CHAT AREA ----
        chatArea = new ChatAreaPanel();

        // ---- ASSEMBLE ----
        add(sidebar,  BorderLayout.WEST);
        add(chatArea, BorderLayout.CENTER);

        // ---- ROOM SELECTION ----
        roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ChatRoom selected = roomList.getSelectedValue();
                if (selected != null) {
                    AppState.activeRoom = selected;
                    chatArea.loadRoom(selected);
                    roomList.repaint(); // refresh unread badges
                }
            }
        });

        // Select first room by default
        if (!AppState.rooms.isEmpty()) {
            roomList.setSelectedIndex(0);
        }

        AppState.addListener(this);
        setVisible(true);
    }

    @Override
    public void onNewMessage(ChatRoom room, Message message) {
        // Refresh room list to update previews & badges
        roomList.repaint();
    }

    @Override public void onTypingIndicator(ChatRoom r, User u, boolean t) {}

    @Override
    public void onUserStatusChange(User user) {
        roomList.repaint();
    }

    @Override
    public void dispose() {
        AppState.scheduler.shutdownNow();
        super.dispose();
    }
}

// ========================= LOGIN WINDOW =========================

class LoginWindow extends JFrame {
    private static final Color BG1    = new Color(103, 58, 183);
    private static final Color BG2    = new Color(33, 150, 243);

    public LoginWindow() {
        setTitle("ChatWave — Login");
        setSize(480, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gradient background panel
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, BG1, getWidth(), getHeight(), BG2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // Top branding
        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBorder(new EmptyBorder(50, 40, 30, 40));

        JLabel appName = new JLabel("ChatWave", SwingConstants.CENTER);
        appName.setFont(new Font("Arial", Font.BOLD, 42));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(CENTER_ALIGNMENT);

        JLabel appSub = new JLabel("Real-Time Chat Simulator", SwingConstants.CENTER);
        appSub.setFont(new Font("Arial", Font.PLAIN, 14));
        appSub.setForeground(new Color(200, 200, 255));
        appSub.setAlignmentX(CENTER_ALIGNMENT);

        JLabel module = new JLabel("Advanced Java Programming  •  UoK", SwingConstants.CENTER);
        module.setFont(new Font("Arial", Font.ITALIC, 11));
        module.setForeground(new Color(180, 180, 240));
        module.setAlignmentX(CENTER_ALIGNMENT);

        topSection.add(appName);
        topSection.add(Box.createVerticalStrut(6));
        topSection.add(appSub);
        topSection.add(Box.createVerticalStrut(6));
        topSection.add(module);

        // Form card
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 36, 30, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridwidth = 2;

        JLabel welcome = new JLabel("Enter your display name");
        welcome.setFont(new Font("Arial", Font.BOLD, 15));
        welcome.setForeground(new Color(60, 20, 120));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(welcome, gbc);

        gbc.gridy = 1;
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 140, 220), 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        nameField.setBackground(new Color(250, 248, 255));
        nameField.setText("Student User");
        card.add(nameField, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(16, 0, 6, 0);
        JButton loginBtn = new JButton("Join Chat  ➤");
        loginBtn.setBackground(BG1);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(new EmptyBorder(12, 24, 12, 24));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(loginBtn, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(4, 0, 0, 0);
        JLabel hint = new JLabel("or pick a name below", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(new Color(140, 120, 180));
        card.add(hint, gbc);

        // Quick name buttons
        gbc.gridy = 4; gbc.insets = new Insets(8, 0, 0, 0);
        JPanel quickNames = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        quickNames.setOpaque(false);
        String[] names = {"Mugisha", "Uwase", "Nkusi", "Ingabire"};
        for (String n : names) {
            JButton nb = new JButton(n);
            nb.setFont(new Font("Arial", Font.PLAIN, 11));
            nb.setBackground(new Color(230, 220, 255));
            nb.setForeground(new Color(80, 40, 160));
            nb.setFocusPainted(false);
            nb.setBorder(new EmptyBorder(5, 10, 5, 10));
            nb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            nb.addActionListener(e -> nameField.setText(n));
            quickNames.add(nb);
        }
        card.add(quickNames, gbc);

        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.setBorder(new EmptyBorder(0, 30, 40, 30));
        cardWrapper.add(card);

        root.add(topSection,  BorderLayout.NORTH);
        root.add(cardWrapper, BorderLayout.CENTER);
        add(root);

        ActionListener doLogin = e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a display name.");
                return;
            }
            dispose();
            AppState.init(name);
            new ChatWindow();
        };
        loginBtn.addActionListener(doLogin);
        nameField.addActionListener(doLogin);
        getRootPane().setDefaultButton(loginBtn);
        setVisible(true);
    }
}