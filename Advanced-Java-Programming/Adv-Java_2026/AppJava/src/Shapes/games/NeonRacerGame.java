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
 * NEON RACER: Circuit Breaker
 * ─────────────────────────────────────────────────────────────────────────────
 * Top-down neon arcade racing game with 4 unique tracks.
 *
 * HOW TO RUN:
 *   javac NeonRacerGame.java
 *   java Shapes.games.NeonRacerGame
 *
 * CONTROLS:
 *   UP / W        – Accelerate
 *   DOWN / S      – Brake / Reverse
 *   LEFT / A      – Steer Left
 *   RIGHT / D     – Steer Right
 *   ENTER         – Start / Resume
 *   ESC           – Pause
 *   R             – Restart
 *   M             – Mute / Unmute
 *   Q             – Quit to Menu
 *   N             – Next track (after winning)
 *
 * GOAL:
 *   Complete the required number of laps as fast as possible.
 *   Collect BOOST pads (yellow) for a speed burst.
 *   Avoid OIL SLICKS (dark) – they spin you out.
 *   Beat the AI opponents to earn 3 stars.
 */
public class NeonRacerGame extends JPanel implements ActionListener, KeyListener {

    // ── Window ────────────────────────────────────────────────────────────
    static final int W = 900, H = 660;
    static final int FPS = 60;
    static final int HUD_H = 64;

    // ── Physics constants ─────────────────────────────────────────────────
    static final double ACCEL      = 0.28;
    static final double BRAKE_DEC  = 0.38;
    static final double FRICTION   = 0.965;
    static final double TURN_SPEED = 3.2;   // degrees per frame
    static final double MAX_SPEED  = 6.5;
    static final double BOOST_SPEE = 10.5;
    static final double BOOST_DUR  = 90;    // frames
    static final double GRASS_MULT = 0.55;
    static final double ICE_FRIC   = 0.992;
    static final double OIL_SPIN   = 8.0;   // extra rotation deg per frame

    // ── Colours ───────────────────────────────────────────────────────────
    static final Color C_BLACK   = new Color(4,   2,  14);
    static final Color C_TRACK   = new Color(38,  35,  55);
    static final Color C_TRACK_L = new Color(55,  50,  75);
    static final Color C_KERB_R  = new Color(210, 30,  30);
    static final Color C_KERB_W  = new Color(240, 240, 240);
    static final Color C_GRASS   = new Color(18,  68,  22);
    static final Color C_GRASS_L = new Color(24,  88,  30);
    static final Color C_ICE     = new Color(170, 220, 255);
    static final Color C_OIL     = new Color(20,  15,  30);
    static final Color C_BOOST   = new Color(255, 220, 30);
    static final Color C_LINE    = new Color(255, 255, 255, 160);
    static final Color C_NEON_P  = new Color(0,   210, 255);  // player neon
    static final Color C_NEON_A  = new Color(255, 60,  180);  // AI neon
    static final Color C_GOLD    = new Color(255, 210, 40);
    static final Color C_GREEN   = new Color(40,  220, 100);
    static final Color C_RED     = new Color(220, 50,  50);
    static final Color C_HUD_BG  = new Color(6,   3,   18, 220);

    // ── Screens ───────────────────────────────────────────────────────────
    enum Screen { MENU, RACING, PAUSED, FINISH, GAME_OVER }
    Screen screen = Screen.MENU;

    // ── Track definitions ─────────────────────────────────────────────────
    // Each track: list of (cx,cy) waypoints (spline path), then properties
    static final int[][][] TRACKS = {
        // Track 1 – "Neon Loop"  (oval with chicane)
        { {120,500},{90,380},{90,250},{120,150},{220,100},{380,80},{540,80},
          {700,100},{800,180},{820,310},{800,440},{700,520},{560,550},{400,555},
          {260,540},{140,520} },
        // Track 2 – "The Figure Eight"
        { {160,500},{100,440},{80,360},{100,280},{160,220},{250,190},{360,200},
          {440,260},{460,330},{440,400},{480,460},{560,500},{660,490},{740,450},
          {790,380},{800,300},{780,220},{730,160},{640,130},{540,150},{460,200},
          {420,270},{400,340},{360,400},{280,440},{200,450} },
        // Track 3 – "Mountain Pass" (tight S-bends)
        { {120,520},{80,430},{80,340},{110,260},{170,200},{250,160},{340,155},
          {420,175},{480,230},{490,300},{470,370},{430,430},{420,500},{460,555},
          {540,570},{630,555},{710,510},{770,450},{800,370},{800,280},{770,200},
          {720,145},{640,115},{560,120},{490,160},{440,210},{400,260},
          {380,320},{380,390},{410,450},{460,490} },
        // Track 4 – "City Grid" (right angles, very tight)
        { {100,540},{100,200},{200,120},{400,120},{600,120},{750,200},{800,320},
          {800,520},{680,580},{500,580},{300,580},{140,560} }
    };

    static final String[] TRACK_NAMES = {
        "NEON LOOP", "FIGURE EIGHT", "MOUNTAIN PASS", "CITY GRID"
    };
    static final int[]   LAP_COUNTS   = {3, 3, 4, 5};
    static final int[]   AI_COUNTS    = {2, 3, 3, 4};
    static final Color[] TRACK_COLORS = {
        new Color(38,35,55), new Color(30,40,50),
        new Color(35,38,30), new Color(30,30,45)
    };
    static final Color[] GRASS_COLORS = {
        C_GRASS, new Color(10,55,50), new Color(30,65,20), new Color(22,22,45)
    };

    // ── Game state ─────────────────────────────────────────────────────────
    int trackIdx = 0;
    int totalLaps;
    CarEntity player;
    List<CarEntity>   aiCars      = new CopyOnWriteArrayList<>();
    List<TrackObject> trackObjs   = new CopyOnWriteArrayList<>(); // boosts, oils, ice
    List<Particle>    particles   = new CopyOnWriteArrayList<>();

    // Smooth camera
    double camX = 0, camY = 0;

    // Race timing
    long   raceStartMs  = 0;
    long   finishTimeMs = 0;
    int    bestLapMs    = Integer.MAX_VALUE;
    long   lapStartMs   = 0;
    String finishMsg    = "";
    int    playerFinPos = 1;

