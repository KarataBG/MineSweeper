package fg331.free.bg.State;

import fg331.free.bg.Display.Display;
import fg331.free.bg.MainBody.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Menu extends State {
    private final int width = 180;
    private final int height = 60;
    private final String[] strings = {"Easy preset", "Inter Preset", "Hard Preset", "Insane Preset", "Start", "Settings", "Scores"};
    Game game;
    State gameState;
    private int startX, startY;
    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY && e.getY() < startY + height) {
                game.BOMBSAMOUNT = 50;
                game.mapWidth = 20;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY + (height + 20) && e.getY() < startY + (height + 20) + height) {
                game.BOMBSAMOUNT = 60;
                game.mapWidth = 25;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY + (height + 20) * 2 && e.getY() < startY + (height + 20) * 2 + height) {
                game.BOMBSAMOUNT = 100;
                game.mapWidth = 30;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY + (height + 20) * 3 && e.getY() < startY + (height + 20) * 3 + height) {
                game.BOMBSAMOUNT = 150;
                game.mapWidth = 40;
                game.mapHeight = 20;
                presetUpdater();
            } else if (e.getX() > startX && e.getX() < startX + width && e.getY() > startY + (height + 20) * 4 && e.getY() < startY + (height + 20) * 4 + height) {
                game.settings.updater();
                if (game.map != null) {
                    mouseRemover();
                    game.gameState.mouseSetter();
                    State.setCurrentState(game.gameState);
                }
            } else if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY + (height + 20) * 5 && e.getY() < startY + (height + 20) * 5 + height) {
                mouseRemover();
                game.settings.setSwitcher(0);
                game.settings.setHolderBombs();
                game.settings.setHolderWidth();
                game.settings.setHolderHeight();
                game.settings.mouseSetter();
                State.setCurrentState(game.settings);
            } else if (e.getX() > startX && e.getX() < startX + width * 2 && e.getY() > startY + (height + 20) * 6 && e.getY() < startY + (height + 20) * 6 + height) {
                mouseRemover();
                game.scores.screenSetter();
                State.setCurrentState(game.scores);
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

    public Menu(Game game) {
        this.game = game;
        gameState = game.gameState;

        startX = game.mapWidth * game.BlockSize / 8;
        startY = game.mapHeight * game.BlockSize / 12;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        startX = game.mapWidth * game.BlockSize / 8;
        startY = game.mapHeight * game.BlockSize / 12;

        g.setFont(game.drawFont);

        for (int i = 0; i < 7; i++) {
            int temp = startY + (height + 20) * i;
            g.setColor(Color.GRAY);
            g.fillRect(startX, temp, width * 2, height);
            g.setColor(Color.BLACK);
            g.drawString(strings[i], startX + width / 8, temp + height * 2 / 3);
        }
    }

    private void presetUpdater() {
        game.map = new int[game.mapWidth][game.mapHeight];
        game.actualMap = new int[game.mapWidth][game.mapHeight];
        game.checkedPoints = new int[game.mapWidth][game.mapHeight];
        game.remainingBombs = game.BOMBSAMOUNT;
        game.gameRunning = true;
        game.sekundi = 0;
        game.minuti = 0;
        game.won = false;
        game.smilingGoodPerson = 0;

        for (int i = 0; i < game.mapWidth; i++) {
            for (int j = 0; j < game.mapHeight; j++) {
                game.map[i][j] = 9;
                game.actualMap[i][j] = 0;
            }
        }

        game.display.getFrame().setVisible(false);
        game.display = new Display(game.title, game.mapWidth * game.BlockSize, game.topOffset + game.mapHeight * game.BlockSize);

        game.gameState.HSFinder();
        game.gameState.meButtonX = game.display.getFrame().getWidth() / 2 + game.BlockSize * 3 - 60;
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
