package GeneticAlgorithm;

public class Chromosome {
    private static final int QUEENS = 8;
    private int[] data;
    private int conflicts;
    private int fitness;
    private float survivalProbability;
    private String dataString;

    public Chromosome () {
        data = new int[QUEENS];
        for (int i = 0; i < QUEENS; i++) {
            data[i] = i;
        }
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int getConflicts() {
        return conflicts;
    }

    public void calculateConflict() {
        conflicts = 0;

        for (int i = 0; i < QUEENS; i++) {
            for (int j = i+1; j < QUEENS; j++) {
                //Check for diagonal conflicts.
                if (Math.abs(i-j) == Math.abs(data[i] - data[j])) {
                    conflicts++;
                }
                //Check for horizontal conflicts.
                if (data[i] == data[j]) {
                    conflicts++;
                }
            }
        }
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public float getSurvivalProbability() {
        return survivalProbability;
    }

    public void setSurvivalProbability(float survivalProbability) {
        this.survivalProbability = survivalProbability;
    }

    public void updateData (int index, int value) {
        data[index] = value;
    }

    public void swapIndices (int index1, int index2) {
        int temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    public void swapValues (int value1, int value2) {
        for (int i = 0; i < QUEENS; i++) {
            if (data[i] == value1) {
                data[i] = value2;
                continue;
            }
            if (data[i] == value2) {
                data[i] = value1;
            }
        }
    }

    public int[] copyData () {
        int[] copiedData = new int[QUEENS];

        for (int i = 0; i < QUEENS; i++) {
            copiedData[i] = data[i];
        }
        return copiedData;
    }
    @Override
    public String toString () {
        if (dataString != null) {
            return dataString;
        }
        StringBuffer dataStr = new StringBuffer();
        for (int i = 0; i < QUEENS; i++) {
            dataStr.append(data[i]);
        }
        dataString = dataStr.toString();
        return dataString;
    }
}
