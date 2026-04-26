/*
 * Enhanced Football Game — Advanced Java Programming
 * @author Elysee NIYIBIZI | @Reg No. 2305000921
 * Enhanced with Tournaments: AFCON, Premier League, Champions League
 */
package Assignments.SideFootballGame;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.io.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║          KICK OFF! ULTIMATE — 5-a-Side Football                        ║
 * ║          Advanced Java Programming — UoK                               ║
 * ║          Lecturer: Dr. NTEZIRIZA NKERABAHIZI Josbert                   ║
 * ╠══════════════════════════════════════════════════════════════════════════╣
 * ║  CONTROLS (Player controls YOUR team):                                 ║
 * ║   ARROW KEYS  — Move selected player                                   ║
 * ║   SPACE       — Kick / Shoot (hold to charge power)                    ║
 * ║   TAB         — Switch to nearest player                               ║
 * ║   ENTER       — Start game / confirm menu                              ║
 * ║   P / ESC     — Pause                                                  ║
 * ║   M           — Mute / Unmute                                          ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 */
public class FootballGame extends JPanel implements ActionListener, KeyListener {

    // ══════════════════════════════════════════════════════════════════
    //  CONSTANTS
    // ══════════════════════════════════════════════════════════════════
    static final int W = 1024, H = 720;
    static final int FPS = 60;

    static final int PX = 70,  PY = 90;
    static final int PW = 884, PH = 540;

    static final int GW = 16;
    static final int GH = 96;
    static final int GY = PY + PH/2 - GH/2;
    static final int LGX = PX;
    static final int RGX = PX + PW;

    static final int  PLAYER_R  = 14;
    static final float BALL_R   = 9f;
    static final float BALL_FRICTION = 0.978f;
    static final float PLAYER_SPEED  = 3.2f;
    static final float AI_SPEED_EASY  = 1.3f;   // Much slower — genuinely easy
    static final float AI_SPEED_MED   = 2.8f;
    static final float AI_SPEED_HARD  = 3.6f;

    static final int HALF_DURATION = 90;
    static final String SCORE_FILE = "football_scores.txt";

    // ══════════════════════════════════════════════════════════════════
    //  ENUMS
    // ══════════════════════════════════════════════════════════════════
    enum Screen { MENU, TOURNAMENT_SELECT, TEAM_SELECT, BRACKET, PLAYING,
                  HALFTIME, PAUSED, GOAL_ANIM, FULL_TIME, SCORES }
    enum Difficulty { EASY, MEDIUM, HARD }
    enum PlayerState { IDLE, RUN, KICK }
    enum TournamentType { FRIENDLY, AFCON, PREMIER_LEAGUE, CHAMPIONS_LEAGUE }

    // ══════════════════════════════════════════════════════════════════
    //  TOURNAMENT DATA
    // ══════════════════════════════════════════════════════════════════
    static final String[][]  AFCON_TEAMS = {
        {"RWANDA STARS",   "AMAVUBI",       "RWA"},
        {"SENEGAL LIONS",  "LIONS DE LA T.","SEN"},
        {"EGYPT PHARAOHS", "PHARAOHS",      "EGY"},
        {"MOROCCO ATLAS",  "ATLAS LIONS",   "MAR"},
        {"NIGERIA EAGLES", "SUPER EAGLES",  "NGA"},
        {"GHANA BLACK ST.", "BLACK STARS",  "GHA"},
        {"CAMEROON LIONS", "INDOMITABLE",   "CMR"},
        {"IVORY COAST",    "LES ÉLÉPHANTS", "CIV"},
        {"MALI EAGLES",    "MALI",          "MLI"},
        {"ALGERIA FOXES",  "LES FENNECS",   "ALG"},
        {"SOUTH AFRICA",   "BAFANA BAFANA", "RSA"},
        {"DR CONGO",       "LÉOPARDS",      "COD"},
        {"TANZANIA",       "TAIFA STARS",   "TZA"},
        {"UGANDA CRANES",  "CRANES",        "UGA"},
        {"ZAMBIA COPPER",  "CHIPOLOPOLO",   "ZMB"},
        {"TUNISIA EAGLES", "AIGLES DE CART","TUN"},
    };
    static final Color[][] AFCON_COLORS = {
        {new Color(0,150,50),   new Color(255,215,0)},   // Rwanda - green/gold
        {new Color(0,70,153),   new Color(255,215,0)},   // Senegal
        {new Color(187,0,0),    new Color(255,255,255)}, // Egypt
        {new Color(196,0,26),   new Color(0,98,51)},     // Morocco
        {new Color(0,68,102),   new Color(0,180,80)},    // Nigeria  -- green kit
        {new Color(0,100,0),    new Color(255,215,0)},   // Ghana
        {new Color(0,100,0),    new Color(255,50,50)},   // Cameroon
        {new Color(245,130,32), new Color(0,70,153)},    // Ivory Coast
        {new Color(0,50,160),   new Color(255,215,0)},   // Mali
        {new Color(0,60,120),   new Color(255,255,255)}, // Algeria
        {new Color(0,56,168),   new Color(255,215,0)},   // South Africa
        {new Color(0,80,160),   new Color(255,215,0)},   // DR Congo
        {new Color(0,115,47),   new Color(255,215,0)},   // Tanzania
        {new Color(0,0,0),      new Color(255,200,0)},   // Uganda
        {new Color(0,102,51),   new Color(255,102,0)},   // Zambia
        {new Color(230,0,18),   new Color(255,255,255)}, // Tunisia
    };

    static final String[][] EPL_TEAMS = {
        {"MAN CITY",      "THE CITIZENS",   "MCI"},
        {"ARSENAL",       "THE GUNNERS",    "ARS"},
        {"LIVERPOOL",     "THE REDS",       "LIV"},
        {"MAN UNITED",    "RED DEVILS",     "MNU"},
        {"CHELSEA",       "THE BLUES",      "CHE"},
        {"TOTTENHAM",     "SPURS",          "TOT"},
        {"NEWCASTLE",     "MAGPIES",        "NEW"},
        {"ASTON VILLA",   "THE VILLANS",    "AVL"},
        {"BRIGHTON",      "SEAGULLS",       "BHA"},
        {"WEST HAM",      "THE HAMMERS",    "WHU"},
        {"EVERTON",       "THE TOFFEES",    "EVE"},
        {"BRENTFORD",     "THE BEES",       "BRE"},
        {"FULHAM",        "THE COTTAGERS",  "FUL"},
        {"WOLVES",        "WANDERERS",      "WOL"},
        {"CRYSTAL PALACE","THE EAGLES",     "CRY"},
        {"LEICESTER",     "THE FOXES",      "LEI"},
        {"BOURNEMOUTH",   "THE CHERRIES",   "BOU"},
        {"NOTM FOREST",   "THE TRICKY TREES","NFO"},
        {"IPSWICH",       "BLUES",          "IPS"},
        {"SOUTHAMPTON",   "THE SAINTS",     "SOU"},
    };
    static final Color[][] EPL_COLORS = {
        {new Color(108,171,221),new Color(255,255,255)}, // Man City sky blue
        {new Color(239,1,7),    new Color(255,255,255)}, // Arsenal red
        {new Color(200,16,46),  new Color(255,215,0)},   // Liverpool red/gold
        {new Color(218,0,24),   new Color(0,0,0)},       // Man United
        {new Color(3,70,148),   new Color(255,255,255)}, // Chelsea
        {new Color(19,34,122),  new Color(255,255,255)}, // Spurs
        {new Color(35,35,35),   new Color(255,255,255)}, // Newcastle
        {new Color(149,45,152), new Color(115,185,43)},  // Aston Villa
        {new Color(0,87,184),   new Color(255,205,0)},   // Brighton
        {new Color(122,38,58),  new Color(100,155,200)}, // West Ham
        {new Color(0,44,130),   new Color(255,255,255)}, // Everton
        {new Color(227,6,19),   new Color(255,255,255)}, // Brentford
        {new Color(0,0,0),      new Color(255,255,255)}, // Fulham
        {new Color(253,185,19), new Color(0,0,0)},       // Wolves
        {new Color(27,69,143),  new Color(180,30,30)},   // Crystal Palace
        {new Color(0,83,160),   new Color(255,255,255)}, // Leicester
        {new Color(218,41,28),  new Color(0,0,0)},       // Bournemouth
        {new Color(227,0,27),   new Color(255,255,255)}, // Notm Forest
        {new Color(0,56,168),   new Color(255,255,255)}, // Ipswich
        {new Color(215,25,32),  new Color(255,255,255)}, // Southampton
    };

    static final String[][] UCL_TEAMS = {
        {"FC BARCELONA",    "BARÇA",           "BAR"},  // 0 — your fave!
        {"REAL MADRID",     "LOS BLANCOS",     "RMA"},
        {"MAN CITY",        "THE CITIZENS",    "MCI"},
        {"BAYERN MUNICH",   "DIE ROTEN",       "BAY"},
        {"PSG",             "LES PARISIENS",   "PSG"},
        {"ARSENAL",         "THE GUNNERS",     "ARS"},
        {"LIVERPOOL",       "THE REDS",        "LIV"},
        {"INTER MILAN",     "I NERAZZURRI",    "INT"},
        {"ATLETICO MADRID", "COLCHONEROS",     "ATM"},
        {"JUVENTUS",        "LA VECCHIA S.",   "JUV"},
        {"BORUSSIA DORTMD", "BVB",             "BVB"},
        {"BENFICA",         "EAGLES",          "BEN"},
        {"PORTO",           "DRAGÕES",         "POR"},
        {"AC MILAN",        "ROSSONERI",       "MIL"},
        {"AJAX",            "DE GODENZONEN",   "AJX"},
        {"CHELSEA",         "THE BLUES",       "CHE"},
    };
    static final Color[][] UCL_COLORS = {
        {new Color(0,82,147),   new Color(165,0,0)},     // Barcelona blue/red
        {new Color(255,255,255),new Color(255,215,0)},   // Real Madrid
        {new Color(108,171,221),new Color(255,255,255)}, // Man City
        {new Color(220,30,30),  new Color(255,255,255)}, // Bayern
        {new Color(0,10,60),    new Color(255,50,50)},   // PSG
        {new Color(239,1,7),    new Color(255,255,255)}, // Arsenal
        {new Color(200,16,46),  new Color(255,215,0)},   // Liverpool
        {new Color(0,0,0),      new Color(0,80,180)},    // Inter
        {new Color(204,0,0),    new Color(255,255,255)}, // Atletico
        {new Color(0,0,0),      new Color(255,255,255)}, // Juventus
        {new Color(255,205,0),  new Color(0,0,0)},       // Dortmund
        {new Color(230,0,30),   new Color(255,255,255)}, // Benfica
        {new Color(0,42,139),   new Color(255,255,255)}, // Porto
        {new Color(180,0,0),    new Color(0,0,0)},       // AC Milan
        {new Color(210,0,0),    new Color(255,255,255)}, // Ajax
        {new Color(3,70,148),   new Color(255,255,255)}, // Chelsea
    };

    // ══════════════════════════════════════════════════════════════════
    //  GAME STATE
    // ══════════════════════════════════════════════════════════════════
    Screen     screen     = Screen.MENU;
    Difficulty difficulty = Difficulty.MEDIUM;
    TournamentType tournamentType = TournamentType.FRIENDLY;

    // Currently active teams (resolved from tournament)
    String[]   blueTeamData, redTeamData;
    Color[]    blueTeamColors, redTeamColors;

    // Selection indices within current tournament
    int blueTeamIdx = 0;
    int redTeamIdx  = 1;

    int scoreBlue = 0, scoreRed = 0;
    int half = 1;
    int matchTimer = 0;
    int menuSelection = 0;
    int tournamentMenuSel = 0;
    int goalAnimTimer = 0;
    boolean lastGoalBlue = false;
    int halfTimeTimer = 0;
    long tick = 0;
    int screenShake = 0;
    boolean muteSound = false;

    // Tournament bracket state
    int[] bracketResults;        // winner indices per match
    int currentBracketMatch = 0;
    int tournamentRound = 0;     // 0=QF,1=SF,2=Final
    int[] bracketTeams;          // shuffled team indices for bracket
    boolean playerInTournament = false;
    boolean tournamentOver = false;
    String tournamentWinnerName = "";

