package Assignments.game;

/**
 *
 * @author Elysee NIYIBIZI
 * @reg no. 2305000921
 * UNIVERSITY OF KIGALI
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import javax.sound.sampled.*;

// =====================================================================
//  GALACTIC DEFENDER — Space Shooter Game
//  Advanced Java Programming - UoK
//  Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert
//
//  Advanced Java concepts demonstrated:
//   - Game loop with javax.swing.Timer (fixed timestep)
//   - Custom rendering with Graphics2D (shapes, gradients, transforms)
//   - Inheritance & Polymorphism (all game objects extend GameObject)
//   - Interfaces (Renderable, Updatable, Collidable)
//   - Collections & Generics (List<Enemy>, List<Bullet>, etc.)
//   - Enum types (GameState, PowerUpType, EnemyType)
//   - Inner classes & Anonymous classes
//   - Multithreading (sound synthesis on a daemon thread)
//   - File I/O (high score persistence)
//   - Exception handling
//   - Observer / event pattern (GameEventListener)
//   - Double-buffered rendering
// =====================================================================

public class SpaceShooter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Galactic Defender — Advanced Java Programming | UoK");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            GamePanel game = new GamePanel();
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}

// ======================================================================
//  ENUMS
// ======================================================================
enum GameState { MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE, HIGH_SCORES }

enum PowerUpType {
    SHIELD("SHIELD", new Color(0, 200, 255)),
    RAPID_FIRE("RAPID FIRE", new Color(255, 200, 0)),
    TRIPLE_SHOT("TRIPLE SHOT", new Color(0, 255, 100)),
    BOMB("BOMB", new Color(255, 80, 80)),
    LIFE("EXTRA LIFE", new Color(255, 100, 200));

    final String label;
    final Color  color;
    PowerUpType(String l, Color c) { label=l; color=c; }
}

enum EnemyType {
    GRUNT(20, 1, 2.0f, 50),
    SHOOTER(40, 2, 1.4f, 100),
    TANK(100, 4, 0.9f, 200),
    BOSS(600, 8, 0.6f, 1000);

    final int hp, bulletDmg; final float speed; final int points;
    EnemyType(int hp,int bd,float sp,int pts){ this.hp=hp;bulletDmg=bd;speed=sp;points=pts; }
}

// ======================================================================
//  SOUND ENGINE  (pure Java — synthesizes audio on the fly)
// ======================================================================
class SoundEngine {
    private static final boolean ENABLED = true;
    private static final ExecutorService pool =
            Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(r); t.setDaemon(true); return t;
            });

    static void play(float freq, int durationMs, float volume, String shape) {
        if (!ENABLED) return;
        pool.submit(() -> {
            try {
                int sr = 44100;
                int samples = sr * durationMs / 1000;
                byte[] buf = new byte[samples * 2];
                for (int i = 0; i < samples; i++) {
                    double t = (double) i / sr;
                    double env = Math.max(0, 1.0 - (double)i / samples);
                    double wave;
                    switch (shape) {
                        case "square":  wave = Math.sin(2*Math.PI*freq*t) >= 0 ? 1:-1; break;
                        case "noise":   wave = Math.random()*2-1; break;
                        case "saw":     wave = 2*(freq*t - Math.floor(0.5+freq*t)); break;
                        default:        wave = Math.sin(2*Math.PI*freq*t); break;
                    }
                    short s = (short)(wave * env * volume * Short.MAX_VALUE);
                    buf[i*2]   = (byte)(s & 0xFF);
                    buf[i*2+1] = (byte)((s >> 8) & 0xFF);
                }
                AudioFormat fmt = new AudioFormat(sr, 16, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(fmt);
                line.open(fmt, buf.length);
                line.start();
                line.write(buf, 0, buf.length);
                line.drain(); line.close();
            } catch (Exception ignored) {}
        });
    }

    static void shoot()     { play(880, 80,  0.18f, "square"); }
    static void explosion() { play(120, 250, 0.30f, "noise");  }
    static void hit()       { play(440, 60,  0.15f, "saw");    }
    static void powerUp()   { play(660, 200, 0.20f, "sine");   }
    static void levelUp()   { play(523, 400, 0.22f, "sine");
                              play(659, 300, 0.22f, "sine");    }
    static void gameOver()  { play(220, 600, 0.25f, "saw");    }
    static void menuBeep()  { play(440, 100, 0.12f, "sine");   }
}

// ======================================================================
//  INTERFACES
// ======================================================================
interface Renderable { void render(Graphics2D g); }
interface Updatable  { void update(); }
interface Collidable { Rectangle2D getBounds(); }

// ======================================================================
//  BASE GAME OBJECT
// ======================================================================
abstract class GameObject implements Renderable, Updatable, Collidable {
    float x, y, vx, vy;
    int   w, h;
    boolean alive = true;

    GameObject(float x, float y, int w, int h) {
        this.x=x; this.y=y; this.w=w; this.h=h;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Float(x - w/2f, y - h/2f, w, h);
    }

    boolean collidesWith(GameObject other) {
        return alive && other.alive && getBounds().intersects(other.getBounds());
    }
}

// ======================================================================
//  STAR (parallax background)
// ======================================================================
class Star {
    float x, y, speed, brightness, size;
    Star(int W, int H) { reset(W, H, true); }
    void reset(int W, int H, boolean randomY) {
        x = (float)(Math.random() * W);
        y = randomY ? (float)(Math.random() * H) : -2;
        speed = (float)(0.3 + Math.random() * 2.0);
        brightness = (float)(0.3 + Math.random() * 0.7);
        size = speed > 1.5f ? 2 : 1;
    }
    void update(int W, int H) {
        y += speed;
        if (y > H + 2) reset(W, H, false);
    }
    void render(Graphics2D g) {
        int c = (int)(brightness * 255);
        g.setColor(new Color(c, c, c, (int)(brightness*200)));
        g.fillRect((int)x, (int)y, (int)size, (int)size);
    }
}

// ======================================================================
//  PARTICLE  (explosion / hit effect)
// ======================================================================
class Particle extends GameObject {
    private int   life, maxLife;
    private Color color;
    private float drag;

    Particle(float x, float y, Color c) {
        super(x, y, 3, 3);
        color   = c;
        maxLife = life = 20 + (int)(Math.random()*20);
        drag    = 0.92f;
        float angle  = (float)(Math.random() * Math.PI * 2);
        float spd    = (float)(1 + Math.random() * 4);
        vx = (float)(Math.cos(angle) * spd);
        vy = (float)(Math.sin(angle) * spd);
    }

    @Override public void update() {
        x += vx; y += vy;
        vx *= drag; vy *= drag;
        vy += 0.08f;
        if (--life <= 0) alive = false;
    }

    @Override public void render(Graphics2D g) {
        float alpha = (float) life / maxLife;
        g.setColor(new Color(color.getRed(), color.getGreen(),
                             color.getBlue(), (int)(alpha * 220)));
        g.fillOval((int)(x - 2), (int)(y - 2), 4, 4);
    }
}

// ======================================================================
//  BULLET
// ======================================================================
class Bullet extends GameObject {
    private boolean fromPlayer;
    private int     damage;
    private Color   color;

    Bullet(float x, float y, float vx, float vy, boolean fromPlayer, int dmg) {
        super(x, y, fromPlayer ? 4 : 6, fromPlayer ? 14 : 10);
        this.vx = vx; this.vy = vy;
        this.fromPlayer = fromPlayer;
        this.damage = dmg;
        this.color = fromPlayer ? new Color(0, 220, 255) : new Color(255, 80, 60);
    }

    boolean isFromPlayer() { return fromPlayer; }
    int getDamage()        { return damage; }

    @Override public void update() { x += vx; y += vy; }

    @Override public void render(Graphics2D g) {
        // Glow core
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
        g.fillOval((int)(x - w), (int)(y - h), w*2, h*2);
        // Bright core
        g.setColor(color);
        g.fillRoundRect((int)(x - w/2), (int)(y - h/2), w, h, w, w);
        // Tip highlight
        g.setColor(Color.WHITE);
        g.fillOval((int)(x - 1), (int)(y - h/2), 2, 4);
    }
}

// ======================================================================
//  POWER-UP
// ======================================================================
class PowerUp extends GameObject {
    private PowerUpType type;
    private float angle = 0;
    private int   pulse = 0;

    PowerUp(float x, float y, PowerUpType type) {
        super(x, y, 22, 22);
        this.type = type;
        this.vy = 1.2f;
    }

    PowerUpType getType() { return type; }

    @Override public void update() {
        y += vy;
        angle += 0.05f;
        pulse = (pulse + 3) % 360;
    }

    @Override public void render(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        g2.rotate(angle);

        float glow = (float)(0.6 + 0.4 * Math.sin(Math.toRadians(pulse)));
        Color c = type.color;
        Color glowC = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(glow*120));

        // Outer glow ring
        g2.setColor(glowC);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(-14, -14, 28, 28);

        // Diamond shape
        int[] px = {0, 11, 0, -11};
        int[] py = {-11, 0, 11, 0};
        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 180));
        g2.fillPolygon(px, py, 4);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(px, py, 4);

        // Label
        g2.setFont(new Font("Arial", Font.BOLD, 7));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String lbl = type.label.substring(0, Math.min(5, type.label.length()));
        g2.drawString(lbl, -fm.stringWidth(lbl)/2, 3);

        g2.dispose();
    }
}

// ======================================================================
//  ENEMY
// ======================================================================
class Enemy extends GameObject {
    private EnemyType type;
    private int   hp, maxHp;
    private float angle = 0;
    private int   shootTimer, shootInterval;
    private int   hitFlash = 0;
    private float waveOffset;

    Enemy(float x, float y, EnemyType type, float waveOffset) {
        super(x, y, type == EnemyType.BOSS ? 64 :
                    type == EnemyType.TANK ? 44 :
                    type == EnemyType.SHOOTER ? 34 : 28,
              type == EnemyType.BOSS ? 56 :
              type == EnemyType.TANK ? 40 :
              type == EnemyType.SHOOTER ? 30 : 26);
        this.type = type;
        this.hp = this.maxHp = type.hp;
        this.waveOffset = waveOffset;
        this.shootInterval = type == EnemyType.BOSS ? 40 :
                             type == EnemyType.SHOOTER ? 90 : 0;
        this.shootTimer = (int)(Math.random() * shootInterval);
        vy = type.speed;
    }

    EnemyType getType()  { return type; }
    int getHp()          { return hp; }
    int getMaxHp()       { return maxHp; }
    int getPoints()      { return type.points; }
    boolean canShoot()   { return type == EnemyType.SHOOTER || type == EnemyType.BOSS; }

    void hit(int damage) {
        hp -= damage;
        hitFlash = 8;
        if (hp <= 0) { hp = 0; alive = false; }
    }

    /** Returns a new enemy bullet if it fires this frame, else null */
    Bullet tryShoot() {
        if (!canShoot()) return null;
        if (++shootTimer >= shootInterval) {
            shootTimer = 0;
            if (type == EnemyType.BOSS) {
                // Boss fires spread shot — caller handles the rest
                return new Bullet(x, y + h/2f, 0, 4.5f, false, type.bulletDmg);
            }
            return new Bullet(x, y + h/2f, 0, 3.5f, false, type.bulletDmg);
        }
        return null;
    }

    /** For boss spread — call after tryShoot returns non-null */
    List<Bullet> bossSpread() {
        List<Bullet> bs = new ArrayList<>();
        float[] angles = {-0.3f, -0.15f, 0, 0.15f, 0.3f};
        for (float a : angles) {
            float spd = 4.0f;
            bs.add(new Bullet(x, y + h/2f,
                    (float)(Math.sin(a)*spd), (float)(Math.cos(a)*spd),
                    false, type.bulletDmg));
        }
        return bs;
    }

    @Override public void update() {
        angle += 0.04f;
        y += vy;
        // Sinusoidal horizontal drift for non-boss
        if (type != EnemyType.BOSS) {
            x += (float)(Math.sin(angle + waveOffset) * 0.8f);
        } else {
            // Boss bobs horizontally
            x += (float)(Math.sin(angle * 0.5f) * 1.4f);
        }
        if (hitFlash > 0) hitFlash--;
    }

    @Override public void render(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);

        Color base = hitFlash > 0 ? Color.WHITE :
                     type == EnemyType.BOSS    ? new Color(220, 50, 220) :
                     type == EnemyType.TANK    ? new Color(80, 200, 80) :
                     type == EnemyType.SHOOTER ? new Color(255, 140, 0) :
                                                 new Color(220, 60, 60);

        if (type == EnemyType.BOSS) drawBoss(g2, base);
        else                        drawShip(g2, base);

        // HP bar (for tougher enemies)
        if (type != EnemyType.GRUNT && hp < maxHp) {
            int bw = w;
            g2.setColor(new Color(0,0,0,160));
            g2.fillRect(-bw/2, -h/2 - 8, bw, 5);
            float ratio = (float)hp / maxHp;
            Color hpC = ratio > 0.5f ? new Color(0,220,0) :
                        ratio > 0.25f? new Color(255,200,0) : new Color(255,60,60);
            g2.setColor(hpC);
            g2.fillRect(-bw/2, -h/2 - 8, (int)(bw * ratio), 5);
        }
        g2.dispose();
    }

    private void drawShip(Graphics2D g2, Color base) {
        // Engine glow
        g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 60));
        g2.fillOval(-w/2 - 4, -h/4, w + 8, h/2 + 8);

        // Body
        int[] bx = {0, w/2, w/3, -w/3, -w/2};
        int[] by = {-h/2, h/3, h/2, h/2, h/3};
        g2.setColor(new Color(base.getRed()/2, base.getGreen()/2, base.getBlue()/2));
        g2.fillPolygon(bx, by, 5);
        g2.setColor(base);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(bx, by, 5);

        // Cockpit
        g2.setColor(new Color(255, 60, 60, 200));
        g2.fillOval(-5, -h/4, 10, 10);
    }

    private void drawBoss(Graphics2D g2, Color base) {
        // Pulsing outer ring
        float pulse = (float)(0.6 + 0.4 * Math.sin(angle * 3));
        g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(),
                              (int)(pulse * 100)));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(-w/2 - 10, -h/2 - 10, w + 20, h + 20);

        // Main body
        GradientPaint gp = new GradientPaint(-w/2, -h/2, base.darker(),
                                              w/2,  h/2, base);
        g2.setPaint(gp);
        g2.fillOval(-w/2, -h/2, w, h);
        g2.setColor(base.brighter());
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(-w/2, -h/2, w, h);

        // Wings
        int[][] lw = {{-w/2, -w/2-20, -w/2-10, -w/3},
                      { -h/4,    h/4,    h/2,    h/6}};
        int[][] rw = {{ w/2,  w/2+20,  w/2+10,  w/3},
                      { -h/4,    h/4,    h/2,    h/6}};
        g2.setColor(base.darker());
        g2.fillPolygon(lw[0], lw[1], 4);
        g2.fillPolygon(rw[0], rw[1], 4);
        g2.setColor(base);
        g2.drawPolygon(lw[0], lw[1], 4);
        g2.drawPolygon(rw[0], rw[1], 4);

        // Eye
        g2.setColor(new Color(255, 255, 0, 200));
        g2.fillOval(-8, -8, 16, 16);
        g2.setColor(Color.RED);
        g2.fillOval(-4, -4, 8, 8);

        // Cannon barrels
        g2.setColor(new Color(100, 100, 120));
        g2.fillRect(-6, h/2 - 4, 4, 14);
        g2.fillRect(2,  h/2 - 4, 4, 14);
    }
}

