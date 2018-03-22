package model;

class Statistics {

    private int allStepsNumber = 0;
    private int allIterationsNumber = 0;
    private long allCyclesNumber = 0;

    private long totalTimeSpentInIterations = 0;
    private long totalTimeSpentInCycles = 0;

    private long currentIterationStartTime = System.currentTimeMillis();
    private long currentCycleStartTime = System.currentTimeMillis();

    private double avgStepsPerIteration = 0;
    private double avgStepsPerCycle = 0;
    private double avgIterationsPerCycle = 0;

    private double avgTimePerCycle = 0;
    private double avgTimePerIteration = 0;

    void update(boolean moved, boolean endOfCycle){
        allStepsNumber++;
        if(moved){
            long currentIterationEndTime = System.currentTimeMillis();
            totalTimeSpentInIterations += currentIterationEndTime - currentIterationStartTime;
            currentIterationStartTime = currentIterationEndTime; // the end-time of the previous cycle is the start-time of a new cycle
            allIterationsNumber++;
            avgTimePerIteration = totalTimeSpentInIterations / allIterationsNumber;
            avgStepsPerIteration = allStepsNumber / allIterationsNumber;
        }
        if(endOfCycle){
            long currentCycleEndTime = System.currentTimeMillis();
            totalTimeSpentInCycles += currentCycleEndTime - currentCycleStartTime;
            currentCycleStartTime = currentCycleEndTime; // the end-time of the previous cycle is the start-time of a new cycle
            allCyclesNumber++;
            avgTimePerCycle = totalTimeSpentInCycles / allCyclesNumber;
            avgStepsPerCycle = allStepsNumber / allCyclesNumber;
            avgIterationsPerCycle = allIterationsNumber / allCyclesNumber;
        }
    }

    double getAvgStepsPerIteration() {
        return avgStepsPerIteration;
    }

    double getAvgStepsPerCycle() {
        return avgStepsPerCycle;
    }

    double getAvgIterationsPerCycle() {
        return avgIterationsPerCycle;
    }

    double getAvgTimePerCycle() {
        return avgTimePerCycle;
    }

    double getAvgTimePerIteration() {
        return avgTimePerIteration;
    }
}
