package MineSweeperGame.MainBody;

public class Main {

    public static void main(String[] args) {

        //Main frame - partial/filling
        //size block - 30px
        //current block =/= last && secondLast

        //blocks -   x,y,width,height, Image

        Game game = new Game("MineSweeper ver2");
        game.start();
    }
}
