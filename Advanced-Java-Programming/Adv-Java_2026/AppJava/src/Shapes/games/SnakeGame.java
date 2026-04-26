package Shapes.games;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SERPENTIS: The Neon Snake
 * ─────────────────────────────────────────────────────────────────────────────
 * Classic Snake reimagined with 4 levels, power-ups, obstacles, and effects.
 * 
 * Developed by Danny Idukundatwese
 * 
 * Student ID 2405001032
 *
 * HOW TO RUN:
 *   javac SnakeGame.java
 *   java Shapes.games.SnakeGame
 *
 * CONTROLS:
 *   Arrow Keys / WASD  – Move snake
 *   ENTER              – Start / Resume
 *   ESC                – Pause
 *   R                  – Restart
 *   M                  – Mute / Unmute
 *   Q                  – Quit to Menu
 *
 * LEVELS:
 *   1 – Open Field    : No walls, slow speed, just eat and grow
 *   2 – Stone Maze    : Static wall obstacles appear
 *   3 – Danger Zone   : Moving obstacles + faster speed
 *   4 – Chaos Mode    : Shrinking arena + all hazards
 *
 * POWER-UPS (appear randomly on the field):
 *   🟡 Golden Apple   – Worth 5 points, brief speed boost
 *   🔵 Frost Berry    – Slows snake down for 5 seconds
 *   🟣 Ghost Fruit    – Snake passes through walls for 5 seconds
 *   ❤  Extra Life     – Grants one extra life (rare)
 *   💣 Bomb           – AVOID! Kills instantly
 */
