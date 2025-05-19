package MineSweeperGame.State;

import MineSweeperGame.Display.Display;
import MineSweeperGame.MainBody.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Settings extends State {
    Game game;

    public Settings(Game game) {
        this.game = game;

        seButtonX = game.mapWidth * game.BlockSize / 2;
        seButtonY = game.mapWidth * game.BlockSize / 2;
    }

    private int seButtonX, seButtonY;
    private int width = 200, height = 60;
    private int switcher = 0;
    private ArrayList<Character> holderBombs = new ArrayList<Character>() {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Character c : holderBombs) {
                stringBuilder.append(c);
            }

            return stringBuilder.toString();
        }
    };
    private ArrayList<Character> holderWidth = new ArrayList<Character>() {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Character c : holderWidth) {
                stringBuilder.append(c);
            }

            return stringBuilder.toString();
        }
    };
    ;
    private ArrayList<Character> holderHeight = new ArrayList<Character>() {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Character c : holderHeight) {
                stringBuilder.append(c);
            }

            return stringBuilder.toString();
        }
    };

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getX() > seButtonX && e.getX() < seButtonX + width && e.getY() > seButtonY && e.getY() < seButtonY + height) {
                mouseRemover();
                game.menu.mouseSetter();
                setCurrentState(game.menu);
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

    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 40) {
                switcher++;
            } else if (e.getKeyCode() == 38) {
                switcher--;
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                switch (switcher) {
                    case 0:
                        holderBombs.remove(holderBombs.size() - 1);
                        break;
                    case 1:
                        holderWidth.remove(holderWidth.size() - 1);
                        break;
                    case 2:
                        holderHeight.remove(holderHeight.size() - 1);
                        break;
                }
            } else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                mouseRemover();
                game.menu.mouseSetter();
                setCurrentState(game.menu);

            } else if (e.getKeyCode() > 47 && e.getKeyCode() < 58) {
                switch (switcher) {
                    case 0:
                        if (holderBombs.size() < 3)
                            holderBombs.add(Character.valueOf(e.getKeyChar()));
                        break;
                    case 1:
                        if (holderWidth.size() < 2)
                            holderWidth.add(e.getKeyChar());
                        break;
                    case 2:
                        if (holderHeight.size() < 2)
                            holderHeight.add(e.getKeyChar());
                        break;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    public void updater() {
        if (holderBombs.size() > 0 && holderWidth.size() > 0 && holderHeight.size() > 0) {
            game.BOMBSAMOUNT = Integer.parseInt(holderBombs.toString());
            game.mapWidth = Integer.parseInt(holderWidth.toString());
            game.mapHeight = Integer.parseInt(holderHeight.toString());

            game.map = new int[Integer.parseInt(holderHeight.toString())][Integer.parseInt(holderWidth.toString())];
            game.actualMap = new int[Integer.parseInt(holderHeight.toString())][Integer.parseInt(holderWidth.toString())];
            game.checkedPoints = new int[Integer.parseInt(holderHeight.toString())][Integer.parseInt(holderWidth.toString())];
            game.remainingBombs = Integer.parseInt(holderBombs.toString());
            game.gameState.meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize*3 -60;
            game.gameRunning = true;
            game.sekundi = 0;
            game.minuti = 0;
            if (game.mapWidth * 30 > game.screenSize.getWidth()) {
                game.BlockSize = (int) (game.screenSize.getWidth() / game.mapWidth);
            } else if (game.mapHeight * 30 + game.topOffset > game.screenSize.getHeight()) {
                game.BlockSize = (int) ((game.screenSize.getHeight() - game.topOffset) / game.mapHeight);
            } else {
                game.BlockSize = 30;
            }

            for (int i = 0; i < Integer.parseInt(holderHeight.toString()); i++) {
                for (int j = 0; j < Integer.parseInt(holderWidth.toString()); j++) {
                    game.map[i][j] = 9;
                    game.actualMap[i][j] = 0;
                }
            }

            game.display.getFrame().setVisible(false);
            game.display = new Display(game.title, game.mapWidth * game.BlockSize, game.topOffset + game.mapHeight * game.BlockSize);
        }
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(seButtonX, seButtonY, width, height);

        g.setColor(Color.BLACK);
        g.setFont(game.drawFont);

        g.drawString("Menu", seButtonX + width / 4, seButtonY + height * 2 / 3);
        g.drawString(holderBombs.toString(), (int) (game.BlockSize * 2.5), 100);
        g.drawString(holderWidth.toString(), game.BlockSize * 8, 100);
        g.drawString(holderHeight.toString(), game.BlockSize * 14, 100);
        g.drawString("Bombs", game.BlockSize, 200);
        g.drawString("Width", game.BlockSize * 8, 200);
        g.drawString("Height", game.BlockSize * 14, 200);
    }

    public void mouseSetter() {
        game.display.getCanvas().addMouseListener(mouseListener);
        game.display.getCanvas().addKeyListener(keyListener);
    }

    public void mouseRemover() {
        game.display.getCanvas().removeMouseListener(mouseListener);
        game.display.getCanvas().removeKeyListener(keyListener);
    }
}
