package GeneticAlgorithm;

import java.util.*;

public class GeneticAlgorithm {
    private static final int QUEENS = 8;
    private static final int MAX_CONFLICTS = 28;
    private static final int START_POPULATION = 100;
    private static final int MIN_SHUFFLE = 8;
    private static final int MAX_SHUFFLE = 20;
    private static final double MAX_PARENTS = 50;
    private static final int MAX_ITR = 5000;
    private static final int NEW_GENERATION = 25;
    private static final int OFFSPRING_SWAP =3;

    private float totalFitness;
    private ArrayList<Chromosome> population;
    private boolean gameSolved;
    private double runningTime;
    private int numberOfBoardsVisited;
    private int numberOfBoardsExpanded;

    private Set<String> populationSet;

    public GeneticAlgorithm (int[] initialState) {
        population = new ArrayList<>();
        populationSet = new HashSet<>();
        gameSolved = false;
        Chromosome initial = new Chromosome();
        initial.setData(initialState);
        population.add(initial);
    }

    public void run () {
        double start = System.currentTimeMillis();
        int itr = 0;
        initializePopulation();
        while(true) {
            totalFitness = 0;
            numberOfBoardsVisited++;
            setFitness();
            if (gameSolved) {
                break;
            }
            //setSurvivalProbability();
            selectParents();
            generateOffspring();
            itr++;
            if (itr == MAX_ITR) {
                break;
            }
        }
        setupAnalysis(getMaximumFitness(), start);
    }

    private void setupAnalysis (int solutionIndex, double start) {
        runningTime = System.currentTimeMillis() - start;
        System.out.println("Running time : " + runningTime);
        System.out.println("Number of boards visited : " + numberOfBoardsVisited);
        System.out.println("Number of Nodes Expanded : " + numberOfBoardsExpanded);
        printSolution(solutionIndex);
    }
    private int getMaximumFitness() {
        int maxFitness = 0;
        int solutionIndex = 0;
        for (int i = 0; i < population.size(); i++) {
            int currentFitness = population.get(i).getFitness();
            if (currentFitness > maxFitness) {
                maxFitness = currentFitness;
                solutionIndex = i;
            }
        }
        return solutionIndex;
    }

    public void printSolution (int index) {
        Chromosome chromosome = population.get(index);
        int[] data = chromosome.getData();

        for (int i = 0; i < QUEENS; i++) {
            System.out.print(data[i] + " ");
        }
    }
    private void setSurvivalProbability() {
        for (int i = 0; i < population.size(); i++) {
            Chromosome chromo = population.get(i);
            chromo.setSurvivalProbability(chromo.getFitness() / totalFitness);
        }
    }

    private void generateOffspring() {
        int parentIndex = 0;
        for (int i = 0; i < NEW_GENERATION; i++) {
            //Pick 3 random positions in the parents
            int[] pos = new int[OFFSPRING_SWAP];

            for (int j = 0; j < OFFSPRING_SWAP; j++) {
                pos[j] = -1;
            }
            for (int j = 0; j < OFFSPRING_SWAP; j++) {
                pos[j] = getRandom(0,QUEENS - 1, pos);
            }

            Chromosome child1 = new Chromosome();
            Chromosome child2 = new Chromosome();

            child1.setData(population.get(parentIndex).copyData());
            child2.setData(population.get(parentIndex + 1).copyData());

            for (int j = 0; j < OFFSPRING_SWAP; j++) {
                int num1 = child1.getData()[pos[j]];
                int num2 = child2.getData()[pos[j]];
                child1.swapValues(num1, num2);
                child2.swapValues(num1, num2);
            }
            if (!populationSet.contains(child1.toString())) {
                population.add(child1);
                populationSet.add(child1.toString());
            }
            if (!populationSet.contains(child2.toString())) {
                population.add(child2);
                populationSet.add(child2.toString());
            }
            parentIndex += 2;
        }
    }

    private void selectParents() {
        Collections.sort(population, Comparator.comparingInt(o -> o.getFitness()));
        for (int i = 0; i < MAX_PARENTS; i++) {
            populationSet.remove(population.get(0));
            population.remove(0);
        }
    }

    private void setFitness() {
        for (int i = 0; i < population.size(); i++) {
            Chromosome chromo = population.get(i);
            chromo.calculateConflict();
            if (chromo.getConflicts() == 0) {
                gameSolved = true;
            }
            chromo.setFitness(MAX_CONFLICTS - chromo.getConflicts());
            numberOfBoardsExpanded++;
            totalFitness += chromo.getFitness();
        }
    }

    private void initializePopulation() {
        for (int i = 0; i < START_POPULATION; i++) {
            Chromosome chromo = new Chromosome();
            int shuffling = (int)getRandom(MIN_SHUFFLE, MAX_SHUFFLE);
            shuffle(chromo, shuffling);
            if (populationSet.contains(chromo.toString())) {
                i--;
                continue;
            }
            populationSet.add(chromo.toString());
            population.add(chromo);
        }
    }

    private void shuffle(Chromosome chromo, int shuffling) {
        for (int i = 0; i < shuffling; i++) {
            int firstPosition = (int)getRandom(0,QUEENS - 1);
            int secondPosition = (int)getRandom(0,QUEENS - 1, firstPosition);
            chromo.swapIndices(firstPosition, secondPosition);
        }
    }

    private double getRandom(double minBound, double maxBound) {
        return (Math.random() * ((maxBound - minBound) + 1)) + minBound;
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

    private int getRandom(int minBound,int maxBound, int[] except) {
        int number = 0;
        boolean found;
        while (true) {
            found = true;
            number = (int)(Math.random() * ((maxBound - minBound) + 1)) + minBound;
            for (int i = 0; i < except.length; i++) {
                if (number == except[i]) {
                    found = false;
                }
            }
            if (found) {
                return number;
            }
        }
    }

    public ArrayList<Chromosome> getPopulation () {
        return population;
    }
}