    // Stats
    int[] shots   = {0,0};
    int[] shotsOnTarget = {0,0};
    int[] possession = {0,0};
    int hiScore = 0;

    // ══════════════════════════════════════════════════════════════════
    //  BALL & PLAYERS
    // ══════════════════════════════════════════════════════════════════
    Ball ball;
    Player[] bluePlayers = new Player[5];
    Player[] redPlayers  = new Player[5];
    int controlledIdx = 0;

    // ── Kick sides — track which side each team attacks ──────────────
    // blueAttacksRight = true means blue attacks right goal (RGX), red attacks left (LGX)
    boolean blueAttacksRight = true;

    // ══════════════════════════════════════════════════════════════════
    //  POWER SHOT
    // ══════════════════════════════════════════════════════════════════
    boolean spaceHeld  = false;
    float   shootPower = 0f;

    // ══════════════════════════════════════════════════════════════════
    //  INPUT
    // ══════════════════════════════════════════════════════════════════
    boolean kUp,kDown,kLeft,kRight,kSpace,kTab;
    boolean kSpacePrev = false;

    // ══════════════════════════════════════════════════════════════════
    //  CROWD / VISUAL
    // ══════════════════════════════════════════════════════════════════
    float[]  crowdX   = new float[140];
    float[]  crowdY   = new float[140];
    Color[]  crowdC   = new Color[140];
    float[]  crowdBob = new float[140];

    List<Particle>  particles = new ArrayList<>();
    List<FloatText> floaters  = new ArrayList<>();
    List<int[]>     confetti  = new ArrayList<>();

    SFX sound = new SFX();
    Random rng = new Random();
    javax.swing.Timer gameTimer;

    // ══════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════
    public FootballGame() {
        setPreferredSize(new Dimension(W, H));
        setBackground(new Color(20, 80, 20));
        setFocusable(true);
        addKeyListener(this);
        initCrowd();
        loadHiScore();
        blueTeamData   = AFCON_TEAMS[0];
        blueTeamColors = AFCON_COLORS[0];
        redTeamData    = AFCON_TEAMS[1];
        redTeamColors  = AFCON_COLORS[1];
        gameTimer = new javax.swing.Timer(1000 / FPS, this);
        gameTimer.start();
    }

    void initCrowd() {
        for (int i = 0; i < 70; i++) {
            crowdX[i]   = 70 + rng.nextFloat() * 884;
            crowdY[i]   = 16 + rng.nextFloat() * 58;
            crowdC[i]   = randomCrowdColor();
            crowdBob[i] = rng.nextFloat() * (float)(Math.PI * 2);
        }
        for (int i = 70; i < 140; i++) {
            crowdX[i]   = 70 + rng.nextFloat() * 884;
            crowdY[i]   = PY + PH + 14 + rng.nextFloat() * 52;
            crowdC[i]   = randomCrowdColor();
            crowdBob[i] = rng.nextFloat() * (float)(Math.PI * 2);
        }
    }

    Color randomCrowdColor() {
        Color[] cs = {
            new Color(220,40,40), new Color(0,120,220), new Color(255,200,0),
            new Color(0,180,80),  new Color(255,100,20), new Color(180,0,180),
            new Color(255,255,255), new Color(40,40,40), new Color(255,150,150)
        };
        return cs[rng.nextInt(cs.length)];
    }

    // ══════════════════════════════════════════════════════════════════
    //  TEAM RESOLUTION HELPERS
    // ══════════════════════════════════════════════════════════════════
    String[][] getTeamsForTournament() {
        return switch (tournamentType) {
            case AFCON            -> AFCON_TEAMS;
            case PREMIER_LEAGUE   -> EPL_TEAMS;
            case CHAMPIONS_LEAGUE -> UCL_TEAMS;
            default               -> AFCON_TEAMS;
        };
    }
    Color[][] getColorsForTournament() {
        return switch (tournamentType) {
            case AFCON            -> AFCON_COLORS;
            case PREMIER_LEAGUE   -> EPL_COLORS;
            case CHAMPIONS_LEAGUE -> UCL_COLORS;
            default               -> AFCON_COLORS;
        };
    }

    // ══════════════════════════════════════════════════════════════════
    //  GAME SETUP
    // ══════════════════════════════════════════════════════════════════
    void startMatch() {
        scoreBlue=0; scoreRed=0; half=1; matchTimer=0;
        shots[0]=shots[1]=0; shotsOnTarget[0]=shotsOnTarget[1]=0;
        possession[0]=possession[1]=0;
        particles.clear(); floaters.clear(); confetti.clear();
        // Half 1: blue attacks right, red attacks left
        blueAttacksRight = true;
        setupPlayers();
        placeBallKickOff();
        screen = Screen.PLAYING;
        sound.play("whistle");
    }

    void setupPlayers() {
        Color bc = blueTeamColors[0];
        Color bs = blueTeamColors[1];
        Color rc = redTeamColors[0];
        Color rs = redTeamColors[1];

        // Blue team: GK on LEFT side, attacks RIGHT
        int[][] bluePos = {
            {PX+32,   PY+PH/2},
            {PX+130,  PY+PH/4},
            {PX+130,  PY+3*PH/4},
            {PX+260,  PY+PH/2},
            {PX+390,  PY+PH/2},
        };
        String[] blueNums  = {"1","4","5","8","9"};
        String[] blueNames = {"MURERA","RUBIO","KALISA","MUGABO","NDAYISHIMIYE"};
        for (int i=0;i<5;i++){
            bluePlayers[i] = new Player(bluePos[i][0], bluePos[i][1], bc, bs,
                    blueNums[i], blueNames[i], true, i==0);
            bluePlayers[i].homeX = bluePos[i][0];
            bluePlayers[i].homeY = bluePos[i][1];
        }

        // Red team: GK on RIGHT side, attacks LEFT
        int[][] redPos = {
            {PX+PW-32,  PY+PH/2},
            {PX+PW-130, PY+PH/4},
            {PX+PW-130, PY+3*PH/4},
            {PX+PW-260, PY+PH/2},
            {PX+PW-390, PY+PH/2},
        };
        String[] redNums  = {"1","3","6","10","7"};
        String[] redNames = {"KAMAU","ODHIAMBO","WAWERU","KIPCHOGE","MWANGI"};
        for (int i=0;i<5;i++){
            redPlayers[i] = new Player(redPos[i][0], redPos[i][1], rc, rs,
                    redNums[i], redNames[i], false, i==0);
            redPlayers[i].homeX = redPos[i][0];
            redPlayers[i].homeY = redPos[i][1];
        }
        controlledIdx = 4;
    }

    void placeBallKickOff() {
        ball = new Ball(PX + PW/2, PY + PH/2, 0, 0);
        for (Player p : bluePlayers) { p.x=p.homeX; p.y=p.homeY; p.state=PlayerState.IDLE; }
        for (Player p : redPlayers)  { p.x=p.homeX; p.y=p.homeY; p.state=PlayerState.IDLE; }
        controlledIdx = 4;
    }

    // Called at half time — swap sides properly
    void startSecondHalf() {
        half = 2; matchTimer = 0;
        // Flip the attack direction
        blueAttacksRight = false; // In 2nd half blue attacks LEFT, red attacks RIGHT

        // Mirror all home positions horizontally
        for (Player p : bluePlayers) {
            p.homeX = PX + PW - (p.homeX - PX);
            p.homeY = p.homeY; // Y unchanged
            p.x = p.homeX;
            p.y = p.homeY;
        }
        for (Player p : redPlayers) {
            p.homeX = PX + PW - (p.homeX - PX);
            p.homeY = p.homeY;
            p.x = p.homeX;
            p.y = p.homeY;
        }
        ball.x = PX + PW/2; ball.y = PY + PH/2; ball.vx = 0; ball.vy = 0;
        screen = Screen.PLAYING;
        sound.play("whistle");
    }

    // ══════════════════════════════════════════════════════════════════
    //  TOURNAMENT SETUP
    // ══════════════════════════════════════════════════════════════════
    void initTournament() {
        String[][] teams = getTeamsForTournament();
        int n = Math.min(teams.length, 16);
        // Shuffle and build bracket of 8 teams (QF x4 → SF x2 → Final)
        Integer[] indices = new Integer[n];
        for (int i=0;i<n;i++) indices[i]=i;
        Collections.shuffle(Arrays.asList(indices));
        bracketTeams = new int[8];
        for (int i=0;i<8;i++) bracketTeams[i] = indices[i % n];
        // Ensure player's team (blueTeamIdx) is in bracket at position 0
        bracketTeams[0] = blueTeamIdx;
        bracketResults = new int[7]; // 4 QF + 2 SF + 1 Final
        Arrays.fill(bracketResults, -1);
        currentBracketMatch = 0;
        tournamentRound = 0;
        playerInTournament = true;
        tournamentOver = false;
        tournamentWinnerName = "";
        screen = Screen.BRACKET;
    }

    // Which two teams play in bracket match i?
    int[] bracketMatchup(int matchIdx) {
        // QF: matches 0-3: teams 0v1, 2v3, 4v5, 6v7
        // SF: matches 4-5: winners of QF
        // Final: match 6: winners of SF
        if (matchIdx < 4) {
            return new int[]{ bracketTeams[matchIdx*2], bracketTeams[matchIdx*2+1] };
        } else if (matchIdx < 6) {
            int sf = matchIdx - 4;
            int w1 = bracketResults[sf*2];
            int w2 = bracketResults[sf*2+1];
            return new int[]{ w1, w2 };
        } else {
            return new int[]{ bracketResults[4], bracketResults[5] };
        }
    }

    void simulateAIMatch(int matchIdx) {
        int[] mu = bracketMatchup(matchIdx);
        // AI vs AI — random result weighted by "strength" (index)
        int winner = rng.nextBoolean() ? mu[0] : mu[1];
        bracketResults[matchIdx] = winner;
    }

    void playNextTournamentMatch() {
        if (currentBracketMatch >= 7) return;
        int[] mu = bracketMatchup(currentBracketMatch);
        boolean playerMatch = (mu[0] == blueTeamIdx || mu[1] == blueTeamIdx);
        if (playerMatch) {
            // Player is in this match
            boolean playerIsBlue = (mu[0] == blueTeamIdx);
            Color[][] cols = getColorsForTournament();
            String[][] teams = getTeamsForTournament();
            if (playerIsBlue) {
                blueTeamData   = teams[mu[0]]; blueTeamColors = cols[mu[0]];
                redTeamData    = teams[mu[1]]; redTeamColors  = cols[mu[1]];
            } else {
                blueTeamData   = teams[mu[1]]; blueTeamColors = cols[mu[1]];
                redTeamData    = teams[mu[0]]; redTeamColors  = cols[mu[0]];
            }
            startMatch();
        } else {
            simulateAIMatch(currentBracketMatch);
            currentBracketMatch++;
            if (currentBracketMatch < 7) playNextTournamentMatch();
            else { tournamentOver = true; resolveChampion(); screen = Screen.BRACKET; }
        }
    }

