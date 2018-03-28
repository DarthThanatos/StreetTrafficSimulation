package model.streetpart;

public class StreetLights implements Runnable{

    private static final int LIGHTS_CYCLE_TIME_MS = 3000;
    private boolean horizontalLightsOn = false;
    private volatile  boolean shouldContinue = true;

    boolean areHorizontalLightsOn(){
        return horizontalLightsOn;
    }

    boolean areVerticalLightsOn(){
        return !horizontalLightsOn;
    }

    @Override
    public void run() {
        while(shouldContinue){
            try {
                Thread.sleep(LIGHTS_CYCLE_TIME_MS);
                horizontalLightsOn = !horizontalLightsOn;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isShouldContinue() {
        return shouldContinue;
    }

    public void setShouldContinue(boolean shouldContinue) {
        this.shouldContinue = shouldContinue;
    }
}
