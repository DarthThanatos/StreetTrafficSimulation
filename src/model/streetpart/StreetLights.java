package model.streetpart;

import sim.engine.SimState;
import sim.engine.Steppable;

public class StreetLights implements Steppable {

    private static final int LIGHTS_CYCLE_TIME_STEPS = 75;
    private int stepsInCurrentLightsCycle = 0;

    private boolean horizontalLightsOn = false;

    boolean areHorizontalLightsOn() {
        return horizontalLightsOn;
    }

    boolean areVerticalLightsOn() {
        return !horizontalLightsOn;
    }


    @Override
    public void step(SimState simState) {
        stepsInCurrentLightsCycle++;
        if (stepsInCurrentLightsCycle == LIGHTS_CYCLE_TIME_STEPS) {
            stepsInCurrentLightsCycle = 0;
            horizontalLightsOn = !horizontalLightsOn;
        }
    }
}
