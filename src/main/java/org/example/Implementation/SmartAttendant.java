package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Exceptions.ParkingLotIsFullException;

public class SmartAttendant extends Attendant {
    private int nextParkingLotIndex = 0;
    @Override
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
}
