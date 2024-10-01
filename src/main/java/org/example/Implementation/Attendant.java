package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.ArrayList;

public class Attendant {
    // Attendant is responsible for managing parking lots
    protected final ArrayList<ParkingLot> assignedParkingLots = new ArrayList<>();

    public void assign (ParkingLot parkingLot) {
        // assign a parking lot to the attendant
        if (assignedParkingLots.contains(parkingLot)) {
            throw new ParkingLotAleradyAssignedException("Parking lot already assigned");
        }
        assignedParkingLots.add(parkingLot);
    }

    public Ticket park(Car car) {
        for (ParkingLot parkingLot : assignedParkingLots) {
            if (!parkingLot.isParkingLotFull()) {
                return parkingLot.park(car);
            }
        }
        throw new ParkingLotIsFullException("No available parking lot found");
    }

    public Car unpark(Ticket carTicket) {
        for (ParkingLot parkingLot : assignedParkingLots) {
            try {
                return parkingLot.unpark(carTicket);
            } catch (InvalidTicketException e) {
                // Continue searching in the next parking lot
            }
        }
        throw new InvalidTicketException("Parking lot not found");
    }
}
