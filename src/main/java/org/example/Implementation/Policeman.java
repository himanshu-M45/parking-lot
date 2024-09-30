package org.example.Implementation;

import java.util.HashMap;
import java.util.Map;

public class Policeman {
    private static Policeman instance;
    private final Map<ParkingLot, Boolean> parkingLotStatus;

    public Policeman() {
        this.parkingLotStatus = new HashMap<>();
    }

    public static synchronized Policeman getInstance() {
        if (instance == null) {
            instance = new Policeman();
        }
        return instance;
    }

    public void addParkingLot(ParkingLot parkingLot) {
        parkingLotStatus.put(parkingLot, parkingLot.isParkingLotFull());
    }

    public boolean getParkingLotStatus(ParkingLot parkingLot) {
        return parkingLotStatus.get(parkingLot);
    }

    public void updateParkingLotStatus(ParkingLot parkingLot) {
        parkingLotStatus.put(parkingLot, parkingLot.isParkingLotFull());
    }
}
