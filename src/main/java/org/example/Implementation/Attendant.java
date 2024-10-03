package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Interface.Attendable;
import org.example.Strategy.BasicNextLotStrategy;
import org.example.Strategy.NextLotStrategy;

import java.util.ArrayList;

public class Attendant implements Attendable {
    // Attendant is responsible for managing parking lots
    private final ArrayList<ParkingLot> assignedParkingLots = new ArrayList<>();
    private final NextLotStrategy nextLotStrategy;

    public Attendant() {
        this.nextLotStrategy = new BasicNextLotStrategy();
    }

    public Attendant(NextLotStrategy strategy) {
        this.nextLotStrategy = strategy;
    }

    public void assign(ParkingLot parkingLot) {
        // assign a parking lot to the attendant
        if (assignedParkingLots.contains(parkingLot)) {
            throw new ParkingLotAleradyAssignedException("Parking lot already assigned");
        }
        assignedParkingLots.add(parkingLot);
    }

    public Ticket park(Car car) {
        ParkingLot parkingLot = nextLotStrategy.getNextLot(assignedParkingLots);
        return parkingLot.park(car);
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
