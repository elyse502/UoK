package Shapes.games;

/**
 * Fruit Shooter Game
 * @author idtda
 */
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;

public class FruitShooterGame extends JPanel implements ActionListener, KeyListener {

    // ─── SCREEN ────────────────────────────────────────────────────────────────
    static final int W = 900, H = 650;

    // ─── COLOURS ───────────────────────────────────────────────────────────────
    static final Color C_RED    = new Color(220,  50,  50);
    static final Color C_GREEN  = new Color( 50, 220,  50);
    static final Color C_BLUE   = new Color( 50, 100, 220);
    static final Color C_YELLOW = new Color(255, 230,  30);
    static final Color C_ORANGE = new Color(255, 140,   0);
    static final Color C_PURPLE = new Color(150,  50, 200);
    static final Color C_CYAN   = new Color(  0, 220, 220);
    static final Color C_WHITE  = Color.WHITE;
    static final Color C_BLACK  = Color.BLACK;
    static final Color C_GRAY   = new Color(120, 120, 120);
    static final Color C_BROWN  = new Color(139,  69,  19);

    // ─── LEVEL CONFIG ──────────────────────────────────────────────────────────
    static final int[]    LVL_TARGET   = {0, 200,  600, 1200, 2000, 3000};
    static final int[]    LVL_SPAWN    = {0,1500, 1100,  800,  600,  400};
    static final int[][]  LVL_SPEED    = {{},{2,4},{4,7},{6,10},{8,13},{10,16}};
    static final int[]    LVL_FRUITS   = {0,   1,    2,    3,    4,    5};
    static final String[] LVL_NAME     = {"","Beginner's Garden","Challenging Orchard",
                                           "Ultimate Challenge","Storm Arena","Inferno Peak"};
    static final String[] LVL_DESC     = {"","Welcome! Learn the basics!",
                                           "Double spawn + faster fruits!",
                                           "Triple spawn + erratic movement!",
                                           "Quad spawn + homing fruits!",
                                           "5x spawn + turbo chaos!"};
    static final Color[]  LVL_BG       = {null,
        new Color(20,20,50), new Color(40,20,60),
        new Color(70,20,20), new Color(20,50,20),
        new Color(60,10,10)};

    // ─── STATE ─────────────────────────────────────────────────────────────────
    enum Screen { TITLE, PLAYING, TRANSITION, PAUSED, GAME_OVER }

    Screen screen = Screen.TITLE;
    int score, lives, level;
    int highScore = 0;
    boolean gameWon;
    long lastFruitSpawn, lastPowerupSpawn;
    long slowTimeEnd, scoreBoostEnd;
    boolean slowTimeActive;
    int scoreMultiplier = 1;

    Player player;
    List<Fruit>     fruits    = new ArrayList<>();
    List<Bullet>    bullets   = new ArrayList<>();
    List<Explosion> explosions= new ArrayList<>();
    List<Powerup>   powerups  = new ArrayList<>();
    List<FloatText> floatTexts= new ArrayList<>();

    Timer gameTimer;
    long transitionStart;

    // stars (static positions)
    int[] starX = new int[120], starY = new int[120], starSz = new int[120];

    // key state
    boolean keyLeft, keyRight, keySpace;
    boolean spaceWasUp = true;

    Random rng = new Random();

    // ─── SOUND ─────────────────────────────────────────────────────────────────
    Clip gunClip, shotgunClip, explosionClip, powerupClip, levelupClip;

