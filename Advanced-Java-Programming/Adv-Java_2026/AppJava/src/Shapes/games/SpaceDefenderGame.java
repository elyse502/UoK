package space.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;

/**
 * Space Defender – 5 Waves of Alien Chaos
 * An arcade space shooter with bosses, shields, and escalating alien swarms.
 */
public class SpaceDefenderGame extends JPanel implements ActionListener, KeyListener {

    // ─── SCREEN ────────────────────────────────────────────────────────────
    static final int W = 960, H = 680;

    // ─── COLOURS ───────────────────────────────────────────────────────────
    static final Color C_NEON_BLUE   = new Color( 0, 200, 255);
    static final Color C_NEON_GREEN  = new Color( 0, 255, 130);
    static final Color C_NEON_RED    = new Color(255,  60,  60);
    static final Color C_NEON_PURPLE = new Color(180,  80, 255);
    static final Color C_NEON_YELLOW = new Color(255, 230,  50);
    static final Color C_NEON_ORANGE = new Color(255, 140,   0);
    static final Color C_WHITE       = Color.WHITE;
    static final Color C_BLACK       = Color.BLACK;
    static final Color C_DARK_BG     = new Color( 5,   5,  18);

    // ─── WAVE CONFIG ───────────────────────────────────────────────────────
    static final int[]    WAVE_TARGET    = {0, 300, 700, 1400, 2400, 4000};
    static final int[]    WAVE_SPAWN_MS  = {0,1800,1300, 900,  600,  350};
    static final int[][]  WAVE_SPEED     = {{},{1,3},{2,5},{3,7},{5,9},{7,12}};
    static final int[]    WAVE_ALIEN_CNT = {0,   1,   2,   2,   3,    4};
    static final String[] WAVE_NAME      = {"","Scouts","Armada","Shock Fleet","Overlord Vanguard","Final Onslaught"};
    static final String[] WAVE_DESC      = {"","Simple scouts – learn your guns!",
                                             "Twin spawns & shields appear!",
                                             "Triple spawn + zigzag movement!",
                                             "Boss appears every 10 seconds!",
                                             "Quad swarm + kamikaze dive-bombs!"};
    static final Color[]  WAVE_TINT      = {null,
        new Color( 5,  5, 25),  new Color( 5, 10, 35),
        new Color(20,  5, 40),  new Color(30,  5, 30),
        new Color(40,  0, 15)};

    // ─── STATE ─────────────────────────────────────────────────────────────
    enum Screen { TITLE, PLAYING, TRANSITION, PAUSED, GAME_OVER }
    Screen screen = Screen.TITLE;

    int  score, lives, wave;
    int  highScore = 0;
    boolean gameWon;

    long lastAlienSpawn, lastPowerupSpawn, lastBossSpawn;
    long shieldEnd, rapidFireEnd, warpEnd;
    boolean shieldActive, rapidFireActive, warpActive;

    Ship          ship;
    List<Alien>     aliens    = new ArrayList<>();
    List<Laser>     lasers    = new ArrayList<>();
    List<AlienShot> shots     = new ArrayList<>();
    List<Explosion> explosions= new ArrayList<>();
    List<Powerup>   powerups  = new ArrayList<>();
    List<FloatText> floatTexts= new ArrayList<>();
    List<Boss>      bosses    = new ArrayList<>();
    List<Star>      stars     = new ArrayList<>();
    List<Nebula>    nebulae   = new ArrayList<>();

    javax.swing.Timer gameTimer;
    long  transitionStart;
    long  frameCount = 0;

    boolean keyLeft, keyRight, keySpace, keyShift;
    boolean spaceWasUp = true;

    Random rng = new Random();

    // ─── SOUNDS ────────────────────────────────────────────────────────────
    Clip laserClip, bigLaserClip, explosionClip, powerupClip, waveClip, bossClip;

    // ─── FONTS ─────────────────────────────────────────────────────────────
    Font fTiny   = new Font("Courier New", Font.BOLD, 13);
    Font fSmall  = new Font("Courier New", Font.BOLD, 17);
    Font fMedium = new Font("Courier New", Font.BOLD, 26);
    Font fLarge  = new Font("Courier New", Font.BOLD, 52);
    Font fHuge   = new Font("Courier New", Font.BOLD, 78);

    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("👾 Space Defender – 5 Waves");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            SpaceDefenderGame g = new SpaceDefenderGame();
            f.add(g);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    // ══════════════════════════════════════════════════════════════════════
    public SpaceDefenderGame() {
        setPreferredSize(new Dimension(W, H));
        setBackground(C_DARK_BG);
        setFocusable(true);
        addKeyListener(this);
        generateStarfield();
        generateNebulae();
        loadSounds();
        loadHighScore();
        gameTimer = new javax.swing.Timer(16, this);
        gameTimer.start();
    }

    // ─── Starfield ─────────────────────────────────────────────────────────
    void generateStarfield() {
        stars.clear();
        for (int i = 0; i < 200; i++) stars.add(new Star());
    }

    void generateNebulae() {
        nebulae.clear();
        Color[] nCols = {
            new Color(80, 0, 120, 40), new Color(0, 60, 120, 35),
            new Color(120, 0, 60, 30), new Color(0, 100, 80, 25)
        };
        for (int i = 0; i < 4; i++)
            nebulae.add(new Nebula(rng.nextInt(W), rng.nextInt(H),
                        rng.nextInt(200)+100, nCols[i % nCols.length]));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SOUNDS
    // ══════════════════════════════════════════════════════════════════════
    void loadSounds() {
        laserClip    = makeSound(buildLaserSound(false));
        bigLaserClip = makeSound(buildLaserSound(true));
        explosionClip= makeSound(buildExplosionSound());
        powerupClip  = makeSound(buildPowerupSound());
        waveClip     = makeSound(buildWaveSound());
        bossClip     = makeSound(buildBossAlertSound());
    }

    Clip makeSound(byte[] data) {
        try {
            AudioFormat fmt = new AudioFormat(44100,16,1,true,false);
            DataLine.Info info = new DataLine.Info(Clip.class, fmt);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(fmt, data, 0, data.length);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl v = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                v.setValue(v.getMaximum());
            }
            return clip;
        } catch (Exception e) { return null; }
    }

    void playSound(Clip c) {
        if (c == null) return;
        c.stop(); c.setFramePosition(0); c.start();
    }