// ======================================================================
//  PLAYER
// ======================================================================
class Player extends GameObject {
    private int   hp, maxHp = 100;
    private int   lives;
    private boolean shielded;
    private int   shieldTimer;
    private boolean rapidFire;
    private int   rapidFireTimer;
    private boolean tripleShot;
    private int   tripleShotTimer;
    private int   invincibleTimer;    // brief invincibility after being hit
    private int   shootCooldown = 0;
    private int   shootRate     = 12; // frames between shots (lower = faster)
    private int   thrustAnim    = 0;

    Player(int W, int H) {
        super(W / 2f, H - 80, 36, 40);
        hp = maxHp;
        lives = 3;
    }

    int getHp()        { return hp; }
    int getMaxHp()     { return maxHp; }
    int getLives()     { return lives; }
    boolean isShielded(){ return shielded && shieldTimer > 0; }
    boolean isRapidFire(){ return rapidFire && rapidFireTimer > 0; }
    boolean isTriple() { return tripleShot && tripleShotTimer > 0; }
    boolean isInvincible(){ return invincibleTimer > 0; }

    void applyPowerUp(PowerUpType type) {
        switch (type) {
            case SHIELD:      shielded = true;  shieldTimer = 400; break;
            case RAPID_FIRE:  rapidFire = true; rapidFireTimer = 350;
                              shootRate = 4; break;
            case TRIPLE_SHOT: tripleShot = true; tripleShotTimer = 350; break;
            case BOMB:        /* handled externally */ break;
            case LIFE:        lives = Math.min(lives + 1, 5); break;
        }
        SoundEngine.powerUp();
    }

