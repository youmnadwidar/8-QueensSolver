package KBeam;

import java.awt.*;
import java.util.ArrayList;

public class Utils {
    public static int[] generateRandomStates(int size){

        return randomizeState(generateAllOneState(size));
    }


    public static int[] generateAllOneState(int n) {

        return new int[n];
    }

    // Randomizes state
    public static int[] randomizeState(int[] r) {

        for (int i = 0; i < r.length; i++)
            r[i] = (int) (Math.random() * r.length);

        return r;
    }
    public static Point[] initializeQueensPosition() {
        Point[] queensPositions = new Point[8];
        for (int i = 0; i < 8; i++) {
            queensPositions[i] = (new Point(i,i));
        }
       return shuffle(queensPositions);
    }

    private static Point[] shuffle(Point[] queensPositions) {
        int shuffling = (int)getRandom(8, 20);
        for (int i = 0; i < shuffling; i++) {
            //choose which columns to switch their rows.
            int firstQueen = (int)getRandom(0,8 - 1);
            int secondQueen = (int)getRandom(0,8 - 1, firstQueen);
            //swap positions
            Point temp = queensPositions[firstQueen];
            queensPositions[firstQueen] = new Point(queensPositions[secondQueen].x, firstQueen);
            queensPositions[secondQueen] = new Point(temp.x, secondQueen);
        }
        return queensPositions;
    }

    private static int getRandom(int minBound, int maxBound) {
        return (int)(Math.random() * ((maxBound - minBound) + 1)) + minBound;
    }

    private static int getRandom(int minBound, int maxBound, int except) {
        int number = 0;
        while (true) {
            number = (int)(Math.random() * ((maxBound - minBound) + 1)) + minBound;
            if (number != except) {
                return number;
            }
        }
    }




    // Returns heuristic cost
    public static int getHeuristicCost(Point[] r) {
        int h = 0;

        // increment cost if two queens are in same row or in same diagonal.
        for (int i = 0; i < r.length; i++){
            for (int j = i + 1; j < r.length; j++){
                if (r[i].x == r[j].x)
                    h += 1;
                if (r[i].y == r[j].y)
                    h+=1;
                if (Math.abs(r[i].x - r[j].x) == Math.abs(r[i].y - r[j].y))
                    h+=1;
            }
        }

        return h;
    }






}
