package model.streetpart;

import java.awt.*;

public class SingleLight {
    public Point position;
    private boolean vertical;
    private StreetLights streetLights;

    SingleLight(Point position, boolean vertical, StreetLights streetLights){
        this.position = position;
        this.vertical = vertical;
        this.streetLights = streetLights;
    }

    public boolean turnedOn(){
        return (vertical && streetLights.areVerticalLightsOn()) || (!vertical && streetLights.areHorizontalLightsOn());
    }
}
