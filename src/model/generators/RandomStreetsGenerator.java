package model.generators;

import model.Traffic;
import model.streetpart.StreetPart;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;

import static model.Traffic.COLUMNS;
import static model.Traffic.ROWS;

public class RandomStreetsGenerator implements Generator {

    @Override
    public void generate(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids, Continuous2D streetsYardLayer) {

    }

    private void generateEdgeStreets(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids, Continuous2D streetsYardLayer){
        for (int i = 0; i < ROWS; i++){

        }

        for (int j = 0; j < COLUMNS; j++){

        }

    }
}
