package fg331.free.bg.State;

import fg331.free.bg.Display.Display;
import fg331.free.bg.GFX.Assets;
import fg331.free.bg.MainBody.Game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Scores extends State {
    private Game game;
    private String[] randomName;
    private String[][] HSRows;

    public Scores(Game game) {
        this.game = game;
    }

    public void screenSetter() {
        fileReader();
        final String[] columns = {"Bombs", "Width", "Height", "Time"};
        final JTable pane = new JTable(new DefaultTableModel(HSRows, columns));

        game.display.getFrame().setVisible(false);

        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton jButton = new JButton("Menu");
        jButton.addActionListener(e -> {
            State.setCurrentState(game.menu);
            jFrame.setVisible(false);
            game.display = new Display(game.title, game.mapWidth * game.BlockSize, game.topOffset + game.mapHeight * game.BlockSize);
            game.menu.mouseSetter();
        });

        jFrame.getContentPane().setLayout(new BorderLayout());
        jFrame.getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);
        jFrame.getContentPane().add(jButton, BorderLayout.SOUTH);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private void fileReader() {
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

        String[] tempString = stringBuilder.toString().split("\\s+");
        HSRows = new String[tempString.length / 4][4]; // 50   4

        for (int k = 0; k < tempString.length / 4; k++) {
            System.arraycopy(tempString, k * 4, HSRows[k], 0, 4);
        }

        randomName = stringBuilder.toString().split("\\s+");
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    public void mouseSetter() {
    }

    public void mouseRemover() {
    }
}
