package org.example.Implementation;

import org.example.Exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Owner {
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

    public Car unpark(Ticket ticket) {
        for (ParkingLot parkingLot : ownedParkingLots.keySet()) {
            if (ownedParkingLots.get(parkingLot)) {
                try {
                    return parkingLot.unpark(ticket);
                } catch (InvalidTicketException e) {
                    // Continue searching in the next parking lot
                }
            }
        }
        throw new InvalidTicketException("Owner cannot attend any parking lot");
    }

    public void assignAttendant(ParkingLot parkingLot, Attendant attendant) {
        if (parkingLot.identifyOwner(ownerId) && parkingLot.isOwned()) {
            attendant.assign(parkingLot);
            return;
        }
        throw new OwnerDoesNotOwnParkingLotException("Owner does not own the parking lot");
    }
}
