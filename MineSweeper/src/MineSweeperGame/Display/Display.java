package MineSweeperGame.Display;

import javax.swing.*;
import java.awt.*;

public class Display {

    private Canvas canvas;
    private JFrame frame;

    private String title;
    private int width, height;

    public Display(String title, double width, double height) {
        this.title = title;
        this.width = (int) width;
        this.height = (int) height;
        Frame();
    }

    private void Frame() {
        frame = new JFrame(title);

        frame.setBounds(0, 0, width, height);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}



