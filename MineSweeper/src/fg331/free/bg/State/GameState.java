package fg331.free.bg.State;

import fg331.free.bg.GFX.Assets;
import fg331.free.bg.MainBody.Game;
import fg331.free.bg.Util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameState extends State {
    Game game;

    public int meButtonX, meButtonY = 0, height = 100, width = 160;
    private String[] rows;
    private int highScore = 0;

    public GameState(Game game) {
        this.game = game;

        meButtonX = (int) (game.display.getFrame().getWidth() / 2 + game.BlockSize * 3 - 55);
    }

    private MouseListener mouseListener = new MouseListener() { // slu6atel na mi6kata
        // a / size of one cell, rounded down same for b
        Point temp; // wremenno zadarjane na koordinatite na mi6kata
        int relativeCursorX;
        int relativeCursorY;

        public void mouseClicked(MouseEvent e) {
            if (e.getX() > meButtonX && e.getX() < meButtonX + width / 2 && e.getY() > 30 && e.getY() < 70) {
                game.menuUpdater();

                mouseRemover();
                game.menu.mouseSetter();
                game.gameRunning = false;
                game.timer = false;
                State.setCurrentState(game.menu);
            }
        }

        public void mousePressed(MouseEvent e) { // natiskaneto na buton
            relativeCursorX = (int) (e.getX() / game.BlockSize);
            relativeCursorY = (int) ((e.getY() - game.topOffset) / game.BlockSize);

            if (!game.gameRunning) {
                return;
            }
            if (SwingUtilities.isLeftMouseButton(e)) { // ako lewiq buton e natisnat
                if (e.getX() > game.display.getFrame().getWidth() / 2 - 2 * game.BlockSize && e.getX() < game.display.getFrame().getWidth() / 2 + game.BlockSize
                        && e.getY() > 20 && e.getY() < game.BlockSize * 3 + 20) {
                    game.smilingGoodPerson = 1;
                } else if (relativeCursorY > 0) {
                    if (game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] == 9) { // ako e prazno mqsto
                        game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] = 0; // blok4eto stawa natisnato
                    }
                }
            }
            if (SwingUtilities.isRightMouseButton(e)) { // ako desniq buton e natisnat
                if (relativeCursorY < 0 || relativeCursorY > game.mapHeight || relativeCursorX < 0 || relativeCursorX > game.mapWidth)
                    return;
                if (game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] == 9) {// za postawqe na flag4eta na nerazkriti mesta
                    if (game.remainingBombs >0) {
                        game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] = 11; // za slagane na flag4e
                        game.remainingBombs--;
                    }
                } else if (game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] == 11) {// za wrastane na originalno nerazkrito mqsto
                    game.map[(int) (relativeCursorY)][(int) (relativeCursorX)] = 9;
                    game.remainingBombs++;
                }
            }
        }

        //TODO? ako e gre6no slojeno flag4e da se mahne?
        public void mouseReleased(MouseEvent e) { // pri otpuskane na mi6kata
            boolean bombTemp = true; // dali ima ostanali ne prowereni mesta

            for (int i = 0; i < game.mapHeight; i++) { // prowerqwa dali ima ostanali mesta
                for (int j = 0; j < game.mapWidth; j++) {
                    if (game.map[i][j] == 9) {
                        bombTemp = false;
                        break;
                    }
                }
            }

            if (SwingUtilities.isLeftMouseButton(e)) { // ako lewiq buton e bil natisnat
                if (e.getX() > game.display.getFrame().getWidth() / 2 - 2 * game.BlockSize && e.getX() < game.display.getFrame().getWidth() / 2 + game.BlockSize
                        && e.getY() > 20 && e.getY() < game.BlockSize * 3 + 20) { // ako e bil natisnat nad butona
                    //restartira igrata
                    game.smilingGoodPerson = 0;
                    game.sekundi = 0;
                    game.minuti = 0;
                    game.won = false;
                    game.remainingBombs = game.BOMBSAMOUNT;
                    game.gameRunning = true;
                    game.timer = true;

                    for (int i = 0; i < game.mapHeight; i++) { // restartira  na4alnite stoinosti na kartite
                        for (int j = 0; j < game.mapWidth; j++) {
                            game.map[i][j] = 9;
                            game.actualMap[i][j] = 0;
                        }
                    }
                    for (int i = 0; i < game.BOMBSAMOUNT; i++) { // zadawa nanowo bombite na slu4aen princip
                        int tempX;
                        int tempY;
                        do {
                            tempX = (int) (Math.random() * game.mapWidth);
                            tempY = (int) (Math.random() * game.mapHeight);
                        } while (game.actualMap[tempY][tempX] == 14);
                        game.actualMap[tempY][tempX] = 14;
                    }
                    for (int i = 0; i < game.mapHeight; i++) { // restartira prowerenite to4ki
                        for (int j = 0; j < game.mapWidth; j++) {
                            game.checkedPoints[i][j] = 0;
                        }
                    }
                    for (int i = 0; i < game.mapHeight; i++) { // zadawa nomera na blok4etata korespondirasti na bombite okolo tqh
                        for (int j = 0; j < game.mapWidth; j++) {
                            if (game.actualMap[i][j] == 14) {
                                for (int k = i - 1; k < i + 2; k++) {
                                    for (int l = j - 1; l < j + 2; l++) {
                                        if (k >= 0 && k < game.mapHeight && l >= 0 && l < game.mapWidth) {
                                            if (game.actualMap[k][l] != 14) {
                                                game.actualMap[k][l]++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    game.gameState.HSFinder();
                } else if (game.gameRunning) { // ako ne e natisnato nad butona i igrata warwi
                    if (relativeCursorY < 0 || relativeCursorY > game.mapHeight || relativeCursorX < 0 || relativeCursorX > game.mapWidth)
                        return;
                    if (game.map[relativeCursorY][relativeCursorX] != 11) {
                        if (game.actualMap[relativeCursorY][relativeCursorX] != 14) { // ako tam nqma bomba
                            if (game.actualMap[relativeCursorY][relativeCursorX] == 0) {//ako e prazno mqsto
                                game.BlankBlockCleaner(relativeCursorY, relativeCursorX);
                            } else { // ako ne e prazno mqsto
                                game.map[relativeCursorY][relativeCursorX] = game.actualMap[relativeCursorY][relativeCursorX];
                            }
                        } else { // ako tam ima bomba
                            game.exiter(relativeCursorY, relativeCursorX, true);
                        }
                    }
                }
            }
            if (bombTemp && game.remainingBombs == 0) { // ako wsi4ki mesta sa prowereni i nqma ostanali bombi
                game.smilingGoodPerson = 3;
                game.won = true;
                if (game.gameRunning) {
                    game.exiter((int) ((relativeCursorY)), relativeCursorX, false); // wika zawar6wa6tiq metod za igrata
                    game.gameRunning = false;
                }
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    };

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        //draw
        for (int i = 0; i < game.mapHeight; i++) {
            for (int j = 0; j < game.mapWidth; j++) {
                g.drawImage(Assets.blocks.get(game.map[i][j]), j * game.BlockSize, game.topOffset + i * game.BlockSize, game.BlockSize, game.BlockSize, null);
            }
        }
        g.drawImage(Assets.numbers.get(game.sekundi % 10), game.display.getFrame().getWidth() - game.BlockSize, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.sekundi / 10), game.display.getFrame().getWidth() - game.BlockSize * 2, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.minuti % 10), game.display.getFrame().getWidth() - 5 - game.BlockSize * 3, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.minuti / 10), game.display.getFrame().getWidth() - 5 - game.BlockSize * 4, 40, game.BlockSize, game.BlockSize, null);

        g.drawImage(Assets.ben.get(game.smilingGoodPerson), game.display.getFrame().getWidth() / 2 - 2 * game.BlockSize, 20, game.BlockSize * 3, game.BlockSize * 3, null);

        g.setColor(Color.BLACK);
        g.setFont(game.drawFont);
        g.drawString(String.valueOf(game.remainingBombs), game.BlockSize / 3, 60);

        g.setFont(game.drawFont1);
        g.drawString("Menu", meButtonX, 60);
        g.drawString("High Score: " + highScore, 0, height);

        // TODO oprawi teksta za po malki resolucii
        if (game.won) {
            game.g.drawString("You win", game.BlockSize * 2, 50);
        }
    }

    public void HSFinder() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Assets.path + "\\" + "HighScores.txt"));
//            BufferedReader bufferedReader = new BufferedReader(new FileReader("res/txt/HighScores.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rows = stringBuilder.toString().split("\n");

        boolean matched = false;
        for (int i = 0; i < rows.length; i++) {
            String[] words = rows[i].split("\\s+");
            if (Util.parseInt(words[0]) == game.BOMBSAMOUNT && Util.parseInt(words[1]) == game.mapWidth && Util.parseInt(words[2]) == game.mapHeight) {
                highScore = Util.parseInt(words[3]);
                matched = true;
            }
        }

        if (!matched) {
            highScore = 0;
        }
    }

    public void setMeButtonX() {
        meButtonX = (int) (game.display.getFrame().getWidth() / 2 + game.BlockSize * 3 - 60);
    }

    public void mouseSetter() {
        game.display.getCanvas().addMouseListener(mouseListener);
    }

    public void mouseRemover() {
        game.display.getCanvas().removeMouseListener(mouseListener);
    }
}
