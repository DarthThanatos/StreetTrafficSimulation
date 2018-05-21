package model.generators;

import model.Traffic;
import model.streetpart.Crossroads;
import model.streetpart.StreetPart;
import sim.field.grid.ObjectGrid2D;
import utils.GeneratorsUtils;

import static model.Traffic.COLUMNS;
import static model.Traffic.ROWS;

public class CrossroadsGenerator implements Generator {

    public void generate(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                GeneratorsUtils.initStreetPart(generateCrossroads(traffic, i, j), streetParts, allStreetsGrids, i, j);
            }
        }
    }

    Crossroads generateCrossroads(Traffic traffic, int i, int j) {
        return new Crossroads(traffic, j, i);
    }
}
