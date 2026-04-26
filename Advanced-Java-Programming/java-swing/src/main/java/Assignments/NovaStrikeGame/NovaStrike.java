/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Assignments.NovaStrikeGame;

/**
 *
 * @author Elysee NIYIBIZI
 * @Reg No 2305000921
 * @Department Computer Science
 */

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.DoubleUnaryOperator;
import java.io.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║          NOVA STRIKE — Galactic Siege                           ║
 * ║    Advanced Java Programming — UoK                              ║
 * ║    Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert                  ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  CONTROLS:                                                       ║
 * ║   Arrow Keys / WASD  — Move                                     ║
 * ║   SPACE              — Shoot                                     ║
 * ║   X / Shift          — Special Weapon (costs energy)            ║
 * ║   P / ESC            — Pause                                     ║
 * ║   M                  — Mute / Unmute                            ║
 * ║   R                  — Restart (Game Over screen)               ║
 * ║   ENTER              — Confirm / Start                          ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  Advanced Java Concepts:                                         ║
 * ║   • OOP: Inheritance, Polymorphism, Encapsulation               ║
 * ║   • Interfaces: Drawable, Updatable                             ║
 * ║   • Enums: Screen, EnemyType, PowerUpType, WeaponType           ║
 * ║   • Collections & Generics: CopyOnWriteArrayList<T>             ║
 * ║   • Lambda & Functional Interface (sound synthesis)             ║
 * ║   • Multithreading (daemon threads for audio)                   ║
 * ║   • Custom Graphics2D rendering (gradients, transforms)         ║
 * ║   • File I/O (high score persistence)                           ║
 * ║   • Exception handling throughout                               ║
 * ║   • Inner classes, Anonymous classes                            ║
 * ║   • Observer / Event pattern                                    ║
 * ║   • javax.swing.Timer game loop (fixed timestep ~60fps)         ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class NovaStrike extends JPanel implements ActionListener, KeyListener {

    // ══════════════════════════════════════════════════════════════════════
    //  CONSTANTS
    // ══════════════════════════════════════════════════════════════════════
    static final int W = 900, H = 660;
    static final int FPS = 60;
    static final String SCORE_FILE = "novastrike_scores.txt";

    // ══════════════════════════════════════════════════════════════════════
    //  ENUMS
    // ══════════════════════════════════════════════════════════════════════
    enum Screen { MENU, PLAYING, PAUSED, LEVEL_CLEAR, GAME_OVER, VICTORY, SCORES }

    enum EnemyType {
        SCOUT   (20,  1.8f, 1, 80,   new Color(220,  80,  80)),
        FIGHTER (50,  1.2f, 2, 180,  new Color(220, 140,  40)),
        BOMBER  (100, 0.7f, 3, 350,  new Color(160,  60, 220)),
        CARRIER (200, 0.5f, 4, 600,  new Color( 60, 180, 220)),
        BOSS    (800, 0.4f, 0, 2000, new Color(255,  40, 120));

        final int hp; final float spd; final int bulletType;
        final int pts; final Color color;
        EnemyType(int hp, float spd, int bt, int pts, Color c) {
            this.hp=hp; this.spd=spd; this.bulletType=bt;
            this.pts=pts; this.color=c;
        }
    }

    enum PowerUpType {
        HEALTH ("HEALTH",   new Color(255,  80, 120), 0),
        ENERGY ("ENERGY",   new Color(  0, 200, 255), 1),
        SHIELD ("SHIELD",   new Color(  0, 255, 160), 2),
        TRIPLE ("3-SHOT",   new Color(255, 200,   0), 3),
        LASER  ("LASER",    new Color(255,  60,  60), 4),
        BOMB   ("BOMB",     new Color(255, 140,   0), 5),
        SPEED  ("SPEED",    new Color(160,  80, 255), 6),
        MAGNET ("MAGNET",   new Color(200, 200, 255), 7);

        final String label; final Color color; final int icon;
        PowerUpType(String l, Color c, int i) { label=l; color=c; icon=i; }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INTERFACES
    // ══════════════════════════════════════════════════════════════════════
    interface Drawable  { void draw(Graphics2D g); }
    interface Updatable { void update(); }

    // ══════════════════════════════════════════════════════════════════════
    //  GAME STATE
    // ══════════════════════════════════════════════════════════════════════
    Screen  screen   = Screen.MENU;
    int     level    = 1;
    int     lives    = 3;
    int     score    = 0;
    int     hiScore  = 0;
    long    tick     = 0;

    // Sub-timers
    int     waveDelay     = 0;
    int     levelClearTimer = 0;
    int     bossWarningTimer = 0;
    boolean bossSpawned  = false;
    boolean bossWarning  = false;

    // ══════════════════════════════════════════════════════════════════════
    //  OBJECT LISTS  (thread-safe for smooth audio callbacks)
    // ══════════════════════════════════════════════════════════════════════
    CopyOnWriteArrayList<Enemy>    enemies    = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Bullet>   bullets    = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<PowerUp>  powerups   = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Particle> particles  = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<FloatText> floatTexts= new CopyOnWriteArrayList<>();

    // ══════════════════════════════════════════════════════════════════════
    //  KEY STATE
    // ══════════════════════════════════════════════════════════════════════
    boolean kUp,kDown,kLeft,kRight,kSpace,kSpecial;
    boolean spaceHeld = false; // for auto-fire tracking

    // ══════════════════════════════════════════════════════════════════════
    //  SCROLLING STAR FIELD  (3 parallax layers)
    // ══════════════════════════════════════════════════════════════════════
    static final int NUM_STARS = 220;
    float[] starX   = new float[NUM_STARS];
    float[] starY   = new float[NUM_STARS];
    float[] starSpd = new float[NUM_STARS];
    int[]   starSz  = new int[NUM_STARS];
    int[]   starA   = new int[NUM_STARS];

    // Nebula cloud positions (decorative)
    float[] nebX = {120, 450, 730, 280, 600};
    float[] nebY = new float[5];

    // ══════════════════════════════════════════════════════════════════════
    //  PLAYER, SOUND, RANDOM
    // ══════════════════════════════════════════════════════════════════════
    Player       player;
    SoundEngine  sound;
    Random       rng    = new Random();
    javax.swing.Timer gameTimer;

    // ══════════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════════
    public NovaStrike() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        sound = new SoundEngine();
        loadHiScore();
        initStars();

        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INITIALISATION
    // ══════════════════════════════════════════════════════════════════════
    void initStars() {
        for (int i = 0; i < NUM_STARS; i++) {
            starX[i] = rng.nextFloat() * W;
            starY[i] = rng.nextFloat() * H;
            // 3 layers: slow/dim, medium, fast/bright
            if (i < 80)      { starSpd[i]=0.3f; starSz[i]=1; starA[i]=80; }
            else if (i < 160){ starSpd[i]=0.8f; starSz[i]=1; starA[i]=140; }
            else             { starSpd[i]=1.6f; starSz[i]=2; starA[i]=220; }
        }
        for (int i=0;i<5;i++) nebY[i] = rng.nextFloat()*H;
    }

    void startGame() {
        level = 1; lives = 3; score = 0;
        player = new Player();
        enemies.clear(); bullets.clear();
        powerups.clear(); particles.clear(); floatTexts.clear();
        bossSpawned = false; bossWarning = false; waveDelay = 0;
        spawnWave();
        screen = Screen.PLAYING;
        sound.play("start");
    }

    void spawnWave() {
        enemies.clear();
        bossSpawned = false;
        bossWarning = false;
        bossWarningTimer = 0;

        boolean bossLevel = (level % 3 == 0);

        if (bossLevel) {
            bossWarning = true;
            bossWarningTimer = 120;
            return; // boss spawns after warning
        }

        // Regular waves scale with level
        int scouts   = 2 + level * 2;
        int fighters = level >= 2 ? level       : 0;
        int bombers  = level >= 3 ? level - 1   : 0;
        int carriers = level >= 4 ? level - 2   : 0;

        float xStep = (float) W / (scouts + 2);
        for (int i = 0; i < scouts; i++)
            enemies.add(new Enemy(xStep * (i + 1),
                    -(30 + rng.nextFloat() * 120), EnemyType.SCOUT));

        float fStep = fighters > 0 ? (float) W / (fighters + 1) : 0;
        for (int i = 0; i < fighters; i++)
            enemies.add(new Enemy(fStep * (i + 1),
                    -(150 + rng.nextFloat() * 100), EnemyType.FIGHTER));

        float bStep = bombers > 0 ? (float) W / (bombers + 1) : 0;
        for (int i = 0; i < bombers; i++)
            enemies.add(new Enemy(bStep * (i + 1),
                    -(280 + rng.nextFloat() * 100), EnemyType.BOMBER));

        float cStep = carriers > 0 ? (float) W / (carriers + 1) : 0;
        for (int i = 0; i < carriers; i++)
            enemies.add(new Enemy(cStep * (i + 1),
                    -(420 + rng.nextFloat() * 100), EnemyType.CARRIER));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ══════════════════════════════════════════════════════════════════════
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        // Scroll stars always
        scrollStars();
        if (screen == Screen.PLAYING) update();
        if (screen == Screen.LEVEL_CLEAR) {
            levelClearTimer++;
            if (levelClearTimer > 150) {
                level++;
                levelClearTimer = 0;
                enemies.clear(); bullets.clear(); particles.clear();
                player.tripleTimer = 0; player.laserTimer = 0;
                player.speedTimer  = 0;
                spawnWave();
                screen = Screen.PLAYING;
            }
        }
        repaint();
    }

    void scrollStars() {
        for (int i = 0; i < NUM_STARS; i++) {
            starY[i] += starSpd[i];
            if (starY[i] > H) { starY[i] = -2; starX[i] = rng.nextFloat()*W; }
        }
        for (int i=0;i<5;i++) {
            nebY[i] += 0.1f;
            if (nebY[i] > H + 200) nebY[i] = -200;
        }
    }

    void update() {
        // Boss warning countdown
        if (bossWarning) {
            if (--bossWarningTimer <= 0) {
                bossWarning = false;
                spawnBoss();
            }
            return;
        }

        // Player
        player.update();

        // Auto-fire while space held
        if (kSpace) player.shoot();

        // Special weapon
        if (kSpecial) { player.special(); kSpecial = false; }

        // Enemies
        for (Enemy en : enemies) en.update();

        // Bullets
        List<Bullet> deadB = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update();
            if (b.x < -30 || b.x > W+30 || b.y < -30 || b.y > H+30) {
                deadB.add(b); continue;
            }
            if (b.fromPlayer) {
                for (Enemy en : enemies) {
                    if (!en.alive) continue;
                    if (b.hits(en)) {
                        int dmg = b.isLaser ? 4 : b.isMissile ? 30 : 10;
                        en.hp -= dmg;
                        deadB.add(b);
                        spawnHitFx((int)en.cx(), (int)en.cy(), en.type.color, 5);
                        sound.play("hit");
                        if (en.hp <= 0) {
                            en.alive = false;
                            score += en.type.pts * level;
                            addFloat("+" + en.type.pts*level, (int)en.cx(),
                                    (int)en.cy(), en.type.color);
                            spawnDeathFx((int)en.cx(), (int)en.cy(), en.type.color);
                            sound.play("die");
                            screenShake = en.type == EnemyType.BOSS ? 20 : 6;
                            // Drop power-up
                            if (rng.nextDouble() < (en.type==EnemyType.BOSS ? 0.95 : 0.22)) {
                                PowerUpType[] types = PowerUpType.values();
                                powerups.add(new PowerUp(en.cx(), en.cy(),
                                    types[rng.nextInt(types.length)]));
                            }
                        }
                        break;
                    }
                }
            } else {
                // Enemy bullet vs player
                if (!player.invincible() && b.hitsPlayer()) {
                    player.takeDamage(b.dmg);
                    deadB.add(b);
                    spawnHitFx((int)player.cx(), (int)player.cy(),
                               new Color(100,180,255), 10);
                    sound.play("hit");
                    screenShake = 12;
                }
            }
        }
        bullets.removeAll(deadB);
        enemies.removeIf(en -> !en.alive || en.y > H + 60);

        // Power-ups
        for (PowerUp p : powerups) p.update();
        List<PowerUp> deadP = new ArrayList<>();
        for (PowerUp p : powerups) {
            if (p.y > H + 40) { deadP.add(p); continue; }
            if (p.hitbox().intersects(player.hitbox()) ||
                (player.magnetActive && dist(p.x,p.y,player.cx(),player.cy())<200)) {
                if (player.magnetActive)
                    { p.x += (player.cx()-p.x)*0.15f; p.y += (player.cy()-p.y)*0.15f; }
                if (p.hitbox().intersects(player.hitbox())) {
                    player.applyPowerUp(p.type);
                    deadP.add(p);
                    sound.play("powerup");
                    addFloat(p.type.label, (int)p.x, (int)p.y, p.type.color);
                }
            }
        }
        powerups.removeAll(deadP);

        // Particles
        particles.removeIf(p -> { p.update(); return p.dead; });
        // Float texts
        floatTexts.removeIf(f -> { f.update(); return f.dead; });

        // Screen shake decay
        if (screenShake > 0) screenShake--;

        // Check player death
        if (player.hp <= 0) {
            spawnDeathFx((int)player.cx(), (int)player.cy(), new Color(100,200,255));
            sound.play("gameover");
            lives--;
            if (lives <= 0) {
                if (score > hiScore) { hiScore = score; saveHiScore(); }
                screen = Screen.GAME_OVER;
            } else {
                player = new Player();
                bullets.removeIf(b -> !b.fromPlayer);
                addFloat("LIFE LOST!", W/2, H/2, new Color(255,80,80));
            }
            return;
        }

        // Check wave clear
        if (enemies.isEmpty() && !bossWarning && powerups.isEmpty()) {
            waveDelay++;
            if (waveDelay > 40) {
                waveDelay = 0;
                sound.play("levelup");
                if (level >= 6) {
                    if (score > hiScore) { hiScore = score; saveHiScore(); }
                    screen = Screen.VICTORY;
                } else {
                    screen = Screen.LEVEL_CLEAR;
                    levelClearTimer = 0;
                }
            }
        }
    }

    int screenShake = 0;

    void spawnBoss() {
        enemies.add(new Enemy(W/2f - 50, -80, EnemyType.BOSS));
        bossSpawned = true;
        sound.play("boss");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  PARTICLE / FLOAT TEXT HELPERS
    // ══════════════════════════════════════════════════════════════════════
    void spawnHitFx(int x, int y, Color c, int n) {
        for (int i=0;i<n;i++) particles.add(new Particle(x,y,c,
            (float)(rng.nextGaussian()*3),(float)(rng.nextGaussian()*3),
            14+rng.nextInt(10)));
    }
    void spawnDeathFx(int x, int y, Color c) {
        for (int i=0;i<22;i++) particles.add(new Particle(x,y,c,
            (float)(rng.nextGaussian()*5),(float)(rng.nextGaussian()*5-1),
            20+rng.nextInt(16)));
        for (int i=0;i<10;i++) particles.add(new Particle(x,y,Color.WHITE,
            (float)(rng.nextGaussian()*8),(float)(rng.nextGaussian()*8),
            10+rng.nextInt(8)));
    }
    void addFloat(String t, int x, int y, Color c) {
        floatTexts.add(new FloatText(t, x, y, c));
    }
    double dist(float ax,float ay,float bx,float by) {
        double dx=ax-bx, dy=ay-by; return Math.sqrt(dx*dx+dy*dy);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  PAINT
    // ══════════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Screen shake transform
        if (screenShake > 0) {
            g.translate(rng.nextInt(screenShake*2)-screenShake,
                        rng.nextInt(screenShake*2)-screenShake);
        }

        drawBackground(g);

        switch (screen) {
            case MENU        -> drawMenu(g);
            case PLAYING     -> drawPlaying(g);
            case PAUSED      -> { drawPlaying(g); drawPause(g); }
            case LEVEL_CLEAR -> { drawPlaying(g); drawLevelClear(g); }
            case GAME_OVER   -> drawGameOver(g);
            case VICTORY     -> drawVictory(g);
            case SCORES      -> drawScores(g);
        }
    }

    // ─── Background (starfield + nebulas) ────────────────────────────────
    void drawBackground(Graphics2D g) {
        // Space gradient
        GradientPaint gp = new GradientPaint(0,0,new Color(4,4,18),
                                              0,H,new Color(8,4,22));
        g.setPaint(gp); g.fillRect(0,0,W,H);

        // Nebula clouds
        Color[] nebCols = {new Color(30,0,60,18), new Color(0,20,60,15),
                           new Color(40,0,40,18), new Color(0,30,50,14),
                           new Color(20,0,50,16)};
        for (int i=0;i<5;i++) {
            g.setColor(nebCols[i%nebCols.length]);
            g.fillOval((int)nebX[i]-80, (int)nebY[i]-60, 200, 140);
        }

        // Stars
        for (int i=0;i<NUM_STARS;i++) {
            int bright = starA[i];
            // Twinkle for layer 3
            if (starSpd[i]>1) bright=(int)(bright*(0.7+0.3*Math.sin(tick*0.07+i)));
            g.setColor(new Color(bright,bright,bright,bright));
            g.fillRect((int)starX[i],(int)starY[i],starSz[i],starSz[i]);
        }
    }

    // ─── Playing screen ───────────────────────────────────────────────────
    void drawPlaying(Graphics2D g) {
        // Game objects
        for (PowerUp p  : powerups)   p.draw(g);
        for (Enemy en   : enemies)    en.draw(g);
        for (Bullet b   : bullets)    b.draw(g);
        for (Particle p : particles)  p.draw(g);
        if (player != null)           player.draw(g);
        for (FloatText f : floatTexts) f.draw(g);

        // Boss warning
        if (bossWarning) {
            float alpha = (float)(0.6 + 0.4*Math.sin(tick*0.2));
            g.setColor(new Color(255,40,40,(int)(alpha*200)));
            g.setFont(new Font("Arial",Font.BOLD,44));
            String w = "!! BOSS INCOMING !!";
            FontMetrics fm=g.getFontMetrics();
            g.drawString(w,(W-fm.stringWidth(w))/2, H/2);
        }

        drawHUD(g);
    }

    // ─── HUD ─────────────────────────────────────────────────────────────
    void drawHUD(Graphics2D g) {
        // Top bar
        g.setColor(new Color(0,0,0,160));
        g.fillRect(0,0,W,54);
        g.setColor(new Color(0,140,255,60));
        g.fillRect(0,52,W,2);

        // Score
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(new Color(120,160,220));
        g.drawString("SCORE",12,16);
        g.setFont(new Font("Arial",Font.BOLD,22));
        g.setColor(Color.WHITE);
        g.drawString(String.format("%08d",score),12,40);

        // Hi-Score
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(new Color(120,160,220));
        String hs="HI-SCORE";
        FontMetrics fm=g.getFontMetrics(); int hsx=(W-fm.stringWidth(hs))/2;
        g.drawString(hs,hsx,16);
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.setColor(new Color(255,215,0));
        String hv=String.format("%08d",hiScore);
        fm=g.getFontMetrics();
        g.drawString(hv,(W-fm.stringWidth(hv))/2,40);

        // Level
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(new Color(120,160,220));
        g.drawString("LEVEL",W-80,16);
        g.setFont(new Font("Arial",Font.BOLD,28));
        g.setColor(levelColor());
        g.drawString(String.valueOf(level),W-60,44);

        // HP bar
        drawBar(g, 12, 60, 180, 14, player.hp, player.maxHp,
                new Color(255,60,80), new Color(255,120,140), "HP");

        // Energy bar
        drawBar(g, 12, 82, 180, 12, (int)player.energy, (int)player.maxEnergy,
                new Color(0,160,255), new Color(60,200,255), "NRG");

        // Shield bar (if active)
        if (player.shield > 0) {
            drawBar(g, 12, 100, 180, 10, player.shield, player.maxShield,
                    new Color(0,220,140), new Color(60,255,180), "SHD");
        }

        // Lives
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(new Color(120,160,220));
        g.drawString("LIVES",W/2-40,64);
        for (int i=0;i<lives;i++) drawMiniShip(g, W/2-30+i*22, 70, new Color(0,180,255));

        // Active power-up icons row
        int px=220, py=68;
        if (player.tripleTimer>0) drawPowerIcon(g,px,py,"3-SHOT",new Color(255,200,0));
        if (player.laserTimer>0)  drawPowerIcon(g,px+62,py,"LASER",new Color(255,60,60));
        if (player.speedTimer>0)  drawPowerIcon(g,px+124,py,"SPEED",new Color(160,80,255));
        if (player.shield>0)      drawPowerIcon(g,px+186,py,"SHIELD",new Color(0,255,160));
        if (player.magnetActive)  drawPowerIcon(g,px+248,py,"MAGNET",new Color(200,200,255));

        // Controls hint
        g.setFont(new Font("Arial",Font.PLAIN,10));
        g.setColor(new Color(80,100,130));
        g.drawString("WASD/Arrows:Move  SPACE:Shoot  X:Special  P:Pause  M:Mute",10,H-8);
    }

    void drawBar(Graphics2D g, int x, int y, int bw, int bh,
                 int val, int max, Color c1, Color c2, String label) {
        // Track
        g.setColor(new Color(20,20,30,200));
        g.fillRoundRect(x,y,bw,bh,6,6);
        // Fill
        float ratio = Math.max(0, Math.min(1f, (float)val/max));
        int fw = (int)(bw*ratio);
        if (fw > 2) {
            GradientPaint gp = new GradientPaint(x,y,c2,x+fw,y+bh,c1);
            g.setPaint(gp);
            g.fillRoundRect(x,y,fw,bh,6,6);
        }
        // Border
        g.setPaint(new Color(80,80,100,180));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x,y,bw,bh,6,6);
        // Label
        g.setFont(new Font("Arial",Font.BOLD,9));
        g.setColor(new Color(200,200,220));
        g.drawString(label, x+bw+4, y+bh-1);
        // Value
        g.setFont(new Font("Arial",Font.PLAIN,9));
        g.setColor(Color.WHITE);
        g.drawString(val+"/"+max, x+4, y+bh-2);
    }

    void drawPowerIcon(Graphics2D g, int x, int y, String label, Color c) {
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),60));
        g.fillRoundRect(x,y,56,16,6,6);
        g.setColor(c);
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x,y,56,16,6,6);
        g.setFont(new Font("Arial",Font.BOLD,9));
        FontMetrics fm=g.getFontMetrics();
        g.drawString(label,x+(56-fm.stringWidth(label))/2,y+11);
    }

    Color levelColor() {
        return switch (level) {
            case 1 -> new Color(80,200,255);
            case 2 -> new Color(80,255,160);
            case 3 -> new Color(255,200,0);
            case 4 -> new Color(255,140,40);
            case 5 -> new Color(255,80,80);
            default-> new Color(255,40,200);
        };
    }

    // ─── Menu ────────────────────────────────────────────────────────────
    void drawMenu(Graphics2D g) {
        // Title glow layers
        for (int glow=4;glow>=0;glow--) {
            float a=(float)(0.5+0.5*Math.sin(tick*0.05));
            g.setColor(new Color(0,100+(int)(60*a),255,(30+glow*8)));
            g.setFont(new Font("Arial",Font.BOLD,72+glow*2));
            String t1="NOVA"; FontMetrics fm=g.getFontMetrics();
            g.drawString(t1,(W-fm.stringWidth(t1))/2, 170+glow);
        }
        g.setFont(new Font("Arial",Font.BOLD,72));
        {
            String t1="NOVA"; FontMetrics fm=g.getFontMetrics();
            GradientPaint gp=new GradientPaint(0,100,new Color(0,200,255),0,180,new Color(0,80,255));
            g.setPaint(gp); g.drawString(t1,(W-fm.stringWidth(t1))/2,170);
        }
        for (int glow=4;glow>=0;glow--) {
            g.setColor(new Color(255,80+(int)(60*Math.sin(tick*0.05)),0,20+glow*7));
            g.setFont(new Font("Arial",Font.BOLD,48+glow));
            String t2="STRIKE"; FontMetrics fm=g.getFontMetrics();
            g.drawString(t2,(W-fm.stringWidth(t2))/2,228+glow);
        }
        g.setFont(new Font("Arial",Font.BOLD,48));
        {
            String t2="STRIKE"; FontMetrics fm=g.getFontMetrics();
            GradientPaint gp=new GradientPaint(0,200,new Color(255,140,0),0,250,new Color(255,40,80));
            g.setPaint(gp); g.drawString(t2,(W-fm.stringWidth(t2))/2,228);
        }

        g.setFont(new Font("Arial",Font.ITALIC,14));
        g.setColor(new Color(160,180,220));
        String sub="G A L A C T I C   S I E G E";
        FontMetrics fm=g.getFontMetrics();
        g.drawString(sub,(W-fm.stringWidth(sub))/2,258);

        // Draw animated demo ship
        drawMenuShip(g, W/2f, 330);

        // Animated "Press Enter" button
        float pulse=(float)(0.6+0.4*Math.sin(tick*0.08));
        drawGlowButton(g, "PRESS  ENTER  TO  PLAY", W/2, 440,
                new Color(0,200,255), 16, pulse);
        drawGlowButton(g, "H — High Scores", W/2, 490,
                new Color(160,160,220), 13, 0.8f);

        // Decorative panel
        g.setColor(new Color(0,0,0,120));
        g.fillRoundRect(60, 520, W-120, 94, 14, 14);
        g.setColor(new Color(0,80,160,80));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(60, 520, W-120, 94, 14, 14);

        g.setFont(new Font("Arial",Font.BOLD,12));
        g.setColor(new Color(0,160,255));
        g.drawString("HOW TO PLAY", 80, 542);
        g.setFont(new Font("Arial",Font.PLAIN,12));
        g.setColor(new Color(160,180,210));
        String[] lines = {
            "WASD / Arrow Keys — Move your ship",
            "SPACE — Fire primary weapon (hold for auto-fire)",
            "X / Shift — Special weapon (costs energy)     6 Levels  •  Boss every 3rd level"
        };
        for (int i=0;i<lines.length;i++)
            g.drawString(lines[i], 80, 560+i*18);

        // Credits footer
        g.setFont(new Font("Arial",Font.ITALIC,11));
        g.setColor(new Color(80,100,140));
        String cr="Advanced Java Programming  •  Dr. NTEZIRIZA NKERABAHIZI Josbert  •  UoK";
        fm=g.getFontMetrics();
        g.drawString(cr,(W-fm.stringWidth(cr))/2, H-14);

        // Hi-score
        g.setFont(new Font("Arial",Font.BOLD,14));
        g.setColor(new Color(255,215,0));
        String hsTxt="HI-SCORE: "+String.format("%,d",hiScore);
        fm=g.getFontMetrics();
        g.drawString(hsTxt,(W-fm.stringWidth(hsTxt))/2,286);
    }

    void drawMenuShip(Graphics2D g, float x, float y) {
        Graphics2D g2=(Graphics2D)g.create();
        g2.translate(x,y);
        float sc=1.6f; g2.scale(sc,sc);
        // Glow
        g2.setColor(new Color(0,150,255,30));
        g2.fillOval(-40,-50,80,100);
        // Engine flames
        long t=System.currentTimeMillis();
        for (int f=3;f>=0;f--) {
            float flicker=(float)(0.7+0.3*Math.sin(t*0.02+f));
            Color fc=f<2?new Color(0,180,255,(int)(160*flicker)):
                       new Color(100,220,255,(int)(120*flicker));
            g2.setColor(fc);
            g2.fillOval(-(6-f*1)+(int)(rng.nextFloat()*2), 22+f*4,
                        12-f*2, 14-f*3);
        }
        Player.drawShipShape(g2, new Color(30,100,200), new Color(0,200,255),
                             new Color(100,210,255));
        g2.dispose();
    }

    void drawGlowButton(Graphics2D g, String text, int cx, int cy,
                        Color c, int fs, float glow) {
        g.setFont(new Font("Arial",Font.BOLD,fs));
        FontMetrics fm=g.getFontMetrics(); int tw=fm.stringWidth(text);
        int bx=cx-tw/2-16, by=cy-fs, bw=tw+32, bh=fs+14;
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*50+10)));
        g.fillRoundRect(bx,by,bw,bh,10,10);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*200+30)));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(bx,by,bw,bh,10,10);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*240+20)));
        g.drawString(text,cx-tw/2,cy);
    }

    // ─── Pause ────────────────────────────────────────────────────────────
    void drawPause(Graphics2D g) {
        g.setColor(new Color(0,0,0,160)); g.fillRect(0,0,W,H);
        g.setFont(new Font("Arial",Font.BOLD,52));
        g.setColor(new Color(0,200,255));
        String p="PAUSED"; FontMetrics fm=g.getFontMetrics();
        g.drawString(p,(W-fm.stringWidth(p))/2,H/2-20);
        g.setFont(new Font("Arial",Font.PLAIN,16));
        g.setColor(new Color(160,180,220));
        String r="Press P or ESC to resume";
        fm=g.getFontMetrics();
        g.drawString(r,(W-fm.stringWidth(r))/2,H/2+22);
    }

    // ─── Level Clear ──────────────────────────────────────────────────────
    void drawLevelClear(Graphics2D g) {
        float a=Math.min(1f, levelClearTimer/40f);
        g.setColor(new Color(0,0,0,(int)(a*160))); g.fillRect(0,0,W,H);
        g.setFont(new Font("Arial",Font.BOLD,52));
        float pulse=(float)(0.8+0.2*Math.sin(tick*0.15));
        g.setColor(new Color(0,(int)(220*pulse),100,(int)(a*255)));
        String t="LEVEL "+level+" CLEAR!"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2, H/2-30);
        g.setFont(new Font("Arial",Font.BOLD,22));
        g.setColor(new Color(255,255,255,(int)(a*200)));
        String sc="Score: "+String.format("%,d",score);
        fm=g.getFontMetrics();
        g.drawString(sc,(W-fm.stringWidth(sc))/2, H/2+18);
        g.setFont(new Font("Arial",Font.ITALIC,14));
        g.setColor(new Color(160,200,255,(int)(a*180)));
        String n="Entering Level "+(level+1)+"...";
        fm=g.getFontMetrics();
        g.drawString(n,(W-fm.stringWidth(n))/2,H/2+50);
    }

    // ─── Game Over ────────────────────────────────────────────────────────
    void drawGameOver(Graphics2D g) {
        drawBackground(g);
        // Dark overlay with red vignette
        RadialGradientPaint vign=new RadialGradientPaint(
            new Point2D.Float(W/2f,H/2f), W*0.7f,
            new float[]{0.4f,1f},
            new Color[]{new Color(0,0,0,40),new Color(180,0,0,120)});
        g.setPaint(vign); g.fillRect(0,0,W,H);
        g.setColor(new Color(0,0,0,120)); g.fillRect(0,0,W,H);

        float p=(float)(0.7+0.3*Math.sin(tick*0.08));
        // Title with glow
        for (int gl=3;gl>=0;gl--) {
            g.setColor(new Color(255,30,30,20+gl*10));
            g.setFont(new Font("Arial",Font.BOLD,68+gl*2));
            String go="GAME OVER"; FontMetrics fm=g.getFontMetrics();
            g.drawString(go,(W-fm.stringWidth(go))/2+gl,H/2-90+gl);
        }
        g.setFont(new Font("Arial",Font.BOLD,68));
        GradientPaint gp2=new GradientPaint(0,H/2-150,new Color(255,80,80),
                                             0,H/2-80, new Color(200,20,20));
        g.setPaint(gp2);
        String go="GAME OVER"; FontMetrics fm=g.getFontMetrics();
        g.drawString(go,(W-fm.stringWidth(go))/2,H/2-90);

        g.setFont(new Font("Arial",Font.BOLD,26));
        g.setColor(Color.WHITE);
        String sc="SCORE: "+String.format("%,d",score);
        fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,H/2-20);

        g.setFont(new Font("Arial",Font.PLAIN,16));
        g.setColor(new Color(180,180,220));
        String lv="Level: "+level; fm=g.getFontMetrics();
        g.drawString(lv,(W-fm.stringWidth(lv))/2,H/2+14);

        if (score >= hiScore && score>0) {
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.setColor(new Color(255,220,0,(int)(p*255)));
            String hs2="** NEW HIGH SCORE! **"; fm=g.getFontMetrics();
            g.drawString(hs2,(W-fm.stringWidth(hs2))/2,H/2+50);
        }

        drawGlowButton(g,"ENTER — Play Again", W/2, H/2+106,
                new Color(0,200,100),15,p);
        drawGlowButton(g,"H — High Scores",    W/2, H/2+146,
                new Color(160,160,220),13,0.8f);
        drawGlowButton(g,"ESC — Main Menu",    W/2, H/2+182,
                new Color(160,80,80),12,0.7f);

        // Credits
        g.setFont(new Font("Arial",Font.ITALIC,11));
        g.setColor(new Color(80,100,140));
        String cr="Nova Strike — Advanced Java Programming | Dr. Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-14);
    }

    // ─── Victory ─────────────────────────────────────────────────────────
    void drawVictory(Graphics2D g) {
        drawBackground(g);
        g.setColor(new Color(0,0,0,100)); g.fillRect(0,0,W,H);

        float p=(float)(0.7+0.3*Math.sin(tick*0.08));
        // Gold rays
        for (int i=0;i<12;i++) {
            double ang=Math.toRadians(i*30 + tick*0.5);
            g.setColor(new Color(255,220,0,18+(int)(10*p)));
            g.setStroke(new BasicStroke(8));
            g.drawLine(W/2,H/2,(int)(W/2+Math.cos(ang)*600),
                               (int)(H/2+Math.sin(ang)*600));
        }
        g.setStroke(new BasicStroke(1));

        g.setFont(new Font("Arial",Font.BOLD,62));
        GradientPaint gg=new GradientPaint(0,H/2-130,new Color(255,220,40),
                                            0,H/2-60,new Color(255,140,0));
        g.setPaint(gg);
        String v="YOU  WIN!"; FontMetrics fm=g.getFontMetrics();
        g.drawString(v,(W-fm.stringWidth(v))/2,H/2-70);

        g.setFont(new Font("Arial",Font.BOLD,24));
        g.setColor(Color.WHITE);
        String sc="FINAL SCORE: "+String.format("%,d",score);
        fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,H/2-10);

        if (score>=hiScore) {
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.setColor(new Color(255,220,0,(int)(p*255)));
            String hs2="*** NEW HIGH SCORE! ***"; fm=g.getFontMetrics();
            g.drawString(hs2,(W-fm.stringWidth(hs2))/2,H/2+28);
        }
        drawGlowButton(g,"ENTER — Play Again",W/2,H/2+90,
                new Color(0,220,80),16,p);
        drawGlowButton(g,"H — High Scores",   W/2,H/2+132,
                new Color(255,220,0),13,0.8f);

        g.setFont(new Font("Arial",Font.ITALIC,11));
        g.setColor(new Color(80,100,140));
        String cr="Nova Strike — Advanced Java Programming | Dr. Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-14);
    }

    // ─── High Scores ──────────────────────────────────────────────────────
    void drawScores(Graphics2D g) {
        drawBackground(g);
        g.setColor(new Color(0,0,0,160)); g.fillRect(0,0,W,H);

        g.setFont(new Font("Arial",Font.BOLD,42));
        GradientPaint gg=new GradientPaint(0,80,new Color(255,220,0),0,130,new Color(255,140,0));
        g.setPaint(gg);
        String t="HIGH SCORES"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,120);

        // Panel
        g.setColor(new Color(0,0,0,140));
        g.fillRoundRect(W/2-200,150,400,300,14,14);
        g.setColor(new Color(0,100,200,60));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(W/2-200,150,400,300,14,14);

        List<int[]> scores=loadAllScores();
        for (int i=0;i<Math.min(scores.size(),8);i++) {
            Color c=i==0?new Color(255,215,0):i==1?new Color(192,192,192):
                     i==2?new Color(205,127,50):new Color(160,180,220);
            g.setFont(new Font("Arial",Font.BOLD,18));
            g.setColor(c);
            String line=(i+1)+".   "+String.format("%,d",scores.get(i)[0]);
            fm=g.getFontMetrics();
            g.drawString(line,(W-fm.stringWidth(line))/2,190+i*38);
        }
        if (scores.isEmpty()) {
            g.setFont(new Font("Arial",Font.ITALIC,15));
            g.setColor(new Color(140,160,200));
            String n="No scores yet — play to set records!";
            fm=g.getFontMetrics(); g.drawString(n,(W-fm.stringWidth(n))/2,280);
        }
        drawGlowButton(g,"ESC / ENTER — Back",W/2,H-60,
                new Color(100,150,220),14,0.9f);
    }

    void drawMiniShip(Graphics2D g, int x, int y, Color c) {
        int[] px={x,x-5,x+5}; int[] py={y,y+10,y+10};
        g.setColor(c); g.fillPolygon(px,py,3);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KEY EVENTS
    // ══════════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e) {
        int k=e.getKeyCode();
        if (k==KeyEvent.VK_UP    ||k==KeyEvent.VK_W) kUp   =true;
        if (k==KeyEvent.VK_DOWN  ||k==KeyEvent.VK_S) kDown =true;
        if (k==KeyEvent.VK_LEFT  ||k==KeyEvent.VK_A) kLeft =true;
        if (k==KeyEvent.VK_RIGHT ||k==KeyEvent.VK_D) kRight=true;
        if (k==KeyEvent.VK_SPACE)  kSpace  =true;
        if (k==KeyEvent.VK_X||k==KeyEvent.VK_SHIFT) kSpecial=true;

        switch (screen) {
            case MENU -> {
                if (k==KeyEvent.VK_ENTER){ sound.play("start"); startGame(); }
                if (k==KeyEvent.VK_H)    { screen=Screen.SCORES; }
            }
            case PLAYING -> {
                if (k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if (k==KeyEvent.VK_M) { sound.muted=!sound.muted; }
            }
            case PAUSED -> {
                if (k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER)
                    screen=Screen.PLAYING;
                if (k==KeyEvent.VK_M) sound.muted=!sound.muted;
            }
            case GAME_OVER -> {
                if (k==KeyEvent.VK_ENTER){ startGame(); }
                if (k==KeyEvent.VK_H)    screen=Screen.SCORES;
                if (k==KeyEvent.VK_ESCAPE) screen=Screen.MENU;
            }
            case VICTORY -> {
                if (k==KeyEvent.VK_ENTER){ startGame(); }
                if (k==KeyEvent.VK_H)    screen=Screen.SCORES;
            }
            case SCORES -> {
                if (k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER) screen=Screen.MENU;
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        int k=e.getKeyCode();
        if (k==KeyEvent.VK_UP    ||k==KeyEvent.VK_W) kUp   =false;
        if (k==KeyEvent.VK_DOWN  ||k==KeyEvent.VK_S) kDown =false;
        if (k==KeyEvent.VK_LEFT  ||k==KeyEvent.VK_A) kLeft =false;
        if (k==KeyEvent.VK_RIGHT ||k==KeyEvent.VK_D) kRight=false;
        if (k==KeyEvent.VK_SPACE)  kSpace  =false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════
    //  HIGH SCORE I/O
    // ══════════════════════════════════════════════════════════════════════
    void loadHiScore() {
        List<int[]> s=loadAllScores();
        if (!s.isEmpty()) hiScore=s.get(0)[0];
    }
    List<int[]> loadAllScores() {
        List<int[]> list=new ArrayList<>();
        try (BufferedReader br=new BufferedReader(new FileReader(SCORE_FILE))) {
            String ln; while ((ln=br.readLine())!=null) {
                try { list.add(new int[]{Integer.parseInt(ln.trim())}); }
                catch(NumberFormatException ignored){}
            }
        } catch (IOException ignored){}
        list.sort((a,b)->b[0]-a[0]);
        return list;
    }
    void saveHiScore() {
        List<int[]> all=loadAllScores();
        all.add(new int[]{score});
        all.sort((a,b)->b[0]-a[0]);
        try (PrintWriter pw=new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (int i=0;i<Math.min(all.size(),10);i++) pw.println(all.get(i)[0]);
        } catch (IOException ignored){}
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame f=new JFrame("Nova Strike — Galactic Siege");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            NovaStrike game=new NovaStrike();
            f.add(game); f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: PLAYER
    // ══════════════════════════════════════════════════════════════════════
    class Player implements Drawable {
        float x=W/2f-22, y=H-110;
        int   w=44, h=52;
        int   hp=120, maxHp=120;
        float energy=100, maxEnergy=100;
        int   shield=0, maxShield=60;
        int   shootCd=0, shootRate=8;
        int   invTimer=0;
        int   tripleTimer=0, laserTimer=0, speedTimer=0;
        boolean magnetActive=false;
        int   magnetTimer=0;
        int   thrustAnim=0;

        float cx(){ return x+w/2f; }
        float cy(){ return y+h/2f; }
        Rectangle2D hitbox(){ return new Rectangle2D.Float(x+6,y+6,w-12,h-12); }
        boolean invincible(){ return invTimer>0; }

        void update() {
            float spd = speedTimer>0 ? 6.5f : 4.8f;
            if (kUp   ) y-=spd;
            if (kDown ) y+=spd;
            if (kLeft ) x-=spd;
            if (kRight) x+=spd;
            x=clamp(x,0,W-w); y=clamp(y,52,H-h-10);

            if (shootCd>0) shootCd--;
            if (invTimer>0) invTimer--;
            if (tripleTimer>0) tripleTimer--;
            if (laserTimer>0) laserTimer--;
            if (speedTimer>0) speedTimer--;
            if (magnetTimer>0) { magnetTimer--; if(magnetTimer<=0) magnetActive=false; }
            energy=Math.min(maxEnergy, energy+0.18f); // regenerate
            thrustAnim=(thrustAnim+1)%8;
        }

        void shoot() {
            if (shootCd>0) return;
            int rate = laserTimer>0 ? 4 : (tripleTimer>0 ? 6 : shootRate);
            shootCd=rate;
            if (laserTimer>0) {
                bullets.add(new Bullet(cx()-3, y-20, 0,-18,true,12,false,true));
                sound.play("laser");
            } else if (tripleTimer>0) {
                bullets.add(new Bullet(cx()-2, y+4, 0,-14,true,10,false,false));
                bullets.add(new Bullet(cx()-2, y+4,-3,-13,true,10,false,false));
                bullets.add(new Bullet(cx()-2, y+4, 3,-13,true,10,false,false));
                sound.play("shoot");
            } else {
                bullets.add(new Bullet(cx()-2, y+4, 0,-14,true,10,false,false));
                sound.play("shoot");
            }
        }

        void special() {
            if (energy<30) return;
            energy-=30;
            // Fire spread missiles
            for (int a=-3;a<=3;a+=2) {
                bullets.add(new Bullet(cx()-3, y-10, a,-12,true,25,true,false));
            }
            sound.play("missile");
        }

        void takeDamage(int dmg) {
            if (shield>0) { shield-=dmg; if(shield<0)shield=0; return; }
            hp-=dmg; invTimer=50;
        }

        void applyPowerUp(PowerUpType t) {
            switch(t) {
                case HEALTH -> hp=Math.min(maxHp,hp+40);
                case ENERGY -> energy=Math.min(maxEnergy,energy+50);
                case SHIELD -> { shield=maxShield; }
                case TRIPLE -> tripleTimer=400;
                case LASER  -> laserTimer=300;
                case BOMB   -> {
                    enemies.forEach(en->{
                        score+=en.type.pts*level;
                        spawnDeathFx((int)en.cx(),(int)en.cy(),en.type.color);
                        en.alive=false;
                    });
                    screenShake=20; sound.play("bomb");
                }
                case SPEED  -> speedTimer=360;
                case MAGNET -> { magnetActive=true; magnetTimer=500; }
            }
        }

        @Override public void draw(Graphics2D g) {
            if (invTimer>0 && (invTimer/6)%2==0) return; // blink

            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(cx(),cy());

            // Engine flames (animated)
            drawEngineFlames(g2);

            // Shield bubble
            if (shield>0) {
                float sa=Math.min(1f,shield/(float)maxShield);
                g2.setColor(new Color(0,255,160,(int)(sa*80)));
                g2.fillOval(-w/2-8,-h/2-8,w+16,h+16);
                g2.setColor(new Color(0,255,160,(int)(sa*200)));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(-w/2-8,-h/2-8,w+16,h+16);
            }

            // Magnet field
            if (magnetActive) {
                g2.setColor(new Color(200,200,255,30));
                g2.fillOval(-100,-100,200,200);
                g2.setColor(new Color(200,200,255,60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(-100,-100,200,200);
            }

            // Ship hull
            drawShipShape(g2, new Color(20,80,180), new Color(0,160,255),
                          new Color(80,200,255));

            // Laser charge glow
            if (laserTimer>0) {
                g2.setColor(new Color(255,60,60,120));
                g2.fillOval(-6,-h/2-10,12,12);
            }
            g2.dispose();
        }

        void drawEngineFlames(Graphics2D g2) {
            int flH=10+thrustAnim;
            if (kUp||kDown||kLeft||kRight) flH+=4;
            Color[] fc={new Color(0,200,255),new Color(0,120,255),new Color(0,60,200)};
            for (int f=0;f<3;f++) {
                g2.setColor(fc[f]);
                g2.fillOval(-(4-f),(h/2+2)+f*3,8-f*2,flH-f*3);
            }
            // Left engine
            g2.setColor(new Color(0,160,255,160));
            g2.fillOval(-w/2+4,h/2+1,6,flH-4);
            // Right engine
            g2.fillOval(w/2-10,h/2+1,6,flH-4);
        }

        static void drawShipShape(Graphics2D g2, Color body, Color accent, Color highlight) {
            // Shadow
            g2.setColor(new Color(0,40,100,60));
            g2.fillOval(-18,4,36,20);

            // Wings
            int[] lwx={-4,-22,-16,-6};
            int[] lwy={-10,-4, 18, 22};
            int[] rwx={ 4, 22, 16,  6};
            int[] rwy={-10,-4, 18, 22};
            g2.setColor(body.darker());
            g2.fillPolygon(lwx,lwy,4); g2.fillPolygon(rwx,rwy,4);
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawPolygon(lwx,lwy,4); g2.drawPolygon(rwx,rwy,4);

            // Main hull
            int[] hx={0,14,10, 0,-10,-14};
            int[] hy={-26,-8, 20,26, 20, -8};
            GradientPaint gp=new GradientPaint(-14,0,body,14,0,accent);
            g2.setPaint(gp);
            g2.fillPolygon(hx,hy,6);
            g2.setColor(highlight);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(hx,hy,6);

            // Cockpit
            g2.setColor(new Color(160,230,255,220));
            g2.fillOval(-6,-16,12,16);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(-6,-16,12,16);

            // Wing accents
            g2.setColor(new Color(0,200,255,180));
            g2.fillRect(-20,-2,6,8);
            g2.fillRect(14,-2,6,8);

            // Cannon tip
            g2.setColor(new Color(100,200,255));
            g2.fillRect(-2,-28,4,6);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: ENEMY
    // ══════════════════════════════════════════════════════════════════════
    class Enemy implements Drawable {
        float x, y;
        int   w, h;
        int   hp, maxHp;
        EnemyType type;
        boolean alive=true;
        int shootCd, shootRate;
        float angle=0, waveOff;
        int bossPhase=0;
        int entryFrames=80; // entrance animation

        Enemy(float x, float y, EnemyType t) {
            this.x=x; this.y=y; this.type=t;
            this.hp=this.maxHp=t.hp*(1+(level-1)/3);
            this.waveOff=(float)(Math.random()*Math.PI*2);
            switch(t) {
                case SCOUT   -> { w=32; h=28; shootRate=0; }
                case FIGHTER -> { w=40; h=36; shootRate=90; }
                case BOMBER  -> { w=52; h=44; shootRate=70; }
                case CARRIER -> { w=64; h=54; shootRate=55; }
                case BOSS    -> { w=100;h=88; shootRate=40; }
            }
            shootCd=(int)(Math.random()*shootRate);
        }

        float cx(){ return x+w/2f; }
        float cy(){ return y+h/2f; }
        Rectangle2D hitbox(){ return new Rectangle2D.Float(x+4,y+4,w-8,h-8); }

        void update() {
            if (entryFrames>0) { y+=2; entryFrames--; return; }
            angle+=0.04f;

            switch(type) {
                case SCOUT -> {
                    y+=type.spd; x+=(float)(Math.sin(angle+waveOff)*2.2);
                    x=clamp(x,0,W-w);
                }
                case FIGHTER -> {
                    y+=type.spd; x+=(float)(Math.sin(angle*0.7+waveOff)*1.6);
                    x=clamp(x,0,W-w);
                    if (--shootCd<=0) { shootCd=shootRate; shootAt(); }
                }
                case BOMBER -> {
                    y+=type.spd;
                    if (y>150) x+=(float)(Math.sin(angle*0.5+waveOff)*2.8);
                    x=clamp(x,0,W-w);
                    if (--shootCd<=0) { shootCd=shootRate; shootSpread(); }
                }
                case CARRIER -> {
                    if (y<80) y+=type.spd*1.4; else y+=type.spd*0.3;
                    x+=(float)(Math.sin(angle*0.4+waveOff)*1.2);
                    x=clamp(x,0,W-w);
                    if (--shootCd<=0) { shootCd=shootRate; shootSpread(); }
                }
                case BOSS -> updateBoss();
            }
        }

        void updateBoss() {
            // Multi-phase boss AI
            float hpRatio=(float)hp/maxHp;
            if (hpRatio<0.33f) bossPhase=2;
            else if (hpRatio<0.66f) bossPhase=1;

            // Entry
            if (y<60) { y+=1.8f; return; }

            // Phase 0: slow sweep
            // Phase 1: faster + vertical bob
            // Phase 2: frantic + homing
            float spd=0.6f+bossPhase*0.4f;
            x+=(float)(Math.sin(angle*(0.5f+bossPhase*0.2f))*spd*(1.5f+bossPhase));
            if (bossPhase>=1) y=60+(float)(Math.sin(angle*0.3)*30);
            x=clamp(x,30,W-w-30);

            if (--shootCd<=0) {
                shootCd=Math.max(20,shootRate-bossPhase*8);
                bossFire();
            }
        }

        void shootAt() {
            float dx=player.cx()-cx(), dy=player.cy()-cy();
            double len=Math.sqrt(dx*dx+dy*dy);
            if (len==0) return;
            float spd=4.5f;
            bullets.add(new Bullet(cx()-3,cy()+h/2,
                (float)(dx/len*spd),(float)(dy/len*spd),false,6,false,false));
            sound.play("enemyshoot");
        }

        void shootSpread() {
            float[] angles={-0.25f,0,0.25f};
            for (float a:angles) {
                float spd=3.5f;
                bullets.add(new Bullet(cx()-3,cy()+h/2f,
                    (float)(Math.sin(a)*spd),(float)(Math.cos(a)*spd),
                    false,8,false,false));
            }
            sound.play("enemyshoot");
        }

        void bossFire() {
            int shots=4+bossPhase*2;
            for (int i=0;i<shots;i++) {
                double a=Math.PI*2*i/shots;
                float spd=3.8f+bossPhase*0.6f;
                bullets.add(new Bullet(cx()-3,cy(),
                    (float)(Math.sin(a)*spd),(float)(Math.cos(a)*spd),
                    false,10+bossPhase*4,false,false));
            }
            if (bossPhase>=1) shootAt(); // aimed shot too
            sound.play("bossfire");
        }

        @Override public void draw(Graphics2D g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(cx(),cy());

            Color base=type.color;
            // Hit flash handled by particle FX

            switch(type) {
                case SCOUT   -> drawScout(g2,base);
                case FIGHTER -> drawFighter(g2,base);
                case BOMBER  -> drawBomber(g2,base);
                case CARRIER -> drawCarrier(g2,base);
                case BOSS    -> drawBoss(g2,base);
            }

            // HP bar for non-scouts
            if (type!=EnemyType.SCOUT && hp<maxHp) {
                int bw=w, bx=-w/2, by=-h/2-10;
                g2.setColor(new Color(0,0,0,160));
                g2.fillRect(bx,by,bw,5);
                float ratio=(float)hp/maxHp;
                Color hc=ratio>0.6f?new Color(0,220,80):
                         ratio>0.3f?new Color(255,200,0):new Color(255,60,60);
                g2.setColor(hc);
                g2.fillRect(bx,by,(int)(bw*ratio),5);
            }
            g2.dispose();
        }

        void drawScout(Graphics2D g2, Color c) {
            int[] sx={0,w/2-2,w/3,-w/3,-w/2+2};
            int[] sy={-h/2,h/6,h/2,h/2,h/6};
            g2.setColor(c.darker());
            g2.fillPolygon(sx,sy,5);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawPolygon(sx,sy,5);
            g2.setColor(new Color(255,200,200,180));
            g2.fillOval(-4,2,8,8);
        }

        void drawFighter(Graphics2D g2, Color c) {
            int[] bx={0,w/2,w/3-2,0,-w/3+2,-w/2};
            int[] by={-h/2,-h/6,h/2,h/2-4,h/2,-h/6};
            GradientPaint gp=new GradientPaint(-w/2,0,c.darker(),w/2,0,c);
            g2.setPaint(gp); g2.fillPolygon(bx,by,6);
            g2.setColor(c.brighter()); g2.setStroke(new BasicStroke(1.4f));
            g2.drawPolygon(bx,by,6);
            g2.setColor(new Color(255,180,80,200)); g2.fillOval(-5,-2,10,10);
            g2.setColor(new Color(255,120,0,120)); g2.fillOval(-w/2+4,h/4,6,10);
            g2.fillOval(w/2-10,h/4,6,10);
        }

        void drawBomber(Graphics2D g2, Color c) {
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),40));
            g2.fillOval(-w/2-6,-h/2-4,w+12,h+8);
            int[] bx={0,w/2+4,w/3,0,-w/3,-w/2-4};
            int[] by={-h/2,-h/4,h/2,h/2,h/2,-h/4};
            GradientPaint gp=new GradientPaint(0,-h/2,c.brighter(),0,h/2,c.darker());
            g2.setPaint(gp); g2.fillPolygon(bx,by,6);
            g2.setColor(new Color(80,40,180)); g2.setStroke(new BasicStroke(2f));
            g2.drawPolygon(bx,by,6);
            g2.setColor(new Color(200,100,255,200));
            g2.fillOval(-8,-6,16,16);
            g2.setColor(new Color(120,60,255,180));
            g2.fillOval(-w/2+6,h/3,8,12); g2.fillOval(w/2-14,h/3,8,12);
        }

        void drawCarrier(Graphics2D g2, Color c) {
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),30));
            g2.fillOval(-w/2-10,-h/2-8,w+20,h+16);
            GradientPaint gp=new GradientPaint(-w/2,0,new Color(20,60,100),
                                                w/2,0,c);
            g2.setPaint(gp);
            g2.fillRoundRect(-w/2,-h/2,w,h,16,16);
            g2.setColor(c); g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(-w/2,-h/2,w,h,16,16);
            g2.setColor(new Color(0,240,255,220));
            g2.fillOval(-w/2+6,-h/4,w/3,h/2);
            g2.fillOval(w/6,-h/4,w/3,h/2);
            g2.setColor(new Color(0,200,255));
            for (int i=-1;i<=1;i+=2) {
                g2.fillOval(i*(w/2+2),-4,8,8);
            }
        }

        void drawBoss(Graphics2D g2, Color c) {
            // Pulsing outer ring
            float pls=(float)(0.6+0.4*Math.sin(angle*4));
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(pls*80)));
            g2.setStroke(new BasicStroke(4f));
            g2.drawOval(-w/2-12,-h/2-12,w+24,h+24);
            if (bossPhase>=1) {
                g2.setColor(new Color(255,200,0,(int)(pls*60)));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(-w/2-22,-h/2-22,w+44,h+44);
            }

            // Main body
            GradientPaint gp=new GradientPaint(-w/2,-h/2,c.darker().darker(),
                                                w/2, h/2,c);
            g2.setPaint(gp);
            g2.fillOval(-w/2,-h/2,w,h);
            g2.setColor(c.brighter()); g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(-w/2,-h/2,w,h);

            // Wings
            int[][] lw={{-w/2,-w/2-24,-w/2-16,-w/3},
                        {-h/4,  h/4,   h/2,   h/6}};
            int[][] rw={{ w/2, w/2+24, w/2+16,  w/3},
                        {-h/4,  h/4,   h/2,   h/6}};
            g2.setColor(c.darker());
            g2.fillPolygon(lw[0],lw[1],4); g2.fillPolygon(rw[0],rw[1],4);
            g2.setColor(c); g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(lw[0],lw[1],4); g2.drawPolygon(rw[0],rw[1],4);

            // Core eye
            g2.setColor(new Color(255,255,0,(int)(pls*255)));
            g2.fillOval(-12,-12,24,24);
            g2.setColor(bossPhase>=2?Color.RED:new Color(200,100,255));
            g2.fillOval(-7,-7,14,14);

            // Cannons
            g2.setColor(new Color(80,80,100));
            g2.fillRect(-8,h/2-4,6,18); g2.fillRect(2,h/2-4,6,18);
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),160));
            g2.fillOval(-4,h/2+10,8,8); g2.fillOval(-4+6,h/2+10,8,8);

            // Phase indicator
            String ph="Phase "+(bossPhase+1);
            g2.setFont(new Font("Arial",Font.BOLD,11));
            g2.setColor(new Color(255,220,0,200));
            FontMetrics fm=g2.getFontMetrics();
            g2.drawString(ph,-fm.stringWidth(ph)/2,h/2+34);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: BULLET
    // ══════════════════════════════════════════════════════════════════════
    class Bullet implements Drawable {
        float x,y,vx,vy;
        boolean fromPlayer, isMissile, isLaser;
        int dmg;

        Bullet(float x,float y,float vx,float vy,
               boolean fp,int dmg,boolean missile,boolean laser) {
            this.x=x; this.y=y; this.vx=vx; this.vy=vy;
            this.fromPlayer=fp; this.dmg=dmg;
            this.isMissile=missile; this.isLaser=laser;
        }
        void update(){ x+=vx; y+=vy; }
        boolean hits(Enemy en){ return en.hitbox().contains(x,y); }
        boolean hitsPlayer(){
            return player.hitbox().contains(x+vx,y+vy);
        }

        @Override public void draw(Graphics2D g) {
            if (fromPlayer) {
                if (isLaser) {
                    // Laser beam
                    g.setColor(new Color(255,60,60,180));
                    g.fillRect((int)x-2,(int)y-16,4,24);
                    g.setColor(new Color(255,180,180,120));
                    g.fillRect((int)x-4,(int)y-18,8,28);
                    g.setColor(Color.WHITE);
                    g.fillRect((int)x-1,(int)y-12,2,18);
                } else if (isMissile) {
                    g.setColor(new Color(255,160,0,200));
                    g.fillOval((int)x-5,(int)y-7,10,14);
                    g.setColor(new Color(255,100,0,180));
                    g.fillOval((int)x-3,(int)y-4,6,8);
                    // Missile trail
                    g.setColor(new Color(255,200,80,80));
                    g.fillOval((int)x-4,(int)y+6,8,14);
                } else {
                    // Standard bolt
                    g.setColor(new Color(0,230,255,200));
                    g.fillRoundRect((int)x-3,(int)y-8,6,16,3,3);
                    g.setColor(new Color(200,240,255,200));
                    g.fillOval((int)x-2,(int)y-6,4,4);
                }
            } else {
                // Enemy bullet
                g.setColor(new Color(255,80,80,200));
                g.fillOval((int)x-5,(int)y-5,10,10);
                g.setColor(new Color(255,160,160,120));
                g.fillOval((int)x-8,(int)y-8,16,16);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: POWERUP
    // ══════════════════════════════════════════════════════════════════════
    class PowerUp implements Drawable {
        float x, y, vy=1.4f;
        PowerUpType type;
        float angle=0;

        PowerUp(float x, float y, PowerUpType t){ this.x=x; this.y=y; this.type=t; }
        void update(){ y+=vy; angle+=0.05f; }
        Rectangle2D hitbox(){ return new Rectangle2D.Float(x-12,y-12,24,24); }

        @Override public void draw(Graphics2D g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(x,y); g2.rotate(angle);

            Color c=type.color;
            // Outer glow
            float pls=(float)(0.5+0.5*Math.sin(angle*8));
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(pls*100)));
            g2.fillOval(-16,-16,32,32);

            // Diamond body
            int[] px={0,11,0,-11}; int[] py={-11,0,11,0};
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),200));
            g2.fillPolygon(px,py,4);
            g2.setColor(c.brighter()); g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(px,py,4);

            // Label
            g2.setFont(new Font("Arial",Font.BOLD,7));
            g2.setColor(Color.WHITE);
            FontMetrics fm=g2.getFontMetrics();
            String lbl=type.label.substring(0,Math.min(5,type.label.length()));
            g2.drawString(lbl,-fm.stringWidth(lbl)/2,3);
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: PARTICLE
    // ══════════════════════════════════════════════════════════════════════
    class Particle implements Drawable {
        float x,y,vx,vy;
        Color c; int life,maxLife; boolean dead;

        Particle(int x,int y,Color c,float vx,float vy,int life){
            this.x=x; this.y=y; this.c=c;
            this.vx=vx; this.vy=vy;
            this.life=this.maxLife=life;
        }
        void update(){
            x+=vx; y+=vy; vx*=0.88f; vy*=0.88f; vy+=0.06f;
            if(--life<=0) dead=true;
        }
        @Override public void draw(Graphics2D g){
            float a=(float)life/maxLife;
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(a*220)));
            g.fillOval((int)(x-2),(int)(y-2),4,4);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: FLOAT TEXT
    // ══════════════════════════════════════════════════════════════════════
    class FloatText implements Drawable {
        String text; float x,y; Color c; float life=1f; boolean dead;
        FloatText(String t,int x,int y,Color c){text=t;this.x=x;this.y=y;this.c=c;}
        void update(){ y-=1.2f; life-=0.02f; if(life<=0) dead=true; }
        @Override public void draw(Graphics2D g){
            g.setFont(new Font("Arial",Font.BOLD,15));
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(life*240)));
            FontMetrics fm=g.getFontMetrics();
            g.drawString(text,(int)x-fm.stringWidth(text)/2,(int)y);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASS: SOUND ENGINE
    //  (Pure Java synthesis — no external audio files needed)
    // ══════════════════════════════════════════════════════════════════════
    static class SoundEngine {
        static final int SR=44100;
        boolean muted=false;

        void play(String name){
            if (muted) return;
            byte[] pcm=gen(name);
            if (pcm==null) return;
            Thread t=new Thread(()->{
                try {
                    AudioFormat fmt=new AudioFormat(SR,16,1,true,false);
                    DataLine.Info info=new DataLine.Info(SourceDataLine.class,fmt);
                    if (!AudioSystem.isLineSupported(info)) return;
                    try (SourceDataLine line=(SourceDataLine)AudioSystem.getLine(info)){
                        line.open(fmt,2048); line.start();
                        line.write(pcm,0,pcm.length); line.drain();
                    }
                } catch(Exception ignored){}
            });
            t.setDaemon(true); t.start();
        }

        private byte[] gen(String name){
            return switch(name){
                case "shoot"      -> synth(0.08, t->tri(t,820)*env(t,0.01,0.02,0.3,0.02)*0.45);
                case "laser"      -> synth(0.12, t->(sine(t,1200-t*400)+tri(t,600))*env(t,0.005,0.01,0.5,0.05)*0.50);
                case "missile"    -> synth(0.20, t->(sine(t,300+t*100)+noise()*0.2)*env(t,0.02,0.05,0.4,0.1)*0.55);
                case "enemyshoot" -> synth(0.10, t->tri(t,540)*env(t,0.01,0.03,0.3,0.02)*0.32);
                case "bossfire"   -> synth(0.18, t->(sine(t,220)+tri(t,330)*0.5)*env(t,0.01,0.04,0.4,0.06)*0.55);
                case "hit"        -> synth(0.10, t->noise()*env(t,0.005,0.02,0.2,0.04)*0.48);
                case "die"        -> synth(0.28, t->(sine(t,260*Math.pow(0.25,t))+noise()*0.15)*env(t,0.01,0.04,0.4,0.1)*0.55);
                case "bomb"       -> synth(0.45, t->(noise()*0.8+sine(t,80+t*40)*0.6)*env(t,0.005,0.08,0.5,0.12)*0.70);
                case "powerup"    -> synth(0.30, t->arp(t)*env(t,0.01,0.05,0.6,0.08)*0.52);
                case "levelup"    -> synth(0.60, t->fanfare(t)*env(t,0.01,0.08,0.65,0.12)*0.55);
                case "gameover"   -> synth(0.80, t->sine(t,340-t*180)*env(t,0.01,0.1,0.55,0.15)*0.52);
                case "boss"       -> synth(0.90, t->(sine(t,110)+sine(t,165)*0.6+noise()*0.1)*env(t,0.02,0.15,0.6,0.15)*0.58);
                case "start"      -> synth(0.50, t->(sine(t,440+t*120)+sine(t,660+t*60))*env(t,0.01,0.05,0.6,0.1)*0.45);
                default           -> null;
            };
        }

        // ── Waveform primitives ───────────────────────────────────────────
        static double sine(double t,double f){ return Math.sin(2*Math.PI*f*t); }
        static double tri(double t,double f){
            double ph=(t*f)%1; return ph<0.5?4*ph-1:3-4*ph;
        }
        static final Random NR=new Random();
        static double noise(){ return NR.nextDouble()*2-1; }

        static double env(double t,double a,double d,double s,double r){
            if(t<a)      return t/a;
            if(t<a+d)    return 1-(1-0.7)*((t-a)/d);
            if(t<1.0-r)  return 0.7;
            return 0.7*(1-(t-(1.0-r))/r);
        }

        static double arp(double t){
            double[] f={330,415,494,622,784};
            int i=Math.min((int)(t*f.length),f.length-1);
            return sine(t,f[i])+sine(t,f[i]*2)*0.25;
        }

        static double fanfare(double t){
            double[] f={330,440,554,659,880};
            int i=Math.min((int)(t*f.length),f.length-1);
            return sine(t,f[i])+sine(t,f[i]*1.5)*0.4+sine(t,f[i]*2)*0.2;
        }

        private byte[] synth(double dur,DoubleUnaryOperator fn){
            int n=(int)(SR*dur);
            byte[] buf=new byte[n*2];
            for(int i=0;i<n;i++){
                double t=(double)i/n;
                int s=(int)(fn.applyAsDouble(t)*28000);
                s=Math.max(-32768,Math.min(32767,s));
                buf[2*i]=(byte)(s&0xFF); buf[2*i+1]=(byte)((s>>8)&0xFF);
            }
            return buf;
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UTILITY
    // ══════════════════════════════════════════════════════════════════════
    static float clamp(float v,float lo,float hi){ return Math.max(lo,Math.min(hi,v)); }
    static int   clamp(int   v,int   lo,int   hi){ return Math.max(lo,Math.min(hi,v)); }
}