package MineSweeperGame.MainBody;

import MineSweeperGame.Display.Display;
import MineSweeperGame.GFX.Assets;
import MineSweeperGame.State.Menu;
import MineSweeperGame.State.*;
import MineSweeperGame.Util.Util;

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
    public int smilingFace = 0; // kod za izobrazqwane na butona
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

        //TODO wazmojno e da prowerqwam za samite bombi i ot tqh da dobawqm po edno na 8-te okolo neq
    }

    public void BlankBlockCleaner(int coordY, int coordX) {
        for (int i = coordY - 1; i < coordY + 2; i++) {
            for (int j = coordX - 1; j < coordX + 2; j++) {
                if (i >= 0 && j >= 0 && i < mapHeight && j < mapWidth) {
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

    private void render() { // risuwa4
        BufferStrategy bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //clear
        g.clearRect(0, 0, mapWidth * BlockSize, mapHeight * BlockSize + topOffset);
        //end clear

        if (State.getCurrentState() != null) {
            State.getCurrentState().render(g);
        }
        //ENDING
        bs.show();
        g.dispose();
    }


    public void exiter(int y, int x, boolean fail) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (map[j][i] != 0 && map[j][i] != 11) { // ako ne e flag4e
                    map[j][i] = actualMap[j][i]; // update na widimata karta
                }

            }
        }
        if (fail) {
            map[y][x] = 15; // wrashta na originalnata aktiwirana bomba
        }
        timer = false;
        gameRunning = false;

        if (!fail)
            Util.loadHighscore("res/txt/HighScores.txt", BOMBSAMOUNT, mapWidth, mapHeight, sekundi, minuti);
    }

    @Override
    public void run() {
        init();

        fps = 10;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;
            if (delta >= 1) {
                if (!isSwitching) {
                    render();
                }
                if (isSwitching)
                    isSwitching = false;

                tick();
                delta--;
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
