package KBeam;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LocalBeam {
    private int steps;
    private Point[] board;
    private int numberOfBoardsExpanded;
    double running_time;



    public LocalBeam(char[][] initial_board) {
        createBoard(initial_board);
    }

    private void createBoard(char[][] initial_board) {
        int currentPosition = 0;
        board = new Point[initial_board.length];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (initial_board[j][i] == 'Q') {
                    //i is the columns and j is the rows
                    board[currentPosition++] = new Point(j, i);
                }
            }
        }
    }



    public LocalBeam(){}

    public Point[] run(int n, int maxNumOfIterations, int k) {
        double start_time =  System.currentTimeMillis();
        Point[] solution = solve(n, maxNumOfIterations, k);
        running_time  = System.currentTimeMillis() - start_time;
        return solution;
    }

    private Point[] solve(int n, int maxNumOfIterations, int k) {
        List<Point[]> k_states = new ArrayList<Point[]>();
        if(board != null) {
            if (Utils.getHeuristicCost(board) == 0)
                return board;
            k_states = generateRandomStatesFromInitial(board, k);
        } else {
            for (int i = 0; i < k; i++) {
                k_states.add(Utils.initializeQueensPosition());
                print_result(k_states.get(i));
            }
        }
        for (int i = 0; i < k; i++) {
            print_result(k_states.get(i));
        }
        for (int x = 0; x < maxNumOfIterations; x++) {
            steps++;
            ArrayList<Point[]> newStates = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                int costToBeat = Utils.getHeuristicCost(k_states.get(i));
                // if solved
                if (costToBeat == 0)
                    return k_states.get(i);
                for (int queen = 0; queen < n; queen ++) {
                    for (int direction = 0; direction < 4; direction++) {
                        newStates.add(makeMove(k_states.get(i), queen,direction, costToBeat));
                        // if stuck
                        if (newStates.get(newStates.size() -1 ) == null)
                            newStates.set(newStates.size()-1, Utils.initializeQueensPosition());
                      /*  System.out.println("At state : ");
                        print_result(newStates.get(newStates.size()-1));
                        System.out.println("cost : ");
                        System.out.println(Utils.getHeuristicCost(newStates.get(newStates.size()-1)));*/
                    }
                }

            }

            System.out.println(newStates.size());
            Collections.sort(newStates, Comparator.comparingInt(Utils::getHeuristicCost));

            k_states = newStates.subList(0, k);
            System.out.println("Current K states : ");
            for (int i = 0; i < k; i++ ) {
                print_result(k_states.get(i));
            }
        }

        return null;
    }


    private Point[] makeMove(Point[] r, int queen, int direction, int costToBeat) {
        int n = r.length;
        for (int m = 0; m < n; m++) {
            Point newPoint = getNewPosition(direction, m, r[queen]);
            if (newPoint == null || queenExists(r, newPoint)) {
                continue;
            }
            numberOfBoardsExpanded++;
            Point tmpPoint = r[queen];
            r[queen] = newPoint;
            int cost = Utils.getHeuristicCost(r);
            if (costToBeat > cost) {
                return r;
            }
            r[queen] = tmpPoint;
        }

        return null;
    }

    private Point getNewPosition(int direction, int position, Point currentQueen) {
        Point newPoint = new Point();
        switch (direction) {
            case 0: //moving in same col
                newPoint.x = position;
                newPoint.y = currentQueen.y;
                break;
            case 1: //moving in same row
                newPoint.y = position;
                newPoint.x = currentQueen.x;
                break;
            case 2: // moving in left diagonal
                int xd1 = currentQueen.x - Math.min(currentQueen.x, currentQueen.y);
                int yd1 = currentQueen.y - Math.min(currentQueen.x, currentQueen.y);
                newPoint.x = xd1 + position;
                newPoint.y = yd1 + position;
                if (newPoint.x >= 8 || newPoint.x < 0 || newPoint.y >= 8 || newPoint.y < 0) {
                    return null;
                }
                break;
            case 3: //moving in right diagonal
                int xd2 = currentQueen.x - Math.min(currentQueen.x, 8 - 1 - currentQueen.y);
                int yd2 = currentQueen.y + Math.min(currentQueen.x, currentQueen.y);
                newPoint.x = xd2 + position;
                newPoint.y = yd2 - position;
                if (newPoint.x >= 8 || newPoint.x < 0 || newPoint.y >= 8 || newPoint.y < 0) {
                    return null;
                }
                break;
        }
        return newPoint;
    }

    private boolean queenExists(Point[] queensPositions, Point newPoint) {
        for (int  i = 0; i < 8; i++) {
            if(queensPositions[i].x == newPoint.x && queensPositions[i].y == newPoint.y) {
                return true;
            }
        }
        return false;
    }

    private List<Point[]> generateRandomStatesFromInitial(Point[] state, int k) {
        int n = state.length;
        ArrayList<Point[]> k_states = new ArrayList<>();
        int costToBeat = Utils.getHeuristicCost(state);
        for (int queen = 0; queen < n; queen ++) {
            for (int direction = 0; direction < 4; direction++) {
                k_states.add(makeMove(state, queen,direction, costToBeat));
                // if stuck
                if (k_states.get(k_states.size()-1) == null)
                    k_states.set(k_states.size()-1, Utils.initializeQueensPosition());
            }
        }
        while (k_states.size() < k) {
            Point[] random_state = Utils.initializeQueensPosition();
            if(stateExists(random_state, k_states)) {
                System.out.println("state exist");
                continue;
            }
            k_states.add(random_state);
            numberOfBoardsExpanded++;
        }

        Collections.sort(k_states, Comparator.comparingInt(Utils::getHeuristicCost));
        return k_states.subList(0, k);
    }

    private boolean stateExists(Point[] random_state, ArrayList<Point[]> k_states) {
        for (int i = 0; i < k_states.size(); i++){
            boolean equal = true;
            for (int j = 0; j < random_state.length; j++) {
                if (random_state[j]!= k_states.get(i)[j]) {
                    equal = false;
                    break;
                }
            }
            if (equal)
                return true;
        }
        return false;
    }

    private static void print_result(Point[] result) {
        String[][] board = new String[8][8];
        for(int i=0; i<8; i++)
            for(int j=0; j<8; j++)
                board[i][j]="X ";
        //place the queens on the board
        for(int i=0; i<8; i++){
            board[result[i].x][result[i].y]="Q ";
        }
        //feed values into the result strin
        String out = "";
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                out+=board[i][j];
            }
            out+="\n";
        }
        System.out.println(out);
    }
    public int getSteps(){
        return steps;
    }
    public int getNumberOfBoardsExpanded() {
        return numberOfBoardsExpanded;
    }

    public double getRunning_time() {
        return running_time;
    }
}
