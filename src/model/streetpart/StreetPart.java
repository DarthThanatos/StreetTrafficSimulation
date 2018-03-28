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



    List<Point> streetCoordsNorthToSouth(){
        return Arrays.asList(
                new Point(3,0),
                new Point(3,1),
                new Point(3,2),
                new Point(3,3),
                new Point(3,4),
                new Point(3,5),
                new Point(3,6),
                new Point(3,7)
        );
    }

    List<Point> streetCoordsNorthToWest(){
        return Arrays.asList(
                new Point(3,0),
                new Point(3,1),
                new Point(3,2),
                new Point(3,3),
                new Point(2,3),
                new Point(1,3),
                new Point(0,3)
        );

    }

    List<Point> streetCoordsNorthToEast(){
        return Arrays.asList(
                new Point(3,0),
                new Point(3,1),
                new Point(3,2),
                new Point(3,3),
                new Point(3,4),
                new Point(4,4),
                new Point(5,4),
                new Point(6,4),
                new Point(7,4)
        );

    }

    List<Point> streetCoordsSouthToNorth(){
        return Arrays.asList(
                new Point(4,7),
                new Point(4,6),
                new Point(4,5),
                new Point(4,4),
                new Point(4,3),
                new Point(4,2),
                new Point(4,1),
                new Point(4,0)
        );
    }

    List<Point> streetCoordsSouthToEast(){
        return Arrays.asList(
                new Point(4,7),
                new Point(4,6),
                new Point(4,5),
                new Point(4,4),
                new Point(5,4),
                new Point(6,4),
                new Point(7,4)
        );
    }

    List<Point> streetCoordsSouthToWest(){
        return Arrays.asList(
                new Point(4,7),
                new Point(4,6),
                new Point(4,5),
                new Point(4,4),
                new Point(4,3),
                new Point(3,3),
                new Point(2,3),
                new Point(1,3),
                new Point(0,3)
        );
    }

    List<Point> streetCoordsEastToNorth(){
        return Arrays.asList(
                new Point(7,3),
                new Point(6,3),
                new Point(5,3),
                new Point(4,3),
                new Point(4,2),
                new Point(4,1),
                new Point(4,0)
        );
    }

    List<Point> streetCoordsEastToSouth(){
        return Arrays.asList(
                new Point(7,3),
                new Point(6,3),
                new Point(5,3),
                new Point(4,3),
                new Point(3,3),
                new Point(3,4),
                new Point(3,5),
                new Point(3,6),
                new Point(3,7)
        );
    }

    List<Point> streetCoordsEastToWest(){
        return Arrays.asList(
                new Point(7,3),
                new Point(6,3),
                new Point(5,3),
                new Point(4,3),
                new Point(3,3),
                new Point(2,3),
                new Point(1,3),
                new Point(0,3)
        );
    }

    List<Point> streetCoordsWestToNorth(){
        return Arrays.asList(
                new Point(0,4),
                new Point(1,4),
                new Point(2,4),
                new Point(3,4),
                new Point(4,4),
                new Point(4,3),
                new Point(4,2),
                new Point(4,1),
                new Point(4,0)
        );
    }

    List<Point> streetCoordsWestToSouth(){
        return Arrays.asList(
                new Point(0,4),
                new Point(1,4),
                new Point(2,4),
                new Point(3,4),
                new Point(3,5),
                new Point(3,6),
                new Point(3,7)
        );
    }

    List<Point> streetCoordsWestToEast(){
        return Arrays.asList(
                new Point(0,4),
                new Point(1,4),
                new Point(2,4),
                new Point(3,4),
                new Point(4,4),
                new Point(5,4),
                new Point(6,4),
                new Point(7,4)
        );
    }

}
