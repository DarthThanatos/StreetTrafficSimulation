package model.generators;

import model.GridPart;
import model.Traffic;
import model.streetpart.Crossroads;
import model.streetpart.StreetPart;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;

import static model.Traffic.COLUMNS;
import static model.Traffic.ROWS;
import static model.Traffic.TILE_SIZE;

public class CrossroadsGenerator implements Generator{

    public void generate(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids, Continuous2D streetsYardLayer){
        for(int i = 0; i < ROWS; i++){
            for (int j = 0; j< COLUMNS; j++){
                Crossroads crossroads =  generateCrossroads(traffic, i, j);
                streetParts[i][j] = crossroads;
                GridPart[][] gridsInStreetPart = new GridPart[TILE_SIZE][TILE_SIZE];
                for(int k = 0; k < TILE_SIZE; k++){
                    for(int l = 0; l < TILE_SIZE; l++){
                        GridPart gridPart = new GridPart(crossroads, l, k, j * TILE_SIZE +l, i* TILE_SIZE + k);
                        allStreetsGrids.set(j * TILE_SIZE +l, i* TILE_SIZE + k, gridPart);
                        gridsInStreetPart[k][l] = gridPart;
                    }
                }
                crossroads.setGridsInStreetPart(gridsInStreetPart);
                updateStreetsYardLayer(streetsYardLayer, crossroads);
            }
        }
    }

    void updateStreetsYardLayer(Continuous2D streetsYardLayer, Crossroads crossroads){ } //hook

    Crossroads generateCrossroads(Traffic traffic, int i, int j){
        return new Crossroads(traffic, j, i);
    }
}