    void takeDamage(int dmg) {
        if (invincibleTimer > 0) return;
        if (isShielded()) { shieldTimer -= 80; if(shieldTimer<0)shieldTimer=0; return; }
        hp -= dmg;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
        invincibleTimer = 80;
    }

    boolean canShoot() { return shootCooldown <= 0; }
    void loseLife()    { lives--; }
    void restoreHp()   { hp = maxHp; }

    List<Bullet> shoot() {
        shootCooldown = isRapidFire() ? 4 : shootRate;
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(x, y - h/2f, 0, -11f, true, 10));
        if (isTriple()) {
            bullets.add(new Bullet(x, y - h/4f, -2.5f, -10.5f, true, 10));
            bullets.add(new Bullet(x, y - h/4f,  2.5f, -10.5f, true, 10));
        }
        return bullets;
    }

    @Override public void update() {
        if (shootCooldown > 0) shootCooldown--;
        if (shielded && --shieldTimer <= 0) { shielded = false; shootRate = 12; }
        if (rapidFire && --rapidFireTimer <= 0) { rapidFire = false; shootRate = 12; }
        if (tripleShot && --tripleShotTimer <= 0) tripleShot = false;
        if (invincibleTimer > 0) invincibleTimer--;
        thrustAnim = (thrustAnim + 1) % 10;
    }

    @Override public void render(Graphics2D g) {
        if (invincibleTimer > 0 && (invincibleTimer / 6) % 2 == 0) return; // blink

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);

        // Shield bubble
        if (isShielded()) {
            float alpha = Math.min(1f, shieldTimer / 60f);
            g2.setColor(new Color(0, 200, 255, (int)(alpha * 100)));
            g2.fillOval(-w/2 - 10, -h/2 - 10, w + 20, h + 20);
            g2.setColor(new Color(0, 200, 255, (int)(alpha * 180)));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(-w/2 - 10, -h/2 - 10, w + 20, h + 20);
        }

        // Engine thrust flame
        int flameH = 10 + thrustAnim;
        Color[] flames = {new Color(255,200,50), new Color(255,120,30), new Color(255,60,0)};
        for (int i = 0; i < 3; i++) {
            g2.setColor(flames[i]);
            g2.fillOval(-5 + i*2, h/2 - 4 + i*2, 10 - i*4, flameH - i*3);
        }

        // Main hull body
        int[] bx = {0, w/2, w/3,  0, -w/3, -w/2};
        int[] by = {-h/2, -h/6, h/4, h/2,  h/4, -h/6};
        GradientPaint hull = new GradientPaint(-w/2, 0, new Color(30,80,180),
                                                w/2, 0, new Color(80,160,255));
        g2.setPaint(hull);
        g2.fillPolygon(bx, by, 6);
        g2.setColor(new Color(120, 200, 255));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(bx, by, 6);

        // Cockpit window
        g2.setColor(new Color(150, 230, 255, 200));
        g2.fillOval(-7, -h/4, 14, 16);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1f));
        g2.drawOval(-7, -h/4, 14, 16);

        // Wing details
        g2.setColor(new Color(0, 160, 255, 180));
        g2.fillRect(-w/2, 0, 12, 8);
        g2.fillRect( w/2 - 12, 0, 12, 8);

        // Cannon tip glow
        if (isRapidFire()) {
            g2.setColor(new Color(255, 220, 0, 150));
            g2.fillOval(-4, -h/2 - 8, 8, 8);
        }
        g2.dispose();
    }
}