    // ─── FONTS ─────────────────────────────────────────────────────────────────
    Font fSmall  = new Font("Arial", Font.BOLD, 16);
    Font fMedium = new Font("Arial", Font.BOLD, 24);
    Font fLarge  = new Font("Arial", Font.BOLD, 52);
    Font fHuge   = new Font("Arial", Font.BOLD, 72);

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🍎 Fruit Shooter – 5 Levels");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            FruitShooterGame game = new FruitShooterGame();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════════════
    public FruitShooterGame() {
        setPreferredSize(new Dimension(W, H));
        setBackground(C_BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Generate stars
        for (int i = 0; i < starX.length; i++) {
            starX[i] = rng.nextInt(W);
            starY[i] = rng.nextInt(H);
            starSz[i] = rng.nextInt(3) + 1;
        }

        loadSounds();
        loadHighScore();

        gameTimer = new Timer(16, this); // ~60 FPS
        gameTimer.start();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SOUND  (programmatically generated – no external files needed)
    // ══════════════════════════════════════════════════════════════════════════
    private void loadSounds() {
        gunClip      = makeSound(buildGunSound());
        shotgunClip  = makeSound(buildShotgunSound());
        explosionClip= makeSound(buildExplosionSound());
        powerupClip  = makeSound(buildPowerupSound());
        levelupClip  = makeSound(buildLevelupSound());
    }

    private Clip makeSound(byte[] data) {
        try {
            AudioFormat fmt = new AudioFormat(44100, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(Clip.class, fmt);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(fmt, data, 0, data.length);
            // Increase volume to maximum
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                vol.setValue(vol.getMaximum()); // full volume
            }
            return clip;
        } catch (Exception e) { return null; }
    }

    private void playSound(Clip clip) {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    // Gun: sharp crack (fast decay white noise + tone)
    private byte[] buildGunSound() {
        int sr = 44100; double dur = 0.18;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n * 2];
        for (int i = 0; i < n; i++) {
            double env = Math.exp(-i * 28.0 / n);
            double noise = (rng.nextDouble() * 2 - 1) * 0.9;
            double tone  = Math.sin(2 * Math.PI * 180 * i / sr) * 0.5;
            double samp  = (noise + tone) * env;
            short s = (short)(Math.max(-1, Math.min(1, samp)) * Short.MAX_VALUE);
            buf[i*2]   = (byte)(s & 0xFF);
            buf[i*2+1] = (byte)((s >> 8) & 0xFF);
        }
        return buf;
    }

    // Shotgun: big BOOM – longer, louder, lower
    private byte[] buildShotgunSound() {
        int sr = 44100; double dur = 0.5;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n * 2];
        for (int i = 0; i < n; i++) {
            double env = Math.exp(-i * 10.0 / n);
            double noise = (rng.nextDouble() * 2 - 1);
            double tone  = Math.sin(2 * Math.PI * 80 * i / sr) * 0.8
                         + Math.sin(2 * Math.PI * 45 * i / sr) * 0.6;
            double samp  = (noise * 0.6 + tone) * env * 1.2;
            short s = (short)(Math.max(-1, Math.min(1, samp)) * Short.MAX_VALUE);
            buf[i*2]   = (byte)(s & 0xFF);
            buf[i*2+1] = (byte)((s >> 8) & 0xFF);
        }
        return buf;
    }

    // Explosion: mid-frequency burst
    private byte[] buildExplosionSound() {
        int sr = 44100; double dur = 0.3;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n * 2];
        for (int i = 0; i < n; i++) {
            double env = Math.exp(-i * 14.0 / n);
            double noise = (rng.nextDouble() * 2 - 1);
            double tone  = Math.sin(2 * Math.PI * 120 * i / sr) * 0.4;
            double samp  = (noise * 0.8 + tone) * env;
            short s = (short)(Math.max(-1, Math.min(1, samp)) * Short.MAX_VALUE);
            buf[i*2]   = (byte)(s & 0xFF);
            buf[i*2+1] = (byte)((s >> 8) & 0xFF);
        }
        return buf;
    }

