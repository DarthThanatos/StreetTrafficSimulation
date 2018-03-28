package model.generators;

import model.Traffic;
import model.streetpart.StreetPart;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;

public interface Generator {
    void generate(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids);
}
