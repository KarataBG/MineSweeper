package MineSweeperGame.GFX;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Assets {
    private static final int numbers1width = 13;
    private static final int numbers1height = 23;
    private static final int covers1width = 16, covers1height = 16;
    private static final int benWidth = 26, benHeight = 26;


    public static HashMap<Integer, BufferedImage> numbers = new HashMap<>();
    public static HashMap<Integer, BufferedImage> blocks = new HashMap<>();
    public static HashMap<Integer, BufferedImage> ben = new HashMap<>();


    public static void init() {
        try {
            SpriteSheet sheet = new SpriteSheet(Image.imageLoader("/img/MS sprite.png"));

            numbers.put(1, sheet.crop(2, 2, numbers1width, numbers1height));
            numbers.put(2, sheet.crop(3 + numbers1width, 2, numbers1width, numbers1height));
            numbers.put(3, sheet.crop(4 + numbers1width * 2, 2, numbers1width, numbers1height));
            numbers.put(4, sheet.crop(5 + numbers1width * 3, 2, numbers1width, numbers1height));
            numbers.put(5, sheet.crop(6 + numbers1width * 4, 2, numbers1width, numbers1height));
            numbers.put(6, sheet.crop(7 + numbers1width * 5, 2, numbers1width, numbers1height));
            numbers.put(7, sheet.crop(8 + numbers1width * 6, 2, numbers1width, numbers1height));
            numbers.put(8, sheet.crop(9 + numbers1width * 7, 2, numbers1width, numbers1height));
            numbers.put(9, sheet.crop(10 + numbers1width * 8, 2, numbers1width, numbers1height));
            numbers.put(0, sheet.crop(11 + numbers1width * 9, 2, numbers1width, numbers1height));

            blocks.put(9, sheet.crop(2, 53, covers1width, covers1height));
            blocks.put(10, sheet.crop(3 + covers1width, 53, covers1width, covers1height));

            blocks.put(1, sheet.crop(2, 70, covers1width, covers1height));
            blocks.put(2, sheet.crop(3 + covers1width, 70, covers1width, covers1height));
            blocks.put(3, sheet.crop(4 + covers1width * 2, 70, covers1width, covers1height));
            blocks.put(4, sheet.crop(5 + covers1width * 3, 70, covers1width, covers1height));
            blocks.put(5, sheet.crop(6 + covers1width * 4, 70, covers1width, covers1height));
            blocks.put(6, sheet.crop(7 + covers1width * 5, 70, covers1width, covers1height));
            blocks.put(7, sheet.crop(8 + covers1width * 6, 70, covers1width, covers1height));
            blocks.put(8, sheet.crop(9 + covers1width * 7, 70, covers1width, covers1height));

            blocks.put(11, sheet.crop(4 + covers1width * 2, 53, covers1width, covers1height));
            blocks.put(12, sheet.crop(5 + covers1width * 3, 53, covers1width, covers1height));
            blocks.put(13, sheet.crop(6 + covers1width * 4, 53, covers1width, covers1height));
            blocks.put(14, sheet.crop(7 + covers1width * 5, 53, covers1width, covers1height));
            blocks.put(15, sheet.crop(8 + covers1width * 6, 53, covers1width, covers1height));
            blocks.put(16, sheet.crop(9 + covers1width * 7, 53, covers1width, covers1height));

            blocks.put(0, sheet.crop(3 + covers1width, 53, covers1width, covers1height));

            ben.put(0, sheet.crop(2, 26, benWidth, benHeight));
            ben.put(1, sheet.crop(3 + benWidth, 26, benWidth, benHeight));
            ben.put(2, sheet.crop(4 + benWidth * 2, 26, benWidth, benHeight));
            ben.put(3, sheet.crop(5 + benWidth * 3, 26, benWidth, benHeight));
            ben.put(4, sheet.crop(6 + benWidth * 4, 26, benWidth, benHeight));

        } catch (Exception e) {
            e.printStackTrace(); // This will print the stack trace to help you debug
        }
    }
}
