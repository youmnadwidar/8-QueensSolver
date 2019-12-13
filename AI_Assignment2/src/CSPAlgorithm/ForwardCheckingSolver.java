package CSPAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class ForwardCheckingSolver {


    private static final int QUEENS = 8;
    private int[] placedQueens;
    private ArrayList<int[]> validSolutions;
    private int[][] threatenedCells;
    private static final ArrayList<Integer> INITIALDOMAIN = initializeDomain();
    private int nodesEntered;
    private int solutionsCount;
    private long totalTime;

    public ForwardCheckingSolver() {
        placedQueens = new int[QUEENS];
        threatenedCells = new int[QUEENS][QUEENS];
        nodesEntered = 0;
        solutionsCount = 0;
        validSolutions = new ArrayList<>();
        totalTime = System.currentTimeMillis();
    }

    private static ArrayList<Integer> initializeDomain() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < QUEENS; i++) {
            list.add(i);
        }
        return list;
    }

    public void solveQueens() {
        totalTime = System.currentTimeMillis();
        solveQueensWithForwardChecking(0);
        totalTime = System.currentTimeMillis() - totalTime;
        System.out.println("Total Running Time : "+totalTime);
        System.out.println("Nodes Expanded : "+nodesEntered);
        System.out.println("Total Number of Solution : "+solutionsCount);
    }

    private boolean solveQueensWithForwardChecking(int col) {
        if (col == QUEENS) {
            solutionsCount++;
            SolutionPrinter.printSolution(placedQueens);
            int[] newSol = Arrays.copyOf(placedQueens, QUEENS);
            validSolutions.add(newSol);
            return true;
        }

        nodesEntered++;
        for (int i = 0; i < QUEENS; i++) {
            if (threatenedCells[i][col] > 0)
                continue;
            placedQueens[col] = i;
            performForwardChecking(col);
            if (isValid(col)) {
                solveQueensWithForwardChecking(col + 1);
            }
            backTrackDomains(col);

        }
        return false;
    }

    private void performForwardChecking(int col) {
        reCalculateForwardChecking(1, col, placedQueens[col]);
    }

    private void backTrackDomains(int col) {
        reCalculateForwardChecking(-1, col, placedQueens[col]);
    }

    private void reCalculateForwardChecking(int changeValue, int col, int lastInsertedValue) {
        for (int i = col + 1; i < QUEENS; i++) {
            threatenedCells[lastInsertedValue][i] += changeValue;
            int x = i - col;
            if (placedQueens[col] + x < QUEENS)
                threatenedCells[lastInsertedValue + x][i] += changeValue;
            if (placedQueens[col] - x >= 0)
                threatenedCells[lastInsertedValue - x][i] += changeValue;
        }
    }

    private boolean isValid(int lastInserted) {
        boolean solution;
        for (int j = lastInserted + 1; j < QUEENS; j++) {
            solution = false;
            for (int i = 0; i < QUEENS && !solution; i++) {
                solution |= threatenedCells[i][j] == 0;
            }
            if (!solution)
                return false;
        }

        return true;
    }

    public ArrayList<int[]> getValidSolutions() {
        return validSolutions;
    }

    public int getNodesEntered() {
        return nodesEntered;
    }

    public int getSolutionsCount() {
        return solutionsCount;
    }

    public long getTotalTime() {
        return totalTime;
    }

}
