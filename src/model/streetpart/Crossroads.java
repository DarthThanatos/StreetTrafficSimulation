package model.streetpart;

import model.GridPart;
import model.RouteNode;
import model.Traffic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Crossroads extends StreetPart {

    @Override
    public int tileIndex() {
        return 10;
    }

    public Crossroads(Traffic traffic, int x, int y) {
        super(traffic, x, y);
    }

    @Override
    public List<GridPart> getSourcePoints() {
        List<GridPart> res = new ArrayList<>();
        if (x == 0 && y == 0) {
            res.add(gridsInStreetPart[0][3]);
            res.add(gridsInStreetPart[4][0]);
        } else if ((this.x == 0) && (this.y == Traffic.ROWS - 1)) {
            res.add(gridsInStreetPart[4][0]);
            res.add(gridsInStreetPart[7][4]);
        } else if ((this.x == Traffic.COLUMNS - 1) && (this.y == 0)) {
            res.add(gridsInStreetPart[0][3]);
            res.add(gridsInStreetPart[3][7]);
        } else if ((this.x == Traffic.COLUMNS - 1) && (this.y == Traffic.ROWS - 1)) {
            res.add(gridsInStreetPart[7][4]);
            res.add(gridsInStreetPart[3][7]);
        } else if (x == 0) {
            res.add(gridsInStreetPart[4][0]);
        } else if (x == Traffic.COLUMNS - 1) {
            res.add(gridsInStreetPart[3][7]);
        } else if (y == Traffic.ROWS - 1) {
            res.add(gridsInStreetPart[7][4]);
        } else if (y == 0) {
            res.add(gridsInStreetPart[0][3]);
        }
        return res;
    }

    @Override
    public List<GridPart> getExitPoints() {
        List<GridPart> res = new ArrayList<>();
        if (x == 0 && y == 0) {
            res.add(gridsInStreetPart[0][4]);
            res.add(gridsInStreetPart[3][0]);
        } else if ((this.x == 0) && (this.y == Traffic.ROWS - 1)) {
            res.add(gridsInStreetPart[3][0]);
            res.add(gridsInStreetPart[7][3]);
        } else if ((this.x == Traffic.COLUMNS - 1) && (this.y == 0)) {
            res.add(gridsInStreetPart[0][4]);
            res.add(gridsInStreetPart[4][7]);
        } else if ((this.x == Traffic.COLUMNS - 1) && (this.y == Traffic.ROWS - 1)) {
            res.add(gridsInStreetPart[7][3]);
            res.add(gridsInStreetPart[4][7]);
        } else if (x == 0) {
            res.add(gridsInStreetPart[3][0]);
        } else if (x == Traffic.COLUMNS - 1) {
            res.add(gridsInStreetPart[4][7]);
        } else if (y == Traffic.ROWS - 1) {
            res.add(gridsInStreetPart[7][3]);
        } else if (y == 0) {
            res.add(gridsInStreetPart[0][4]);
        }
        return res;
    }

    @Override
    public void initRouteNodeHaving(Map<Point, RouteNode> routeGraph) {
        RouteNode routeNode = routeGraph.get(new Point(x, y));
        Point key;
        if (x - 1 >= 0) {
            key = new Point(x - 1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if (x + 1 < Traffic.COLUMNS) {
            key = new Point(x + 1, y);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if (y - 1 >= 0) {
            key = new Point(x, y - 1);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
        if (y + 1 < Traffic.ROWS) {
            key = new Point(x, y + 1);
            routeNode.neighbours.put(key, routeGraph.get(key));
        }
    }
}
