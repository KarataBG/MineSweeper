package MineSweeperGame.Util;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class Util {

    public static void loadHighscore(String path, int bomb, int width, int height, int sekundi, int minuti) {
        StringBuilder builder = new StringBuilder(), copy = new StringBuilder();
        boolean isNew = true;

        File file = new File(path);
        if (!file.exists()){
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getAbsolutePath());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            String temp = builder.toString().trim();
            String[] linesArray = temp.split("\n");

            String formattedNewRow = String.format("%s %s %s %s", String.valueOf(bomb), String.valueOf(width), String.valueOf(height), String.valueOf(sekundi + minuti * 60));
            for (int i = 0; i < linesArray.length; i++) {
                String[] row = linesArray[i].split("\\s+");
                if (parseInt(row[0]) == bomb && parseInt(row[1]) == width && parseInt(row[2]) == height) { // prowerqwa za sawpadenie w HighScores
                    isNew = false;
                    if (sekundi + minuti * 60 < parseInt(row[3])) { // prowerqwa dali nowoto wreme e po malko
                        copy.append(formattedNewRow).append("\n");
                    } else { // ako staroto wreme e po malko go apendwa nego
                        copy.append(linesArray[i]).append("\n");
                    }
                } else {
                    copy.append(linesArray[i]).append("\n");
                }
            }
            if (isNew) {
                copy.append(formattedNewRow).append("\n");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, false)); // print za izkarwaneto na obnowenata informaciq
//            PrintWriter printWriter = new PrintWriter(bufferedWriter); // print za izkarwaneto na obnowenata informaciq
//            printWriter.print(copy);

            bufferedWriter.write(copy.toString());
            bufferedWriter.flush();
            bufferedWriter.close();

            bufferedReader.close();
//            printWriter.flush();
//            printWriter.close();
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