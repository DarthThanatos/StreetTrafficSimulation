package model;

import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import sim.engine.SimState;
import sim.engine.Steppable;
import utils.DirectionUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Vehicle implements Steppable {


    private List<Point> streetPartsRoute;
    private int currentStreetPartInPath = 0;

    private List<Point> localRouteMoves;
    private int currentLocalRouteMove = 0;

    private TravelPoint source;
    private TravelPoint target;

    private GridPart gridPart;

    void setSource(TravelPoint source){
        this.source = source;
        this.gridPart = source.gridPart;
    }

    TravelPoint getSource(){
        return source;
    }

    public TravelPoint getTarget(){
        return target;
    }

    @Override
    public void step(SimState simState) {
        if(gridPart == source.gridPart && !source.pointReached){
            tryToReachSource();
        }
        else{
            normalStep();
        }
    }

    private void tryToReachSource(){
        if(source.streetPart.addedVehicle(
                this,
                DirectionUtils.localPointToDirection(source.gridPart.getLocalPoint())
        )){
            source.pointReached = true;
            resetRoute();
            setupCurrentLocalRouteMoves();
            log();
        }
    }


    private void setupCurrentLocalRouteMoves(){
//        if(currentStreetPartInPath == streetPartsRoute.size() -1) return;
        Point currentStreet = streetPartsRoute.get(currentStreetPartInPath);
        StreetPart.DIRECTION from = DirectionUtils.localPointToDirection(gridPart.getLocalPoint());
        System.out.println(from);
        Point nextStreet = (currentStreetPartInPath + 1) != streetPartsRoute.size() ? streetPartsRoute.get(currentStreetPartInPath + 1) : null;
        StreetPart.DIRECTION to = nextStreet != null ? DirectionUtils.directionOfFirstComparedToSecond(nextStreet, currentStreet) : DirectionUtils.localPointToDirection(target.gridPart.getLocalPoint());
        localRouteMoves = gridPart.getStreetPart().streetPartMoves(from, to);
        currentLocalRouteMove = 0;

        System.out.println(currentStreet + " -> " + (nextStreet != null ? nextStreet : target.gridPart.getLocalPoint()) + ", local: " + gridPart.getLocalPoint());
        System.out.println(
                localRouteMoves.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) ->  agg + "; " + s) + "\n"
        );
    }

    private void normalStep() {
        if(currentLocalRouteMove < localRouteMoves.size() - 1) {
            if (gridPart.getStreetPart().movedVehicleLocally(
                        this,
                        localRouteMoves.get(currentLocalRouteMove),
                        localRouteMoves.get(currentLocalRouteMove + 1))) {
                currentLocalRouteMove++;
//                System.out.println("Moved locally: " + currentLocalRouteMove);
            }
        }
        else{
             if(currentStreetPartInPath < streetPartsRoute.size() - 1){
                 if(gridPart.getStreetPart().getTraffic().movedVehicleAcrossStreets(
                         this,
                         streetPartsRoute.get(currentStreetPartInPath),
                         streetPartsRoute.get(currentStreetPartInPath + 1),
                         gridPart.getLocalPoint()
                 )) {
                     currentStreetPartInPath ++;
//                     System.out.println("Moved globally: " + currentStreetPartInPath + " curentSPIP: " + currentStreetPartInPath + ", size: " + (streetPartsRoute.size() - 1));
                     setupCurrentLocalRouteMoves();
                 }
             }
             else{
                 gridPart.getStreetPart().getTraffic().endVehicleCycle(this);
             }
        }
//        log();

    }

    private void log(){
        System.out.println("======");
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println(
                streetPartsRoute.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) ->  agg + "; " + s)
        );
        System.out.println(
                localRouteMoves.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) ->  agg + "; " + s)
        );
        System.out.println("current local: " + currentLocalRouteMove);
        System.out.println("current street: " + currentStreetPartInPath);
        System.out.println("======");
    }

    private void resetRoute(){
        currentStreetPartInPath = 0;
        target = gridPart.getStreetPart().getTraffic().getRandomTargetTravelPoint(source);
        streetPartsRoute = gridPart.getStreetPart().getTraffic().mountRouteFromTo(source, target);
    }

    void setGridPart(GridPart gridPart){
        this.gridPart = gridPart;
    }

    public GridPart getGridPart() {
        return gridPart;
    }

    public Point[] getLocalMovesFromCurrent(){
        return Arrays.copyOfRange(localRouteMoves.toArray(new Point[localRouteMoves.size()]), currentLocalRouteMove, localRouteMoves.size());
    }
}
