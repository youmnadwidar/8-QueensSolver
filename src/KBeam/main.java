package KBeam;

import java.awt.*;

public class main {
    public static void main2 (String[] args) {
        char[][] s = {{'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q'},
                {'X' ,'X', 'X', 'X' ,'X' ,'X' ,'X' ,'X'},
                {'X' ,'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X' ,'X' ,'X' ,'X' ,'X' ,'X' ,'X' ,'X'},
                {'X' ,'X' ,'X', 'X', 'X', 'X', 'X', 'X'},
                {'X' ,'X' ,'X' ,'X' ,'X' ,'X' ,'X' ,'X'},
                {'X' ,'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X' ,'X' ,'X' ,'X', 'X' ,'X' ,'X' ,'X'} };
        LocalBeam localBeam = new LocalBeam(s);


        Point[] result = localBeam.run(8,100,  100);
        System.out.println(Utils.getHeuristicCost(result));
//        print_result(result);
        System.out.println("#Of steps : " + localBeam.getSteps());
        System.out.println("#Of expanded : " + localBeam.getNumberOfBoardsExpanded());
        System.out.println("Running Time(ms) : " + localBeam.getRunning_time());
    }


}
