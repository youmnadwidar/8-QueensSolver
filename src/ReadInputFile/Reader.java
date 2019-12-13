package ReadInputFile;

import java.io.*;

public class Reader {
    private static final int QUEENS = 8;
    private static int[] queens1D;
    private static boolean[][] queens2DBoolean;
    private static char[][] queens2DChars;


    private static boolean readFile(String file_url) {
        queens1D = new int[QUEENS];
        queens2DBoolean = new boolean[QUEENS][QUEENS];
        queens2DChars = new char[QUEENS][QUEENS];
        int row = 0;
        File file = new File(file_url);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] chars = line.trim().split("\\s+");
                if (chars.length != 8)
                    return false;
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i].compareTo("Q") == 0) {
                        queens1D[i] = row;
                        queens2DBoolean[row][i] = true;
                    }
                    queens2DChars[row][i] = chars[i].charAt(0);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return row == 8;
    }

    public static int[] getInputState1D(String file_url) {
        if (queens1D == null) {
            readFile(file_url);
        }
        return queens1D;
    }

    public static boolean[][] getInputState2DBoolean(String file_url) {
        if (queens2DBoolean == null) {
            readFile(file_url);
        }
        return queens2DBoolean;
    }

    public static char[][] getInputState2DChars(String file_url) {
        if (queens2DChars == null) {
            readFile(file_url);
        }
        return queens2DChars;
    }

}
