package model.generators;

import model.Traffic;
import model.streetpart.Crossroads;
import model.streetpart.CrossroadsWithLights;
import model.streetpart.SingleLight;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

import java.awt.*;

public class DifferentCrossroadsGenerator extends CrossroadsGenerator {

    @Override
    Crossroads generateCrossroads(Traffic traffic, int i, int j){
        if(i % 2 == 0){
            if(j%2 == 0) return new Crossroads(traffic, j, i);
            else return new CrossroadsWithLights(traffic, j, i);
        }
        else{
            if(j%2 == 0) return new CrossroadsWithLights(traffic, j, i);
            else return new Crossroads(traffic, j, i);

        }
    }

    @Override void updateStreetsYardLayer(Continuous2D streetsYardLayer, Crossroads crossroads){
        if(crossroads instanceof CrossroadsWithLights){
            CrossroadsWithLights crossroadsWithLights = (CrossroadsWithLights) crossroads;
            for(SingleLight light : crossroadsWithLights.getLights()){
                streetsYardLayer.setObjectLocation(light, new Double2D(light.position.x  + .5, light.position.y  + .5));
            }
        }
    }
}