    byte[] buildLaserSound(boolean big) {
        int sr = 44100; double dur = big ? 0.22 : 0.12;
        int n = (int)(sr * dur);
        byte[] buf = new byte[n*2];
        double startF = big ? 800 : 1200, endF = big ? 100 : 200;
        for (int i = 0; i < n; i++) {
            double t = (double)i/n;
            double freq = startF + (endF - startF)*t;
            double env = Math.exp(-t * (big ? 5 : 8));
            double s = Math.sin(2*Math.PI*freq*i/sr) * env * (big?1.1:0.9);
            short v = (short)(Math.max(-1,Math.min(1,s)) * Short.MAX_VALUE);
            buf[i*2]=(byte)(v&0xFF); buf[i*2+1]=(byte)((v>>8)&0xFF);
        }
        return buf;
    }

    byte[] buildExplosionSound() {
        int sr=44100; int n=(int)(sr*0.4);
        byte[] buf=new byte[n*2];
        for(int i=0;i<n;i++){
            double t=(double)i/n;
            double env=Math.exp(-t*10);
            double s=(rng.nextDouble()*2-1)*0.9*env + Math.sin(2*Math.PI*60*i/sr)*0.3*env;
            short v=(short)(Math.max(-1,Math.min(1,s))*Short.MAX_VALUE);
            buf[i*2]=(byte)(v&0xFF);buf[i*2+1]=(byte)((v>>8)&0xFF);
        }
        return buf;
    }

    byte[] buildPowerupSound() {
        int sr=44100; int n=(int)(sr*0.4);
        byte[] buf=new byte[n*2];
        double[] fs={523,659,784,1047,1319};
        for(int i=0;i<n;i++){
            int fi=(int)(i*fs.length/n);
            double f=fs[Math.min(fi,fs.length-1)];
            double env=Math.sin(Math.PI*i/n);
            double s=Math.sin(2*Math.PI*f*i/sr)*env*0.9;
            short v=(short)(Math.max(-1,Math.min(1,s))*Short.MAX_VALUE);
            buf[i*2]=(byte)(v&0xFF);buf[i*2+1]=(byte)((v>>8)&0xFF);
        }
        return buf;
    }

    byte[] buildWaveSound() {
        int sr=44100; int n=(int)(sr*0.9);
        byte[] buf=new byte[n*2];
        double[] fs={392,494,587,740,880};
        for(int i=0;i<n;i++){
            int fi=(int)(i*fs.length/n);
            double f=fs[Math.min(fi,fs.length-1)];
            double env=0.4+0.6*Math.sin(Math.PI*i/n);
            double s=Math.sin(2*Math.PI*f*i/sr)*env*0.95;
            short v=(short)(Math.max(-1,Math.min(1,s))*Short.MAX_VALUE);
            buf[i*2]=(byte)(v&0xFF);buf[i*2+1]=(byte)((v>>8)&0xFF);
        }
        return buf;
    }