// ======================================================================
//  HIGH SCORE MANAGER
// ======================================================================
class HighScoreManager {
    private static final String FILE = "galactic_scores.txt";
    static final int MAX = 5;
    private List<int[]> scores = new ArrayList<>(); // {score, level}

    HighScoreManager() { load(); }

    void add(int score, int level) {
        scores.add(new int[]{score, level});
        scores.sort((a,b) -> b[0]-a[0]);
        if (scores.size() > MAX) scores = scores.subList(0, MAX);
        save();
    }

    boolean isHighScore(int score) {
        if (scores.size() < MAX) return true;
        return score > scores.get(scores.size()-1)[0];
    }

    List<int[]> getScores() { return scores; }

    private void load() {
        try (java.io.BufferedReader br =
                 new java.io.BufferedReader(new java.io.FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 2)
                    scores.add(new int[]{Integer.parseInt(p[0]),
                                         Integer.parseInt(p[1])});
            }
        } catch (Exception ignored) {}
    }

    private void save() {
        try (java.io.PrintWriter pw =
                 new java.io.PrintWriter(new java.io.FileWriter(FILE))) {
            for (int[] s : scores) pw.println(s[0]+","+s[1]);
        } catch (Exception ignored) {}
    }
}

// ======================================================================
//  GAME PANEL  (main game loop + rendering)
// ======================================================================
class GamePanel extends JPanel implements KeyListener {
    // Dimensions
    static final int W = 520, H = 720;

    // Game objects
    private Player          player;
    private List<Enemy>     enemies    = new ArrayList<>();
    private List<Bullet>    bullets    = new ArrayList<>();
    private List<PowerUp>   powerUps   = new ArrayList<>();
    private List<Particle>  particles  = new ArrayList<>();
    private List<Star>      stars      = new ArrayList<>();
    private List<String>    messages   = new ArrayList<>();  // on-screen popups
    private int[]           msgTimers  = new int[20];
    private List<Integer>   msgTimerList = new ArrayList<>();

    // State
    private GameState  state = GameState.MENU;
    private int        score, level, wave;
    private int        waveTimer, bossSpawnedAt;
    private boolean    bossAlive;
    private boolean[]  keys = new boolean[256];
    private HighScoreManager hsm = new HighScoreManager();

    // UI timers
    private int        screenShake = 0;
    private int        levelCompleteTimer = 0;
    private int        menuPulse = 0;

    // Popup messages: each is {text, x, y, life, r, g, b}
    private List<Object[]> popups = new ArrayList<>();

    private javax.swing.Timer gameTimer;

    GamePanel() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initStars();

