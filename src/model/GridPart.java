package model;

import model.streetpart.StreetPart;

public class GridPart {

    private StreetPart streetPart;
    private Vehicle vehicle;
    private int localx,localy, globalx, globaly;

    GridPart(StreetPart streetPart, Vehicle vehicle, int localx, int localy, int globalx, int globaly){
        this.streetPart = streetPart;
        this.vehicle = vehicle;
        this.localx = localx; this.localy = localy;
        this.globalx = globalx; this.globaly = globaly;
    }

    public StreetPart getStreetPart() {
        return streetPart;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getLocalx() {
        return localx;
    }

    public int getLocaly() {
        return localy;
    }

    public int getGlobalx() {
        return globalx;
    }

    public int getGlobaly() {
        return globaly;
    }
}

