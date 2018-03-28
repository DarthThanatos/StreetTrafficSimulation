package model.streetpart;

import model.GridPart;
import model.RouteNode;
import model.Traffic;
import model.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Crossroads extends StreetPart {

    public Crossroads(Traffic traffic, int x, int y) {
        super(traffic, x, y);
    }

    @Override
    public List<GridPart> getSourcePoints() {
        List<GridPart> res = new ArrayList<>();
        if(x == 0 && y == 0){
            res.add(gridsInStreetPart[0][3]);
            res.add(gridsInStreetPart[4][0]);
        }
        else if((this.x == 0) && (this.y == Traffic.ROWS - 1)){
            res.add(gridsInStreetPart[4][0]);
            res.add(gridsInStreetPart[7][4]);
        }
        else if((this.x == Traffic.COLUMNS - 1) && (this.y == 0)){
            res.add(gridsInStreetPart[0][3]);
            res.add(gridsInStreetPart[3][7]);
        }
        else if((this.x == Traffic.COLUMNS - 1) && (this.y == Traffic.ROWS - 1)){
            res.add(gridsInStreetPart[7][4]);
            res.add(gridsInStreetPart[3][7]);
        }
        else if(x == 0){
            res.add(gridsInStreetPart[4][0]);
        }
        else if(x == Traffic.COLUMNS - 1){
            res.add(gridsInStreetPart[3][7]);
        }
        else if(y == Traffic.ROWS - 1){
            res.add(gridsInStreetPart[7][4]);
        }
        else if(y == 0){
            res.add(gridsInStreetPart[0][3]);
        }
        return res;
    }

    @Override
    public List<GridPart> getExitPoints() {
        List<GridPart> res = new ArrayList<>();
        if(x == 0 && y == 0){
            res.add(gridsInStreetPart[0][4]);
            res.add(gridsInStreetPart[3][0]);
        }
        else if((this.x == 0) && (this.y == Traffic.ROWS - 1)){
            res.add(gridsInStreetPart[3][0]);
            res.add(gridsInStreetPart[7][3]);
        }
        else if((this.x == Traffic.COLUMNS - 1) && (this.y == 0)){
            res.add(gridsInStreetPart[0][4]);
            res.add(gridsInStreetPart[4][7]);
        }
        else if((this.x == Traffic.COLUMNS - 1) && (this.y == Traffic.ROWS - 1)){
            res.add(gridsInStreetPart[7][3]);
            res.add(gridsInStreetPart[4][7]);
        }
        else if(x == 0){
            res.add(gridsInStreetPart[3][0]);
        }
        else if(x == Traffic.COLUMNS - 1){
            res.add(gridsInStreetPart[4][7]);
        }
        else if(y == Traffic.ROWS - 1){
            res.add(gridsInStreetPart[7][3]);
        }
        else if(y == 0){
            res.add(gridsInStreetPart[0][4]);
        }
        return res;
    }

    private List<Point> streetCoordsNorthToSouth(){
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

    private List<Point> streetCoordsNorthToWest(){
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

    private List<Point> streetCoordsNorthToEast(){
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

    private List<Point> streetCoordsSouthToNorth(){
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

    private List<Point> streetCoordsSouthToEast(){
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

    private List<Point> streetCoordsSouthToWest(){
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

    private List<Point> streetCoordsEastToNorth(){
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

    private List<Point> streetCoordsEastToSouth(){
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

    private List<Point> streetCoordsEastToWest(){
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

    private List<Point> streetCoordsWestToNorth(){
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

    private List<Point> streetCoordsWestToSouth(){
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

    private List<Point> streetCoordsWestToEast(){
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

    @Override
    public List<Point> streetPartMoves(DIRECTION from, DIRECTION to) {
        System.out.println("MOving from " + from + " to "+ to);
        if(from == DIRECTION.NORTH && to == DIRECTION.SOUTH) return streetCoordsNorthToSouth();
        if(from == DIRECTION.NORTH && to == DIRECTION.EAST) return streetCoordsNorthToEast();
        if(from == DIRECTION.NORTH && to == DIRECTION.WEST) return streetCoordsNorthToWest();
        if(from == DIRECTION.SOUTH && to == DIRECTION.NORTH) return streetCoordsSouthToNorth();
        if(from == DIRECTION.SOUTH && to == DIRECTION.WEST) return streetCoordsSouthToWest();
        if(from == DIRECTION.SOUTH && to == DIRECTION.EAST) return streetCoordsSouthToEast();
        if(from == DIRECTION.EAST && to == DIRECTION.SOUTH) return streetCoordsEastToSouth();
        if(from == DIRECTION.EAST && to == DIRECTION.NORTH) return streetCoordsEastToNorth();
        if(from == DIRECTION.EAST && to == DIRECTION.WEST) return streetCoordsEastToWest();
        if(from == DIRECTION.WEST && to == DIRECTION.SOUTH) return streetCoordsWestToSouth();
        if(from == DIRECTION.WEST && to == DIRECTION.NORTH) return streetCoordsWestToNorth();
        if(from == DIRECTION.WEST && to == DIRECTION.EAST) return streetCoordsWestToEast();
        return new ArrayList<>();
    }

    @Override
    public void initRouteNodeHaving(Map<Point, RouteNode> routeGraph) {
        RouteNode routeNode = routeGraph.get(new Point(x, y));
        Point key;
        if(x - 1 >= 0){
            key  = new Point (x-1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if(x+1 < Traffic.COLUMNS){
            key = new Point(x+1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if(y-1 >= 0){
            key  = new Point (x, y-1);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if(y+1 < Traffic.ROWS){
            key  = new Point (x, y+1);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
    }


    @Override
    public synchronized boolean addedVehicle(Vehicle vehicle, DIRECTION from) {
        boolean res = false;
        if(from == DIRECTION.NORTH){
            res |=  gridsInStreetPart[0][3].setVehicle(vehicle);
        }
        if(from == DIRECTION.EAST){
            res |= gridsInStreetPart[3][7].setVehicle(vehicle);
        }
        if(from == DIRECTION.SOUTH){
            res |= gridsInStreetPart[7][4].setVehicle(vehicle);
        }
        if(from == DIRECTION.WEST)
            res |= gridsInStreetPart[4][0].setVehicle(vehicle);
        if(res) {
            vehiclesByLocation.put(vehicle.getGridPart().getLocalPoint(), vehicle);
            System.out.println("Added at dir: " + from);
        }
        return res;
    }


    @Override
    public void removeVehicleAtTarget(Vehicle vehicle) {
        Point target = vehicle.getTarget().gridPart.getLocalPoint();
        gridsInStreetPart[target.y][target.x].setVehicle(null);
        vehiclesByLocation.remove(target);
    }

    @Override
    public void removeVehicleAt(Point localPosition) {
        gridsInStreetPart[localPosition.y][localPosition.x].setVehicle(null);
        vehiclesByLocation.remove(localPosition);

    }






}
