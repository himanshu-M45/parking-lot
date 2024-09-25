package org.example.Implementation;

import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotNotFoundException;

import java.util.ArrayList;

public class Attendant {
    // Attendant is responsible for managing parking lots
    private final ArrayList<ParkingLot> assignedParkingLots = new ArrayList<>();

    public void assign (ParkingLot parkingLot) {
        // assign a parking lot to the attendant
        if (assignedParkingLots.contains(parkingLot)) {
            throw new ParkingLotAleradyAssignedException("Parking lot already assigned");
        }
        assignedParkingLots.add(parkingLot);
    }

    public int getTotalParkingLot() {
        // return the total number of parking lots assigned to the attendant
        return assignedParkingLots.size();
    }

    public Ticket park(ParkingLot parkingLot, Car car) {
        return parkingLot.park(car);
    }

    public Car unPark(Ticket carTicket) {
        ParkingLot parkingLot = findParkingLotByObjectId(carTicket.parkingLotObjId);
        if (parkingLot != null) {
            return parkingLot.unPark(carTicket);
        }
        throw new ParkingLotNotFoundException("Parking lot not found");
    }

    private ParkingLot findParkingLotByObjectId(int parkingLotObjId) {
        // find the parking lot by the object id
        for (ParkingLot parkingLot: assignedParkingLots) {
            if (System.identityHashCode(parkingLot) == parkingLotObjId) {
                return parkingLot;
            }
        }
        return null;
    }
}
