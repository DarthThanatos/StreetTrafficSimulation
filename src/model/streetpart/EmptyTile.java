package model.streetpart;

import model.GridPart;
import model.RouteNode;
import model.Traffic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmptyTile extends StreetPart {

    public EmptyTile(Traffic traffic, int x, int y) {
        super(traffic, x, y);
    }

    @Override
    public int tileIndex() {
        return 11;
    }

    @Override
    public List<GridPart> getSourcePoints() {
        return new ArrayList<>();
    }

    @Override
    public List<GridPart> getExitPoints() {
        return new ArrayList<>();
    }

    @Override
    public void initRouteNodeHaving(Map<Point, RouteNode> routeGraph) {

    }
}
