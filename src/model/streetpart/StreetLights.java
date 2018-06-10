package model.streetpart;

import sim.engine.SimState;
import sim.engine.Steppable;
import java.util.Random;

public class StreetLights implements Steppable{

    private int min = 20, max = 75;
    private  final int LIGHTS_CYCLE_TIME_STEPS = new Random().nextInt(max - min + 1) + min;
    private int stepsInCurrentLightsCycle = 0;

    private boolean horizontalLightsOn = false;

    boolean areHorizontalLightsOn(){
        return horizontalLightsOn;
    }

    boolean areVerticalLightsOn(){
        return !horizontalLightsOn;
    }


    @Override
    public void step(SimState simState) {
        stepsInCurrentLightsCycle ++;
        if(stepsInCurrentLightsCycle == LIGHTS_CYCLE_TIME_STEPS){
            stepsInCurrentLightsCycle = 0;
            horizontalLightsOn = !horizontalLightsOn;
        }
    }
}
