package model.streetpart;

import model.GridPart;
import model.Traffic;
import model.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Crossroads extends StreetPart {

    public Crossroads(Traffic traffic, int x, int y) {
        super(traffic, x, y);
    }

    @Override
    public void addVehicle(Vehicle vehicle, DIRECTION from, DIRECTION direction) {
        vehiclesByLocation.put(new Point(4,7), vehicle);

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
            res.add(gridsInStreetPart[7][4]);
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

    @Override
    public void moveVehicle(Vehicle vehicle, DIRECTION direction) {

    }
}
