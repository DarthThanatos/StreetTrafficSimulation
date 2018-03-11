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

    public abstract void addVehicle(Vehicle vehicle, DIRECTION from, DIRECTION direction);

    public abstract List<GridPart> getExitPoints();

    public abstract void moveVehicle(Vehicle vehicle, DIRECTION direction);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setGridsInStreetPart(GridPart[][] gridsInStreetPart) {
        this.gridsInStreetPart = gridsInStreetPart;
    }

    public void initVehicles() {
        for(Vehicle vehicle: vehiclesByLocation.values()){
            vehicle.calculateRoute();
        }
    }

    public abstract void initRouteNodeFrom(Map<Point, RouteNode> routeGraph);

    public enum DIRECTION{NORT, EAST, SOUTH, WEST};

    public Set<Point> getVehiclesPositions(){
        return vehiclesByLocation.keySet();
    }

    public Traffic getTraffic() {
        return traffic;
    }

    public boolean tryPerformMove(Point current, Point target){
     return false;
    }

}
