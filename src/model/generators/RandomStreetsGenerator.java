package model.generators;

import model.Traffic;
import model.streetpart.*;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;
import utils.GeneratorsUtils;

import java.util.*;

import static model.Traffic.COLUMNS;
import static model.Traffic.ROWS;

public class RandomStreetsGenerator implements Generator {



    @Override
    public void generate(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids) {
        initConnections();
        generateEdgeStreets(traffic, streetParts, allStreetsGrids);
        phaseOne(traffic, streetParts, allStreetsGrids);
        phaseTwo(traffic, streetParts, allStreetsGrids);
    }

    private void generateEdgeStreets(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids){
        for (int i = 0; i < ROWS; i++){
            if(i != 0 && i != ROWS - 1) {
                connectionsAt[i][1][3] = Boolean.TRUE;
                connectionsAt[i][COLUMNS - 2][1] = Boolean.TRUE;
            }
            GeneratorsUtils.initStreetPart(new CrossroadsWithLights(traffic, 0, i), streetParts, allStreetsGrids, i, 0);
            GeneratorsUtils.initStreetPart(new CrossroadsWithLights(traffic, COLUMNS - 1, i), streetParts, allStreetsGrids, i, COLUMNS - 1);
        }

        for (int j = 1; j < COLUMNS - 1; j++){
            if(j != COLUMNS - 1) {
                connectionsAt[1][j][0] = Boolean.TRUE;
                connectionsAt[ROWS-2][j][2] = Boolean.TRUE;
            }
            GeneratorsUtils.initStreetPart(new CrossroadsWithLights(traffic, j, 0), streetParts, allStreetsGrids, 0, j);
            GeneratorsUtils.initStreetPart(new CrossroadsWithLights(traffic, j, ROWS - 1), streetParts, allStreetsGrids, ROWS - 1, j);

        }

    }


    interface StreetPartFactory{
        StreetPart newStreetPart(Traffic traffic, int i, int j);
    }

    private StreetPartFactory[] factories = {
            (t, i,j) -> new NorthSouthTileStreet(t,j,i),
            (t, i,j) -> new WestEastTileStreet(t,j,i),
            (t, i,j) -> new NorthWestTileStreet(t,j,i),
            (t, i,j) -> new NorthEastTileStreet(t,j,i),
            (t, i,j) -> new SouthWestTileStreet(t,j,i),
            (t, i,j) -> new SouthEastTileStreet(t,j,i),
            (t, i,j) -> new NorthSouthWestTileStreet(t,j,i),
            (t, i,j) -> new NorthSouthEastTileStreet(t,j,i),
            (t, i,j) -> new SouthWestEastTileStreet(t,j,i),
            (t, i,j) -> new NorthWestEastTileStreet(t,j,i),
            (t, i,j) -> new CrossroadsWithLights(t,j,i),
            (t,i,j) -> new EmptyTile(t,j,i)
    };

