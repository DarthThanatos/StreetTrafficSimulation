package model.streetpart;

import model.GridPart;
import model.RouteNode;
import model.Traffic;
import model.Vehicle;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class StreetPart{

    private Traffic traffic;
    HashMap<Point, Vehicle> vehiclesByLocation;
    GridPart[][] gridsInStreetPart;

    int x,y;

    StreetPart(Traffic traffic, int x, int  y){
        this.traffic = traffic;
        this.x = x; this.y=y;
        vehiclesByLocation = new HashMap<>();
    }



    public final synchronized boolean movedVehicleLocally(Vehicle vehicle, Point from, Point to) {
        if(waitingAtCross(from)){
            return false;
        }
        else{
            boolean res = gridsInStreetPart[to.y][to.x].setVehicle(vehicle);
            if(res){
                gridsInStreetPart[from.y][from.x].setVehicle(null);
                vehiclesByLocation.remove(from);
                vehiclesByLocation.put(to, vehicle);
            }
            return res;
        }
    }

    public abstract List<GridPart> getSourcePoints();
    public abstract List<GridPart> getExitPoints();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setGridsInStreetPart(GridPart[][] gridsInStreetPart) {
        this.gridsInStreetPart = gridsInStreetPart;
    }

    public abstract void initRouteNodeHaving(Map<Point, RouteNode> routeGraph);


    public enum DIRECTION{NORTH, EAST, SOUTH, WEST};

    public Set<Point> getVehiclesPositions(){
        return vehiclesByLocation.keySet();
    }

    public Traffic getTraffic() {
        return traffic;
    }


    public abstract boolean addedVehicle(Vehicle vehicle, DIRECTION from);
    public abstract void removeVehicleAtTarget(Vehicle vehicle);
    public abstract void removeVehicleAt(Point localPosition);

    public boolean waitingAtCross(Point from){
        return false;
    }

    public abstract List<Point> streetPartMoves(DIRECTION from, DIRECTION to);


}
