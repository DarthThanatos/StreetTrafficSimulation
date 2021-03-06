package model;

import model.streetpart.StreetPart;
import model.streetpart.TravelPoint;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;
import utils.DirectionUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Vehicle implements Steppable {

    private String id = UUID.randomUUID().toString();

    private List<Point> streetPartsRoute;
    private int currentStreetPartInPath = 0;

    private List<Point> localRouteMoves;
    private int currentLocalRouteMove = 0;

    private TravelPoint source;
    private TravelPoint target;

    private GridPart gridPart;

    private Statistics statistics = new Statistics();

    private int speedSlowFactor = 0;
    private int stepCounter = 0;

    private Random random;

    public Vehicle() {
        random = new Random();
    }

    void setSource(TravelPoint source) {
        this.source = source;
        this.gridPart = source.gridPart;
    }

    TravelPoint getSource() {
        return source;
    }

    public TravelPoint getTarget() {
        return target;
    }

    @Override
    public void step(SimState simState) {
        initOrUpdateSpeedFactor();
        if (canMakeStep()) {
            return;
        }
        boolean posChanged;
        if (gridPart == source.gridPart && !source.pointReached) {
            posChanged = tryToReachSource();
        } else {
            posChanged = normalStep();
        }
        if (posChanged) {
            Continuous2D vehiclesYardLayer = gridPart.getStreetPart().getTraffic().getVehiclesYardLayer();
            vehiclesYardLayer.setObjectLocation(
                    gridPart.getVehicle(), new Double2D(gridPart.getGlobalx() + .5, gridPart.getGlobaly() + .5)
            );
        }
    }

    private boolean tryToReachSource() {
        boolean vehicleAdded = source.streetPart.addedVehicle(
                this,
                DirectionUtils.localPointToDirection(source.gridPart.getLocalPoint())
        );
        if (vehicleAdded) {
            source.pointReached = true;
            resetRoute();
            setupCurrentLocalRouteMoves();
            log();
        }
        statistics.update(vehicleAdded, false);
        return vehicleAdded;
    }


    private void setupCurrentLocalRouteMoves() {
        Point currentStreet = streetPartsRoute.get(currentStreetPartInPath);
        StreetPart.DIRECTION from = DirectionUtils.localPointToDirection(gridPart.getLocalPoint());
        System.out.println(from);
        Point nextStreet = (currentStreetPartInPath + 1) != streetPartsRoute.size() ? streetPartsRoute.get(currentStreetPartInPath + 1) : null;
        StreetPart.DIRECTION to = nextStreet != null ? DirectionUtils.directionOfFirstComparedToSecond(nextStreet, currentStreet) : DirectionUtils.localPointToDirection(target.gridPart.getLocalPoint());
        localRouteMoves = gridPart.getStreetPart().streetPartMoves(from, to);
        currentLocalRouteMove = 0;

        System.out.println(currentStreet + " -> " + (nextStreet != null ? nextStreet : target.gridPart.getLocalPoint()) + ", local: " + gridPart.getLocalPoint());
        System.out.println(
                localRouteMoves.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) -> agg + "; " + s) + "\n"
        );
    }

    private boolean normalStep() {
        boolean notEndOfCurrentTile = currentLocalRouteMove < localRouteMoves.size() - 1;
        boolean notEndOfCurrentCycle = currentStreetPartInPath < streetPartsRoute.size() - 1;
        boolean movedVehicleInThisStep = false;
        if (notEndOfCurrentTile) {
            boolean movedVehicleLocally = gridPart.getStreetPart().movedVehicleLocally(
                    this,
                    localRouteMoves.get(currentLocalRouteMove),
                    localRouteMoves.get(currentLocalRouteMove + 1));
            if (movedVehicleLocally) {
                currentLocalRouteMove++;
                movedVehicleInThisStep = true;
            }
        } else {
            if (notEndOfCurrentCycle) {
                boolean movedVehicleAcrossStreets = gridPart.getStreetPart().getTraffic().movedVehicleAcrossStreets(
                        this,
                        streetPartsRoute.get(currentStreetPartInPath),
                        streetPartsRoute.get(currentStreetPartInPath + 1),
                        gridPart.getLocalPoint()
                );
                if (movedVehicleAcrossStreets) {
                    currentStreetPartInPath++;
                    setupCurrentLocalRouteMoves();
                    movedVehicleInThisStep = true;
                }
            } else {
                gridPart.getStreetPart().getTraffic().endVehicleCycle(this);
            }
        }
        statistics.update(movedVehicleInThisStep, !notEndOfCurrentCycle);
        return movedVehicleInThisStep;
    }

    private void log() {
        System.out.println("======");
        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println(
                streetPartsRoute.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) -> agg + "; " + s)
        );
        System.out.println(
                localRouteMoves.stream().map(point -> "(" + point.x + ", " + point.y + ")").reduce((agg, s) -> agg + "; " + s)
        );
        System.out.println("current local: " + currentLocalRouteMove);
        System.out.println("current street: " + currentStreetPartInPath);
        System.out.println("======");
    }

    private void resetRoute() {
        currentStreetPartInPath = 0;
        target = gridPart.getStreetPart().getTraffic().getRandomTargetTravelPoint(source);
        streetPartsRoute = gridPart.getStreetPart().getTraffic().mountRouteFromTo(source, target);
    }

    private void initOrUpdateSpeedFactor() {
        if (speedSlowFactor == 0) {
            speedSlowFactor = random.nextInt(6) + 1;
        } else {
            if (random.nextInt(100) < 3) {
                speedSlowFactor = random.nextInt(6) + 1;
            }
        }
    }

    private boolean canMakeStep() {
        stepCounter++;
        return stepCounter % speedSlowFactor == 0;
    }

    void setGridPart(GridPart gridPart) {
        this.gridPart = gridPart;
    }

    public GridPart getGridPart() {
        return gridPart;
    }

    public Point[] getLocalMovesFromCurrent() {
        return Arrays.copyOfRange(localRouteMoves.toArray(new Point[localRouteMoves.size()]), currentLocalRouteMove, localRouteMoves.size());
    }

    Statistics getStatistics() {
        return statistics;
    }

    public double getAvgStepsPerIteration() {
        return statistics.getAvgStepsPerIteration();
    }

    public double getAvgStepsPerCycle() {
        return statistics.getAvgStepsPerCycle();
    }

    public double getAvgIterationsPerCycle() {
        return statistics.getAvgIterationsPerCycle();
    }

    public double getAvgTimePerCycle() {
        return statistics.getAvgTimePerCycle();
    }

    public double getAvgTimePerIteration() {
        return statistics.getAvgTimePerIteration();
    }

    public String getId() {
        return id;
    }

    public Point getCurrentGlobalPos() {
        Double2D vehiclePos = gridPart.getStreetPart().getTraffic().getVehiclesYardLayer().getObjectLocation(this);
        return new Point((int) vehiclePos.x, (int) vehiclePos.y);
    }
}
