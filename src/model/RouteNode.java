package model;

import java.awt.*;
import java.util.*;

public class RouteNode {

    boolean visited;
    public Map<Point, RouteNode> neighbours ;
    int x, y;


    RouteNode(int x, int y){
        this.x = x;
        this.y = y;
        visited  = false;
        neighbours = new HashMap<>();
    }

    private ArrayList<Point> findRouteTo(Point target){
        visited = true;
        if (target.x == x && target.y == y) return new ArrayList<>(Collections.singletonList(new Point(x, y)));
        ArrayList<ArrayList<Point>> results = new ArrayList<>();
        for(RouteNode neighbor : neighbours.values()){
            if(!neighbor.visited) {
                ArrayList<Point> res = neighbor.findRouteTo(target);
                if(res != null) {
                    results.add(res);
                }
            }
        }
        if(results.size() == 0)
            return null;
        ArrayList<Point> res = new ArrayList<>();
        int minSize = 100000;
        for (ArrayList<Point> potentialRes: results){
            if(potentialRes.size() < minSize){
                minSize = potentialRes.size();
                res = potentialRes;
            }
        }
        res.add(new Point(x, y));
        return res;
    }
}
