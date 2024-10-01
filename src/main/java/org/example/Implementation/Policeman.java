package org.example.Implementation;

import org.example.Interface.Notifiable;

import java.util.HashMap;
import java.util.Map;

public class Policeman implements Notifiable {
    private final Map<ParkingLot, Boolean> parkingLotStatus;

    public Policeman() {
        this.parkingLotStatus = new HashMap<>();
    }

    public boolean getParkingLotStatus(ParkingLot parkingLot) {
        return parkingLotStatus.get(parkingLot);
    }

    @Override
    public void updateStatus(ParkingLot parkingLot) {
        parkingLotStatus.put(parkingLot, parkingLot.isParkingLotFull());
    }
}