    byte[] buildBossAlertSound() {
        int sr=44100; int n=(int)(sr*1.0);
        byte[] buf=new byte[n*2];
        for(int i=0;i<n;i++){
            double t=(double)i/n;
            double freq=110+80*Math.sin(2*Math.PI*3*t);
            double env=Math.sin(Math.PI*t);
            double s=Math.sin(2*Math.PI*freq*i/sr)*env*0.9;
            short v=(short)(Math.max(-1,Math.min(1,s))*Short.MAX_VALUE);
            buf[i*2]=(byte)(v&0xFF);buf[i*2+1]=(byte)((v>>8)&0xFF);
        }
        return buf;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HIGH SCORE
    // ══════════════════════════════════════════════════════════════════════
    void loadHighScore() {
        try(BufferedReader r=new BufferedReader(new FileReader("space_highscore.txt"))){
            highScore=Integer.parseInt(r.readLine().trim());
        } catch(Exception ignored){}
    }
    void saveHighScore() {
        try(PrintWriter w=new PrintWriter("space_highscore.txt")){
            w.println(highScore);
        } catch(Exception ignored){}
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INIT
    // ══════════════════════════════════════════════════════════════════════
    void startGame() {
        score=0; lives=3; wave=1; gameWon=false;
        shieldActive=false; rapidFireActive=false; warpActive=false;
        shieldEnd=0; rapidFireEnd=0; warpEnd=0;
        aliens.clear(); lasers.clear(); shots.clear();
        explosions.clear(); powerups.clear(); floatTexts.clear(); bosses.clear();
        ship = new Ship();
        lastAlienSpawn = lastPowerupSpawn = lastBossSpawn = System.currentTimeMillis();
        screen = Screen.PLAYING;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ══════════════════════════════════════════════════════════════════════
    @Override public void actionPerformed(ActionEvent e) { update(); repaint(); }

    void update() {
        frameCount++;
        if (screen == Screen.TRANSITION) {
            if (System.currentTimeMillis() - transitionStart > 3200) {
                screen = Screen.PLAYING;
                lastAlienSpawn = System.currentTimeMillis();
                lastBossSpawn  = System.currentTimeMillis();
            }
            // Scroll stars during transition
            scrollStars();
            return;
        }
        if (screen != Screen.PLAYING) return;

        long now = System.currentTimeMillis();

        scrollStars();

        // Move ship
        int spd = warpActive ? 14 : 7;
        if (keyLeft)  ship.x = Math.max(0, ship.x - spd);
        if (keyRight) ship.x = Math.min(W - ship.w, ship.x + spd);

        // Shoot
        if (keySpace && spaceWasUp) {
            spaceWasUp = false;
            shoot(now);
        }
        if (!keySpace) spaceWasUp = true;

        // Power-up timers
        if (shieldActive   && now > shieldEnd)    shieldActive = false;
        if (rapidFireActive && now > rapidFireEnd) { rapidFireActive=false; ship.cooldown=220; }
        if (warpActive     && now > warpEnd)       warpActive = false;

        // Spawn
        spawnAlien(now);
        spawnPowerup(now);
        if (wave >= 4) spawnBoss(now);

        // Update lasers
        lasers.removeIf(l -> { l.update(); return l.dead; });

        // Update alien shots
        shots.removeIf(s -> { s.update(); return s.dead; });

        // Update aliens
        List<Alien> alienDead = new ArrayList<>();
        for (Alien a : aliens) {
            a.update(ship.x + ship.w/2, now);
            if (a.y > H + 10) {
                alienDead.add(a);
                loseLife(); if (screen != Screen.PLAYING) return;
            }
            // Alien shoots back
            if (wave >= 2 && rng.nextInt(800) < wave)
                shots.add(new AlienShot(a.x + a.w/2, a.y + a.h));
            // Alien-ship collision
            if (!shieldActive && overlap(a.x,a.y,a.w,a.h,ship.x,ship.y,ship.w,ship.h)) {
                alienDead.add(a);
                explosions.add(new Explosion(a.x+a.w/2, a.y+a.h/2, false));
                loseLife(); if (screen != Screen.PLAYING) return;
            }
        }
        aliens.removeAll(alienDead);

        // Update bosses
        List<Boss> bossDead = new ArrayList<>();
        for (Boss b : bosses) {
            b.update(ship.x + ship.w/2, now);
            if (b.y > H+20) bossDead.add(b);
            if (b.hp <= 0) {
                bossDead.add(b);
                int pts = 150;
                score += pts;
                explosions.add(new Explosion(b.x+b.w/2, b.y+b.h/2, true));
                floatTexts.add(new FloatText("+"+pts+" BOSS KILL!", b.x+b.w/2, b.y, C_NEON_PURPLE));
                playSound(explosionClip);
            }
            // Boss shots
            if (rng.nextInt(300) < wave)
                shots.add(new AlienShot(b.x + b.w/2, b.y + b.h));
            if (!shieldActive && overlap(b.x,b.y,b.w,b.h,ship.x,ship.y,ship.w,ship.h)) {
                loseLife(); if (screen != Screen.PLAYING) return;
            }
        }
        bosses.removeAll(bossDead);

        // Alien shot hits ship
        List<AlienShot> shotDead = new ArrayList<>();
        for (AlienShot s : shots) {
            if (!shieldActive && overlap((int)s.x-4,(int)s.y-4,8,8, ship.x,ship.y,ship.w,ship.h)) {
                shotDead.add(s);
                explosions.add(new Explosion(ship.x+ship.w/2, ship.y, false));
                loseLife(); if (screen != Screen.PLAYING) return;
            }
        }
        shots.removeAll(shotDead);

        // Laser-alien collision
        checkLaserCollisions(now);

        // Laser-boss collision
        checkLaserBossCollisions();

        // Powerup collision
        List<Powerup> pDead = new ArrayList<>();
        for (Powerup p : powerups) {
            p.y += p.speed;
            if (p.y > H) pDead.add(p);
            else if (overlap(p.x,p.y,32,32, ship.x,ship.y,ship.w,ship.h)) {
                applyPowerup(p, now);
                pDead.add(p);
            }
        }
        powerups.removeAll(pDead);

        // Update effects
        explosions.removeIf(ex -> { ex.update(); return ex.dead; });
        floatTexts.removeIf(ft -> { ft.update(); return ft.dead; });

        // Wave progression
        if (score >= WAVE_TARGET[wave] && wave < 5) {
            wave++;
            lives++;
            playSound(waveClip);
            transitionStart = System.currentTimeMillis();
            screen = Screen.TRANSITION;
            aliens.clear(); bosses.clear(); shots.clear();
        } else if (score >= WAVE_TARGET[5] && wave == 5) {
            gameWon = true;
            endGame();
        }
    }

    void scrollStars() {
        for (Star s : stars) {
            s.y += s.speed;
            if (s.y > H) { s.y = -2; s.x = rng.nextInt(W); }
        }
    }

    void shoot(long now) {
        if (now - ship.lastShot < ship.cooldown) return;
        ship.lastShot = now;
        if (rapidFireActive) {
            // Triple shot spread
            lasers.add(new Laser(ship.x+ship.w/2-20, ship.y, -2, true));
            lasers.add(new Laser(ship.x+ship.w/2,    ship.y,  0, true));
            lasers.add(new Laser(ship.x+ship.w/2+20, ship.y,  2, true));
            playSound(bigLaserClip);
        } else {
            lasers.add(new Laser(ship.x+ship.w/2, ship.y, 0, false));
            playSound(laserClip);
        }
    }

    void checkLaserCollisions(long now) {
        List<Laser> lDead = new ArrayList<>();
        List<Alien> aDead = new ArrayList<>();
        for (Laser l : lasers) {
            for (Alien a : aliens) {
                if (aDead.contains(a)) continue;
                if (overlap((int)l.x-3,(int)l.y-10,6,20, a.x,a.y,a.w,a.h)) {
                    lDead.add(l);
                    a.hp--;
                    if (a.hp <= 0) {
                        aDead.add(a);
                        int pts = a.points;
                        score += pts;
                        explosions.add(new Explosion(a.x+a.w/2, a.y+a.h/2, false));
                        floatTexts.add(new FloatText("+"+pts, a.x+a.w/2, a.y, C_NEON_YELLOW));
                        playSound(explosionClip);
                    } else {
                        floatTexts.add(new FloatText("HIT!", a.x+a.w/2, a.y, C_NEON_ORANGE));
                    }
                    break;
                }
            }
        }
        lasers.removeAll(lDead);
        aliens.removeAll(aDead);
    }

    void checkLaserBossCollisions() {
        List<Laser> lDead = new ArrayList<>();
        for (Laser l : lasers) {
            for (Boss b : bosses) {
                if (overlap((int)l.x-3,(int)l.y-10,6,20, b.x,b.y,b.w,b.h)) {
                    lDead.add(l);
                    b.hp -= l.big ? 3 : 1;
                    floatTexts.add(new FloatText("BOSS HIT!", b.x+b.w/2, b.y, C_NEON_RED));
                    break;
                }
            }
        }
        lasers.removeAll(lDead);
    }

    boolean overlap(int ax,int ay,int aw,int ah,int bx,int by,int bw,int bh){
        return ax<bx+bw && ax+aw>bx && ay<by+bh && ay+ah>by;
    }

    void loseLife() {
        lives--;
        if (lives <= 0) endGame();
    }

    void applyPowerup(Powerup p, long now) {
        playSound(powerupClip);
        switch (p.type) {
            case 0: // Shield
                shieldActive=true; shieldEnd=now+8000;
                floatTexts.add(new FloatText("SHIELD UP!", p.x,p.y, C_NEON_BLUE)); break;
            case 1: // Rapid Fire
                rapidFireActive=true; rapidFireEnd=now+10000; ship.cooldown=80;
                floatTexts.add(new FloatText("RAPID FIRE!", p.x,p.y, C_NEON_RED)); break;
            case 2: // Warp Drive
                warpActive=true; warpEnd=now+7000;
                floatTexts.add(new FloatText("WARP DRIVE!", p.x,p.y, C_NEON_PURPLE)); break;
            case 3: // Extra life
                lives++;
                floatTexts.add(new FloatText("+1 LIFE!", p.x,p.y, C_NEON_GREEN)); break;
        }
    }

    void spawnAlien(long now) {
        int interval = WAVE_SPAWN_MS[wave];
        if (now - lastAlienSpawn < interval) return;
        lastAlienSpawn = now;
        int cnt = WAVE_ALIEN_CNT[wave];
        for (int i = 0; i < cnt; i++) aliens.add(new Alien(wave));
    }

    void spawnPowerup(long now) {
        if (now - lastPowerupSpawn < 12000) return;
        lastPowerupSpawn = now;
        powerups.add(new Powerup());
    }

    void spawnBoss(long now) {
        if (now - lastBossSpawn < 12000) return;
        lastBossSpawn = now;
        bosses.add(new Boss(wave));
        playSound(bossClip);
        floatTexts.add(new FloatText("⚠ BOSS INCOMING! ⚠", W/2, 200, C_NEON_RED));
    }

    void endGame() {
        if (score > highScore) { highScore = score; saveHighScore(); }
        screen = Screen.GAME_OVER;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  PAINT
    // ══════════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
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

    void drawSpaceBackground(Graphics2D g) {
        // Deep space gradient
        Color bg = (screen == Screen.TITLE || screen == Screen.GAME_OVER)
                   ? C_DARK_BG
                   : (wave >= 1 && wave <= 5 ? WAVE_TINT[wave] : C_DARK_BG);
        g.setColor(bg);
        g.fillRect(0,0,W,H);

        // Nebulae
        for (Nebula n : nebulae) n.draw(g);

        // Stars
        for (Star s : stars) s.draw(g);

        // Earth glow at bottom
        g.setColor(new Color(0, 80, 180, 40));
        g.fillRect(0, H-60, W, 60);
        g.setColor(new Color(0, 50, 120, 60));
        g.fillRect(0, H-20, W, 20);
        // Grid lines on ground
        g.setColor(new Color(0, 150, 255, 25));
        for (int x = 0; x < W; x += 40)
            g.drawLine(x, H-60, x, H);
        g.setColor(new Color(0, 150, 255, 30));
        for (int y = H-60; y < H; y += 12)
            g.drawLine(0, y, W, y);
    }

    // ── TITLE ──────────────────────────────────────────────────────────────
    void drawTitle(Graphics2D g) {
        drawSpaceBackground(g);

        // Animated scan line
        long t = System.currentTimeMillis();
        int scanY = (int)((t / 20) % H);
        g.setColor(new Color(0, 200, 255, 18));
        g.fillRect(0, scanY, W, 3);

        // Title
        drawGlow(g, "SPACE DEFENDER", fHuge, C_NEON_BLUE, W/2, 85, 18);
        drawGlow(g, "5 WAVES OF ALIEN CHAOS", fMedium, C_NEON_GREEN, W/2, 130, 8);

        // Controls
        drawPanel(g, 30, 158, W-60, 62, new Color(0,30,60,180));
        drawGlowBorder(g, 30, 158, W-60, 62, C_NEON_BLUE);
        drawCentred(g, "← → / A D  =  Move        SPACE  =  Fire        P  =  Pause        Q  =  Quit", fTiny, C_WHITE, W/2, 183);
        drawCentred(g, "Power-ups:  🔵 Shield   🔴 Rapid Fire   🟣 Warp Drive   🟢 +Life", fTiny, C_NEON_BLUE, W/2, 206);

        // Wave cards
        Color[] colors = {C_NEON_GREEN, C_NEON_YELLOW, C_NEON_ORANGE, C_NEON_RED, C_NEON_PURPLE};
        String[] icons  = {"👾","🛸","💥","👹","☠"};
        int cx = W/2, sy = 240, bw = 860, bh = 64;
        for (int i = 1; i <= 5; i++) {
            int y = sy + (i-1)*(bh+5);
            Color c = colors[i-1];
            drawPanel(g, cx-bw/2, y, bw, bh, new Color(0,10,30,210));
            drawGlowBorder(g, cx-bw/2, y, bw, bh, new Color(c.getRed(),c.getGreen(),c.getBlue(),140));

            // Icon badge
            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.drawString(icons[i-1], cx-bw/2+14, y+44);

            // Wave number
            g.setColor(c);
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.drawString("W"+i, cx-bw/2+54, y+28);

            // Name + desc
            drawText(g, WAVE_NAME[i], fSmall, C_WHITE, cx-bw/2+90, y+26);
            drawText(g, WAVE_DESC[i], fTiny,  c,       cx-bw/2+90, y+50);

            // Goal badge
            int dw = 110;
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),60));
            g.fillRoundRect(cx+bw/2-dw-6, y+8, dw, 20, 6,6);
            g.setColor(c);
            g.setStroke(new BasicStroke(1));
            g.drawRoundRect(cx+bw/2-dw-6, y+8, dw, 20, 6,6);
            drawCentred(g, "GOAL "+WAVE_TARGET[i], fTiny, c, cx+bw/2-dw/2-6, y+22);

            // Speed indicator
            int dots = i;
            for (int d=0; d<5; d++) {
                g.setColor(d<dots ? c : new Color(c.getRed(),c.getGreen(),c.getBlue(),50));
                g.fillOval(cx+bw/2-dw-14-(4-d)*14, y+34, 10, 10);
            }
        }

        // High score
        if (highScore > 0) drawGlow(g, "BEST SCORE: "+highScore, fSmall, C_NEON_YELLOW, W/2, H-58, 8);

        // Blink start
        if ((t/520)%2==0)
            drawGlow(g, "▶  PRESS SPACE TO LAUNCH  ◀", fLarge, C_NEON_GREEN, W/2, H-22, 12);
    }

    // ── GAME ───────────────────────────────────────────────────────────────
    void drawGame(Graphics2D g) {
        drawSpaceBackground(g);
        for (Alien a : aliens)     a.draw(g);
        for (Boss  b : bosses)     b.draw(g);
        for (Laser l : lasers)     l.draw(g);
        for (AlienShot s : shots)  s.draw(g);
        for (Powerup p : powerups) p.draw(g);
        for (Explosion ex : explosions) ex.draw(g);
        ship.draw(g);
        for (FloatText ft : floatTexts) ft.draw(g);
        drawHUD(g);
    }

    void drawHUD(Graphics2D g) {
        long now = System.currentTimeMillis();

        // Top bar
        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,W,130);
        g.setColor(new Color(0,200,255,50));
        g.fillRect(0,128,W,2);

        // Score / Lives / Wave
        drawGlow(g, "SCORE: "+score, fMedium, C_WHITE, W/2, 26, 6);
        drawGlow(g, "♥ ".repeat(Math.max(0,lives)), fMedium, C_NEON_RED, W/2, 55, 6);
        drawGlow(g, "WAVE "+wave+"/5 – "+WAVE_NAME[wave], fSmall, C_NEON_GREEN, W/2, 82, 5);

        // Progress bar
        int target = WAVE_TARGET[wave];
        float prog = Math.min((float)score/target, 1f);
        int bx=W/2-160, by=95, bw=320, bh=20;
        g.setColor(new Color(0,50,80,120));
        g.fillRoundRect(bx,by,bw,bh,8,8);

        // Gradient fill
        GradientPaint gp = new GradientPaint(bx,by,C_NEON_BLUE,bx+(int)(bw*prog),by,C_NEON_GREEN);
        g.setPaint(gp);
        g.fillRoundRect(bx,by,(int)(bw*prog),bh,8,8);
        g.setPaint(null);

        g.setColor(C_NEON_BLUE);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(bx,by,bw,bh,8,8);
        g.setStroke(new BasicStroke(1));
        drawCentred(g, score+" / "+target, fTiny, C_WHITE, W/2, by+bh/2+5);

        // Status top-right
        int sy = 16;
        if (shieldActive && now < shieldEnd) {
            long tl=(shieldEnd-now)/1000;
            drawGlow(g, "🔵 SHIELD: "+tl+"s", fTiny, C_NEON_BLUE, W-130, sy, 4);
            sy+=20;
        }
        if (rapidFireActive && now < rapidFireEnd) {
            long tl=(rapidFireEnd-now)/1000;
            drawGlow(g, "🔴 RAPID: "+tl+"s", fTiny, C_NEON_RED, W-130, sy, 4);
            sy+=20;
        }
        if (warpActive && now < warpEnd) {
            long tl=(warpEnd-now)/1000;
            drawGlow(g, "🟣 WARP: "+tl+"s", fTiny, C_NEON_PURPLE, W-130, sy, 4);
        }

        // Weapon top-left
        String wep = rapidFireActive ? "⚡ RAPID LASER" : "▶ LASER";
        Color wc   = rapidFireActive ? C_NEON_RED : C_NEON_GREEN;
        drawGlow(g, wep, fTiny, wc, 10, 16, 4);

        // Boss warning
        if (!bosses.isEmpty()) {
            long t2 = System.currentTimeMillis();
            if ((t2/350)%2==0) drawGlow(g,"⚠ BOSS ACTIVE ⚠", fSmall, C_NEON_RED, W/2, 115, 10);
        }
    }

