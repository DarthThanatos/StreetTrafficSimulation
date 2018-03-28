package model.streetpart;

import model.Traffic;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CrossroadsWithLights extends Crossroads {

    private StreetLights streetLights;
    private Thread lightsThread; //TO-DO finish thread

    public CrossroadsWithLights(Traffic traffic, int x, int y) {
        super(traffic, x, y);
        streetLights = new StreetLights();
        lightsThread = new Thread(streetLights);
        lightsThread.start();
    }

    public List<SingleLight> getLights(){
        return Arrays.asList(
                new SingleLight(gridsInStreetPart[5][5].getGlobalPoint(),true, streetLights),
                new SingleLight(gridsInStreetPart[2][2].getGlobalPoint(),true, streetLights),
                new SingleLight(gridsInStreetPart[2][5].getGlobalPoint(),false, streetLights),
                new SingleLight(gridsInStreetPart[5][2].getGlobalPoint(),false, streetLights)
        );
    }

    private List<Point> verticalCrossPoints(){
        return Arrays.asList(
              new Point(4,5),
              new Point(3,2)
        );

    }


    private List<Point> horizontalCrossPoints() {
        return Arrays.asList(
                new Point(5, 3),
                new Point(2, 4)
        );
    }

    @Override public boolean waitingAtCross(Point from){
        return (verticalCrossPoints().contains(from) && streetLights.areHorizontalLightsOn())
                || (horizontalCrossPoints().contains(from) && streetLights.areVerticalLightsOn());
    }
}
