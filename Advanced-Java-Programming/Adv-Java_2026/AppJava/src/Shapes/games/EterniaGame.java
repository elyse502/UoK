/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes.games;

/**
 *
 * @author STUDENTS
 */
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Eternia: The Crystal of Balance
 * Enhanced Edition — Real Soldier Sprites + Synthesized Sound
 *
 * HOW TO RUN:
 *   javac EterniaGame.java
 *   java jos_test.EterniaGame
 *
 * CONTROLS:
 *   Arrow Keys / WASD  - Move player
 *   SPACE              - Shoot
 *   ESC                - Pause
 *   R                  - Restart
 *   M                  - Mute/Unmute
 *   ENTER              - Start / Resume
 *   Q                  - Quit to Menu
 *   P                  - Proceed to next level
 */
public class EterniaGame extends JPanel implements ActionListener, KeyListener {

    // ── Constants ──────────────────────────────────────────────────────────
    static final int SCREEN_WIDTH    = 900;
    static final int SCREEN_HEIGHT   = 620;
    static final int PLAYER_SPEED    = 5;
    static final int ATTACK_COOLDOWN = 12;
    static final int ENEMY_ATTACK_CD = 100;
    static final int FPS             = 60;
    static final int PLAYER_ZONE_W   = SCREEN_WIDTH / 2;
    static final int HUD_TOP         = 56;
    static final int HUD_BOTTOM      = 110;

    // ── Colours ────────────────────────────────────────────────────────────
    static final Color WHITE   = Color.WHITE;
    static final Color RED     = new Color(220, 40,  40);
    static final Color BLACK   = Color.BLACK;
    static final Color GREEN   = new Color(50,  210, 90);
    static final Color GOLD    = new Color(255, 205, 30);
    static final Color DARK_BG = new Color(12,  8,   25);

    // ── Level data ─────────────────────────────────────────────────────────
    static final String[] LEVEL_NAMES = {
        "The Forest of Beginnings",
        "The Caverns of Despair",
        "The Citadel of Eternity"
    };
    static final Color[] BG_COLORS = {
        new Color(14, 38, 14),
        new Color(14, 10, 32),
        new Color(32,  8,  8)
    };

    // ── Game state ─────────────────────────────────────────────────────────
    enum Screen { MENU, PLAYING, PAUSED, LEVEL_COMPLETE, GAME_OVER, WIN }

    Screen  screen      = Screen.MENU;
    int     level       = 1;
    int     lives       = 3;
    boolean musicMuted  = false;

    Player           player;
    List<Enemy>      enemies     = new CopyOnWriteArrayList<>();
    List<Projectile> projectiles = new CopyOnWriteArrayList<>();
    List<Particle>   particles   = new CopyOnWriteArrayList<>();

    Random rng = new Random();
    javax.swing.Timer gameTimer;

    // Input
    boolean keyUp, keyDown, keyLeft, keyRight, keySpace;

    // Sound
    SoundEngine sound;

    // Tick counter for animations / star twinkle
    long tick = 0;

    // ── Constructor ────────────────────────────────────────────────────────
    public EterniaGame() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(BLACK);
        setFocusable(true);
        addKeyListener(this);

        sound     = new SoundEngine();
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    // ── Setup ──────────────────────────────────────────────────────────────
    void setup() {
        player = new Player();
        enemies.clear();
        projectiles.clear();
        particles.clear();
        int n = (level == 1) ? 5 : (level == 2) ? 8 : 13;
        for (int i = 0; i < n; i++)
            enemies.add(new Enemy(rng.nextDouble() < 0.25));
    }

