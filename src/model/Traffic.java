package model;

import model.generators.CrossroadsGenerator;
import model.generators.DifferentCrossroadsGenerator;
import model.generators.Generator;
import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;
import sim.util.Double2D;
import utils.DirectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Traffic extends SimState {

    private int vehiclesNumber = 20;

    public static int ROWS = 9, COLUMNS = 9, TILE_SIZE =  8;
    private ObjectGrid2D allStreetsGrids = new ObjectGrid2D(COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);
    private Continuous2D vehiclesYardLayer = new Continuous2D(1, COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);
    private Continuous2D streetsYardLayer = new Continuous2D(1, COLUMNS * TILE_SIZE, ROWS * TILE_SIZE);
    private  StreetPart [][] streetParts = new StreetPart[ROWS][COLUMNS];
    private Map<Point, RouteNode> routeGraph;

    private List<Vehicle> vehicles = new ArrayList<>();

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

    @Override public void finish(){

        super.finish();
    }

    private void initVehicles(){
        vehiclesYardLayer.clear();
        vehicles = new ArrayList<>();
        for(int i = 0; i< vehiclesNumber; i++){
            Vehicle vehicle = new Vehicle();
            TravelPoint source = getRandomSourcePoint();
            vehicle.setSource(source);
            vehicles.add(vehicle);
            vehiclesYardLayer.setObjectLocation(vehicle, new Double2D(-100,-100));
            schedule.scheduleRepeating(vehicle);
        }

    }

    private void initPlayground(){
        streetsYardLayer.clear();
        Generator generator = new DifferentCrossroadsGenerator();
        generator.generate(this, streetParts, allStreetsGrids, streetsYardLayer);
    }

    public static void main(String[] args){
        doLoop(Traffic.class, args);
        System.exit(0);
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

        }
        return res;
    }

    void endVehicleCycle(Vehicle vehicle){
        System.out.println("Ending cycle");
        vehicle.getGridPart().getStreetPart().removeVehicleAtTarget(vehicle);
        TravelPoint source = vehicle.getSource();
        source.pointReached = false;
        vehicle.setSource(source);

    }

    public Continuous2D getVehiclesYardLayer(){
        return vehiclesYardLayer;
    }

    public Continuous2D getStreetLightsYardLayer(){return  streetsYardLayer;}

    public List<Vehicle> getVehicles(){
        return vehicles;
    }

    public void setVehiclesNumber(int vehiclesNumber) {
        this.vehiclesNumber = vehiclesNumber;
    }

    public int getVehiclesNumber() {
        return vehiclesNumber;
    }
}