    // Track geometry (inflated from waypoints)
    int[]   wpX, wpY;           // waypoints
    Polygon trackOuter;         // outer polygon
    Polygon trackInner;         // inner polygon (hole)
    int     trackWidth = 92;    // px

    // Keys
    boolean keyUp, keyDown, keyLeft, keyRight;
    boolean musicMuted = false;

    long tick = 0;
    Random rng = new Random();
    SynthSound sound;
    javax.swing.Timer gameTimer;

    // ── Constructor ────────────────────────────────────────────────────────
    public NeonRacerGame() {
        setPreferredSize(new Dimension(W, H));
        setBackground(C_BLACK);
        setFocusable(true);
        addKeyListener(this);
        sound     = new SynthSound();
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    // ── Track / Race setup ────────────────────────────────────────────────
    void buildTrack(int idx) {
        int[][] pts = TRACKS[idx];
        wpX = new int[pts.length];
        wpY = new int[pts.length];
        for (int i=0;i<pts.length;i++) { wpX[i]=pts[i][0]; wpY[i]=pts[i][1]; }

        // Build outer + inner polygons by offsetting each waypoint perpendicular to path
        int n = pts.length;
        int[] ox = new int[n], oy = new int[n];
        int[] ix = new int[n], iy = new int[n];
        for (int i=0;i<n;i++) {
            int prev=(i-1+n)%n, next=(i+1)%n;
            double dx=pts[next][0]-pts[prev][0], dy=pts[next][1]-pts[prev][1];
            double len=Math.sqrt(dx*dx+dy*dy);
            if (len<1) len=1;
            double nx=-dy/len, ny=dx/len;
            int hw = trackWidth/2;
            ox[i]=(int)(pts[i][0]+nx*hw); oy[i]=(int)(pts[i][1]+ny*hw);
            ix[i]=(int)(pts[i][0]-nx*hw); iy[i]=(int)(pts[i][1]-ny*hw);
        }
        trackOuter = new Polygon(ox,oy,n);
        trackInner = new Polygon(ix,iy,n);

        // Place track objects
        trackObjs.clear();
        placeTrackObjects(idx);
    }

    void placeTrackObjects(int idx) {
        int n = wpX.length;
        // Boost pads (every 3rd waypoint)
        for (int i=0;i<n;i+=3) {
            int mx=(wpX[i]+wpX[(i+1)%n])/2, my=(wpY[i]+wpY[(i+1)%n])/2;
            if (isOnTrack(mx,my)) trackObjs.add(new TrackObject(mx,my,TrackObject.Type.BOOST));
        }
        // Oil slicks (random on track)
        Random sr=new Random(idx*100+7);
        for (int i=0;i<4+idx;i++) {
            int wi=sr.nextInt(n);
            double ang=sr.nextDouble()*Math.PI*2;
            int ox2=(int)(wpX[wi]+Math.cos(ang)*20), oy2=(int)(wpY[wi]+Math.sin(ang)*20);
            if (isOnTrack(ox2,oy2)) trackObjs.add(new TrackObject(ox2,oy2,TrackObject.Type.OIL));
        }
        // Ice patches (track 3+)
        if (idx>=2) {
            for (int i=1;i<n;i+=4) {
                int mx=(wpX[i]+wpX[(i+2)%n])/2+15, my=(wpY[i]+wpY[(i+2)%n])/2+15;
                if (isOnTrack(mx,my)) trackObjs.add(new TrackObject(mx,my,TrackObject.Type.ICE));
            }
        }
    }

    void startRace() {
        buildTrack(trackIdx);
        totalLaps = LAP_COUNTS[trackIdx];
        particles.clear();

        // Place player at first waypoint, facing toward second
        double startAngle = Math.toDegrees(Math.atan2(wpY[1]-wpY[0], wpX[1]-wpX[0]));
        player = new CarEntity(wpX[0], wpY[0], startAngle, true, C_NEON_P);

        // AI cars
        aiCars.clear();
        Color[] aiColors = {C_NEON_A, new Color(0,255,140), new Color(255,140,0), new Color(180,0,255)};
        for (int i=0;i<AI_COUNTS[trackIdx];i++) {
            // Stagger starting positions
            int sWp = (i+1) % wpX.length;
            double ang = Math.toDegrees(Math.atan2(
                wpY[(sWp+1)%wpY.length]-wpY[sWp],
                wpX[(sWp+1)%wpX.length]-wpX[sWp]));
            aiCars.add(new CarEntity(wpX[sWp]+i*10, wpY[sWp]-i*8, ang, false, aiColors[i%aiColors.length]));
        }

        raceStartMs  = System.currentTimeMillis();
        lapStartMs   = raceStartMs;
        bestLapMs    = Integer.MAX_VALUE;
        finishTimeMs = 0;
        camX = player.x - W/2.0;
        camY = player.y - H/2.0;
    }

    // ── Game loop ──────────────────────────────────────────────────────────
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        if (screen == Screen.RACING) update();
        repaint();
    }

    void update() {
        // Player physics
        if (player.lapsCompleted < totalLaps) {
            if (keyUp)   player.speed += ACCEL;
            if (keyDown) player.speed -= BRAKE_DEC;
            if (!keyUp && !keyDown) player.speed *= FRICTION;

            double turn = TURN_SPEED * (player.speed / MAX_SPEED);
            if (keyLeft)  player.angle -= turn;
            if (keyRight) player.angle += turn;

            // Oil spin
            if (player.oilTimer > 0) {
                player.angle += OIL_SPIN * Math.signum(player.speed);
                player.oilTimer--;
            }
            // Boost
            if (player.boostTimer > 0) {
                player.speed = Math.min(player.speed, BOOST_SPEE);
                player.boostTimer--;
            }

            // Terrain
            boolean onTrack = isOnTrack((int)player.x,(int)player.y);
            if (!onTrack) { player.speed *= GRASS_MULT; spawnGrassParticle(player); }

            // Ice (friction change)
            boolean onIce = isOnIce(player);
            if (onIce) { player.speed *= ICE_FRIC / FRICTION; } // less friction

            // Speed cap
            double maxSpd = (player.boostTimer>0) ? BOOST_SPEE : MAX_SPEED;
            player.speed = Math.max(-maxSpd*0.4, Math.min(maxSpd, player.speed));

            player.update();

            // Boundary clamp
            player.x = Math.max(30, Math.min(W+200, player.x));
            player.y = Math.max(30, Math.min(H+200, player.y));

            // Check track objects
            checkPickups(player);

            // Checkpoint / lap logic
            checkLap(player);
        }

        // AI
        for (CarEntity ai : aiCars) {
            if (ai.lapsCompleted < totalLaps) {
                updateAI(ai);
                checkPickups(ai);
                checkLap(ai);
            }
        }

        // Camera smooth follow
        double targetCX = player.x - W/2.0;
        double targetCY = player.y - H/2.0;
        camX += (targetCX - camX) * 0.12;
        camY += (targetCY - camY) * 0.12;

        // Particles
        particles.removeIf(p -> { p.update(); return p.dead; });

        // Race finish check
        if (player.lapsCompleted >= totalLaps && finishTimeMs == 0) {
            finishTimeMs = System.currentTimeMillis() - raceStartMs;
            playerFinPos = 1;
            for (CarEntity ai : aiCars)
                if (ai.lapsCompleted >= totalLaps) playerFinPos++;
            sound.play("finish");
            screen = Screen.FINISH;
        }
    }

