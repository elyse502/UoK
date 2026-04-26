/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shapes.games;

/**
 *
 * @author idtda
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Platformer Adventure — 7 Levels
 * Levels 1-4: Easy  |  Levels 5-6: Medium  |  Level 7: Hard Boss
 *
 * CONTROLS:
 *   LEFT / RIGHT arrows  — Move
 *   SPACE               — Jump
 *   R                   — Restart (from Game Over / Victory screen)
 */
public class PlatformerAdventure extends JPanel implements ActionListener, KeyListener {

    // ── Screen ────────────────────────────────────────────────────────────────
    static final int WIDTH = 800, HEIGHT = 600;

    // ── Physics ───────────────────────────────────────────────────────────────
    static final int    PLAYER_WIDTH  = 30;
    static final int    PLAYER_HEIGHT = 40;
    static final int    PLAYER_SPEED  = 5;
    static final int    PLAYER_JUMP   = 15;
    static final double GRAVITY           = 0.8;
    static final double TERMINAL_VELOCITY = 15.0;

    // ── Colors ────────────────────────────────────────────────────────────────
    static final Color WHITE    = Color.WHITE;
    static final Color BLACK    = Color.BLACK;
    static final Color RED      = Color.RED;
    static final Color GREEN    = new Color(60, 200, 80);
    static final Color YELLOW   = new Color(255, 220, 0);
    static final Color BROWN    = new Color(139, 90, 43);
    static final Color ORANGE   = new Color(255, 140, 0);
    static final Color PURPLE   = new Color(128, 0, 200);
    static final Color CYAN     = new Color(0, 200, 220);
    static final Color GOLD     = new Color(255, 200, 0);
    static final Color DARK_RED = new Color(160, 0, 0);

    // Per-level backgrounds
    static final Color[] BG_COLORS = {
        new Color(135, 206, 235),  // 1 – sky blue
        new Color(100, 100, 160),  // 2 – dusk
        new Color( 60,  90,  60),  // 3 – jungle night
        new Color( 80,  60, 100),  // 4 – cave
        new Color( 30,  30,  80),  // 5 – space
        new Color( 20,  20,  20),  // 6 – abyss
        new Color( 60,  10,  10),  // 7 – inferno
    };

    // Platform & accent colors per level
    static final Color[] PLAT_COLORS = {
        BROWN,
        new Color(100, 80, 140),
        new Color( 50, 110,  50),
        new Color( 60,  50,  80),
        new Color( 30,  30,  90),
        new Color( 40,  40,  40),
        new Color(120,  30,  10),
    };

    // ── Player state ──────────────────────────────────────────────────────────
    double  playerX = 50, playerY = HEIGHT - 100;
    double  velocityY = 0;
    int     playerHealth = 5;
    int     maxHealth    = 5;
    boolean isJumping    = false;
    boolean facingRight  = true;
    boolean isOnGround   = false;
    int     invincibleFrames = 0;  // brief i-frames after hit

    // ── Game state ────────────────────────────────────────────────────────────
    int     score    = 0;
    int     level    = 1;
    boolean gameOver = false;
    boolean victory  = false;
    boolean hasKey   = false;

    // ── Input ─────────────────────────────────────────────────────────────────
    boolean keyLeft = false, keyRight = false, keySpace = false;
    boolean keySpacePrev = false;

    // ── Level data ────────────────────────────────────────────────────────────
    int[][]     platforms;
    List<int[]> coins   = new ArrayList<>();
    List<int[]> spikes  = new ArrayList<>();
    List<int[]> enemies = new ArrayList<>();  // {x,y,w,h,speed,dir,minX,maxX,alive,type}
    int[]       exitDoor;                     // {x,y,w,h}
    int[]       keyPos;                       // {x,y} or null

    // ── Boss (level 7) ────────────────────────────────────────────────────────
    int   bossX = 600, bossY = 100, bossHP = 10, bossDir = -1;
    boolean bossActive = false;

    // ── Timer ─────────────────────────────────────────────────────────────────
    javax.swing.Timer gameTimer;
    Font uiFont = new Font("Arial", Font.BOLD, 20);