    // Powerup: ascending chime
    private byte[] buildPowerupSound() {
        int sr = 44100; double dur = 0.35;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n * 2];
        double[] freqs = {440, 550, 660, 880};
        for (int i = 0; i < n; i++) {
            int fi = (int)(i * freqs.length / n);
            double f = freqs[Math.min(fi, freqs.length - 1)];
            double env = Math.sin(Math.PI * i / n);
            double samp = Math.sin(2 * Math.PI * f * i / sr) * env * 0.9;
            short s = (short)(Math.max(-1, Math.min(1, samp)) * Short.MAX_VALUE);
            buf[i*2]   = (byte)(s & 0xFF);
            buf[i*2+1] = (byte)((s >> 8) & 0xFF);
        }
        return buf;
    }

    // Level-up: fanfare
    private byte[] buildLevelupSound() {
        int sr = 44100; double dur = 0.8;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n * 2];
        double[] freqs = {523, 659, 784, 1047};
        for (int i = 0; i < n; i++) {
            int fi = (int)(i * freqs.length / n);
            double f = freqs[Math.min(fi, freqs.length - 1)];
            double env = 0.5 + 0.5 * Math.sin(Math.PI * i / n);
            double samp = Math.sin(2 * Math.PI * f * i / sr) * env * 0.95;
            short s = (short)(Math.max(-1, Math.min(1, samp)) * Short.MAX_VALUE);
            buf[i*2]   = (byte)(s & 0xFF);
            buf[i*2+1] = (byte)((s >> 8) & 0xFF);
        }
        return buf;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HIGH SCORE
    // ══════════════════════════════════════════════════════════════════════════
    private void loadHighScore() {
        try (BufferedReader r = new BufferedReader(new FileReader("highscore.txt"))) {
            highScore = Integer.parseInt(r.readLine().trim());
        } catch (Exception ignored) {}
    }
    private void saveHighScore() {
        try (PrintWriter w = new PrintWriter("highscore.txt")) {
            w.println(highScore);
        } catch (Exception ignored) {}
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INIT GAME
    // ══════════════════════════════════════════════════════════════════════════
    private void startGame() {
        score = 0; lives = 3; level = 1;
        gameWon = false; slowTimeActive = false;
        scoreMultiplier = 1; slowTimeEnd = 0; scoreBoostEnd = 0;
        fruits.clear(); bullets.clear(); explosions.clear();
        powerups.clear(); floatTexts.clear();
        player = new Player();
        lastFruitSpawn = System.currentTimeMillis();
        lastPowerupSpawn = System.currentTimeMillis();
        screen = Screen.PLAYING;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ══════════════════════════════════════════════════════════════════════════
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        if (screen == Screen.TRANSITION) {
            if (System.currentTimeMillis() - transitionStart > 3000) {
                screen = Screen.PLAYING;
                lastFruitSpawn = System.currentTimeMillis();
            }
            return;
        }
        if (screen != Screen.PLAYING) return;

        long now = System.currentTimeMillis();

        // Player movement
        if (keyLeft)  player.x = Math.max(0, player.x - player.speed);
        if (keyRight) player.x = Math.min(W - player.width, player.x + player.speed);

        // Shoot
        if (keySpace && spaceWasUp) {
            spaceWasUp = false;
            shoot(now);
        }
        if (!keySpace) spaceWasUp = true;

        // Weapon switch
        player.updateWeapon(now);

        // Slow time
        if (slowTimeActive && now >= slowTimeEnd) slowTimeActive = false;

        // Spawn
        spawnFruit(now);
        spawnPowerup(now);

        // Update bullets
        bullets.removeIf(b -> { b.update(); return b.dead; });

        // Update fruits
        List<Fruit> toRemove = new ArrayList<>();
        for (Fruit f : fruits) {
            f.update(player.x + player.width / 2);
            if (f.y > H) {
                toRemove.add(f);
                lives--;
                if (lives <= 0) { endGame(); return; }
            }
        }
        fruits.removeAll(toRemove);

        // Update explosions
        explosions.removeIf(ex -> { ex.update(); return ex.dead; });

        // Update powerups
        List<Powerup> puRemove = new ArrayList<>();
        for (Powerup p : powerups) {
            p.y += p.speed;
            if (p.y > H) puRemove.add(p);
            else if (rectOverlap(p.x, p.y, 30, 30, player.x, player.y, player.width, player.height)) {
                applyPowerup(p, now);
                puRemove.add(p);
                playSound(powerupClip);
            }
        }
        powerups.removeAll(puRemove);

        // Bullet-fruit collision
        checkCollisions(now);

        // Float texts
        floatTexts.removeIf(ft -> { ft.update(); return ft.dead; });

        // Level progression
        if (score >= LVL_TARGET[level] && level < 5) {
            level++;
            lives++;
            playSound(levelupClip);
            transitionStart = System.currentTimeMillis();
            screen = Screen.TRANSITION;
            fruits.clear();
        } else if (score >= LVL_TARGET[5] && level == 5) {
            gameWon = true;
            endGame();
        }
    }

    private void shoot(long now) {
        if (now - player.lastShot < player.cooldown) return;
        player.lastShot = now;
        if (player.isShotgun) {
            playSound(shotgunClip);
            int[] angles = {-20, -10, 0, 10, 20};
            for (int a : angles) bullets.add(new Bullet(player.x + player.width/2, player.y, a, true));
        } else {
            playSound(gunClip);
            bullets.add(new Bullet(player.x + player.width/2, player.y, 0, false));
        }
    }

    private void checkCollisions(long now) {
        List<Bullet> bDead = new ArrayList<>();
        List<Fruit>  fDead = new ArrayList<>();
        for (Bullet b : bullets) {
            for (Fruit f : fruits) {
                if (fDead.contains(f)) continue;
                if (rectOverlap((int)b.x-(int)b.vx, (int)b.y-(int)b.vy, 8, 18, f.x, f.y, f.size, f.size)) {
                    bDead.add(b);
                    fDead.add(f);
                    int pts = f.points * scoreMultiplier;
                    score += pts;
                    explosions.add(new Explosion(f.x + f.size/2, f.y + f.size/2));
                    floatTexts.add(new FloatText("+" + pts, f.x + f.size/2, f.y, C_YELLOW));
                    playSound(explosionClip);
                    break;
                }
            }
        }
        bullets.removeAll(bDead);
        fruits.removeAll(fDead);
    }

    private boolean rectOverlap(int ax, int ay, int aw, int ah,
                                 int bx, int by, int bw, int bh) {
        return ax < bx+bw && ax+aw > bx && ay < by+bh && ay+ah > by;
    }

    private void applyPowerup(Powerup p, long now) {
        switch (p.type) {
            case 0: lives++; floatTexts.add(new FloatText("+1 LIFE!", p.x, p.y, C_RED)); break;
            case 1: scoreMultiplier = 2; scoreBoostEnd = now + 10000;
                    floatTexts.add(new FloatText("SCORE x2!", p.x, p.y, C_YELLOW)); break;
            case 2: slowTimeActive = true; slowTimeEnd = now + 8000;
                    floatTexts.add(new FloatText("SLOW TIME!", p.x, p.y, C_CYAN)); break;
        }
    }

    private void spawnFruit(long now) {
        int interval = LVL_SPAWN[level];
        if (slowTimeActive) interval *= 2;
        if (now - lastFruitSpawn < interval) return;
        lastFruitSpawn = now;
        int count = LVL_FRUITS[level];
        for (int i = 0; i < count; i++) fruits.add(new Fruit(level));
    }

    private void spawnPowerup(long now) {
        if (now - lastPowerupSpawn < 15000) return;
        lastPowerupSpawn = now;
        powerups.add(new Powerup());
    }

    private void endGame() {
        if (score > highScore) { highScore = score; saveHighScore(); }
        screen = Screen.GAME_OVER;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PAINT
    // ══════════════════════════════════════════════════════════════════════════
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        switch (screen) {
            case TITLE:      drawTitle(g);      break;
            case PLAYING:    drawGame(g);       break;
            case TRANSITION: drawGame(g); drawTransition(g); break;
            case PAUSED:     drawGame(g); drawPaused(g);     break;
            case GAME_OVER:  drawGameOver(g);   break;
        }
    }

    // ── background ────────────────────────────────────────────────────────────
    private void drawBackground(Graphics2D g) {
        Color bg = (screen == Screen.TITLE) ? new Color(10, 10, 30) : LVL_BG[level];
        g.setColor(bg);
        g.fillRect(0, 0, W, H);

        // Animated stars
        long t = System.currentTimeMillis();
        for (int i = 0; i < starX.length; i++) {
            int bright = 80 + (int)((Math.sin((t / 600.0) + i) + 1) * 87);
            g.setColor(new Color(bright, bright, bright));
            g.fillOval(starX[i], starY[i], starSz[i], starSz[i]);
        }

        // Ground
        g.setColor(new Color(30, 100, 30));
        g.fillRect(0, H - 22, W, 22);
        g.setColor(new Color(20, 80, 20));
        g.fillRect(0, H - 22, W, 4);
    }

    // ── TITLE SCREEN ─────────────────────────────────────────────────────────
    private void drawTitle(Graphics2D g) {
        drawBackground(g);

        // Title
        drawShadowText(g, "🍎 FRUIT SHOOTER", fHuge, C_YELLOW, W/2, 70);
        drawShadowText(g, "5 Levels of Chaos!", fMedium, C_ORANGE, W/2, 130);

        // Controls box
        drawPanel(g, 20, 155, W - 40, 70, new Color(0,0,0,160));
        drawCentredText(g, "← → or A/D = Move    SPACE = Shoot    P = Pause    Q = Quit", fSmall, C_WHITE, W/2, 183);
        drawCentredText(g, "Power-ups:  ❤ Red=+Life   ★ Yellow=Score×2   ◷ Blue=Slow Time", fSmall, C_CYAN, W/2, 208);

        // Level cards
        int cx = W/2, sy = 245, bw = 820, bh = 72;
        Color[] cols  = {C_GREEN, C_YELLOW, C_RED, C_ORANGE, C_PURPLE};
        String[] diff = {"EASY","HARD","VERY HARD","EXTREME","INSANE"};
        for (int i = 1; i <= 5; i++) {
            int y = sy + (i-1) * (bh + 6);
            Color c = cols[i-1];
            drawPanel(g, cx - bw/2, y, bw, bh, new Color(20, 20, 30, 200));
            g.setColor(c);
            g.setStroke(new BasicStroke(3));
            g.drawRoundRect(cx - bw/2, y, bw, bh, 14, 14);
            g.setStroke(new BasicStroke(1));

            // Circle badge
            g.setColor(c);
            g.fillOval(cx - bw/2 + 12, y + 12, 48, 48);
            g.setColor(C_BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            drawCentredText(g, String.valueOf(i), new Font("Arial",Font.BOLD,26), C_BLACK,
                            cx - bw/2 + 36, y + 41);

            // Name + desc
            g.setFont(fMedium);
            drawText(g, LVL_NAME[i], fMedium, C_WHITE, cx - bw/2 + 74, y + 26);
            drawText(g, LVL_DESC[i], fSmall,  c,       cx - bw/2 + 74, y + 52);

            // Difficulty badge
            int dw = 90;
            g.setColor(c);
            g.fillRoundRect(cx + bw/2 - dw - 8, y + 8, dw, 22, 8, 8);
            drawCentredText(g, diff[i-1], fSmall, C_BLACK, cx + bw/2 - dw/2 - 8 + dw/2, y + 22);

            // Target
            drawText(g, "Goal: " + LVL_TARGET[i] + " pts", fSmall, C_CYAN,
                     cx + bw/2 - 140, y + 48);
        }

        // High score
        if (highScore > 0)
            drawCentredText(g, "Best: " + highScore, fMedium, C_YELLOW, W/2, H - 60);

        // Blink start
        long t = System.currentTimeMillis();
        if ((t / 500) % 2 == 0)
            drawShadowText(g, "▶  Press SPACE to Start  ◀", fLarge, C_GREEN, W/2, H - 22);
    }

    // ── MAIN GAME ─────────────────────────────────────────────────────────────
    private void drawGame(Graphics2D g) {
        drawBackground(g);

        // Fruits
        for (Fruit f : fruits) f.draw(g);
        // Bullets
        for (Bullet b : bullets) b.draw(g);
        // Powerups
        for (Powerup p : powerups) p.draw(g);
        // Explosions
        for (Explosion ex : explosions) ex.draw(g);
        // Player
        player.draw(g);
        // Float texts
        for (FloatText ft : floatTexts) ft.draw(g);

        drawHUD(g);
    }

    private void drawHUD(Graphics2D g) {
        long now = System.currentTimeMillis();

        // Top bar background
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, W, 135);

        // Score / Lives / Level
        drawShadowText(g, "Score: " + score, fMedium, C_WHITE, W/2, 24);
        drawShadowText(g, "Lives: " + "❤ ".repeat(Math.max(0, lives)), fMedium, C_RED, W/2, 52);
        drawShadowText(g, "Level: " + level + "/5 – " + LVL_NAME[level], fMedium, C_GREEN, W/2, 80);

        // Progress bar
        int target = LVL_TARGET[level];
        float prog = Math.min((float) score / target, 1f);
        int bx = W/2 - 150, by = 95, bw = 300, bh = 22;
        g.setColor(C_GRAY);
        g.fillRoundRect(bx, by, bw, bh, 10, 10);
        g.setColor(C_GREEN);
        g.fillRoundRect(bx, by, (int)(bw * prog), bh, 10, 10);
        g.setColor(C_WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(bx, by, bw, bh, 10, 10);
        g.setStroke(new BasicStroke(1));
        drawCentredText(g, score + " / " + target, fSmall, C_WHITE, W/2, by + bh/2 + 5);

        // Status effects (top-right)
        int sy = 14;
        if (now < scoreBoostEnd) {
            long tl = (scoreBoostEnd - now) / 1000;
            drawText(g, "⚡ Score x2: " + tl + "s", fSmall, C_YELLOW, W - 200, sy);
            sy += 22;
        }
        if (slowTimeActive && now < slowTimeEnd) {
            long tl = (slowTimeEnd - now) / 1000;
            drawText(g, "◷ Slow: " + tl + "s", fSmall, C_CYAN, W - 200, sy);
            sy += 22;
        }

        // Weapon (top-left)
        String wep = player.isShotgun ? "⬤ SHOTGUN" : "• PISTOL";
        Color wc   = player.isShotgun ? C_ORANGE : C_GREEN;
        drawText(g, wep, fSmall, wc, 10, 14);
        long swapIn = Math.max(0, (player.lastWeaponSwitch + 10000 - now) / 1000);
        drawText(g, "Swap in: " + swapIn + "s", fSmall, C_WHITE, 10, 36);
    }

    // ── TRANSITION ────────────────────────────────────────────────────────────
    private void drawTransition(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, W, H);
        drawShadowText(g, "LEVEL " + level, fHuge, C_YELLOW, W/2, H/3);
        drawShadowText(g, LVL_NAME[level], fLarge, C_WHITE, W/2, H/2 - 30);
        drawShadowText(g, LVL_DESC[level], fMedium, C_GREEN, W/2, H/2 + 20);
        drawShadowText(g, "Target: " + LVL_TARGET[level] + " pts", fMedium, C_CYAN, W/2, H/2 + 60);
        drawShadowText(g, "Get Ready!", fLarge, C_RED, W/2, H*2/3);
        drawShadowText(g, "+1 BONUS LIFE!", fMedium, C_RED, W/2, H*2/3 + 50);
    }

    // ── PAUSED ────────────────────────────────────────────────────────────────
    private void drawPaused(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, W, H);
        drawShadowText(g, "PAUSED", fHuge, C_WHITE, W/2, H/2 - 30);
        drawShadowText(g, "Press P to continue", fMedium, C_WHITE, W/2, H/2 + 40);
    }

    // ── GAME OVER ─────────────────────────────────────────────────────────────
    private void drawGameOver(Graphics2D g) {
        g.setColor(C_BLACK);
        g.fillRect(0, 0, W, H);
        drawBackground(g);

        String title = gameWon ? "🏆 YOU WON! 🏆" : "GAME OVER";
        Color  tc    = gameWon ? C_GREEN : C_RED;
        drawShadowText(g, title, fHuge, tc, W/2, H/4);
        drawShadowText(g, "Final Level: " + level + "/5", fLarge, C_CYAN,  W/2, H/2 - 60);
        drawShadowText(g, "Final Score: " + score,        fLarge, C_WHITE, W/2, H/2 - 10);
        drawShadowText(g, "Best Score:  " + highScore,    fLarge, C_YELLOW,W/2, H/2 + 40);
        drawShadowText(g, "SPACE = Play Again    Q = Quit", fMedium, C_GREEN, W/2, H*3/4);
    }

    // ── draw helpers ──────────────────────────────────────────────────────────
    private void drawPanel(Graphics2D g, int x, int y, int w, int h, Color c) {
        g.setColor(c);
        g.fillRoundRect(x, y, w, h, 14, 14);
    }
    private void drawCentredText(Graphics2D g, String t, Font f, Color c, int cx, int cy) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(0,0,0,100));
        g.drawString(t, cx - fm.stringWidth(t)/2 + 2, cy + 2);
        g.setColor(c);
        g.drawString(t, cx - fm.stringWidth(t)/2, cy);
    }
    private void drawText(Graphics2D g, String t, Font f, Color c, int x, int y) {
        g.setFont(f); g.setColor(c); g.drawString(t, x, y);
    }
    private void drawShadowText(Graphics2D g, String t, Font f, Color c, int cx, int cy) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int tx = cx - fm.stringWidth(t)/2;
        g.setColor(new Color(0,0,0,160));
        g.drawString(t, tx+3, cy+3);
        g.setColor(c);
        g.drawString(t, tx, cy);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  KEY EVENTS
    // ══════════════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT  || k == KeyEvent.VK_A) keyLeft  = true;
        if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) keyRight = true;
        if (k == KeyEvent.VK_SPACE) keySpace = true;

        if (screen == Screen.TITLE && k == KeyEvent.VK_SPACE)  startGame();
        if (screen == Screen.GAME_OVER) {
            if (k == KeyEvent.VK_SPACE) { screen = Screen.TITLE; }
            if (k == KeyEvent.VK_Q)     System.exit(0);
        }
        if (screen == Screen.PLAYING && k == KeyEvent.VK_P) screen = Screen.PAUSED;
        if (screen == Screen.PAUSED  && k == KeyEvent.VK_P) screen = Screen.PLAYING;
        if (k == KeyEvent.VK_Q && screen != Screen.PLAYING) System.exit(0);
    }
    @Override public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT  || k == KeyEvent.VK_A) keyLeft  = false;
        if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) keyRight = false;
        if (k == KeyEvent.VK_SPACE) { keySpace = false; spaceWasUp = true; }
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════════
    //  INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════════

    // ── Player ────────────────────────────────────────────────────────────────
    class Player {
        int x, y, width = 80, height = 50, speed = 8;
        boolean isShotgun = false;
        long lastShot = 0, cooldown = 250;
        long lastWeaponSwitch = System.currentTimeMillis();

        Player() {
            x = W/2 - width/2;
            y = H - height - 22;
        }

        void updateWeapon(long now) {
            if (now - lastWeaponSwitch > 10000) {
                isShotgun = !isShotgun;
                lastWeaponSwitch = now;
            }
        }

        void draw(Graphics2D g) {
            // Body
            g.setColor(isShotgun ? C_BROWN : C_GRAY);
            g.fillRoundRect(x, y + 20, width, 20, 8, 8);

            // Barrel
            g.setColor(isShotgun ? new Color(100,60,20) : new Color(80,80,80));
            if (isShotgun) {
                g.fillRect(x + width - 25, y + 14, 28, 8);
                g.fillRect(x + width - 25, y + 24, 28, 8);
            } else {
                g.fillRect(x + width - 22, y + 16, 24, 8);
            }

            // Handle
            g.setColor(C_BROWN);
            g.fillRoundRect(x + 10, y + 35, 18, 24, 6, 6);

            // Muzzle flash
            if (System.currentTimeMillis() - lastShot < 80) {
                g.setColor(new Color(255, 230, 80, 200));
                g.fillOval(x + width + 2, y + 10, 18, 18);
                g.setColor(new Color(255, 150, 0, 160));
                g.fillOval(x + width + 6, y + 14, 10, 10);
            }

            // Label
            g.setColor(isShotgun ? C_ORANGE : C_GREEN);
            g.setFont(fSmall);
            String lbl = isShotgun ? "SHOTGUN" : "PISTOL";
            g.drawString(lbl, x + 2, y + 16);
        }
    }

    // ── Fruit ─────────────────────────────────────────────────────────────────
    class Fruit {
        int x, y, size;
        int type;        // 0=apple 1=banana 2=orange 3=watermelon 4=bomb
        int points;
        float vx, vy;
        double angle = 0, rotSpeed;
        int lvl;
        boolean homing;

        static final String[] NAMES = {"Apple","Banana","Orange","Watermelon","Bomb"};

        Fruit(int level) {
            lvl = level;
            int[] pool = level >= 4 ? new int[]{0,1,2,3,4} : new int[]{0,1,2,3};
            type = pool[rng.nextInt(pool.length)];
            size = (type == 3) ? 56 : (type == 1) ? 52 : 44;
            x = rng.nextInt(W - size);
            y = -size;
            int[] sp = LVL_SPEED[level];
            vy = sp[0] + rng.nextInt(sp[1] - sp[0] + 1);
            vx = (level >= 3) ? (rng.nextFloat() - 0.5f) * 4 : 0;
            rotSpeed = (rng.nextDouble() - 0.5) * 6;
            homing = (level >= 4);
            int[] pts = {10, 20, 15, 25, -50};
            points = pts[type];
        }

        void update(int playerCX) {
            if (homing) {
                float dx = playerCX - (x + size/2);
                vx += dx * 0.003f;
                vx = Math.max(-4, Math.min(4, vx));
            }
            x += (int) vx;
            y += (int) vy;
            angle += rotSpeed;
            if (x < 0)     { x = 0;        vx = Math.abs(vx); }
            if (x > W-size){ x = W-size;    vx = -Math.abs(vx);}
        }

        void draw(Graphics2D g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.rotate(Math.toRadians(angle), x + size/2.0, y + size/2.0);
            switch (type) {
                case 0: drawApple(g2); break;
                case 1: drawBanana(g2); break;
                case 2: drawOrange(g2); break;
                case 3: drawWatermelon(g2); break;
                case 4: drawBomb(g2); break;
            }
            g2.dispose();
        }

        void drawApple(Graphics2D g) {
            g.setColor(new Color(200, 30, 30));
            g.fillOval(x + 4, y + 8, size - 8, size - 8);
            g.setColor(new Color(230, 70, 70));
            g.fillOval(x + 8, y + 12, 12, 12);
            g.setColor(new Color(40, 130, 40));
            g.fillRect(x + size/2 - 3, y, 5, 10);
            g.setColor(new Color(60, 180, 60));
            int[] lx = {x+size/2+1, x+size/2+14, x+size/2+2};
            int[] ly = {y+5, y+2, y+12};
            g.fillPolygon(lx, ly, 3);
        }

        void drawBanana(Graphics2D g) {
            g.setColor(new Color(240, 220, 20));
            int[] px = {x+5, x+10, x+30, x+size-5, x+size-10, x+size-30};
            int[] py = {y+size-10, y+size-5, y+5, y+size-10, y+size-5, y+10};
            g.fillPolygon(px, py, 6);
            g.setColor(new Color(180, 140, 10));
            g.setStroke(new BasicStroke(2));
            g.drawArc(x+4, y+4, size-8, size-8, 220, 100);
            g.setStroke(new BasicStroke(1));
        }

        void drawOrange(Graphics2D g) {
            g.setColor(new Color(230, 130, 20));
            g.fillOval(x+3, y+3, size-6, size-6);
            g.setColor(new Color(255, 160, 50));
            g.fillOval(x+8, y+8, 14, 14);
            g.setColor(new Color(200, 100, 10));
            int cx2 = x + size/2, cy2 = y + size/2;
            for (int i = 0; i < 8; i++) {
                double a = i * Math.PI / 4;
                g.drawLine(cx2, cy2, cx2 + (int)(16*Math.cos(a)), cy2 + (int)(16*Math.sin(a)));
            }
            g.setColor(new Color(60,140,20));
            g.fillRect(cx2-2, y, 5, 8);
        }

        void drawWatermelon(Graphics2D g) {
            g.setColor(new Color(30, 160, 40));
            g.fillOval(x, y+size/2, size, size/2);
            g.fillArc(x, y, size, size, 0, 180);
            g.setColor(new Color(230, 50, 60));
            g.fillArc(x+5, y+4, size-10, size-8, 0, 180);
            g.fillOval(x+5, y+size/2, size-10, size/2-5);
            g.setColor(C_BLACK);
            for (int i = 0; i < 5; i++)
                g.fillOval(x+8+i*10, y+size/2+4+(i%2)*8, 5, 7);
            g.setColor(new Color(20,140,30));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x, y, size, size);
            g.setStroke(new BasicStroke(1));
        }

        void drawBomb(Graphics2D g) {
            g.setColor(new Color(30,30,30));
            g.fillOval(x+4, y+8, size-8, size-8);
            g.setColor(new Color(60,60,60));
            g.fillOval(x+8, y+12, 14, 14);
            g.setColor(C_BROWN);
            g.setStroke(new BasicStroke(3));
            g.drawLine(x+size/2, y+8, x+size/2+6, y);
            g.setStroke(new BasicStroke(1));
            // Fuse spark
            long t2 = System.currentTimeMillis();
            if ((t2/200)%2==0) {
                g.setColor(C_YELLOW);
                g.fillOval(x+size/2+4, y-3, 8, 8);
                g.setColor(C_ORANGE);
                g.fillOval(x+size/2+6, y-1, 5, 5);
            }
            g.setColor(C_RED);
            g.setFont(new Font("Arial",Font.BOLD,10));
            g.drawString("BOMB", x+4, y+size-2);
        }
    }

    // ── Bullet ────────────────────────────────────────────────────────────────
    class Bullet {
        float x, y, vx, vy;
        boolean dead, isShotgun;

        Bullet(int sx, int sy, int angleDeg, boolean shotgun) {
            x = sx; y = sy;
            isShotgun = shotgun;
            double rad = Math.toRadians(angleDeg);
            float spd = shotgun ? 13 : 17;
            vx = (float)(spd * Math.sin(rad));
            vy = (float)(-spd * Math.cos(rad));
        }

        void update() {
            x += vx; y += vy;
            if (y < -20 || x < -20 || x > W + 20) dead = true;
        }

        void draw(Graphics2D g) {
            if (isShotgun) {
                g.setColor(C_ORANGE);
                g.fillOval((int)x - 4, (int)y - 4, 9, 9);
                g.setColor(new Color(255,180,0,120));
                g.fillOval((int)x - 7, (int)y - 7, 15, 15);
            } else {
                g.setColor(C_YELLOW);
                g.fillRoundRect((int)x - 3, (int)y - 8, 6, 16, 4, 4);
                g.setColor(new Color(255, 200, 50));
                g.fillOval((int)x-2,(int)y-4, 4, 4);
            }
        }
    }

    // ── Explosion ─────────────────────────────────────────────────────────────
    class Explosion {
        int cx, cy, radius = 4;
        int alpha = 230;
        boolean dead;

        Explosion(int cx, int cy) { this.cx = cx; this.cy = cy; }

        void update() {
            radius += 3; alpha -= 14;
            if (alpha <= 0) dead = true;
        }

        void draw(Graphics2D g) {
            g.setColor(new Color(255, 200, 20, Math.max(0,alpha)));
            g.fillOval(cx-radius, cy-radius, radius*2, radius*2);
            g.setColor(new Color(255, 100, 20, Math.max(0,alpha/2)));
            g.fillOval(cx-radius+4, cy-radius+4, (radius-4)*2, (radius-4)*2);
        }
    }

    // ── Powerup ───────────────────────────────────────────────────────────────
    class Powerup {
        int x, y = -30, speed = 3;
        int type; // 0=life 1=score 2=slow
        static final Color[] COLS = {new Color(220,40,40), new Color(240,220,20), new Color(40,100,220)};
        static final String[] LABELS = {"❤","★","◷"};

        Powerup() {
            type = rng.nextInt(3);
            x = rng.nextInt(W - 30);
        }

        void draw(Graphics2D g) {
            // Glowing circle
            g.setColor(new Color(COLS[type].getRed(), COLS[type].getGreen(), COLS[type].getBlue(), 80));
            g.fillOval(x-6, y-6, 42, 42);
            g.setColor(COLS[type]);
            g.fillOval(x, y, 30, 30);
            g.setColor(C_WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x, y, 30, 30);
            g.setStroke(new BasicStroke(1));
            g.setFont(fMedium);
            FontMetrics fm = g.getFontMetrics();
            g.setColor(C_WHITE);
            g.drawString(LABELS[type], x + 15 - fm.stringWidth(LABELS[type])/2, y + 21);
        }
    }

    // ── FloatText ─────────────────────────────────────────────────────────────
    class FloatText {
        String text; int x; float y; Color color; float life = 1f; boolean dead;
        FloatText(String t, int x, int y, Color c) { text=t; this.x=x; this.y=y; color=c; }
        void update() { y -= 1.5f; life -= 0.025f; if (life<=0) dead=true; }
        void draw(Graphics2D g) {
            g.setFont(fMedium);
            g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),(int)(life*255)));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, x - fm.stringWidth(text)/2, (int)y);
        }
    }
}