    void updateAI(CarEntity ai) {
        // Navigate toward next waypoint
        int target = ai.nextWaypoint % wpX.length;
        double dx = wpX[target] - ai.x;
        double dy = wpY[target] - ai.y;
        double dist = Math.sqrt(dx*dx+dy*dy);

        if (dist < 35) {
            ai.nextWaypoint = (ai.nextWaypoint + 1) % wpX.length;
            target = ai.nextWaypoint;
            dx = wpX[target] - ai.x;
            dy = wpY[target] - ai.y;
        }

        double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
        double diff = targetAngle - ai.angle;
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;

        double aiTurn = TURN_SPEED * 0.85 + trackIdx * 0.08;
        if (diff > 2)       ai.angle += aiTurn;
        else if (diff < -2) ai.angle -= aiTurn;

        // AI speed varies
        double aiMaxSpeed = MAX_SPEED * (0.80 + trackIdx*0.05 + rng.nextDouble()*0.06);
        if (ai.boostTimer > 0) aiMaxSpeed = BOOST_SPEE * 0.85;
        ai.speed += ACCEL * 0.9;
        if (!isOnTrack((int)ai.x,(int)ai.y)) ai.speed *= GRASS_MULT;
        if (ai.oilTimer>0) { ai.angle += OIL_SPIN*0.7; ai.oilTimer--; }
        if (ai.boostTimer>0) ai.boostTimer--;
        ai.speed = Math.max(0, Math.min(aiMaxSpeed, ai.speed));
        ai.speed *= FRICTION;
        ai.update();
    }

    void checkPickups(CarEntity car) {
        for (TrackObject obj : trackObjs) {
            if (!obj.active) continue;
            double dx=car.x-obj.x, dy=car.y-obj.y;
            if (dx*dx+dy*dy < 20*20) {
                switch (obj.type) {
                    case BOOST -> {
                        car.boostTimer = (int)BOOST_DUR;
                        obj.respawnTimer = 180;
                        obj.active = false;
                        if (car.isPlayer) { sound.play("boost"); spawnBoostFx(car); }
                    }
                    case OIL -> {
                        if (car.oilTimer <= 0) {
                            car.oilTimer = 45;
                            if (car.isPlayer) { sound.play("oil"); }
                        }
                    }
                    case ICE -> { /* handled via isOnIce */ }
                }
            }
        }
        // Respawn boost pads
        for (TrackObject obj : trackObjs) {
            if (!obj.active && obj.respawnTimer > 0) {
                obj.respawnTimer--;
                if (obj.respawnTimer <= 0) obj.active = true;
            }
        }
    }

    void checkLap(CarEntity car) {
        // Use first two waypoints as finish line
        double fx1=wpX[0], fy1=wpY[0], fx2=wpX[1], fy2=wpY[1];
        double dx=fx2-fx1, dy=fy2-fy1;
        double len=Math.sqrt(dx*dx+dy*dy);
        // Project car onto line
        double t=((car.x-fx1)*dx+(car.y-fy1)*dy)/(len*len);
        double px=fx1+t*dx, py=fy1+t*dy;
        double dist=Math.sqrt((car.x-px)*(car.x-px)+(car.y-py)*(car.y-py));

        if (dist < 28 && t>=0 && t<=1) {
            long now = System.currentTimeMillis();
            if (car.lastLapCross < 0 || (now - car.lastLapCross) > 2000) {
                if (car.lastLapCross > 0) {
                    car.lapsCompleted++;
                    if (car.isPlayer) {
                        int lapMs = (int)(now - lapStartMs);
                        if (lapMs < bestLapMs) bestLapMs = lapMs;
                        lapStartMs = now;
                        sound.play("lap");
                        spawnLapFx(car);
                    }
                }
                car.lastLapCross = now;
            }
        }
    }

    boolean isOnTrack(int x, int y) {
        if (trackOuter == null) return true;
        return trackOuter.contains(x, y) || trackInner.contains(x, y) ||
               isNearPath(x, y, trackWidth/2 + 6);
    }

    boolean isNearPath(int x, int y, int radius) {
        int n = wpX.length;
        for (int i=0;i<n;i++) {
            int nx=wpX[i], ny=wpY[i];
            double dx=x-nx, dy=y-ny;
            if (dx*dx+dy*dy < radius*(double)radius) return true;
        }
        return false;
    }

    boolean isOnIce(CarEntity car) {
        for (TrackObject obj : trackObjs) {
            if (obj.type != TrackObject.Type.ICE) continue;
            double dx=car.x-obj.x, dy=car.y-obj.y;
            if (dx*dx+dy*dy < 32*32) return true;
        }
        return false;
    }