    void resolveChampion() {
        Color[][] cols = getColorsForTournament();
        String[][] teams = getTeamsForTournament();
        int champIdx = bracketResults[6];
        if (champIdx >= 0 && champIdx < teams.length) {
            tournamentWinnerName = teams[champIdx][0];
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  GAME LOOP
    // ══════════════════════════════════════════════════════════════════
    @Override public void actionPerformed(ActionEvent e) {
        tick++;
        for (int i=0;i<140;i++) crowdBob[i] += 0.06f + rng.nextFloat()*0.02f;
        switch(screen) {
            case PLAYING   -> updatePlaying();
            case GOAL_ANIM -> updateGoalAnim();
            case HALFTIME  -> { halfTimeTimer++; if(halfTimeTimer>240) startSecondHalf(); }
            default -> {}
        }
        particles.removeIf(p->{ p.update(); return p.dead; });
        floaters.removeIf(f->{ f.update(); return f.dead; });
        if (screenShake>0) screenShake--;
        repaint();
    }

    void updatePlaying() {
        matchTimer++;
        int halfFrames = HALF_DURATION * FPS;

        if (matchTimer >= halfFrames && half==1) {
            sound.play("whistle");
            screen = Screen.HALFTIME;
            halfTimeTimer = 0;
            return;
        }
        if (matchTimer >= halfFrames && half==2) {
            sound.play("whistle"); sound.play("whistle");
            saveScore(Math.max(scoreBlue, scoreRed));
            if (Math.max(scoreBlue,scoreRed) > hiScore) { hiScore=Math.max(scoreBlue,scoreRed); saveHiScore(); }
            screen = Screen.FULL_TIME;
            // If in tournament, record result
            if (playerInTournament) {
                int[] mu = bracketMatchup(currentBracketMatch);
                boolean playerIsBlue = (mu[0] == blueTeamIdx);
                if (playerIsBlue) {
                    bracketResults[currentBracketMatch] = (scoreBlue >= scoreRed) ? mu[0] : mu[1];
                } else {
                    bracketResults[currentBracketMatch] = (scoreBlue >= scoreRed) ? mu[1] : mu[0];
                }
                currentBracketMatch++;
            }
            return;
        }

        if (kTab) { switchControl(); kTab=false; }

        Player cp = bluePlayers[controlledIdx];
        float dx=0, dy=0;
        if (kLeft)  dx=-PLAYER_SPEED;
        if (kRight) dx= PLAYER_SPEED;
        if (kUp)    dy=-PLAYER_SPEED;
        if (kDown)  dy= PLAYER_SPEED;
        if (dx!=0&&dy!=0){ dx*=0.707f; dy*=0.707f; }
        if (dx!=0||dy!=0) {
            cp.x = clamp(cp.x+dx, PX+PLAYER_R+1, PX+PW-PLAYER_R-1);
            cp.y = clamp(cp.y+dy, PY+PLAYER_R+1, PY+PH-PLAYER_R-1);
            cp.state = PlayerState.RUN;
            cp.facingAngle = (float)Math.atan2(dy,dx);
        } else { cp.state = PlayerState.IDLE; }

        boolean spaceNow = kSpace;
        if (spaceNow && !kSpacePrev) { shootPower=0f; spaceHeld=true; }
        if (spaceNow && spaceHeld)   shootPower = Math.min(1.0f, shootPower+0.025f);
        if (!spaceNow && kSpacePrev && spaceHeld) {
            tryKick(cp, shootPower);
            shootPower=0f; spaceHeld=false;
        }
        kSpacePrev = spaceNow;

        float aiSpd = aiSpeed();
        for (int i=0;i<5;i++) {
            if (i==controlledIdx) continue;
            blueAI(bluePlayers[i], i, aiSpd);
        }
        for (int i=0;i<5;i++) redAI(redPlayers[i], i, aiSpd);

        ball.update();
        autoKickAI();

        for (Player p : bluePlayers) p.update();
        for (Player p : redPlayers)  p.update();

        Player nearest = closestToBall(bluePlayers);
        Player nearestR = closestToBall(redPlayers);
        float dB = dist(nearest.x,nearest.y,ball.x,ball.y);
        float dR = dist(nearestR.x,nearestR.y,ball.x,ball.y);
        if (dB < dR) possession[0]++; else possession[1]++;

        if (dist(cp.x,cp.y,ball.x,ball.y) > 150) {
            controlledIdx = nearestBlueIdx();
        }

        checkGoal();
        keepBallInBounds();
    }

    float aiSpeed(){
        return switch(difficulty){
            case EASY   -> AI_SPEED_EASY;
            case MEDIUM -> AI_SPEED_MED;
            case HARD   -> AI_SPEED_HARD;
        };
    }

    // ── BLUE AI ── (attacks the goal that is on the RIGHT in half 1, LEFT in half 2)
    void blueAI(Player p, int idx, float spd) {
        // GK stays in front of BLUE's own goal
        if (p.isGK) {
            float gkX = blueAttacksRight ? PX + 42 : PX + PW - 42;
            float gkY = clamp(ball.y, PY + PH/2f - GH/2f + 10, PY + PH/2f + GH/2f - 10);
            moveToward(p, gkX, gkY, spd * 1.1f);
            if (dist(p.x,p.y,ball.x,ball.y)<32) autoKick(p, false);
            return;
        }
        float targetX, targetY;
        float ownHalfX = blueAttacksRight ? PX + PW/2f : PX + PW/2f;
        boolean ballOnOurHalf = blueAttacksRight ? ball.x < ownHalfX : ball.x > ownHalfX;
        if (ballOnOurHalf) {
            targetX = p.homeX * 0.8f + ball.x * 0.2f;
            targetY = p.homeY * 0.7f + ball.y * 0.3f;
        } else {
            targetX = p.homeX * 0.5f + ball.x * 0.5f;
            targetY = p.homeY * 0.5f + ball.y * 0.5f;
        }
        moveToward(p, targetX, targetY, spd);
        if (dist(p.x,p.y,ball.x,ball.y)<30) autoKick(p, false);
    }

    // ── RED AI ── (attacks the goal on the LEFT in half 1, RIGHT in half 2)
    void redAI(Player p, int idx, float spd) {
        if (p.isGK) {
            float gkX = blueAttacksRight ? PX + PW - 42 : PX + 42;
            float gkY = clamp(ball.y, PY + PH/2f - GH/2f + 10, PY + PH/2f + GH/2f - 10);
            moveToward(p, gkX, gkY, spd * 1.1f);
            if (dist(p.x,p.y,ball.x,ball.y)<32) autoKick(p, true);
            return;
        }
        float chaseX, chaseY;
        boolean ballOnRedHalf = blueAttacksRight ? ball.x > PX + PW/2f : ball.x < PX + PW/2f;
        if (ballOnRedHalf) {
            chaseX = ball.x; chaseY = ball.y;
        } else {
            if (idx == 4) {
                chaseX = blueAttacksRight ? PX + 90 : PX + PW - 90;
                chaseY = PY + PH/2f;
            } else {
                chaseX = p.homeX * 0.4f + ball.x * 0.6f;
                chaseY = p.homeY * 0.5f + ball.y * 0.5f;
            }
        }
        // Easy mode: red AI sometimes wanders off target
        if (difficulty == Difficulty.EASY && rng.nextInt(120) == 0) {
            chaseX += (rng.nextFloat()-0.5f)*200;
            chaseY += (rng.nextFloat()-0.5f)*150;
        }
        moveToward(p, chaseX, chaseY, spd);
        if (dist(p.x,p.y,ball.x,ball.y)<28) autoKick(p, true);
    }

    void moveToward(Player p, float tx, float ty, float spd) {
        float dx=tx-p.x, dy=ty-p.y;
        float d=(float)Math.sqrt(dx*dx+dy*dy);
        if (d<2){ p.state=PlayerState.IDLE; return; }
        float nx=dx/d, ny=dy/d;
        p.x = clamp(p.x+nx*spd, PX+PLAYER_R, PX+PW-PLAYER_R);
        p.y = clamp(p.y+ny*spd, PY+PLAYER_R, PY+PH-PLAYER_R);
        p.facingAngle = (float)Math.atan2(dy,dx);
        p.state = PlayerState.RUN;
    }

    void autoKick(Player p, boolean isRedTeam) {
        // Shoot toward OPPONENT's goal
        float goalX, goalY;
        if (isRedTeam) {
            // Red shoots toward BLUE's goal
            goalX = blueAttacksRight ? PX + 10 : PX + PW - 10;
        } else {
            // Blue AI shoots toward RED's goal
            goalX = blueAttacksRight ? PX + PW - 10 : PX + 10;
        }
        goalY = PY + PH/2f + (rng.nextFloat()-0.5f) * 30;

        float shotDx = goalX - ball.x, shotDy = goalY - ball.y;
        float sl = (float)Math.sqrt(shotDx*shotDx+shotDy*shotDy);
        if (sl>0){ shotDx/=sl; shotDy/=sl; }

        float power = 0.5f + rng.nextFloat()*0.4f;
        float spd2  = 7f + power*6f;

        // Easy mode: huge inaccuracy for red, tiny for blue AI
        float acc;
        if (difficulty == Difficulty.EASY) {
            acc = isRedTeam ? 0.80f : 0.20f;
        } else if (difficulty == Difficulty.MEDIUM) {
            acc = 0.20f;
        } else {
            acc = 0.08f;
        }
        shotDx += (rng.nextFloat()-0.5f)*acc;
        shotDy += (rng.nextFloat()-0.5f)*acc;

        ball.vx = shotDx*spd2; ball.vy = shotDy*spd2;
        p.state = PlayerState.KICK; p.kickAnim = 8;
        sound.play("kick");
        if (isRedTeam) shots[1]++; else shots[0]++;
    }

    void autoKickAI() {
        for (Player rp : redPlayers) {
            if (dist(rp.x,rp.y,ball.x,ball.y) < PLAYER_R+BALL_R+2 && rng.nextInt(4)==0) {
                autoKick(rp, true); break;
            }
        }
        for (int i=0;i<5;i++) {
            if (i==controlledIdx) continue;
            Player bp = bluePlayers[i];
            if (dist(bp.x,bp.y,ball.x,ball.y) < PLAYER_R+BALL_R+2 && rng.nextInt(5)==0) {
                autoKick(bp, false); break;
            }
        }
    }

    void tryKick(Player p, float power) {
        if (dist(p.x,p.y,ball.x,ball.y) > PLAYER_R + BALL_R + 20) return;
        // Player shoots toward RED's goal
        float goalX = blueAttacksRight ? PX + PW - 10 : PX + 10;
        float goalY = PY + PH/2f + (rng.nextFloat()-0.5f)*20;
        float dx=goalX-p.x, dy=goalY-p.y;
        float dl=(float)Math.sqrt(dx*dx+dy*dy);
        if (dl>0){dx/=dl;dy/=dl;}
        float spd2 = 6f + power*12f;
        ball.vx=dx*spd2; ball.vy=dy*spd2;
        p.state=PlayerState.KICK; p.kickAnim=10;
        sound.play("kick"); shots[0]++;
        spawnKickFx((int)ball.x,(int)ball.y);
    }

    void switchControl() {
        controlledIdx = nearestBlueIdx();
        sound.play("switch");
    }

    int nearestBlueIdx() {
        int best=0; float bestD=Float.MAX_VALUE;
        for (int i=0;i<5;i++){
            float d=dist(bluePlayers[i].x,bluePlayers[i].y,ball.x,ball.y);
            if(d<bestD){bestD=d;best=i;}
        }
        return best;
    }

    Player closestToBall(Player[] team){
        Player best=team[0]; float bestD=Float.MAX_VALUE;
        for (Player p:team){float d=dist(p.x,p.y,ball.x,ball.y);if(d<bestD){bestD=d;best=p;}}
        return best;
    }

    // ── GOAL CHECK: uses blueAttacksRight to know which side each team's goal is ──
    void checkGoal() {
        if (blueAttacksRight) {
            // Blue goal on LEFT (LGX), Red goal on RIGHT (RGX)
            if (ball.x <= LGX + GW && ball.y >= GY && ball.y <= GY+GH) {
                // Red scores in blue's left goal
                scoreRed++; lastGoalBlue=false; goalScored();
            }
            if (ball.x >= RGX - GW && ball.y >= GY && ball.y <= GY+GH) {
                // Blue scores in red's right goal
                scoreBlue++; lastGoalBlue=true; shotsOnTarget[0]++; goalScored();
            }
        } else {
            // 2nd half sides swapped: Blue goal on RIGHT, Red goal on LEFT
            if (ball.x >= RGX - GW && ball.y >= GY && ball.y <= GY+GH) {
                // Red scores in blue's right goal
                scoreRed++; lastGoalBlue=false; goalScored();
            }
            if (ball.x <= LGX + GW && ball.y >= GY && ball.y <= GY+GH) {
                // Blue scores in red's left goal
                scoreBlue++; lastGoalBlue=true; shotsOnTarget[0]++; goalScored();
            }
        }
    }

    void goalScored() {
        sound.play("goal");
        screen = Screen.GOAL_ANIM;
        goalAnimTimer = 0;
        screenShake = 22;
        for (int i=0;i<100;i++) {
            confetti.add(new int[]{
                rng.nextInt(W), rng.nextInt(H/2),
                rng.nextInt(10)+2, rng.nextInt(10)+2,
                rng.nextInt(360),
                rng.nextInt(256), rng.nextInt(256), rng.nextInt(256),
                rng.nextInt(6)-3, 2+rng.nextInt(5)
            });
        }
        addFloat("GOAL!", W/2, H/2-60, lastGoalBlue ? blueTeamColors[0] : redTeamColors[0]);
        for (int i=0;i<50;i++) {
            particles.add(new Particle(W/2, H/2,
                new Color(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256)),
                (float)(rng.nextGaussian()*9),(float)(rng.nextGaussian()*9),
                40+rng.nextInt(35)));
        }
    }

    void updateGoalAnim() {
        goalAnimTimer++;
        for (int[] c:confetti) {
            c[0]+=c[8]; c[1]+=c[9]; c[4]=(c[4]+5)%360;
            if (c[1]>H) { c[0]=rng.nextInt(W); c[1]=-10; }
        }
        if (goalAnimTimer>160) {
            confetti.clear();
            placeBallKickOff();
            screen = Screen.PLAYING;
        }
    }

    void keepBallInBounds() {
        if (ball.x <= PX+BALL_R) {
            if (ball.y < GY || ball.y > GY+GH) { ball.x=PX+BALL_R; ball.vx=Math.abs(ball.vx)*0.6f; }
        }
        if (ball.x >= PX+PW-BALL_R) {
            if (ball.y < GY || ball.y > GY+GH) { ball.x=PX+PW-BALL_R; ball.vx=-Math.abs(ball.vx)*0.6f; }
        }
        if (ball.y <= PY+BALL_R)    { ball.y=PY+BALL_R;    ball.vy= Math.abs(ball.vy)*0.6f; sound.play("bounce"); }
        if (ball.y >= PY+PH-BALL_R) { ball.y=PY+PH-BALL_R; ball.vy=-Math.abs(ball.vy)*0.6f; sound.play("bounce"); }
    }

    void spawnKickFx(int x,int y){
        for(int i=0;i<10;i++) particles.add(new Particle(x,y,new Color(255,220,100),
            (float)(rng.nextGaussian()*4),(float)(rng.nextGaussian()*4),10+rng.nextInt(8)));
    }
    void addFloat(String t,int x,int y,Color c){ floaters.add(new FloatText(t,x,y,c)); }
    float dist(float ax,float ay,float bx,float by){float dx=ax-bx,dy=ay-by;return(float)Math.sqrt(dx*dx+dy*dy);}

    // ══════════════════════════════════════════════════════════════════
    //  PAINT
    // ══════════════════════════════════════════════════════════════════
    @Override protected void paintComponent(Graphics g0){
        super.paintComponent(g0);
        Graphics2D g=(Graphics2D)g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (screenShake>0) g.translate(rng.nextInt(screenShake+1)-screenShake/2,
                                       rng.nextInt(screenShake+1)-screenShake/2);
        switch(screen){
            case MENU              -> drawMenu(g);
            case TOURNAMENT_SELECT -> drawTournamentSelect(g);
            case TEAM_SELECT       -> drawTeamSelect(g);
            case BRACKET           -> drawBracket(g);
            case PLAYING           -> drawMatch(g);
            case HALFTIME          -> { drawMatch(g); drawHalfTime(g); }
            case PAUSED            -> { drawMatch(g); drawPause(g); }
            case GOAL_ANIM         -> drawGoalAnim(g);
            case FULL_TIME         -> drawFullTime(g);
            case SCORES            -> drawScores(g);
        }
    }

    // ─── STADIUM ──────────────────────────────────────────────────────
    void drawStadium(Graphics2D g){
        GradientPaint sky=new GradientPaint(0,0,new Color(8,12,40),0,PY,new Color(25,30,70));
        g.setPaint(sky); g.fillRect(0,0,W,PY);
        GradientPaint sky2=new GradientPaint(0,PY+PH,new Color(25,30,70),0,H,new Color(8,12,40));
        g.setPaint(sky2); g.fillRect(0,PY+PH,W,H-(PY+PH));

        // Stars in night sky
        for (int i=0;i<30;i++){
            int sx=(i*97+43)%W, sy=(i*53+11)%(PY-10);
            float br=(float)(0.5+0.5*Math.sin(tick*0.05+i));
            g.setColor(new Color(255,255,255,(int)(br*180)));
            g.fillOval(sx,sy,2,2);
        }

        // Floodlights (4 corners)
        int[][] lights={{PX-30,PY-30},{PX+PW+30,PY-30},{PX-30,PY+PH+30},{PX+PW+30,PY+PH+30}};
        for(int[] lp:lights){
            g.setColor(new Color(255,255,200,8)); g.fillOval(lp[0]-80,lp[1]-80,160,160);
            g.setColor(new Color(255,255,220,20)); g.fillOval(lp[0]-28,lp[1]-28,56,56);
            g.setColor(new Color(255,255,240,200)); g.fillOval(lp[0]-7,lp[1]-7,14,14);
        }

        // Animated crowd
        for(int i=0;i<140;i++){
            float bob=(float)(Math.sin(crowdBob[i])*4);
            Color cc=crowdC[i];
            g.setColor(cc); g.fillOval((int)crowdX[i]-5,(int)(crowdY[i]+bob)-9,10,10);
            g.setColor(cc.darker()); g.fillRect((int)crowdX[i]-4,(int)(crowdY[i]+bob)+1,8,9);
        }

        // Scoreboard banner
        g.setColor(new Color(0,0,0,200));
        g.fillRoundRect(W/2-200,6,400,50,14,14);
        g.setColor(new Color(0,120,255,60));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(W/2-200,6,400,50,14,14);
        g.setStroke(new BasicStroke(1f));
    }

    // ─── PITCH ────────────────────────────────────────────────────────
    void drawPitch(Graphics2D g){
        for(int s=0;s<10;s++){
            int sx=PX+s*(PW/10);
            Color gc=(s%2==0)?new Color(34,130,34):new Color(28,110,28);
            g.setColor(gc); g.fillRect(sx,PY,PW/10,PH);
        }
        g.setColor(new Color(255,255,255,200));
        g.setStroke(new BasicStroke(2.5f));
        g.drawRect(PX,PY,PW,PH);
        g.drawLine(PX+PW/2,PY,PX+PW/2,PY+PH);
        g.drawOval(PX+PW/2-65,PY+PH/2-65,130,130);
        g.setColor(new Color(255,255,255,150)); g.fillOval(PX+PW/2-4,PY+PH/2-4,8,8);
        g.setColor(new Color(255,255,255,200));
        g.drawRect(PX,PY+PH/2-110,110,220);
        g.drawRect(PX,PY+PH/2-GH/2,32,GH);
        g.drawRect(PX+PW-110,PY+PH/2-110,110,220);
        g.drawRect(PX+PW-32,PY+PH/2-GH/2,32,GH);
        g.fillOval(PX+65-3,PY+PH/2-3,6,6);
        g.fillOval(PX+PW-65-3,PY+PH/2-3,6,6);
        g.drawArc(PX-8,PY-8,16,16,270,90);
        g.drawArc(PX+PW-8,PY-8,16,16,180,90);
        g.drawArc(PX-8,PY+PH-8,16,16,0,90);
        g.drawArc(PX+PW-8,PY+PH-8,16,16,90,90);
        // Penalty arcs
        g.drawArc(PX+110-55,PY+PH/2-55,110,110,330,60);
        g.drawArc(PX+PW-110-55,PY+PH/2-55,110,110,150,60);
        g.setStroke(new BasicStroke(1f));

        drawGoal(g, PX-GW, GY, GW, GH, false);
        drawGoal(g, PX+PW, GY, GW, GH, true);

        // Half indicator arrows (which way each team attacks)
        drawAttackArrow(g);
    }

    void drawAttackArrow(Graphics2D g) {
        // Small arrows near centre line showing attack directions
        g.setFont(new Font("Arial",Font.BOLD,11));
        Color bc=blueTeamColors[0];
        Color rc=redTeamColors[0];
        if (blueAttacksRight) {
            g.setColor(bc); drawArrow(g, PX+PW/2+10, PY-22, PX+PW/2+50, PY-22);
            g.setColor(rc); drawArrow(g, PX+PW/2-10, PY-22, PX+PW/2-50, PY-22);
        } else {
            g.setColor(bc); drawArrow(g, PX+PW/2-10, PY-22, PX+PW/2-50, PY-22);
            g.setColor(rc); drawArrow(g, PX+PW/2+10, PY-22, PX+PW/2+50, PY-22);
        }
    }

    void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.setStroke(new BasicStroke(2f));
        g.drawLine(x1,y1,x2,y2);
        int dx=x2-x1>0?1:-1;
        int[] ax={x2, x2-dx*8, x2-dx*8};
        int[] ay={y2, y2-5, y2+5};
        g.fillPolygon(ax,ay,3);
        g.setStroke(new BasicStroke(1f));
    }

