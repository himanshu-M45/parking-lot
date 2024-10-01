package org.example.Implementation;

import org.example.Exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Owner extends Attendant implements Notifiable {
    private final Map<ParkingLot, Boolean> ownedParkingLots;
    private final String ownerId;

    public Owner () {
        this.ownedParkingLots = new HashMap<>();
        this.ownerId = UUID.randomUUID().toString();
    }

    public ParkingLot createParkingLot (int numberOfSlots) {
        ParkingLot parkingLot = new ParkingLot(numberOfSlots);
        addParkingLot(parkingLot);
        return parkingLot;
    }

    public void addParkingLot(ParkingLot parkingLot) {
        if (parkingLot.isOwned()) {
            throw new ParkingLotAlreadyOwnedException("Parking lot is already owned by someone");
        }
        parkingLot.setOwner(ownerId);
        ownedParkingLots.put(parkingLot, false);
    }

    @Override
    public void assign(ParkingLot parkingLot) {
        if (ownedParkingLots.containsKey(parkingLot)) {
            super.assign(parkingLot);
            return;
        }
        throw new OwnerDoesNotOwnParkingLotException("Owner does not own the parking lot");
    }

    public void assignAttendant(ParkingLot parkingLot, Attendant attendant) {
        if (parkingLot.identifyOwner(ownerId) && parkingLot.isOwned()) {
            attendant.assign(parkingLot);
            return;
        }
        throw new OwnerDoesNotOwnParkingLotException("Owner does not own the parking lot");
    }

    @Override
    public void updateStatus(ParkingLot parkingLot) {
        ownedParkingLots.put(parkingLot, parkingLot.isParkingLotFull());
    }

    public boolean getParkingLotStatus(ParkingLot parkingLot) {
        return ownedParkingLots.get(parkingLot);
    }
}
