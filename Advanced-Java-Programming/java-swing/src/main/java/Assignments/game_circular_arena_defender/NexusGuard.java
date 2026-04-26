/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Assignments.game_circular_arena_defender;

/**
 *
 * @author Elysee NIYBIZI
 * @Reg No. 2305000921
 */

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.DoubleUnaryOperator;
import java.io.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║        NEXUS GUARD — Circular Arena Defender                    ║
 * ║   Advanced Java Programming — UoK                               ║
 * ║   Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert                   ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  UNIQUE CONCEPT: Radial arena — you rotate a cannon at the      ║
 * ║  center and shoot outward at enemies spiralling in from ALL      ║
 * ║  directions. Completely different from every reference game.    ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  CONTROLS:                                                       ║
 * ║   LEFT / A          — Rotate cannon counter-clockwise           ║
 * ║   RIGHT / D         — Rotate cannon clockwise                   ║
 * ║   SPACE             — Fire (auto-fire while held)               ║
 * ║   W / UP            — Activate Shield (costs energy)            ║
 * ║   S / DOWN          — Nova Bomb — destroys all (costs energy)   ║
 * ║   P / ESC           — Pause                                     ║
 * ║   M                 — Mute / Unmute                             ║
 * ║   ENTER             — Start / Confirm                           ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  Advanced Java Concepts Demonstrated:                           ║
 * ║   • OOP Hierarchy: GameObject → Enemy / Bullet / Particle       ║
 * ║   • Interfaces: Drawable, Updatable, Collidable                 ║
 * ║   • Enums: Screen, EnemyType, PowerUpType, UpgradeType          ║
 * ║   • Generics: CopyOnWriteArrayList<T>                           ║
 * ║   • Lambda & DoubleUnaryOperator (sound synthesis)              ║
 * ║   • Multithreading (daemon audio threads)                       ║
 * ║   • Custom Graphics2D (radial geometry, polar coords, glow)     ║
 * ║   • File I/O (high score persistence)                           ║
 * ║   • Exception handling                                          ║
 * ║   • Inner classes & Anonymous classes                           ║
 * ║   • Math: trigonometry, polar→cartesian, radial collision       ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class NexusGuard extends JPanel implements ActionListener, KeyListener {

    // ══════════════════════════════════════════════════════════════════
    //  SCREEN & ARENA CONSTANTS
    // ══════════════════════════════════════════════════════════════════
    static final int W = 900, H = 900;          // square window
    static final int CX = W / 2, CY = H / 2;    // arena centre
    static final int ARENA_R    = 380;           // outer spawn radius
    static final int CORE_R     = 42;            // nexus core radius
    static final int DANGER_R   = 80;            // if enemy reaches this, damage
    static final int CANNON_LEN = 72;            // cannon barrel length
    static final int FPS        = 60;
    static final String SCORE_FILE = "nexusguard_scores.txt";

    // ══════════════════════════════════════════════════════════════════
    //  ENUMS
    // ══════════════════════════════════════════════════════════════════
    enum Screen { MENU, PLAYING, PAUSED, WAVE_CLEAR, UPGRADE, GAME_OVER, VICTORY, SCORES }

    enum EnemyType {
        DRONE   (15,  1.20f, 0,  50,   new Color(220, 60,  60),  18),
        SPEEDER (10,  2.20f, 1,  80,   new Color(255, 160,  0),  12),
        TANK    (60,  0.65f, 2,  200,  new Color( 60, 180, 255),  26),
        ZIGZAG  (25,  1.50f, 3,  120,  new Color(160,  60, 255),  16),
        SPLITTER(40,  0.90f, 4,  160,  new Color( 60, 220, 140),  22),
        GHOST   (30,  1.10f, 5,  220,  new Color(200, 200, 255),  14),
        BOSS    (500, 0.40f, 6, 2000,  new Color(255,  40, 160),  60);

        final int   hp;       // base HP
        final float spd;      // radial speed (px/frame inward)
        final int   ai;       // AI behaviour index
        final int   pts;      // score points
        final Color color;
        final int   radius;   // visual size

        EnemyType(int h,float s,int a,int p,Color c,int r){
            hp=h;spd=s;ai=a;pts=p;color=c;radius=r;
        }
    }

    enum PowerUpType {
        HEALTH  ("+ HEALTH",  new Color(255,  80, 120)),
        ENERGY  ("+ ENERGY",  new Color(  0, 200, 255)),
        RAPID   ("RAPID FIRE",new Color(255, 220,   0)),
        PIERCE  ("PIERCE",    new Color(255, 100,  50)),
        SHIELD  ("SHIELD",    new Color(  0, 255, 180)),
        MULTI   ("3-WAY",     new Color(180,  80, 255)),
        NOVA    ("NOVA BOMB",  new Color(255,  80,  80)),
        SLOW    ("TIME SLOW",  new Color(  0, 200, 255));

        final String label; final Color color;
        PowerUpType(String l, Color c){ label=l; color=c; }
    }

    enum UpgradeType {
        DAMAGE  ("Damage +25%",    new Color(255, 120,  80)),
        FIRE    ("Fire Rate +20%", new Color(255, 220,   0)),
        MAXHP   ("Max HP +20",     new Color(255,  80, 120)),
        MAXNRG  ("Max Energy +30", new Color(  0, 200, 255)),
        REGEN   ("HP Regen",       new Color( 80, 255, 120)),
        SPREAD  ("Spread Shot",    new Color(180,  80, 255)),
        BOUNCER ("Bouncing Shots", new Color(  0, 220, 255));

        final String desc; final Color color;
        UpgradeType(String d,Color c){ desc=d; color=c; }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INTERFACES
    // ══════════════════════════════════════════════════════════════════
    interface Drawable   { void draw(Graphics2D g); }
    interface Updatable  { void update(); }
    interface Collidable { double getDistFromCenter(); int getRadius(); }

    // ══════════════════════════════════════════════════════════════════
    //  GAME STATE
    // ══════════════════════════════════════════════════════════════════
    Screen screen   = Screen.MENU;
    int    wave     = 0;
    int    score    = 0;
    int    hiScore  = 0;
    int    combo    = 0;
    int    comboTimer = 0;
    int    maxCombo = 0;
    long   tick     = 0;
    int    waveDelay  = 0;
    int    waveClearTimer = 0;
    int    screenShake = 0;
    boolean slowTime   = false;
    int     slowTimer  = 0;

    // Upgrade shop state
    UpgradeType[] shopChoices = new UpgradeType[3];
    int           shopSelected = 0;

    // ══════════════════════════════════════════════════════════════════
    //  OBJECT POOLS
    // ══════════════════════════════════════════════════════════════════
    CopyOnWriteArrayList<Enemy>    enemies   = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Bullet>   bullets   = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<PowerUp>  powerups  = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<FloatText> floaters = new CopyOnWriteArrayList<>();

    // Background ring particles (always running)
    List<RingParticle> ringParticles = new ArrayList<>();

    // ══════════════════════════════════════════════════════════════════
    //  CANNON & INPUT
    // ══════════════════════════════════════════════════════════════════
    double cannonAngle  = -Math.PI / 2; // starts pointing up
    double cannonSpeed  = 0.055;        // rad/frame
    int    shootCd      = 0;
    int    shootRate    = 10;           // frames between shots

    boolean kLeft, kRight, kSpace, kShield, kNova;

    // ══════════════════════════════════════════════════════════════════
    //  CORE (nexus health & energy)
    // ══════════════════════════════════════════════════════════════════
    int   coreHp    = 100;
    int   maxCoreHp = 100;
    float energy    = 100;
    float maxEnergy = 100;
    float energyRegen = 0.15f;

    // Upgrades (accumulated)
    int   upgDamage  = 10;   // bullet damage
    float upgFireMul = 1.0f; // fire rate multiplier
    boolean upgSpread  = false;
    boolean upgBounce  = false;
    boolean upgPierce  = false;
    boolean rapidFire  = false; int rapidTimer = 0;
    boolean multiShot  = false; int multiTimer = 0;
    boolean shieldActive = false; int shieldTimer = 0;

    // Regen
    boolean hasRegen = false;
    int     regenTick = 0;

    // ══════════════════════════════════════════════════════════════════
    //  BACKGROUND AURORA RING SYSTEM
    // ══════════════════════════════════════════════════════════════════
    // Pre-allocated aurora layers
    float[]  auroraPhase = new float[8];
    Color[]  auroraColor = {
        new Color(0,80,160), new Color(0,120,200), new Color(0,60,140),
        new Color(20,80,180),new Color(0,100,160), new Color(0,140,200),
        new Color(10,60,150),new Color(0,90,170)
    };

    // ══════════════════════════════════════════════════════════════════
    //  SOUND, RANDOM, TIMER
    // ══════════════════════════════════════════════════════════════════
    SFX     sound  = new SFX();
    Random  rng    = new Random();
    javax.swing.Timer gameTimer;

    // ══════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════
    public NexusGuard() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        loadHiScore();
        initBackground();
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    void initBackground() {
        for (int i = 0; i < 8; i++) auroraPhase[i] = rng.nextFloat() * (float)(Math.PI * 2);
        // Ring particles
        for (int i = 0; i < 60; i++) ringParticles.add(new RingParticle());
    }

    // ══════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ══════════════════════════════════════════════════════════════════
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        // Aurora animation always runs
        for (int i = 0; i < 8; i++) auroraPhase[i] += 0.008f + i * 0.001f;
        for (RingParticle rp : ringParticles) rp.update();

        if (screen == Screen.PLAYING)    updatePlaying();
        if (screen == Screen.WAVE_CLEAR) { waveClearTimer++; if(waveClearTimer>90) showUpgrade(); }
        if (screen == Screen.UPGRADE)    updateUpgradeInput();
        repaint();
    }

    // ══════════════════════════════════════════════════════════════════
    //  UPDATE — PLAYING
    // ══════════════════════════════════════════════════════════════════
    void updatePlaying() {
        float timeScale = (slowTime && slowTimer > 0) ? 0.35f : 1.0f;
        if (slowTimer > 0 && --slowTimer <= 0) slowTime = false;

        // Cannon rotation
        if (kLeft)  cannonAngle -= cannonSpeed * timeScale;
        if (kRight) cannonAngle += cannonSpeed * timeScale;

        // Auto-fire
        if (kSpace && shootCd <= 0) fire();
        if (shootCd > 0) shootCd--;

        // Shield
        if (kShield && energy >= 1.5f) {
            shieldActive = true; shieldTimer = 10;
            energy -= 1.5f;
        }
        if (shieldTimer > 0) shieldTimer--;
        else shieldActive = false;

        // Nova bomb
        if (kNova) { novaBomb(); kNova = false; }

        // Power-up timers
        if (rapidTimer > 0 && --rapidTimer <= 0) rapidFire = false;
        if (multiTimer > 0 && --multiTimer <= 0) multiShot = false;

        // Energy regen
        energy = Math.min(maxEnergy, energy + energyRegen * timeScale);

        // HP regen
        if (hasRegen && ++regenTick >= 120) { regenTick = 0; coreHp = Math.min(maxCoreHp, coreHp+1); }

        // Combo decay
        if (comboTimer > 0 && --comboTimer <= 0) combo = 0;

        // Screen shake decay
        if (screenShake > 0) screenShake--;

        // Update enemies
        for (Enemy en : enemies) en.update(timeScale);

        // Update bullets
        List<Bullet> deadBullets = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update(timeScale);
            if (b.outOfBounds()) { deadBullets.add(b); continue; }
            // Bullet vs enemy collision (radial)
            boolean hit = false;
            for (Enemy en : enemies) {
                if (!en.alive) continue;
                if (b.hits(en)) {
                    en.hp -= b.damage;
                    if (!b.pierce) { deadBullets.add(b); hit = true; }
                    spawnHitFx(en.worldX(), en.worldY(), en.type.color, 5);
                    sound.play("hit");
                    if (en.hp <= 0) killEnemy(en);
                    if (!b.pierce) break;
                }
            }
        }
        bullets.removeAll(deadBullets);
        enemies.removeIf(en -> !en.alive);

        // Enemy reaches core
        for (Enemy en : new ArrayList<>(enemies)) {
            if (en.dist <= DANGER_R) {
                if (shieldActive) {
                    en.alive = false;
                    spawnDeathFx(en.worldX(), en.worldY(), en.type.color);
                    sound.play("shieldblock");
                    score += en.type.pts / 2;
                } else {
                    int dmg = en.type == EnemyType.BOSS ? 25 : en.type == EnemyType.TANK ? 15 : 8;
                    coreHp -= dmg;
                    en.alive = false;
                    spawnDeathFx(en.worldX(), en.worldY(), new Color(255,100,100));
                    sound.play("corehit");
                    screenShake = en.type == EnemyType.BOSS ? 22 : 10;
                    addFloat("-" + dmg, CX, CY, new Color(255,60,60));
                }
            }
        }
        enemies.removeIf(en -> !en.alive);

        // Power-ups drift toward center, player collects by aiming near
        List<PowerUp> deadP = new ArrayList<>();
        for (PowerUp p : powerups) {
            p.update(timeScale);
            if (p.dist <= CORE_R + 20) {
                applyPowerUp(p.type);
                deadP.add(p);
                sound.play("powerup");
                addFloat(p.type.label, p.worldX(), p.worldY(), p.type.color);
            }
            // Cannon tip collects
            double tipX = CX + Math.cos(cannonAngle) * (CANNON_LEN + 10);
            double tipY = CY + Math.sin(cannonAngle) * (CANNON_LEN + 10);
            double dx = p.worldX() - tipX, dy = p.worldY() - tipY;
            if (Math.sqrt(dx*dx+dy*dy) < 30) {
                applyPowerUp(p.type);
                deadP.add(p);
                sound.play("powerup");
                addFloat(p.type.label, (int)tipX, (int)tipY, p.type.color);
            }
        }
        powerups.removeAll(deadP);

        // Particles & floaters
        particles.removeIf(p -> { p.update(); return p.dead; });
        floaters.removeIf(f -> { f.update(); return f.dead; });

        // Core HP check
        if (coreHp <= 0) {
            coreHp = 0;
            sound.play("gameover");
            if (score > hiScore) { hiScore = score; saveHiScore(); }
            screen = Screen.GAME_OVER;
            return;
        }

        // Wave clear check
        if (enemies.isEmpty() && powerups.isEmpty()) {
            waveDelay++;
            if (waveDelay > 50) {
                waveDelay = 0;
                if (wave >= 10) {
                    if (score > hiScore) { hiScore = score; saveHiScore(); }
                    screen = Screen.VICTORY;
                    sound.play("victory");
                } else {
                    screen = Screen.WAVE_CLEAR;
                    waveClearTimer = 0;
                    sound.play("waveclear");
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  FIRE
    // ══════════════════════════════════════════════════════════════════
    void fire() {
        int rate = (int)((rapidFire ? 4 : shootRate) / upgFireMul);
        shootCd = Math.max(2, rate);

        int dmg = upgDamage;
        boolean pierce = upgPierce;
        boolean bounce = upgBounce;

        if (multiShot || upgSpread) {
            // 3-way spread
            double[] angles = {cannonAngle - 0.20, cannonAngle, cannonAngle + 0.20};
            for (double a : angles)
                bullets.add(new Bullet(cannonAngle, a, dmg, pierce, bounce));
        } else {
            bullets.add(new Bullet(cannonAngle, cannonAngle, dmg, pierce, bounce));
        }
        sound.play("shoot");
    }

    void novaBomb() {
        if (energy < 40) { addFloat("NOT ENOUGH ENERGY!", CX, CY-60, new Color(255,80,80)); return; }
        energy -= 40;
        for (Enemy en : enemies) {
            score += en.type.pts * (combo > 1 ? combo : 1);
            spawnDeathFx(en.worldX(), en.worldY(), en.type.color);
            en.alive = false;
        }
        enemies.clear();
        // Big flash ring
        for (int i = 0; i < 60; i++) {
            double a = Math.random() * Math.PI * 2;
            double r = Math.random() * ARENA_R;
            particles.add(new Particle((int)(CX+Math.cos(a)*r),
                                       (int)(CY+Math.sin(a)*r),
                    new Color(255,200,60),(float)(Math.cos(a)*8),(float)(Math.sin(a)*8),
                    20+rng.nextInt(20)));
        }
        screenShake = 25;
        sound.play("nova");
        addFloat("NOVA BOMB!", CX, CY, new Color(255,200,60));
    }

    void killEnemy(Enemy en) {
        en.alive = false;
        combo++;
        comboTimer = 120;
        if (combo > maxCombo) maxCombo = combo;
        int pts = en.type.pts * wave * (combo > 1 ? combo : 1);
        score += pts;
        spawnDeathFx(en.worldX(), en.worldY(), en.type.color);
        sound.play("die");
        screenShake = en.type == EnemyType.BOSS ? 18 : 4;
        String txt = combo > 2 ? "x"+combo+" COMBO! +"+pts : "+"+pts;
        addFloat(txt, en.worldX(), en.worldY(),
                combo > 4 ? new Color(255,215,0) :
                combo > 2 ? new Color(255,180,60) : Color.WHITE);
        // Drop power-up
        double dropChance = en.type == EnemyType.BOSS ? 0.98 : 0.20;
        if (Math.random() < dropChance) {
            PowerUpType[] all = PowerUpType.values();
            powerups.add(new PowerUp(en.angle, en.dist - 10,
                    all[rng.nextInt(all.length)]));
        }
        // Splitter spawns two drones
        if (en.type == EnemyType.SPLITTER) {
            enemies.add(new Enemy(en.angle - 0.25, en.dist, EnemyType.DRONE));
            enemies.add(new Enemy(en.angle + 0.25, en.dist, EnemyType.DRONE));
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  WAVE SPAWNING
    // ══════════════════════════════════════════════════════════════════
    void spawnWave() {
        enemies.clear(); bullets.clear();
        waveDelay = 0; combo = 0;

        boolean isBossWave = (wave % 5 == 0 && wave > 0);

        if (isBossWave) {
            // Boss + escorts
            enemies.add(new Enemy(Math.random() * Math.PI * 2, ARENA_R, EnemyType.BOSS));
            int escorts = 3 + wave / 5;
            for (int i = 0; i < escorts; i++)
                enemies.add(new Enemy(Math.PI * 2 * i / escorts, ARENA_R, EnemyType.DRONE));
            sound.play("bosswave");
            addFloat("!! BOSS WAVE !!", CX, CY - 80, new Color(255,40,40));
        } else {
            // Regular wave — more enemies as wave increases
            int drones   = 4 + wave * 2;
            int speeders = wave >= 2 ? wave       : 0;
            int tanks    = wave >= 3 ? wave - 1   : 0;
            int zigzags  = wave >= 4 ? wave - 2   : 0;
            int splitters= wave >= 5 ? wave - 3   : 0;
            int ghosts   = wave >= 7 ? wave - 5   : 0;

            spawnRing(EnemyType.DRONE,    drones);
            spawnRing(EnemyType.SPEEDER,  speeders);
            spawnRing(EnemyType.TANK,     tanks);
            spawnRing(EnemyType.ZIGZAG,   zigzags);
            spawnRing(EnemyType.SPLITTER, splitters);
            spawnRing(EnemyType.GHOST,    ghosts);
        }
    }

    void spawnRing(EnemyType type, int count) {
        if (count <= 0) return;
        double offset = rng.nextDouble() * Math.PI * 2;
        for (int i = 0; i < count; i++) {
            double angle = offset + Math.PI * 2 * i / count;
            double spawnDist = ARENA_R - 10 + rng.nextDouble() * 40;
            enemies.add(new Enemy(angle, spawnDist, type));
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  UPGRADE SHOP
    // ══════════════════════════════════════════════════════════════════
    void showUpgrade() {
        screen = Screen.UPGRADE;
        shopSelected = 0;
        UpgradeType[] all = UpgradeType.values();
        List<UpgradeType> pool = new ArrayList<>(Arrays.asList(all));
        Collections.shuffle(pool, rng);
        for (int i = 0; i < 3; i++) shopChoices[i] = pool.get(i);
    }

    void applyUpgrade(UpgradeType t) {
        switch (t) {
            case DAMAGE -> upgDamage = (int)(upgDamage * 1.25);
            case FIRE   -> upgFireMul *= 1.20f;
            case MAXHP  -> { maxCoreHp += 20; coreHp = Math.min(coreHp+20, maxCoreHp); }
            case MAXNRG -> { maxEnergy += 30; energy = Math.min(energy+30, maxEnergy); }
            case REGEN  -> hasRegen = true;
            case SPREAD -> upgSpread = true;
            case BOUNCER-> upgBounce = true;
        }
        sound.play("upgrade");
        wave++;
        spawnWave();
        screen = Screen.PLAYING;
    }

    void applyPowerUp(PowerUpType t) {
        switch (t) {
            case HEALTH -> coreHp = Math.min(maxCoreHp, coreHp + 25);
            case ENERGY -> energy = Math.min(maxEnergy, energy + 40);
            case RAPID  -> { rapidFire = true; rapidTimer = 400; }
            case PIERCE -> upgPierce = true;
            case SHIELD -> { shieldActive = true; shieldTimer = 200; }
            case MULTI  -> { multiShot = true; multiTimer = 350; }
            case NOVA   -> { energy = Math.min(maxEnergy, energy + 50); }
            case SLOW   -> { slowTime = true; slowTimer = 300; }
        }
    }

    void updateUpgradeInput() {
        // Handled in keyPressed
    }

    // ══════════════════════════════════════════════════════════════════
    //  FX HELPERS
    // ══════════════════════════════════════════════════════════════════
    void spawnHitFx(int x, int y, Color c, int n) {
        for (int i=0;i<n;i++) particles.add(new Particle(x,y,c,
            (float)(rng.nextGaussian()*3),(float)(rng.nextGaussian()*3),12+rng.nextInt(8)));
    }
    void spawnDeathFx(int x, int y, Color c) {
        for (int i=0;i<30;i++) particles.add(new Particle(x,y,c,
            (float)(rng.nextGaussian()*6),(float)(rng.nextGaussian()*6),18+rng.nextInt(14)));
        for (int i=0;i<12;i++) particles.add(new Particle(x,y,Color.WHITE,
            (float)(rng.nextGaussian()*9),(float)(rng.nextGaussian()*9),10+rng.nextInt(8)));
    }
    void addFloat(String t, int x, int y, Color c) {
        floaters.add(new FloatText(t,x,y,c));
    }

    // ══════════════════════════════════════════════════════════════════
    //  PAINT
    // ══════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Screen shake
        if (screenShake > 0) {
            g.translate(rng.nextInt(screenShake*2+1)-screenShake,
                        rng.nextInt(screenShake*2+1)-screenShake);
        }

        switch (screen) {
            case MENU     -> drawMenu(g);
            case PLAYING  -> { drawArena(g); drawHUD(g); }
            case PAUSED   -> { drawArena(g); drawHUD(g); drawOverlay(g,"PAUSED",
                              new Color(0,200,255),"P / ESC — Resume"); }
            case WAVE_CLEAR->{ drawArena(g); drawHUD(g);
                              drawWaveClear(g); }
            case UPGRADE  -> drawUpgradeShop(g);
            case GAME_OVER -> drawGameOver(g);
            case VICTORY  -> drawVictory(g);
            case SCORES   -> drawScores(g);
        }
    }

    // ─── SHARED BACKGROUND AURORA ─────────────────────────────────────
    void drawAuroraBackground(Graphics2D g) {
        // Deep space gradient
        RadialGradientPaint bg = new RadialGradientPaint(
            new Point2D.Float(CX, CY), W * 0.75f,
            new float[]{0f, 0.4f, 1f},
            new Color[]{new Color(8,4,24), new Color(4,8,20), new Color(2,2,12)});
        g.setPaint(bg); g.fillRect(0,0,W,H);

        // Aurora rings (multiple translucent sine-wave rings)
        for (int r = 0; r < 8; r++) {
            float baseR = 80 + r * 48;
            int pts = 180;
            int[] px = new int[pts], py = new int[pts];
            for (int i = 0; i < pts; i++) {
                double a = Math.PI * 2 * i / pts;
                double wobble = 8 * Math.sin(auroraPhase[r] + a * (2 + r % 3));
                double rr = baseR + wobble;
                px[i] = (int)(CX + Math.cos(a) * rr);
                py[i] = (int)(CY + Math.sin(a) * rr);
            }
            Color ac = auroraColor[r % auroraColor.length];
            int alpha = 12 + (int)(8 * Math.abs(Math.sin(auroraPhase[r])));
            g.setColor(new Color(ac.getRed(),ac.getGreen(),ac.getBlue(),alpha));
            g.setStroke(new BasicStroke(2.5f + r * 0.4f));
            g.drawPolyline(px, py, pts);
        }
        g.setStroke(new BasicStroke(1f));

        // Ring particles
        for (RingParticle rp : ringParticles) rp.draw(g);
    }

    // ─── MAIN ARENA ───────────────────────────────────────────────────
    void drawArena(Graphics2D g) {
        drawAuroraBackground(g);

        // ── Grid rings (range markers) ──
        g.setStroke(new BasicStroke(0.8f));
        for (int r = 80; r <= ARENA_R; r += 80) {
            float alpha = 0.08f + 0.04f * (float)Math.sin(tick*0.02 + r);
            g.setColor(new Color(0,100,200,(int)(alpha*255)));
            g.drawOval(CX-r, CY-r, r*2, r*2);
            // Radius label
            g.setFont(new Font("Arial",Font.PLAIN,9));
            g.setColor(new Color(0,80,160,100));
            g.drawString(String.valueOf(r), CX+r+3, CY-3);
        }

        // ── Radial grid lines (like compass) ──
        g.setStroke(new BasicStroke(0.5f));
        for (int i = 0; i < 24; i++) {
            double a = Math.PI * 2 * i / 24;
            g.setColor(new Color(0,60,120,40));
            g.drawLine(CX + (int)(Math.cos(a)*CORE_R*1.4),
                       CY + (int)(Math.sin(a)*CORE_R*1.4),
                       CX + (int)(Math.cos(a)*ARENA_R),
                       CY + (int)(Math.sin(a)*ARENA_R));
        }
        g.setStroke(new BasicStroke(1f));

        // ── Danger zone ring ──
        float dPulse = (float)(0.6+0.4*Math.sin(tick*0.1));
        g.setColor(new Color(255,60,60,(int)(dPulse*60)));
        g.setStroke(new BasicStroke(2.5f));
        g.drawOval(CX-DANGER_R, CY-DANGER_R, DANGER_R*2, DANGER_R*2);
        g.setStroke(new BasicStroke(1f));

        // ── Outer boundary ring ──
        g.setColor(new Color(0,120,255,100));
        g.setStroke(new BasicStroke(2f));
        g.drawOval(CX-ARENA_R, CY-ARENA_R, ARENA_R*2, ARENA_R*2);
        g.setStroke(new BasicStroke(1f));

        // ── Power-ups ──
        for (PowerUp p : powerups) p.draw(g);

        // ── Enemies ──
        for (Enemy en : enemies) en.draw(g);

        // ── Bullets ──
        for (Bullet b : bullets) b.draw(g);

        // ── Particles ──
        for (Particle p : particles) p.draw(g);

        // ── Float texts ──
        for (FloatText f : floaters) f.draw(g);

        // ── Nexus Core ──
        drawNexusCore(g);

        // ── Cannon ──
        drawCannon(g);
    }

    void drawNexusCore(Graphics2D g) {
        float hpRatio = (float)coreHp / maxCoreHp;
        float pulse   = (float)(0.7 + 0.3 * Math.sin(tick * 0.12));

        // Outer glow rings
        for (int r = CORE_R + 24; r >= CORE_R; r -= 8) {
            float a = (float)(CORE_R + 24 - r) / 24f;
            Color gc = hpRatio > 0.5f ?
                new Color(0,120,255,(int)(a*pulse*80)) :
                hpRatio > 0.25f ?
                new Color(255,180,0,(int)(a*pulse*80)) :
                new Color(255,40,40,(int)(a*pulse*120));
            g.setColor(gc);
            g.fillOval(CX-r, CY-r, r*2, r*2);
        }

        // Shield bubble
        if (shieldActive) {
            float sa = Math.min(1f, shieldTimer / 60f);
            g.setColor(new Color(0,255,180,(int)(sa*60)));
            g.fillOval(CX-CORE_R-14,CY-CORE_R-14,(CORE_R+14)*2,(CORE_R+14)*2);
            g.setColor(new Color(0,255,180,(int)(sa*200)));
            g.setStroke(new BasicStroke(2.5f));
            g.drawOval(CX-CORE_R-14,CY-CORE_R-14,(CORE_R+14)*2,(CORE_R+14)*2);
            g.setStroke(new BasicStroke(1f));
        }

        // Core body — multi-layer
        Color coreInner = hpRatio > 0.5f ? new Color(20,80,200) :
                          hpRatio > 0.25f? new Color(180,80,0) : new Color(180,20,20);
        Color coreOuter = hpRatio > 0.5f ? new Color(0,160,255) :
                          hpRatio > 0.25f? new Color(255,160,0) : new Color(255,60,60);

        RadialGradientPaint rp = new RadialGradientPaint(
            new Point2D.Float(CX, CY), CORE_R,
            new float[]{0f, 0.6f, 1f},
            new Color[]{Color.WHITE, coreInner, coreOuter});
        g.setPaint(rp);
        g.fillOval(CX-CORE_R, CY-CORE_R, CORE_R*2, CORE_R*2);

        // Inner detail rings
        g.setColor(new Color(255,255,255,60));
        g.setStroke(new BasicStroke(1f));
        g.drawOval(CX-CORE_R+6, CY-CORE_R+6, (CORE_R-6)*2, (CORE_R-6)*2);
        g.drawOval(CX-CORE_R+14, CY-CORE_R+14, (CORE_R-14)*2, (CORE_R-14)*2);

        // Spinning inner cross
        Graphics2D g2 = (Graphics2D)g.create();
        g2.translate(CX,CY);
        g2.rotate(tick * 0.03);
        g2.setColor(new Color(255,255,255,100));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(-CORE_R+8,0,CORE_R-8,0);
        g2.drawLine(0,-CORE_R+8,0,CORE_R-8);
        g2.dispose();

        // "NEXUS" label
        g.setFont(new Font("Arial",Font.BOLD,11));
        g.setColor(new Color(255,255,255,200));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("NEXUS", CX-fm.stringWidth("NEXUS")/2, CY+4);
    }

    void drawCannon(Graphics2D g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.translate(CX, CY);
        g2.rotate(cannonAngle);

        // Barrel shadow
        g2.setColor(new Color(0,40,80,100));
        g2.fillRoundRect(-6, 4, 12, CANNON_LEN+4, 4, 4);

        // Barrel glow
        float pulse = (float)(0.5+0.5*Math.sin(tick*0.15));
        g2.setColor(new Color(0,160,255,(int)(pulse*60)));
        g2.fillRoundRect(-9,-2,18,CANNON_LEN+6,6,6);

        // Barrel body
        GradientPaint bpaint = new GradientPaint(-6,0,new Color(20,80,180),6,0,new Color(0,160,255));
        g2.setPaint(bpaint);
        g2.fillRoundRect(-6,0,12,CANNON_LEN,4,4);

        // Detail stripes
        g2.setColor(new Color(0,200,255,140));
        g2.setStroke(new BasicStroke(1.5f));
        for (int s=12;s<CANNON_LEN-10;s+=14) g2.drawLine(-4,s,4,s);

        // Tip charge glow
        g2.setColor(rapidFire ? new Color(255,220,0,200) :
                    multiShot ? new Color(180,80,255,200) :
                    new Color(0,220,255,(int)(pulse*220)));
        g2.fillOval(-6, CANNON_LEN-6, 12, 12);
        g2.setColor(Color.WHITE);
        g2.fillOval(-3, CANNON_LEN-3, 6, 6);

        // Base mount
        g2.setColor(new Color(30,90,180));
        g2.fillOval(-10,-10,20,20);
        g2.setColor(new Color(0,180,255));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(-10,-10,20,20);

        g2.dispose();
    }

    // ─── HUD ──────────────────────────────────────────────────────────
    void drawHUD(Graphics2D g) {
        // Top strip
        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,W,58);
        g.setColor(new Color(0,100,200,80));
        g.fillRect(0,56,W,2);

        // Score
        g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(new Color(100,160,220));
        g.drawString("SCORE",12,15);
        g.setFont(new Font("Arial",Font.BOLD,22)); g.setColor(Color.WHITE);
        g.drawString(String.format("%09d",score),12,40);

        // Wave
        g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(new Color(100,160,220));
        g.drawString("WAVE", W/2-20, 15);
        g.setFont(new Font("Arial",Font.BOLD,28));
        g.setColor(waveColor());
        String wt=String.valueOf(wave)+"/10";
        FontMetrics fm=g.getFontMetrics();
        g.drawString(wt, W/2-fm.stringWidth(wt)/2, 44);

        // Hi-Score
        g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(new Color(255,215,0));
        g.drawString("BEST",W-100,15);
        g.setFont(new Font("Arial",Font.BOLD,16)); g.setColor(new Color(255,215,0));
        g.drawString(String.format("%,d",hiScore),W-110,38);

        // Combo
        if (combo > 1) {
            float ca = Math.min(1f, comboTimer/60f);
            g.setFont(new Font("Arial",Font.BOLD,22));
            Color cc = combo>8?new Color(255,215,0):combo>4?new Color(255,180,60):new Color(200,220,255);
            g.setColor(new Color(cc.getRed(),cc.getGreen(),cc.getBlue(),(int)(ca*255)));
            String cs = "x"+combo+" COMBO!";
            fm=g.getFontMetrics();
            g.drawString(cs, (W-fm.stringWidth(cs))/2, 88);
        }

        // Bottom strip — bars
        g.setColor(new Color(0,0,0,180));
        g.fillRect(0, H-50, W, 50);
        g.setColor(new Color(0,100,200,80));
        g.fillRect(0, H-50, W, 2);

        // HP bar
        drawHUDBar(g, 12, H-38, 200, 14,
                coreHp, maxCoreHp,
                new Color(255,80,100), new Color(255,140,160), "NEXUS HP");
        // Energy bar
        drawHUDBar(g, 12, H-20, 200, 12,
                (int)energy, (int)maxEnergy,
                new Color(0,160,255), new Color(60,200,255), "ENERGY");

        // Active bonuses row
        int bx = 230, by = H-38;
        if (rapidFire)    drawMiniTag(g, bx,    by, "RAPID",  new Color(255,220,0));
        if (multiShot)    drawMiniTag(g, bx+70, by, "3-WAY",  new Color(180,80,255));
        if (upgPierce)    drawMiniTag(g, bx+140,by, "PIERCE", new Color(255,120,60));
        if (upgBounce)    drawMiniTag(g, bx+210,by, "BOUNCE", new Color(0,220,255));
        if (upgSpread)    drawMiniTag(g, bx+280,by, "SPREAD", new Color(200,80,255));
        if (slowTime)     drawMiniTag(g, bx+350,by, "SLOW",   new Color(0,200,255));

        // Key hints
        g.setFont(new Font("Arial",Font.PLAIN,10)); g.setColor(new Color(60,90,130));
        g.drawString("LEFT/RIGHT: Aim   SPACE: Shoot   W: Shield   S: Nova Bomb   M: Mute   P: Pause",
                     W/2-280, H-6);
    }

    void drawHUDBar(Graphics2D g,int x,int y,int bw,int bh,
                    int val,int max,Color c1,Color c2,String label){
        g.setColor(new Color(10,10,20));
        g.fillRoundRect(x,y,bw,bh,6,6);
        float r=Math.max(0,Math.min(1f,(float)val/max));
        int fw=(int)(bw*r);
        if(fw>2){
            GradientPaint gp=new GradientPaint(x,y,c2,x+fw,y,c1);
            g.setPaint(gp); g.fillRoundRect(x,y,fw,bh,6,6);
        }
        g.setColor(new Color(80,100,140,180));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x,y,bw,bh,6,6);
        g.setFont(new Font("Arial",Font.BOLD,9)); g.setColor(Color.WHITE);
        g.drawString(label,x+3,y+bh-2);
        g.setColor(new Color(200,220,255));
        g.drawString(val+"/"+max, x+bw+4, y+bh-2);
    }

    void drawMiniTag(Graphics2D g,int x,int y,String t,Color c){
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),50));
        g.fillRoundRect(x,y,62,14,5,5);
        g.setColor(c); g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x,y,62,14,5,5);
        g.setFont(new Font("Arial",Font.BOLD,9)); g.setColor(Color.WHITE);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(t,x+(62-fm.stringWidth(t))/2,y+10);
    }

    Color waveColor(){
        return wave<=3?new Color(80,200,255):wave<=6?new Color(255,200,60):
               wave<=8?new Color(255,120,40):new Color(255,60,60);
    }

    // ─── MENU ─────────────────────────────────────────────────────────
    void drawMenu(Graphics2D g) {
        drawAuroraBackground(g);

        // Draw a demo arena ring
        g.setColor(new Color(0,80,160,40));
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(CX-ARENA_R,CY-ARENA_R,ARENA_R*2,ARENA_R*2);
        g.setStroke(new BasicStroke(1f));

        // Rotating demo enemies
        for (int i=0;i<8;i++) {
            double a=Math.PI*2*i/8 + tick*0.01;
            int ex=(int)(CX+Math.cos(a)*280), ey=(int)(CY+Math.sin(a)*280);
            Color c=EnemyType.values()[i%5].color;
            float pls=(float)(0.6+0.4*Math.sin(tick*0.08+i));
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(pls*160)));
            g.fillOval(ex-10,ey-10,20,20);
        }

        // Rotating cannon preview
        Graphics2D gp=(Graphics2D)g.create();
        gp.translate(CX,CY); gp.rotate(tick*0.02);
        gp.setColor(new Color(0,140,255,200));
        gp.fillRoundRect(-4,0,8,55,3,3);
        gp.setColor(new Color(0,220,255));
        gp.fillOval(-5,50,10,10);
        gp.dispose();

        // Core
        RadialGradientPaint cp=new RadialGradientPaint(new Point2D.Float(CX,CY),CORE_R,
            new float[]{0f,0.6f,1f},
            new Color[]{Color.WHITE,new Color(20,80,200),new Color(0,120,255)});
        g.setPaint(cp); g.fillOval(CX-CORE_R,CY-CORE_R,CORE_R*2,CORE_R*2);

        // Title
        drawGlowTitle(g,"NEXUS",new Color(0,200,255), CX,180,72);
        drawGlowTitle(g,"GUARD",new Color(255,140,0),  CX,250,52);

        g.setFont(new Font("Arial",Font.ITALIC,14));
        g.setColor(new Color(140,160,200));
        String sub="C I R C U L A R   A R E N A   D E F E N D E R";
        FontMetrics fm=g.getFontMetrics();
        g.drawString(sub,(W-fm.stringWidth(sub))/2,278);

        // Hi-score
        g.setFont(new Font("Arial",Font.BOLD,15));
        g.setColor(new Color(255,215,0));
        String hs="BEST SCORE: "+String.format("%,d",hiScore);
        fm=g.getFontMetrics(); g.drawString(hs,(W-fm.stringWidth(hs))/2,306);

        // Buttons
        float pulse=(float)(0.6+0.4*Math.sin(tick*0.08));
        drawMenuButton(g,"PRESS  ENTER  TO  PLAY",CX,620,new Color(0,200,255),17,pulse);
        drawMenuButton(g,"H — High Scores",       CX,668,new Color(180,180,220),13,0.8f);

        // Info panel
        g.setColor(new Color(0,0,0,120));
        g.fillRoundRect(30,690,W-60,120,12,12);
        g.setColor(new Color(0,80,160,80));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(30,690,W-60,120,12,12);

        g.setFont(new Font("Arial",Font.BOLD,13)); g.setColor(new Color(0,180,255));
        g.drawString("CONTROLS",52,712);
        g.setFont(new Font("Arial",Font.PLAIN,12)); g.setColor(new Color(160,190,220));
        String[] ctrl={"LEFT / RIGHT  —  Rotate cannon",
                        "SPACE (hold)  —  Auto-fire",
                        "W / UP        —  Shield (costs energy)",
                        "S / DOWN      —  Nova Bomb (costs energy)",
                        "Survive 10 waves and collect upgrades between waves!"};
        for (int i=0;i<ctrl.length;i++) {
            if(i==2) {
                g.setFont(new Font("Arial",Font.PLAIN,12));
                g.drawString(ctrl[i], 52, 730+i*16);
            } else g.drawString(ctrl[i],52,730+i*16);
        }

        // Credits
        g.setFont(new Font("Arial",Font.ITALIC,11));
        g.setColor(new Color(60,90,130));
        String cr="Nova Strike — Advanced Java Programming | Dr. NTEZIRIZA NKERABAHIZI Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-8);
    }

    void drawGlowTitle(Graphics2D g, String text, Color c, int cx, int y, int size) {
        for (int gl=5;gl>=0;gl--) {
            float a=(float)(0.5+0.5*Math.sin(tick*0.06));
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(a*(18+gl*8))));
            g.setFont(new Font("Arial",Font.BOLD,size+gl*2));
            FontMetrics fm=g.getFontMetrics();
            g.drawString(text,(cx-fm.stringWidth(text)/2)+gl/2,y+gl/2);
        }
        g.setFont(new Font("Arial",Font.BOLD,size));
        GradientPaint gp=new GradientPaint(cx-100,y-size,c.brighter(),cx+100,y,c);
        g.setPaint(gp);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(text,cx-fm.stringWidth(text)/2,y);
    }

    void drawMenuButton(Graphics2D g,String text,int cx,int cy,Color c,int fs,float glow){
        g.setFont(new Font("Arial",Font.BOLD,fs));
        FontMetrics fm=g.getFontMetrics(); int tw=fm.stringWidth(text);
        int bx=cx-tw/2-16,by=cy-fs,bw=tw+32,bh=fs+14;
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*50+10)));
        g.fillRoundRect(bx,by,bw,bh,10,10);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*200+30)));
        g.setStroke(new BasicStroke(1.5f)); g.drawRoundRect(bx,by,bw,bh,10,10);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*240+20)));
        g.drawString(text,cx-tw/2,cy);
        g.setStroke(new BasicStroke(1f));
    }

    // ─── WAVE CLEAR ───────────────────────────────────────────────────
    void drawWaveClear(Graphics2D g) {
        float alpha=Math.min(1f,waveClearTimer/40f);
        g.setColor(new Color(0,0,0,(int)(alpha*140))); g.fillRect(0,0,W,H);
        float p=(float)(0.7+0.3*Math.sin(tick*0.12));
        g.setFont(new Font("Arial",Font.BOLD,48));
        g.setColor(new Color(0,(int)(220*p),100,(int)(alpha*255)));
        String t="WAVE "+wave+" CLEAR!"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,H/2-40);
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.setColor(new Color(255,255,255,(int)(alpha*200)));
        String sc="Score: "+String.format("%,d",score);
        fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,H/2+8);
        g.setFont(new Font("Arial",Font.ITALIC,14));
        g.setColor(new Color(160,200,255,(int)(alpha*180)));
        String u="Preparing upgrade shop..."; fm=g.getFontMetrics();
        g.drawString(u,(W-fm.stringWidth(u))/2,H/2+40);
    }

    // ─── UPGRADE SHOP ─────────────────────────────────────────────────
    void drawUpgradeShop(Graphics2D g) {
        drawAuroraBackground(g);
        g.setColor(new Color(0,0,0,160)); g.fillRect(0,0,W,H);

        drawGlowTitle(g,"UPGRADE",new Color(255,200,0),CX,100,40);

        g.setFont(new Font("Arial",Font.ITALIC,14));
        g.setColor(new Color(180,200,220));
        String sub="Use LEFT/RIGHT to select, ENTER to confirm";
        FontMetrics fm=g.getFontMetrics();
        g.drawString(sub,(W-fm.stringWidth(sub))/2,132);

        // Three upgrade cards
        int cardW=220, cardH=280, gap=30;
        int totalW=3*cardW+2*gap;
        int startX=(W-totalW)/2;

        for (int i=0;i<3;i++) {
            int cx2=startX+i*(cardW+gap);
            boolean sel=(i==shopSelected);
            UpgradeType ut=shopChoices[i];
            Color uc=ut.color;

            // Card bg
            float glc=sel?0.25f:0.10f;
            g.setColor(new Color(uc.getRed(),uc.getGreen(),uc.getBlue(),(int)(glc*255)));
            g.fillRoundRect(cx2,160,cardW,cardH,16,16);

            // Border
            float borderAlpha=sel?1f:0.4f;
            g.setColor(new Color(uc.getRed(),uc.getGreen(),uc.getBlue(),(int)(borderAlpha*255)));
            g.setStroke(new BasicStroke(sel?3f:1.5f));
            g.drawRoundRect(cx2,160,cardW,cardH,16,16);
            g.setStroke(new BasicStroke(1f));

            // Icon circle
            g.setColor(new Color(uc.getRed(),uc.getGreen(),uc.getBlue(),160));
            g.fillOval(cx2+cardW/2-28,184,56,56);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial",Font.BOLD,22));
            g.drawString(String.valueOf((char)('A'+i)),cx2+cardW/2-7,220);

            // Name
            g.setFont(new Font("Arial",Font.BOLD,15));
            g.setColor(sel?Color.WHITE:new Color(200,210,230));
            String name=ut.name().replace("_"," ");
            fm=g.getFontMetrics();
            g.drawString(name,cx2+(cardW-fm.stringWidth(name))/2,264);

            // Description
            g.setFont(new Font("Arial",Font.PLAIN,13));
            g.setColor(new Color(180,200,220));
            String desc=ut.desc;
            fm=g.getFontMetrics();
            g.drawString(desc,cx2+(cardW-fm.stringWidth(desc))/2,292);

            // "SELECTED" badge
            if (sel) {
                float p=(float)(0.7+0.3*Math.sin(tick*0.15));
                g.setColor(new Color(uc.getRed(),uc.getGreen(),uc.getBlue(),(int)(p*255)));
                g.setFont(new Font("Arial",Font.BOLD,11));
                g.drawString("[ SELECTED ]", cx2+(cardW-g.getFontMetrics().stringWidth("[ SELECTED ]"))/2,380);
            }
        }

        // Key hints
        g.setFont(new Font("Arial",Font.PLAIN,13));
        g.setColor(new Color(140,160,200));
        String hint="LEFT / RIGHT — Change selection     ENTER — Confirm";
        fm=g.getFontMetrics(); g.drawString(hint,(W-fm.stringWidth(hint))/2,480);

        // Current stats
        g.setColor(new Color(0,0,0,140));
        g.fillRoundRect(30,510,W-60,120,12,12);
        g.setColor(new Color(0,80,160,80));
        g.setStroke(new BasicStroke(1f)); g.drawRoundRect(30,510,W-60,120,12,12);

        g.setFont(new Font("Arial",Font.BOLD,13)); g.setColor(new Color(0,180,255));
        g.drawString("CURRENT STATS",50,532);
        g.setFont(new Font("Arial",Font.PLAIN,12)); g.setColor(new Color(160,190,220));
        String[] stats={
            "Score: "+String.format("%,d",score)+"   Wave: "+wave+"/10",
            "Damage: "+upgDamage+"   Fire Rate: x"+String.format("%.1f",upgFireMul),
            "HP: "+coreHp+"/"+maxCoreHp+"   Energy: "+(int)energy+"/"+(int)maxEnergy,
            "Upgrades: "+(upgSpread?"SPREAD ":"")+(upgBounce?"BOUNCE ":"")+(upgPierce?"PIERCE ":"")+(hasRegen?"REGEN":"")
        };
        for (int i=0;i<stats.length;i++) g.drawString(stats[i],50,552+i*18);
    }

    // ─── OVERLAY (Pause etc.) ─────────────────────────────────────────
    void drawOverlay(Graphics2D g,String title,Color c,String subtitle){
        g.setColor(new Color(0,0,0,170)); g.fillRect(0,0,W,H);
        drawGlowTitle(g,title,c,CX,H/2-30,52);
        g.setFont(new Font("Arial",Font.PLAIN,16)); g.setColor(new Color(180,200,230));
        FontMetrics fm=g.getFontMetrics();
        g.drawString(subtitle,(W-fm.stringWidth(subtitle))/2,H/2+30);
    }

    // ─── GAME OVER ────────────────────────────────────────────────────
    void drawGameOver(Graphics2D g) {
        drawAuroraBackground(g);
        RadialGradientPaint vign=new RadialGradientPaint(new Point2D.Float(CX,CY),W*0.7f,
            new float[]{0.4f,1f},new Color[]{new Color(0,0,0,40),new Color(160,0,0,120)});
        g.setPaint(vign); g.fillRect(0,0,W,H);
        g.setColor(new Color(0,0,0,110)); g.fillRect(0,0,W,H);

        drawGlowTitle(g,"GAME  OVER",new Color(255,60,60),CX,220,58);

        g.setFont(new Font("Arial",Font.BOLD,26)); g.setColor(Color.WHITE);
        String sc="SCORE: "+String.format("%,d",score);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(sc,(W-fm.stringWidth(sc))/2,300);

        g.setFont(new Font("Arial",Font.PLAIN,16)); g.setColor(new Color(180,180,220));
        String info="Wave "+wave+"  •  Max Combo x"+maxCombo;
        fm=g.getFontMetrics(); g.drawString(info,(W-fm.stringWidth(info))/2,334);

        if (score>=hiScore && score>0) {
            float p=(float)(0.7+0.3*Math.sin(tick*0.1));
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.setColor(new Color(255,215,0,(int)(p*255)));
            String hs2="*** NEW HIGH SCORE! ***"; fm=g.getFontMetrics();
            g.drawString(hs2,(W-fm.stringWidth(hs2))/2,374);
        }

        float pulse=(float)(0.6+0.4*Math.sin(tick*0.08));
        drawMenuButton(g,"ENTER — Play Again",CX,440,new Color(0,200,100),15,pulse);
        drawMenuButton(g,"H — High Scores",   CX,488,new Color(180,180,220),13,0.8f);
        drawMenuButton(g,"ESC — Main Menu",   CX,530,new Color(160,80,80),12,0.7f);

        g.setFont(new Font("Arial",Font.ITALIC,11)); g.setColor(new Color(60,90,130));
        String cr="Nexus Guard — Advanced Java Programming | Dr. Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-14);
    }

    // ─── VICTORY ──────────────────────────────────────────────────────
    void drawVictory(Graphics2D g) {
        drawAuroraBackground(g);
        g.setColor(new Color(0,0,0,100)); g.fillRect(0,0,W,H);
        // Gold rays
        for (int i=0;i<16;i++) {
            double a=Math.toRadians(i*22.5+tick*0.4);
            g.setColor(new Color(255,215,0,14));
            g.setStroke(new BasicStroke(10));
            g.drawLine(CX,CY,(int)(CX+Math.cos(a)*700),(int)(CY+Math.sin(a)*700));
        }
        g.setStroke(new BasicStroke(1f));
        drawGlowTitle(g,"NEXUS  SAVED!",new Color(255,215,0),CX,220,46);
        g.setFont(new Font("Arial",Font.BOLD,24)); g.setColor(Color.WHITE);
        String sc="FINAL SCORE: "+String.format("%,d",score);
        FontMetrics fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,288);
        g.setFont(new Font("Arial",Font.PLAIN,16)); g.setColor(new Color(180,200,255));
        String info="Max Combo x"+maxCombo+"  •  All 10 waves cleared!";
        fm=g.getFontMetrics(); g.drawString(info,(W-fm.stringWidth(info))/2,322);
        if (score>=hiScore) {
            float p=(float)(0.7+0.3*Math.sin(tick*0.1));
            g.setFont(new Font("Arial",Font.BOLD,20));
            g.setColor(new Color(255,215,0,(int)(p*255)));
            String hs2="*** NEW HIGH SCORE! ***"; fm=g.getFontMetrics();
            g.drawString(hs2,(W-fm.stringWidth(hs2))/2,360);
        }
        float pulse=(float)(0.6+0.4*Math.sin(tick*0.08));
        drawMenuButton(g,"ENTER — Play Again",CX,430,new Color(0,220,80),16,pulse);
        drawMenuButton(g,"H — High Scores",   CX,476,new Color(255,215,0),13,0.8f);
        g.setFont(new Font("Arial",Font.ITALIC,11)); g.setColor(new Color(60,90,130));
        String cr="Nexus Guard — Advanced Java Programming | Dr. Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-14);
    }

    // ─── HIGH SCORES ──────────────────────────────────────────────────
    void drawScores(Graphics2D g) {
        drawAuroraBackground(g);
        g.setColor(new Color(0,0,0,160)); g.fillRect(0,0,W,H);
        drawGlowTitle(g,"HIGH SCORES",new Color(255,215,0),CX,120,40);
        g.setColor(new Color(0,0,0,140));
        g.fillRoundRect(CX-220,148,440,320,14,14);
        g.setColor(new Color(0,100,200,60)); g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(CX-220,148,440,320,14,14); g.setStroke(new BasicStroke(1f));
        List<int[]> scores=loadAllScores();
        for (int i=0;i<Math.min(scores.size(),8);i++) {
            Color c=i==0?new Color(255,215,0):i==1?new Color(192,192,192):
                     i==2?new Color(205,127,50):new Color(160,180,220);
            g.setFont(new Font("Arial",Font.BOLD,18)); g.setColor(c);
            String line=(i+1)+".    "+String.format("%,d",scores.get(i)[0]);
            FontMetrics fm=g.getFontMetrics();
            g.drawString(line,(W-fm.stringWidth(line))/2,192+i*36);
        }
        if(scores.isEmpty()){
            g.setFont(new Font("Arial",Font.ITALIC,14)); g.setColor(new Color(140,160,200));
            String n="No scores yet — defend the Nexus!";
            FontMetrics fm=g.getFontMetrics(); g.drawString(n,(W-fm.stringWidth(n))/2,300);
        }
        drawMenuButton(g,"ESC / ENTER — Back",CX,H-60,new Color(100,150,220),14,0.9f);
    }

    // ══════════════════════════════════════════════════════════════════
    //  INPUT
    // ══════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e) {
        int k=e.getKeyCode();
        if (k==KeyEvent.VK_LEFT||k==KeyEvent.VK_A)  kLeft  =true;
        if (k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) kRight =true;
        if (k==KeyEvent.VK_SPACE)                    kSpace =true;
        if (k==KeyEvent.VK_UP||k==KeyEvent.VK_W)    kShield=true;
        if (k==KeyEvent.VK_DOWN||k==KeyEvent.VK_S)  kNova  =true;

        switch(screen) {
            case MENU -> {
                if(k==KeyEvent.VK_ENTER){ startGame(); sound.play("start"); }
                if(k==KeyEvent.VK_H)    screen=Screen.SCORES;
            }
            case PLAYING -> {
                if(k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if(k==KeyEvent.VK_M) sound.muted=!sound.muted;
            }
            case PAUSED -> {
                if(k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER) screen=Screen.PLAYING;
                if(k==KeyEvent.VK_M) sound.muted=!sound.muted;
            }
            case UPGRADE -> {
                if(k==KeyEvent.VK_LEFT||k==KeyEvent.VK_A)   shopSelected=(shopSelected+2)%3;
                if(k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D)  shopSelected=(shopSelected+1)%3;
                if(k==KeyEvent.VK_ENTER)                     applyUpgrade(shopChoices[shopSelected]);
            }
            case GAME_OVER -> {
                if(k==KeyEvent.VK_ENTER){ startGame(); }
                if(k==KeyEvent.VK_H)    screen=Screen.SCORES;
                if(k==KeyEvent.VK_ESCAPE) screen=Screen.MENU;
            }
            case VICTORY -> {
                if(k==KeyEvent.VK_ENTER){ startGame(); }
                if(k==KeyEvent.VK_H)    screen=Screen.SCORES;
            }
            case SCORES -> {
                if(k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER) screen=Screen.MENU;
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        int k=e.getKeyCode();
        if(k==KeyEvent.VK_LEFT||k==KeyEvent.VK_A)  kLeft  =false;
        if(k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) kRight =false;
        if(k==KeyEvent.VK_SPACE)                    kSpace =false;
        if(k==KeyEvent.VK_UP||k==KeyEvent.VK_W)    kShield=false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════
    //  GAME SETUP
    // ══════════════════════════════════════════════════════════════════
    void startGame() {
        wave=1; score=0; combo=0; maxCombo=0;
        coreHp=100; maxCoreHp=100; energy=100; maxEnergy=100;
        upgDamage=10; upgFireMul=1f; upgSpread=false; upgBounce=false;
        upgPierce=false; rapidFire=false; multiShot=false; hasRegen=false;
        shieldActive=false; slowTime=false;
        enemies.clear(); bullets.clear(); powerups.clear();
        particles.clear(); floaters.clear();
        spawnWave();
        screen=Screen.PLAYING;
    }

    // ══════════════════════════════════════════════════════════════════
    //  FILE I/O
    // ══════════════════════════════════════════════════════════════════
    void loadHiScore(){ List<int[]> s=loadAllScores(); if(!s.isEmpty()) hiScore=s.get(0)[0]; }
    List<int[]> loadAllScores(){
        List<int[]> list=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(SCORE_FILE))){
            String ln; while((ln=br.readLine())!=null){
                try{list.add(new int[]{Integer.parseInt(ln.trim())});}catch(Exception ignored){}
            }
        }catch(IOException ignored){}
        list.sort((a,b)->b[0]-a[0]); return list;
    }
    void saveHiScore(){
        List<int[]> all=loadAllScores(); all.add(new int[]{score});
        all.sort((a,b)->b[0]-a[0]);
        try(PrintWriter pw=new PrintWriter(new FileWriter(SCORE_FILE))){
            for(int i=0;i<Math.min(all.size(),10);i++) pw.println(all.get(i)[0]);
        }catch(IOException ignored){}
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — ENEMY  (uses polar coordinates)
    // ══════════════════════════════════════════════════════════════════
    class Enemy implements Drawable, Updatable, Collidable {
        double angle;   // angle from center (radians)
        double dist;    // distance from center (pixels)
        int    hp, maxHp;
        EnemyType type;
        boolean alive = true;
        int    zigDir = 1;
        int    zigTimer = 0;
        float  ghostAlpha = 1f;
        boolean ghostVisible = true;
        int    ghostFlicker = 0;

        Enemy(double angle, double dist, EnemyType t) {
            this.angle = angle; this.dist = dist; this.type = t;
            this.hp = this.maxHp = t.hp;
        }

        // Polar → Cartesian
        int worldX(){ return (int)(CX + Math.cos(angle)*dist); }
        int worldY(){ return (int)(CY + Math.sin(angle)*dist); }

        @Override public double getDistFromCenter(){ return dist; }
        @Override public int    getRadius()        { return type.radius; }

        @Override public void update() { update(1f); }

        void update(float ts) {
            switch(type.ai) {
                case 0 -> dist -= type.spd * ts;                   // DRONE: straight in
                case 1 -> {                                         // SPEEDER: fast straight
                    dist -= type.spd * ts;
                    angle += 0.005 * ts;
                }
                case 2 -> dist -= type.spd * ts;                   // TANK: slow straight
                case 3 -> {                                         // ZIGZAG: zig-zag
                    dist -= type.spd * ts;
                    if(++zigTimer > 20) { zigDir=-zigDir; zigTimer=0; }
                    angle += 0.04 * zigDir * ts;
                }
                case 4 -> dist -= type.spd * ts;                   // SPLITTER: straight (splits on death)
                case 5 -> {                                         // GHOST: flickers
                    dist -= type.spd * ts;
                    if(++ghostFlicker > 30) {
                        ghostVisible=!ghostVisible; ghostFlicker=0;
                        ghostAlpha=ghostVisible?1f:0.25f;
                    }
                }
                case 6 -> {                                         // BOSS: orbit + charge
                    dist -= type.spd * ts;
                    angle += 0.008 * ts;
                }
            }
            if(dist < 0) dist = 0;
        }

        @Override public void draw(Graphics2D g) {
            int x=worldX(), y=worldY();
            float alphaScale = (type==EnemyType.GHOST) ? ghostAlpha : 1f;
            int alpha=(int)(255*alphaScale);

            // Outer glow
            Color c=type.color;
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(60*alphaScale)));
            g.fillOval(x-type.radius-6,y-type.radius-6,(type.radius+6)*2,(type.radius+6)*2);

            // Body
            if (type==EnemyType.BOSS) drawBoss(g,x,y,alpha);
            else                      drawRegular(g,x,y,alpha,c);

            // HP bar (for tougher types)
            if (type!=EnemyType.DRONE && type!=EnemyType.SPEEDER && hp<maxHp) {
                int bw=(type.radius*2+8), bx=x-bw/2, by=y-type.radius-12;
                g.setColor(new Color(0,0,0,120)); g.fillRect(bx,by,bw,5);
                float hpR=(float)hp/maxHp;
                g.setColor(hpR>0.5f?new Color(0,200,80):hpR>0.25f?new Color(255,200,0):new Color(255,60,60));
                g.fillRect(bx,by,(int)(bw*hpR),5);
            }
        }

        void drawRegular(Graphics2D g,int x,int y,int alpha,Color c){
            // Enemy body with type-specific shape
            switch(type){
                case DRONE -> {
                    GradientPaint gp=new GradientPaint(x-type.radius,y,c.darker(),x+type.radius,y,c);
                    g.setPaint(gp);
                    int[] px={x,x+type.radius,x,x-type.radius};
                    int[] py={y-type.radius,y,y+type.radius,y};
                    g.fillPolygon(px,py,4);
                    g.setColor(new Color(255,200,200,alpha)); g.fillOval(x-4,y-4,8,8);
                }
                case SPEEDER -> {
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    // Elongated in direction of travel
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.translate(x,y); g2.rotate(angle+Math.PI/2);
                    int[] px={0,type.radius/2,0,-type.radius/2};
                    int[] py={-type.radius,type.radius/2,type.radius/3,type.radius/2};
                    g2.fillPolygon(px,py,4);
                    g2.setColor(new Color(255,220,100,alpha)); g2.fillOval(-3,-3,6,6);
                    g2.dispose();
                }
                case TANK -> {
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    g.fillOval(x-type.radius,y-type.radius,type.radius*2,type.radius*2);
                    g.setColor(new Color(c.getRed()/2,c.getGreen()/2,c.getBlue()/2,alpha));
                    g.setStroke(new BasicStroke(3f)); g.drawOval(x-type.radius,y-type.radius,type.radius*2,type.radius*2);
                    g.setStroke(new BasicStroke(1f));
                    g.setColor(new Color(200,240,255,alpha)); g.fillOval(x-6,y-6,12,12);
                }
                case ZIGZAG -> {
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    int r=type.radius;
                    int[] px2={x,x+r,x+r/2,x,x-r/2,x-r};
                    int[] py2={y-r,y-r/3,y+r/2,y+r,y+r/2,y-r/3};
                    g.fillPolygon(px2,py2,6);
                    g.setColor(new Color(255,140,255,alpha)); g.fillOval(x-4,y-4,8,8);
                }
                case SPLITTER -> {
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    int r=type.radius;
                    // Octagon
                    int[] px3=new int[8]; int[] py3=new int[8];
                    for(int i=0;i<8;i++){
                        px3[i]=(int)(x+r*Math.cos(Math.PI/8+i*Math.PI/4));
                        py3[i]=(int)(y+r*Math.sin(Math.PI/8+i*Math.PI/4));
                    }
                    g.fillPolygon(px3,py3,8);
                    g.setColor(new Color(100,255,180,alpha)); g.fillOval(x-5,y-5,10,10);
                    // "SPLIT" indicator
                    g.setFont(new Font("Arial",Font.BOLD,7)); g.setColor(new Color(255,255,255,alpha));
                    g.drawString("x2",x-5,y+3);
                }
                case GHOST -> {
                    int r=type.radius;
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    g.fillOval(x-r,y-r,r*2,r*2);
                    g.setColor(new Color(255,255,255,(int)(alpha*0.6f)));
                    g.setStroke(new BasicStroke(2f));
                    g.drawOval(x-r,y-r,r*2,r*2);
                    g.setStroke(new BasicStroke(1f));
                    g.setColor(new Color(200,200,255,alpha)); g.fillOval(x-4,y-4,8,8);
                }
                default -> {
                    g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
                    g.fillOval(x-type.radius,y-type.radius,type.radius*2,type.radius*2);
                }
            }
        }

        void drawBoss(Graphics2D g,int x,int y,int alpha){
            Color c=type.color;
            float pls=(float)(0.6+0.4*Math.sin(tick*0.08));
            // Multiple glow rings
            for(int r=type.radius+20;r>=type.radius;r-=6){
                float a=(float)(type.radius+20-r)/20f;
                g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(a*pls*100)));
                g.fillOval(x-r,y-r,r*2,r*2);
            }
            // Body
            RadialGradientPaint rp=new RadialGradientPaint(new Point2D.Float(x,y),type.radius,
                new float[]{0f,0.5f,1f},
                new Color[]{Color.WHITE,new Color(200,20,100),c});
            g.setPaint(rp); g.fillOval(x-type.radius,y-type.radius,type.radius*2,type.radius*2);
            // Rotating spikes
            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(x,y); g2.rotate(tick*0.04);
            g2.setColor(new Color(255,80,160,(int)(pls*200)));
            for(int i=0;i<6;i++){
                double a2=Math.PI*2*i/6;
                int sx=(int)(Math.cos(a2)*(type.radius+14));
                int sy=(int)(Math.sin(a2)*(type.radius+14));
                g2.fillOval(sx-5,sy-5,10,10);
            }
            g2.dispose();
            // Eye
            g.setColor(new Color(255,255,0,(int)(pls*255))); g.fillOval(x-8,y-8,16,16);
            g.setColor(Color.RED); g.fillOval(x-4,y-4,8,8);
            // HP bar
            int bw=type.radius*3; int bx2=x-bw/2, by2=y-type.radius-18;
            g.setColor(new Color(0,0,0,160)); g.fillRect(bx2,by2,bw,8);
            float hpR=(float)hp/maxHp;
            g.setColor(hpR>0.5f?new Color(0,200,80):hpR>0.25f?new Color(255,180,0):new Color(255,40,40));
            g.fillRect(bx2,by2,(int)(bw*hpR),8);
            g.setFont(new Font("Arial",Font.BOLD,10)); g.setColor(Color.WHITE);
            g.drawString("BOSS "+hp+"/"+maxHp,bx2,by2-2);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — BULLET  (fired outward radially from center)
    // ══════════════════════════════════════════════════════════════════
    class Bullet implements Drawable {
        float x, y, vx, vy;
        int   damage;
        boolean pierce, bounce, bounced;

        Bullet(double fromAngle, double travelAngle, int dmg, boolean pierce, boolean bounce) {
            // Start just outside the cannon tip
            this.x = (float)(CX + Math.cos(fromAngle) * (CANNON_LEN + 8));
            this.y = (float)(CY + Math.sin(fromAngle) * (CANNON_LEN + 8));
            float spd = 12f;
            this.vx = (float)(Math.cos(travelAngle) * spd);
            this.vy = (float)(Math.sin(travelAngle) * spd);
            this.damage = dmg;
            this.pierce = pierce;
            this.bounce = bounce;
        }

        void update(float ts){ x+=vx*ts; y+=vy*ts; }
        boolean outOfBounds(){ return x<0||x>W||y<0||y>H; }

        boolean hits(Enemy en){
            if(en.type==EnemyType.GHOST && !en.ghostVisible) return false;
            double dx=x-en.worldX(), dy=y-en.worldY();
            return Math.sqrt(dx*dx+dy*dy) < en.type.radius+4;
        }

        @Override public void draw(Graphics2D g){
            // Glow trail
            g.setColor(new Color(0,180,255,60));
            g.fillOval((int)x-6,(int)y-6,12,12);
            // Core
            if(pierce){
                g.setColor(new Color(255,120,60,220));
                g.fillRoundRect((int)x-4,(int)y-10,8,20,4,4);
            } else {
                g.setColor(new Color(0,220,255,220));
                g.fillOval((int)x-4,(int)y-4,8,8);
            }
            g.setColor(new Color(255,255,255,200));
            g.fillOval((int)x-2,(int)y-2,4,4);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — POWERUP  (drifts inward, player collects at tip)
    // ══════════════════════════════════════════════════════════════════
    class PowerUp implements Drawable {
        double angle, dist;
        PowerUpType type;
        float spinAngle = 0;

        PowerUp(double angle, double dist, PowerUpType t){
            this.angle=angle; this.dist=dist; this.type=t;
        }

        void update(float ts){ dist -= 0.5f * ts; spinAngle += 0.06f; }
        int worldX(){ return (int)(CX + Math.cos(angle)*dist); }
        int worldY(){ return (int)(CY + Math.sin(angle)*dist); }
        Rectangle2D hitbox(){ return new Rectangle2D.Double(worldX()-12,worldY()-12,24,24); }

        @Override public void draw(Graphics2D g){
            int x=worldX(), y=worldY();
            Color c=type.color;
            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(x,y); g2.rotate(spinAngle);

            float pls=(float)(0.5+0.5*Math.sin(spinAngle*3));
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(pls*100)));
            g2.fillOval(-16,-16,32,32);

            int[] px={0,11,0,-11}; int[] py={-11,0,11,0};
            g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),200));
            g2.fillPolygon(px,py,4);
            g2.setColor(c.brighter()); g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(px,py,4);
            g2.setFont(new Font("Arial",Font.BOLD,7)); g2.setColor(Color.WHITE);
            FontMetrics fm=g2.getFontMetrics();
            String lbl=type.label.substring(0,Math.min(5,type.label.length()));
            g2.drawString(lbl,-fm.stringWidth(lbl)/2,3);
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — PARTICLE
    // ══════════════════════════════════════════════════════════════════
    class Particle implements Drawable {
        float x,y,vx,vy; Color c; int life,maxLife; boolean dead;
        Particle(int x,int y,Color c,float vx,float vy,int life){
            this.x=x;this.y=y;this.c=c;this.vx=vx;this.vy=vy;this.life=this.maxLife=life;
        }
        void update(){ x+=vx;y+=vy;vx*=0.90f;vy*=0.90f;if(--life<=0)dead=true; }
        @Override public void draw(Graphics2D g){
            float a=(float)life/maxLife;
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(a*220)));
            g.fillOval((int)(x-2),(int)(y-2),4,4);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — FLOAT TEXT
    // ══════════════════════════════════════════════════════════════════
    class FloatText implements Drawable {
        String text; float x,y; Color c; float life=1f; boolean dead;
        FloatText(String t,int x,int y,Color c){text=t;this.x=x;this.y=y;this.c=c;}
        void update(){ y-=1.4f; life-=0.018f; if(life<=0)dead=true; }
        @Override public void draw(Graphics2D g){
            g.setFont(new Font("Arial",Font.BOLD,15));
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(life*240)));
            FontMetrics fm=g.getFontMetrics();
            g.drawString(text,(int)x-fm.stringWidth(text)/2,(int)y);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — RING PARTICLE  (background ambience)
    // ══════════════════════════════════════════════════════════════════
    class RingParticle {
        double angle, dist, speed;
        int    size, alpha;
        Color  color;

        RingParticle() { reset(); }
        void reset() {
            angle  = Math.random() * Math.PI * 2;
            dist   = 60 + Math.random() * (ARENA_R - 40);
            speed  = 0.003 + Math.random() * 0.008;
            size   = 1 + rng.nextInt(3);
            alpha  = 20 + rng.nextInt(60);
            int[] rs={0,60,120,160,200}; int[] gs={80,100,140,160,200}; int[] bs={160,180,200,220,255};
            int i=rng.nextInt(5);
            color  = new Color(rs[i],gs[i],bs[i]);
        }
        void update() { angle += speed; }
        void draw(Graphics2D g) {
            int x=(int)(CX+Math.cos(angle)*dist);
            int y=(int)(CY+Math.sin(angle)*dist);
            g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
            g.fillOval(x-size,y-size,size*2,size*2);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASS — SOUND ENGINE (pure Java synthesis)
    // ══════════════════════════════════════════════════════════════════
    static class SFX {
        static final int SR = 44100;
        boolean muted = false;

        void play(String name) {
            if (muted) return;
            byte[] pcm = gen(name); if (pcm==null) return;
            Thread t = new Thread(() -> {
                try {
                    AudioFormat fmt=new AudioFormat(SR,16,1,true,false);
                    DataLine.Info info=new DataLine.Info(SourceDataLine.class,fmt);
                    if(!AudioSystem.isLineSupported(info)) return;
                    try(SourceDataLine line=(SourceDataLine)AudioSystem.getLine(info)){
                        line.open(fmt,2048); line.start();
                        line.write(pcm,0,pcm.length); line.drain();
                    }
                } catch(Exception ignored){}
            });
            t.setDaemon(true); t.start();
        }

        private byte[] gen(String name){
            return switch(name){
                case "shoot"      -> synth(0.07, t->tri(t,900)*env(t,0.01,0.02,0.25,0.02)*0.42);
                case "hit"        -> synth(0.09, t->noise()*env(t,0.005,0.015,0.18,0.03)*0.45);
                case "die"        -> synth(0.26, t->(sine(t,240*Math.pow(0.3,t))+noise()*0.18)*env(t,0.01,0.04,0.38,0.1)*0.52);
                case "corehit"    -> synth(0.35, t->(sine(t,180)+noise()*0.3)*env(t,0.01,0.08,0.5,0.12)*0.65);
                case "nova"       -> synth(0.50, t->(noise()*0.8+sine(t,80+t*40)*0.5)*env(t,0.005,0.1,0.55,0.12)*0.72);
                case "shieldblock"-> synth(0.18, t->(sine(t,660)+sine(t,880)*0.4)*env(t,0.01,0.04,0.45,0.06)*0.44);
                case "powerup"    -> synth(0.28, t->arp(t)*env(t,0.01,0.05,0.62,0.08)*0.50);
                case "upgrade"    -> synth(0.55, t->fanfare(t)*env(t,0.01,0.08,0.65,0.12)*0.55);
                case "waveclear"  -> synth(0.50, t->(sine(t,440+t*100)+sine(t,660+t*60))*env(t,0.01,0.06,0.6,0.1)*0.46);
                case "bosswave"   -> synth(0.80, t->(sine(t,110)+sine(t,165)*0.5+noise()*0.15)*env(t,0.02,0.12,0.62,0.14)*0.60);
                case "gameover"   -> synth(0.75, t->sine(t,320-t*140)*env(t,0.01,0.1,0.52,0.14)*0.52);
                case "victory"    -> synth(0.90, t->fanfare(t)*env(t,0.01,0.1,0.7,0.1)*0.55);
                case "start"      -> synth(0.42, t->(sine(t,440+t*110)+sine(t,660))*env(t,0.01,0.05,0.58,0.1)*0.44);
                default           -> null;
            };
        }

        static double sine(double t,double f){ return Math.sin(2*Math.PI*f*t); }
        static double tri(double t,double f){ double p=(t*f)%1; return p<0.5?4*p-1:3-4*p; }
        static final Random NR=new Random();
        static double noise(){ return NR.nextDouble()*2-1; }
        static double env(double t,double a,double d,double s,double r){
            if(t<a) return t/a;
            if(t<a+d) return 1-(1-0.7)*((t-a)/d);
            if(t<1.0-r) return 0.7;
            return 0.7*(1-(t-(1.0-r))/r);
        }
        static double arp(double t){
            double[] f={330,415,494,622,784}; int i=Math.min((int)(t*f.length),f.length-1);
            return sine(t,f[i])+sine(t,f[i]*2)*0.25;
        }
        static double fanfare(double t){
            double[] f={330,440,554,659,880}; int i=Math.min((int)(t*f.length),f.length-1);
            return sine(t,f[i])+sine(t,f[i]*1.5)*0.4+sine(t,f[i]*2)*0.2;
        }
        private byte[] synth(double dur,DoubleUnaryOperator fn){
            int n=(int)(SR*dur); byte[] buf=new byte[n*2];
            for(int i=0;i<n;i++){
                double t=(double)i/n;
                int s=(int)(fn.applyAsDouble(t)*28000);
                s=Math.max(-32768,Math.min(32767,s));
                buf[2*i]=(byte)(s&0xFF); buf[2*i+1]=(byte)((s>>8)&0xFF);
            }
            return buf;
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  UTILITY
    // ══════════════════════════════════════════════════════════════════
    static float clamp(float v,float lo,float hi){ return Math.max(lo,Math.min(hi,v)); }

    // ══════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Nexus Guard — Circular Arena Defender");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            NexusGuard game = new NexusGuard();
            f.add(game); f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}
