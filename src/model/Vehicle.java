package model;

import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;

public class Vehicle implements Steppable {


    private ArrayList<StreetPart.DIRECTION> route;
    private int currentPosition = 0;
    private TravelPoint source;
    private TravelPoint target;
    private GridPart gridPart;


    public void setSource(TravelPoint source){
        this.source = source;
    }

    @Override
    public void step(SimState simState) {
        System.out.println("======");
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("======");
    }

    private void resetRoute(){
        route = new ArrayList<>();
        currentPosition = 0;
        target = gridPart.getStreetPart().getTraffic().getRandomTargetTravelPoint(source);
    }


    public void calculateRoute(){
        resetRoute();
    }

    public void move(){
        currentPosition ++;
    }

    public StreetPart.DIRECTION getCurrentDirection(){
        return route.get(currentPosition);
    }

    public void setGridPart(GridPart gridPart){
        this.gridPart = gridPart;
    }
}
