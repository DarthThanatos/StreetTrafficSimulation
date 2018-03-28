package model.streetpart;

import model.GridPart;
import model.RouteNode;
import model.Traffic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WestEastTileStreet extends StreetPart{

    public WestEastTileStreet(Traffic traffic, int x, int y) {
        super(traffic, x, y);
    }

    @Override
    public int tileIndex() {
        return 1;
    }
    @Override
    public void initRouteNodeHaving(Map<Point, RouteNode> routeGraph) {
        RouteNode routeNode = routeGraph.get(new Point(x, y));
        Point key;
        if(x - 1 >= 0){
            key  = new Point (x-1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if(x+1 < Traffic.COLUMNS){
            key = new Point(x+1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }

    }


    @Override
    public List<GridPart> getSourcePoints() {
        return new ArrayList<>();
    }

    @Override
    public List<GridPart> getExitPoints() {
        return new ArrayList<>();
    }
}