    void drawGoal(Graphics2D g, int x, int y, int gw, int gh, boolean right){
        g.setColor(new Color(255,255,255,25));
        if(!right) g.fillRect(x-gw, y, gw, gh);
        else       g.fillRect(x,    y, gw, gh);
        g.setColor(new Color(255,255,255,55));
        g.setStroke(new BasicStroke(0.8f));
        for(int gy2=y;gy2<y+gh;gy2+=12){
            if(!right) g.drawLine(x-gw,gy2,x,gy2);
            else       g.drawLine(x,gy2,x+gw,gy2);
        }
        for(int gx2=(right?x:x-gw);gx2<=(right?x+gw:x);gx2+=12) g.drawLine(gx2,y,gx2,y+gh);
        g.setColor(Color.WHITE); g.setStroke(new BasicStroke(4f));
        g.drawLine(x,y,x,y+gh);
        g.drawLine(x,y,right?x+gw:x-gw,y);
        g.drawLine(x,y+gh,right?x+gw:x-gw,y+gh);
        g.setStroke(new BasicStroke(1f));
    }

    // ─── MATCH SCENE ──────────────────────────────────────────────────
    void drawMatch(Graphics2D g){
        drawStadium(g);
        drawPitch(g);
        for(Player p:redPlayers)  drawPlayerShadow(g,p);
        for(Player p:bluePlayers) drawPlayerShadow(g,p);
        g.setColor(new Color(0,0,0,55)); g.fillOval((int)(ball.x-BALL_R+3),(int)(ball.y+5),(int)(BALL_R*2),(int)BALL_R);
        for(Player p:redPlayers)  drawPlayer(g,p,false);
        for(Player p:bluePlayers) drawPlayer(g,p,true);
        drawBall(g);
        for(Particle p:particles) p.draw(g);
        for(FloatText f:floaters) f.draw(g);
        drawHUD(g);
    }

    void drawPlayerShadow(Graphics2D g, Player p){
        g.setColor(new Color(0,0,0,45));
        g.fillOval((int)(p.x-PLAYER_R+3),(int)(p.y+PLAYER_R-3),PLAYER_R*2-2,8);
    }

