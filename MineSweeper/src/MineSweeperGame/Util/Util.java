package MineSweeperGame.Util;

import java.io.*;
import java.util.Objects;

public class Util {

    public static void loadHighscore(String path, int bomb, int width, int height, int sekundi, int minuti) {
        StringBuilder builder = new StringBuilder(), copy = new StringBuilder(); // builder koito ste prieme informaciqta ot celiq fail
        boolean isNew = true; // ste proweri dali ste ima nowa informaciq za dobawqne

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path)); // buferiran 4etec na txt file

            String line; // priema edin po edin redowete ot txt file
            while ((line = bufferedReader.readLine()) != null) { // dokato ima liniq s tekst
                builder.append(line).append("\n"); // wzima wsi4ki redowe kato edin red i now red i otnowo
            }
            String temp = builder.toString(); // prehwarlqm wzetiq tekst
            String[] streaks = temp.split("\n"); // wzima otdelno redowete

            for (int i = 0; i < streaks.length; i++) {
                String[] row = streaks[i].split("\\s+"); // wzima indiwidualno redowete za analiz
                if (parseInt(row[0]) == bomb && parseInt(row[1]) == width && parseInt(row[2]) == height) { // ako ima we4e score
                    isNew = false;
                    if (sekundi + minuti * 100 < parseInt(row[3])) { // prowerqwa dali nowoto wreme e po malko
                        copy.append(String.format("%s %s %s %s", String.valueOf(bomb), String.valueOf(width), String.valueOf(height), String.valueOf(sekundi + minuti * 100))).append("\n");
                    } else { // ako nqma takaw score direktno wawejda
                        copy.append(streaks[i]).append("\n");
                    }
                } else {
                    copy.append(streaks[i]).append("\n");
                }
            }
            if (isNew) {
                copy.append(String.format("%s %s %s %s", String.valueOf(bomb), String.valueOf(width), String.valueOf(height), String.valueOf(sekundi + minuti * 100))).append("\n");
            }
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, false))); // print za izkarwaneto na obnowenata informaciq
            printWriter.print(copy.toString());

            bufferedReader.close();
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int parseInt(String number) {
        try {
            if (Objects.equals(number, "")) {
                return -1;
            }
            return Integer.parseInt(number);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

}