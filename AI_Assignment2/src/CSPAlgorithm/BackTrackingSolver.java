package CSPAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class BackTrackingSolver {

    private static final int QUEENS = 8;
    private int[] placedQueens;
    private ArrayList<int[]> validSolutions;

    public int getNodesEntered() {
        return nodesEntered;
    }

    private int nodesEntered;
    private int solutionsCount;
    private int solutionStepsGivenState;
    private long totalTime;
    private boolean initialStateSolution;
    private int nodesEnteredGivenState;
    private long totalTimeGivenState;
    private int[] initialState;

    public BackTrackingSolver() {
        placedQueens = new int[QUEENS];
        nodesEntered = 0;
        solutionsCount = 0;
        validSolutions = new ArrayList<>();
        totalTime = System.currentTimeMillis();
    }

    public void solveQueens(int[] initialState) {
        totalTime = System.currentTimeMillis();
        this.initialState = initialState;
        solveQueensWithBackTracking(0);
        totalTime = System.currentTimeMillis() - totalTime;
        System.out.println("Total Running Time : "+totalTime);
        System.out.println("Nodes Expanded : "+nodesEntered);
        System.out.println("Total Number of Solution : "+solutionsCount);
        System.out.println("Running time for the Initial State :"+totalTimeGivenState);
        System.out.println("Nodes Expanded the Initial State :"+nodesEnteredGivenState);
        System.out.println("Steps for the Initial State :"+solutionStepsGivenState);
    }

    private boolean solveQueensWithBackTracking(int col) {
        if (col == QUEENS) {
            solutionsCount++;
            if (initialStateSolution) {
                setVariables();
            }
            SolutionPrinter.printSolution(placedQueens);
            int[] newSol = Arrays.copyOf(placedQueens, QUEENS);
            validSolutions.add(newSol);
            return true;
        }
        nodesEntered++;
        for (int i = 0; i < QUEENS; i++) {
            placedQueens[col] = i;
            if (checkInitialState(placedQueens))
                saveVariables();
            if (isValid(col)) {
                if (initialStateSolution)
                    solutionStepsGivenState++;

                solveQueensWithBackTracking(col + 1);
                if (initialStateSolution)
                    solutionStepsGivenState--;
            }
            placedQueens[col] = 0;
        }
        return false;
    }

    private void setVariables() {
        initialStateSolution = false;
        nodesEnteredGivenState = nodesEntered - nodesEnteredGivenState;
        totalTimeGivenState = System.currentTimeMillis() - totalTimeGivenState;
    }

    private void saveVariables() {
        solutionStepsGivenState = 0;
        initialStateSolution = true;
        nodesEnteredGivenState = nodesEntered;
        totalTimeGivenState = System.currentTimeMillis();
    }

    private boolean checkInitialState(int[] placedQueens) {
        for (int i = 0; i < QUEENS; i++) {
            if (placedQueens[i] != initialState[i])
                return false;
        }
        return true;
    }

    private boolean isValid(int lastInserted) {
        for (int i = 0; i < lastInserted; i++) {
            if (placedQueens[i] == placedQueens[lastInserted]
                    || Math.abs(placedQueens[i] - placedQueens[lastInserted]) == Math.abs(i - lastInserted))
                return false;
        }
        return true;
    }

    public ArrayList<int[]> getValidSolutions() {
        return validSolutions;
    }

    public long getTotalTime() {
        return totalTime;
    }
}
