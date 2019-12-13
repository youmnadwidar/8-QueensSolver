package HillClimbingAlgorithm;

import java.awt.*;
import java.util.Queue;

public class HillClimbingAlgorithm {
    private static final int MIN_SHUFFLE = 8;
    private static final int MAX_SHUFFLE = 20;
    private static final boolean QUEEN = true;
    private static final int QUEENS = 8;
    private static final int COLLISION = 1;
    private static final int NO_COLLISION = 0;
    private static final int MAX_COLLISIONS = 28;
    private static final int DIRECTIONS = 4;

    private Point[] queensPositions;
    boolean[][] game;
    private int numberOfRandomGeneratedStates;
    private double runningTime;
    private int numberOfBoardsVisited; //has less heuristic so it was chosen as next candidate
    private int numberOfBoardsExpanded; //just expanded but didn't have cost less than current so neglected

    public HillClimbingAlgorithm (boolean[][] game) {
        queensPositions = new Point[QUEENS];
        this.game = game;
        setQueensPosition();
        numberOfRandomGeneratedStates = 0;
    }

    public void run () {
        printSolution();
        double start = System.currentTimeMillis();
        if (isSolution()) {
            setupAnalysis(start);
            return;
        }
        solve();
        while (!isSolution()) {
            numberOfRandomGeneratedStates++;
            numberOfBoardsVisited++;
            generateRandomState();
            solve();
        }
        setupAnalysis(start);
    }

    private void setupAnalysis (double start) {
        runningTime = System.currentTimeMillis() - start;
        System.out.println("Running time : " + runningTime);
        System.out.println("Number of Random generated States : " + numberOfRandomGeneratedStates);
        System.out.println("Number of boards visited : " + numberOfBoardsVisited);
        System.out.println("Number of Nodes Expanded : " + numberOfBoardsExpanded);
        printSolution();
    }
    private void printSolution() {
        boolean[][] finalGame = new boolean[QUEENS][QUEENS];
        for (int i = 0; i < QUEENS; i++) {
            finalGame[queensPositions[i].x][queensPositions[i].y] = true;
        }
        for (int i = 0; i < QUEENS; i++) {
            for (int j = 0; j < QUEENS; j++) {
                System.out.print(finalGame[i][j]? "Q" : "#");
            }
            System.out.println();
        }
    }

    private void generateRandomState() {
        initializeQueensPosition();
        shuffle();
    }

    private void initializeQueensPosition() {
        for (int i = 0; i < QUEENS; i++) {
            queensPositions[i] = new Point(i,i);
        }
    }

    private void shuffle() {
        int shuffling = (int)getRandom(MIN_SHUFFLE, MAX_SHUFFLE);
        for (int i = 0; i < shuffling; i++) {
            //choose which columns to switch their rows.
            int firstQueen = (int)getRandom(0,QUEENS - 1);
            int secondQueen = (int)getRandom(0,QUEENS - 1, firstQueen);
            //swap positions
            Point temp = queensPositions[firstQueen];
            queensPositions[firstQueen] = new Point(queensPositions[secondQueen].x, firstQueen);
            queensPositions[secondQueen] = new Point(temp.x, secondQueen);
        }
    }

    private int getRandom(int minBound, int maxBound) {
        return (int)(Math.random() * ((maxBound - minBound) + 1)) + minBound;
    }

    private int getRandom(int minBound,int maxBound, int except) {
        int number = 0;
        while (true) {
            number = (int)(Math.random() * ((maxBound - minBound) + 1)) + minBound;
            if (number != except) {
                return number;
            }
        }
    }

    private void solve () {
        while (true) {
            Point[] newPositions = getBestCandidate();
            //apply one of them if heuristic it less than before
            if (getHeuristic(newPositions) < getHeuristic(queensPositions)) {
                queensPositions = newPositions;
                numberOfBoardsVisited++;
            } else {
                break;
            }
        }
    }