    // ── Particles ─────────────────────────────────────────────────────────
    void spawnGrassParticle(CarEntity car) {
        if (tick%3!=0) return;
        for (int i=0;i<3;i++) {
            float vx=(float)(rng.nextGaussian()*2), vy=(float)(rng.nextGaussian()*2);
            particles.add(new Particle((int)car.x,(int)car.y-HUD_H,
                    new Color(30+rng.nextInt(40),100+rng.nextInt(60),20),vx,vy,20));
        }
    }
    void spawnBoostFx(CarEntity car) {
        for (int i=0;i<16;i++) {
            float vx=(float)(rng.nextGaussian()*4), vy=(float)(rng.nextGaussian()*4);
            particles.add(new Particle((int)car.x,(int)car.y-HUD_H,C_BOOST,vx,vy,35));
        }
    }
    void spawnLapFx(CarEntity car) {
        for (int i=0;i<24;i++) {
            double ang=rng.nextDouble()*Math.PI*2;
            float vx=(float)(Math.cos(ang)*5), vy=(float)(Math.sin(ang)*5);
            particles.add(new Particle((int)car.x,(int)car.y-HUD_H,C_GREEN,vx,vy,50));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // PAINT
    // ══════════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        switch (screen) {
            case MENU    -> drawMenu(g);
            case RACING  -> drawRace(g);
            case PAUSED  -> { drawRace(g); drawPause(g); }
            case FINISH  -> { drawRace(g); drawFinish(g); }
            case GAME_OVER -> drawGameOver(g);
        }
    }

    // ── Menu ──────────────────────────────────────────────────────────────
    void drawMenu(Graphics2D g) {
        // Deep space bg
        g.setColor(C_BLACK);
        g.fillRect(0,0,W,H);
        // Grid lines (neon perspective)
        drawNeonGrid(g);

        // Title
        drawNeonText(g, new Font("Courier New",Font.BOLD,50), "NEON RACER",
                C_NEON_P, W/2, H/2-160);
        drawNeonText(g, new Font("Courier New",Font.BOLD,20), "CIRCUIT BREAKER",
                C_NEON_A, W/2, H/2-108);

        // Track selector
        g.setFont(new Font("Courier New",Font.BOLD,16));
        drawCentered(g,"SELECT TRACK", new Color(180,180,220), H/2-60);
        for (int i=0;i<TRACK_NAMES.length;i++) {
            boolean sel = (i==trackIdx);
            Color c = sel ? C_GOLD : new Color(120,115,150);
            String arrow = sel ? "  ►  " : "     ";
            drawCentered(g, arrow+TRACK_NAMES[i]+"  ("+LAP_COUNTS[i]+" laps)",
                         c, H/2-26+i*30);
        }

        // Controls
        g.setFont(new Font("Courier New",Font.PLAIN,15));
        String[] ctrl = {
            "◄ / ► ARROW  –  Select Track",
            "ENTER  –  Start Race",
            "UP/W  –  Accelerate     DOWN/S  –  Brake",
            "LEFT/A  –  Steer Left   RIGHT/D  –  Steer Right",
            "R  –  Restart    ESC  –  Pause    M  –  Mute    Q  –  Quit"
        };
        for (int i=0;i<ctrl.length;i++)
            drawCentered(g, ctrl[i], new Color(160,155,195), H/2+100+i*24);

        // Mini car previews
        drawMiniCar(g, W/2-60, H/2+240, 0, C_NEON_P);
        drawMiniCar(g, W/2+60, H/2+240, 180, C_NEON_A);
    }

    void drawNeonGrid(Graphics2D g) {
        int horizon = H/2;
        g.setStroke(new BasicStroke(0.8f));
        for (int x=0; x<W; x+=50) {
            int a = Math.max(0, 40-(int)(Math.abs(x-W/2)*0.05));
            g.setColor(new Color(0,180,255,a));
            g.drawLine(x,horizon,W/2, H+50);
        }
        for (int yi=0;yi<12;yi++) {
            int y=horizon+yi*((H-horizon)/12);
            int a=Math.max(0,50-yi*3);
            g.setColor(new Color(0,180,255,a));
            g.drawLine(0,y,W,y);
        }
        g.setStroke(new BasicStroke(1));
    }

    // ── Race ──────────────────────────────────────────────────────────────
    void drawRace(Graphics2D g) {
        g.setColor(GRASS_COLORS[trackIdx]);
        g.fillRect(0, HUD_H, W, H-HUD_H);

        // Grass texture dots
        Random gr = new Random(trackIdx*31+7);
        for (int i=0;i<200;i++) {
            int gx=gr.nextInt(W), gy=gr.nextInt(H-HUD_H)+HUD_H;
            g.setColor(new Color(0,0,0,20));
            g.fillOval(gx,gy,3,3);
        }

        Graphics2D gc = (Graphics2D) g.create();
        gc.translate(-camX, -camY + HUD_H);

        // Draw track surface
        drawTrackSurface(gc);

        // Track objects (boost, oil, ice)
        for (TrackObject obj : trackObjs) if (obj.active) drawTrackObj(gc, obj);

        // Particles (world space)
        for (Particle p : particles) drawParticle(gc, p);

        // AI cars
        for (CarEntity ai : aiCars) drawCar(gc, ai);

        // Player car
        drawCar(gc, player);

        // Finish line at waypoint 0
        drawFinishLine(gc);

        gc.dispose();

        drawHud(g);
    }

