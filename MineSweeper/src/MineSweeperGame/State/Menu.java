package MineSweeperGame.State;

import MineSweeperGame.Display.Display;
import MineSweeperGame.MainBody.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Menu extends State {
    Game game;
    State gameState;

    public Menu(Game game) {
        this.game = game;
        gameState = game.gameState;

        seButtonX = (int) (game.mapWidth * game.BlockSize / 3);
        seButtonY = game.mapWidth * game.BlockSize / 2;

        stButtonX = game.mapWidth * game.BlockSize / 8;
        stButtonY = game.mapWidth * game.BlockSize / 2;

        scButtonX = (int) (game.mapWidth * game.BlockSize / 1.8) + 7;
        scButtonY = game.mapWidth * game.BlockSize / 2;

        begButtonX = game.mapWidth * game.BlockSize / 8;
        begButtonY = game.mapHeight * game.BlockSize / 12;

        intButtonX = game.mapWidth * game.BlockSize / 8;
        intButtonY = game.mapHeight * game.BlockSize / 12 + height + 20;

        harButtonX = game.mapWidth * game.BlockSize / 8;
        harButtonY = game.mapHeight * game.BlockSize / 12 + (height + 20) * 2;

        insButtonX = game.mapWidth * game.BlockSize / 8;
        insButtonY = game.mapHeight * game.BlockSize / 12 + (height + 20) * 3;
    }

    private int seButtonX, seButtonY, stButtonX, stButtonY, scButtonX, scButtonY;
    private int begButtonX, begButtonY;
    private int intButtonX, intButtonY;
    private int harButtonX, harButtonY;
    private int insButtonX, insButtonY;
    private int width = 180, height = 60;

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getX() > stButtonX && e.getX() < stButtonX + width && e.getY() > stButtonY && e.getY() < stButtonY + height) {
                game.settings.updater();
                if (game.map != null) {
                    mouseRemover();
                    game.gameState.mouseSetter();
                    State.setCurrentState(game.gameState);
                }
            } else if (e.getX() > seButtonX && e.getX() < seButtonX + width + 20 && e.getY() > seButtonY && e.getY() < seButtonY + height) {
                mouseRemover();
                game.settings.mouseSetter();
                State.setCurrentState(game.settings);//TODO tuk dowar6i nestata trite
            } else if (e.getX() > scButtonX && e.getX() < scButtonX + width && e.getY() > scButtonY && e.getY() < scButtonY + height) {
                mouseRemover();
                game.scores.screenSetter();
                State.setCurrentState(game.scores);//TODO tuk dowar6i nestata trite
            } else if (e.getX() > begButtonX && e.getX() < begButtonX + width * 2 && e.getY() > begButtonY && e.getY() < begButtonY + height) {
                game.BOMBSAMOUNT = 50;
                game.mapWidth = 20;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > intButtonX && e.getX() < intButtonX + width * 2 && e.getY() > intButtonY && e.getY() < intButtonY + height) {
                game.BOMBSAMOUNT = 60;
                game.mapWidth = 25;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > harButtonX && e.getX() < harButtonX + width * 2 && e.getY() > harButtonY && e.getY() < harButtonY + height) {
                game.BOMBSAMOUNT = 100;
                game.mapWidth = 30;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > insButtonX && e.getX() < insButtonX + width * 2 && e.getY() > insButtonY && e.getY() < insButtonY + height) {
                game.BOMBSAMOUNT = 150;
                game.mapWidth = 40;
                game.mapHeight = 20;
                presetUpdater();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

        g.setColor(Color.GRAY);
        g.fillRect(stButtonX, stButtonY, width, height);
        g.fillRect(seButtonX, seButtonY, width + 20, height);
        g.fillRect(scButtonX, scButtonY, width, height);

        g.fillRect(begButtonX, begButtonY, width * 2, height);
        g.fillRect(intButtonX, intButtonY, width * 2, height);
        g.fillRect(harButtonX, harButtonY, width * 2, height);
        g.fillRect(insButtonX, insButtonY, width * 2, height);

        g.setColor(Color.BLACK);
        g.setFont(game.drawFont);

        g.drawString("Start", stButtonX + width / 8, stButtonY + height * 2 / 3);
        g.drawString("Settings", seButtonX + width / 8, seButtonY + height * 2 / 3);
        g.drawString("Scores", scButtonX + width / 8, scButtonY + height * 2 / 3);
        g.drawString("Easy Preset", begButtonX + width / 8, begButtonY + height * 2 / 3);
        g.drawString("Inter Preset", intButtonX + width / 8, intButtonY + height * 2 / 3);
        g.drawString("Hard Preset", harButtonX + width / 8, harButtonY + height * 2 / 3);
        g.drawString("Insane Preset", insButtonX + width / 8, insButtonY + height * 2 / 3);
    }

    private void presetUpdater() {
        game.map = new int[game.mapHeight][game.mapWidth];
        game.actualMap = new int[game.mapHeight][game.mapWidth];
        game.checkedPoints = new int[game.mapHeight][game.mapWidth];
        game.remainingBombs = game.BOMBSAMOUNT;
        game.gameState.meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize*3 -60;
        game.gameRunning = true;
        game.sekundi = 0;
        game.minuti = 0;

        for (int i = 0; i < game.mapHeight; i++) {
            for (int j = 0; j < game.mapWidth; j++) {
                game.map[i][j] = 9;
                game.actualMap[i][j] = 0;
            }
        }

        game.display.getFrame().setVisible(false);
        game.display = new Display(game.title, game.mapWidth * game.BlockSize, game.topOffset + game.mapHeight * game.BlockSize);

        mouseRemover();
        game.gameState.mouseSetter();
        setCurrentState(game.gameState);
    }

    public void mouseSetter() {
        game.display.getCanvas().addMouseListener(mouseListener);
    }

    public void mouseRemover() {
        game.display.getCanvas().removeMouseListener(mouseListener);
    }
}
