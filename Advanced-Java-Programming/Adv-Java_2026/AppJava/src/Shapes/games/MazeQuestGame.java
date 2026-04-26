package Shapes.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sound.sampled.*;

/**
 * MazeQuest: Dungeon of Echoes
 * ─────────────────────────────────────────────────────────────────────────────
 * A top-down tile-based maze puzzle adventure with 4 levels.
 *
 * HOW TO RUN:
 *   javac MazeQuestGame.java
 *   java Shapes.games.MazeQuestGame
 *
 * CONTROLS:
 *   Arrow Keys / WASD  - Move player (one tile per press)
 *   E                  - Pick up / interact
 *   ESC                - Pause
 *   R                  - Restart level
 *   ENTER              - Start / Resume
 *   Q                  - Quit to Menu
 *   M                  - Mute / Unmute
 *
 * GOAL:
 *   Collect ALL keys on the floor, then reach the glowing EXIT door.
 *   Avoid or time-dodge moving Guardian enemies.
 *   Torches light up the fog-of-war around you.
 */
public class MazeQuestGame extends JPanel implements ActionListener, KeyListener {

    // ── Constants ────────────────────────────────────────────────────────
    static final int TILE       = 36;   // px per tile
    static final int COLS       = 23;
    static final int ROWS       = 17;
    static final int HUD_H      = 60;
    static final int SCREEN_W   = COLS * TILE;           // 828
    static final int SCREEN_H   = ROWS * TILE + HUD_H;  // 672
    static final int FPS        = 60;
    static final int FOG_RADIUS = 4;    // tiles visible around player

    // Tile types
    static final int T_WALL   = 0;
    static final int T_FLOOR  = 1;
    static final int T_EXIT   = 2;
    static final int T_TORCH  = 3;  // floor + torch decoration (lights area)
    static final int T_WATER  = 4;  // slows movement
    static final int T_SPIKE  = 5;  // instant kill if stepped on while active
    static final int T_CHEST  = 6;  // unopened chest (open with E nearby)

    // ── Colours ───────────────────────────────────────────────────────────
    static final Color C_WALL       = new Color(30,  25,  55);
    static final Color C_WALL_LITE  = new Color(55,  48,  90);
    static final Color C_FLOOR      = new Color(55,  50,  72);
    static final Color C_FLOOR_LITE = new Color(75,  68,  96);
    static final Color C_EXIT_OFF   = new Color(80,  40,  10);
    static final Color C_EXIT_ON    = new Color(255, 200, 50);
    static final Color C_WATER      = new Color(20,  80,  160);
    static final Color C_SPIKE_OFF  = new Color(60,  55,  65);
    static final Color C_SPIKE_ON   = new Color(220, 50,  50);
    static final Color C_FOG        = new Color(0,   0,   0,  220);
    static final Color C_FOG_EDGE   = new Color(0,   0,   0,  130);
    static final Color C_HUD_BG     = new Color(8,   5,   20);
    static final Color C_GOLD       = new Color(255, 210, 50);
    static final Color C_BLUE_GLOW  = new Color(80,  160, 255);
    static final Color C_GREEN      = new Color(50,  210, 100);
    static final Color C_RED        = new Color(220, 50,  50);
    static final Color C_WHITE      = Color.WHITE;

    // ── Level definitions (hand-crafted maps) ────────────────────────────
    //  0=wall  1=floor  2=exit  3=torch  4=water  5=spike  6=chest
    //  K = key (floor + key item)
    static final String[][] MAPS = {
        // Level 1 – "The Stone Crypt"  (23×17)
        {
        "00000000000000000000000",
        "01111110001110001100000",
        "01000110001110011100000",
        "01011110011110011110000",
        "01011100011110011010000",
        "01011100011110011010000",
        "01011100011110011010000",
        "01K111000111111111110P0",
        "01011100011110011010000",
        "01011100011110011010000",
        "01011111011110011010000",
        "01011111011110011010000",
        "01011111011110011010000",
        "01011111011110011010000",
        "01111111011110011E1000",
        "000000000000000000000000",
        "000000000000000000000000"
        },
        // Level 2 – "The Flooded Cavern"  (23×17)
        {
        "00000000000000000000000",
        "0P1111111111111111111110",
        "01000100040004000001000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011K00044004001101K00",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "01011100044004001101000",
        "010111000440040011E1000",
        "000000000000000000000000",
        "000000000000000000000000"
        },
        // Level 3 – "The Spike Gauntlet"  (23×17)
        {
        "00000000000000000000000",
        "0P1111111111111111111110",
        "01000100050005000001000",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "0101K100055005001101K00",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "01011100055005001101000",
        "010111000550050011E1000",
        "000000000000000000000000",
        "000000000000000000000000"
        },
        // Level 4 – "The Sanctum" (all hazards)  (23×17)
        {
        "00000000000000000000000",
        "0P111111111111111111110",
        "01000100040054000001000",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "0101K100044554001101K00",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "01011100044554001101000",
        "010111000445540011E1000",
        "000000000000000000000000",
        "000000000000000000000000"
        }
    };

    // ── Procedurally generated fallback maps for each level ──────────────
    // (used if hand-crafted map has issues; also used for actual map gen)
    int[][]  tileMap;        // current tile grid
    boolean[][]  fogMap;    // fog-of-war: true = revealed
    boolean[][]  litMap;    // lit by torch this frame

    // Keys placed on floor (positions where 'K'/'P' appear)
    List<int[]>  keyPositions   = new ArrayList<>(); // [col, row]
    Set<String>  collectedKeys  = new HashSet<>();
    int          totalKeys      = 0;

    int[]        playerPos      = new int[2]; // [col, row]
    int[]        exitPos        = new int[2];