    void drawPlayer(Graphics2D g, Player p, boolean isBlue){
        Graphics2D g2=(Graphics2D)g.create();
        g2.translate(p.x,p.y);
        boolean facingRight=Math.cos(p.facingAngle)>0;
        if(!facingRight) g2.scale(-1,1);

        Color shirt=p.shirtColor, shorts=p.shortsColor;
        Color skin=new Color(210,170,120);
        Color skinDk=new Color(170,130,90);

        float legSwing=(p.state==PlayerState.RUN)?(float)(Math.sin(tick*0.32)*9):0;
        float armSwing=(p.state==PlayerState.RUN)?(float)(Math.sin(tick*0.32)*7):0;
        float kickLeg=(p.kickAnim>0)?(float)(-20+p.kickAnim*3):0;

        // Boots
        g2.setColor(new Color(20,12,5));
        g2.fillRoundRect(-5,17+(int)legSwing,7,6,2,2);
        g2.fillRoundRect(-1,17-(int)legSwing,7,6,2,2);
        // Socks
        g2.setColor(Color.WHITE);
        g2.fillRect(-5,10+(int)legSwing,5,9); g2.fillRect(-1,10-(int)legSwing,5,9);
        // Shorts
        g2.setColor(shorts); g2.fillRoundRect(-9,4,18,13,4,4);
        // Legs
        g2.setColor(skinDk);
        g2.fillRoundRect(-7,4,6,15+(int)legSwing,3,3);
        g2.fillRoundRect(1,4,6,15-(int)legSwing,3,3);
        if(p.kickAnim>0){ g2.setColor(shorts); g2.fillRoundRect(2,0,(int)kickLeg+4,12,3,3); }
        // Shirt
        GradientPaint sg=new GradientPaint(-9,-13,shirt.brighter(),9,9,shirt.darker());
        g2.setPaint(sg); g2.fillRoundRect(-11,-15,22,22,7,7);
        // Number
        g2.setFont(new Font("Arial",Font.BOLD,8)); g2.setColor(Color.WHITE);
        FontMetrics fm=g2.getFontMetrics();
        g2.drawString(p.number,-fm.stringWidth(p.number)/2,-3);
        // Arms
        g2.setColor(shirt); g2.fillRoundRect(-14,-13+(int)armSwing,6,7,2,2); g2.fillRoundRect(8,-13-(int)armSwing,6,7,2,2);
        g2.setColor(skin); g2.fillRoundRect(-14,-7+(int)armSwing,6,10,3,3); g2.fillRoundRect(8,-7-(int)armSwing,6,10,3,3);
        // Neck
        g2.setColor(skin); g2.fillRect(-3,-18,6,5);
        // Head
        g2.setColor(skin); g2.fillOval(-10,-32,20,20);
        g2.setColor(skinDk); g2.fillArc(-10,-32,20,10,0,180);
        // Hair
        g2.setColor(new Color(30,15,5)); g2.fillRoundRect(-9,-32,18,9,7,7);
        // Eyes
        g2.setColor(Color.WHITE); g2.fillOval(-6,-28,5,3); g2.fillOval(1,-28,5,3);
        g2.setColor(new Color(30,15,5)); g2.fillOval(-5,-27,2,2); g2.fillOval(2,-27,2,2);
        // Mouth
        g2.setColor(new Color(160,70,70));
        if(p.kickAnim>0) g2.drawOval(-3,-22,6,4); else g2.drawLine(-2,-21,2,-21);
        // GK gloves
        if(p.isGK){ g2.setColor(new Color(255,210,0)); g2.fillRoundRect(-18,-9,7,9,3,3); g2.fillRoundRect(11,-9,7,9,3,3); }
        // Controlled indicator
        if(isBlue && p==bluePlayers[controlledIdx]){
            g2.setColor(new Color(255,255,255,150)); g2.setStroke(new BasicStroke(1.8f));
            g2.drawOval(-PLAYER_R-3,-PLAYER_R-3,(PLAYER_R+3)*2,(PLAYER_R+3)*2);
            g2.setColor(new Color(255,255,80,210));
            int[] ax={0,-6,6}; int[] ay={-37,-44,-44}; g2.fillPolygon(ax,ay,3);
            g2.setFont(new Font("Arial",Font.BOLD,9)); g2.setColor(Color.WHITE);
            String n=p.name.split(" ")[0]; g2.drawString(n,-g2.getFontMetrics().stringWidth(n)/2,-40);
        }
        g2.dispose();
    }

    void drawBall(Graphics2D g){
        int bx=(int)(ball.x-BALL_R), by=(int)(ball.y-BALL_R), bd=(int)(BALL_R*2);
        g.setColor(new Color(0,0,0,45)); g.fillOval(bx+3,by+7,bd,bd/2);
        RadialGradientPaint rp=new RadialGradientPaint(
            new Point2D.Float(ball.x-BALL_R*0.3f,ball.y-BALL_R*0.3f),BALL_R,
            new float[]{0f,0.7f,1f}, new Color[]{Color.WHITE,new Color(220,220,220),new Color(150,150,150)});
        g.setPaint(rp); g.fillOval(bx,by,bd,bd);
        Graphics2D g2=(Graphics2D)g.create();
        g2.translate(ball.x,ball.y); g2.rotate(ball.spin); g2.setColor(new Color(25,25,25));
        for(int i=0;i<5;i++){
            double a=Math.PI*2*i/5-Math.PI/2;
            g2.fillOval((int)(Math.cos(a)*BALL_R*0.5f)-3,(int)(Math.sin(a)*BALL_R*0.5f)-3,6,6);
        }
        g2.fillOval(-3,-3,6,6); g2.dispose();
        g.setColor(new Color(255,255,255,170)); g.fillOval(bx+2,by+2,bd/3,bd/4);
    }

    void drawHUD(Graphics2D g){
        // Team badges
        drawTeamBadge(g, W/2-220, 8, blueTeamColors[0], blueTeamColors[1], blueTeamData[2], blueTeamData[0]);
        drawTeamBadge(g, W/2+100, 8, redTeamColors[0],  redTeamColors[1],  redTeamData[2],  redTeamData[0]);

        // Score
        g.setFont(new Font("Arial",Font.BOLD,32));
        g.setColor(Color.WHITE);
        String sc=scoreBlue+"  :  "+scoreRed;
        FontMetrics fm=g.getFontMetrics();
        g.drawString(sc,(W-fm.stringWidth(sc))/2,38);

        // Timer
        int secsLeft=(HALF_DURATION*FPS-matchTimer)/FPS;
        String timeStr=String.format("%02d:%02d",secsLeft/60,secsLeft%60);
        g.setFont(new Font("Arial",Font.BOLD,14));
        Color timeColor=secsLeft<=10?new Color(255,80,80):new Color(255,220,150);
        g.setColor(timeColor);
        fm=g.getFontMetrics(); g.drawString("H"+half+" "+timeStr,(W-fm.stringWidth("H"+half+" "+timeStr))/2,52);

        // Tournament label
        if (tournamentType != TournamentType.FRIENDLY) {
            String tn=tournamentLabel();
            g.setFont(new Font("Arial",Font.BOLD,10)); g.setColor(new Color(255,215,0));
            fm=g.getFontMetrics(); g.drawString(tn,(W-fm.stringWidth(tn))/2,66);
        }

        // Power bar
        if(spaceHeld&&shootPower>0){
            int bw=110, bx2=(int)bluePlayers[controlledIdx].x-55;
            int by2=(int)bluePlayers[controlledIdx].y+30;
            g.setColor(new Color(0,0,0,120)); g.fillRoundRect(bx2,by2,bw,9,4,4);
            Color pc=shootPower<0.5f?new Color(0,220,80):shootPower<0.8f?new Color(255,200,0):new Color(255,60,60);
            g.setColor(pc); g.fillRoundRect(bx2,by2,(int)(bw*shootPower),9,4,4);
            g.setColor(Color.WHITE); g.setStroke(new BasicStroke(1f)); g.drawRoundRect(bx2,by2,bw,9,4,4);
            g.setFont(new Font("Arial",Font.BOLD,9)); g.setColor(Color.WHITE); g.drawString("POWER",bx2,by2-2);
        }

        // Bottom stats
        g.setColor(new Color(0,0,0,110)); g.fillRect(0,H-24,W,24);
        g.setFont(new Font("Arial",Font.PLAIN,10)); g.setColor(new Color(160,180,220));
        int blPoss=possession[0]+possession[1]>0?(int)(100f*possession[0]/(possession[0]+possession[1])):50;
        String stats=String.format("Shots %d-%d  |  Possession %d%%-%d%%  |  TAB:Switch  SPACE:Kick  P:Pause  M:Mute",
                shots[0],shots[1],blPoss,100-blPoss);
        fm=g.getFontMetrics(); g.drawString(stats,(W-fm.stringWidth(stats))/2,H-7);

        // Mute indicator
        if (muteSound) {
            g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(new Color(255,100,100));
            g.drawString("🔇 MUTED",W-72,H-7);
        }
    }

    void drawTeamBadge(Graphics2D g,int x,int y,Color c1,Color c2,String abbr,String name){
        g.setColor(c1); g.fillRoundRect(x,y,90,44,8,8);
        g.setColor(c2); g.fillRoundRect(x,y+22,90,22,8,8);
        g.setColor(c1.darker()); g.setStroke(new BasicStroke(1.5f)); g.drawRoundRect(x,y,90,44,8,8);
        g.setStroke(new BasicStroke(1f));
        g.setFont(new Font("Arial",Font.BOLD,16)); g.setColor(Color.WHITE);
        FontMetrics fm=g.getFontMetrics(); g.drawString(abbr,x+(90-fm.stringWidth(abbr))/2,y+18);
        g.setFont(new Font("Arial",Font.PLAIN,7)); g.setColor(new Color(240,240,240));
        fm=g.getFontMetrics();
        String n=name.length()>14?name.substring(0,14):name;
        g.drawString(n,x+(90-fm.stringWidth(n))/2,y+38);
    }

    String tournamentLabel(){
        return switch(tournamentType){
            case AFCON            -> "🌍 AFCON TOURNAMENT";
            case PREMIER_LEAGUE   -> "🏴󠁧󠁢󠁥󠁮󠁧󠁿 PREMIER LEAGUE";
            case CHAMPIONS_LEAGUE -> "⭐ UEFA CHAMPIONS LEAGUE";
            default               -> "";
        };
    }

    // ─── GOAL ANIMATION ───────────────────────────────────────────────
    void drawGoalAnim(Graphics2D g){
        drawMatch(g);
        for(int[] c:confetti){
            g.setColor(new Color(c[5],c[6],c[7],(int)(200*(1f-(float)goalAnimTimer/160))));
            Graphics2D g2=(Graphics2D)g.create();
            g2.translate(c[0],c[1]); g2.rotate(Math.toRadians(c[4]));
            g2.fillRect(-c[2]/2,-c[3]/2,c[2],c[3]); g2.dispose();
        }
        float scale=1f+(float)Math.sin(tick*0.22)*0.06f;
        Graphics2D g2=(Graphics2D)g.create();
        g2.translate(W/2,H/2-40); g2.scale(scale,scale);
        for(int gl=8;gl>=0;gl--){
            Color gc=lastGoalBlue?new Color(0,120,255,10+gl*6):new Color(255,80,80,10+gl*6);
            g2.setColor(gc); g2.setFont(new Font("Arial",Font.BOLD,92+gl*2));
            FontMetrics fm2=g2.getFontMetrics(); g2.drawString("GOAL!",-fm2.stringWidth("GOAL!")/2+gl/2,gl/2);
        }
        g2.setFont(new Font("Arial",Font.BOLD,90));
        g2.setPaint(new GradientPaint(-200,-40,lastGoalBlue?new Color(0,200,255):new Color(255,200,0),
                200,40,lastGoalBlue?new Color(0,80,200):new Color(255,80,40)));
        FontMetrics fm3=g2.getFontMetrics(); g2.drawString("GOAL!",-fm3.stringWidth("GOAL!")/2,0);
        g2.dispose();
        String scorer=lastGoalBlue?blueTeamData[0]:redTeamData[0];
        g.setFont(new Font("Arial",Font.BOLD,22)); g.setColor(new Color(255,255,200));
        FontMetrics fm4=g.getFontMetrics(); g.drawString(scorer,(W-fm4.stringWidth(scorer))/2,H/2+30);
        g.setFont(new Font("Arial",Font.BOLD,34)); g.setColor(Color.WHITE);
        String sc=scoreBlue+" — "+scoreRed; fm4=g.getFontMetrics();
        g.drawString(sc,(W-fm4.stringWidth(sc))/2,H/2+72);
    }