    private HashMap<Boolean[], Integer> partsConnectingDirections = new HashMap<Boolean[], Integer>(){
        {
            put(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}, 0);
            put(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, 1);
            put(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE}, 2);
            put(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}, 3);
            put(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE}, 4);
            put(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}, 5);
            put(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE}, 6);
            put(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}, 7);
            put(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, 8);
            put(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, 9);
            put(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, 10);
            put(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}, 11);
        }
    };

    private HashMap<String, Integer> partsConnectingStringDirections = new HashMap<String, Integer>(){
        {
            put("tftf", 0);
            put("ftft", 1);
            put("tfft", 2);
            put("ttff", 3);
            put("fftt", 4);
            put("fttf", 5);
            put("tftt", 6);
            put("tttf", 7);
            put("fttt", 8);
            put("ttft", 9);
            put("tttt", 10);
            put("ffff", 11);
        }
    };

    private List<Integer> connectingWithNorth = Arrays.asList(0, 2,3,6,7, 9, 10);
    private List<Integer> connectingWithEast =  Arrays.asList(1,3,5,7,8,9,10);
    private List<Integer> connectingWithSouth =  Arrays.asList(0,4,5,6,7,8,10);
    private List<Integer> connectingWithWest =  Arrays.asList(1,2,4,6,8,9,10);

    private Boolean[][][] connectionsAt = new Boolean[ROWS][COLUMNS][4];

    private void initConnections(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                for(int k = 0; k < 4; k++){
                    connectionsAt[i][j][k] = Boolean.FALSE;
                }
            }
        }
    }

    private void phaseOne(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids){
        for (int i = 1; i < ROWS -1; i++){
            for(int j = i % 2 == 1? 1 : 2; j < COLUMNS -1; j+=2) {
                Boolean[] possibleFactoriesIndecies = new Boolean[11];
                for (int k = 0; k < 11; k++) possibleFactoriesIndecies[k] = true;
                if (streetParts[i - 1][j] != null) {
                    possibleFactoriesIndecies[1] = Boolean.FALSE;
                    possibleFactoriesIndecies[4] = Boolean.FALSE;
                    possibleFactoriesIndecies[5] = Boolean.FALSE;
                    possibleFactoriesIndecies[8] = Boolean.FALSE;
                }
                if (streetParts[i][j + 1] != null) {
                    possibleFactoriesIndecies[0] = Boolean.FALSE;
                    possibleFactoriesIndecies[2] = Boolean.FALSE;
                    possibleFactoriesIndecies[4] = Boolean.FALSE;
                    possibleFactoriesIndecies[6] = Boolean.FALSE;
                }
                if (streetParts[i + 1][j] != null) {
                    possibleFactoriesIndecies[1] = Boolean.FALSE;
                    possibleFactoriesIndecies[2] = Boolean.FALSE;
                    possibleFactoriesIndecies[3] = Boolean.FALSE;
                    possibleFactoriesIndecies[9] = Boolean.FALSE;
                }
                if (streetParts[i][j - 1] != null) {
                    possibleFactoriesIndecies[0] = Boolean.FALSE;
                    possibleFactoriesIndecies[3] = Boolean.FALSE;
                    possibleFactoriesIndecies[5] = Boolean.FALSE;
                    possibleFactoriesIndecies[7] = Boolean.FALSE;
                }

                int selectedFactoryIndex;
                do {
                    selectedFactoryIndex = new Random().nextInt(11);
                }while(!possibleFactoriesIndecies[selectedFactoryIndex]);


                if(connectingWithNorth.contains(selectedFactoryIndex)) connectionsAt[i - 1][j][2] = Boolean.TRUE;
                if(connectingWithEast.contains(selectedFactoryIndex)) connectionsAt[i][j + 1][3] = Boolean.TRUE;
                if(connectingWithSouth.contains(selectedFactoryIndex)) connectionsAt[i + 1][j][0] = Boolean.TRUE;
                if(connectingWithWest.contains(selectedFactoryIndex)) connectionsAt[i][j - 1][1] = Boolean.TRUE;

                GeneratorsUtils.initStreetPart(factories[selectedFactoryIndex].newStreetPart(traffic,i,j), streetParts, allStreetsGrids, i, j);
            }
        }
    }

    private void phaseTwo(Traffic traffic, StreetPart[][] streetParts, ObjectGrid2D allStreetsGrids){

        for (int i = 1; i < ROWS -1; i++){
            for(int j = i % 2 == 1? 2 : 1; j < COLUMNS -1; j+=2) {
                String key = Arrays.stream(connectionsAt[i][j]).map(b -> b ? "t" : "f" ).reduce((s, s2) -> s + s2).get();
                System.out.print("[" + i + "][" + j + "] = ");
                for(int k = 0; k < 4; k ++){
                    System.out.print(connectionsAt[i][j][k]+ " ") ;
                }

                System.out.println("Key: " + key);
                System.out.println(
                        streetParts[i-1][j].getClass().getSimpleName() + " " +
                        streetParts[i][j+1].getClass().getSimpleName() + " " +
                        streetParts[i+1][j].getClass().getSimpleName() + " " +
                        streetParts[i][j-1].getClass().getSimpleName()
                );
                Integer factoryIndex = partsConnectingStringDirections.get(key);
                System.out.println(factoryIndex);
                try {
                    GeneratorsUtils.initStreetPart(factories[factoryIndex].newStreetPart(traffic, i, j), streetParts, allStreetsGrids, i, j);
                }catch(NullPointerException e){
                    e.printStackTrace();
                    switch(key.indexOf('t')){
                        case 0: mapSouthOf(streetParts, i, j, traffic, allStreetsGrids); GeneratorsUtils.initStreetPart(factories[0].newStreetPart(traffic, i, j), streetParts, allStreetsGrids, i, j); break;
                        case 1: mapWestOf(streetParts, i,j, traffic, allStreetsGrids); GeneratorsUtils.initStreetPart(factories[1].newStreetPart(traffic, i, j), streetParts, allStreetsGrids, i, j);break;
                        case 2: mapNorthOf(streetParts, i,j, traffic, allStreetsGrids); GeneratorsUtils.initStreetPart(factories[0].newStreetPart(traffic, i, j), streetParts, allStreetsGrids, i, j);break;
                        case 3: mapEastOf(streetParts, i,j, traffic, allStreetsGrids); GeneratorsUtils.initStreetPart(factories[1].newStreetPart(traffic, i, j), streetParts, allStreetsGrids, i, j);break;
                    }
                }
            }
        }
    }

    private void mapSouthOf(StreetPart[][] streetParts, int i, int j, Traffic traffic, ObjectGrid2D allStreetGrids) {
        switch(streetParts[i+1][j].tileIndex()){
            case 1: GeneratorsUtils.initStreetPart(factories[9].newStreetPart(traffic, i+1, j), streetParts, allStreetGrids, i+1, j); break;
            case 8: GeneratorsUtils.initStreetPart(factories[10].newStreetPart(traffic, i+1, j), streetParts, allStreetGrids, i+1, j);break;
            case 4: GeneratorsUtils.initStreetPart(factories[6].newStreetPart(traffic, i+1, j), streetParts, allStreetGrids, i+1, j);break;
            case 5: GeneratorsUtils.initStreetPart(factories[7].newStreetPart(traffic, i+1, j), streetParts, allStreetGrids, i+1, j);break;
        }
    }

    private void mapWestOf(StreetPart[][] streetParts, int i, int j, Traffic traffic, ObjectGrid2D allStreetGrids) {
        switch(streetParts[i][j-1].tileIndex()){
            case 0: GeneratorsUtils.initStreetPart(factories[7].newStreetPart(traffic, i,j-1 ), streetParts, allStreetGrids, i, j-1);break;
            case 7:GeneratorsUtils.initStreetPart(factories[10].newStreetPart(traffic, i,j-1), streetParts, allStreetGrids, i, j-1); break;
            case 2: GeneratorsUtils.initStreetPart(factories[9].newStreetPart(traffic, i,j-1), streetParts, allStreetGrids, i, j-1);break;
            case 4: GeneratorsUtils.initStreetPart(factories[8].newStreetPart(traffic, i,j-1), streetParts, allStreetGrids, i, j-1);break;
        }
    }
    private void mapNorthOf(StreetPart[][] streetParts, int i, int j, Traffic traffic, ObjectGrid2D allStreetGrids) {
        switch(streetParts[i -1][j].tileIndex()){
            case 1: GeneratorsUtils.initStreetPart(factories[8].newStreetPart(traffic,  i-1, j), streetParts, allStreetGrids, i-1, j);break;
            case 9: GeneratorsUtils.initStreetPart(factories[10].newStreetPart(traffic, i-1, j), streetParts, allStreetGrids, i-1, j);break;
            case 2:GeneratorsUtils.initStreetPart(factories[6].newStreetPart(traffic,  i-1, j), streetParts, allStreetGrids, i-1, j); break;
            case 3: GeneratorsUtils.initStreetPart(factories[7].newStreetPart(traffic, i-1, j), streetParts, allStreetGrids, i-1, j);break;
        }
    }

    private void mapEastOf(StreetPart[][] streetParts, int i, int j, Traffic traffic, ObjectGrid2D allStreetGrids) {
        switch(streetParts[i][j+1].tileIndex()){
            case 0: GeneratorsUtils.initStreetPart(factories[6].newStreetPart(traffic, i,j+1), streetParts, allStreetGrids, i, j+1);break;
            case 7:GeneratorsUtils.initStreetPart(factories[10].newStreetPart(traffic, i, j+1), streetParts, allStreetGrids, i, j+1); break;
            case 3: GeneratorsUtils.initStreetPart(factories[9].newStreetPart(traffic, i,j+1), streetParts, allStreetGrids, i, j+1);break;
            case 5: GeneratorsUtils.initStreetPart(factories[8].newStreetPart(traffic, i,j+1), streetParts, allStreetGrids, i, j+1);break;
        }
    }


}