    void drawTrackSurface(Graphics2D g) {
        if (wpX == null) return;
        int n = wpX.length;
        Color trackC = TRACK_COLORS[trackIdx];
        Color trackL = new Color(
            Math.min(255,trackC.getRed()+18),
            Math.min(255,trackC.getGreen()+18),
            Math.min(255,trackC.getBlue()+18));

        // Draw track segments as thick polyline using filled quads
        g.setColor(trackC);
        int hw = trackWidth/2;
        for (int i=0;i<n;i++) {
            int ni=(i+1)%n;
            double dx=wpX[ni]-wpX[i], dy=wpY[ni]-wpY[i];
            double len=Math.sqrt(dx*dx+dy*dy);
            if (len<1) continue;
            double nx=-dy/len*hw, ny=dx/len*hw;
            int[] px={wpX[i]+(int)nx, wpX[ni]+(int)nx, wpX[ni]-(int)nx, wpX[i]-(int)nx};
            int[] py={wpY[i]+(int)ny, wpY[ni]+(int)ny, wpY[ni]-(int)ny, wpY[i]-(int)ny};
            g.setColor(((i/3)%2==0) ? trackC : trackL);
            g.fillPolygon(px,py,4);

            // Kerb strips at edges
            for (int side=-1;side<=1;side+=2) {
                double kw=8;
                double ko=(hw-kw/2)*side;
                double ko2=(hw+kw/2)*side;
                int[] kpx={(int)(wpX[i]+nx/hw*ko),(int)(wpX[ni]+nx/hw*ko),
                            (int)(wpX[ni]+nx/hw*ko2),(int)(wpX[i]+nx/hw*ko2)};
                int[] kpy={(int)(wpY[i]+ny/hw*ko),(int)(wpY[ni]+ny/hw*ko),
                            (int)(wpY[ni]+ny/hw*ko2),(int)(wpY[i]+ny/hw*ko2)};
                boolean redKerb = ((i/2)%2==0);
                g.setColor(redKerb ? C_KERB_R : C_KERB_W);
                g.fillPolygon(kpx,kpy,4);
            }
        }

        // Centre dashed line
        g.setColor(C_LINE);
        g.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,
                0,new float[]{14,10},(float)(tick%24)));
        for (int i=0;i<n;i++) {
            int ni=(i+1)%n;
            g.drawLine(wpX[i],wpY[i],wpX[ni],wpY[ni]);
        }
        g.setStroke(new BasicStroke(1));

        // Neon track border glow
        Stroke glowStroke = new BasicStroke(hw*2+4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(glowStroke);
        for (int i=0;i<n;i++) {
            int ni=(i+1)%n;
            g.setColor(new Color(C_NEON_P.getRed(),C_NEON_P.getGreen(),C_NEON_P.getBlue(),8));
            g.drawLine(wpX[i],wpY[i],wpX[ni],wpY[ni]);
        }
        g.setStroke(new BasicStroke(1));
    }

    void drawTrackObj(Graphics2D g, TrackObject obj) {
        int x=(int)obj.x, y=(int)obj.y;
        switch (obj.type) {
            case BOOST -> {
                int pulse = (int)(Math.abs(Math.sin(tick*0.10))*30)+20;
                g.setColor(new Color(255,220,30,pulse));
                g.fillOval(x-18,y-18,36,36);
                g.setColor(C_BOOST);
                g.setStroke(new BasicStroke(2.5f));
                g.drawOval(x-12,y-12,24,24);
                g.setStroke(new BasicStroke(1));
                g.setFont(new Font("Courier New",Font.BOLD,14));
                FontMetrics fm=g.getFontMetrics();
                g.drawString("▶▶", x-fm.stringWidth("▶▶")/2, y+5);
            }
            case OIL -> {
                g.setColor(new Color(20,15,30,180));
                g.fillOval(x-20,y-14,40,28);
                g.setColor(new Color(60,50,80,120));
                g.fillOval(x-16,y-10,32,20);
                // Rainbow sheen
                for (int r=0;r<360;r+=30) {
                    double rad=Math.toRadians(r);
                    int cx2=(int)(x+Math.cos(rad)*8), cy2=(int)(y+Math.sin(rad)*5);
                    g.setColor(new Color(Color.HSBtoRGB(r/360f,0.7f,0.6f)));
                    g.setColor(new Color(
                        ((Color)g.getPaint()).getRed(),
                        ((Color)g.getPaint()).getGreen(),
                        ((Color)g.getPaint()).getBlue(),80));
                    g.fillOval(cx2-3,cy2-3,6,6);
                }
            }
            case ICE -> {
                g.setColor(new Color(170,220,255,100));
                g.fillOval(x-26,y-20,52,40);
                g.setColor(new Color(200,235,255,140));
                g.setStroke(new BasicStroke(1.5f));
                g.drawOval(x-22,y-16,44,32);
                g.setStroke(new BasicStroke(1));
                // Snowflake
                g.setColor(new Color(220,245,255,180));
                g.setStroke(new BasicStroke(1.5f));
                for (int a=0;a<6;a++) {
                    double rad=Math.toRadians(a*60);
                    g.drawLine(x,y,(int)(x+Math.cos(rad)*12),(int)(y+Math.sin(rad)*12));
                }
                g.setStroke(new BasicStroke(1));
            }
        }
    }

    void drawCar(Graphics2D g, CarEntity car) {
        Graphics2D gc=(Graphics2D)g.create();
        gc.translate(car.x, car.y);
        gc.rotate(Math.toRadians(car.angle + 90));

        Color body = car.carColor;
        Color dark = body.darker().darker();
        Color lite = body.brighter();

        // Neon glow
        int glowA = (car.boostTimer>0) ? 80 : (car.oilTimer>0) ? 40 : 20;
        gc.setColor(new Color(body.getRed(),body.getGreen(),body.getBlue(),glowA));
        gc.fillRoundRect(-14,-22,28,44,8,8);

        // Car body
        gc.setColor(dark);
        gc.fillRoundRect(-10,-18,20,36,6,6);
        gc.setColor(body);
        gc.fillRoundRect(-9,-17,18,18,5,5); // front half
        gc.setColor(dark);
        gc.fillRoundRect(-9,-1,18,16,5,5);  // rear half

        // Windscreen
        gc.setColor(new Color(180,240,255,160));
        gc.fillRoundRect(-6,-14,12,9,3,3);
        gc.setColor(new Color(255,255,255,60));
        gc.fillRoundRect(-5,-13,5,4,2,2);

        // Headlights
        gc.setColor(new Color(255,255,200));
        gc.fillOval(-8,-18,5,5);
        gc.fillOval(3,-18,5,5);
        // Light beam (if boosting or fast)
        if (car.boostTimer>0 || car.speed > MAX_SPEED*0.7) {
            gc.setColor(new Color(255,255,200,40));
            gc.fillPolygon(new int[]{-5,5,-15},new int[]{-18,-18,-40},3);
        }

        // Tail lights
        gc.setColor(new Color(255,30,30));
        gc.fillOval(-8,14,5,5);
        gc.fillOval(3,14,5,5);

        // Wheels
        gc.setColor(new Color(20,18,25));
        gc.fillRoundRect(-14,-14,6,10,2,2); gc.fillRoundRect(-14,6,6,10,2,2);
        gc.fillRoundRect(8,-14,6,10,2,2);   gc.fillRoundRect(8,6,6,10,2,2);
        gc.setColor(new Color(55,50,65));
        gc.fillOval(-13,-12,4,6); gc.fillOval(-13,8,4,6);
        gc.fillOval(9,-12,4,6);   gc.fillOval(9,8,4,6);

        // Exhaust flame when boosting
        if (car.boostTimer > 0) {
            double flameLen = 8+rng.nextInt(8);
            gc.setColor(new Color(255,140,0,180));
            gc.fillOval(-4,(int)(18+flameLen*0.5),(int)(8),(int)flameLen);
            gc.setColor(new Color(255,230,50,140));
            gc.fillOval(-2,(int)(18+flameLen*0.2),4,(int)(flameLen*0.6));
        }

        // Oil spin indicator
        if (car.oilTimer > 0) {
            gc.setColor(new Color(120,80,200,150));
            gc.setStroke(new BasicStroke(2));
            gc.drawOval(-14,-20,28,40);
            gc.setStroke(new BasicStroke(1));
        }

        gc.dispose();

        // Player name tag
        if (car.isPlayer) {
            g.setFont(new Font("Courier New",Font.BOLD,11));
            g.setColor(new Color(0,210,255,200));
            FontMetrics fm=g.getFontMetrics();
            g.drawString("YOU", (int)car.x-fm.stringWidth("YOU")/2, (int)car.y-28);
        }
    }

    void drawFinishLine(Graphics2D g) {
        int n=wpX.length;
        double dx=wpX[1]-wpX[0], dy=wpY[1]-wpY[0];
        double len=Math.sqrt(dx*dx+dy*dy);
        double nx=-dy/len*(trackWidth/2+8), ny=dx/len*(trackWidth/2+8);
        // Chequered finish line
        int segs = 8;
        for (int i=0;i<segs;i++) {
            double t1=i/(double)segs, t2=(i+1)/(double)segs;
            double x1=wpX[0]+nx*2*t1, y1=wpY[0]+ny*2*t1;
            double x2=wpX[0]+nx*2*t2, y2=wpY[0]+ny*2*t2;
            g.setColor((i%2==0) ? Color.WHITE : Color.BLACK);
            g.setStroke(new BasicStroke(7, BasicStroke.CAP_FLAT, BasicStroke.JOIN_MITER));
            g.drawLine((int)(wpX[0]-nx+(x1-wpX[0])), (int)(wpY[0]-ny+(y1-wpY[0])),
                       (int)(wpX[0]-nx+(x2-wpX[0])), (int)(wpY[0]-ny+(y2-wpY[0])));
        }
        // Duplicate on other side
        for (int i=0;i<segs;i++) {
            double t1=i/(double)segs, t2=(i+1)/(double)segs;
            double x1=-nx*2*t1+wpX[0], y1=-ny*2*t1+wpY[0];
            double x2=-nx*2*t2+wpX[0], y2=-ny*2*t2+wpY[0];
            g.setColor((i%2==1) ? Color.WHITE : Color.BLACK);
            g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
        }
        g.setStroke(new BasicStroke(1));
    }

    void drawHud(Graphics2D g) {
        g.setColor(C_HUD_BG);
        g.fillRect(0,0,W,HUD_H);
        g.setColor(new Color(0,200,255,80));
        g.setStroke(new BasicStroke(2));
        g.drawLine(0,HUD_H,W,HUD_H);
        g.setStroke(new BasicStroke(1));

        // Track name
        g.setFont(new Font("Courier New",Font.BOLD,18));
        g.setColor(C_GOLD);
        g.drawString(TRACK_NAMES[trackIdx], 14, 24);

        // Lap counter
        int lap = Math.min(player.lapsCompleted+1, totalLaps);
        g.setFont(new Font("Courier New",Font.BOLD,16));
        g.setColor(C_NEON_P);
        g.drawString("LAP  "+lap+" / "+totalLaps, 14, 48);

        // Timer
        long elapsed = System.currentTimeMillis() - raceStartMs;
        g.setColor(C_WHITE);
        g.setFont(new Font("Courier New",Font.BOLD,22));
        drawCentered(g, formatTime(elapsed), C_WHITE, 30);

        // Best lap
        g.setFont(new Font("Courier New",Font.PLAIN,13));
        g.setColor(new Color(180,175,215));
        String best = bestLapMs==Integer.MAX_VALUE ? "--:--.---" : formatTime(bestLapMs);
        drawCentered(g, "BEST LAP "+best, new Color(180,175,215), 50);

        // Speedometer
        drawSpeedometer(g, W-130, 10, player.speed);

        // Position
        int pos = getPlayerPosition();
        g.setFont(new Font("Courier New",Font.BOLD,20));
        g.setColor(pos==1 ? C_GOLD : C_NEON_P);
        g.drawString("P"+pos, W-180, 32);

        // Boost bar
        if (player.boostTimer>0) {
            int bw=100;
            g.setColor(new Color(255,220,30,80));
            g.fillRoundRect(W/2-bw/2,HUD_H-16,bw,10,4,4);
            g.setColor(C_BOOST);
            g.fillRoundRect(W/2-bw/2,HUD_H-16,(int)(bw*(player.boostTimer/BOOST_DUR)),10,4,4);
            g.setFont(new Font("Courier New",Font.BOLD,11));
            drawCentered(g,"BOOST",C_BOOST,HUD_H-20);
        }
        // Oil warning
        if (player.oilTimer>0) {
            g.setFont(new Font("Courier New",Font.BOLD,14));
            g.setColor((tick/4%2==0) ? C_RED : new Color(200,100,50));
            drawCentered(g,"⚠ SPIN OUT!",C_RED,HUD_H-14);
        }

        // Mini map
        drawMiniMap(g, W-75, HUD_H+10, 65);
    }

    void drawSpeedometer(Graphics2D g, int x, int y, double speed) {
        int r=22;
        g.setColor(new Color(0,0,0,120));
        g.fillOval(x,y,r*2,r*2);
        g.setColor(new Color(0,200,255,60));
        g.drawOval(x,y,r*2,r*2);
        // Needle
        double pct = Math.abs(speed)/(MAX_SPEED);
        double ang = Math.toRadians(-210 + pct*240);
        g.setColor(pct>0.85 ? C_RED : C_NEON_P);
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x+r, y+r,
                   x+r+(int)(Math.cos(ang)*r*0.8),
                   y+r+(int)(Math.sin(ang)*r*0.8));
        g.setStroke(new BasicStroke(1));
        g.setFont(new Font("Courier New",Font.BOLD,9));
        g.setColor(new Color(180,175,210));
        g.drawString("km/h", x+r-11, y+r*2+12);
    }

    void drawMiniMap(Graphics2D g, int mx, int my, int size) {
        if (wpX==null) return;
        // Find bounds
        int minX=Integer.MAX_VALUE,minY=Integer.MAX_VALUE,maxX=0,maxY=0;
        for (int i=0;i<wpX.length;i++) {
            minX=Math.min(minX,wpX[i]); minY=Math.min(minY,wpY[i]);
            maxX=Math.max(maxX,wpX[i]); maxY=Math.max(maxY,wpY[i]);
        }
        double sx=size/(double)(maxX-minX+1), sy=size/(double)(maxY-minY+1);
        double sc=Math.min(sx,sy)*0.85;

        g.setColor(new Color(0,0,0,160));
        g.fillRoundRect(mx-4,my-4,size+8,size+8,8,8);

        // Track path
        g.setColor(new Color(80,75,110));
        g.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        for (int i=0;i<wpX.length;i++) {
            int ni=(i+1)%wpX.length;
            int x1=(int)((wpX[i]-minX)*sc)+mx, y1=(int)((wpY[i]-minY)*sc)+my;
            int x2=(int)((wpX[ni]-minX)*sc)+mx, y2=(int)((wpY[ni]-minY)*sc)+my;
            g.drawLine(x1,y1,x2,y2);
        }
        g.setStroke(new BasicStroke(1));

        // AI blips
        for (CarEntity ai : aiCars) {
            int ax=(int)((ai.x-minX)*sc)+mx, ay=(int)((ai.y-minY)*sc)+my;
            g.setColor(ai.carColor);
            g.fillOval(ax-3,ay-3,6,6);
        }
        // Player blip
        int px2=(int)((player.x-minX)*sc)+mx, py2=(int)((player.y-minY)*sc)+my;
        g.setColor(C_WHITE);
        g.fillOval(px2-4,py2-4,8,8);
    }

    void drawParticle(Graphics2D g, Particle p) {
        int a=(int)(255f*p.life/p.maxLife);
        g.setColor(new Color(p.c.getRed(),p.c.getGreen(),p.c.getBlue(),Math.max(0,Math.min(255,a))));
        int sz=1+(int)(4f*p.life/p.maxLife);
        g.fillOval((int)p.x-sz/2,(int)p.y-sz/2,sz,sz);
    }

    // ── Overlays ──────────────────────────────────────────────────────────
    void drawPause(Graphics2D g) {
        g.setColor(new Color(0,0,0,175));
        g.fillRect(0,0,W,H);
        drawNeonText(g, new Font("Courier New",Font.BOLD,54),"PAUSED",C_GOLD,W/2,H/2-60);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        drawCentered(g,"ENTER – Resume",  Color.WHITE, H/2+10);
        drawCentered(g,"R     – Restart", Color.WHITE, H/2+46);
        drawCentered(g,"Q     – Menu",    Color.WHITE, H/2+82);
    }

    void drawFinish(Graphics2D g) {
        g.setColor(new Color(0,0,0,185));
        g.fillRect(0,0,W,H);
        String title = playerFinPos==1 ? "1ST PLACE!" : "P"+playerFinPos+" PLACE";
        Color tc = playerFinPos==1 ? C_GOLD : playerFinPos==2 ? new Color(200,200,210) : C_NEON_P;
        drawNeonText(g, new Font("Courier New",Font.BOLD,52), title, tc, W/2, H/2-90);

        g.setFont(new Font("Courier New",Font.BOLD,22));
        drawCentered(g,"TIME:  "+formatTime(finishTimeMs), Color.WHITE, H/2-20);
        drawCentered(g,"BEST LAP:  "+formatTime(bestLapMs), new Color(180,255,180), H/2+20);

        // Stars
        int stars = playerFinPos==1?3 : playerFinPos==2?2:1;
        for (int i=0;i<3;i++) {
            g.setFont(new Font("Serif",Font.BOLD,38));
            g.setColor(i<stars ? C_GOLD : new Color(60,55,80));
            g.drawString("★", W/2-55+i*40, H/2+80);
        }

        g.setFont(new Font("Courier New",Font.PLAIN,20));
        if (trackIdx < TRACK_NAMES.length-1)
            drawCentered(g,"N – Next Track",Color.WHITE,H/2+120);
        drawCentered(g,"R – Retry   Q – Menu",Color.WHITE,H/2+150);
    }

    void drawGameOver(Graphics2D g) {
        g.setColor(C_BLACK);
        g.fillRect(0,0,W,H);
        drawNeonText(g, new Font("Courier New",Font.BOLD,52),"RACE OVER",C_RED,W/2,H/2-60);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        drawCentered(g,"R – Restart  Q – Menu",Color.WHITE,H/2+30);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    void drawNeonText(Graphics2D g, Font f, String text, Color bright, int cx, int y) {
        g.setFont(f);
        FontMetrics fm=g.getFontMetrics(f);
        int tx=cx-fm.stringWidth(text)/2;
        for (int r=7;r>=1;r--) {
            g.setColor(new Color(bright.getRed(),bright.getGreen(),bright.getBlue(),
                        Math.min(255,8+r*16)));
            g.drawString(text,tx+r,y+r); g.drawString(text,tx-r,y-r);
            g.drawString(text,tx+r,y-r); g.drawString(text,tx-r,y+r);
        }
        g.setColor(bright.darker()); g.drawString(text,tx+2,y+2);
        g.setColor(bright);          g.drawString(text,tx,y);
    }

    void drawCentered(Graphics2D g, String text, Color c, int y) {
        g.setColor(c);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(text,(W-fm.stringWidth(text))/2,y);
    }

    void drawMiniCar(Graphics2D g, int cx, int cy, double angle, Color c) {
        Graphics2D gc=(Graphics2D)g.create();
        gc.translate(cx,cy);
        gc.rotate(Math.toRadians(angle));
        gc.setColor(c.darker());
        gc.fillRoundRect(-10,-18,20,36,6,6);
        gc.setColor(c);
        gc.fillRoundRect(-9,-17,18,18,5,5);
        gc.setColor(new Color(180,240,255,160));
        gc.fillRoundRect(-6,-14,12,9,3,3);
        gc.dispose();
    }

    String formatTime(long ms) {
        long min=ms/60000, sec=(ms%60000)/1000, millis=ms%1000;
        return String.format("%02d:%02d.%03d",min,sec,millis);
    }

    int getPlayerPosition() {
        int pos=1;
        for (CarEntity ai:aiCars)
            if (ai.lapsCompleted>player.lapsCompleted ||
                (ai.lapsCompleted==player.lapsCompleted && ai.nextWaypoint>player.nextWaypoint))
                pos++;
        return pos;
    }

    // ── Key input ─────────────────────────────────────────────────────────
    @Override public void keyPressed(KeyEvent e) {
        int k=e.getKeyCode();
        if (k==KeyEvent.VK_UP   ||k==KeyEvent.VK_W) keyUp=true;
        if (k==KeyEvent.VK_DOWN ||k==KeyEvent.VK_S) keyDown=true;
        if (k==KeyEvent.VK_LEFT ||k==KeyEvent.VK_A) keyLeft=true;
        if (k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) keyRight=true;

        switch(screen) {
            case MENU -> {
                if (k==KeyEvent.VK_LEFT  ||k==KeyEvent.VK_A) trackIdx=(trackIdx-1+TRACKS.length)%TRACKS.length;
                if (k==KeyEvent.VK_RIGHT ||k==KeyEvent.VK_D) trackIdx=(trackIdx+1)%TRACKS.length;
                if (k==KeyEvent.VK_ENTER){ startRace(); screen=Screen.RACING; sound.play("start"); }
                if (k==KeyEvent.VK_M)    musicMuted=!musicMuted;
                if (k==KeyEvent.VK_Q)    System.exit(0);
            }
            case RACING -> {
                if (k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if (k==KeyEvent.VK_R)     { startRace(); }
                if (k==KeyEvent.VK_M)     musicMuted=!musicMuted;
            }
            case PAUSED -> {
                if (k==KeyEvent.VK_ENTER) screen=Screen.RACING;
                if (k==KeyEvent.VK_R)    { startRace(); screen=Screen.RACING; }
                if (k==KeyEvent.VK_Q)     screen=Screen.MENU;
            }
            case FINISH -> {
                if (k==KeyEvent.VK_N && trackIdx<TRACKS.length-1){
                    trackIdx++; startRace(); screen=Screen.RACING; sound.play("start");
                }
                if (k==KeyEvent.VK_R){ startRace(); screen=Screen.RACING; sound.play("start"); }
                if (k==KeyEvent.VK_Q) screen=Screen.MENU;
            }
            case GAME_OVER -> {
                if (k==KeyEvent.VK_R){ startRace(); screen=Screen.RACING; sound.play("start"); }
                if (k==KeyEvent.VK_Q) screen=Screen.MENU;
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        int k=e.getKeyCode();
        if (k==KeyEvent.VK_UP   ||k==KeyEvent.VK_W) keyUp=false;
        if (k==KeyEvent.VK_DOWN ||k==KeyEvent.VK_S) keyDown=false;
        if (k==KeyEvent.VK_LEFT ||k==KeyEvent.VK_A) keyLeft=false;
        if (k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) keyRight=false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════
    // INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════

    class CarEntity {
        double x, y, angle, speed;
        boolean isPlayer;
        Color  carColor;
        int    lapsCompleted = 0;
        int    nextWaypoint  = 1;
        long   lastLapCross  = -1;
        int    boostTimer    = 0;
        int    oilTimer      = 0;

        CarEntity(int sx, int sy, double ang, boolean player, Color c) {
            x=sx; y=sy; angle=ang; isPlayer=player; carColor=c;
        }
        void update() {
            x += Math.cos(Math.toRadians(angle)) * speed;
            y += Math.sin(Math.toRadians(angle)) * speed;
        }
    }

    static class TrackObject {
        enum Type { BOOST, OIL, ICE }
        double x, y;
        Type   type;
        boolean active       = true;
        int     respawnTimer = 0;
        TrackObject(double x, double y, Type t) { this.x=x; this.y=y; this.type=t; }
    }

    static class Particle {
        float x,y,vx,vy; Color c; int life,maxLife; boolean dead;
        Particle(int x,int y,Color c,float vx,float vy,int life){
            this.x=x; this.y=y; this.c=c; this.vx=vx; this.vy=vy; this.life=this.maxLife=life;
        }
        void update(){ x+=vx;y+=vy;vx*=0.91f;vy*=0.91f; if(--life<=0) dead=true; }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SYNTHESIZED SOUND
    // ══════════════════════════════════════════════════════════════════════
    static class SynthSound {
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
                        line.open(fmt,2048); line.start();
                        line.write(pcm,0,pcm.length); line.drain();
                    }
                }catch(Exception ignored){}
            });
            t.setDaemon(true); t.start();
        }

        byte[] gen(String name){
            return switch(name){
                case "boost"  -> synth(0.22, f->(sq(f,440+f*300)+sq(f,660+f*200)*.4)*adsr(f,.01,.05,.6,.1)*.4);
                case "oil"    -> synth(0.28, f->noise()*adsr(f,.005,.03,.5,.1)*.5);
                case "lap"    -> synth(0.45, f->arpUp(f)*adsr(f,.01,.1,.6,.1)*.5);
                case "finish" -> synth(0.80, f->(sine(f,523)+sine(f,659)+sine(f,784))*.28*adsr(f,.01,.1,.65,.1));
                case "start"  -> synth(0.35, f->(sine(f,330+f*120)+sine(f,550))*.3*adsr(f,.01,.05,.55,.1));
                default       -> null;
            };
        }

        static double sine(double t,double f){return Math.sin(2*Math.PI*f*t);}
        static double sq(double t,double f){return Math.signum(Math.sin(2*Math.PI*f*t));}
        static final Random NR=new Random();
        static double noise(){return NR.nextDouble()*2-1;}
        static double adsr(double t,double a,double d,double s,double r){
            if(t<a)return t/a; if(t<a+d)return 1-(1-.7)*((t-a)/d);
            if(t<1-r)return .7; return .7*(1-(t-(1-r))/r);
        }
        static double arpUp(double t){
            double[] fr={392,494,587,784}; int i=Math.min((int)(t*fr.length),fr.length-1);
            return sine(t,fr[i])+sine(t,fr[i]*2)*.25;
        }
        byte[] synth(double dur,java.util.function.DoubleUnaryOperator fn){
            int n=(int)(SR*dur); byte[] b=new byte[n*2];
            for(int i=0;i<n;i++){
                double t=(double)i/n;
                int s=Math.max(-32768,Math.min(32767,(int)(fn.applyAsDouble(t)*28000)));
                b[2*i]=(byte)(s&0xFF); b[2*i+1]=(byte)((s>>8)&0xFF);
            }
            return b;
        }
    }

    // ── Main ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame f=new JFrame("Neon Racer: Circuit Breaker");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            NeonRacerGame game=new NeonRacerGame();
            f.add(game); f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}