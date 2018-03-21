package model;

public class Statistics {

    private int currentCycleIterationsAmount = 0;
    private long currentTimeInStep;
    private long currentTimeInCycle;

    private double avgStepsPerIteration = 0;
    private double avgStepsPerCycle;
    private double avgTimePerCycle = 0;
    private long avgTimePerIteration = 0;

    void update(boolean moved, boolean endOfCycle){
        if(moved){
            currentCycleIterationsAmount ++;
            if(endOfCycle){

            }
        }
    }

    public double getAvgStepsPerIteration() {
        return avgStepsPerIteration;
    }

    public double getAvgStepsPerCycle() {
        return avgStepsPerCycle;
    }

    public double getAvgTimePerCycle() {
        return avgTimePerCycle;
    }

    public long getAvgTimePerIteration() {
        return avgTimePerIteration;
    }
}
