package MineSweeperGame.State;

import MineSweeperGame.GFX.Assets;
import MineSweeperGame.MainBody.Game;
import MineSweeperGame.Util.Util;

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

        meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize * 3 - 60;
    }

    public void setupGame () {
        game.smilingFace = 0;
        game.gameRunning = true;
        game.sekundi = 0;
        game.minuti = 0;
        game.won = false;
        game.remainingBombs = game.BOMBSAMOUNT;
        game.timer = true;
        meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize*3 -60;


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
    }

    private MouseListener mouseListener = new MouseListener() { // slu6atel na mi6kata
        // a / size of one cell, rounded down same for b
        Point temp; // wremenno zadarjane na koordinatite na mishkata
        double cursorY; // wremenno zadarjane na mishka Y
        double cursorX; // wremenno zadarjane na mishka X

        public void mouseClicked(MouseEvent e) {
            meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize * 3 - 60;
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
            temp = new Point(MouseInfo.getPointerInfo().getLocation()); // priema na stoinosti ^45
            cursorY = temp.getY(); // priema na stoinosti ^46
            cursorX = temp.getX(); // priema na stoinosti ^47

            double relativeCursorY = cursorY - game.topOffset - game.display.getFrame().getLocation().getY(); // poziciq Y na kursora sprqma igralniq ekran
            double relativeCursorX = cursorX - game.display.getFrame().getLocation().getX(); // poziciq X na kursora sprqma igralniq ekran


            if (!game.gameRunning)
                return;
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (relativeCursorX > game.mapWidth * game.BlockSize / 2 - 60 && relativeCursorX < game.mapWidth * game.BlockSize / 2 + 2 * game.BlockSize && relativeCursorY > game.faceBase && relativeCursorY < game.faceBase + game.BlockSize * 3) { // ako e natisnat nad butona
                    game.smilingFace = 1;
                } else if (relativeCursorY / game.BlockSize > 1) {
                    if (game.map[(int) (relativeCursorY / game.BlockSize) - 1][(int) (relativeCursorX / game.BlockSize)] != 14) { // ako ne e natisnata bomba
                        if (game.map[(int) (relativeCursorY / game.BlockSize) - 1][(int) (relativeCursorX / game.BlockSize)] == 9) { // ako e prazno mqsto
                            game.map[(int) (relativeCursorY / game.BlockSize) - 1][(int) (relativeCursorX / game.BlockSize)] = 0; // blok4eto stawa natisnato
                        }
                    }
                }
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                if (game.map[(int) (relativeCursorY / game.BlockSize - 1)][(int) (relativeCursorX / game.BlockSize)] == 9) {// za postawqe na flag4eta na nerazkriti mesta
                    game.map[(int) (relativeCursorY / game.BlockSize - 1)][(int) (relativeCursorX / game.BlockSize)] = 11; // za slagane na flag4e
                    game.remainingBombs--;
                } else if (game.map[(int) (relativeCursorY / game.BlockSize - 1)][(int) (relativeCursorX / game.BlockSize)] == 11) {// za wrastane na originalno nerazkrito mqsto
                    game.map[(int) (relativeCursorY / game.BlockSize - 1)][(int) (relativeCursorX / game.BlockSize)] = 9;
                    game.remainingBombs++;
                }
            }
        }

        //TODO? ako e gre6no slojeno flagche da se mahne?
        public void mouseReleased(MouseEvent e) { // pri otpuskane na mi6kata


            double relativeCursorY = cursorY - game.topOffset - game.display.getFrame().getLocation().getY(); // // poziciq Y na kursora sprqma igralniq ekran
            double relativeCursorX = cursorX - game.display.getFrame().getLocation().getX(); // // poziciq X na kursora sprqma igralniq ekran
            int coordY = (int) ((relativeCursorY / game.BlockSize) - 1); // wisochinata na blok4e na koeto e caknato
            int coordX = (int) (relativeCursorX / game.BlockSize); // 6iro4inata na blok4e na koeto e caknato
            boolean bombTemp = true; // dali ima ostanali ne prowereni mesta

            for (int i = 0; i < game.mapHeight; i++) { // prowerqwa dali ima ostanali mesta
                for (int j = 0; j < game.mapWidth; j++) {
                    if (game.map[i][j] == 9) {
                        bombTemp = false;
                        break;

                    }
                }
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (relativeCursorX > game.mapWidth * game.BlockSize / 2 - 60 && relativeCursorX < game.mapWidth * game.BlockSize / 2 + 2 * game.BlockSize && relativeCursorY > game.faceBase && relativeCursorY < game.faceBase + game.BlockSize * 3) { // ako e bil natisnat nad butona
                    //restartira igrata

                    setupGame();


                } else if (game.gameRunning) { // ako ne e natisnato nad butona i igrata warwi
                    if (coordY < 0 || coordY > game.mapHeight || coordX < 0 || coordX > game.mapWidth)
                        return;
                    if (game.map[coordY][coordX] != 9 // ne e prazno mqsto
                            && game.map[coordY][coordX] != 11) { // ne e flag4e
                        if (game.actualMap[coordY][coordX] != 14) { // ne e bomba
                            if (game.actualMap[coordY][coordX] == 0) { // ako e prazno mqstoto
                                game.BlankBlockCleaner(coordY, coordX); // wika rekursiq
                            } else { // ako ne e prazno
                                game.map[(int) ((relativeCursorY / game.BlockSize) - 1)][coordX]
                                        = game.actualMap[(int) ((relativeCursorY / game.BlockSize) - 1)][coordX]; // update ot istinska karta na widimata karta
                            }
                        } else {// ako e bomba spira igrata
                            game.exiter((int) ((relativeCursorY / game.BlockSize) - 1), coordX, true);
                        }
                    }
                }
            }
            if (bombTemp && game.remainingBombs == 0) { // ako wsi4ki mesta sa prowereni i nqma ostanali bombi
                game.smilingFace = 3;
                game.won = true;
                if (game.gameRunning) {
                    game.exiter((int) ((relativeCursorY / game.BlockSize) - 1), coordX, false); // wika zawar6wa6tiq metod za igrata
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
//        g.drawImage(Assets.numbers.get(game.sekundi % 10), game.mapWidth * game.BlockSize / 2 + game.BlockSize * 6, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.sekundi % 10), game.mapWidth * game.BlockSize - game.BlockSize, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.sekundi / 10), game.mapWidth * game.BlockSize - game.BlockSize * 2, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.minuti % 10), game.mapWidth * game.BlockSize - 10 - game.BlockSize * 3, 40, game.BlockSize, game.BlockSize, null);
        g.drawImage(Assets.numbers.get(game.minuti / 10), game.mapWidth * game.BlockSize - 10 - game.BlockSize * 4, 40, game.BlockSize, game.BlockSize, null);

        g.drawImage(Assets.ben.get(game.smilingFace), game.mapWidth * game.BlockSize / 2 - 60, 20, game.BlockSize * 3, game.BlockSize * 3, null);

        g.setColor(Color.BLACK);
        g.setFont(game.drawFont);
        g.drawString(String.valueOf(game.remainingBombs), game.BlockSize / 3, 60);

        g.setFont(game.drawFont1);
        g.drawString("Menu", meButtonX, 60);
        g.drawString("High Score: " + highScore, 0, height);

        // TODO oprawi teksta za po malki resolucii
        if (game.won) {
            game.g.drawString("You win", game.BlockSize * 2, 100);

        }
    }

    private void HSFinder() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("res/txt/HighScores.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rows = stringBuilder.toString().split("\n");
        System.out.println(rows.length);

        for (int i = 0; i < rows.length; i++) {
            String[] words = rows[i].split("\\s+");
            if (Util.parseInt(words[0]) == game.BOMBSAMOUNT && Util.parseInt(words[1]) == game.mapWidth && Util.parseInt(words[2]) == game.mapHeight) {
                highScore = Util.parseInt(words[4]);
            }
        }

    }

    public void setMeButtonX() {
        meButtonX = game.mapWidth * game.BlockSize / 2 + game.BlockSize * 3 - 60;
    }

    public void mouseSetter() {
        game.display.getCanvas().addMouseListener(mouseListener);
    }

    public void mouseRemover() {
        game.display.getCanvas().removeMouseListener(mouseListener);
    }
}
