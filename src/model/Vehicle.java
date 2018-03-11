package model;

import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Steppable {


    private ArrayList<StreetPart.DIRECTION> route;
    private List<Point> streetPartsRoute;
    private int currentPosition = 0;
    private TravelPoint source;
    private TravelPoint target;
    private GridPart gridPart;


    void setSource(TravelPoint source){
        this.source = source;
    }

    @Override
    public void step(SimState simState) {
        if(currentPosition < streetPartsRoute.size() - 1)
            if(gridPart.getStreetPart().tryPerformMove(streetPartsRoute.get(currentPosition), streetPartsRoute.get(currentPosition + 1)))
                currentPosition ++;
        
        System.out.println("======");
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println(
            streetPartsRoute.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) ->  agg + "; " + s)
        );
        System.out.println("======");
    }

    private void resetRoute(){
        route = new ArrayList<>();
        currentPosition = 0;
        target = gridPart.getStreetPart().getTraffic().getRandomTargetTravelPoint(source);
        streetPartsRoute = gridPart.getStreetPart().getTraffic().mountRouteFromTo(source, target);
    }


    public void calculateRoute(){
        resetRoute();
    }

    public StreetPart.DIRECTION getCurrentDirection(){
        return route.get(currentPosition);
    }

    void setGridPart(GridPart gridPart){
        this.gridPart = gridPart;
    }
}