    private Point[] getBestCandidate() {
        int bestHeuristic = MAX_COLLISIONS;
        Point[] newPositions = null;
        Point bestNewState = new Point();
        int bestQueenToMove = 0;

        //for each of the 8 queens do the following
        for (int q = 0; q < QUEENS; q++) {
            Point currentQueen = queensPositions[q];
            //get positions for all row, col, diagonal1, diagonal2
            for (int d = 0; d < DIRECTIONS; d++) {
                //loop on the 8 cells for each direction
                for (int p = 0; p < QUEENS; p++) {
                    Point newPoint = getNewPosition(d, p, currentQueen);
                    //if a queen already exists in same position skip
                    if (newPoint == null || queenExists(queensPositions, newPoint)) {
                        continue;
                    }
                    numberOfBoardsExpanded++;
                    int currentHeuristic = getHeuristic(queensPositions, q, newPoint);
                    if (currentHeuristic < bestHeuristic) {
                        //could be optimized
                        //newPositions = copy(queensPositions);
                        //newPositions[q] = newPoint;
                        bestQueenToMove = q;
                        bestNewState = newPoint;
                        bestHeuristic = currentHeuristic;
                    }
                }
            }
        }
        newPositions = copy(queensPositions);
        newPositions[bestQueenToMove] = bestNewState;
        return newPositions;
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
                if (newPoint.x >= QUEENS || newPoint.x < 0 || newPoint.y >= QUEENS || newPoint.y < 0) {
                    return null;
                }
                break;
            case 3: //moving in right diagonal
                int xd2 = currentQueen.x - Math.min(currentQueen.x, QUEENS - 1 - currentQueen.y);
                int yd2 = currentQueen.y + Math.min(currentQueen.x, currentQueen.y);
                newPoint.x = xd2 + position;
                newPoint.y = yd2 - position;
                if (newPoint.x >= QUEENS || newPoint.x < 0 || newPoint.y >= QUEENS || newPoint.y < 0) {
                    return null;
                }
                break;
        }
        return newPoint;
    }

    private boolean queenExists(Point[] queensPositions, Point newPoint) {
        for (int  i = 0; i < QUEENS; i++) {
           if(queensPositions[i].x == newPoint.x && queensPositions[i].y == newPoint.y) {
               return true;
           }
        }
        return false;
    }

    private Point[] copy(Point[] queensPositions) {
        Point[] newPositions = new Point[QUEENS];
        for (int  i = 0; i < QUEENS; i++) {
            newPositions[i] = new Point(queensPositions[i].x, queensPositions[i].y);
        }
        return newPositions;
    }

    private int getHeuristic (Point[] queensPositions, int q, Point queenNewPosition) {
        int totalCollisions = 0;
        for (int i = 0; i < QUEENS; i++) {
            Point queen1 = queensPositions[i];
            if (i == q) {
                queen1 = queenNewPosition;
            }
            for (int j = i + 1; j < QUEENS; j++) {
                Point queen2 = queensPositions[j];
                if (j == q) {
                    queen2 = queenNewPosition;
                }
                totalCollisions += columnCollision(queen1, queen2)
                        + rowCollision(queen1, queen2)
                        + diagonalCollision(queen1, queen2);
            }
        }
        return totalCollisions;
    }

    private int getHeuristic (Point[] queensPositions) {
        int totalCollisions = 0;
        for (int i = 0; i < QUEENS; i++) {
            for (int j = i + 1; j < QUEENS; j++) {
                totalCollisions += columnCollision(queensPositions[i], queensPositions[j])
                                + rowCollision(queensPositions[i], queensPositions[j])
                                + diagonalCollision(queensPositions[i], queensPositions[j]);
            }
        }
        return totalCollisions;
    }

    private int diagonalCollision(Point queen1, Point queen2) {
        return Math.abs(queen1.x - queen2.x)
                == Math.abs(queen1.y - queen2.y)
                ? COLLISION : NO_COLLISION;
    }

    private int rowCollision(Point queen1, Point queen2) {
        return queen1.x == queen2.x ? COLLISION : NO_COLLISION;
    }

    private int columnCollision(Point queen1, Point queen2) {
        return queen1.y == queen2.y ? COLLISION : NO_COLLISION;
    }

    private boolean isSolution () {
        if (getHeuristic(queensPositions) == 0) {
            return true;
        }
        return false;
    }

    private void setQueensPosition () {
        int currentPosition = 0;
        for (int i = 0; i < QUEENS; i++) {
            for (int j = 0; j < QUEENS; j++) {
                if (game[j][i] == QUEEN) {
                    //i is the columns and j is the rows
                    queensPositions[currentPosition++] = new Point(j, i);
                }
            }
        }
    }

    public int getNumberOfRandomGeneratedStates() {
        return numberOfRandomGeneratedStates;
    }
}