    // ── TRANSITION ─────────────────────────────────────────────────────────
    void drawTransition(Graphics2D g) {
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,W,H);
        drawGlow(g, "WAVE "+wave, fHuge, C_NEON_YELLOW, W/2, H/3, 24);
        drawGlow(g, WAVE_NAME[wave], fLarge, C_WHITE, W/2, H/2-30, 12);
        drawGlow(g, WAVE_DESC[wave], fMedium, C_NEON_GREEN, W/2, H/2+20, 8);
        drawGlow(g, "TARGET: "+WAVE_TARGET[wave]+" PTS", fMedium, C_NEON_BLUE, W/2, H/2+62, 8);
        drawGlow(g, "⬆  PREPARE FOR BATTLE  ⬆", fLarge, C_NEON_RED, W/2, H*2/3+10, 12);
        drawGlow(g, "+1 BONUS LIFE AWARDED", fSmall, C_NEON_GREEN, W/2, H*2/3+58, 6);
    }

    // ── PAUSED ─────────────────────────────────────────────────────────────
    void drawPaused(Graphics2D g) {
        g.setColor(new Color(0,0,0,160));
        g.fillRect(0,0,W,H);
        drawGlow(g, "// PAUSED //", fHuge, C_NEON_BLUE, W/2, H/2-30, 20);
        drawGlow(g, "PRESS  P  TO RESUME", fMedium, C_WHITE, W/2, H/2+40, 6);
    }

    // ── GAME OVER ──────────────────────────────────────────────────────────
    void drawGameOver(Graphics2D g) {
        drawSpaceBackground(g);
        String title = gameWon ? "★ EARTH DEFENDED ★" : "EARTH DESTROYED";
        Color  tc    = gameWon ? C_NEON_GREEN : C_NEON_RED;
        drawGlow(g, title, fHuge, tc, W/2, H/4, 22);
        drawGlow(g, "WAVES SURVIVED: "+wave+"/5", fLarge, C_NEON_BLUE, W/2, H/2-55, 10);
        drawGlow(g, "FINAL SCORE: "+score,         fLarge, C_WHITE,     W/2, H/2-5,  10);
        drawGlow(g, "BEST  SCORE: "+highScore,      fLarge, C_NEON_YELLOW,W/2, H/2+48, 10);
        drawGlow(g, "SPACE = Play Again     Q = Quit", fMedium, C_NEON_GREEN, W/2, H*3/4+10, 8);
    }

    // ─── Draw Helpers ──────────────────────────────────────────────────────
    void drawPanel(Graphics2D g, int x, int y, int w, int h, Color c) {
        g.setColor(c); g.fillRoundRect(x,y,w,h,12,12);
    }
    void drawGlowBorder(Graphics2D g, int x, int y, int w, int h, Color c) {
        g.setColor(c); g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x,y,w,h,12,12); g.setStroke(new BasicStroke(1));
    }
    void drawCentred(Graphics2D g, String t, Font f, Color c, int cx, int cy) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(c);
        g.drawString(t, cx-fm.stringWidth(t)/2, cy);
    }
    void drawText(Graphics2D g, String t, Font f, Color c, int x, int y) {
        g.setFont(f); g.setColor(c); g.drawString(t,x,y);
    }
    void drawGlow(Graphics2D g, String t, Font f, Color c, int cx, int cy, int glowR) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int tx = cx - fm.stringWidth(t)/2;
        // Glow
        for (int r = glowR; r > 0; r -= 3) {
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(), Math.max(0, 18-r)));
            g.drawString(t, tx-r/2, cy+r/2);
            g.drawString(t, tx+r/2, cy-r/2);
        }
        // Shadow
        g.setColor(new Color(0,0,0,120));
        g.drawString(t, tx+2, cy+2);
        // Main
        g.setColor(c);
        g.drawString(t, tx, cy);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KEY EVENTS
    // ══════════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k==KeyEvent.VK_LEFT  || k==KeyEvent.VK_A) keyLeft  = true;
        if (k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_D) keyRight = true;
        if (k==KeyEvent.VK_SPACE)                     keySpace = true;
        if (k==KeyEvent.VK_SHIFT)                     keyShift = true;

        if (screen==Screen.TITLE    && k==KeyEvent.VK_SPACE) startGame();
        if (screen==Screen.GAME_OVER) {
            if (k==KeyEvent.VK_SPACE) screen=Screen.TITLE;
            if (k==KeyEvent.VK_Q)     System.exit(0);
        }
        if (screen==Screen.PLAYING && k==KeyEvent.VK_P) screen=Screen.PAUSED;
        if (screen==Screen.PAUSED  && k==KeyEvent.VK_P) screen=Screen.PLAYING;
        if (k==KeyEvent.VK_Q && screen!=Screen.PLAYING) System.exit(0);
    }
    @Override public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k==KeyEvent.VK_LEFT  || k==KeyEvent.VK_A) keyLeft  = false;
        if (k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_D) keyRight = false;
        if (k==KeyEvent.VK_SPACE) { keySpace=false; spaceWasUp=true; }
        if (k==KeyEvent.VK_SHIFT) keyShift = false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════
    //  INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════

    // ── Star ───────────────────────────────────────────────────────────────
    class Star {
        int x, y, size; float speed; int bright;
        Star() { reset(); y = rng.nextInt(H); }
        void reset() {
            x = rng.nextInt(W); y = -2;
            size = rng.nextInt(3)+1;
            speed = 0.5f + rng.nextFloat()*2.5f;
            bright = 80 + rng.nextInt(176);
        }
        void draw(Graphics2D g) {
            g.setColor(new Color(bright,bright,bright));
            g.fillOval(x,y,size,size);
        }
    }

    // ── Nebula ─────────────────────────────────────────────────────────────
    class Nebula {
        int x, y, r; Color c;
        Nebula(int x, int y, int r, Color c) {this.x=x;this.y=y;this.r=r;this.c=c;}
        void draw(Graphics2D g) {
            RadialGradientPaint rp = new RadialGradientPaint(x,y,r,
                new float[]{0f,1f}, new Color[]{c, new Color(0,0,0,0)});
            g.setPaint(rp); g.fillOval(x-r,y-r,r*2,r*2); g.setPaint(null);
        }
    }

    // ── Ship (player) ──────────────────────────────────────────────────────
    class Ship {
        int x, y, w=64, h=52, speed=7;
        long lastShot=0; int cooldown=220;
        Ship() { x=W/2-w/2; y=H-h-30; }

        void draw(Graphics2D g) {
            long now = System.currentTimeMillis();

            // Shield aura
            if (shieldActive) {
                long r = (now/60)%360;
                g.setColor(new Color(0,180,255,40));
                g.fillOval(x-16,y-16,w+32,h+32);
                g.setColor(new Color(0,200,255,100));
                g.setStroke(new BasicStroke(2.5f));
                g.drawOval(x-16,y-16,w+32,h+32);
                g.setStroke(new BasicStroke(1));
            }

            // Warp effect
            if (warpActive) {
                for (int i = 0; i < 4; i++) {
                    g.setColor(new Color(180,80,255, 30-i*6));
                    g.fillRoundRect(x-i*4, y+i*3, w+i*8, h, 8,8);
                }
            }

            // Engine glow
            long pulse = (now/80)%20;
            int pulseY = (int)(3*Math.sin(now/150.0));
            g.setColor(new Color(0, 180, 255, 100+pulseY*10));
            g.fillOval(x+8,  y+h-8, 14, 10+pulseY);
            g.fillOval(x+42, y+h-8, 14, 10+pulseY);

            // Main body
            Color bodyC = rapidFireActive ? C_NEON_RED : new Color(30,100,200);
            int[] bx = {x+w/2, x+w-4, x+w, x+w-10, x+10, x, x+4};
            int[] by = {y,     y+18,  y+h, y+h,     y+h,  y+h, y+18};
            g.setColor(bodyC);
            g.fillPolygon(bx, by, 7);

            // Cockpit
            g.setColor(new Color(0,230,255,200));
            g.fillOval(x+w/2-10, y+4, 20, 20);
            g.setColor(new Color(150,230,255,120));
            g.fillOval(x+w/2-6, y+7, 10, 10);

            // Wing highlights
            g.setColor(new Color(80,180,255,150));
            g.fillPolygon(new int[]{x+4,x+w/2-4,x+12}, new int[]{y+18,y+8,y+h}, 3);
            g.fillPolygon(new int[]{x+w-4,x+w/2+4,x+w-12}, new int[]{y+18,y+8,y+h}, 3);

            // Neon edge
            g.setColor(new Color(0,200,255,160));
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(bx,by,7);
            g.setStroke(new BasicStroke(1));

            // Muzzle flash
            if (now - lastShot < 90) {
                g.setColor(new Color(255,220,60,200));
                g.fillOval(x+w/2-5, y-14, 10, 14);
                if (rapidFireActive) {
                    g.fillOval(x+w/2-25, y-10, 8, 10);
                    g.fillOval(x+w/2+17, y-10, 8, 10);
                }
            }
        }
    }

    // ── Alien ──────────────────────────────────────────────────────────────
    class Alien {
        int x, y, w, h, type, points, hp;
        float vx, vy;
        double angle=0, rotSpd;
        boolean zigzag, diving, homing;
        int waveNum;
        long diveStart=0;

        Alien(int wn) {
            waveNum = wn;
            int[] pool = wn >= 4 ? new int[]{0,1,2,3,4}
                       : wn == 3 ? new int[]{0,1,2,3}
                       : wn == 2 ? new int[]{0,1,2}
                       :           new int[]{0,1};
            type = pool[rng.nextInt(pool.length)];
            int[] sizes  = {46,52,40,58,44};
            w = h = sizes[type];
            x = rng.nextInt(W-w);
            y = -(h + rng.nextInt(40));
            int[] sp = WAVE_SPEED[wn];
            vy = sp[0] + rng.nextInt(Math.max(1, sp[1]-sp[0]+1));
            vx = (wn>=3) ? (rng.nextFloat()-0.5f)*3 : 0;
            zigzag = (wn >= 3);
            homing = (wn >= 4);
            diving = (wn == 5 && rng.nextBoolean());
            rotSpd = (rng.nextDouble()-0.5)*5;
            int[] pts = {20, 30, 25, 40, 50};
            points = pts[type] + wn*5;
            int[] hps = {1, 2, 1, 3, 2};
            hp = hps[type] + (wn >= 4 ? 1 : 0);
        }

        void update(int shipCX, long now) {
            if (homing) {
                float dx = shipCX - (x+w/2);
                vx += dx*0.002f;
                vx = Math.max(-5, Math.min(5, vx));
            }
            if (zigzag) vx = (float)(2.5*Math.sin(now/400.0 + x*0.05));
            if (diving && y > H/3) vy = Math.min(vy+0.2f, 14);
            x += (int)vx; y += (int)vy; angle += rotSpd;
            if (x<0) {x=0; vx=Math.abs(vx);}
            if (x>W-w){x=W-w; vx=-Math.abs(vx);}
        }

        void draw(Graphics2D g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.rotate(Math.toRadians(angle), x+w/2.0, y+h/2.0);
            switch (type) {
                case 0: drawScout(g2);    break;
                case 1: drawFighter(g2);  break;
                case 2: drawDrone(g2);    break;
                case 3: drawCarrier(g2);  break;
                case 4: drawInfector(g2); break;
            }
            // HP pips
            if (hp > 1) {
                for (int i=0; i<hp; i++) {
                    g2.setColor(C_NEON_RED);
                    g2.fillOval(x+2+i*8, y-8, 6, 6);
                }
            }
            g2.dispose();
        }

        void drawScout(Graphics2D g) {
            // Classic saucer
            g.setColor(new Color(80,200,80));
            g.fillOval(x+4, y+h/3, w-8, h*2/3);
            g.setColor(new Color(50,150,50));
            g.fillOval(x+12, y, w-24, h/2);
            g.setColor(new Color(120,255,120,180));
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(x+4, y+h/3, w-8, h*2/3);
            g.setStroke(new BasicStroke(1));
            // Window
            g.setColor(new Color(180,255,180,200));
            g.fillOval(x+w/2-6, y+6, 12, 10);
            // Lights
            for (int i=0;i<3;i++) {
                g.setColor(new Color(255,255,0, 150+(i*30)%100));
                g.fillOval(x+6+i*(w/3-4), y+h*2/3, 6, 5);
            }
        }

        void drawFighter(Graphics2D g) {
            // Angular fighter
            Color c = new Color(200,80,80);
            int[] px={x+w/2,x+w,x+w-8,x+w/2,x+8,x};
            int[] py={y,y+h/3,y+h,y+h*3/4,y+h,y+h/3};
            g.setColor(c);
            g.fillPolygon(px,py,6);
            g.setColor(new Color(255,120,120,160));
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(px,py,6);
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(255,50,50,200));
            g.fillOval(x+w/2-7,y+h/4,14,14);
        }

        void drawDrone(Graphics2D g) {
            // Hexagonal drone
            int[] hx=new int[6], hy=new int[6];
            int cx=x+w/2, cy=y+h/2, r=w/2-2;
            for(int i=0;i<6;i++){
                hx[i]=(int)(cx+r*Math.cos(Math.PI/3*i));
                hy[i]=(int)(cy+r*Math.sin(Math.PI/3*i));
            }
            g.setColor(new Color(80,80,220));
            g.fillPolygon(hx,hy,6);
            g.setColor(new Color(120,120,255,200));
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(hx,hy,6);
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(200,200,255,220));
            g.fillOval(cx-6,cy-6,12,12);
        }

        void drawCarrier(Graphics2D g) {
            // Big carrier
            g.setColor(new Color(150,80,200));
            g.fillRoundRect(x,y+h/4,w,h*2/3,12,12);
            g.setColor(new Color(180,50,230));
            g.fillRoundRect(x+8,y,w-16,h/2,8,8);
            g.setColor(new Color(200,120,255,160));
            g.setStroke(new BasicStroke(2f));
            g.drawRoundRect(x,y+h/4,w,h*2/3,12,12);
            g.setStroke(new BasicStroke(1));
            // Energy cannons
            g.setColor(new Color(255,180,80));
            g.fillRect(x-4,y+h*2/3-4,8,8);
            g.fillRect(x+w-4,y+h*2/3-4,8,8);
            g.setColor(new Color(255,80,80,200));
            g.fillOval(x+w/2-8,y+h/4+4,16,14);
        }

        void drawInfector(Graphics2D g) {
            // Organic biomech shape
            g.setColor(new Color(0,180,100));
            g.fillOval(x,y,w,h);
            // Tendrils
            g.setColor(new Color(0,220,120,180));
            for (int i=0;i<4;i++) {
                double a=i*Math.PI/2;
                int tx=(int)(x+w/2+16*Math.cos(a));
                int ty=(int)(y+h/2+16*Math.sin(a));
                g.setStroke(new BasicStroke(3));
                g.drawLine(x+w/2,y+h/2,tx,ty);
                g.fillOval(tx-4,ty-4,8,8);
            }
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(200,255,200,220));
            g.fillOval(x+w/2-7,y+h/2-7,14,14);
        }
    }

    // ── Boss ───────────────────────────────────────────────────────────────
    class Boss {
        int x, y, w=100, h=80, hp, maxHp;
        float vx=2, vy=1;
        int waveNum;
        double pulse=0;

        Boss(int wn) {
            waveNum = wn;
            x = rng.nextInt(W-w);
            y = -h;
            maxHp = hp = 20 + wn * 10;
            vx = (rng.nextBoolean() ? 1:-1) * (1.5f + wn*0.4f);
            vy = 0.8f + wn*0.2f;
        }

        void update(int shipCX, long now) {
            x += (int)vx; y += (int)vy;
            if (x<0){x=0;vx=Math.abs(vx);}
            if (x>W-w){x=W-w;vx=-Math.abs(vx);}
            if (y > H/4) vy = 0; // hold position
            pulse += 0.08;
        }

        void draw(Graphics2D g) {
            int glow = (int)(20+15*Math.sin(pulse));
            // Outer glow
            g.setColor(new Color(255,40,40,30));
            g.fillOval(x-12,y-12,w+24,h+24);

            // Body
            g.setColor(new Color(180,20,20));
            g.fillRoundRect(x,y,w,h,16,16);

            // Armour plates
            g.setColor(new Color(220,60,60));
            g.fillRoundRect(x+8,y+8,w-16,20,8,8);
            g.fillRoundRect(x+8,y+h-28,w-16,20,8,8);

            // Core eye
            g.setColor(new Color(255,180,0, 200+glow));
            g.fillOval(x+w/2-16,y+h/2-16,32,32);
            g.setColor(new Color(255,240,80,200));
            g.fillOval(x+w/2-8,y+h/2-8,16,16);

            // Side cannons
            g.setColor(new Color(80,80,80));
            g.fillRect(x-12,y+h/2-8,14,16);
            g.fillRect(x+w-2,y+h/2-8,14,16);
            g.setColor(new Color(255,100,0,200));
            g.fillOval(x-14,y+h/2-4,8,8);
            g.fillOval(x+w+6,y+h/2-4,8,8);

            // Neon border
            g.setColor(new Color(255, 60+glow, 60, 200));
            g.setStroke(new BasicStroke(2.5f));
            g.drawRoundRect(x,y,w,h,16,16);
            g.setStroke(new BasicStroke(1));

            // HP bar
            int bw=w, bh=8;
            g.setColor(new Color(80,0,0));
            g.fillRoundRect(x,y-14,bw,bh,4,4);
            g.setColor(new Color(255,40,40));
            g.fillRoundRect(x,y-14,(int)(bw*(float)hp/maxHp),bh,4,4);
            g.setColor(C_NEON_RED);
            g.setStroke(new BasicStroke(1.2f));
            g.drawRoundRect(x,y-14,bw,bh,4,4);
            g.setStroke(new BasicStroke(1));
            drawCentred(g,"BOSS "+hp+"/"+maxHp, fTiny, C_WHITE, x+w/2, y-18);
        }
    }

    // ── Laser ──────────────────────────────────────────────────────────────
    class Laser {
        float x,y,vx,vy; boolean dead, big;
        Laser(int sx, int sy, int angleDeg, boolean big) {
            x=sx; y=sy; this.big=big;
            double r=Math.toRadians(angleDeg);
            float spd = big ? 14 : 18;
            vx=(float)(spd*Math.sin(r));
            vy=(float)(-spd*Math.cos(r));
        }
        void update() { x+=vx; y+=vy; if(y<-20||x<-20||x>W+20) dead=true; }
        void draw(Graphics2D g) {
            if (big) {
                g.setColor(new Color(255,80,80,180));
                g.fillOval((int)x-5,(int)y-5,11,11);
                g.setColor(C_NEON_RED);
                g.fillRect((int)x-3,(int)y-12,6,22);
            } else {
                g.setColor(new Color(0,220,255,160));
                g.fillOval((int)x-3,(int)y-3,7,7);
                g.setColor(C_NEON_BLUE);
                g.fillRect((int)x-2,(int)y-10,4,18);
            }
        }
    }

    // ── AlienShot ──────────────────────────────────────────────────────────
    class AlienShot {
        float x,y; boolean dead;
        AlienShot(int sx, int sy) { x=sx; y=sy; }
        void update() { y+=5; if(y>H+10) dead=true; }
        void draw(Graphics2D g) {
            g.setColor(new Color(255,80,200,200));
            g.fillOval((int)x-4,(int)y-4,9,9);
            g.setColor(new Color(255,180,255,100));
            g.fillOval((int)x-7,(int)y-7,15,15);
        }
    }

    // ── Explosion ──────────────────────────────────────────────────────────
    class Explosion {
        int cx,cy,r=4,alpha=230; boolean dead, big;
        List<int[]> sparks = new ArrayList<>();
        Explosion(int cx, int cy, boolean big) {
            this.cx=cx; this.cy=cy; this.big=big;
            int n = big ? 12 : 7;
            for (int i=0;i<n;i++) {
                double a=rng.nextDouble()*Math.PI*2;
                float spd=2+rng.nextFloat()*(big?5:3);
                sparks.add(new int[]{(int)(Math.cos(a)*spd*10),(int)(Math.sin(a)*spd*10),255});
            }
        }
        void update() {
            r += big?5:3; alpha-=big?10:14;
            for (int[] sp : sparks) sp[2]-=15;
            if(alpha<=0) dead=true;
        }
        void draw(Graphics2D g) {
            g.setColor(new Color(255,200,20,Math.max(0,alpha)));
            g.fillOval(cx-r,cy-r,r*2,r*2);
            g.setColor(new Color(255,100,20,Math.max(0,alpha/2)));
            g.fillOval(cx-r/2,cy-r/2,r,r);
            for (int[] sp : sparks) {
                if (sp[2]<=0) continue;
                g.setColor(new Color(255,200,50,Math.max(0,sp[2])));
                int sx=cx+sp[0]*alpha/255, sy=cy+sp[1]*alpha/255;
                g.fillOval(sx-2,sy-2,4,4);
            }
        }
    }

    // ── Powerup ────────────────────────────────────────────────────────────
    class Powerup {
        int x, y=-36, speed=2, type;
        static final Color[] COLS={
            new Color(0,180,255),   // shield = blue
            new Color(255,60,60),   // rapid  = red
            new Color(160,60,255),  // warp   = purple
            new Color(60,230,100)   // life   = green
        };
        static final String[] LABELS={"🔵","🔴","🟣","🟢"};
        static final String[] NAMES={"SHIELD","RAPID","WARP","+LIFE"};

        Powerup() { type=rng.nextInt(4); x=rng.nextInt(W-36); }

        void draw(Graphics2D g) {
            Color c=COLS[type];
            // Glow
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),50));
            g.fillOval(x-8,y-8,52,52);
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),120));
            g.fillOval(x-4,y-4,44,44);
            // Body
            g.setColor(c);
            g.fillOval(x,y,36,36);
            g.setColor(C_WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x,y,36,36);
            g.setStroke(new BasicStroke(1));
            // Label
            g.setFont(new Font("Arial",Font.PLAIN,18));
            g.drawString(LABELS[type], x+9, y+25);
        }
    }

    // ── FloatText ──────────────────────────────────────────────────────────
    class FloatText {
        String txt; int x; float y; Color c; float life=1f; boolean dead;
        FloatText(String t, int x, int y, Color c){txt=t;this.x=x;this.y=y;this.c=c;}
        void update(){y-=1.6f;life-=0.022f;if(life<=0)dead=true;}
        void draw(Graphics2D g){
            g.setFont(fSmall);
            FontMetrics fm=g.getFontMetrics();
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(life*255)));
            g.drawString(txt, x-fm.stringWidth(txt)/2, (int)y);
        }
    }
}
