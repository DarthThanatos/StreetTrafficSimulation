package utils;

import model.Traffic;
import model.streetpart.StreetPart;

import java.awt.*;

public class DirectionUtils {

    public static StreetPart.DIRECTION localPointToDirection(Point point){
        System.out.print("Mapped " + point + " to ");
        if(point.x == 0){
            return StreetPart.DIRECTION.WEST;
        }
        if(point.x == Traffic.COLUMNS - 1){
            return StreetPart.DIRECTION.EAST;
        }
        if(point.y == 0){
            return StreetPart.DIRECTION.NORTH;
        }
        return StreetPart.DIRECTION.SOUTH;
    }

    public static StreetPart.DIRECTION directionOfFirstComparedToSecond(Point firstStreet, Point secondStreet){
        if(firstStreet.x == secondStreet.x){
            return firstStreet.y < secondStreet.y ? StreetPart.DIRECTION.NORTH : StreetPart.DIRECTION.SOUTH;
        }
        else {
            return firstStreet.x < secondStreet.x ? StreetPart.DIRECTION.WEST : StreetPart.DIRECTION.EAST;
        }
    }
}