    // Guardians
    List<Guardian>   guardians  = new CopyOnWriteArrayList<>();
    List<Sparkle>    sparkles   = new CopyOnWriteArrayList<>();

    // Spike timing
    int  spikeTick    = 0;
    boolean spikesUp  = false;

    // Water slow
    int  moveDelay    = 0;

    // Player state
    int  playerHp     = 3;
    int  invincibleTicks = 0;
    boolean playerDead   = false;

    // Level & score
    int  level        = 1;
    int  lives        = 3;
    int  steps        = 0;

    // Input
    boolean moveQueued = false;
    int     queueDx = 0, queueDy = 0;
    int     moveTimer = 0;
    static final int MOVE_DELAY = 8; // ticks between moves

    // Screens
    enum Screen { MENU, PLAYING, PAUSED, LEVEL_COMPLETE, GAME_OVER, WIN }
    Screen screen = Screen.MENU;

    // Animation tick
    long tick = 0;

    // Sound
    SoundBox sound;

    Random rng = new Random();
    javax.swing.Timer gameTimer;

    // ── Constructor ───────────────────────────────────────────────────────
    public MazeQuestGame() {
        setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        sound     = new SoundBox();
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    // ── Map loading ───────────────────────────────────────────────────────
    void loadLevel() {
        tileMap  = new int[ROWS][COLS];
        fogMap   = new boolean[ROWS][COLS];
        litMap   = new boolean[ROWS][COLS];
        keyPositions.clear();
        collectedKeys.clear();
        guardians.clear();
        sparkles.clear();
        playerDead   = false;
        invincibleTicks = 0;
        moveTimer    = 0;
        spikeTick    = 0;
        spikesUp     = false;
        steps        = 0;
        playerHp     = 3;

        // Build procedural map for this level
        buildProceduralMap(level);

        // Illuminate starting area
        updateFog();
    }

    void buildProceduralMap(int lvl) {
        // Generate a random-ish maze using recursive-backtracker then add special tiles
        // Start with all walls
        for (int r = 0; r < ROWS; r++)
            Arrays.fill(tileMap[r], T_WALL);

        // Carve passages (simple grid maze: step=2)
        boolean[][] visited = new boolean[ROWS][COLS];
        int startR = 1, startC = 1;
        playerPos[0] = startC; playerPos[1] = startR;
        carve(startR, startC, visited);

        // Ensure border walls
        for (int c = 0; c < COLS; c++) { tileMap[0][c] = T_WALL; tileMap[ROWS-1][c] = T_WALL; }
        for (int r = 0; r < ROWS; r++) { tileMap[r][0] = T_WALL; tileMap[r][COLS-1] = T_WALL; }

        // Place exit far from start
        int[] farPos = farthestFloor(startR, startC);
        exitPos[0] = farPos[1]; exitPos[1] = farPos[0];
        tileMap[exitPos[1]][exitPos[0]] = T_EXIT;

        // Place keys
        int numKeys = lvl + 1; // 2, 3, 4, 5
        totalKeys = numKeys;
        List<int[]> floors = collectFloorCells();
        Collections.shuffle(floors, rng);
        int placed = 0;
        for (int[] fc : floors) {
            if (placed >= numKeys) break;
            int r = fc[0], c = fc[1];
            if (r == startR && c == startC) continue;
            if (r == exitPos[1] && c == exitPos[0]) continue;
            keyPositions.add(new int[]{c, r});
            placed++;
        }

        // Place torches
        int numTorches = 4 + lvl;
        int ti = 0;
        for (int[] fc : floors) {
            if (ti >= numTorches) break;
            if (tileMap[fc[0]][fc[1]] == T_FLOOR) {
                boolean near = false;
                for (int[] kp : keyPositions)
                    if (kp[0]==fc[1] && kp[1]==fc[0]) { near=true; break; }
                if (!near && !(fc[0]==startR && fc[1]==startC)) {
                    tileMap[fc[0]][fc[1]] = T_TORCH;
                    ti++;
                }
            }
        }

        // Place water (lvl 2+)
        if (lvl >= 2) placeHazardPatches(T_WATER, 8 + lvl * 2, floors);

        // Place spikes (lvl 3+)
        if (lvl >= 3) placeHazardPatches(T_SPIKE, 6 + lvl * 2, floors);

        // Place guardians
        int numGuardians = lvl;
        int gi = 0;
        for (int[] fc : floors) {
            if (gi >= numGuardians) break;
            int gr = fc[0], gc = fc[1];
            if (Math.abs(gr-startR)+Math.abs(gc-startC) < 5) continue;
            if (gr==exitPos[1] && gc==exitPos[0]) continue;
            guardians.add(new Guardian(gc, gr, lvl >= 4));
            tileMap[gr][gc] = T_FLOOR; // ensure floor
            gi++;
        }
    }

    int[] dirs = {-2,0, 2,0, 0,-2, 0,2};
    void carve(int r, int c, boolean[][] visited) {
        visited[r][c] = true;
        tileMap[r][c] = T_FLOOR;
        Integer[] order = {0,1,2,3};
        List<Integer> idxs = Arrays.asList(order);
        Collections.shuffle(idxs, rng);
        for (int i : idxs) {
            int dr = dirs[i*2], dc = dirs[i*2+1];
            int nr = r+dr, nc = c+dc;
            if (nr>0 && nr<ROWS-1 && nc>0 && nc<COLS-1 && !visited[nr][nc]) {
                tileMap[r+dr/2][c+dc/2] = T_FLOOR;
                carve(nr, nc, visited);
            }
        }
    }

    List<int[]> collectFloorCells() {
        List<int[]> list = new ArrayList<>();
        for (int r=1;r<ROWS-1;r++)
            for (int c=1;c<COLS-1;c++)
                if (tileMap[r][c]==T_FLOOR||tileMap[r][c]==T_TORCH)
                    list.add(new int[]{r,c});
        Collections.shuffle(list, rng);
        return list;
    }

    int[] farthestFloor(int sr, int sc) {
        int[][] dist = new int[ROWS][COLS];
        for (int[] d : dist) Arrays.fill(d, -1);
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{sr, sc, 0});
        dist[sr][sc] = 0;
        int[] best = {sr, sc};
        int bestD = 0;
        int[][] nd = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int cr=cur[0], cc=cur[1], d=cur[2];
            for (int[] dd : nd) {
                int nr=cr+dd[0], nc=cc+dd[1];
                if (nr>=0&&nr<ROWS&&nc>=0&&nc<COLS&&dist[nr][nc]<0&&tileMap[nr][nc]!=T_WALL) {
                    dist[nr][nc]=d+1;
                    if (d+1>bestD) { bestD=d+1; best=new int[]{nr,nc}; }
                    q.add(new int[]{nr,nc,d+1});
                }
            }
        }
        return best;
    }

    void placeHazardPatches(int hazard, int count, List<int[]> floors) {
        int placed = 0;
        for (int[] fc : floors) {
            if (placed >= count) break;
            int r=fc[0], c=fc[1];
            if (tileMap[r][c]==T_FLOOR) {
                boolean badSpot = false;
                for (int[] kp : keyPositions)
                    if (kp[0]==c && kp[1]==r) { badSpot=true; break; }
                if (!badSpot && !(r==playerPos[1]&&c==playerPos[0]) && !(r==exitPos[1]&&c==exitPos[0])) {
                    tileMap[r][c] = hazard;
                    placed++;
                }
            }
        }
    }

    // ── Fog of war ────────────────────────────────────────────────────────
    void updateFog() {
        for (boolean[] row : litMap) Arrays.fill(row, false);
        int pr = playerPos[1], pc = playerPos[0];
        int radius = FOG_RADIUS;
        // Torches extend vision
        for (int r=Math.max(0,pr-radius); r<=Math.min(ROWS-1,pr+radius); r++)
            for (int c=Math.max(0,pc-radius); c<=Math.min(COLS-1,pc+radius); c++) {
                double dist = Math.sqrt((r-pr)*(r-pr)+(c-pc)*(c-pc));
                if (dist <= radius) { fogMap[r][c]=true; litMap[r][c]=true; }
            }
        // Torch tiles light nearby area
        for (int r=0;r<ROWS;r++)
            for (int c=0;c<COLS;c++)
                if (tileMap[r][c]==T_TORCH)
                    for (int dr=-2;dr<=2;dr++)
                        for (int dc=-2;dc<=2;dc++) {
                            int nr=r+dr, nc=c+dc;
                            if (nr>=0&&nr<ROWS&&nc>=0&&nc<COLS) { fogMap[nr][nc]=true; litMap[nr][nc]=true; }
                        }
    }

    // ── Game loop ─────────────────────────────────────────────────────────
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        if (screen == Screen.PLAYING) update();
        repaint();
    }

    void update() {
        // Spike toggle (every 90 ticks)
        spikeTick++;
        if (spikeTick >= 90) { spikeTick=0; spikesUp=!spikesUp; }

        if (invincibleTicks > 0) invincibleTicks--;

        // Movement
        if (moveTimer > 0) { moveTimer--; return; }
        if (moveQueued) {
            moveQueued = false;
            int nc = playerPos[0] + queueDx;
            int nr = playerPos[1] + queueDy;
            if (nc>=0 && nc<COLS && nr>=0 && nr<ROWS) {
                int tile = tileMap[nr][nc];
                if (tile != T_WALL) {
                    playerPos[0]=nc; playerPos[1]=nr;
                    steps++;
                    // Tile effects
                    if (tile==T_WATER) { moveTimer=MOVE_DELAY*2; } else { moveTimer=MOVE_DELAY; }
                    if (tile==T_SPIKE && spikesUp && invincibleTicks==0) { hurtPlayer(); }
                    // Pick up key?
                    String key = nc+","+nr;
                    Iterator<int[]> it = keyPositions.iterator();
                    while (it.hasNext()) {
                        int[] kp = it.next();
                        if (kp[0]==nc && kp[1]==nr && !collectedKeys.contains(key)) {
                            collectedKeys.add(key);
                            sound.play("key");
                            spawnSparkles(nc,nr,C_GOLD,12);
                        }
                    }
                    // Exit?
                    if (tile==T_EXIT && collectedKeys.size()>=totalKeys) {
                        sound.play("levelup");
                        spawnSparkles(nc,nr,C_EXIT_ON,20);
                        if (level>=4) { screen=Screen.WIN; }
                        else { screen=Screen.LEVEL_COMPLETE; }
                        return;
                    }
                    updateFog();
                }
            }
        }

        // Guardian movement
        for (Guardian g : guardians) {
            g.tickMove();
            if (g.col==playerPos[0] && g.row==playerPos[1] && invincibleTicks==0) {
                hurtPlayer();
            }
        }

        // Sparkles
        sparkles.removeIf(s -> { s.update(); return s.dead; });
    }

    void hurtPlayer() {
        playerHp--;
        invincibleTicks = 90;
        sound.play("hurt");
        spawnSparkles(playerPos[0], playerPos[1], C_RED, 10);
        if (playerHp <= 0) {
            lives--;
            sound.play("die");
            if (lives <= 0) { screen=Screen.GAME_OVER; }
            else { loadLevel(); }
        }
    }

    void spawnSparkles(int col, int row, Color c, int n) {
        int px = col*TILE+TILE/2, py = row*TILE+TILE/2+HUD_H;
        for (int i=0;i<n;i++) {
            float vx=(float)(rng.nextGaussian()*2.5), vy=(float)(rng.nextGaussian()*2.5);
            sparkles.add(new Sparkle(px,py,c,vx,vy,20+rng.nextInt(15)));
        }
    }

    // ── Paint ─────────────────────────────────────────────────────────────
    @Override protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (screen) {
            case MENU           -> drawMenu(g);
            case PLAYING        -> drawGame(g);
            case PAUSED         -> { drawGame(g); drawOverlay(g,"PAUSED",C_GOLD,
                                      new String[]{"ENTER – Resume","R – Restart","Q – Menu"}); }
            case LEVEL_COMPLETE -> drawLevelComplete(g);
            case GAME_OVER      -> drawEndScreen(g,"GAME OVER",C_RED,
                                      new String[]{"R – Try Again","Q – Menu"});
            case WIN            -> drawEndScreen(g,"YOU ESCAPED!",C_GOLD,
                                      new String[]{"You conquered all dungeons!","Q – Menu"});
        }
    }

    void drawMenu(Graphics2D g) {
        // Dark bg with grid
        g.setColor(new Color(8,5,20));
        g.fillRect(0,0,SCREEN_W,SCREEN_H);
        drawMenuGrid(g);

        // Title
        drawGlow(g, new Font("Courier New",Font.BOLD,40), "MAZE QUEST", C_GOLD,
                 new Color(120,80,0), SCREEN_W/2, SCREEN_H/2-160);
        drawGlow(g, new Font("Courier New",Font.BOLD,18), "DUNGEON OF ECHOES", C_BLUE_GLOW,
                 new Color(10,40,100), SCREEN_W/2, SCREEN_H/2-112);

        String[] info = {
            "ENTER  ─ Start Game",
            "ARROWS / WASD  ─ Move",
            "E  ─ Interact (chests, torches)",
            "ESC    ─ Pause",
            "R      ─ Restart Level",
            "M      ─ Mute/Unmute",
            "Q      ─ Quit"
        };
        g.setFont(new Font("Courier New",Font.PLAIN,16));
        for (int i=0;i<info.length;i++)
            drawCentered(g, info[i], new Color(190,185,220), SCREEN_H/2-42+i*30);

        g.setFont(new Font("Courier New",Font.BOLD,14));
        drawCentered(g, "GOAL: Collect all keys  ►  Reach the EXIT", new Color(255,210,50), SCREEN_H-65);
        drawCentered(g, "Avoid Guardians · Dodge Spikes · Cross the Water", new Color(160,160,200), SCREEN_H-40);
    }

    void drawMenuGrid(Graphics2D g) {
        g.setColor(new Color(255,255,255,10));
        g.setStroke(new BasicStroke(0.5f));
        for (int x=0;x<SCREEN_W;x+=TILE) g.drawLine(x,0,x,SCREEN_H);
        for (int y=0;y<SCREEN_H;y+=TILE) g.drawLine(0,y,SCREEN_W,y);
        g.setStroke(new BasicStroke(1));
    }

    void drawGame(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,SCREEN_W,SCREEN_H);

        drawHud(g);

        // Tiles
        for (int r=0;r<ROWS;r++) {
            for (int c=0;c<COLS;c++) {
                int px=c*TILE, py=r*TILE+HUD_H;
                boolean lit   = litMap[r][c];
                boolean seen  = fogMap[r][c];
                int tile = tileMap[r][c];

                if (!seen) continue; // full fog

                drawTile(g, tile, px, py, lit, r, c);
            }
        }

        // Keys on floor
        for (int[] kp : keyPositions) {
            String ks = kp[0]+","+kp[1];
            if (!collectedKeys.contains(ks) && litMap[kp[1]][kp[0]]) {
                drawKey(g, kp[0]*TILE, kp[1]*TILE+HUD_H);
            }
        }

        // Guardians
        for (Guardian gu : guardians)
            if (litMap[gu.row][gu.col]) drawGuardian(g, gu);

        // Player
        drawPlayer(g);

        // Sparkles
        for (Sparkle sp : sparkles) drawSparkle(g, sp);

        // Fog overlay
        drawFogOverlay(g);
    }

    void drawTile(Graphics2D g, int tile, int px, int py, boolean lit, int r, int c) {
        float brightness = lit ? 1.0f : 0.35f;
        switch (tile) {
            case T_WALL -> {
                Color wc = blend(C_WALL, C_WALL_LITE, brightness);
                g.setColor(wc);
                g.fillRect(px,py,TILE,TILE);
                // Brick texture
                g.setColor(new Color(0,0,0,60));
                int bh=TILE/3;
                int off = (r%2==0)?0:TILE/2;
                for (int row=0;row<3;row++) {
                    g.drawLine(px, py+row*bh, px+TILE, py+row*bh);
                    if (row<2) g.drawLine(px+((off+(c*TILE/2))%TILE), py+row*bh, px+((off+(c*TILE/2))%TILE), py+(row+1)*bh);
                }
            }
            case T_FLOOR, T_CHEST -> {
                Color fc = blend(C_FLOOR, C_FLOOR_LITE, brightness);
                g.setColor(fc);
                g.fillRect(px,py,TILE,TILE);
                // Subtle checker
                if ((r+c)%2==0) {
                    g.setColor(new Color(255,255,255, lit?18:6));
                    g.fillRect(px,py,TILE,TILE);
                }
            }
            case T_EXIT -> {
                boolean ready = collectedKeys.size()>=totalKeys;
                Color ec = ready ? C_EXIT_ON : C_EXIT_OFF;
                g.setColor(blend(C_FLOOR,C_FLOOR_LITE,brightness));
                g.fillRect(px,py,TILE,TILE);
                if (lit) {
                    int pulse = ready ? (int)(Math.abs(Math.sin(tick*0.07))*30)+20 : 0;
                    g.setColor(new Color(ec.getRed(), ec.getGreen(), ec.getBlue(), 150+pulse));
                    g.fillOval(px+4,py+4,TILE-8,TILE-8);
                    g.setColor(ec);
                    g.setStroke(new BasicStroke(2));
                    g.drawOval(px+4,py+4,TILE-8,TILE-8);
                    g.setStroke(new BasicStroke(1));
                    // Portal symbol
                    g.setFont(new Font("Serif",Font.BOLD,18));
                    FontMetrics fm=g.getFontMetrics();
                    String sym = ready ? "▲" : "▲";
                    g.setColor(ready ? Color.WHITE : new Color(200,160,80));
                    g.drawString(sym, px+(TILE-fm.stringWidth(sym))/2, py+TILE/2+6);
                }
            }
            case T_TORCH -> {
                g.setColor(blend(C_FLOOR,C_FLOOR_LITE,brightness));
                g.fillRect(px,py,TILE,TILE);
                if (lit) {
                    // Torch glow
                    int glow = (int)(Math.abs(Math.sin(tick*0.10))*20)+30;
                    Color gc2 = new Color(255,160,20,glow);
                    g.setColor(gc2);
                    g.fillOval(px-4,py-4,TILE+8,TILE+8);
                    // Torch icon
                    g.setColor(new Color(80,50,20));
                    g.fillRect(px+TILE/2-2,py+TILE/2,4,12);
                    g.setColor(new Color(255,150,30));
                    g.fillOval(px+TILE/2-4,py+TILE/2-8,8,10);
                    g.setColor(new Color(255,220,80));
                    g.fillOval(px+TILE/2-2,py+TILE/2-10,4,8);
                }
            }
            case T_WATER -> {
                g.setColor(blend(C_WATER, new Color(50,120,220), brightness));
                g.fillRect(px,py,TILE,TILE);
                if (lit) {
                    // Wave ripple
                    double woff = Math.sin((tick*0.05)+c*0.7+r*0.4)*3;
                    g.setColor(new Color(255,255,255,50));
                    g.setStroke(new BasicStroke(1));
                    for (int wr=0;wr<3;wr++) {
                        int wy = py+6+wr*8+(int)woff;
                        g.drawArc(px+2,wy,10,6,0,180);
                        g.drawArc(px+14,wy,10,6,0,180);
                    }
                    g.setStroke(new BasicStroke(1));
                }
            }
            case T_SPIKE -> {
                g.setColor(blend(C_SPIKE_OFF, C_FLOOR_LITE, brightness));
                g.fillRect(px,py,TILE,TILE);
                if (lit) {
                    Color sc = spikesUp ? C_SPIKE_ON : new Color(80,75,85);
                    g.setColor(sc);
                    for (int si=0;si<4;si++) {
                        int sx2 = px+4+si*8;
                        int sy2 = spikesUp ? py+4 : py+TILE-6;
                        int sh  = spikesUp ? TILE-8 : 5;
                        g.fillPolygon(new int[]{sx2,sx2+4,sx2+2}, new int[]{py+TILE-4,py+TILE-4,sy2}, 3);
                    }
                }
            }
        }
        // Grid border
        g.setColor(new Color(0,0,0,60));
        g.drawRect(px,py,TILE-1,TILE-1);
    }

    void drawKey(Graphics2D g, int px, int py) {
        int kx=px+TILE/2, ky=py+TILE/2;
        // Glow
        int glow=(int)(Math.abs(Math.sin(tick*0.08))*25)+20;
        g.setColor(new Color(255,200,30,glow));
        g.fillOval(kx-10,ky-10,20,20);
        // Key body
        g.setColor(C_GOLD);
        g.fillOval(kx-6,ky-6,12,12);
        g.setColor(new Color(180,140,20));
        g.fillOval(kx-3,ky-3,6,6);
        g.setColor(C_GOLD);
        g.setStroke(new BasicStroke(2.5f));
        g.drawLine(kx+5,ky,kx+12,ky);
        g.drawLine(kx+9,ky,kx+9,ky-3);
        g.drawLine(kx+12,ky,kx+12,ky-3);
        g.setStroke(new BasicStroke(1));
    }

    void drawPlayer(Graphics2D g) {
        int pc = playerPos[0], pr = playerPos[1];
        int px = pc*TILE, py = pr*TILE+HUD_H;
        int cx = px+TILE/2, cy = py+TILE/2;

        boolean blink = (invincibleTicks>0) && ((tick/5)%2==0);
        if (blink) return;

        // Aura
        g.setColor(new Color(80,160,255,40));
        g.fillOval(cx-14,cy-14,28,28);
        // Body
        g.setColor(new Color(50,120,220));
        g.fillOval(cx-10,cy-8,20,18);
        // Head
        g.setColor(new Color(230,185,135));
        g.fillOval(cx-7,cy-20,14,14);
        // Eyes
        g.setColor(Color.BLACK);
        g.fillOval(cx-4,cy-16,3,3);
        g.fillOval(cx+1,cy-16,3,3);
        // Hat
        g.setColor(new Color(40,90,180));
        g.fillRoundRect(cx-9,cy-25,18,6,3,3);
        g.fillRoundRect(cx-6,cy-33,12,10,3,3);
        // Legs
        g.setColor(new Color(30,70,160));
        g.fillRect(cx-8,cy+9,6,9);
        g.fillRect(cx+2,cy+9,6,9);
        // Boots
        g.setColor(new Color(30,20,10));
        g.fillRoundRect(cx-9,cy+16,8,5,2,2);
        g.fillRoundRect(cx+1,cy+16,8,5,2,2);
        // Staff/wand
        g.setColor(new Color(100,65,25));
        g.setStroke(new BasicStroke(2));
        g.drawLine(cx+10,cy-18,cx+18,cy+8);
        g.setStroke(new BasicStroke(1));
        int starGlow=(int)(Math.abs(Math.sin(tick*0.12))*20)+30;
        g.setColor(new Color(100,200,255,starGlow));
        g.fillOval(cx+14,cy-22,8,8);
        g.setColor(new Color(200,240,255));
        g.fillOval(cx+16,cy-20,4,4);

        // HP hearts below player
        for (int h=0;h<playerHp;h++) {
            int hx=cx-playerHp*8+h*16;
            int hy=py+TILE+3;
            g.setColor(C_RED);
            drawHeart(g, hx, hy, 7);
        }
    }

    void drawHeart(Graphics2D g, int cx, int cy, int size) {
        GeneralPath p = new GeneralPath();
        p.moveTo(cx, cy+size*0.25);
        p.curveTo(cx, cy-size*0.5, cx-size, cy-size*0.5, cx-size, cy);
        p.curveTo(cx-size, cy+size*0.6, cx, cy+size, cx, cy+size);
        p.curveTo(cx, cy+size, cx+size, cy+size*0.6, cx+size, cy);
        p.curveTo(cx+size, cy-size*0.5, cx, cy-size*0.5, cx, cy+size*0.25);
        g.fill(p);
    }

    void drawGuardian(Graphics2D g, Guardian gu) {
        int px=gu.col*TILE, py=gu.row*TILE+HUD_H;
        int cx=px+TILE/2, cy=py+TILE/2;
        // Shadow
        g.setColor(new Color(0,0,0,60));
        g.fillOval(cx-11,cy+12,22,8);
        // Body (dark wraith)
        Color bodyC = gu.isElite ? new Color(140,30,200) : new Color(50,10,80);
        g.setColor(bodyC);
        g.fillOval(cx-10,cy-8,20,20);
        // Cloak
        g.setColor(gu.isElite ? new Color(120,20,170) : new Color(30,5,55));
        g.fillArc(cx-12,cy-4,24,24,0,180);
        // Head
        g.setColor(new Color(15,10,30));
        g.fillOval(cx-8,cy-22,16,16);
        // Eyes glow
        int eyeGlow=(int)(Math.abs(Math.sin(tick*0.11))*30)+30;
        Color eyeC = gu.isElite ? new Color(200,80,255,180+eyeGlow) : new Color(200,30,30,180+eyeGlow);
        g.setColor(eyeC);
        g.fillOval(cx-5,cy-18,5,5);
        g.fillOval(cx+1,cy-18,5,5);
        // Elite crown
        if (gu.isElite) {
            g.setColor(C_GOLD);
            g.setStroke(new BasicStroke(2));
            g.drawLine(cx-6,cy-26,cx-6,cy-32);
            g.drawLine(cx,cy-26,cx,cy-34);
            g.drawLine(cx+6,cy-26,cx+6,cy-32);
            g.drawLine(cx-6,cy-26,cx+6,cy-26);
            g.setStroke(new BasicStroke(1));
        }
    }

    void drawSparkle(Graphics2D g, Sparkle sp) {
        int a = (int)(255f*sp.life/sp.maxLife);
        g.setColor(new Color(sp.c.getRed(),sp.c.getGreen(),sp.c.getBlue(),Math.max(0,Math.min(255,a))));
        int sz=2+(int)(4f*sp.life/sp.maxLife);
        g.fillOval((int)sp.x-sz/2,(int)sp.y-sz/2,sz,sz);
    }

    void drawFogOverlay(Graphics2D g) {
        for (int r=0;r<ROWS;r++) {
            for (int c=0;c<COLS;c++) {
                int px=c*TILE, py=r*TILE+HUD_H;
                if (!fogMap[r][c]) {
                    g.setColor(C_FOG);
                    g.fillRect(px,py,TILE,TILE);
                } else if (!litMap[r][c]) {
                    g.setColor(new Color(0,0,0,160));
                    g.fillRect(px,py,TILE,TILE);
                }
            }
        }
    }

    void drawHud(Graphics2D g) {
        g.setColor(C_HUD_BG);
        g.fillRect(0,0,SCREEN_W,HUD_H);
        g.setColor(new Color(80,60,120));
        g.setStroke(new BasicStroke(2));
        g.drawLine(0,HUD_H,SCREEN_W,HUD_H);
        g.setStroke(new BasicStroke(1));

        // Title
        g.setFont(new Font("Courier New",Font.BOLD,16));
        g.setColor(C_GOLD);
        String[] levelNames = {"The Stone Crypt","The Flooded Cavern","The Spike Gauntlet","The Sanctum"};
        g.drawString("LVL "+level+": "+levelNames[Math.min(level-1,3)], 14, 24);

        // Keys collected
        int collected = collectedKeys.size();
        g.setFont(new Font("Courier New",Font.BOLD,14));
        g.setColor(collected>=totalKeys ? C_GREEN : new Color(220,180,50));
        g.drawString("KEYS: "+collected+"/"+totalKeys, 14, 46);

        // Key icons
        for (int k=0;k<totalKeys;k++) {
            int kx=120+k*18, ky=36;
            if (k<collected) {
                g.setColor(C_GOLD);
                g.fillOval(kx,ky-7,10,10);
                g.setColor(new Color(180,140,20));
                g.fillOval(kx+2,ky-5,6,6);
            } else {
                g.setColor(new Color(80,70,50));
                g.fillOval(kx,ky-7,10,10);
            }
        }

        // Lives (hearts)
        g.setFont(new Font("Courier New",Font.BOLD,14));
        g.setColor(new Color(255,100,100));
        g.drawString("LIVES:", SCREEN_W-200, 24);
        for (int l=0;l<lives;l++) {
            g.setColor(l<lives ? C_RED : new Color(80,30,30));
            drawHeart(g, SCREEN_W-120+l*22, 18, 8);
        }

        // Steps counter
        g.setFont(new Font("Courier New",Font.PLAIN,12));
        g.setColor(new Color(140,130,170));
        g.drawString("STEPS: "+steps, SCREEN_W-200, 48);

        // Spike warning
        if (level>=3) {
            int warn = 90 - spikeTick;
            boolean nearToggle = warn < 20;
            g.setFont(new Font("Courier New",Font.BOLD,12));
            g.setColor(spikesUp ? C_RED : (nearToggle ? new Color(255,140,30) : new Color(120,115,130)));
            g.drawString(spikesUp ? "⚠ SPIKES UP!" : (nearToggle ? "SPIKES RISING!" : "spikes down"),
                         SCREEN_W/2-55, 30);
            g.setColor(new Color(60,55,70));
            g.fillRect(SCREEN_W/2-55, 36, 110, 7);
            g.setColor(spikesUp ? C_RED : C_GREEN);
            g.fillRect(SCREEN_W/2-55, 36, (int)(110.0*spikeTick/90), 7);
        }
    }

    void drawLevelComplete(Graphics2D g) {
        drawStarBg(g, new Color(5,20,8), new Color(15,50,20));
        drawGlow(g, new Font("Courier New",Font.BOLD,48),
                "LEVEL "+level+" CLEAR!", C_GREEN, new Color(10,80,10),
                SCREEN_W/2, SCREEN_H/2-70);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        drawCentered(g,"Steps taken: "+steps, new Color(200,200,230), SCREEN_H/2);
        drawCentered(g,"P – Next Level", C_WHITE, SCREEN_H/2+46);
        drawCentered(g,"Q – Main Menu",  C_WHITE, SCREEN_H/2+80);
    }

    void drawOverlay(Graphics2D g, String title, Color tc, String[] opts) {
        g.setColor(new Color(0,0,0,190));
        g.fillRect(0,0,SCREEN_W,SCREEN_H);
        drawGlow(g, new Font("Courier New",Font.BOLD,56), title, tc,
                 new Color(80,60,0), SCREEN_W/2, SCREEN_H/2-70);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        for (int i=0;i<opts.length;i++)
            drawCentered(g, opts[i], C_WHITE, SCREEN_H/2+10+i*40);
    }

    void drawEndScreen(Graphics2D g, String title, Color tc, String[] opts) {
        drawStarBg(g, new Color(8,5,20), new Color(25,15,50));
        drawGlow(g, new Font("Courier New",Font.BOLD,54), title, tc,
                 new Color(80,50,0), SCREEN_W/2, SCREEN_H/2-80);
        g.setFont(new Font("Courier New",Font.PLAIN,22));
        for (int i=0;i<opts.length;i++)
            drawCentered(g, opts[i], C_WHITE, SCREEN_H/2+10+i*42);
    }

    void drawStarBg(Graphics2D g, Color top, Color bottom) {
        GradientPaint gp = new GradientPaint(0,0,top,0,SCREEN_H,bottom);
        g.setPaint(gp);
        g.fillRect(0,0,SCREEN_W,SCREEN_H);
        Random sr = new Random(99);
        for (int i=0;i<100;i++) {
            int sx=sr.nextInt(SCREEN_W), sy=sr.nextInt(SCREEN_H);
            int bri=80+sr.nextInt(175);
            int tw=(int)(Math.abs(Math.sin(tick*0.04+i))*50);
            g.setColor(new Color(bri,bri,Math.min(255,bri+50),100+tw));
            g.fillOval(sx,sy,sr.nextInt(2)+1,sr.nextInt(2)+1);
        }
    }

    void drawGlow(Graphics2D g, Font f, String text, Color bright, Color dark, int cx, int y) {
        g.setFont(f);
        FontMetrics fm=g.getFontMetrics(f);
        int tx=cx-fm.stringWidth(text)/2;
        for (int r=6;r>=1;r--) {
            g.setColor(new Color(bright.getRed(),bright.getGreen(),bright.getBlue(),Math.min(255,10+r*18)));
            g.drawString(text,tx+r,y+r); g.drawString(text,tx-r,y-r);
            g.drawString(text,tx+r,y-r); g.drawString(text,tx-r,y+r);
        }
        g.setColor(dark);  g.drawString(text,tx+2,y+2);
        g.setColor(bright); g.drawString(text,tx,y);
    }

    void drawCentered(Graphics2D g, String text, Color c, int y) {
        g.setColor(c);
        FontMetrics fm=g.getFontMetrics();
        g.drawString(text,(SCREEN_W-fm.stringWidth(text))/2,y);
    }

    Color blend(Color a, Color b, float t) {
        t=Math.max(0,Math.min(1,t));
        return new Color(
            (int)(a.getRed()  *(1-t)+b.getRed()  *t),
            (int)(a.getGreen()*(1-t)+b.getGreen()*t),
            (int)(a.getBlue() *(1-t)+b.getBlue() *t));
    }

    // ── Key input ─────────────────────────────────────────────────────────
    @Override public void keyPressed(KeyEvent e) {
        int k=e.getKeyCode();
        switch (screen) {
            case MENU -> {
                if (k==KeyEvent.VK_ENTER){ level=1; lives=3; loadLevel(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_M)    sound.muted=!sound.muted;
                if (k==KeyEvent.VK_Q)    System.exit(0);
            }
            case PLAYING -> {
                if (k==KeyEvent.VK_UP    ||k==KeyEvent.VK_W){ queueDx=0;queueDy=-1;moveQueued=true; }
                if (k==KeyEvent.VK_DOWN  ||k==KeyEvent.VK_S){ queueDx=0;queueDy=1; moveQueued=true; }
                if (k==KeyEvent.VK_LEFT  ||k==KeyEvent.VK_A){ queueDx=-1;queueDy=0;moveQueued=true; }
                if (k==KeyEvent.VK_RIGHT ||k==KeyEvent.VK_D){ queueDx=1; queueDy=0;moveQueued=true; }
                if (k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if (k==KeyEvent.VK_R)    { loadLevel(); }
                if (k==KeyEvent.VK_M)    sound.muted=!sound.muted;
            }
            case PAUSED -> {
                if (k==KeyEvent.VK_ENTER) screen=Screen.PLAYING;
                if (k==KeyEvent.VK_R)     { loadLevel(); screen=Screen.PLAYING; }
                if (k==KeyEvent.VK_Q)     screen=Screen.MENU;
            }
            case LEVEL_COMPLETE -> {
                if (k==KeyEvent.VK_P){ level++; loadLevel(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; lives=3; }
            }
            case GAME_OVER -> {
                if (k==KeyEvent.VK_R){ lives=3; level=1; loadLevel(); screen=Screen.PLAYING; sound.play("start"); }
                if (k==KeyEvent.VK_Q){ screen=Screen.MENU; level=1; lives=3; }
            }
            case WIN -> {
                if (k==KeyEvent.VK_Q||k==KeyEvent.VK_ENTER){ screen=Screen.MENU; level=1; lives=3; }
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    // ══════════════════════════════════════════════════════════════════════
    // INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════

    class Guardian {
        int col, row;
        boolean isElite;
        int moveCd;
        static final int MOVE_SPEED = 50;

        Guardian(int c, int r, boolean elite) {
            col=c; row=r; isElite=elite; moveCd=rng.nextInt(MOVE_SPEED);
        }

        void tickMove() {
            moveCd--;
            if (moveCd > 0) return;
            moveCd = isElite ? MOVE_SPEED/2 : MOVE_SPEED;
            // BFS toward player (simple pathfind)
            int pr=playerPos[1], pc=playerPos[0];
            int[][] prev = new int[ROWS*COLS][2];
            for (int[] a: prev) { a[0]=-1; a[1]=-1; }
            boolean[][] vis = new boolean[ROWS][COLS];
            Queue<int[]> q = new LinkedList<>();
            q.add(new int[]{row,col});
            vis[row][col]=true;
            int[][] nd={{0,1},{0,-1},{1,0},{-1,0}};
            outer:
            while (!q.isEmpty()) {
                int[] cur=q.poll();
                int cr=cur[0], cc=cur[1];
                for (int[] d:nd) {
                    int nr2=cr+d[0], nc2=cc+d[1];
                    if (nr2<0||nr2>=ROWS||nc2<0||nc2>=COLS) continue;
                    if (vis[nr2][nc2]) continue;
                    if (tileMap[nr2][nc2]==T_WALL) continue;
                    vis[nr2][nc2]=true;
                    prev[nr2*COLS+nc2][0]=cr; prev[nr2*COLS+nc2][1]=cc;
                    if (nr2==pr && nc2==pc) break outer;
                    q.add(new int[]{nr2,nc2});
                }
            }
            // Walk back one step
            if (prev[pr*COLS+pc][0]>=0) {
                int br=pr, bc=pc;
                while (prev[br*COLS+bc][0]!=row || prev[br*COLS+bc][1]!=col) {
                    int pr2=prev[br*COLS+bc][0], pc2=prev[br*COLS+bc][1];
                    if (pr2==row && pc2==col) break;
                    br=pr2; bc=pc2;
                }
                // br,bc is the step right next to guardian toward player
                boolean occupied=false;
                for (Guardian gg:guardians) if(gg!=this&&gg.row==br&&gg.col==bc) occupied=true;
                if (!occupied) { row=br; col=bc; }
            }
        }
    }

    class Sparkle {
        float x,y,vx,vy; Color c; int life,maxLife; boolean dead;
        Sparkle(int x,int y,Color c,float vx,float vy,int life){
            this.x=x;this.y=y;this.c=c;this.vx=vx;this.vy=vy;this.life=this.maxLife=life;
        }
        void update(){ x+=vx;y+=vy;vx*=0.88f;vy*=0.88f; if(--life<=0) dead=true; }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SOUND
    // ══════════════════════════════════════════════════════════════════════
    static class SoundBox {
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
                case "key"     -> synth(0.18, f->sine(f,660+f*80)*adsr(f,.01,.05,.5,.1)*.45);
                case "hurt"    -> synth(0.22, f->noise()*adsr(f,.005,.03,.3,.06)*.5);
                case "die"     -> synth(0.50, f->(sine(f,220*Math.pow(.3,f))+noise()*.2)*adsr(f,.01,.05,.4,.1)*.55);
                case "levelup" -> synth(0.60, f->arpUp(f)*adsr(f,.01,.1,.6,.1)*.5);
                case "start"   -> synth(0.35, f->(sine(f,440+f*80)+sine(f,660))*.3*adsr(f,.01,.05,.55,.1));
                default        -> null;
            };
        }

        static double sine(double t,double f){return Math.sin(2*Math.PI*f*t);}
        static final Random NR=new Random();
        static double noise(){return NR.nextDouble()*2-1;}
        static double adsr(double t,double a,double d,double s,double r){
            if(t<a) return t/a;
            if(t<a+d) return 1-(1-.7)*((t-a)/d);
            if(t<1-r) return .7;
            return .7*(1-(t-(1-r))/r);
        }
        static double arpUp(double t){
            double[] fr={330,415,494,659,880};
            int i=Math.min((int)(t*fr.length),fr.length-1);
            return sine(t,fr[i])+sine(t,fr[i]*2)*.25;
        }
        byte[] synth(double dur, java.util.function.DoubleUnaryOperator fn){
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
            JFrame f=new JFrame("MazeQuest: Dungeon of Echoes");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            MazeQuestGame game=new MazeQuestGame();
            f.add(game); f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}