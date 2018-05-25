package model.streetpart;

import java.util.Random;
import sim.engine.SimState;
import sim.engine.Steppable;

public class StreetLights implements Steppable{

    private int min = 20, max = 75;
    private int LIGHTS_CYCLE_TIME_STEPS = new Random().nextInt(max - min + 1) + min;
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
