package model;

import model.streetpart.StreetPart;

import java.awt.*;

public class GridPart {

    private StreetPart streetPart;
    private Vehicle vehicle;
    private int localx,localy, globalx, globaly;

    GridPart(StreetPart streetPart, int localx, int localy, int globalx, int globaly){
        this.streetPart = streetPart;
        this.localx = localx; this.localy = localy;
        this.globalx = globalx; this.globaly = globaly;
    }

    public StreetPart getStreetPart() {
        return streetPart;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public synchronized boolean setVehicle(Vehicle vehicle) {
        if(vehicle == null){
            this.vehicle = null;
            return true;
        }
        if(this.vehicle == null) {
           this.vehicle = vehicle;
           vehicle.setGridPart(this);
           return true;
        }
        return false;
    }

    public int getLocalx() {
        return localx;
    }

    public int getLocaly() {
        return localy;
    }

    public int getGlobalx() {
        return globalx;
    }

    public int getGlobaly() {
        return globaly;
    }

    public Point getGlobalPoint(){
        return new Point(globalx, globaly);
    }

    public Point getLocalPoint(){
        return new Point(localx, localy);
    }

    public double getAvgStepsPerIteration() {
        return vehicle == null ? 0 : vehicle.getStatistics().getAvgStepsPerIteration();
    }

    public double getAvgStepsPerCycle() {
        return vehicle == null ? 0 : vehicle.getStatistics().getAvgStepsPerCycle();
    }

    public double getAvgIterationsPerCycle() {
        return vehicle == null ? 0 : vehicle.getStatistics().getAvgIterationsPerCycle();
    }

    public double getAvgTimePerCycle() {
        return vehicle == null ? 0 : vehicle.getStatistics().getAvgTimePerCycle();
    }

    public double getAvgTimePerIteration() {
        return vehicle == null ? 0 : vehicle.getStatistics().getAvgTimePerIteration();
    }
}