        gameTimer = new javax.swing.Timer(16, e -> tick()); // ~60 fps
        gameTimer.start();
    }

    private void initStars() {
        for (int i = 0; i < 120; i++) stars.add(new Star(W, H));
    }

    // ----------------------------------------------------------------
    //  GAME LOOP
    // ----------------------------------------------------------------
    private void tick() {
        menuPulse = (menuPulse + 2) % 360;
        for (Star s : stars) s.update(W, H);

        switch (state) {
            case PLAYING:       tickPlaying(); break;
            case LEVEL_COMPLETE:tickLevelComplete(); break;
            default: break;
        }
        repaint();
    }

    private void tickPlaying() {
        // Input
        handleInput();

        // Update player
        player.update();

        // Enemies
        for (Enemy e : enemies) e.update();

        // Enemy bullets (shooting enemies)
        List<Bullet> newBullets = new ArrayList<>();
        for (Enemy e : enemies) {
            if (!e.alive) continue;
            Bullet b = e.tryShoot();
            if (b != null) {
                if (e.getType() == EnemyType.BOSS)
                    newBullets.addAll(e.bossSpread());
                else
                    newBullets.add(b);
            }
        }
        bullets.addAll(newBullets);

        // Bullets
        for (Bullet b : bullets) b.update();

        // Power-ups
        for (PowerUp p : powerUps) p.update();

        // Particles
        for (Particle p : particles) p.update();

        // Screen shake
        if (screenShake > 0) screenShake--;

        // Popup messages
        popups.removeIf(p -> (int)p[3] <= 0);
        for (Object[] p : popups) p[3] = (int)p[3] - 1;

        // --- Collision detection ---
        collisions();

        // --- Cleanup ---
        enemies.removeIf(e  -> !e.alive || e.y > H + 60);
        bullets.removeIf(b  -> !b.alive || b.y < -20 || b.y > H + 20 ||
                               b.x < -20 || b.x > W + 20);
        powerUps.removeIf(p -> !p.alive || p.y > H + 30);
        particles.removeIf(p -> !p.alive);

        // --- Enemy fell off screen penalty ---
        for (Enemy e : new ArrayList<>(enemies)) {
            if (!e.alive && e.y > H + 60) {
                // already removed above
            }
        }

        // --- Wave management ---
        if (enemies.isEmpty()) {
            if (!bossAlive) {
                waveTimer++;
                if (waveTimer > 90) spawnWave();
            }
        }

        // --- Game over ---
        if (!player.alive || player.getLives() <= 0) {
            if (!player.alive) {
                player.alive = false;
                spawnExplosion(player.x, player.y, 30, Color.CYAN);
                SoundEngine.explosion();
            }
            hsm.add(score, level);
            state = GameState.GAME_OVER;
            SoundEngine.gameOver();
        }
    }

    private void handleInput() {
        float spd = 4.5f;
        if (keys[KeyEvent.VK_LEFT]  || keys[KeyEvent.VK_A]) player.x -= spd;
        if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) player.x += spd;
        if (keys[KeyEvent.VK_UP]    || keys[KeyEvent.VK_W]) player.y -= spd * 0.7f;
        if (keys[KeyEvent.VK_DOWN]  || keys[KeyEvent.VK_S]) player.y += spd * 0.7f;

        // Clamp to screen
        player.x = Math.max(player.w/2f, Math.min(W - player.w/2f, player.x));
        player.y = Math.max(player.h/2f, Math.min(H - player.h/2f, player.y));

        // Shoot
        if ((keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_Z]) && player.canShoot()) {
            bullets.addAll(player.shoot());
            SoundEngine.shoot();
        }
    }

    private void collisions() {
        // Player bullets vs enemies
        for (Bullet b : bullets) {
            if (!b.alive || !b.isFromPlayer()) continue;
            for (Enemy e : enemies) {
                if (!e.alive) continue;
                if (b.collidesWith(e)) {
                    e.hit(b.getDamage());
                    b.alive = false;
                    SoundEngine.hit();
                    spawnExplosion(b.x, b.y, 4,
                        e.getType() == EnemyType.BOSS ? Color.MAGENTA : Color.ORANGE);
                    if (!e.alive) {
                        int pts = e.getPoints() * level;
                        score += pts;
                        bossAlive = false;
                        spawnExplosion(e.x, e.y,
                            e.getType() == EnemyType.BOSS ? 40 : 12,
                            e.getType() == EnemyType.BOSS ? Color.MAGENTA : Color.ORANGE);
                        SoundEngine.explosion();
                        screenShake = e.getType() == EnemyType.BOSS ? 18 : 5;
                        addPopup("+" + pts, e.x, e.y,
                            e.getType() == EnemyType.BOSS ?
                                new Color(255,200,0) : Color.WHITE);
                        // Chance to drop power-up
                        if (Math.random() < (e.getType() == EnemyType.BOSS ? 0.9 : 0.18)) {
                            PowerUpType[] types = PowerUpType.values();
                            powerUps.add(new PowerUp(e.x, e.y,
                                types[(int)(Math.random() * types.length)]));
                        }
                    }
                    break;
                }
            }
        }

        // Enemy bullets vs player
        for (Bullet b : bullets) {
            if (!b.alive || b.isFromPlayer() || !player.alive) continue;
            if (b.collidesWith(player)) {
                b.alive = false;
                player.takeDamage(b.getDamage());
                screenShake = 10;
                SoundEngine.hit();
                if (!player.alive) {
                    player.loseLife();
                    if (player.getLives() > 0) {
                        // Respawn
                        player.alive = true;
                        player.restoreHp();
                        player.x = W / 2f;
                        player.y = H - 80;
                        addPopup("LIFE LOST!", W/2f, H/2f, new Color(255,80,80));
                    }
                }
            }
        }

        // Enemies vs player
        for (Enemy e : enemies) {
            if (!e.alive) continue;
            if (e.collidesWith(player)) {
                player.takeDamage(30);
                e.alive = false;
                screenShake = 14;
                spawnExplosion(e.x, e.y, 12, Color.ORANGE);
                SoundEngine.explosion();
            }
        }

        // Player vs power-ups
        for (PowerUp p : powerUps) {
            if (!p.alive) continue;
            if (p.collidesWith(player)) {
                p.alive = false;
                if (p.getType() == PowerUpType.BOMB) {
                    // Destroy all enemies
                    for (Enemy e : enemies) {
                        if (!e.alive) continue;
                        score += e.getPoints() * level;
                        spawnExplosion(e.x, e.y, 10, Color.ORANGE);
                        e.alive = false;
                        bossAlive = false;
                    }
                    screenShake = 20;
                    SoundEngine.explosion();
                    addPopup("BOMB!", W/2f, H/2f, new Color(255,80,80));
                } else {
                    player.applyPowerUp(p.getType());
                    addPopup(p.getType().label + "!", player.x, player.y - 30,
                             p.getType().color);
                }
            }
        }
    }

    // ----------------------------------------------------------------
    //  WAVE SPAWNING
    // ----------------------------------------------------------------
    private void spawnWave() {
        waveTimer = 0;
        wave++;
        enemies.clear();
        bullets.removeIf(b -> !b.isFromPlayer());

        boolean bossWave = (wave % 5 == 0);

        if (bossWave) {
            // BOSS wave
            Enemy boss = new Enemy(W / 2f, -40, EnemyType.BOSS, 0);
            enemies.add(boss);
            bossAlive = true;
            addPopup("!! BOSS !!", W/2f, H/2f, new Color(255,50,50));
            SoundEngine.explosion();
        } else {
            int grunts   = 3 + level * 2 + wave;
            int shooters = level >= 2 ? 1 + level   : 0;
            int tanks    = level >= 3 ? level - 1   : 0;

            float spacing = (float) W / (grunts + 1);
            for (int i = 0; i < grunts; i++) {
                float wx = spacing * (i + 1);
                float wy = -30 - (float)(Math.random() * 60);
                enemies.add(new Enemy(wx, wy, EnemyType.GRUNT,
                        (float)(Math.random() * Math.PI * 2)));
            }
            float sSpacing = (float) W / (shooters + 1);
            for (int i = 0; i < shooters; i++) {
                enemies.add(new Enemy(sSpacing*(i+1), -80-(float)(Math.random()*40),
                        EnemyType.SHOOTER, (float)(Math.random()*Math.PI*2)));
            }
            for (int i = 0; i < tanks; i++) {
                enemies.add(new Enemy(80 + (float)(Math.random()*(W-160)),
                        -120, EnemyType.TANK, 0));
            }
        }
    }

    private void tickLevelComplete() {
        levelCompleteTimer++;
        if (levelCompleteTimer > 180) {
            levelCompleteTimer = 0;
            level++;
            wave  = 0;
            waveTimer = 0;
            enemies.clear();
            bullets.clear();
            powerUps.clear();
            particles.clear();
            player.restoreHp();
            SoundEngine.levelUp();
            state = GameState.PLAYING;
            spawnWave();
        }
    }

    // ----------------------------------------------------------------
    //  HELPERS
    // ----------------------------------------------------------------
    private void spawnExplosion(float x, float y, int count, Color c) {
        for (int i = 0; i < count; i++) particles.add(new Particle(x, y, c));
    }

    private void addPopup(String text, float x, float y, Color c) {
        popups.add(new Object[]{text, x, y - 20, 90,
                                c.getRed(), c.getGreen(), c.getBlue()});
    }

    private void startGame() {
        score = 0; level = 1; wave = 0; waveTimer = 0; bossAlive = false;
        enemies.clear(); bullets.clear(); powerUps.clear();
        particles.clear(); popups.clear();
        player = new Player(W, H);
        state = GameState.PLAYING;
        spawnWave();
    }

    // ----------------------------------------------------------------
    //  RENDERING
    // ----------------------------------------------------------------
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Screen shake
        if (screenShake > 0) {
            int sx = (int)((Math.random()-0.5)*screenShake*1.5);
            int sy = (int)((Math.random()-0.5)*screenShake*1.5);
            g2.translate(sx, sy);
        }

        // Background
        drawBackground(g2);

        switch (state) {
            case MENU:          drawMenu(g2);       break;
            case PLAYING:       drawPlaying(g2);    break;
            case PAUSED:        drawPlaying(g2);
                                drawPause(g2);      break;
            case GAME_OVER:     drawGameOver(g2);   break;
            case LEVEL_COMPLETE:drawLevelComplete(g2);break;
            case HIGH_SCORES:   drawHighScores(g2); break;
        }
    }

    private void drawBackground(Graphics2D g2) {
        // Space gradient
        GradientPaint bg = new GradientPaint(0,0,new Color(5,5,20),
                                              0,H,new Color(15,5,35));
        g2.setPaint(bg);
        g2.fillRect(0, 0, W, H);
        // Stars
        for (Star s : stars) s.render(g2);
    }

    private void drawPlaying(Graphics2D g2) {
        // Game objects
        for (PowerUp p  : powerUps)  p.render(g2);
        for (Enemy e    : enemies)   e.render(g2);
        for (Bullet b   : bullets)   b.render(g2);
        for (Particle p : particles) p.render(g2);
        if (player != null && player.alive) player.render(g2);

        // Floating score popups
        for (Object[] p : popups) {
            String text = (String) p[0];
            float  px   = (Float)(p[1] instanceof Float ? p[1] : ((Number)p[1]).floatValue());
            float  py   = (Float)(p[2] instanceof Float ? p[2] : ((Number)p[2]).floatValue()) -
                           (90 - (int)p[3]) * 0.4f;
            int    life = (int) p[3];
            int    r    = (int) p[4], gr = (int) p[5], bl = (int) p[6];
            float  alpha = Math.min(1f, life / 60f);
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(new Color(r, gr, bl, (int)(alpha * 230)));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, px - fm.stringWidth(text)/2f, py);
        }

        drawHUD(g2);
    }

    private void drawHUD(Graphics2D g2) {
        // Top bar background
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, W, 52);
        g2.setColor(new Color(0, 150, 255, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(0, 52, W, 52);

        // Score
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(0, 200, 255));
        g2.drawString("SCORE", 12, 18);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(String.format("%07d", score), 12, 40);

        // Level / Wave
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(new Color(0, 200, 255));
        g2.drawString("LEVEL " + level + "  WAVE " + wave, W/2 - 55, 18);

        // HP bar
        int hpW = 120, hpH = 12;
        int hpX = W - hpW - 12, hpY = 10;
        g2.setColor(new Color(255,255,255,40));
        g2.fillRoundRect(hpX-1, hpY-1, hpW+2, hpH+2, 6, 6);
        g2.setColor(new Color(40,40,40));
        g2.fillRoundRect(hpX, hpY, hpW, hpH, 6, 6);
        float hpRatio = (float)player.getHp() / player.getMaxHp();
        Color hpCol = hpRatio > 0.5f ? new Color(0,220,80) :
                      hpRatio > 0.25f? new Color(255,180,0) : new Color(255,50,50);
        g2.setColor(hpCol);
        g2.fillRoundRect(hpX, hpY, (int)(hpW * hpRatio), hpH, 6, 6);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        g2.drawString("HP " + player.getHp() + "/" + player.getMaxHp(),
                      hpX, hpY + hpH + 14);

        // Lives
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(0,200,255));
        g2.drawString("LIVES: ", hpX, 44);
        g2.setColor(new Color(100, 200, 255));
        for (int i = 0; i < player.getLives(); i++) {
            int tx = hpX + 50 + i * 18;
            int[] sx = {tx, tx-6, tx+6};
            int[] sy = {hpY+28, hpY+38, hpY+38};
            g2.fillPolygon(sx, sy, 3);
        }

        // Active power-ups indicator
        int pux = 12, puy = 60;
        if (player.isShielded()) drawPowerUpIndicator(g2, "SHIELD",    PowerUpType.SHIELD.color,    pux, puy);
        if (player.isRapidFire()) {pux += 80; drawPowerUpIndicator(g2, "RAPID",  PowerUpType.RAPID_FIRE.color, pux, puy);}
        if (player.isTriple())   {pux += 80; drawPowerUpIndicator(g2, "TRIPLE", PowerUpType.TRIPLE_SHOT.color, pux, puy);}

        // Controls hint
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.setColor(new Color(100,100,130));
        g2.drawString("WASD/Arrows: Move   Space/Z: Shoot   P: Pause", 12, H - 8);
    }

    private void drawPowerUpIndicator(Graphics2D g2, String label, Color c,
                                      int x, int y) {
        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 120));
        g2.fillRoundRect(x, y, 72, 20, 8, 8);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, 72, 20, 8, 8);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        g2.drawString(label, x + 8, y + 14);
    }

    private void drawMenu(Graphics2D g2) {
        // Title glow
        float glow = (float)(0.6 + 0.4 * Math.sin(Math.toRadians(menuPulse)));
        g2.setColor(new Color(0, 150, 255, (int)(glow * 80)));
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2.getFontMetrics();
        String t1 = "GALACTIC";
        String t2 = "DEFENDER";
        g2.drawString(t1, (W - fm.stringWidth(t1))/2 + 3, 163);
        g2.drawString(t2, (W - fm.stringWidth(t2))/2 + 3, 218);

        g2.setColor(new Color(0, 200, 255));
        g2.drawString(t1, (W - fm.stringWidth(t1))/2, 160);
        g2.setColor(new Color(100, 220, 255));
        g2.drawString(t2, (W - fm.stringWidth(t2))/2, 215);

        // Subtitle
        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        g2.setColor(new Color(150, 170, 200));
        String sub = "Advanced Java Programming  •  UoK";
        g2.drawString(sub, (W - g2.getFontMetrics().stringWidth(sub))/2, 245);

        // Demo ship
        drawMenuShip(g2, W/2f, 310);

        // Buttons
        drawMenuButton(g2, "PRESS  ENTER  TO  PLAY",
                W/2, 420, new Color(0,200,255), 16, glow);
        drawMenuButton(g2, "H — HIGH SCORES",
                W/2, 470, new Color(180,180,220), 13, 0.8f);

        // Controls
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(100,130,160));
        String[] ctrl = {
            "WASD / Arrow Keys  — Move",
            "SPACE / Z          — Shoot",
            "P                  — Pause"
        };
        int cy = 520;
        for (String c : ctrl) {
            g2.drawString(c, (W - g2.getFontMetrics().stringWidth(c))/2, cy);
            cy += 20;
        }
    }

    private void drawMenuShip(Graphics2D g2, float x, float y) {
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.translate(x, y);
        float f = 1.4f; g3.scale(f, f);
        // Thrust
        g3.setColor(new Color(255,150,30,180));
        g3.fillOval(-6, 22, 12, 14);
        g3.setColor(new Color(255,220,80,200));
        g3.fillOval(-4, 22, 8, 10);
        // Hull
        int[] bx = {0,18,13,0,-13,-18};
        int[] by = {-22,-6,10,18,10,-6};
        GradientPaint gp = new GradientPaint(-18,0,new Color(30,80,180),
                                              18,0,new Color(80,160,255));
        g3.setPaint(gp);
        g3.fillPolygon(bx, by, 6);
        g3.setColor(new Color(120,200,255));
        g3.setStroke(new BasicStroke(1.5f));
        g3.drawPolygon(bx, by, 6);
        g3.setColor(new Color(180,240,255,200));
        g3.fillOval(-5,-10,10,12);
        g3.dispose();
    }

    private void drawMenuButton(Graphics2D g2, String text, int cx, int cy,
                                Color c, int fs, float glow) {
        g2.setFont(new Font("Arial", Font.BOLD, fs));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int bx = cx - tw/2 - 14, bw = tw + 28, bh = fs + 14;
        int by = cy - fs;

        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(),
                              (int)(glow * 40 + 20)));
        g2.fillRoundRect(bx, by, bw, bh, 10, 10);
        g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(),
                              (int)(glow * 180 + 60)));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(bx, by, bw, bh, 10, 10);
        g2.setColor(c); g2.drawString(text, cx - tw/2, cy);
    }

    private void drawGameOver(Graphics2D g2) {
        // Overlay
        g2.setColor(new Color(0,0,0,180));
        g2.fillRect(0,0,W,H);

        float p = (float)(0.7 + 0.3*Math.sin(Math.toRadians(menuPulse)));
        g2.setFont(new Font("Arial", Font.BOLD, 54));
        FontMetrics fm = g2.getFontMetrics();
        String go = "GAME OVER";
        g2.setColor(new Color(255, (int)(80*p), (int)(80*p), 180));
        g2.drawString(go, (W-fm.stringWidth(go))/2+2, H/2-70+2);
        g2.setColor(new Color(255, 80, 80));
        g2.drawString(go, (W-fm.stringWidth(go))/2, H/2-70);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        fm = g2.getFontMetrics();
        String sc = "SCORE: " + String.format("%,d", score);
        g2.setColor(Color.WHITE); g2.drawString(sc,(W-fm.stringWidth(sc))/2, H/2-10);

        String lv = "Level: " + level + "   Wave: " + wave;
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2.getFontMetrics();
        g2.setColor(new Color(180,180,220));
        g2.drawString(lv,(W-fm.stringWidth(lv))/2, H/2+22);

        if (hsm.isHighScore(score)) {
            String hs = "*** NEW HIGH SCORE! ***";
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            fm = g2.getFontMetrics();
            g2.setColor(new Color(255,220,0,(int)(200*p)));
            g2.drawString(hs,(W-fm.stringWidth(hs))/2, H/2+56);
        }

        drawMenuButton(g2,"ENTER — Play Again", W/2, H/2+110,
                       new Color(0,200,100), 15, p);
        drawMenuButton(g2,"H — High Scores",    W/2, H/2+150,
                       new Color(180,180,220), 13, 0.8f);
        drawMenuButton(g2,"ESC — Menu",         W/2, H/2+186,
                       new Color(180,100,100), 12, 0.7f);
    }

    private void drawPause(Graphics2D g2) {
        g2.setColor(new Color(0,0,0,160));
        g2.fillRect(0,0,W,H);
        g2.setFont(new Font("Arial", Font.BOLD, 42));
        FontMetrics fm = g2.getFontMetrics();
        String p = "PAUSED";
        g2.setColor(new Color(0,200,255));
        g2.drawString(p,(W-fm.stringWidth(p))/2, H/2-20);
        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        fm = g2.getFontMetrics();
        String r = "Press P to resume";
        g2.setColor(new Color(180,180,220));
        g2.drawString(r,(W-fm.stringWidth(r))/2, H/2+20);
    }

    private void drawLevelComplete(Graphics2D g2) {
        float alpha = Math.min(1f, levelCompleteTimer / 40f);
        g2.setColor(new Color(0,0,0,(int)(alpha*180)));
        g2.fillRect(0,0,W,H);

        float p = (float)(0.7+0.3*Math.sin(Math.toRadians(menuPulse)));
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();
        String t = "LEVEL " + level + " COMPLETE!";
        g2.setColor(new Color(0,(int)(200*p),100,(int)(alpha*255)));
        g2.drawString(t,(W-fm.stringWidth(t))/2, H/2-20);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        fm = g2.getFontMetrics();
        String sc = "Score: " + String.format("%,d", score);
        g2.setColor(new Color(255,255,255,(int)(alpha*200)));
        g2.drawString(sc,(W-fm.stringWidth(sc))/2, H/2+22);

        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        fm = g2.getFontMetrics();
        String next = "Get ready for Level " + (level+1) + "...";
        g2.setColor(new Color(180,200,255,(int)(alpha*180)));
        g2.drawString(next,(W-fm.stringWidth(next))/2, H/2+55);
    }

    private void drawHighScores(Graphics2D g2) {
        g2.setColor(new Color(0,0,0,220)); g2.fillRect(0,0,W,H);

        g2.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2.getFontMetrics();
        String t = "HIGH SCORES";
        g2.setColor(new Color(255,200,0));
        g2.drawString(t,(W-fm.stringWidth(t))/2, 120);

        List<int[]> scores = hsm.getScores();
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        for (int i = 0; i < scores.size(); i++) {
            int[] s = scores.get(i);
            String line = (i+1) + ".   " + String.format("%,d", s[0]) +
                          "   (Level " + s[1] + ")";
            Color c = i == 0 ? new Color(255,215,0) :
                      i == 1 ? new Color(192,192,192) :
                      i == 2 ? new Color(205,127,50) :
                               new Color(180,180,220);
            g2.setColor(c);
            fm = g2.getFontMetrics();
            g2.drawString(line, (W-fm.stringWidth(line))/2, 200 + i*50);
        }
        if (scores.isEmpty()) {
            g2.setFont(new Font("Arial", Font.ITALIC, 16));
            g2.setColor(new Color(150,150,180));
            String none = "No scores yet. Play to set records!";
            fm = g2.getFontMetrics();
            g2.drawString(none,(W-fm.stringWidth(none))/2, 280);
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        fm = g2.getFontMetrics();
        String back = "Press ESC or ENTER to return";
        g2.setColor(new Color(150,170,200));
        g2.drawString(back,(W-fm.stringWidth(back))/2, H-60);
    }

    // ----------------------------------------------------------------
    //  INPUT HANDLING
    // ----------------------------------------------------------------
    @Override public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k < keys.length) keys[k] = true;

        switch (state) {
            case MENU:
                if (k == KeyEvent.VK_ENTER) { SoundEngine.menuBeep(); startGame(); }
                if (k == KeyEvent.VK_H)     { SoundEngine.menuBeep();
                                              state = GameState.HIGH_SCORES; }
                break;
            case PLAYING:
                if (k == KeyEvent.VK_P) state = GameState.PAUSED;
                if (k == KeyEvent.VK_ESCAPE) state = GameState.MENU;
                // Check level complete (all enemies cleared by player, big score)
                break;
            case PAUSED:
                if (k == KeyEvent.VK_P || k == KeyEvent.VK_ENTER)
                    state = GameState.PLAYING;
                if (k == KeyEvent.VK_ESCAPE) state = GameState.MENU;
                break;
            case GAME_OVER:
                if (k == KeyEvent.VK_ENTER) { SoundEngine.menuBeep(); startGame(); }
                if (k == KeyEvent.VK_H)     { SoundEngine.menuBeep();
                                              state = GameState.HIGH_SCORES; }
                if (k == KeyEvent.VK_ESCAPE) state = GameState.MENU;
                break;
            case HIGH_SCORES:
                if (k == KeyEvent.VK_ESCAPE || k == KeyEvent.VK_ENTER)
                    state = GameState.MENU;
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k < keys.length) keys[k] = false;

        // Check for level complete when wave threshold reached
        if (state == GameState.PLAYING && wave >= 3 + level * 2 && enemies.isEmpty()) {
            state = GameState.LEVEL_COMPLETE;
            levelCompleteTimer = 0;
            SoundEngine.levelUp();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}
