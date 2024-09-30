package org.example.Implementation;

import org.example.Exceptions.OwnerCannotAttendParkingLotException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.HashMap;
import java.util.Map;

public class Owner {
    private final Map<ParkingLot, Boolean> ownedParkingLots;

    public Owner () {
        this.ownedParkingLots = new HashMap<>();
    }

    public ParkingLot createParkingLot (int numberOfSlots) {
        ParkingLot parkingLot = new ParkingLot(numberOfSlots);
        ownedParkingLots.put(parkingLot, false);
        return parkingLot;
    }

    public boolean canAttend(ParkingLot parkingLot) {
        return ownedParkingLots.get(parkingLot);
    }

    public void setCanAttend(ParkingLot parkingLot) {
       ownedParkingLots.put(parkingLot, true);
    }

    public Ticket park(Car car) {
        for (ParkingLot parkingLot : ownedParkingLots.keySet()) {
            if (ownedParkingLots.get(parkingLot) && !parkingLot.isParkingLotFull()) {
                return parkingLot.park(car);
            }
        }
        throw new OwnerCannotAttendParkingLotException("Owner cannot attend any parking lot");
    }
}
