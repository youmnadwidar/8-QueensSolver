import CSPAlgorithm.BackTrackingSolver;
import CSPAlgorithm.ForwardCheckingSolver;
import GeneticAlgorithm.GeneticAlgorithm;
import HillClimbingAlgorithm.HillClimbingAlgorithm;
import KBeam.LocalBeam;
import KBeam.Utils;
import ReadInputFile.Reader;

import java.awt.*;
import java.util.Scanner;

public class MainRunner {
    public static void main(String[] args) {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        int input;
        while (!exit) {
            System.out.println("\nSelect the Algorithm you need :\n" +
                    "1) Hill Climbing Algorithm \n" +
                    "2) K-Beam Algorithm\n" +
                    "3) Genetic Algorithm\n" +
                    "4) Constraint Satisfaction Problem (CSP) BackTracking\n" +
                    "5) Constraint Satisfaction Problem (CSP) with Forward Checking\n" +
                    "For exit press 0");
            input = scanner.nextInt();

            switch (input) {
                case 1:
                    boolean[][] queens1 = Reader.getInputState2DBoolean("Sample_Input.txt");
                    HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm(queens1);
                    hillClimbingAlgorithm.run();
                    break;
                case 2:
                    int[] queens2 = Reader.getInputState1D("Sample_Input.txt");
                    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(queens2);
                    geneticAlgorithm.run();
                    break;
                case 3:
                    char[][] queens3 = Reader.getInputState2DChars("Sample_Input.txt");
                    LocalBeam localBeam = new LocalBeam(queens3);
                    Point[] result = localBeam.run(8, 100, 100);
                    System.out.println(Utils.getHeuristicCost(result));
                    print_result(result);
                    System.out.println("#Of steps : " + localBeam.getSteps());
                    System.out.println("#Of expanded : " + localBeam.getNumberOfBoardsExpanded());
                    System.out.println("Running Time(ms) : " + localBeam.getRunning_time());
                    break;
                case 4:
                    int[] queens4 = Reader.getInputState1D("Sample_Input.txt");
                    BackTrackingSolver backTrack = new BackTrackingSolver();
                    backTrack.solveQueens(queens4);
                    break;
                case 5:
                    ForwardCheckingSolver forwardChecking = new ForwardCheckingSolver();
                    forwardChecking.solveQueens();
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }

    }

    private static void print_result(Point[] result) {
        String[][] board = new String[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = "X ";
        //place the queens on the board
        for (int i = 0; i < 8; i++) {
            board[result[i].x][result[i].y] = "Q ";
        }
        //feed values into the result strin
        String out = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                out += board[i][j];
            }
            out += "\n";
        }
        System.out.println(out);
    }
}
