package fg331.free.bg.MainBody;

import fg331.free.bg.Display.Display;
import fg331.free.bg.GFX.Assets;
import fg331.free.bg.State.Menu;
import fg331.free.bg.State.*;
import fg331.free.bg.Util.Util;

import java.awt.*;
import java.awt.image.BufferStrategy;

@SuppressWarnings("SpellCheckingInspection")
public class Game implements Runnable {

    public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final int faceBase = -75; // otmestwane ot nulewo niwo za igralnoto pole za izobrazqwane na butona


    public int BOMBSAMOUNT; // broq na bombi
    public int mapWidth = 30, mapHeight = 20; // 6iro4ina i wiso4ina na poleto za igra kato broi blok4eta
    public int BlockSize; // golemina pixeli za edno blok4e
    public int remainingBombs; // broi ostawisti bombi
    public int sekundi = 0; // taimer sekundi
    public int minuti = 0; // taimer minuti
    private int sekundomerCounter = 0; // osiguritel na prawilno otbroqwane
    private int fps = 0; // broi update za sekunda na koda
    public int smilingGoodPerson = 0; // kod za izobrazqwane na butona
    private boolean isSwitching = false;

    public Display display; // display obekt
    public Menu menu;
    public GameState gameState;
    public Settings settings;
    public Scores scores;

    private Thread thread;
    public Font drawFont = new Font("Arial", Font.BOLD, 45); // fon za pisaneto na ostawa6ti bombo i pri pe4elene
    public Font drawFont1 = new Font("Arial", Font.BOLD, 20); // fon za pisaneto na ostawa6ti bombo i pri pe4elene
    public boolean gameRunning = false; // dali igrata warwi
    public boolean timer = false; // dali taimera warwi
    public boolean won = false; // dali igrata e spe4elena
    public int[][] checkedPoints; // prowereni to4ki za izkarwaneto na swobodni mesta

    public Graphics g;
    private boolean running = false;
    public int topOffset = 120;

    public int[][] map;
    public int[][] actualMap;

    public String title;

    Game(String title) {
        this.title = title;
    }

    private void init() {
        Assets.init();

        if (mapWidth * 30 > screenSize.getWidth()) {
            BlockSize = (int) screenSize.getWidth() / mapWidth;
        } else if (mapHeight * 30 + 270 > screenSize.getHeight()) {
            BlockSize = (int) ((screenSize.getHeight() - 225) / mapHeight);
        } else {
            BlockSize = 30;
        }

        display = new Display(title, mapWidth * BlockSize, topOffset + mapHeight * BlockSize);

        gameState = new GameState(this);
        menu = new Menu(this);
        settings = new Settings(this);
        scores = new Scores(this);

        menu.mouseSetter();
        State.setCurrentState(menu);

        for (int i = 0; i < 6; i++) {
            render();
        }
    }

    public void BlankBlockCleaner(int coordX, int coordY) {
        for (int i = coordX - 1; i < coordX + 2; i++) {
            for (int j = coordY - 1; j < coordY + 2; j++) {
                if (i >= 0 && j >= 0 && i < mapWidth && j < mapHeight) {
                    if (map[i][j] == 11) {
                        remainingBombs++;
                    }
                    if (actualMap[i][j] == 0 && checkedPoints[i][j] == 0) {
                        map[i][j] = 0;
                        checkedPoints[i][j] = 1;
                        BlankBlockCleaner(i, j);
                    } else {
                        map[i][j] = actualMap[i][j];
                    }
                }
            }
        }
    }

    private void tick() { // sekundomer
        if (timer) {
            sekundomerCounter++;
            if (sekundomerCounter == fps) {
                sekundi++;
                sekundomerCounter = 0;
            }
            if (sekundi == 60) {
                sekundi = 0;
                minuti++;
            }
            if (minuti == 100) {
                minuti = 0;
            }
        }
    }

    public void render() { // risuwa4
        BufferStrategy bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //clear
        g.clearRect(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        //end clear

        if (State.getCurrentState() != null) {
            State.getCurrentState().render(g);
        }
        //ENDING
        bs.show();
        g.dispose();
    }

    public void exiter(int x, int y, boolean fail) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (map[i][j] != 0 && map[i][j] != 11) { // ako ne e flag4e
                    map[i][j] = actualMap[i][j]; // update na widimata karta
                }

            }
        }
        if (fail) {
            map[x][y] = 15; // wrashta na originalnata aktiwirana bomba
        }
        timer = false;
        gameRunning = false;

        if (!fail)
//            Util.loadHighscore(Assets.path + "\\" + "HighScores.txt", BOMBSAMOUNT, mapWidth, mapHeight, sekundi, minuti);
            Util.loadHighscore("res/txt/HighScores.txt", BOMBSAMOUNT, mapWidth, mapHeight, sekundi, minuti);
    }

    @Override
    public void run() {
        init();
        fps = 10;

        while (running) {
                if (!isSwitching) {
                    render();
                }
                if (isSwitching)
                    isSwitching = false;

                tick();
            try {
                Thread.sleep(1000/fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    public synchronized void menuUpdater() {
        isSwitching = true;
        mapWidth = 30;
        mapHeight = 20;
        display.getFrame().setVisible(false);
        display = new Display(title, mapWidth * BlockSize, topOffset + mapHeight * BlockSize);
    }

    synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