public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // ── Grid ──────────────────────────────────────────────────────────────
    static final int COLS      = 40;
    static final int ROWS      = 28;
    static final int TILE      = 28;
    static final int HUD_H     = 62;
    static final int W         = COLS * TILE;          // 1120
    static final int H         = ROWS * TILE + HUD_H;  // 846

    // ── Speed (ticks per move) ────────────────────────────────────────────
    static final int[] BASE_SPEED = { 0, 10, 8, 6, 5 }; // per level (index 0 unused)
    static final int FPS = 60;

    // ── Direction ─────────────────────────────────────────────────────────
    static final int UP=0, DOWN=1, LEFT=2, RIGHT=3;

    // ── Colours ───────────────────────────────────────────────────────────
    static final Color C_BG       = new Color(6,   4,  18);
    static final Color C_GRID     = new Color(255,255,255, 10);
    static final Color C_HEAD     = new Color(0,   230, 120);
    static final Color C_BODY     = new Color(0,   190, 100);
    static final Color C_BODY2    = new Color(0,   155,  80);
    static final Color C_EYE      = new Color(255, 255, 255);
    static final Color C_PUPIL    = new Color(10,  10,  20);
    static final Color C_FOOD     = new Color(255,  70,  70);
    static final Color C_FOOD_GLO = new Color(255, 100, 100);
    static final Color C_GOLD     = new Color(255, 210,  40);
    static final Color C_FROST    = new Color(100, 200, 255);
    static final Color C_GHOST    = new Color(190, 140, 255);
    static final Color C_LIFE     = new Color(255,  80, 130);
    static final Color C_BOMB     = new Color(50,   45,  55);
    static final Color C_WALL     = new Color(70,   60,  90);
    static final Color C_WALL_L   = new Color(100,  90, 120);
    static final Color C_HUD_BG   = new Color(5,    3,  16, 230);
    static final Color C_WHITE    = Color.WHITE;
    static final Color C_GREEN    = new Color(40,  220, 100);
    static final Color C_RED      = new Color(220,  50,  50);

    // ── Screens ───────────────────────────────────────────────────────────
    enum Screen { MENU, PLAYING, PAUSED, LEVEL_COMPLETE, GAME_OVER, WIN }
    Screen screen = Screen.MENU;

    // ── Snake ─────────────────────────────────────────────────────────────
    LinkedList<int[]> snake = new LinkedList<>(); // each: [col, row]
    int direction   = RIGHT;
    int nextDir     = RIGHT;

    // ── Food & Power-ups ──────────────────────────────────────────────────
    int[] food = new int[2];
    enum PowerType { GOLDEN, FROST, GHOST, LIFE, BOMB }
    static class PowerUp {
        int col, row; PowerType type; int ttl; // time-to-live in ticks
        PowerUp(int c, int r, PowerType t, int ttl){ col=c; row=r; type=t; this.ttl=ttl; }
    }
    List<PowerUp> powers = new ArrayList<>();

    // ── Obstacles ─────────────────────────────────────────────────────────
    Set<String>      walls    = new HashSet<>(); // "col,row"
    List<int[]>      mWalls   = new ArrayList<>(); // moving: [col,row,dc,dr]
    int              arenaMargin = 0; // shrinks in level 4

    // ── Active effects ────────────────────────────────────────────────────
    int  frostTimer  = 0;   // ticks remaining for frost slow
    int  ghostTimer  = 0;   // ticks remaining for ghost (pass through walls)
    int  speedBoost  = 0;   // ticks of speed boost (golden apple)

    // ── Game state ────────────────────────────────────────────────────────
    int  level   = 1;
    int  score   = 0;
    int  lives   = 3;
    int  highScore = 0;

    int  moveTick    = 0;  // counts up to speed threshold
    int  powerTick   = 0;  // spawn power-up timer
    int  mWallTick   = 0;  // moving wall update timer
    int  arenaTick   = 0;  // arena shrink timer

    // ── Particles ─────────────────────────────────────────────────────────
    List<Particle> particles = new CopyOnWriteArrayList<>();

    // ── Input buffer ──────────────────────────────────────────────────────
    // Store last 2 direction presses so fast turns aren't lost
    Deque<Integer> dirQueue = new ArrayDeque<>();

    long tick = 0;
    Random rng = new Random();
    Buzzer sound;
    javax.swing.Timer gameTimer;

    // ── Constructor ───────────────────────────────────────────────────────
    public SnakeGame() {
        setPreferredSize(new Dimension(W, H));
        setBackground(C_BG);
        setFocusable(true);
        addKeyListener(this);
        sound     = new Buzzer();
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    // ── Setup ─────────────────────────────────────────────────────────────
    void setup() {
        snake.clear();
        dirQueue.clear();
        walls.clear();
        mWalls.clear();
        powers.clear();
        particles.clear();
        frostTimer = ghostTimer = speedBoost = 0;
        moveTick = powerTick = mWallTick = arenaTick = 0;
        arenaMargin = 0;
        direction = nextDir = RIGHT;

        // Start snake in the middle
        int midC = COLS/2, midR = ROWS/2;
        snake.addFirst(new int[]{midC,   midR});
        snake.addFirst(new int[]{midC+1, midR});
        snake.addFirst(new int[]{midC+2, midR});

        buildWalls();
        placeFood();
        spawnPowerUp();
    }

    void buildWalls() {
        walls.clear();
        if (level == 2) {
            // Static cross/plus patterns
            addWallRect(5,  5,  4, 1);
            addWallRect(18, 5,  4, 1);
            addWallRect(5,  18, 4, 1);
            addWallRect(18, 18, 4, 1);
            addWallRect(12, 11, 1, 3);
        }
        if (level == 3) {
            addWallRect(4,  4,  3, 1);
            addWallRect(21, 4,  3, 1);
            addWallRect(4,  19, 3, 1);
            addWallRect(21, 19, 3, 1);
            addWallRect(12, 10, 4, 1);
            addWallRect(12, 13, 4, 1);
            // Add moving wall seeds
            mWalls.add(new int[]{8,  12, 1, 0});
            mWalls.add(new int[]{19, 12,-1, 0});
        }
        if (level == 4) {
            addWallRect(4,  4,  4, 1);
            addWallRect(20, 4,  4, 1);
            addWallRect(4,  19, 4, 1);
            addWallRect(20, 19, 4, 1);
            mWalls.add(new int[]{7,  8,  0,  1});
            mWalls.add(new int[]{20, 8,  0,  1});
            mWalls.add(new int[]{7,  16, 0, -1});
            mWalls.add(new int[]{20, 16, 0, -1});
        }
    }

    void addWallRect(int c, int r, int w, int h) {
        for (int dc=0;dc<w;dc++)
            for (int dr=0;dr<h;dr++)
                walls.add((c+dc)+","+(r+dr));
    }

    // ── Food & Power-up placement ─────────────────────────────────────────
    void placeFood() {
        int c, r;
        do {
            c = arenaMargin + rng.nextInt(COLS - arenaMargin*2);
            r = arenaMargin + rng.nextInt(ROWS - arenaMargin*2);
        } while (isOccupied(c,r));
        food[0]=c; food[1]=r;
    }

    void spawnPowerUp() {
        if (powers.size() >= 2) return;
        int c, r;
        int tries=0;
        do {
            c = arenaMargin + rng.nextInt(COLS - arenaMargin*2);
            r = arenaMargin + rng.nextInt(ROWS - arenaMargin*2);
            tries++;
        } while (isOccupied(c,r) && tries<50);
        if (tries>=50) return;

        // Weighted random power-up type
        int roll = rng.nextInt(100);
        PowerType t;
        if      (roll < 25) t = PowerType.GOLDEN;
        else if (roll < 45) t = PowerType.FROST;
        else if (roll < 60) t = PowerType.GHOST;
        else if (roll < 68) t = PowerType.LIFE;
        else                t = PowerType.BOMB;

        powers.add(new PowerUp(c, r, t, 300 + rng.nextInt(200)));
    }

    boolean isOccupied(int c, int r) {
        for (int[] s : snake)  if (s[0]==c && s[1]==r) return true;
        if (walls.contains(c+","+r)) return true;
        if (food[0]==c && food[1]==r) return true;
        for (PowerUp p : powers) if (p.col==c && p.row==r) return true;
        if (c < arenaMargin || c >= COLS-arenaMargin) return true;
        if (r < arenaMargin || r >= ROWS-arenaMargin) return true;
        return false;
    }

    // ── Game loop ─────────────────────────────────────────────────────────
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        if (screen == Screen.PLAYING) update();
        repaint();
    }

    void update() {
        // Effect timers
        if (frostTimer  > 0) frostTimer--;
        if (ghostTimer  > 0) ghostTimer--;
        if (speedBoost  > 0) speedBoost--;

        // Power-up TTL
        powers.removeIf(p -> { p.ttl--; return p.ttl<=0; });

        // Spawn power-up periodically
        powerTick++;
        if (powerTick >= 180) { powerTick=0; spawnPowerUp(); }

        // Moving walls
        if (level >= 3) {
            mWallTick++;
            if (mWallTick >= 30) {
                mWallTick=0;
                for (int[] mw : mWalls) {
                    int nc = mw[0]+mw[2], nr = mw[1]+mw[3];
                    // Bounce off edges and static walls
                    if (nc<1||nc>=COLS-1||walls.contains(nc+","+mw[1])) { mw[2]*=-1; nc=mw[0]+mw[2]; }
                    if (nr<1||nr>=ROWS-1||walls.contains(mw[0]+","+nr)) { mw[3]*=-1; nr=mw[1]+mw[3]; }
                    mw[0]=nc; mw[1]=nr;
                }
            }
        }

        // Arena shrink (level 4)
        if (level == 4) {
            arenaTick++;
            if (arenaTick >= 400 && arenaMargin < 5) {
                arenaTick=0;
                arenaMargin++;
                // Kill snake segments in the margin
                snake.removeIf(s -> s[0]<arenaMargin||s[0]>=COLS-arenaMargin
                                 || s[1]<arenaMargin||s[1]>=ROWS-arenaMargin);
            }
        }

        // Particles
        particles.removeIf(p -> { p.update(); return p.dead; });

        // Move snake at current speed
        int speed = BASE_SPEED[Math.min(level, 4)];
        if (frostTimer > 0)  speed = (int)(speed * 1.7);
        if (speedBoost > 0)  speed = Math.max(2, speed - 3);

        moveTick++;
        if (moveTick < speed) return;
        moveTick = 0;

        // Consume direction queue
        if (!dirQueue.isEmpty()) {
            int nd = dirQueue.poll();
            // Prevent 180-degree reversal
            if (!((nd==UP&&direction==DOWN)||(nd==DOWN&&direction==UP)
                ||(nd==LEFT&&direction==RIGHT)||(nd==RIGHT&&direction==LEFT)))
                direction = nd;
        }

        // Compute next head position
        int[] head = snake.peekFirst();
        int nc = head[0], nr = head[1];
        switch (direction) {
            case UP    -> nr--;
            case DOWN  -> nr++;
            case LEFT  -> nc--;
            case RIGHT -> nc++;
        }

        // Wall wrapping (always wrap on level 1; ghost power also wraps)
        boolean ghostMode = ghostTimer > 0 || level == 1;
        if (ghostMode) {
            if (nc < 0) nc = COLS-1; else if (nc >= COLS) nc = 0;
            if (nr < 0) nr = ROWS-1; else if (nr >= ROWS) nr = 0;
        }

        // Collision: border
        if (!ghostMode && (nc<0||nc>=COLS||nr<0||nr>=ROWS)) { die(); return; }
        // Arena shrink border
        if (nc<arenaMargin||nc>=COLS-arenaMargin||nr<arenaMargin||nr>=ROWS-arenaMargin) { die(); return; }
        // Static walls
        if (walls.contains(nc+","+nr)) { die(); return; }
        // Moving walls
        for (int[] mw : mWalls) if (mw[0]==nc && mw[1]==nr) { die(); return; }
        // Self collision
        for (int[] s : snake) if (s[0]==nc && s[1]==nr) { die(); return; }

        // Move: add new head
        snake.addFirst(new int[]{nc, nr});

        // Check food
        boolean ate = false;
        if (nc==food[0] && nr==food[1]) {
            score += 10;
            ate = true;
            sound.play("eat");
            spawnEatFx(nc, nr, C_FOOD);
            placeFood();
            // Level up every 50 points
            if (score > 0 && score % 50 == 0 && level < 4) {
                screen = Screen.LEVEL_COMPLETE;
                sound.play("levelup");
                return;
            }
        }
        if (!ate) snake.removeLast(); // no growth

        // Check power-ups
        Iterator<PowerUp> it = powers.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (p.col==nc && p.row==nr) {
                applyPower(p);
                it.remove();
            }
        }
    }

    void applyPower(PowerUp p) {
        switch (p.type) {
            case GOLDEN -> { score+=5; speedBoost=80; sound.play("power"); spawnEatFx(p.col,p.row,C_GOLD); }
            case FROST  -> { frostTimer=360; sound.play("power"); spawnEatFx(p.col,p.row,C_FROST); }
            case GHOST  -> { ghostTimer=360; sound.play("power"); spawnEatFx(p.col,p.row,C_GHOST); }
            case LIFE   -> { lives=Math.min(lives+1,5); sound.play("life"); spawnEatFx(p.col,p.row,C_LIFE); }
            case BOMB   -> { sound.play("bomb"); spawnDeathFx(p.col,p.row); die(); }
        }
    }

    void die() {
        sound.play("die");
        int[] h = snake.peekFirst();
        if (h!=null) spawnDeathFx(h[0], h[1]);
        lives--;
        if (lives <= 0) {
            if (score > highScore) highScore = score;
            screen = Screen.GAME_OVER;
        } else {
            setup();
            buildWalls();
        }
    }

    // ── Particles ─────────────────────────────────────────────────────────
    void spawnEatFx(int c, int r, Color col) {
        int px=c*TILE+TILE/2, py=r*TILE+TILE/2+HUD_H;
        for (int i=0;i<10;i++) {
            float vx=(float)(rng.nextGaussian()*3), vy=(float)(rng.nextGaussian()*3);
            particles.add(new Particle(px,py,col,vx,vy,25+rng.nextInt(15)));
        }
    }
    void spawnDeathFx(int c, int r) {
        int px=c*TILE+TILE/2, py=r*TILE+TILE/2+HUD_H;
        for (int i=0;i<20;i++) {
            double ang=rng.nextDouble()*Math.PI*2;
            float vx=(float)(Math.cos(ang)*4), vy=(float)(Math.sin(ang)*4);
            Color col=new Color(200+rng.nextInt(55),40+rng.nextInt(40),20);
            particles.add(new Particle(px,py,col,vx,vy,40+rng.nextInt(20)));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // PAINT
    // ══════════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g=(Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (screen) {
            case MENU           -> drawMenu(g);
            case PLAYING        -> drawGame(g);
            case PAUSED         -> { drawGame(g); drawOverlay(g); }
            case LEVEL_COMPLETE -> drawLevelComplete(g);
            case GAME_OVER      -> drawGameOver(g);
            case WIN            -> drawWin(g);
        }
    }

    // ── Menu ──────────────────────────────────────────────────────────────
    void drawMenu(Graphics2D g) {
        g.setColor(C_BG);
        g.fillRect(0,0,W,H);
        drawBgGrid(g);

        // Animated snake preview on menu
        drawMenuSnake(g);

        drawGlow(g, new Font("Courier New",Font.BOLD,46),"SERPENTIS",
                C_HEAD, new Color(0,80,40), W/2, H/2-155);
        drawGlow(g, new Font("Courier New",Font.BOLD,16),"THE NEON SNAKE",
                C_FROST, new Color(10,50,90), W/2, H/2-110);

        g.setFont(new Font("Courier New",Font.PLAIN,15));
        String[] info = {
            "ENTER  ─  Start Game",
            "ARROWS / WASD  ─  Move",
            "ESC  ─  Pause    R  ─  Restart",
            "M  ─  Mute/Unmute    Q  ─  Quit"
        };
        for (int i=0;i<info.length;i++)
            drawCentered(g, info[i], new Color(180,178,215), H/2-50+i*28);

        // Power-up legend
        g.setFont(new Font("Courier New",Font.BOLD,13));
        drawCentered(g,"POWER-UPS", C_GOLD, H/2+70);
        String[][] legend = {
            {"●","GOLDEN APPLE","5 pts + speed burst"},
            {"●","FROST BERRY","slow down"},
            {"●","GHOST FRUIT","pass through walls"},
            {"❤","EXTRA LIFE","one more chance"},
            {"💣","BOMB","instant death — avoid!"}
        };
        Color[] lcols = {C_GOLD, C_FROST, C_GHOST, C_LIFE, new Color(160,60,60)};
        for (int i=0;i<legend.length;i++) {
            int ly = H/2+94+i*24;
            g.setColor(lcols[i]);
            FontMetrics fm=g.getFontMetrics();
            int bx = W/2 - 160;
            g.drawString(legend[i][0], bx, ly);
            g.setColor(C_WHITE);
            g.drawString(legend[i][1], bx+20, ly);
            g.setColor(new Color(160,155,190));
            g.drawString("─ "+legend[i][2], bx+150, ly);
        }

        if (highScore > 0) {
            g.setFont(new Font("Courier New",Font.BOLD,14));
            drawCentered(g,"HIGH SCORE: "+highScore, C_GOLD, H-20);
        }
    }

    void drawMenuSnake(Graphics2D g) {
        // Animate a small snake wiggling on the menu
        int n=8;
        for (int i=n-1;i>=0;i--) {
            double t = tick*0.04 + i*0.45;
            int sx = W/2 - 100 + i*22;
            int sy = (int)(H/2 - 185 + Math.sin(t)*12);
            Color sc = (i==0) ? C_HEAD : (i%2==0 ? C_BODY : C_BODY2);
            if (ghostTimer>0) sc=new Color(sc.getRed(),sc.getGreen(),sc.getBlue(),160);
            g.setColor(sc);
            g.fillRoundRect(sx-9,sy-9,18,18,8,8);
            if (i==0) {
                g.setColor(C_EYE); g.fillOval(sx+2,sy-4,5,5);
                g.setColor(C_PUPIL); g.fillOval(sx+3,sy-3,3,3);
            }
        }
    }

    // ── Main game ─────────────────────────────────────────────────────────
    void drawGame(Graphics2D g) {
        // Background
        Color bgC = levelBg();
        g.setColor(bgC);
        g.fillRect(0,0,W,H);
        drawBgGrid(g);

        // Arena shrink border (level 4)
        if (arenaMargin > 0) {
            g.setColor(new Color(180,30,30,80));
            int m=arenaMargin*TILE;
            g.fillRect(0,HUD_H,W,m);
            g.fillRect(0,H-m,W,m);
            g.fillRect(0,HUD_H,m,H-HUD_H);
            g.fillRect(W-m,HUD_H,m,H-HUD_H);
            g.setColor(new Color(220,50,50,160));
            g.setStroke(new BasicStroke(2.5f));
            g.drawRect(m,HUD_H+m,W-m*2,H-HUD_H-m*2);
            g.setStroke(new BasicStroke(1));
        }

        // Static walls
        for (String wk : walls) {
            String[] sp=wk.split(",");
            int wc=Integer.parseInt(sp[0]), wr=Integer.parseInt(sp[1]);
            drawWallTile(g, wc*TILE, wr*TILE+HUD_H);
        }

        // Moving walls
        for (int[] mw : mWalls) {
            drawMovingWall(g, mw[0]*TILE, mw[1]*TILE+HUD_H);
        }

        // Food
        drawFood(g);

        // Power-ups
        for (PowerUp p : powers) drawPowerUp(g, p);

        // Particles
        for (Particle p : particles) drawParticle(g, p);

        // Snake
        drawSnake(g);

        // HUD
        drawHud(g);

        // Active effect overlays
        drawEffectOverlay(g);
    }

    Color levelBg() {
        return switch(level) {
            case 1 -> new Color(6,  4, 18);
            case 2 -> new Color(8,  5, 20);
            case 3 -> new Color(10, 5, 14);
            case 4 -> new Color(14, 4,  8);
            default-> C_BG;
        };
    }

    void drawBgGrid(Graphics2D g) {
        g.setColor(C_GRID);
        g.setStroke(new BasicStroke(0.5f));
        for (int c=0;c<=COLS;c++) g.drawLine(c*TILE,HUD_H,c*TILE,H);
        for (int r=0;r<=ROWS;r++) g.drawLine(0,r*TILE+HUD_H,W,r*TILE+HUD_H);
        g.setStroke(new BasicStroke(1));
    }

    void drawWallTile(Graphics2D g, int px, int py) {
        g.setColor(C_WALL);
        g.fillRoundRect(px+1,py+1,TILE-2,TILE-2,4,4);
        g.setColor(C_WALL_L);
        g.fillRoundRect(px+3,py+3,TILE-6,8,2,2);
        g.setColor(new Color(0,0,0,80));
        g.drawRoundRect(px+1,py+1,TILE-2,TILE-2,4,4);
    }

    void drawMovingWall(Graphics2D g, int px, int py) {
        // Pulsing red moving obstacle
        int pulse=(int)(Math.abs(Math.sin(tick*0.12))*20);
        g.setColor(new Color(180+pulse,30,30));
        g.fillRoundRect(px+1,py+1,TILE-2,TILE-2,6,6);
        g.setColor(new Color(220,80,80));
        g.fillRoundRect(px+3,py+3,TILE-6,8,3,3);
        g.setColor(new Color(255,160,160,80));
        g.drawRoundRect(px+1,py+1,TILE-2,TILE-2,6,6);
    }

    void drawFood(Graphics2D g) {
        int px=food[0]*TILE, py=food[1]*TILE+HUD_H;
        int cx=px+TILE/2, cy=py+TILE/2;
        int glow=(int)(Math.abs(Math.sin(tick*0.10))*25)+15;
        g.setColor(new Color(C_FOOD_GLO.getRed(),C_FOOD_GLO.getGreen(),C_FOOD_GLO.getBlue(),glow));
        g.fillOval(cx-13,cy-13,26,26);
        // Apple body
        g.setColor(C_FOOD);
        g.fillOval(cx-8,cy-8,16,16);
        g.setColor(new Color(255,130,130));
        g.fillOval(cx-6,cy-6,8,7);
        // Stem
        g.setColor(new Color(80,55,20));
        g.setStroke(new BasicStroke(2));
        g.drawLine(cx,cy-8,cx+2,cy-14);
        g.setStroke(new BasicStroke(1));
        // Leaf
        g.setColor(new Color(50,180,60));
        g.fillOval(cx+1,cy-14,6,4);
    }

    void drawPowerUp(Graphics2D g, PowerUp p) {
        int px=p.col*TILE, py=p.row*TILE+HUD_H;
        int cx=px+TILE/2, cy=py+TILE/2;
        int glow=(int)(Math.abs(Math.sin(tick*0.13))*25)+15;
        // Fade warning when TTL low
        float alpha = p.ttl < 80 ? (p.ttl%20<10 ? 0.5f : 1f) : 1f;
        Color col = switch(p.type) {
            case GOLDEN -> C_GOLD;
            case FROST  -> C_FROST;
            case GHOST  -> C_GHOST;
            case LIFE   -> C_LIFE;
            case BOMB   -> new Color(180,60,60);
        };
        int a=(int)(glow*alpha);
        g.setColor(new Color(col.getRed(),col.getGreen(),col.getBlue(),Math.max(0,Math.min(255,a))));
        g.fillOval(cx-13,cy-13,26,26);

        switch (p.type) {
            case GOLDEN -> {
                g.setColor(new Color((int)(C_GOLD.getRed()*alpha),(int)(C_GOLD.getGreen()*alpha),10));
                g.fillOval(cx-7,cy-7,14,14);
                g.setColor(new Color(255,255,180,(int)(200*alpha)));
                g.fillOval(cx-4,cy-5,6,6);
                // Star
                drawStar(g, cx, cy-1, 9, 5, 4, new Color(255,230,80,(int)(220*alpha)));
            }
            case FROST -> {
                g.setColor(new Color(40,120,200,(int)(220*alpha)));
                g.fillOval(cx-7,cy-7,14,14);
                // Snowflake arms
                g.setColor(new Color(200,235,255,(int)(200*alpha)));
                g.setStroke(new BasicStroke(1.5f));
                for (int arm=0;arm<6;arm++) {
                    double rad=Math.toRadians(arm*60);
                    g.drawLine(cx,cy,(int)(cx+Math.cos(rad)*8),(int)(cy+Math.sin(rad)*8));
                }
                g.setStroke(new BasicStroke(1));
            }
            case GHOST -> {
                g.setColor(new Color(140,80,220,(int)(220*alpha)));
                g.fillOval(cx-7,cy-7,14,14);
                g.setColor(new Color(220,200,255,(int)(180*alpha)));
                g.setFont(new Font("Serif",Font.PLAIN,12));
                FontMetrics fm=g.getFontMetrics();
                g.drawString("👻",cx-fm.stringWidth("👻")/2,cy+5);
            }
            case LIFE -> {
                g.setColor(new Color((int)(200*alpha),(int)(40*alpha),(int)(90*alpha)));
                drawHeart(g, cx, cy+3, 8, alpha);
            }
            case BOMB -> {
                g.setColor(new Color(30,25,40,(int)(230*alpha)));
                g.fillOval(cx-8,cy-7,16,14);
                g.setColor(new Color(60,55,70,(int)(200*alpha)));
                g.fillOval(cx-6,cy-5,8,7);
                g.setColor(new Color(80,55,20,(int)(200*alpha)));
                g.setStroke(new BasicStroke(2));
                g.drawLine(cx+3,cy-7,cx+6,cy-12);
                g.setStroke(new BasicStroke(1));
                // Fuse spark
                int sp=(int)(Math.abs(Math.sin(tick*0.2))*255);
                g.setColor(new Color(255,200,50,sp));
                g.fillOval(cx+5,cy-13,4,4);
            }
        }
    }

    void drawSnake(Graphics2D g) {
        List<int[]> seg = new ArrayList<>(snake);
        boolean ghost = ghostTimer > 0;
        boolean boost = speedBoost > 0;

        for (int i=seg.size()-1;i>=0;i--) {
            int[] s=seg.get(i);
            int px=s[0]*TILE+1, py=s[1]*TILE+1+HUD_H;
            int sz=TILE-2;
            boolean isHead=(i==0);

            Color base = isHead ? C_HEAD : (i%2==0 ? C_BODY : C_BODY2);
            if (ghost) base=new Color(base.getRed(),base.getGreen(),base.getBlue(),150);
            if (boost)  {
                int pulse=(int)(Math.abs(Math.sin(tick*0.25+i*0.3))*40);
                base=new Color(Math.min(255,base.getRed()+pulse),
                               Math.min(255,base.getGreen()+40+pulse),
                               Math.min(255,base.getBlue()+pulse));
            }
            if (frostTimer>0) base=blendCol(base,C_FROST,0.35f);

            g.setColor(base);
            g.fillRoundRect(px,py,sz,sz,8,8);

            // Highlight
            g.setColor(new Color(255,255,255,40));
            g.fillRoundRect(px+2,py+2,sz/2,sz/3,4,4);

            // Border
            g.setColor(new Color(0,0,0,60));
            g.drawRoundRect(px,py,sz,sz,8,8);

            // Head features
            if (isHead) {
                // Glow
                int ga=ghost?80:40;
                g.setColor(new Color(C_HEAD.getRed(),C_HEAD.getGreen(),C_HEAD.getBlue(),ga));
                g.fillRoundRect(px-3,py-3,sz+6,sz+6,10,10);

                // Eyes (offset by direction)
                int e1x=px+5, e1y=py+5, e2x=px+sz-10, e2y=py+5;
                switch(direction) {
                    case DOWN  -> { e1x=px+5; e1y=py+sz-10; e2x=px+sz-10; e2y=py+sz-10; }
                    case LEFT  -> { e1x=px+2;  e1y=py+4;     e2x=px+2;     e2y=py+sz-10;}
                    case RIGHT -> { e1x=px+sz-8;e1y=py+4;   e2x=px+sz-8;  e2y=py+sz-10;}
                }
                g.setColor(C_EYE);
                g.fillOval(e1x,e1y,6,6); g.fillOval(e2x,e2y,6,6);
                g.setColor(C_PUPIL);
                g.fillOval(e1x+1,e1y+1,4,4); g.fillOval(e2x+1,e2y+1,4,4);

                // Tongue flick
                if (tick%30 < 10) {
                    g.setColor(new Color(255,40,80));
                    g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    int tx=s[0]*TILE+TILE/2, ty=s[1]*TILE+TILE/2+HUD_H;
                    int tlen=10;
                    int t2x=tx, t2y=ty;
                    switch(direction) {
                        case UP    -> { t2y=ty-TILE/2-tlen; }
                        case DOWN  -> { t2y=ty+TILE/2+tlen; }
                        case LEFT  -> { t2x=tx-TILE/2-tlen; }
                        case RIGHT -> { t2x=tx+TILE/2+tlen; }
                    }
                    int mx=(tx+t2x)/2, my=(ty+t2y)/2;
                    g.drawLine(tx,ty,t2x,t2y);
                    // Fork
                    switch(direction) {
                        case UP,DOWN    -> { g.drawLine(t2x,t2y,t2x-4,t2y-4); g.drawLine(t2x,t2y,t2x+4,t2y-4); }
                        case LEFT,RIGHT -> { g.drawLine(t2x,t2y,t2x-4,t2y-4); g.drawLine(t2x,t2y,t2x-4,t2y+4); }
                    }
                    g.setStroke(new BasicStroke(1));
                }
            }
        }
    }

    void drawEffectOverlay(Graphics2D g) {
        // Ghost: purple vignette
        if (ghostTimer > 0) {
            int a = Math.min(60, (int)(60f*ghostTimer/360));
            g.setColor(new Color(130,60,220,a));
            g.fillRect(0,HUD_H,W,H-HUD_H);
        }
        // Frost: blue tint
        if (frostTimer > 0) {
            int a=Math.min(40,(int)(40f*frostTimer/360));
            g.setColor(new Color(60,140,220,a));
            g.fillRect(0,HUD_H,W,H-HUD_H);
        }
        // Boost: yellow flash
        if (speedBoost > 0 && tick%6<3) {
            g.setColor(new Color(255,220,0,12));
            g.fillRect(0,HUD_H,W,H-HUD_H);
        }
    }

    void drawHud(Graphics2D g) {
        g.setColor(C_HUD_BG);
        g.fillRect(0,0,W,HUD_H);
        g.setColor(new Color(0,200,120,80));
        g.setStroke(new BasicStroke(2));
        g.drawLine(0,HUD_H,W,HUD_H);
        g.setStroke(new BasicStroke(1));

        // Level name
        String[] levelNames={"","OPEN FIELD","STONE MAZE","DANGER ZONE","CHAOS MODE"};
        g.setFont(new Font("Courier New",Font.BOLD,17));
        g.setColor(C_GOLD);
        g.drawString("LV"+level+" "+levelNames[Math.min(level,4)], 12, 24);

        // Score
        g.setFont(new Font("Courier New",Font.BOLD,22));
        drawCentered(g, ""+score, C_WHITE, 28);

        // Hi score
        g.setFont(new Font("Courier New",Font.PLAIN,12));
        drawCentered(g, "HI "+highScore, new Color(180,175,215), 46);

        // Lives (heart icons)
        g.setFont(new Font("Courier New",Font.BOLD,13));
        g.setColor(new Color(200,50,90));
        g.drawString("LIVES:", W-160, 24);
        for (int i=0;i<5;i++) {
            boolean has=i<lives;
            drawHeart(g, W-90+i*18, 18, 8, has?1f:0.25f);
        }

        // Active effects bar
        int ex=12, ey=44;
        if (ghostTimer>0)  drawEffectBadge(g, "GHOST",  C_GHOST,  ex, ey, ghostTimer, 360); ex+=90;
        if (frostTimer>0)  drawEffectBadge(g, "FROST",  C_FROST,  ex, ey, frostTimer, 360); ex+=90;
        if (speedBoost>0)  drawEffectBadge(g, "BOOST",  C_GOLD,   ex, ey, speedBoost,  80);

        // Snake length
        g.setFont(new Font("Courier New",Font.PLAIN,12));
        g.setColor(new Color(140,135,175));
        g.drawString("LENGTH: "+snake.size(), W-160, 50);
    }

    void drawEffectBadge(Graphics2D g, String label, Color c, int x, int y, int timer, int max) {
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),50));
        g.fillRoundRect(x,y-11,80,14,4,4);
        g.setColor(c);
        g.fillRoundRect(x,y-11,(int)(80f*timer/max),14,4,4);
        g.setFont(new Font("Courier New",Font.BOLD,10));
        g.setColor(C_WHITE);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(label, x+40-fm.stringWidth(label)/2, y);
    }

    void drawParticle(Graphics2D g, Particle p) {
        int a=(int)(255f*p.life/p.maxLife);
        g.setColor(new Color(p.c.getRed(),p.c.getGreen(),p.c.getBlue(),Math.max(0,Math.min(255,a))));
        int sz=1+(int)(4f*p.life/p.maxLife);
        g.fillOval((int)p.x-sz/2,(int)p.y-sz/2,sz,sz);
    }

    // ── Overlay screens ───────────────────────────────────────────────────
    void drawOverlay(Graphics2D g) {
        g.setColor(new Color(0,0,0,185));
        g.fillRect(0,0,W,H);
        drawGlow(g, new Font("Courier New",Font.BOLD,52),"PAUSED",
                C_GOLD,new Color(90,65,0),W/2,H/2-60);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        drawCentered(g,"ENTER – Resume",  C_WHITE, H/2+10);
        drawCentered(g,"R     – Restart", C_WHITE, H/2+46);
        drawCentered(g,"Q     – Menu",    C_WHITE, H/2+82);
    }

    void drawLevelComplete(Graphics2D g) {
        drawGame(g);
        g.setColor(new Color(0,0,0,190));
        g.fillRect(0,0,W,H);
        drawGlow(g, new Font("Courier New",Font.BOLD,46),"LEVEL "+level+" CLEAR!",
                C_GREEN, new Color(10,80,20), W/2, H/2-70);
        g.setFont(new Font("Courier New",Font.BOLD,20));
        drawCentered(g,"Score: "+score,    C_WHITE,       H/2+10);
        drawCentered(g,"ENTER – Next Level",C_WHITE,      H/2+50);
        drawCentered(g,"Q – Main Menu",     new Color(180,175,215), H/2+85);
    }

    void drawGameOver(Graphics2D g) {
        g.setColor(C_BG); g.fillRect(0,0,W,H);
        drawBgGrid(g);
        drawGlow(g, new Font("Courier New",Font.BOLD,52),"GAME OVER",
                C_RED, new Color(90,10,10), W/2, H/2-80);
        g.setFont(new Font("Courier New",Font.BOLD,20));
        drawCentered(g,"Score: "+score,      C_WHITE,            H/2+0);
        if (score>=highScore && score>0)
            drawCentered(g,"NEW HIGH SCORE!",C_GOLD,            H/2+35);
        drawCentered(g,"R – Try Again",       C_WHITE,            H/2+70);
        drawCentered(g,"Q – Main Menu",       new Color(180,175,215), H/2+104);
    }

    void drawWin(Graphics2D g) {
        g.setColor(C_BG); g.fillRect(0,0,W,H);
        drawBgGrid(g);
        drawGlow(g, new Font("Courier New",Font.BOLD,42),"YOU WIN!",
                C_GOLD, new Color(100,75,0), W/2, H/2-90);
        g.setFont(new Font("Courier New",Font.PLAIN,20));
        drawCentered(g,"Final Score: "+score, C_WHITE, H/2+0);
        drawCentered(g,"All 4 levels conquered!", new Color(180,255,180), H/2+36);
        drawCentered(g,"R – Play Again    Q – Menu", C_WHITE, H/2+90);
    }

    // ── Drawing helpers ───────────────────────────────────────────────────
    void drawGlow(Graphics2D g, Font f, String text, Color bright, Color dark, int cx, int y) {
        g.setFont(f);
        FontMetrics fm=g.getFontMetrics(f);
        int tx=cx-fm.stringWidth(text)/2;
        for (int r=7;r>=1;r--) {
            g.setColor(new Color(bright.getRed(),bright.getGreen(),bright.getBlue(),
                        Math.min(255,8+r*16)));
            g.drawString(text,tx+r,y+r); g.drawString(text,tx-r,y-r);
            g.drawString(text,tx+r,y-r); g.drawString(text,tx-r,y+r);
        }
        g.setColor(dark);  g.drawString(text,tx+2,y+2);
        g.setColor(bright); g.drawString(text,tx,y);
    }

    void drawCentered(Graphics2D g, String text, Color c, int y) {
        g.setColor(c);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(text,(W-fm.stringWidth(text))/2,y);
    }

    void drawHeart(Graphics2D g, int cx, int cy, int size, float alpha) {
        int r=(int)(200*alpha), gr=(int)(40*alpha), b=(int)(80*alpha);
        g.setColor(new Color(Math.max(0,Math.min(255,r)),
                             Math.max(0,Math.min(255,gr)),
                             Math.max(0,Math.min(255,b))));
        GeneralPath p=new GeneralPath();
        p.moveTo(cx,cy+size*0.3f);
        p.curveTo(cx,cy-size*0.5f,cx-size,cy-size*0.5f,cx-size,cy);
        p.curveTo(cx-size,cy+size*0.6f,cx,cy+size,cx,cy+size);
        p.curveTo(cx,cy+size,cx+size,cy+size*0.6f,cx+size,cy);
        p.curveTo(cx+size,cy-size*0.5f,cx,cy-size*0.5f,cx,cy+size*0.3f);
        g.fill(p);
    }

    void drawStar(Graphics2D g, int cx, int cy, int outer, int inner, int points, Color c) {
        g.setColor(c);
        double step=Math.PI/points;
        int[] xs=new int[points*2], ys=new int[points*2];
        for (int i=0;i<points*2;i++) {
            double ang=-Math.PI/2+i*step;
            int r2=(i%2==0)?outer:inner;
            xs[i]=(int)(cx+Math.cos(ang)*r2);
            ys[i]=(int)(cy+Math.sin(ang)*r2);
        }
        g.fillPolygon(xs,ys,points*2);
    }

    Color blendCol(Color a, Color b, float t) {
        return new Color(
            (int)(a.getRed()*(1-t)+b.getRed()*t),
            (int)(a.getGreen()*(1-t)+b.getGreen()*t),
            (int)(a.getBlue()*(1-t)+b.getBlue()*t));
    }

    // ── Key input ─────────────────────────────────────────────────────────
    @Override public void keyPressed(KeyEvent e) {
        int k=e.getKeyCode();
        switch(screen) {
            case MENU -> {
                if (k==KeyEvent.VK_ENTER){ level=1; score=0; lives=3; setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_M)    sound.muted=!sound.muted;
                if (k==KeyEvent.VK_Q)    System.exit(0);
            }
            case PLAYING -> {
                if (k==KeyEvent.VK_UP   ||k==KeyEvent.VK_W){ if(dirQueue.size()<2) dirQueue.add(UP); }
                if (k==KeyEvent.VK_DOWN ||k==KeyEvent.VK_S){ if(dirQueue.size()<2) dirQueue.add(DOWN); }
                if (k==KeyEvent.VK_LEFT ||k==KeyEvent.VK_A){ if(dirQueue.size()<2) dirQueue.add(LEFT); }
                if (k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D){ if(dirQueue.size()<2) dirQueue.add(RIGHT); }
                if (k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if (k==KeyEvent.VK_R)    { score=0; lives=3; setup(); }
                if (k==KeyEvent.VK_M)    sound.muted=!sound.muted;
            }
            case PAUSED -> {
                if (k==KeyEvent.VK_ENTER) screen=Screen.PLAYING;
                if (k==KeyEvent.VK_R)    { score=0; lives=3; setup(); screen=Screen.PLAYING; }
                if (k==KeyEvent.VK_Q)     screen=Screen.MENU;
            }
            case LEVEL_COMPLETE -> {
                if (k==KeyEvent.VK_ENTER){
                    if (level>=4) { screen=Screen.WIN; return; }
                    level++; setup(); screen=Screen.PLAYING; sound.play("start");
                }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; score=0; lives=3; }
            }
            case GAME_OVER -> {
                if (k==KeyEvent.VK_R){ level=1; score=0; lives=3; setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; score=0; lives=3; }
            }
            case WIN -> {
                if (k==KeyEvent.VK_R){ level=1; score=0; lives=3; setup(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; score=0; lives=3; }
            }
        }
    }
    @Override public void keyReleased(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}

    // ══════════════════════════════════════════════════════════════════════
    // INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════

    static class Particle {
        float x,y,vx,vy; Color c; int life,maxLife; boolean dead;
        Particle(int x,int y,Color c,float vx,float vy,int life){
            this.x=x;this.y=y;this.c=c;this.vx=vx;this.vy=vy;this.life=this.maxLife=life;
        }
        void update(){x+=vx;y+=vy;vx*=0.90f;vy*=0.90f;if(--life<=0)dead=true;}
    }

    // ══════════════════════════════════════════════════════════════════════
    // SYNTHESIZED SOUND
    // ══════════════════════════════════════════════════════════════════════
    static class Buzzer {
        static final int SR=44100;
        boolean muted=false;

        void play(String name){
            if(muted)return;
            byte[] pcm=gen(name);
            if(pcm==null)return;
            Thread t=new Thread(()->{
                try{
                    AudioFormat fmt=new AudioFormat(SR,16,1,true,false);
                    DataLine.Info info=new DataLine.Info(SourceDataLine.class,fmt);
                    if(!AudioSystem.isLineSupported(info))return;
                    try(SourceDataLine line=(SourceDataLine)AudioSystem.getLine(info)){
                        line.open(fmt,2048);line.start();
                        line.write(pcm,0,pcm.length);line.drain();
                    }
                }catch(Exception ignored){}
            });
            t.setDaemon(true);t.start();
        }

        byte[] gen(String name){
            return switch(name){
                case "eat"     -> synth(0.07, f->sq(f,880)*adsr(f,.01,.02,.3,.02)*.35);
                case "power"   -> synth(0.25, f->(sine(f,440+f*220)+sine(f,660))*adsr(f,.01,.06,.5,.1)*.4);
                case "life"    -> synth(0.40, f->arpUp(f)*adsr(f,.01,.1,.6,.1)*.45);
                case "die"     -> synth(0.45, f->(sine(f,220*Math.pow(.3,f))+noise()*.2)*adsr(f,.01,.05,.4,.1)*.5);
                case "bomb"    -> synth(0.30, f->noise()*Math.exp(-f*8)*adsr(f,.005,.03,.5,.1)*.6);
                case "levelup" -> synth(0.55, f->(sine(f,523)+sine(f,659)+sine(f,784))*.25*adsr(f,.01,.1,.65,.1));
                case "start"   -> synth(0.30, f->(sine(f,330+f*80)+sine(f,550))*.3*adsr(f,.01,.05,.55,.1));
                default        -> null;
            };
        }

        static double sine(double t,double f){return Math.sin(2*Math.PI*f*t);}
        static double sq(double t,double f){return Math.signum(Math.sin(2*Math.PI*f*t));}
        static final Random NR=new Random();
        static double noise(){return NR.nextDouble()*2-1;}
        static double adsr(double t,double a,double d,double s,double r){
            if(t<a)return t/a;if(t<a+d)return 1-(1-.7)*((t-a)/d);
            if(t<1-r)return .7;return .7*(1-(t-(1-r))/r);
        }
        static double arpUp(double t){
            double[] fr={392,494,587,784};int i=Math.min((int)(t*fr.length),fr.length-1);
            return sine(t,fr[i])+sine(t,fr[i]*2)*.25;
        }
        byte[] synth(double dur,java.util.function.DoubleUnaryOperator fn){
            int n=(int)(SR*dur);byte[] b=new byte[n*2];
            for(int i=0;i<n;i++){
                double t=(double)i/n;
                int s=Math.max(-32768,Math.min(32767,(int)(fn.applyAsDouble(t)*28000)));
                b[2*i]=(byte)(s&0xFF);b[2*i+1]=(byte)((s>>8)&0xFF);
            }
            return b;
        }
    }

    // ── Main ─────────────────────────────────────────────────────────────
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            JFrame f=new JFrame("Serpentis: The Neon Snake");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            SnakeGame game=new SnakeGame();
            f.add(game);f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}