    // ─────────────────────────────────────────────────────────────────────────
    public PlatformerAdventure() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        loadLevel(1);
        gameTimer = new javax.swing.Timer(16, this);
        gameTimer.start();
    }

    // =========================================================================
    // LEVEL LOADER
    // =========================================================================
    void loadLevel(int lvl) {
        coins.clear(); spikes.clear(); enemies.clear();
        hasKey = false; bossActive = false;
        resetPlayerPos();

        switch (lvl) {

            // ── LEVEL 1 — Tutorial ────────────────────────────────────────────
            // Very wide platforms, few enemies, generous spacing
            case 1 -> {
                platforms = new int[][]{
                    {0,   HEIGHT-40,  WIDTH, 40},
                    {80,  HEIGHT-130, 220,   20},
                    {380, HEIGHT-200, 180,   20},
                    {620, HEIGHT-270, 180,   20},
                    {200, HEIGHT-300, 150,   20},
                    {0,   HEIGHT-380, 160,   20}
                };
                addCoins(new int[][]{{130,HEIGHT-160},{440,HEIGHT-230},{680,HEIGHT-300},{260,HEIGHT-330},{50,HEIGHT-410}});
                enemies.add(enemy(200, HEIGHT-140, 1, 1, 80, 300));
                spikes.add(new int[]{360, HEIGHT-40, 30, 20});
                exitDoor = new int[]{60, HEIGHT-430, 40, 50};
                keyPos   = new int[]{700, HEIGHT-300};
            }

            // ── LEVEL 2 — Stepping Stones ─────────────────────────────────────
            case 2 -> {
                platforms = new int[][]{
                    {0,   HEIGHT-40,  WIDTH, 40},
                    {100, HEIGHT-140, 180,   20},
                    {360, HEIGHT-220, 160,   20},
                    {580, HEIGHT-160, 180,   20},
                    {680, HEIGHT-270, 120,   20},
                    {400, HEIGHT-340, 130,   20},
                    {180, HEIGHT-380, 160,   20},
                    {0,   HEIGHT-320, 120,   20}
                };
                addCoins(new int[][]{{140,HEIGHT-170},{410,HEIGHT-250},{620,HEIGHT-190},{720,HEIGHT-300},{450,HEIGHT-370},{230,HEIGHT-410},{50,HEIGHT-350}});
                enemies.add(enemy(170, HEIGHT-160, 2, 1, 100, 280));
                enemies.add(enemy(420, HEIGHT-240, 2, 1, 360, 520));
                spikes.add(new int[]{300, HEIGHT-40, 50, 20});
                spikes.add(new int[]{550, HEIGHT-40, 40, 20});
                exitDoor = new int[]{50, HEIGHT-370, 40, 50};
                keyPos   = new int[]{430, HEIGHT-390};
            }

            // ── LEVEL 3 — Jungle ──────────────────────────────────────────────
            case 3 -> {
                platforms = new int[][]{
                    {0,   HEIGHT-40,  WIDTH, 40},
                    {0,   HEIGHT-130, 160,   20},
                    {220, HEIGHT-200, 140,   20},
                    {420, HEIGHT-270, 160,   20},
                    {640, HEIGHT-200, 160,   20},
                    {500, HEIGHT-350, 130,   20},
                    {260, HEIGHT-390, 160,   20},
                    {60,  HEIGHT-450, 140,   20}
                };
                addCoins(new int[][]{{60,HEIGHT-160},{280,HEIGHT-230},{480,HEIGHT-300},{690,HEIGHT-230},{550,HEIGHT-380},{300,HEIGHT-420},{90,HEIGHT-480}});
                enemies.add(enemy( 60, HEIGHT-150, 2, 1,  0, 160));
                enemies.add(enemy(250, HEIGHT-220, 2, 1, 220, 360));
                enemies.add(enemy(650, HEIGHT-220, 3, 1, 640, 800));
                spikes.add(new int[]{180, HEIGHT-40,  50, 20});
                spikes.add(new int[]{380, HEIGHT-40,  50, 20});
                spikes.add(new int[]{600, HEIGHT-40,  40, 20});
                exitDoor = new int[]{70, HEIGHT-500, 40, 50};
                keyPos   = new int[]{510, HEIGHT-390};
            }

            // ── LEVEL 4 — Caves ───────────────────────────────────────────────
            case 4 -> {
                platforms = new int[][]{
                    {0,   HEIGHT-40,  WIDTH, 40},
                    {0,   HEIGHT-140, 120,   20},
                    {160, HEIGHT-220, 120,   20},
                    {330, HEIGHT-160, 130,   20},
                    {510, HEIGHT-240, 120,   20},
                    {670, HEIGHT-160, 130,   20},
                    {580, HEIGHT-330, 120,   20},
                    {390, HEIGHT-390, 130,   20},
                    {200, HEIGHT-460, 130,   20},
                    {30,  HEIGHT-530, 130,   20}
                };
                addCoins(new int[][]{{40,HEIGHT-170},{200,HEIGHT-250},{380,HEIGHT-190},{550,HEIGHT-270},{710,HEIGHT-190},{620,HEIGHT-360},{430,HEIGHT-420},{240,HEIGHT-490},{60,HEIGHT-560}});
                enemies.add(enemy( 40, HEIGHT-160, 2, 1,   0, 120));
                enemies.add(enemy(200, HEIGHT-240, 3, 1, 160, 280));
                enemies.add(enemy(380, HEIGHT-180, 3, 1, 330, 460));
                enemies.add(enemy(680, HEIGHT-180, 3, 1, 670, 800));
                spikes.add(new int[]{130,  HEIGHT-40,  50, 20});
                spikes.add(new int[]{470,  HEIGHT-40,  50, 20});
                spikes.add(new int[]{350, HEIGHT-390, 30, 20});
                exitDoor = new int[]{40, HEIGHT-580, 40, 50};
                keyPos   = new int[]{410, HEIGHT-440};
            }

            // ── LEVEL 5 — Space Station (MEDIUM) ──────────────────────────────
            case 5 -> {
                platforms = new int[][]{
                    {0,    HEIGHT-40,  WIDTH, 40},
                    {0,    HEIGHT-150, 100,   15},
                    {160,  HEIGHT-220, 100,   15},
                    {300,  HEIGHT-300, 90,    15},
                    {440,  HEIGHT-220, 100,   15},
                    {580,  HEIGHT-300, 100,   15},
                    {680,  HEIGHT-380, 100,   15},
                    {500,  HEIGHT-440, 100,   15},
                    {330,  HEIGHT-500, 100,   15},
                    {160,  HEIGHT-440, 100,   15},
                    {40,   HEIGHT-510, 100,   15}
                };
                addCoins(new int[][]{{30,HEIGHT-180},{190,HEIGHT-250},{330,HEIGHT-330},{470,HEIGHT-250},{610,HEIGHT-330},{710,HEIGHT-410},{530,HEIGHT-470},{360,HEIGHT-530},{190,HEIGHT-470},{60,HEIGHT-540}});
                enemies.add(enemy(  0, HEIGHT-165, 3, 1,   0, 100));
                enemies.add(enemy(175, HEIGHT-235, 3, 1, 160, 260));
                enemies.add(enemy(310, HEIGHT-315, 3, 1, 300, 390));
                enemies.add(enemy(680, HEIGHT-395, 3,-1, 680, 780));
                enemies.add(enemy(510, HEIGHT-455, 4, 1, 500, 600));
                spikes.add(new int[]{ 110, HEIGHT-40,  50, 20});
                spikes.add(new int[]{ 260, HEIGHT-40,  50, 20});
                spikes.add(new int[]{ 400, HEIGHT-300, 30, 20});
                spikes.add(new int[]{ 600, HEIGHT-40,  50, 20});
                exitDoor = new int[]{45, HEIGHT-560, 40, 50};
                keyPos   = new int[]{345, HEIGHT-550};
            }

            // ── LEVEL 6 — Abyss (HARD) ────────────────────────────────────────
            case 6 -> {
                platforms = new int[][]{
                    {0,    HEIGHT-40,  100,   15},
                    {160,  HEIGHT-100, 80,    15},
                    {290,  HEIGHT-175, 80,    15},
                    {420,  HEIGHT-100, 80,    15},
                    {550,  HEIGHT-175, 80,    15},
                    {680,  HEIGHT-100, 120,   15},
                    {620,  HEIGHT-250, 80,    15},
                    {470,  HEIGHT-320, 80,    15},
                    {310,  HEIGHT-390, 80,    15},
                    {150,  HEIGHT-320, 80,    15},
                    {30,   HEIGHT-400, 80,    15},
                    {160,  HEIGHT-470, 80,    15},
                    {310,  HEIGHT-540, 100,   15}
                };
                addCoins(new int[][]{{180,HEIGHT-130},{310,HEIGHT-205},{450,HEIGHT-130},{580,HEIGHT-205},{700,HEIGHT-130},{650,HEIGHT-280},{490,HEIGHT-350},{330,HEIGHT-420},{170,HEIGHT-350},{50,HEIGHT-430},{180,HEIGHT-500},{330,HEIGHT-570}});
                enemies.add(enemy(170, HEIGHT-115, 3, 1, 160, 240));
                enemies.add(enemy(440, HEIGHT-115, 3, 1, 420, 500));
                enemies.add(enemy(620, HEIGHT-265, 4, 1, 620, 700));
                enemies.add(enemy(470, HEIGHT-335, 4, 1, 470, 550));
                enemies.add(enemy(150, HEIGHT-335, 4, 1, 150, 230));
                enemies.add(enemy(160, HEIGHT-485, 3, 1, 160, 240));
                spikes.add(new int[]{100, HEIGHT-40, 60, 20});
                spikes.add(new int[]{370, HEIGHT-40, 60, 20});
                spikes.add(new int[]{600, HEIGHT-40, 80, 20});
                spikes.add(new int[]{295, HEIGHT-175, 25, 20});
                spikes.add(new int[]{470, HEIGHT-320, 30, 20});
                exitDoor = new int[]{320, HEIGHT-590, 40, 50};
                keyPos   = new int[]{170, HEIGHT-520};
            }

            // ── LEVEL 7 — Boss (HARD) ─────────────────────────────────────────
            case 7 -> {
                platforms = new int[][]{
                    {0,   HEIGHT-40,  WIDTH, 40},
                    {0,   HEIGHT-200, 150,   20},
                    {650, HEIGHT-200, 150,   20},
                    {300, HEIGHT-280, 200,   20},
                    {100, HEIGHT-400, 180,   20},
                    {520, HEIGHT-400, 180,   20},
                    {300, HEIGHT-520, 200,   20}
                };
                addCoins(new int[][]{{50,HEIGHT-230},{680,HEIGHT-230},{360,HEIGHT-310},{150,HEIGHT-430},{570,HEIGHT-430},{360,HEIGHT-550}});
                spikes.add(new int[]{ 200, HEIGHT-40, 100, 20});
                spikes.add(new int[]{ 500, HEIGHT-40, 100, 20});
                bossActive = true;
                bossX = 360; bossY = HEIGHT - 420; bossHP = 10;
                exitDoor = new int[]{370, HEIGHT-570, 40, 50};
                keyPos   = null; // unlocked by defeating boss
            }
        }
    }

    // Helper: build enemy array {x,y,30,20, speed, dir, minX, maxX, alive=1, type=0}
    int[] enemy(int x, int y, int speed, int dir, int minX, int maxX) {
        return new int[]{x, y, 30, 20, speed, dir, minX, maxX, 1, 0};
    }

    void addCoins(int[][] pts) {
        for (int[] p : pts) coins.add(p.clone());
    }

    // =========================================================================
    // GAME LOOP
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !victory) update();
        repaint();
    }

    void update() {
        // Horizontal movement
        if (keyLeft)  { playerX -= PLAYER_SPEED; facingRight = false; }
        if (keyRight) { playerX += PLAYER_SPEED; facingRight = true;  }

        // Jump (edge-triggered)
        if (keySpace && !keySpacePrev && !isJumping) {
            isJumping = true;
            velocityY = -PLAYER_JUMP;
        }
        keySpacePrev = keySpace;

        // Gravity
        velocityY += GRAVITY;
        if (velocityY > TERMINAL_VELOCITY) velocityY = TERMINAL_VELOCITY;
        playerY += velocityY;

        // Screen bounds
        if (playerX < 0) playerX = 0;
        if (playerX > WIDTH - PLAYER_WIDTH) playerX = WIDTH - PLAYER_WIDTH;

        // Platform collisions
        isOnGround = false;
        for (int[] p : platforms) {
            double pb = playerY + PLAYER_HEIGHT, pr = playerX + PLAYER_WIDTH;
            if (velocityY >= 0 && pb >= p[1] && pb <= p[1]+p[3]+2 && pr > p[0] && playerX < p[0]+p[2]) {
                playerY = p[1] - PLAYER_HEIGHT; velocityY = 0; isOnGround = true; isJumping = false;
            } else if (velocityY < 0 && playerY <= p[1]+p[3] && pb >= p[1] && pr > p[0] && playerX < p[0]+p[2]) {
                playerY = p[1]+p[3]; velocityY = 0;
            } else if (pb > p[1] && playerY < p[1]+p[3]) {
                if (pr > p[0] && playerX < p[0]) playerX = p[0] - PLAYER_WIDTH;
                else if (playerX < p[0]+p[2] && pr > p[0]+p[2]) playerX = p[0]+p[2];
            }
        }

        // Fall off world
        if (playerY > HEIGHT) {
            takeDamage(1); resetPlayerPos(); return;
        }

        // Invincibility frames
        if (invincibleFrames > 0) invincibleFrames--;

        // Coins
        Iterator<int[]> ci = coins.iterator();
        while (ci.hasNext()) {
            int[] c = ci.next();
            if (Math.abs((playerX + PLAYER_WIDTH/2) - c[0]) < 20 && Math.abs((playerY + PLAYER_HEIGHT/2) - c[1]) < 20) {
                score += 10; ci.remove();
            }
        }

        // Key pickup
        if (!hasKey && keyPos != null) {
            if (Math.abs((playerX + PLAYER_WIDTH/2) - keyPos[0]) < 20 && Math.abs((playerY + PLAYER_HEIGHT/2) - keyPos[1]) < 20) {
                hasKey = true;
            }
        }

        // Boss level 7: boss defeated = key available / door open
        if (level == 7 && bossActive) {
            updateBoss();
            if (bossHP <= 0) {
                bossActive = false;
                hasKey = true;  // door unlocks
            }
        }

        // Enemies
        for (int[] en : enemies) {
            if (en[8] == 0) continue;
            en[0] += en[4] * en[5];
            if (en[0] <= en[6] || en[0]+en[2] >= en[7]) en[5] *= -1;

            if (invincibleFrames == 0 &&
                playerX+PLAYER_WIDTH > en[0] && playerX < en[0]+en[2] &&
                playerY+PLAYER_HEIGHT > en[1] && playerY < en[1]+en[3]) {
                if (velocityY > 0 && playerY + PLAYER_HEIGHT < en[1]+en[3]/2+5) {
                    en[8] = 0; velocityY = -10; score += 20;
                } else {
                    takeDamage(1);
                    playerX += facingRight ? -50 : 50;
                    velocityY = -8;
                }
            }
        }

        // Spikes
        if (invincibleFrames == 0) {
            for (int[] s : spikes) {
                if (playerX+PLAYER_WIDTH > s[0] && playerX < s[0]+s[2] &&
                    playerY+PLAYER_HEIGHT > s[1] && playerY < s[1]+s[3]) {
                    takeDamage(1); resetPlayerPos(); return;
                }
            }
        }

        // Exit door
        if (hasKey) {
            int ex = exitDoor[0], ey = exitDoor[1], ew = exitDoor[2], eh = exitDoor[3];
            if (playerX+PLAYER_WIDTH > ex && playerX < ex+ew &&
                playerY+PLAYER_HEIGHT > ey && playerY < ey+eh) {
                if (level < 7) { level++; loadLevel(level); }
                else           { victory = true; }
            }
        }
    }

    void takeDamage(int dmg) {
        if (invincibleFrames > 0) return;
        playerHealth -= dmg;
        invincibleFrames = 60;
        if (playerHealth <= 0) gameOver = true;
    }

    void updateBoss() {
        // Boss moves horizontally across the arena
        bossX += 3 * bossDir;
        if (bossX < 100 || bossX > WIDTH - 100) bossDir *= -1;

        // Boss drops (gravity-like pinned to platform height area)
        bossY = HEIGHT - 420;

        // Boss hits player
        if (invincibleFrames == 0 &&
            playerX+PLAYER_WIDTH > bossX && playerX < bossX+60 &&
            playerY+PLAYER_HEIGHT > bossY && playerY < bossY+80) {
            if (velocityY > 0 && playerY+PLAYER_HEIGHT < bossY+30) {
                bossHP--;
                velocityY = -12;
                score += 50;
            } else {
                takeDamage(1);
                playerX += facingRight ? -60 : 60;
                velocityY = -10;
            }
        }
    }

    void resetPlayerPos() {
        playerX = 50; playerY = HEIGHT - 100; velocityY = 0; isJumping = false;
    }

    // =========================================================================
    // RENDERING
    // =========================================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameOver || victory) { drawEndScreen(g2); return; }

        // Background
        g2.setColor(BG_COLORS[level-1]);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        // Decorative stars for space/dark levels
        if (level >= 5) drawStars(g2);

        // Platforms
        g2.setColor(PLAT_COLORS[level-1]);
        for (int[] p : platforms) {
            g2.fillRect(p[0], p[1], p[2], p[3]);
            g2.setColor(PLAT_COLORS[level-1].brighter());
            g2.fillRect(p[0], p[1], p[2], 4);  // highlight top edge
            g2.setColor(PLAT_COLORS[level-1]);
        }

        // Spikes
        g2.setColor(new Color(200, 60, 60));
        for (int[] s : spikes) {
            int sx = s[0], sy = s[1], sw = s[2];
            for (int i = 0; i < sw / 10; i++) {
                int bx = sx + i*10;
                g2.fillPolygon(new int[]{bx, bx+5, bx+10}, new int[]{sy+20, sy, sy+20}, 3);
            }
        }

        // Coins
        for (int[] c : coins) {
            g2.setColor(GOLD);
            g2.fillOval(c[0]-10, c[1]-10, 20, 20);
            g2.setColor(ORANGE);
            g2.fillOval(c[0]-6, c[1]-6, 12, 12);
            g2.setColor(YELLOW);
            g2.fillOval(c[0]-3, c[1]-3, 6, 6);
        }

        // Key
        if (!hasKey && keyPos != null) {
            g2.setColor(GOLD);
            g2.fillOval(keyPos[0]-8, keyPos[1]-8, 16, 16);
            g2.setColor(YELLOW);
            g2.fillRect(keyPos[0], keyPos[1]-2, 15, 5);
            g2.fillRect(keyPos[0]+10, keyPos[1]+3, 5, 4);
        }

        // Exit door
        drawDoor(g2);

        // Enemies
        for (int[] en : enemies) {
            if (en[8] == 0) continue;
            // Body
            g2.setColor(DARK_RED);
            g2.fillRoundRect(en[0], en[1], en[2], en[3], 6, 6);
            // Eyes
            int eyeX = en[5] > 0 ? en[0]+en[2]-8 : en[0]+3;
            g2.setColor(WHITE); g2.fillOval(eyeX, en[1]+2, 9, 9);
            g2.setColor(RED);   g2.fillOval(eyeX+2, en[1]+4, 5, 5);
        }

        // Boss
        if (level == 7 && bossActive) drawBoss(g2);

        // Player
        drawPlayer(g2);

        // HUD
        drawHUD(g2);
    }

    void drawDoor(Graphics2D g) {
        int ex = exitDoor[0], ey = exitDoor[1], ew = exitDoor[2], eh = exitDoor[3];
        g.setColor(hasKey ? new Color(80,200,80) : new Color(120,60,20));
        g.fillRect(ex, ey, ew, eh);
        g.setColor(BLACK);
        g.fillRect(ex+4, ey+4, ew-8, eh-12);
        // Lock / open indicator
        if (!hasKey) {
            g.setColor(GOLD);
            g.fillOval(ex+ew/2-5, ey+eh/2-5, 10, 10);
            g.fillRect(ex+ew/2-3, ey+eh/2, 6, 8);
        } else {
            g.setColor(GREEN);
            int[] xs = {ex+ew/2-6, ex+ew/2-2, ex+ew/2+6};
            int[] ys = {ey+eh/2,   ey+eh/2+6, ey+eh/2-6};
            g.fillPolygon(xs, ys, 3);
        }
    }

    void drawPlayer(Graphics2D g) {
        int px = (int)playerX, py = (int)playerY;
        // Flash when invincible
        if (invincibleFrames > 0 && (invincibleFrames / 5) % 2 == 0) return;

        // Body
        g.setColor(new Color(50, 180, 255));
        g.fillRoundRect(px+4, py+12, PLAYER_WIDTH-8, PLAYER_HEIGHT-12, 6, 6);
        // Head
        g.setColor(new Color(255, 220, 170));
        g.fillOval(px+5, py, PLAYER_WIDTH-10, 22);
        // Eye
        int eyeX = facingRight ? px+PLAYER_WIDTH-10 : px+4;
        g.setColor(WHITE); g.fillOval(eyeX, py+5, 8, 8);
        g.setColor(BLACK); g.fillOval(eyeX+2, py+7, 4, 4);
    }

    void drawBoss(Graphics2D g) {
        // Boss body
        g.setColor(new Color(150, 0, 0));
        g.fillRoundRect(bossX, bossY, 60, 80, 10, 10);
        // Eyes
        g.setColor(YELLOW); g.fillOval(bossX+8, bossY+15, 16, 16);
        g.setColor(YELLOW); g.fillOval(bossX+36, bossY+15, 16, 16);
        g.setColor(RED);    g.fillOval(bossX+12, bossY+19, 8, 8);
        g.setColor(RED);    g.fillOval(bossX+40, bossY+19, 8, 8);
        // Mouth (jagged)
        g.setColor(BLACK); g.fillRect(bossX+10, bossY+50, 40, 14);
        g.setColor(WHITE);
        for (int i = 0; i < 4; i++) {
            g.fillPolygon(new int[]{bossX+10+i*10, bossX+15+i*10, bossX+20+i*10},
                          new int[]{bossY+50, bossY+44, bossY+50}, 3);
        }
        // HP bar
        g.setColor(new Color(80,0,0)); g.fillRect(bossX, bossY-16, 60, 10);
        g.setColor(RED);               g.fillRect(bossX, bossY-16, (int)(60.0*bossHP/10), 10);
        g.setColor(WHITE); g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString("BOSS", bossX+15, bossY-18);
    }

    void drawStars(Graphics2D g) {
        g.setColor(new Color(255,255,255,140));
        int[] xs = {30,90,160,240,340,430,530,620,720,770,55,180,310,470,600,740,20,140,290,450,590,710,80,200,380,510,680};
        int[] ys = {30,80,50,120,20,90,40,70,30,100,180,160,190,150,170,200,250,280,260,300,240,270,350,370,330,360,340};
        for (int i = 0; i < xs.length; i++) {
            int sz = (i % 3 == 0) ? 3 : 2;
            g.fillOval(xs[i], ys[i], sz, sz);
        }
    }

    void drawHUD(Graphics2D g) {
        // Semi-transparent HUD bar
        g.setColor(new Color(0,0,0,140));
        g.fillRect(0, 0, WIDTH, 50);

        // Score
        g.setFont(uiFont); g.setColor(WHITE);
        g.drawString("Score: " + score, 10, 34);

        // Health hearts
        for (int i = 0; i < maxHealth; i++) {
            g.setColor(i < playerHealth ? RED : new Color(80,0,0));
            drawHeart(g, 150 + i * 28, 14, 20);
        }

        // Level
        String lvlLabel = "Level " + level + "/7: " + getLevelName();
        FontMetrics fm = g.getFontMetrics();
        g.setColor(GOLD);
        g.drawString(lvlLabel, WIDTH/2 - fm.stringWidth(lvlLabel)/2, 34);

        // Key indicator
        if (hasKey) {
            g.setColor(YELLOW);
            g.fillOval(WIDTH-60, 16, 14, 14);
            g.fillRect(WIDTH-47, 20, 12, 4);
            g.setColor(WHITE); g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("KEY", WIDTH-70, 44);
        }
    }

    void drawHeart(Graphics2D g, int x, int y, int size) {
        g.fillOval(x, y, size/2, size/2);
        g.fillOval(x+size/2, y, size/2, size/2);
        int[] hx = {x, x+size, x+size/2};
        int[] hy = {y+size/4, y+size/4, y+size};
        g.fillPolygon(hx, hy, 3);
    }

    void drawEndScreen(Graphics2D g) {
        g.setColor(new Color(0,0,0,220));
        g.fillRect(0,0,WIDTH,HEIGHT);

        if (victory) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String msg = "YOU WIN!";
            FontMetrics fm = g.getFontMetrics();
            g.setColor(GOLD);
            g.drawString(msg, WIDTH/2 - fm.stringWidth(msg)/2, HEIGHT/2 - 60);

            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.setColor(WHITE);
            String s = "Final Score: " + score;
            fm = g.getFontMetrics();
            g.drawString(s, WIDTH/2 - fm.stringWidth(s)/2, HEIGHT/2);

            g.setFont(new Font("Arial", Font.PLAIN, 22));
            g.setColor(new Color(180,180,180));
            String r = "Press R to play again";
            fm = g.getFontMetrics();
            g.drawString(r, WIDTH/2 - fm.stringWidth(r)/2, HEIGHT/2 + 50);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 52));
            String msg = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            g.setColor(RED);
            g.drawString(msg, WIDTH/2 - fm.stringWidth(msg)/2, HEIGHT/2 - 60);

            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.setColor(WHITE);
            String s = "Score: " + score + "  |  Reached Level " + level;
            fm = g.getFontMetrics();
            g.drawString(s, WIDTH/2 - fm.stringWidth(s)/2, HEIGHT/2);

            g.setFont(new Font("Arial", Font.PLAIN, 22));
            g.setColor(new Color(180,180,180));
            String r = "Press R to restart";
            fm = g.getFontMetrics();
            g.drawString(r, WIDTH/2 - fm.stringWidth(r)/2, HEIGHT/2 + 50);
        }
    }

    String getLevelName() {
        return switch (level) {
            case 1 -> "Tutorial Meadow";
            case 2 -> "Stepping Stones";
            case 3 -> "Jungle Canopy";
            case 4 -> "Crystal Caves";
            case 5 -> "Space Station";
            case 6 -> "The Abyss";
            case 7 -> "Boss Lair";
            default -> "???";
        };
    }

    // =========================================================================
    // KEY HANDLING
    // =========================================================================
    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT  -> keyLeft  = true;
            case KeyEvent.VK_RIGHT -> keyRight = true;
            case KeyEvent.VK_SPACE -> keySpace = true;
            case KeyEvent.VK_R -> {
                if (gameOver || victory) {
                    playerX = 50; playerY = HEIGHT-100; velocityY = 0;
                    playerHealth = maxHealth; score = 0; level = 1;
                    gameOver = false; victory = false; hasKey = false;
                    loadLevel(1);
                }
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT  -> keyLeft  = false;
            case KeyEvent.VK_RIGHT -> keyRight = false;
            case KeyEvent.VK_SPACE -> keySpace = false;
        }
    }
    @Override public void keyTyped(KeyEvent e) {}

    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Platformer Adventure — 7 Levels");
            PlatformerAdventure game = new PlatformerAdventure();
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}