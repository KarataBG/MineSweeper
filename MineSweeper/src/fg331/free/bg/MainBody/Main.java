package fg331.free.bg.MainBody;

public class Main {

    public static void main(String[] args) {

        //Main frame - partial/filling
        //size block - 30px
        //current block =/= last && secondLast

        //blocks -   x,y,width,height, Image

        Game game = new Game("MineSweeper");
        game.start();
    }

    //TODO sloji pod "Menu" w Igrata(GameState) w format min : sek nai dobar rezultat za tezi nastroiki
    //TODO naprawi now State w koito da moje da se razglejdat wsi4ki rezultati (nai wero`tno sas skrol)

    //TODO kogato mu sloja palen pat do txt file (/HighScore.txt) moje da se wzeme dori w exe prognoziram
    // 4e ste moga ako mu kaja da wzima ot okolo sebe i sloja faila tam ste moje da go izpozwa
    //TODO oprawi scale pri po malkite resolucii (butonite za start settings i score)
    //TODO moga da nakaram .exe file da e zawisim ot txt fail na kompa koito toi da updeitwa i da 4ete

    //TODO ostawa da probwam da mu naprawq swoi si txt file koito da e negowata dir i pak da updeitwa i 4ete ot nego

    //TODO naprawi apadtiwnost za width < 14 da risuwa prazno pole koeto dopalwa do 6iro4ina
    //TODO mahni scale ot game.BlovkSize * game.mapWiddth i go sloji na frame.width/game.width (6iro4ina na samiq ekran)

    //TODO da refre6wam won wseki pat kogato wlizam w GameState i powe4e wikane na HSFinder

    //TODO cursorna animaciq za kadeto pi6e da bade spored switcher(stoinost)
}
