package utils;

import model.GridPart;
import model.streetpart.StreetPart;
import sim.field.grid.ObjectGrid2D;

import static model.Traffic.TILE_SIZE;

public class GeneratorsUtils {

    private static void initGridInStreetPart(StreetPart streetPart, ObjectGrid2D allStreetsGrids, int i, int j) {
        GridPart[][] gridsInStreetPart = new GridPart[TILE_SIZE][TILE_SIZE];
        for (int k = 0; k < TILE_SIZE; k++) {
            for (int l = 0; l < TILE_SIZE; l++) {
                GridPart gridPart = new GridPart(streetPart, l, k, j * TILE_SIZE + l, i * TILE_SIZE + k);
                allStreetsGrids.set(j * TILE_SIZE + l, i * TILE_SIZE + k, gridPart);
                gridsInStreetPart[k][l] = gridPart;
            }
        }
        streetPart.setGridsInStreetPart(gridsInStreetPart);

    }

    public static void initStreetPart(StreetPart streetPart, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids, int i, int j) {
        streetParts[i][j] = streetPart;
        initGridInStreetPart(streetPart, allStreetsGrids, i, j);
    }
}
