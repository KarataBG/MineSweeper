package fg331.free.bg.Util;

import java.io.*;
import java.util.Objects;

public class Util {

    public static void loadHighscore(String path, int bomb, int width, int height, int sekundi, int minuti) {
        StringBuilder builder = new StringBuilder(), copy = new StringBuilder();
        boolean isNew = true;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            String temp = builder.toString();
            String[] streaks = temp.split("\n");

//            System.out.println(streaks.length);
            String timeString = String.format("%s %s %s %s", bomb, width, height, (sekundi + minuti * 60));
            for (int i = 0; i < streaks.length; i++) {
                String[] row = streaks[i].split("\\s+"); // wzima indiwidualno redowete za analiz
                if (parseInt(row[0]) == bomb && parseInt(row[1]) == width && parseInt(row[2]) == height) { // ako ima we4e score
                    isNew = false;
                    if (sekundi + minuti * 60 < parseInt(row[3])) { // prowerqwa dali nowoto wreme e po malko
                        copy.append(timeString).append("\n");
                    } else { // ako nqma takaw score direktno wawejda
                        copy.append(streaks[i]).append("\n");
                    }
                } else {
                    copy.append(streaks[i]).append("\n");
                }
            }
            if (isNew) {
                copy.append(timeString).append("\n");
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