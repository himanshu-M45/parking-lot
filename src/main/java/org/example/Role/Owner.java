package org.example.Role;

import org.example.Exceptions.*;
import org.example.Implementation.ParkingLot;
import org.example.Interface.Notifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Owner extends Attendant implements Notifiable {
    protected Map<ParkingLot, Boolean> ownedParkingLots;
    private final String ownerId;

    public Owner() {
        this.ownedParkingLots = new HashMap<>();
        this.ownerId = UUID.randomUUID().toString();
    }

    public ParkingLot createParkingLot(int numberOfSlots) {
        ParkingLot parkingLot = new ParkingLot(numberOfSlots, this.ownerId);
        ownedParkingLots.put(parkingLot, false);
        parkingLot.setNotifiable(this);
        return parkingLot;
    }

    public void assign(ParkingLot parkingLot, Attendant attendant) {
        if (parkingLot.identifyOwner(ownerId)) {
            attendant.assign(parkingLot);
            return;
        }
        throw new OwnerDoesNotOwnParkingLotException("Owner does not own the parking lot");
    }

    @Override
    public void updateAvailableStatus(String parkingLotId) {
        ParkingLot parkingLot = getParkingLotById(parkingLotId);
        if (parkingLot != null) {
            ownedParkingLots.put(parkingLot, parkingLot.isParkingLotFull());
        }
    }
    private ParkingLot getParkingLotById(String parkingLotId) {
        for (ParkingLot parkingLot : ownedParkingLots.keySet()) {
            if (parkingLot.isSameParkingLot(parkingLotId)) {
                return parkingLot;
            }
        }
        return null; // or throw an exception if preferred
    }

    public boolean getParkingLotStatus(ParkingLot parkingLot) {
        return ownedParkingLots.get(parkingLot);
    }
}
