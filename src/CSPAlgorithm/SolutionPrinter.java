package CSPAlgorithm;

public class SolutionPrinter {
    private static final int QUEENS = 8;

    static void printSolution(int[] placedQueens) {
        int[] rowsValues = new int[QUEENS];
        for (int i = 0; i < QUEENS; i++) {
            rowsValues[placedQueens[i]] = i;
            System.out.print(" " +placedQueens[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < QUEENS; i++) {
            for (int j = 0; j < QUEENS; j++) {
                if (rowsValues[i] == j)
                    System.out.print(" Q ");
                else
                    System.out.print(" # ");
            }
            System.out.println();
        }
        System.out.println("===========================");

    }
}
