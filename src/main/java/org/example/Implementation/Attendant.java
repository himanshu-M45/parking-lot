package org.example.Implementation;

import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.ArrayList;

public class Attendant {
    // Attendant is responsible for managing parking lots
    private final ArrayList<ParkingLot> assignedParkingLots = new ArrayList<>();
    private int nextParkingLotIndex = 0;

    public void assign (ParkingLot parkingLot) {
        // assign a parking lot to the attendant
        if (assignedParkingLots.contains(parkingLot)) {
            throw new ParkingLotAleradyAssignedException("Parking lot already assigned");
        }
        assignedParkingLots.add(parkingLot);
    }

    public Ticket park(Car car) {
        int attempts = 0;
        while (attempts < assignedParkingLots.size()) {
            ParkingLot parkingLot = assignedParkingLots.get(nextParkingLotIndex);
            nextParkingLotIndex = (nextParkingLotIndex + 1) % assignedParkingLots.size();
            if (!parkingLot.isParkingLotFull()) {
                return parkingLot.park(car);
            }
            attempts++;
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
