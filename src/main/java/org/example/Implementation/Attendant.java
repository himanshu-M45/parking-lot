package org.example.Implementation;

import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotNotFoundException;

import java.util.ArrayList;

public class Attendant {
    private final ArrayList<ParkingLot> assignedParkingLots = new ArrayList<>();

    public void assign (ParkingLot parkingLot) {
        if (assignedParkingLots.contains(parkingLot)) {
            throw new ParkingLotAleradyAssignedException("Parking lot already assigned");
        }
        assignedParkingLots.add(parkingLot);
    }

    public ParkingLot getParkingLot(ParkingLot parkingLot) {
        if (assignedParkingLots.contains(parkingLot)) {
            return parkingLot;
        }
        throw new ParkingLotNotFoundException("Parking lot not found");
    }

    public int getTotalParkingLot() {
        return assignedParkingLots.size();
    }
}
