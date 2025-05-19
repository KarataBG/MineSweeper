package MineSweeperGame.GFX;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Image {
    static BufferedImage imageLoader(String path){
        try {
            return ImageIO.read(Image.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}