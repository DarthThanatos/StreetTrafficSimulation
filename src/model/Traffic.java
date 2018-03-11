package model;

import model.streetpart.Crossroads;
import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Traffic extends SimState {

    private int vehiclesNumber = 50;

    public static int ROWS = 9, COLUMNS = 9, TILE_SIZE =  8;
    private ObjectGrid2D allStreetsGrids = new ObjectGrid2D(COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);
    private  StreetPart [][] streetParts = new StreetPart[ROWS][COLUMNS];

    @Override
    public void start(){
        super.start();
        for(int i = 0; i < ROWS; i++){
            for (int j = 0; j< COLUMNS; j++){
                Vehicle vehicle = new Vehicle();
                Crossroads crossroads =  new Crossroads(this, j, i);
                streetParts[i][j] = crossroads;
                GridPart[][] gridsInStreetPart = new GridPart[Traffic.TILE_SIZE][Traffic.TILE_SIZE];
                for(int k = 0; k < TILE_SIZE; k++){
                    for(int l = 0; l < TILE_SIZE; l++){
                        GridPart gridPart = new GridPart(crossroads, (k ==7 && l ==4) ? vehicle: null, l, k, j * TILE_SIZE +l, i* TILE_SIZE + k);
                        allStreetsGrids.set(j * TILE_SIZE +l, i* TILE_SIZE + k, gridPart);
                        gridsInStreetPart[k][l] = gridPart;
                    }
                }
                crossroads.setGridsInStreetPart(gridsInStreetPart);
                crossroads.addVehicle(vehicle, Crossroads.DIRECTION.EAST, Crossroads.DIRECTION.EAST);
                vehicle.setSource(new TravelPoint(crossroads, gridsInStreetPart[7][4]));
                vehicle.setGridPart(gridsInStreetPart[7][4]);
                schedule.scheduleRepeating(vehicle);
            }
        }
        for(int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                streetParts[i][j].initVehicles();
            }
        }
    }


    public static void main(String[] args){
        doLoop(Traffic.class, args);
        System.exit(0);
    }

    public Traffic(long seed) {
        super(seed);
    }

    public void setVehiclesNumber(int vehiclesNumber) {
        this.vehiclesNumber = vehiclesNumber;
    }

    public int getVehiclesNumber() {
        return vehiclesNumber;
    }

    public ObjectGrid2D getAllStreetsGrids() {
        return allStreetsGrids;
    }

    TravelPoint getRandomTargetTravelPoint(TravelPoint source){
        HashMap<StreetPart, List<GridPart>> exitPoints = new HashMap<>();
        for(int j = 0 ; j< COLUMNS; j++){
            if(source.streetPart.getX() != j || source.streetPart.getY() != 0){
                StreetPart streetPart = streetParts[0][j];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
            if(source.streetPart.getX() != j || source.streetPart.getY() != ROWS -1){
                StreetPart streetPart = streetParts[ROWS - 1][j];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
        }

        for (int i = 0; i < ROWS; i++){
            if(source.streetPart.getY() != i || source.streetPart.getX() != 0){
                StreetPart streetPart = streetParts[i][0];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
            if(source.streetPart.getY() != i || source.streetPart.getX() != COLUMNS - 1){
                StreetPart streetPart = streetParts[i][COLUMNS - 1];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
        }

        int randomStreetIndex = new Random().nextInt(exitPoints.keySet().size());
        StreetPart exitStreet = (StreetPart) exitPoints.keySet().toArray()[randomStreetIndex];
        int randomGridIndex = new Random().nextInt(exitPoints.get(exitStreet).size());
        return new TravelPoint(exitStreet, exitPoints.get(exitStreet).get(randomGridIndex));
    }
}