    // ─── MENU ─────────────────────────────────────────────────────────
    void drawMenu(Graphics2D g){
        // Night stadium background
        GradientPaint bg=new GradientPaint(0,0,new Color(4,8,28),0,H,new Color(8,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);
        // Pitch silhouette
        g.setColor(new Color(30,100,30,60)); g.fillRect(60,180,W-120,340);
        g.setColor(new Color(255,255,255,20)); g.setStroke(new BasicStroke(2f));
        g.drawRect(60,180,W-120,340); g.drawLine(W/2,180,W/2,520);
        g.drawOval(W/2-90,320,180,180); g.setStroke(new BasicStroke(1f));

        // Animated crowd at top
        for(int i=0;i<140;i++){
            float bob=(float)(Math.sin(crowdBob[i])*3);
            g.setColor(crowdC[i]); g.fillOval((int)crowdX[i]-4,(int)(20+bob)-7,8,8);
            g.setColor(crowdC[i].darker()); g.fillRect((int)crowdX[i]-3,(int)(20+bob)+1,6,7);
        }

        // Title glow
        for(int gl=8;gl>=0;gl--){
            float a=(float)(0.5+0.5*Math.sin(tick*0.07));
            g.setColor(new Color(0,210,70,(int)(a*(10+gl*10))));
            g.setFont(new Font("Arial",Font.BOLD,86+gl*2));
            FontMetrics fm=g.getFontMetrics();
            g.drawString("KICK OFF!",(W-fm.stringWidth("KICK OFF!"))/2+gl/2,155+gl/2);
        }
        g.setFont(new Font("Arial",Font.BOLD,86));
        g.setPaint(new GradientPaint(0,70,new Color(0,255,90),0,155,new Color(0,160,40)));
        FontMetrics fm=g.getFontMetrics();
        g.drawString("KICK OFF!",(W-fm.stringWidth("KICK OFF!"))/2,155);

        g.setFont(new Font("Arial",Font.BOLD,18));
        g.setColor(new Color(160,230,160));
        String sub="⚽  ULTIMATE 5-a-Side Football  ⚽"; fm=g.getFontMetrics();
        g.drawString(sub,(W-fm.stringWidth(sub))/2,180);

        // Hi score
        g.setFont(new Font("Arial",Font.BOLD,13)); g.setColor(new Color(255,215,0));
        String hs="🏆 TOP GOALS: "+hiScore; fm=g.getFontMetrics();
        g.drawString(hs,(W-fm.stringWidth(hs))/2,202);

        // Menu options
        String[] opts={"🚀  KICK OFF  (Friendly)","🏆  TOURNAMENT MODE","⚙  DIFFICULTY: "+difficulty.name(),"📊  HIGH SCORES"};
        Color[] optC={new Color(0,230,80),new Color(255,215,0),new Color(100,180,255),new Color(180,180,255)};
        for(int i=0;i<opts.length;i++){
            boolean sel=(i==menuSelection);
            float pulse=(float)(0.7+0.3*Math.sin(tick*0.09+i));
            int mx=W/2, my=530+i*48;
            if(sel){
                g.setColor(new Color(optC[i].getRed(),optC[i].getGreen(),optC[i].getBlue(),(int)(pulse*50+20)));
                g.fillRoundRect(mx-190,my-30,380,42,12,12);
                g.setColor(new Color(optC[i].getRed(),optC[i].getGreen(),optC[i].getBlue(),(int)(pulse*200+30)));
                g.setStroke(new BasicStroke(2.2f)); g.drawRoundRect(mx-190,my-30,380,42,12,12); g.setStroke(new BasicStroke(1f));
            }
            g.setFont(new Font("Arial",Font.BOLD,sel?20:16));
            g.setColor(sel?optC[i]:new Color(100,130,100)); fm=g.getFontMetrics();
            g.drawString(opts[i],mx-fm.stringWidth(opts[i])/2,my);
        }

        // Credits
        g.setFont(new Font("Arial",Font.ITALIC,10)); g.setColor(new Color(50,90,50));
        String cr="Kick Off Ultimate — Advanced Java Programming | Dr. NTEZIRIZA NKERABAHIZI Josbert | UoK";
        fm=g.getFontMetrics(); g.drawString(cr,(W-fm.stringWidth(cr))/2,H-8);
    }

    // ─── TOURNAMENT SELECT ────────────────────────────────────────────
    void drawTournamentSelect(Graphics2D g){
        GradientPaint bg=new GradientPaint(0,0,new Color(4,8,28),0,H,new Color(8,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);

        g.setFont(new Font("Arial",Font.BOLD,38)); g.setColor(new Color(255,215,0));
        String t="SELECT TOURNAMENT"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,60);

        // 3 tournament options
        String[] tnames={"🌍 AFCON 2024","🏴󠁧󠁢󠁥󠁮󠁧󠁿 PREMIER LEAGUE","⭐ CHAMPIONS LEAGUE"};
        String[] tdesc={"African Cup of Nations — Rwanda included!","England's top flight — 20 clubs","Europe's elite — with FC Barcelona!"};
        Color[] tcol={new Color(0,180,60),new Color(100,0,200),new Color(0,80,220)};
        TournamentType[] types={TournamentType.AFCON,TournamentType.PREMIER_LEAGUE,TournamentType.CHAMPIONS_LEAGUE};

        for(int i=0;i<3;i++){
            boolean sel=(i==tournamentMenuSel);
            float pulse=(float)(0.7+0.3*Math.sin(tick*0.08+i));
            int tx=W/2-320+i*0, ty=120+i*160;
            int bw=640, bh=130;

            g.setColor(new Color(tcol[i].getRed(),tcol[i].getGreen(),tcol[i].getBlue(),sel?80:30));
            g.fillRoundRect(W/2-bw/2,ty,bw,bh,16,16);
            g.setColor(new Color(tcol[i].getRed(),tcol[i].getGreen(),tcol[i].getBlue(),sel?(int)(pulse*220+30):80));
            g.setStroke(new BasicStroke(sel?3f:1.5f)); g.drawRoundRect(W/2-bw/2,ty,bw,bh,16,16); g.setStroke(new BasicStroke(1f));

            g.setFont(new Font("Arial",Font.BOLD,sel?26:22)); g.setColor(sel?tcol[i]:new Color(150,170,150));
            fm=g.getFontMetrics(); g.drawString(tnames[i],W/2-fm.stringWidth(tnames[i])/2,ty+50);
            g.setFont(new Font("Arial",Font.ITALIC,14)); g.setColor(new Color(180,210,180));
            fm=g.getFontMetrics(); g.drawString(tdesc[i],W/2-fm.stringWidth(tdesc[i])/2,ty+82);

            if(sel){
                g.setFont(new Font("Arial",Font.BOLD,12)); g.setColor(new Color(0,255,100));
                String s="↑↓ Select  |  ENTER to pick your team"; fm=g.getFontMetrics();
                g.drawString(s,W/2-fm.stringWidth(s)/2,ty+112);
            }
        }

        g.setFont(new Font("Arial",Font.PLAIN,13)); g.setColor(new Color(120,160,120));
        String hint="UP/DOWN to choose  |  ENTER to confirm  |  ESC to go back";
        fm=g.getFontMetrics(); g.drawString(hint,(W-fm.stringWidth(hint))/2,H-24);
    }

    // ─── TEAM SELECT ──────────────────────────────────────────────────
    void drawTeamSelect(Graphics2D g){
        GradientPaint bg=new GradientPaint(0,0,new Color(4,8,28),0,H,new Color(8,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);

        String[][] teams=getTeamsForTournament(); Color[][] cols=getColorsForTournament();
        g.setFont(new Font("Arial",Font.BOLD,30)); g.setColor(new Color(255,215,0));
        String t="SELECT YOUR TEAM — "+tournamentLabel(); FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,45);

        int total=teams.length;
        int cols2=Math.min(4, total), rows=(int)Math.ceil((double)total/cols2);
        int cw=230, ch=88, padX=10, padY=10;
        int startX=(W-(cols2*cw+(cols2-1)*padX))/2;
        int startY=60;

        for(int i=0;i<total;i++){
            int col=i%cols2, row=i/cols2;
            int tx=startX+col*(cw+padX), ty=startY+row*(ch+padY);
            boolean sel=(i==blueTeamIdx);
            Color c1=cols[i][0], c2=cols[i][1];
            g.setColor(new Color(c1.getRed(),c1.getGreen(),c1.getBlue(),sel?90:35));
            g.fillRoundRect(tx,ty,cw,ch,10,10);
            g.setColor(c1); g.setStroke(new BasicStroke(sel?3f:1f));
            g.drawRoundRect(tx,ty,cw,ch,10,10); g.setStroke(new BasicStroke(1f));
            g.setColor(c1); g.fillRect(tx+8,ty+8,cw-16,18);
            g.setColor(c2); g.fillRect(tx+8,ty+26,cw-16,8);
            g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(Color.WHITE);
            fm=g.getFontMetrics(); g.drawString(teams[i][0],tx+(cw-fm.stringWidth(teams[i][0]))/2,ty+52);
            g.setFont(new Font("Arial",Font.ITALIC,9)); g.setColor(new Color(200,210,220));
            fm=g.getFontMetrics();
            g.drawString('"'+teams[i][1]+'"',tx+(cw-fm.stringWidth('"'+teams[i][1]+'"'))/2,ty+66);
            if(sel){
                g.setFont(new Font("Arial",Font.BOLD,9)); g.setColor(new Color(0,255,100));
                g.drawString("YOUR TEAM",tx+(cw-g.getFontMetrics().stringWidth("YOUR TEAM"))/2,ty+80);
            }
        }

        g.setFont(new Font("Arial",Font.PLAIN,13)); g.setColor(new Color(160,200,180));
        String hint="ARROW KEYS to browse  |  ENTER to confirm"; fm=g.getFontMetrics();
        g.drawString(hint,(W-fm.stringWidth(hint))/2,H-24);
        // Preview
        if(blueTeamIdx<teams.length){
            g.setFont(new Font("Arial",Font.BOLD,14)); g.setColor(cols[blueTeamIdx][0]);
            String p2="YOUR: "+teams[blueTeamIdx][0]; g.drawString(p2,20,H-48);
            redTeamIdx=(blueTeamIdx+1)%teams.length;
            g.setColor(cols[redTeamIdx][0]);
            g.drawString("OPP:  "+teams[redTeamIdx][0],20,H-30);
        }
    }

    // ─── TOURNAMENT BRACKET ───────────────────────────────────────────
    void drawBracket(Graphics2D g){
        GradientPaint bg=new GradientPaint(0,0,new Color(4,8,28),0,H,new Color(8,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);
        // UCL star background
        g.setColor(new Color(255,215,0,15));
        for(int i=0;i<12;i++){
            double a=Math.toRadians(i*30+tick*0.5);
            g.setStroke(new BasicStroke(3)); g.drawLine(W/2,H/2,(int)(W/2+Math.cos(a)*600),(int)(H/2+Math.sin(a)*600));
        }
        g.setStroke(new BasicStroke(1f));

        String tl=tournamentLabel();
        g.setFont(new Font("Arial",Font.BOLD,30)); g.setColor(new Color(255,215,0));
        FontMetrics fm=g.getFontMetrics(); g.drawString(tl,(W-fm.stringWidth(tl))/2,40);

        String[][] teams=getTeamsForTournament(); Color[][] cols=getColorsForTournament();

        // Draw bracket lines and team boxes
        // QF round
        int qfy=75, sfY=220, finY=350;
        int[] qfx={50,50,540,540};
        int[] sfx={295,295};
        String[] roundLabels={"QUARTER-FINALS","SEMI-FINALS","FINAL"};
        g.setFont(new Font("Arial",Font.BOLD,13)); g.setColor(new Color(255,215,0));
        g.drawString(roundLabels[0],80,qfy-10); g.drawString(roundLabels[0],570,qfy-10);
        g.drawString(roundLabels[1],310,sfY-10); g.drawString(roundLabels[2],430,finY-10);

        // QF matches: 0,1 on left; 2,3 on right
        for(int m=0;m<4;m++){
            int[] mu=bracketMatchup(m);
            int bx=(m<2)?80:550, by=qfy+(m%2)*130;
            drawBracketMatch(g,bx,by,mu[0],mu[1],bracketResults[m],teams,cols,m==currentBracketMatch&&!tournamentOver);
        }

        // Connector lines QF → SF
        g.setColor(new Color(255,215,0,80)); g.setStroke(new BasicStroke(1.5f));
        g.drawLine(280,qfy+40,310,sfY+40);  g.drawLine(280,qfy+170,310,sfY+40);
        g.drawLine(750,qfy+40,720,sfY+40);  g.drawLine(750,qfy+170,720,sfY+40);
        // SF matches
        if(bracketResults[0]>=0&&bracketResults[1]>=0){
            int[] mu4=bracketMatchup(4);
            drawBracketMatch(g,310,sfY,mu4[0],mu4[1],bracketResults[4],teams,cols,currentBracketMatch==4&&!tournamentOver);
        }
        if(bracketResults[2]>=0&&bracketResults[3]>=0){
            int[] mu5=bracketMatchup(5);
            drawBracketMatch(g,580,sfY,mu5[0],mu5[1],bracketResults[5],teams,cols,currentBracketMatch==5&&!tournamentOver);
        }
        // Final
        g.drawLine(510,sfY+40,530,finY+40); g.drawLine(680,sfY+40,660,finY+40);
        if(bracketResults[4]>=0&&bracketResults[5]>=0){
            int[] mu6=bracketMatchup(6);
            drawBracketMatch(g,530,finY,mu6[0],mu6[1],bracketResults[6],teams,cols,currentBracketMatch==6&&!tournamentOver);
        }
        g.setStroke(new BasicStroke(1f));

        if(tournamentOver&&!tournamentWinnerName.isEmpty()){
            // Champion banner
            g.setColor(new Color(0,0,0,180)); g.fillRoundRect(W/2-250,finY+120,500,80,16,16);
            g.setColor(new Color(255,215,0)); g.setFont(new Font("Arial",Font.BOLD,14));
            fm=g.getFontMetrics(); g.drawString("🏆 TOURNAMENT CHAMPION 🏆",(W-fm.stringWidth("🏆 TOURNAMENT CHAMPION 🏆"))/2,finY+148);
            g.setFont(new Font("Arial",Font.BOLD,22));
            fm=g.getFontMetrics(); g.drawString(tournamentWinnerName,(W-fm.stringWidth(tournamentWinnerName))/2,finY+176);
            g.setFont(new Font("Arial",Font.PLAIN,13)); g.setColor(new Color(0,220,100));
            String back="ENTER / ESC — Back to Menu"; fm=g.getFontMetrics();
            g.drawString(back,(W-fm.stringWidth(back))/2,H-20);
        } else {
            g.setFont(new Font("Arial",Font.BOLD,14)); g.setColor(new Color(0,220,100));
            String next="ENTER — Play Next Match"; fm=g.getFontMetrics();
            g.drawString(next,(W-fm.stringWidth(next))/2,H-20);
        }
    }

    void drawBracketMatch(Graphics2D g,int x,int y,int t1,int t2,int winner,
            String[][] teams,Color[][] cols,boolean isCurrent){
        int w=200,h=80;
        Color border=isCurrent?new Color(0,255,100):new Color(100,100,100);
        if(isCurrent){
            float pulse=(float)(0.6+0.4*Math.sin(tick*0.12));
            g.setColor(new Color(0,255,100,(int)(pulse*40))); g.fillRoundRect(x-3,y-3,w+6,h+6,12,12);
        }
        g.setColor(new Color(20,30,20,200)); g.fillRoundRect(x,y,w,h,10,10);
        g.setColor(border); g.setStroke(new BasicStroke(isCurrent?2f:1f));
        g.drawRoundRect(x,y,w,h,10,10); g.setStroke(new BasicStroke(1f));
        // Team 1 strip
        if(t1>=0&&t1<teams.length){
            Color c=cols[t1][0];
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),60)); g.fillRect(x+1,y+1,w-2,h/2-1);
            g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(winner==t1?new Color(255,215,0):Color.WHITE);
            g.drawString(teams[t1][2],x+6,y+22);
            g.setFont(new Font("Arial",Font.PLAIN,9)); g.setColor(new Color(200,210,200));
            g.drawString(teams[t1][0].length()>16?teams[t1][0].substring(0,16):teams[t1][0],x+30,y+22);
        }
        // Team 2 strip
        if(t2>=0&&t2<teams.length){
            Color c=cols[t2][0];
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),60)); g.fillRect(x+1,y+h/2,w-2,h/2-1);
            g.setFont(new Font("Arial",Font.BOLD,11)); g.setColor(winner==t2?new Color(255,215,0):Color.WHITE);
            g.drawString(teams[t2][2],x+6,y+h/2+22);
            g.setFont(new Font("Arial",Font.PLAIN,9)); g.setColor(new Color(200,210,200));
            g.drawString(teams[t2][0].length()>16?teams[t2][0].substring(0,16):teams[t2][0],x+30,y+h/2+22);
        }
        g.drawLine(x,y+h/2,x+w,y+h/2);
    }

    // ─── HALF TIME ────────────────────────────────────────────────────
    void drawHalfTime(Graphics2D g){
        g.setColor(new Color(0,0,0,165)); g.fillRect(0,0,W,H);
        g.setFont(new Font("Arial",Font.BOLD,52)); g.setColor(new Color(0,230,100));
        String t="HALF TIME"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,H/2-50);
        g.setFont(new Font("Arial",Font.BOLD,30)); g.setColor(Color.WHITE);
        String sc=blueTeamData[2]+"  "+scoreBlue+"  —  "+scoreRed+"  "+redTeamData[2];
        fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,H/2+8);
        g.setFont(new Font("Arial",Font.ITALIC,15)); g.setColor(new Color(180,200,180));
        String r="Sides swapped for 2nd half — 2nd half starting soon..."; fm=g.getFontMetrics();
        g.drawString(r,(W-fm.stringWidth(r))/2,H/2+48);
    }

    // ─── PAUSE ────────────────────────────────────────────────────────
    void drawPause(Graphics2D g){
        g.setColor(new Color(0,0,0,165)); g.fillRect(0,0,W,H);
        g.setFont(new Font("Arial",Font.BOLD,56)); g.setColor(new Color(0,230,80));
        String p2="⏸ PAUSED"; FontMetrics fm=g.getFontMetrics();
        g.drawString(p2,(W-fm.stringWidth(p2))/2,H/2-18);
        g.setFont(new Font("Arial",Font.PLAIN,15)); g.setColor(new Color(180,220,180));
        String r="P / ESC — Resume  |  M — Toggle Sound"; fm=g.getFontMetrics();
        g.drawString(r,(W-fm.stringWidth(r))/2,H/2+22);
    }

    // ─── FULL TIME ────────────────────────────────────────────────────
    void drawFullTime(Graphics2D g){
        GradientPaint bg=new GradientPaint(0,0,new Color(4,10,4),0,H,new Color(0,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);
        String winner; Color winC;
        if(scoreBlue>scoreRed){winner="🏆 YOU WIN!"; winC=new Color(0,210,255);}
        else if(scoreRed>scoreBlue){winner="YOU LOSE"; winC=new Color(255,60,60);}
        else{winner="DRAW!"; winC=new Color(255,200,0);}
        // Rays
        for(int i=0;i<18;i++){
            double a=Math.toRadians(i*20+tick*0.35);
            g.setColor(new Color(winC.getRed(),winC.getGreen(),winC.getBlue(),10));
            g.setStroke(new BasicStroke(9)); g.drawLine(W/2,H/2-60,(int)(W/2+Math.cos(a)*800),(int)(H/2-60+Math.sin(a)*800));
        }
        g.setStroke(new BasicStroke(1f));
        g.setFont(new Font("Arial",Font.BOLD,24)); g.setColor(new Color(255,210,100));
        String ft="⚽ FULL TIME ⚽"; FontMetrics fm=g.getFontMetrics(); g.drawString(ft,(W-fm.stringWidth(ft))/2,H/2-140);
        g.setFont(new Font("Arial",Font.BOLD,64)); g.setColor(winC);
        fm=g.getFontMetrics(); g.drawString(winner,(W-fm.stringWidth(winner))/2,H/2-70);
        g.setFont(new Font("Arial",Font.BOLD,36)); g.setColor(Color.WHITE);
        String sc=blueTeamData[0]+"   "+scoreBlue+" — "+scoreRed+"   "+redTeamData[0];
        fm=g.getFontMetrics(); g.drawString(sc,(W-fm.stringWidth(sc))/2,H/2-18);
        // Stats box
        g.setColor(new Color(0,0,0,120)); g.fillRoundRect(W/2-300,H/2+10,600,110,14,14);
        g.setFont(new Font("Arial",Font.BOLD,14)); g.setColor(new Color(0,220,100));
        g.drawString("MATCH STATS",W/2-45,H/2+34);
        g.setFont(new Font("Arial",Font.PLAIN,13)); g.setColor(new Color(180,230,180));
        int blPoss=possession[0]+possession[1]>0?(int)(100f*possession[0]/(possession[0]+possession[1])):50;
        String[] stats={"Shots: "+shots[0]+" — "+shots[1],"Possession: "+blPoss+"% — "+(100-blPoss)+"%","On Target: "+shotsOnTarget[0]+" — "+shotsOnTarget[1]};
        for(int i=0;i<3;i++){fm=g.getFontMetrics();g.drawString(stats[i],(W-fm.stringWidth(stats[i]))/2,H/2+56+i*18);}
        float p=(float)(0.6+0.4*Math.sin(tick*0.09));
        drawMenuBtn(g,"ENTER — Play Again",W/2,H/2+148,new Color(0,210,80),15,p);
        if(playerInTournament&&!tournamentOver){
            drawMenuBtn(g,"B — Continue Tournament",W/2,H/2+185,new Color(255,215,0),13,0.8f);
        }
        drawMenuBtn(g,"ESC — Main Menu",W/2,H/2+222,new Color(160,160,200),13,0.8f);
    }

    void drawMenuBtn(Graphics2D g,String t,int cx,int cy,Color c,int fs,float glow){
        g.setFont(new Font("Arial",Font.BOLD,fs));
        FontMetrics fm=g.getFontMetrics(); int tw=fm.stringWidth(t);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*50+10)));
        g.fillRoundRect(cx-tw/2-14,cy-fs,tw+28,fs+14,10,10);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(glow*200+30)));
        g.setStroke(new BasicStroke(1.5f)); g.drawRoundRect(cx-tw/2-14,cy-fs,tw+28,fs+14,10,10); g.setStroke(new BasicStroke(1f));
        g.setColor(c); g.drawString(t,cx-tw/2,cy);
    }

    // ─── HIGH SCORES ──────────────────────────────────────────────────
    void drawScores(Graphics2D g){
        GradientPaint bg=new GradientPaint(0,0,new Color(4,8,28),0,H,new Color(8,30,8));
        g.setPaint(bg); g.fillRect(0,0,W,H);
        g.setFont(new Font("Arial",Font.BOLD,42));
        g.setPaint(new GradientPaint(0,80,new Color(255,215,0),0,130,new Color(255,140,0)));
        String t="🏆 TOP SCORES 🏆"; FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(W-fm.stringWidth(t))/2,95);
        g.setColor(new Color(0,0,0,140)); g.fillRoundRect(W/2-220,120,440,340,14,14);
        g.setColor(new Color(0,120,60,80)); g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(W/2-220,120,440,340,14,14); g.setStroke(new BasicStroke(1f));
        List<int[]> scores=loadAllScores();
        for(int i=0;i<Math.min(scores.size(),8);i++){
            Color c=i==0?new Color(255,215,0):i==1?new Color(192,192,192):i==2?new Color(205,127,50):new Color(160,200,160);
            String medal=i==0?"🥇":i==1?"🥈":i==2?"🥉":"  ";
            g.setFont(new Font("Arial",Font.BOLD,18)); g.setColor(c);
            String line=medal+" "+(i+1)+".   "+scores.get(i)[0]+" goals";
            fm=g.getFontMetrics(); g.drawString(line,(W-fm.stringWidth(line))/2,168+i*36);
        }
        if(scores.isEmpty()){
            g.setFont(new Font("Arial",Font.ITALIC,14)); g.setColor(new Color(120,160,120));
            String n="No scores yet — play your first match!";
            fm=g.getFontMetrics(); g.drawString(n,(W-fm.stringWidth(n))/2,290);
        }
        drawMenuBtn(g,"ESC / ENTER — Back to Menu",W/2,H-50,new Color(0,210,80),14,0.9f);
    }

    // ══════════════════════════════════════════════════════════════════
    //  INPUT
    // ══════════════════════════════════════════════════════════════════
    @Override public void keyPressed(KeyEvent e){
        int k=e.getKeyCode();
        if(k==KeyEvent.VK_UP)    kUp   =true;
        if(k==KeyEvent.VK_DOWN)  kDown =true;
        if(k==KeyEvent.VK_LEFT)  kLeft =true;
        if(k==KeyEvent.VK_RIGHT) kRight=true;
        if(k==KeyEvent.VK_SPACE) kSpace=true;
        if(k==KeyEvent.VK_TAB)   kTab  =true;

        switch(screen){
            case MENU -> {
                if(k==KeyEvent.VK_UP)   menuSelection=(menuSelection+3)%4;
                if(k==KeyEvent.VK_DOWN) menuSelection=(menuSelection+1)%4;
                if(k==KeyEvent.VK_ENTER){
                    switch(menuSelection){
                        case 0 -> { // Friendly
                            tournamentType=TournamentType.FRIENDLY;
                            screen=Screen.TEAM_SELECT; blueTeamIdx=0;
                            String[][] teams=getTeamsForTournament(); Color[][] cols=getColorsForTournament();
                            blueTeamData=teams[0]; blueTeamColors=cols[0];
                            redTeamData=teams[1]; redTeamColors=cols[1]; redTeamIdx=1;
                        }
                        case 1 -> screen=Screen.TOURNAMENT_SELECT;
                        case 2 -> difficulty=Difficulty.values()[(difficulty.ordinal()+1)%3];
                        case 3 -> screen=Screen.SCORES;
                    }
                    sound.play("menubeep");
                }
            }
            case TOURNAMENT_SELECT -> {
                if(k==KeyEvent.VK_UP)    tournamentMenuSel=(tournamentMenuSel+2)%3;
                if(k==KeyEvent.VK_DOWN)  tournamentMenuSel=(tournamentMenuSel+1)%3;
                if(k==KeyEvent.VK_ENTER){
                    tournamentType=switch(tournamentMenuSel){
                        case 0->TournamentType.AFCON;
                        case 1->TournamentType.PREMIER_LEAGUE;
                        case 2->TournamentType.CHAMPIONS_LEAGUE;
                        default->TournamentType.AFCON;
                    };
                    blueTeamIdx=0;
                    String[][] teams=getTeamsForTournament(); Color[][] cols=getColorsForTournament();
                    blueTeamData=teams[0]; blueTeamColors=cols[0];
                    redTeamData=teams[1]; redTeamColors=cols[1]; redTeamIdx=1;
                    screen=Screen.TEAM_SELECT;
                    sound.play("menubeep");
                }
                if(k==KeyEvent.VK_ESCAPE) screen=Screen.MENU;
            }
            case TEAM_SELECT -> {
                String[][] teams=getTeamsForTournament(); Color[][] cols=getColorsForTournament();
                if(k==KeyEvent.VK_LEFT)  blueTeamIdx=(blueTeamIdx+teams.length-1)%teams.length;
                if(k==KeyEvent.VK_RIGHT) blueTeamIdx=(blueTeamIdx+1)%teams.length;
                if(k==KeyEvent.VK_UP)    blueTeamIdx=(blueTeamIdx+teams.length-4)%teams.length;
                if(k==KeyEvent.VK_DOWN)  blueTeamIdx=(blueTeamIdx+4)%teams.length;
                redTeamIdx=(blueTeamIdx+1)%teams.length;
                blueTeamData=teams[blueTeamIdx]; blueTeamColors=cols[blueTeamIdx];
                redTeamData=teams[redTeamIdx]; redTeamColors=cols[redTeamIdx];
                if(k==KeyEvent.VK_ENTER){
                    playerInTournament=false;
                    if(tournamentType!=TournamentType.FRIENDLY){ initTournament(); }
                    else { startMatch(); }
                    sound.play("menubeep");
                }
                if(k==KeyEvent.VK_ESCAPE) screen=Screen.MENU;
            }
            case BRACKET -> {
                if((k==KeyEvent.VK_ENTER)&&!tournamentOver) playNextTournamentMatch();
                if((k==KeyEvent.VK_ENTER||k==KeyEvent.VK_ESCAPE)&&tournamentOver) screen=Screen.MENU;
                if(k==KeyEvent.VK_ESCAPE&&!tournamentOver) screen=Screen.MENU;
            }
            case PLAYING -> {
                if(k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE) screen=Screen.PAUSED;
                if(k==KeyEvent.VK_M) muteSound=!muteSound;
            }
            case PAUSED -> {
                if(k==KeyEvent.VK_P||k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER) screen=Screen.PLAYING;
                if(k==KeyEvent.VK_M) muteSound=!muteSound;
            }
            case FULL_TIME -> {
                if(k==KeyEvent.VK_ENTER){
                    if(playerInTournament&&!tournamentOver){
                        // Continue tournament bracket
                        if(currentBracketMatch>=7){ tournamentOver=true; resolveChampion(); screen=Screen.BRACKET; }
                        else { screen=Screen.BRACKET; }
                    } else {
                        screen=Screen.TEAM_SELECT;
                    }
                }
                if(k==KeyEvent.VK_B&&playerInTournament&&!tournamentOver){
                    screen=Screen.BRACKET;
                }
                if(k==KeyEvent.VK_ESCAPE) screen=Screen.MENU;
            }
            case SCORES -> {
                if(k==KeyEvent.VK_ESCAPE||k==KeyEvent.VK_ENTER) screen=Screen.MENU;
            }
        }
    }
    @Override public void keyReleased(KeyEvent e){
        int k=e.getKeyCode();
        if(k==KeyEvent.VK_UP)    kUp   =false;
        if(k==KeyEvent.VK_DOWN)  kDown =false;
        if(k==KeyEvent.VK_LEFT)  kLeft =false;
        if(k==KeyEvent.VK_RIGHT) kRight=false;
        if(k==KeyEvent.VK_SPACE) kSpace=false;
    }
    @Override public void keyTyped(KeyEvent e){}

    // ══════════════════════════════════════════════════════════════════
    //  FILE I/O
    // ══════════════════════════════════════════════════════════════════
    void loadHiScore(){ List<int[]> s=loadAllScores(); if(!s.isEmpty()) hiScore=s.get(0)[0]; }
    List<int[]> loadAllScores(){
        List<int[]> list=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(SCORE_FILE))){
            String ln; while((ln=br.readLine())!=null){ try{list.add(new int[]{Integer.parseInt(ln.trim())});}catch(Exception ignored){} }
        }catch(IOException ignored){}
        list.sort((a,b)->b[0]-a[0]); return list;
    }
    void saveScore(int goals){
        List<int[]> all=loadAllScores(); all.add(new int[]{goals}); all.sort((a,b)->b[0]-a[0]);
        try(PrintWriter pw=new PrintWriter(new FileWriter(SCORE_FILE))){
            for(int i=0;i<Math.min(all.size(),10);i++) pw.println(all.get(i)[0]);
        }catch(IOException ignored){}
    }
    void saveHiScore(){ saveScore(hiScore); }

    // ══════════════════════════════════════════════════════════════════
    //  UTILITY
    // ══════════════════════════════════════════════════════════════════
    static float clamp(float v,float lo,float hi){ return Math.max(lo,Math.min(hi,v)); }

    // ══════════════════════════════════════════════════════════════════
    //  INNER CLASSES
    // ══════════════════════════════════════════════════════════════════
    class Ball {
        float x,y,vx,vy,spin;
        Ball(float x,float y,float vx,float vy){ this.x=x;this.y=y;this.vx=vx;this.vy=vy; }
        void update(){
            x+=vx; y+=vy; vx*=BALL_FRICTION; vy*=BALL_FRICTION;
            spin+=(vx+vy)*0.04f;
            if(Math.abs(vx)<0.05f) vx=0; if(Math.abs(vy)<0.05f) vy=0;
        }
    }

    class Player {
        float x,y,homeX,homeY;
        Color shirtColor,shortsColor;
        String number,name;
        boolean isBlue,isGK;
        PlayerState state=PlayerState.IDLE;
        float facingAngle=0;
        int kickAnim=0;
        Player(float x,float y,Color shirt,Color shorts,String num,String name,boolean isBlue,boolean isGK){
            this.x=x;this.y=y;this.shirtColor=shirt;this.shortsColor=shorts;
            this.number=num;this.name=name;this.isBlue=isBlue;this.isGK=isGK;
        }
        void update(){
            if(kickAnim>0) kickAnim--;
            x=clamp(x,PX+PLAYER_R,PX+PW-PLAYER_R); y=clamp(y,PY+PLAYER_R,PY+PH-PLAYER_R);
        }
    }

    class Particle {
        float x,y,vx,vy; Color c; int life,maxLife; boolean dead;
        Particle(int x,int y,Color c,float vx,float vy,int life){this.x=x;this.y=y;this.c=c;this.vx=vx;this.vy=vy;this.life=this.maxLife=life;}
        void update(){x+=vx;y+=vy;vx*=0.88f;vy*=0.88f;vy+=0.1f;if(--life<=0)dead=true;}
        void draw(Graphics2D g){float a=(float)life/maxLife;g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(a*220)));g.fillOval((int)(x-3),(int)(y-3),6,6);}
    }

    class FloatText {
        String text; float x,y; Color c; float life=1f; boolean dead;
        FloatText(String t,int x,int y,Color c){text=t;this.x=x;this.y=y;this.c=c;}
        void update(){y-=1.2f;life-=0.015f;if(life<=0)dead=true;}
        void draw(Graphics2D g){
            g.setFont(new Font("Arial",Font.BOLD,18));
            g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(life*240)));
            FontMetrics fm=g.getFontMetrics(); g.drawString(text,(int)x-fm.stringWidth(text)/2,(int)y);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  SOUND ENGINE
    // ══════════════════════════════════════════════════════════════════
    class SFX {
        static final int SR=44100;
        void play(String name){
            if(muteSound) return;
            byte[] pcm=gen(name); if(pcm==null) return;
            Thread t=new Thread(()->{
                try{
                    AudioFormat fmt=new AudioFormat(SR,16,1,true,false);
                    DataLine.Info info=new DataLine.Info(SourceDataLine.class,fmt);
                    if(!AudioSystem.isLineSupported(info)) return;
                    try(SourceDataLine line=(SourceDataLine)AudioSystem.getLine(info)){
                        line.open(fmt,2048);line.start();line.write(pcm,0,pcm.length);line.drain();
                    }
                }catch(Exception ignored){}
            });
            t.setDaemon(true); t.start();
        }
        byte[] gen(String name){
            return switch(name){
                case "kick"     -> synth(0.14,t->(noise()*0.7+sine(t,120-t*60)*0.5)*env(t,0.005,0.03,0.3,0.06)*0.75);
                case "bounce"   -> synth(0.10,t->(sine(t,400-t*200)+noise()*0.2)*env(t,0.005,0.02,0.25,0.05)*0.55);
                case "goal"     -> synth(1.80,t->(crowdRoar(t)+hornBlast(t)*0.6)*env(t,0.02,0.15,0.7,0.1)*0.80);
                case "whistle"  -> synth(0.60,t->(sine(t,2200+sine(t*3,80)*30)+sine(t,2400)*0.3)*env(t,0.01,0.05,0.65,0.1)*0.55);
                case "switch"   -> synth(0.10,t->sine(t,660)*env(t,0.01,0.03,0.35,0.04)*0.35);
                case "menubeep" -> synth(0.12,t->(sine(t,440)+sine(t,880)*0.3)*env(t,0.01,0.03,0.4,0.04)*0.38);
                default         -> null;
            };
        }
        double crowdRoar(double t){return noise()*(0.3+t*0.7)*Math.sin(t*Math.PI);}
        double hornBlast(double t){
            if(t<0.3) return sine(t,440+t*200);
            if(t<0.6) return sine(t,660)+sine(t,880)*0.4;
            return sine(t,880)*Math.max(0,1-t);
        }
        static double sine(double t,double f){return Math.sin(2*Math.PI*f*t);}
        static final Random NR=new Random();
        static double noise(){return NR.nextDouble()*2-1;}
        static double env(double t,double a,double d,double s,double r){
            if(t<a) return t/a;
            if(t<a+d) return 1-(1-0.7)*((t-a)/d);
            if(t<1.0-r) return 0.7;
            return 0.7*(1-(t-(1.0-r))/r);
        }
        byte[] synth(double dur,DoubleUnaryOperator fn){
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
    //  MAIN
    // ══════════════════════════════════════════════════════════════════
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            JFrame f=new JFrame("Kick Off! Ultimate — 5-a-Side Football");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            FootballGame game=new FootballGame();
            f.add(game); f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            game.requestFocusInWindow();
        });
    }
}