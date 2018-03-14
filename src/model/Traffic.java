package model;

import model.streetpart.Crossroads;
import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import utils.DirectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Traffic extends SimState {

    private int vehiclesNumber = 10;

    public static int ROWS = 9, COLUMNS = 9, TILE_SIZE =  8;
    private ObjectGrid2D allStreetsGrids = new ObjectGrid2D(COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);
    private  StreetPart [][] streetParts = new StreetPart[ROWS][COLUMNS];
    private Map<Point, RouteNode> routeGraph;

    private DirectedGraph<Point, DefaultEdge> directedGraph =
            new DefaultDirectedGraph<>(DefaultEdge.class);

    public Traffic(long seed){
        super(seed);
    }

    @Override
    public void start(){
        super.start();
        allStreetsGrids.clear();
        initPlayground();
        initRouteGraph();
        initVehicles();
    }

    private void initVehicles(){
        for(int i = 0; i< vehiclesNumber; i++){
            Vehicle vehicle = new Vehicle();
            TravelPoint source = getRandomSourcePoint();
            vehicle.setSource(source);
            schedule.scheduleRepeating(vehicle);
        }

    }

    private void initPlayground(){
        for(int i = 0; i < ROWS; i++){
            for (int j = 0; j< COLUMNS; j++){
                Crossroads crossroads =  new Crossroads(this, j, i);
                streetParts[i][j] = crossroads;
                GridPart[][] gridsInStreetPart = new GridPart[Traffic.TILE_SIZE][Traffic.TILE_SIZE];
                for(int k = 0; k < TILE_SIZE; k++){
                    for(int l = 0; l < TILE_SIZE; l++){
                        GridPart gridPart = new GridPart(crossroads, l, k, j * TILE_SIZE +l, i* TILE_SIZE + k);
                        allStreetsGrids.set(j * TILE_SIZE +l, i* TILE_SIZE + k, gridPart);
                        gridsInStreetPart[k][l] = gridPart;
                    }
                }
                crossroads.setGridsInStreetPart(gridsInStreetPart);
            }
        }
    }

    public static void main(String[] args){
        doLoop(Traffic.class, args);
        System.exit(0);
    }


    public void setVehiclesNumber(int vehiclesNumber) {
        this.vehiclesNumber = vehiclesNumber;
    }

    public int getVehiclesNumber() {
        return vehiclesNumber;
    }

    public ObjectGrid2D getAllStreetsGrids() {
        return allStreetsGrids;
    }

    private TravelPoint getRandomSourcePoint(){
        HashMap<StreetPart, List<GridPart>> sourcePoints = new HashMap<>();
        StreetPart streetPart;
        for(int j = 0 ; j< COLUMNS; j++){
                streetPart = streetParts[0][j];
                sourcePoints.put(streetPart, streetPart.getSourcePoints());
                streetPart = streetParts[ROWS - 1][j];
                sourcePoints.put(streetPart, streetPart.getSourcePoints());

        }

        for (int i = 0; i < ROWS; i++){
            streetPart = streetParts[i][0];
            sourcePoints.put(streetPart, streetPart.getSourcePoints());
            streetPart = streetParts[i][COLUMNS - 1];
            sourcePoints.put(streetPart, streetPart.getSourcePoints());
        }

        int randomStreetIndex = new Random().nextInt(sourcePoints.keySet().size());
        StreetPart sourceStreet = (StreetPart) sourcePoints.keySet().toArray()[randomStreetIndex];
        int randomGridIndex = new Random().nextInt(sourcePoints.get(sourceStreet).size());
        return new TravelPoint(sourceStreet, sourcePoints.get(sourceStreet).get(randomGridIndex));
    }

    TravelPoint getRandomTargetTravelPoint(TravelPoint source){
        HashMap<StreetPart, List<GridPart>> exitPoints = new HashMap<>();
        for(int j = 0 ; j< COLUMNS; j++){
            if(source.streetPart.getX() != j || source.streetPart.getY() != 0){
                StreetPart streetPart = streetParts[0][j];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
            if(source.streetPart.getX() != j || source.streetPart.getY() != ROWS -1){
                StreetPart streetPart = streetParts[ROWS - 1][j];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
        }

        for (int i = 0; i < ROWS; i++){
            if(source.streetPart.getY() != i || source.streetPart.getX() != 0){
                StreetPart streetPart = streetParts[i][0];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
            if(source.streetPart.getY() != i || source.streetPart.getX() != COLUMNS - 1){
                StreetPart streetPart = streetParts[i][COLUMNS - 1];
                exitPoints.put(streetPart, streetPart.getExitPoints());
            }
        }

        int randomStreetIndex = new Random().nextInt(exitPoints.keySet().size());
        StreetPart exitStreet = (StreetPart) exitPoints.keySet().toArray()[randomStreetIndex];
        int randomGridIndex = new Random().nextInt(exitPoints.get(exitStreet).size());
        return new TravelPoint(exitStreet, exitPoints.get(exitStreet).get(randomGridIndex));
    }

    private void initRouteGraph(){
        routeGraph = new HashMap<>();
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                routeGraph.put(new Point(j, i), new RouteNode(j, i));
                directedGraph.addVertex(new Point(j,i));
            }
        }
        for(int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                StreetPart streetPart = streetParts[i][j];
                streetPart.initRouteNodeHaving(routeGraph);
                RouteNode routeNode = routeGraph.get(new Point(j,i));
                for(RouteNode neighbor: routeNode.neighbours.values()){
                    directedGraph.addEdge(new Point(routeNode.x, routeNode.y), new Point(neighbor.x, neighbor.y));
                }
            }
        }

    }

    private void cleanGraph(){
        for(RouteNode routeNode : routeGraph.values())
            routeNode.visited = false;
    }

    synchronized List<Point> mountRouteFromTo(TravelPoint from, TravelPoint to){
        cleanGraph();
        RouteNode start = routeGraph.get(new Point(from.streetPart.getX(), from.streetPart.getY()));
        StreetPart target = to.streetPart;
        DijkstraShortestPath<Point, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(directedGraph);
        ShortestPathAlgorithm.SingleSourcePaths<Point, DefaultEdge> paths = dijkstraShortestPath.getPaths(new Point(start.x, start.y) );
        return paths.getPath(new Point (target.getX(), target.getY())).getVertexList();
    }


    synchronized boolean  movedVehicleAcrossStreets(Vehicle vehicle, Point firstStreet, Point targetStreet, Point currentLocalLocation){
        boolean res = streetParts[targetStreet.y][targetStreet.x].addedVehicle(vehicle, DirectionUtils.directionOfFirstComparedToSecond(firstStreet, targetStreet));
        if(res) {
            streetParts[firstStreet.y][firstStreet.x].removeVehicleAt(currentLocalLocation);
            System.out.println("Removing at " + firstStreet + " current local: " + currentLocalLocation);
//            try {
//                new BufferedReader(new InputStreamReader(System.in)).readLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        return res;
    }

    void endVehicleCycle(Vehicle vehicle){
        System.out.println("Ending cycle");
        vehicle.getGridPart().getStreetPart().removeVehicleAtTarget(vehicle);
//        vehicle.getGridPart().getStreetPart().removeVehicleAt(vehicle.getGridPart().getLocalPoint());
        TravelPoint source = vehicle.getSource();
        source.pointReached = false;
        vehicle.setSource(source);

    }
}