    // ── Game loop ──────────────────────────────────────────────────────────
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        if (screen == Screen.PLAYING) update();
        repaint();
    }

    void update() {
        // Player movement
        int dx = 0, dy = 0;
        if (keyUp)    dy -= PLAYER_SPEED;
        if (keyDown)  dy += PLAYER_SPEED;
        if (keyLeft)  dx -= PLAYER_SPEED;
        if (keyRight) dx += PLAYER_SPEED;
        if (dx != 0 && dy != 0) { dx = (int)(dx*0.707); dy = (int)(dy*0.707); }
        player.rect.x = clamp(player.rect.x + dx, 0,         PLAYER_ZONE_W - player.rect.width);
        player.rect.y = clamp(player.rect.y + dy, HUD_TOP,   SCREEN_HEIGHT - HUD_BOTTOM - player.rect.height);

        if (player.atkCd > 0) player.atkCd--;
        if (keySpace)          player.shoot();

        // Enemies
        for (Enemy en : enemies) en.update();

        // Projectiles
        List<Projectile> dead = new ArrayList<>();
        for (Projectile p : projectiles) {
            p.update();
            if (p.x < -20 || p.x > SCREEN_WIDTH + 20) { dead.add(p); continue; }

            if (!p.fromPlayer && p.bounds().intersects(player.bounds())) {
                player.health -= p.dmg;
                dead.add(p);
                spawnHitFx(p.x + 8, p.y + 4, new Color(80, 180, 255));
            }
            if (p.fromPlayer) {
                for (Enemy en : enemies) {
                    if (p.bounds().intersects(en.bounds())) {
                        en.health -= p.dmg;
                        dead.add(p);
                        spawnHitFx((int)en.rect.getCenterX(), (int)en.rect.getCenterY(),
                                   new Color(255, 90, 90));
                        if (en.health <= 0) {
                            spawnDeathFx((int)en.rect.getCenterX(), (int)en.rect.getCenterY());
                            sound.play("die");
                        }
                        break;
                    }
                }
            }
        }
        projectiles.removeAll(dead);
        enemies.removeIf(en -> en.health <= 0);

        // Particles
        particles.removeIf(p -> { p.update(); return p.dead; });

        if (player.health <= 0) {
            sound.play("gameover");
            lives--;
            if (lives <= 0) { screen = Screen.GAME_OVER; return; }
            setup();
            return;
        }
        if (enemies.isEmpty()) {
            sound.play("levelup");
            screen = (level == 3) ? Screen.WIN : Screen.LEVEL_COMPLETE;
        }
    }

    // ── Particles ──────────────────────────────────────────────────────────
    void spawnHitFx(int x, int y, Color c) {
        for (int i = 0; i < 7; i++)
            particles.add(new Particle(x, y, c,
                (float)(rng.nextGaussian()*3.5), (float)(rng.nextGaussian()*3.5), 18+rng.nextInt(12)));
    }
    void spawnDeathFx(int x, int y) {
        spawnHitFx(x, y, new Color(230, 60, 60));
        spawnHitFx(x, y, new Color(255, 160, 40));
        spawnHitFx(x, y, new Color(255, 230, 100));
    }

    // ─────────────────────────────────────────────────────────────────────
    // PAINT
    // ─────────────────────────────────────────────────────────────────────
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        switch (screen) {
            case MENU           -> drawMenu(g);
            case PLAYING        -> drawGame(g);
            case PAUSED         -> drawPause(g);
            case LEVEL_COMPLETE -> drawLevelComplete(g);
            case GAME_OVER      -> drawGameOver(g);
            case WIN            -> drawWin(g);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SCREEN RENDERS
    // ══════════════════════════════════════════════════════════════════════

    void drawMenu(Graphics2D g) {
        // Starfield bg
        drawStarfield(g, DARK_BG, new Color(40, 10, 60));

        // Title glow
        drawGlowText(g, new Font("Courier New", Font.BOLD, 38),
                "ETERNIA: THE CRYSTAL OF BALANCE", GOLD, new Color(140, 100, 0),
                SCREEN_WIDTH/2, SCREEN_HEIGHT/2 - 165);

        g.setFont(new Font("Courier New", Font.PLAIN, 17));
        String[] lines = {
            "ENTER  ─ Start Game",
            "ARROWS / WASD  ─ Move",
            "SPACE  ─ Shoot",
            "ESC    ─ Pause",
            "R      ─ Restart",
            "M      ─ Mute / Unmute",
            "Q      ─ Quit"
        };
        for (int i = 0; i < lines.length; i++)
            drawCentered(g, lines[i], new Color(200, 200, 230), SCREEN_HEIGHT/2 - 80 + i*34);

        g.setFont(new Font("Courier New", Font.ITALIC, 15));
        drawCentered(g, musicMuted ? "♪ Music: OFF" : "♪ Music: ON",
                musicMuted ? new Color(160,100,100) : GREEN, SCREEN_HEIGHT - 36);

        // Preview tiny soldiers
        drawSoldier(g, SCREEN_WIDTH/2 - 90, SCREEN_HEIGHT/2 + 130, true,  false, 0);
        drawSoldier(g, SCREEN_WIDTH/2 + 90, SCREEN_HEIGHT/2 + 130, false, false, 180);
    }

    void drawGame(Graphics2D g) {
        // Background
        Color bg = BG_COLORS[Math.min(level-1, 2)];
        g.setColor(bg);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        drawBgDetails(g, bg);

        // Zone divider
        g.setColor(new Color(255,255,255,30));
        float[] dash = {10,8};
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash, tick%18));
        g.drawLine(PLAYER_ZONE_W, HUD_TOP, PLAYER_ZONE_W, SCREEN_HEIGHT - HUD_BOTTOM);
        g.setStroke(new BasicStroke(1));

        // Particles
        for (Particle p : particles) drawParticle(g, p);

        // Entities
        for (Enemy en : enemies)     drawEnemy(g, en);
        for (Projectile p : projectiles) drawProjectile(g, p);
        drawPlayerEntity(g, player);

        drawHud(g);
    }

    void drawBgDetails(Graphics2D g, Color base) {
        // Subtle grid
        Color grid = new Color(
            Math.min(255, base.getRed()+12),
            Math.min(255, base.getGreen()+12),
            Math.min(255, base.getBlue()+12), 60);
        g.setColor(grid);
        g.setStroke(new BasicStroke(0.5f));
        for (int x = 0; x < SCREEN_WIDTH; x+=40)  g.drawLine(x, HUD_TOP, x, SCREEN_HEIGHT - HUD_BOTTOM);
        for (int y = HUD_TOP; y < SCREEN_HEIGHT - HUD_BOTTOM; y+=40) g.drawLine(0, y, SCREEN_WIDTH, y);
        g.setStroke(new BasicStroke(1));
    }

    void drawHud(Graphics2D g) {
        // Top bar
        g.setColor(new Color(0,0,0,190));
        g.fillRect(0, 0, SCREEN_WIDTH, HUD_TOP);
        g.setColor(GOLD);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(0, HUD_TOP, SCREEN_WIDTH, HUD_TOP);
        g.setStroke(new BasicStroke(1));

        // Level name
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(GOLD);
        g.drawString(LEVEL_NAMES[Math.min(level-1,2)], 14, 28);

        // Health bar
        int bw = 180, bh = 12, bx = 14, by = HUD_TOP - 20;
        g.setColor(new Color(50,10,10));
        g.fillRoundRect(bx, by, bw, bh, 4,4);
        float hpPct = player.health / 100f;
        Color hpc = hpPct > 0.5f ? GREEN : hpPct > 0.25f ? new Color(230,180,30) : RED;
        g.setColor(hpc);
        g.fillRoundRect(bx, by, (int)(bw*hpPct), bh, 4,4);
        g.setColor(new Color(255,255,255,60));
        g.fillRoundRect(bx, by, (int)(bw*hpPct), bh/2, 4,4);
        g.setColor(new Color(255,255,255,80));
        g.drawRoundRect(bx, by, bw, bh, 4,4);
        g.setFont(new Font("Courier New", Font.BOLD, 12));
        g.setColor(WHITE);
        g.drawString("HP " + player.health + "/100", bx+bw+8, by+10);

        // Right HUD items
        g.setFont(new Font("Courier New", Font.BOLD, 16));
        int rx = SCREEN_WIDTH - 14;
        String[] items = { "LVL " + level, "LIVES " + lives, "ENEMIES " + enemies.size() };
        Color[] cols = { GOLD, new Color(100,200,255), new Color(255,120,120) };
        for (int i = 0; i < items.length; i++) {
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(items[i]);
            g.setColor(cols[i]);
            g.drawString(items[i], rx - sw, 32);
            rx -= sw + 28;
        }
    }

    void drawPause(Graphics2D g) {
        drawGame(g);
        g.setColor(new Color(0,0,0,175));
        g.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        drawGlowText(g, new Font("Courier New", Font.BOLD, 56), "PAUSED",
                GOLD, new Color(120,90,0), SCREEN_WIDTH/2, SCREEN_HEIGHT/2 - 70);
        g.setFont(new Font("Courier New", Font.PLAIN, 24));
        drawCentered(g, "ENTER – Resume",     WHITE, SCREEN_HEIGHT/2 + 10);
        drawCentered(g, "R     – Restart",    WHITE, SCREEN_HEIGHT/2 + 48);
        drawCentered(g, "Q     – Quit Menu",  WHITE, SCREEN_HEIGHT/2 + 86);
    }

    void drawLevelComplete(Graphics2D g) {
        drawStarfield(g, new Color(8,25,8), new Color(20,60,20));
        drawGlowText(g, new Font("Courier New", Font.BOLD, 46),
                "LEVEL " + level + " COMPLETE!", GREEN, new Color(10,100,10),
                SCREEN_WIDTH/2, SCREEN_HEIGHT/2 - 80);
        g.setFont(new Font("Courier New", Font.PLAIN, 24));
        drawCentered(g, "P – Proceed to Level " + (level+1), WHITE, SCREEN_HEIGHT/2 + 10);
        drawCentered(g, "M – Main Menu",                     WHITE, SCREEN_HEIGHT/2 + 52);
        drawCentered(g, "Q – Quit",                          WHITE, SCREEN_HEIGHT/2 + 94);
    }

    void drawGameOver(Graphics2D g) {
        drawStarfield(g, new Color(25,4,4), new Color(60,10,10));
        drawGlowText(g, new Font("Courier New", Font.BOLD, 60), "GAME OVER",
                RED, new Color(100,10,10), SCREEN_WIDTH/2, SCREEN_HEIGHT/2 - 70);
        g.setFont(new Font("Courier New", Font.PLAIN, 24));
        drawCentered(g, "R – Restart",     WHITE, SCREEN_HEIGHT/2 + 15);
        drawCentered(g, "Q – Quit Menu",   WHITE, SCREEN_HEIGHT/2 + 55);
    }

    void drawWin(Graphics2D g) {
        drawStarfield(g, new Color(8,8,28), new Color(30,20,60));
        drawGlowText(g, new Font("Courier New", Font.BOLD, 42),
                "★  VICTORY  ★", GOLD, new Color(130,95,0),
                SCREEN_WIDTH/2, SCREEN_HEIGHT/2 - 100);
        g.setFont(new Font("Courier New", Font.PLAIN, 22));
        drawCentered(g, "You conquered all three levels!", WHITE,              SCREEN_HEIGHT/2 - 20);
        drawCentered(g, "The Crystal of Balance is restored.", new Color(180,255,180), SCREEN_HEIGHT/2 + 22);
        g.setFont(new Font("Courier New", Font.PLAIN, 20));
        drawCentered(g, "M – Main Menu", WHITE, SCREEN_HEIGHT/2 + 82);
        drawCentered(g, "Q – Quit",      WHITE, SCREEN_HEIGHT/2 + 116);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ENTITY DRAWING
    // ══════════════════════════════════════════════════════════════════════

    void drawPlayerEntity(Graphics2D g, Player p) {
        int cx = (int)p.rect.getCenterX();
        int cy = (int)p.rect.getCenterY();
        // Low-health danger glow
        if (p.health <= 30) {
            int pulse = (int)(Math.abs(Math.sin(tick * 0.12)) * 60) + 20;
            g.setColor(new Color(255, 0, 0, pulse));
            g.fillOval(cx-30, cy-32, 60, 64);
        }
        // Shadow
        g.setColor(new Color(0,0,0,55));
        g.fillOval(cx-16, cy+24, 32, 9);
        // Soldier facing right (toward enemies)
        drawSoldier(g, cx, cy, true, false, 0);
    }

    void drawEnemy(Graphics2D g, Enemy en) {
        int cx = (int)en.rect.getCenterX();
        int cy = (int)en.rect.getCenterY();
        // Shadow
        g.setColor(new Color(0,0,0,50));
        g.fillOval(cx-14, cy+22, 28, 8);
        // Soldier facing left (toward player)
        drawSoldier(g, cx, cy, false, en.isStrong, 180);
        // Health bar
        drawEnemyHealthBar(g, en);
    }

    /**
     * Draw a detailed human soldier sprite.
     * @param cx       centre-x of soldier
     * @param cy       centre-y of torso
     * @param allied   true = blue allied, false = red enemy
     * @param elite    true = add crown / officer markings
     * @param faceDeg  0 = faces RIGHT, 180 = faces LEFT (horizontal flip)
     */
    void drawSoldier(Graphics2D g, int cx, int cy, boolean allied, boolean elite, double faceDeg) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(cx, cy);
        if (faceDeg != 0) g2.scale(-1, 1); // mirror for left-facing

        // ── Palette ────────────────────────────────────────────────
        Color skin        = new Color(235, 190, 138);
        Color skinShad    = new Color(190, 145,  95);
        Color uniform     = allied ? new Color(35,  80, 175) : new Color(165, 28,  28);
        Color uniformDark = allied ? new Color(18,  45, 115) : new Color(100, 12,  12);
        Color uniformLite = allied ? new Color(80, 140, 220) : new Color(210, 75,  75);
        Color helmet      = allied ? new Color(22,  52, 148) : new Color(120,  8,   8);
        Color helmetShine = allied ? new Color(90, 155, 235) : new Color(200, 60,  60);
        Color bootClr     = new Color(28,  20,  12);
        Color glovClr     = new Color(48,  35,  18);
        Color beltClr     = new Color(42,  33,  16);
        Color metalDark   = new Color(50,  50,  55);
        Color metalLite   = new Color(105, 105, 115);
        Color stockClr    = new Color(90,  55,  25);

        Stroke thin    = new BasicStroke(0.7f);
        Stroke med     = new BasicStroke(1.2f);

        // ── LEGS ──────────────────────────────────────────────────
        g2.setColor(uniformDark);
        g2.fillRoundRect( 2,  10,  8, 18, 3, 3); // left thigh
        g2.fillRoundRect(-10, 10,  8, 18, 3, 3); // right thigh
        g2.setColor(uniform);
        g2.fillRoundRect( 3,  10,  5,  9, 2, 2); // left thigh highlight
        g2.fillRoundRect(-9,  10,  5,  9, 2, 2); // right thigh highlight
        g2.setColor(uniformDark);
        g2.fillRoundRect( 3,  26,  7, 11, 2, 2); // left shin
        g2.fillRoundRect(-9,  26,  7, 11, 2, 2); // right shin

        // kneepads
        g2.setColor(new Color(uniformDark.getRed()-10, uniformDark.getGreen()-10, uniformDark.getBlue()+10));
        g2.fillRoundRect( 3, 24,  6, 5, 2, 2);
        g2.fillRoundRect(-8, 24,  6, 5, 2, 2);

        // ── BOOTS ─────────────────────────────────────────────────
        g2.setColor(bootClr);
        g2.fillRoundRect( 1, 34, 10, 7, 3, 3); // left boot
        g2.fillRoundRect(-9, 34, 10, 7, 3, 3); // right boot
        g2.setColor(new Color(75, 60, 38));     // boot shine
        g2.fillRect(3,  35, 4, 2);
        g2.fillRect(-7, 35, 4, 2);

        // ── BELT & POUCHES ────────────────────────────────────────
        g2.setColor(beltClr);
        g2.fillRoundRect(-11, 6, 22, 6, 2, 2);
        g2.setColor(new Color(190, 160, 65));  // buckle
        g2.fillRect(-2, 7, 4, 4);
        // ammo pouch left
        g2.setColor(new Color(55, 42, 20));
        g2.fillRoundRect(-11, 7, 5, 5, 1, 1);
        // grenade clip right
        g2.setColor(new Color(60, 75, 45));
        g2.fillOval(7, 7, 4, 5);

        // ── TORSO / BODY ARMOUR ────────────────────────────────────
        g2.setColor(uniform);
        g2.fillRoundRect(-12, -18, 24, 27, 5, 5);
        // chest plate highlight
        g2.setColor(uniformLite);
        g2.fillRoundRect(-9, -16, 18, 13, 4, 4);
        // centre seam
        g2.setColor(uniformDark);
        g2.setStroke(thin);
        g2.drawLine(0, -17, 0, 6);
        // shoulder pads
        g2.setColor(uniformDark);
        g2.fillRoundRect(-17, -17, 7, 9, 3, 3);
        g2.fillRoundRect(10,  -17, 7, 9, 3, 3);
        // vest pockets
        g2.setColor(uniformDark);
        g2.setStroke(med);
        g2.drawRect(-9, -13, 5, 5);
        g2.drawRect( 4, -13, 5, 5);
        // tactical vest straps
        g2.setColor(new Color(uniformDark.getRed()+20, uniformDark.getGreen()+20, uniformDark.getBlue()+20));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(-6, -18, -6, 6);
        g2.drawLine( 6, -18,  6, 6);

        // ── ARMS ─────────────────────────────────────────────────
        // Left arm (off-hand)
        g2.setColor(uniform);
        g2.fillRoundRect(-20, -16, 8, 15, 3, 3);
        g2.setColor(uniformDark);
        g2.fillRoundRect(-20,  -3, 7, 11, 2, 2);
        g2.setColor(glovClr);
        g2.fillRoundRect(-20,  7,  8,  8, 3, 3);

        // Right arm (gun arm)
        g2.setColor(uniform);
        g2.fillRoundRect(12, -16, 8, 15, 3, 3);
        g2.setColor(uniformDark);
        g2.fillRoundRect(12,  -3, 7, 11, 2, 2);
        g2.setColor(glovClr);
        g2.fillRoundRect(12,   7, 8,  8, 3, 3);

        // ── NECK ─────────────────────────────────────────────────
        g2.setColor(skinShad);
        g2.fillRoundRect(-3, -23, 7, 7, 2, 2);

        // ── HEAD ─────────────────────────────────────────────────
        g2.setColor(skin);
        g2.fillOval(-9, -42, 18, 20);
        // face shading
        g2.setColor(skinShad);
        g2.fillArc(-9, -42, 7, 20, 90, 180);
        // ear L
        g2.setColor(skin);
        g2.fillOval(-11, -37, 5, 8);
        g2.setColor(skinShad);
        g2.fillOval(-10, -36, 3, 6);
        // ear R
        g2.setColor(skin);
        g2.fillOval(6,   -37, 5, 8);
        g2.setColor(skinShad);
        g2.fillOval(7,   -36, 3, 6);
        // eyes
        g2.setColor(new Color(25, 18, 8));
        g2.fillOval(-6, -36, 3, 3);
        g2.fillOval( 3, -36, 3, 3);
        g2.setColor(Color.WHITE);
        g2.fillOval(-6, -37, 2, 2);
        g2.fillOval( 3, -37, 2, 2);
        // nose
        g2.setColor(skinShad);
        g2.fillOval(-1, -31, 3, 3);
        // mouth
        g2.setColor(new Color(160, 90, 70));
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawArc(-4, -27, 8, 4, 200, 140);
        // brow
        g2.setColor(new Color(100, 70, 40));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(-6, -38, -2, -37);
        g2.drawLine( 6, -38,  2, -37);

        // ── HELMET ───────────────────────────────────────────────
        g2.setColor(helmet);
        g2.fillArc(-11, -48, 22, 22, 0, 180);  // dome
        g2.fillRoundRect(-12, -37, 24, 6, 2, 2); // brim
        // shine
        g2.setColor(helmetShine);
        g2.fillArc(-7, -47, 12, 14, 20, 110);
        // band
        g2.setColor(uniformDark);
        g2.setStroke(thin);
        g2.drawLine(-11, -40, 11, -40);
        // chin strap
        g2.setColor(new Color(38, 28, 14));
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawLine(-9, -37, -7, -25);
        g2.drawLine( 9, -37,  7, -25);
        // Optional camo spot
        g2.setColor(new Color(uniformDark.getRed(), uniformDark.getGreen()+10, uniformDark.getBlue(), 120));
        g2.fillOval(-5, -46, 5, 4);
        g2.fillOval( 2, -44, 4, 3);

        // ── ASSAULT RIFLE ─────────────────────────────────────────
        // Stock (wood)
        g2.setColor(stockClr);
        g2.fillRoundRect( 8, 10, 8, 5, 2, 2);
        // Receiver body
        g2.setColor(metalDark);
        g2.fillRoundRect(10, 5, 14, 7, 2, 2);
        // Barrel
        g2.fillRoundRect(22, 7, 18, 4, 1, 1);
        // Barrel shine
        g2.setColor(metalLite);
        g2.fillRect(23, 7, 14, 2);
        // Magazine
        g2.setColor(new Color(40, 40, 45));
        g2.fillRoundRect(14, 11, 4, 8, 1, 1);
        // Trigger guard + trigger
        g2.setColor(metalDark);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawArc(11, 9, 6, 6, 200, 160);
        g2.setColor(metalLite);
        g2.fillRect(13, 11, 1, 3);
        // Scope / iron sights
        g2.setColor(new Color(30,30,35));
        g2.fillRect(15, 4, 6, 3);
        g2.setColor(new Color(80,80,90));
        g2.fillRect(16, 4, 4, 2);
        // Front sight post
        g2.setColor(metalDark);
        g2.fillRect(35, 5, 2, 3);
        // Muzzle glow hint
        g2.setColor(new Color(255, 220, 80, 70));
        g2.fillOval(37, 6, 5, 5);

        // ── ELITE OFFICER MARKINGS ─────────────────────────────────
        if (elite) {
            // Gold chevrons on shoulder
            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(1.8f));
            for (int ci = 0; ci < 2; ci++) {
                int oy = -9 - ci*5;
                g2.drawLine(-7, oy-2, -4, oy);
                g2.drawLine(-4, oy,   -1, oy-2);
            }
            // Gold band on helmet
            g2.setColor(new Color(255, 210, 40, 180));
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawArc(-11, -48, 22, 22, 15, 150);
        }

        // ── SUBTLE OUTLINE ────────────────────────────────────────
        g2.setColor(new Color(0,0,0,50));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawOval(-9, -42, 18, 20);
        g2.drawRoundRect(-12, -18, 24, 27, 5, 5);

        g2.dispose();
    }

    void drawProjectile(Graphics2D g, Projectile p) {
        if (p.fromPlayer) {
            // Cyan energy bolt
            g.setColor(new Color(0, 200, 255, 55));
            g.fillRoundRect(p.x-3, p.y-4, 22, 14, 8, 8);
            g.setColor(new Color(0, 230, 255));
            g.fillRoundRect(p.x, p.y, 18, 8, 5, 5);
            g.setColor(new Color(200, 255, 255));
            g.fillRoundRect(p.x+2, p.y+2, 12, 4, 3, 3);
        } else {
            // Orange energy bolt
            g.setColor(new Color(255, 130, 0, 55));
            g.fillRoundRect(p.x-3, p.y-4, 22, 14, 8, 8);
            g.setColor(new Color(255, 140, 0));
            g.fillRoundRect(p.x, p.y, 18, 8, 5, 5);
            g.setColor(new Color(255, 220, 120));
            g.fillRoundRect(p.x+2, p.y+2, 10, 4, 3, 3);
        }
    }

    void drawParticle(Graphics2D g, Particle p) {
        int a = (int)(255 * ((float)p.life / p.maxLife));
        g.setColor(new Color(p.c.getRed(), p.c.getGreen(), p.c.getBlue(), Math.max(0,Math.min(255,a))));
        int sz = 2 + (int)(4 * ((float)p.life / p.maxLife));
        g.fillOval((int)p.x - sz/2, (int)p.y - sz/2, sz, sz);
    }

    void drawEnemyHealthBar(Graphics2D g, Enemy en) {
        int bw = 34, bh = 5;
        int bx = (int)en.rect.getCenterX() - bw/2;
        int by = en.rect.y - 12;
        float pct = (float)en.health / (en.isStrong ? 20 : 10);
        g.setColor(new Color(50,10,10,190));
        g.fillRoundRect(bx-1, by-1, bw+2, bh+2, 3,3);
        g.setColor(pct > 0.5f ? GREEN : pct > 0.25f ? new Color(230,180,30) : RED);
        g.fillRoundRect(bx, by, (int)(bw*pct), bh, 2,2);
        g.setColor(new Color(255,255,255,50));
        g.drawRoundRect(bx, by, bw, bh, 2,2);
    }

    // ══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════

    void drawStarfield(Graphics2D g, Color top, Color bottom) {
        GradientPaint gp = new GradientPaint(0,0,top, 0,SCREEN_HEIGHT,bottom);
        g.setPaint(gp);
        g.fillRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        Random sr = new Random(42);
        for (int i = 0; i < 120; i++) {
            int sx = sr.nextInt(SCREEN_WIDTH);
            int sy = sr.nextInt(SCREEN_HEIGHT);
            int bri = 100 + sr.nextInt(155);
            int sz  = sr.nextInt(2)+1;
            int twinkle = (int)(Math.abs(Math.sin((tick*0.04) + i)) * 60);
            g.setColor(new Color(bri, bri, Math.min(255, bri+40), 120 + twinkle));
            g.fillOval(sx, sy, sz, sz);
        }
    }

    void drawGlowText(Graphics2D g, Font f, String text, Color bright, Color dark, int cx, int y) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        int tx = cx - fm.stringWidth(text)/2;
        for (int r = 8; r >= 1; r--) {
            int a = 15 + (8-r)*12;
            g.setColor(new Color(bright.getRed(), bright.getGreen(), bright.getBlue(), Math.min(255,a)));
            g.drawString(text, tx+r, y+r);
            g.drawString(text, tx-r, y-r);
            g.drawString(text, tx+r, y-r);
            g.drawString(text, tx-r, y+r);
        }
        g.setColor(dark);  g.drawString(text, tx+2, y+2);
        g.setColor(bright); g.drawString(text, tx, y);
    }

    void drawCentered(Graphics2D g, String text, Color c, int y) {
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (SCREEN_WIDTH - fm.stringWidth(text))/2, y);
    }

    static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

    // ══════════════════════════════════════════════════════════════════════
    // KEY INPUT
    // ══════════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k==KeyEvent.VK_UP    || k==KeyEvent.VK_W) keyUp    = true;
        if (k==KeyEvent.VK_DOWN  || k==KeyEvent.VK_S) keyDown  = true;
        if (k==KeyEvent.VK_LEFT  || k==KeyEvent.VK_A) keyLeft  = true;
        if (k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_D) keyRight = true;
        if (k==KeyEvent.VK_SPACE)                      keySpace = true;

        switch (screen) {
            case MENU -> {
                if (k==KeyEvent.VK_ENTER){ setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_M)    { musicMuted = !musicMuted; }
                if (k==KeyEvent.VK_Q)    System.exit(0);
            }
            case PLAYING -> {
                if (k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if (k==KeyEvent.VK_R)      setup();
                if (k==KeyEvent.VK_M)      musicMuted=!musicMuted;
            }
            case PAUSED -> {
                if (k==KeyEvent.VK_ENTER) screen=Screen.PLAYING;
                if (k==KeyEvent.VK_R)     { setup(); screen=Screen.PLAYING; }
                if (k==KeyEvent.VK_Q)     screen=Screen.MENU;
            }
            case LEVEL_COMPLETE -> {
                if (k==KeyEvent.VK_P){ level++; setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_M){ screen=Screen.MENU; level=1; lives=3; }
                if (k==KeyEvent.VK_Q) System.exit(0);
            }
            case GAME_OVER -> {
                if (k==KeyEvent.VK_R){ lives=3; level=1; setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; lives=3; }
            }
            case WIN -> {
                if (k==KeyEvent.VK_M){ screen=Screen.MENU; level=1; lives=3; }
                if (k==KeyEvent.VK_Q) System.exit(0);
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k==KeyEvent.VK_UP    || k==KeyEvent.VK_W) keyUp    = false;
        if (k==KeyEvent.VK_DOWN  || k==KeyEvent.VK_S) keyDown  = false;
        if (k==KeyEvent.VK_LEFT  || k==KeyEvent.VK_A) keyLeft  = false;
        if (k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_D) keyRight = false;
        if (k==KeyEvent.VK_SPACE)                      keySpace = false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════
    // INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════

    // ── Player ────────────────────────────────────────────────────────────
    class Player {
        Rectangle rect;
        int health = 100;
        int atkCd  = 0;

        Player() {
            rect = new Rectangle(PLAYER_ZONE_W/2 - 20, SCREEN_HEIGHT/2 - 32, 40, 65);
        }
        void shoot() {
            if (atkCd <= 0) {
                projectiles.add(new Projectile(
                    rect.x + rect.width + 2,
                    rect.y + rect.height/2 - 4,
                    11, true, 8));
                atkCd = ATTACK_COOLDOWN;
                sound.play("shoot");
            }
        }
        Rectangle bounds() { return rect; }
    }

    // ── Enemy ─────────────────────────────────────────────────────────────
    class Enemy {
        Rectangle rect;
        int health, maxHealth;
        boolean isStrong;
        int atkCd;

        Enemy(boolean strong) {
            isStrong = strong;
            maxHealth = health = strong ? 20 : 10;
            atkCd = rng.nextInt(ENEMY_ATTACK_CD);
            int w = 40, h = 65;
            int x = rng.nextInt(PLAYER_ZONE_W - 100) + PLAYER_ZONE_W + 50;
            int y = rng.nextInt(SCREEN_HEIGHT - HUD_TOP - HUD_BOTTOM - h) + HUD_TOP;
            rect = new Rectangle(x, y, w, h);
        }
        void update() {
            rect.x += rng.nextInt(5)-2;
            rect.y += rng.nextInt(5)-2;
            rect.x = clamp(rect.x, PLAYER_ZONE_W+2, SCREEN_WIDTH - rect.width - 2);
            rect.y = clamp(rect.y, HUD_TOP+2,        SCREEN_HEIGHT - HUD_BOTTOM - rect.height - 2);
            if (atkCd <= 0) {
                projectiles.add(new Projectile(
                    rect.x - 2,
                    rect.y + rect.height/2 - 4,
                    8, false, 5));
                atkCd = ENEMY_ATTACK_CD;
                sound.play("enemyshoot");
            } else atkCd--;
        }
        Rectangle bounds() { return rect; }
    }

    // ── Projectile ────────────────────────────────────────────────────────
    class Projectile {
        int x, y, speed, dmg;
        boolean fromPlayer;
        Projectile(int x, int y, int speed, boolean fromPlayer, int dmg) {
            this.x=x; this.y=y; this.speed=speed;
            this.fromPlayer=fromPlayer; this.dmg=dmg;
        }
        void update() { x += fromPlayer ? speed : -speed; }
        Rectangle bounds() { return new Rectangle(x, y, 18, 8); }
    }

    // ── Particle ──────────────────────────────────────────────────────────
    class Particle {
        float x, y, vx, vy;
        Color c;
        int life, maxLife;
        boolean dead;
        Particle(int x, int y, Color c, float vx, float vy, int life) {
            this.x=x; this.y=y; this.c=c;
            this.vx=vx; this.vy=vy; this.life=this.maxLife=life;
        }
        void update() {
            x+=vx; y+=vy; vx*=0.90f; vy*=0.90f;
            if (--life<=0) dead=true;
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SOUND ENGINE  (pure Java synthesized – no external audio files)
    // ══════════════════════════════════════════════════════════════════════
    static class SoundEngine {
        private static final int SAMPLE_RATE = 44100;
        boolean muted = false;

        /** Play a named sound on a daemon thread so it never blocks the game loop. */
        void play(String name) {
            if (muted) return;
            byte[] pcm = generate(name);
            if (pcm == null) return;
            Thread t = new Thread(() -> {
                try {
                    AudioFormat fmt = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
                    if (!AudioSystem.isLineSupported(info)) return;
                    try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                        line.open(fmt, 2048);
                        line.start();
                        line.write(pcm, 0, pcm.length);
                        line.drain();
                    }
                } catch (Exception ignored) {}
            });
            t.setDaemon(true);
            t.start();
        }

        private byte[] generate(String name) {
            return switch (name) {
                case "shoot"      -> synth(0.08, f -> triangle(f,800) * adsr(f,0.01,0.02,0.3,0.02) * 0.50);
                case "enemyshoot" -> synth(0.10, f -> triangle(f,520) * adsr(f,0.01,0.03,0.3,0.02) * 0.35);
                case "hit"        -> synth(0.12, f -> noise()          * adsr(f,0.005,0.02,0.2,0.05) * 0.45);
                case "die"        -> synth(0.30, f -> (sine(f,220*Math.pow(0.3,f)) + noise()*0.2) * adsr(f,0.01,0.05,0.4,0.1) * 0.55);
                case "levelup"    -> synth(0.55, f -> arpChord(f)      * adsr(f,0.01,0.1,0.6,0.1) * 0.55);
                case "gameover"   -> synth(0.70, f -> sine(f,330 - f*120) * adsr(f,0.01,0.1,0.5,0.15) * 0.50);
                case "start"      -> synth(0.40, f -> (sine(f,440+f*110)+sine(f,660)) * adsr(f,0.01,0.05,0.55,0.1) * 0.40);
                default           -> null;
            };
        }

        // ── Waveform primitives ───────────────────────────────────────────
        private static double sine(double t, double freq) {
            return Math.sin(2 * Math.PI * freq * t);
        }
        private static double triangle(double t, double freq) {
            double phase = (t * freq) % 1.0;
            return phase < 0.5 ? 4*phase-1 : 3-4*phase;
        }
        static final Random NR = new Random();
        private static double noise() { return NR.nextDouble()*2-1; }

        /** Simple ADSR envelope: t=normalised 0..1, durations also normalised. */
        private static double adsr(double t, double a, double d, double s, double r) {
            if (t < a)           return t / a;
            if (t < a+d)         return 1.0 - (1.0-0.7)*((t-a)/d);
            if (t < 1.0-r)       return 0.7;
            return 0.7 * (1.0 - (t-(1.0-r))/r);
        }

        /** Ascending arpeggio chord for level-up jingle. */
        private static double arpChord(double t) {
            double[] freqs = {330, 415, 494, 659};
            int idx = Math.min((int)(t * freqs.length), freqs.length-1);
            return sine(t, freqs[idx]) + sine(t, freqs[idx]*2)*0.3;
        }

        /**
         * Synthesise PCM bytes.
         * @param dur     duration in seconds
         * @param fn      lambda: normalised time 0..1 → sample amplitude -1..1
         */
        private byte[] synth(double dur, java.util.function.DoubleUnaryOperator fn) {
            int n = (int)(SAMPLE_RATE * dur);
            byte[] buf = new byte[n * 2];
            for (int i = 0; i < n; i++) {
                double t = (double)i / n;
                int sample = (int)(fn.applyAsDouble(t) * 28000);
                sample = Math.max(-32768, Math.min(32767, sample));
                buf[2*i]   = (byte)(sample & 0xFF);
                buf[2*i+1] = (byte)((sample >> 8) & 0xFF);
            }
            return buf;
        }
    }

    // ── Main ──────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Eternia: The Crystal of Balance");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            EterniaGame game = new EterniaGame();
            f.add(game);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}

