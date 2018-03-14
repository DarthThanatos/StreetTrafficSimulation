package model.streetpart;

import model.GridPart;

public class TravelPoint{

    public StreetPart streetPart;
    public GridPart gridPart;
    public boolean pointReached = false;

    public TravelPoint(StreetPart streetPart, GridPart gridPart){
        this.streetPart = streetPart;
        this.gridPart = gridPart;
    }

    @Override
    public String toString(){
        return "streetx, streety = " + streetPart.x + ", " + streetPart.y
                + "; local_x, local_y = "  + gridPart.getLocalx() + ", " + gridPart.getLocaly()
                + "; global_x, global_y = " + gridPart.getGlobalx() + ", " + gridPart.getGlobaly();
    }
}