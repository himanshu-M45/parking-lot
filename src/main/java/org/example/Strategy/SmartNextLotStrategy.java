package org.example.Strategy;

import org.example.Exceptions.ParkingLotIsFullException;
import org.example.Entities.ParkingLot;

import java.util.ArrayList;

public class SmartNextLotStrategy extends NextLotStrategy {
    @Override
    public ParkingLot getNextLot(ArrayList<ParkingLot> parkingLots) {
        ParkingLot bestParkingLot = null;
        int maxAvailableSlots = -1;

        for (ParkingLot parkingLot : parkingLots) {
            int availableSlots = parkingLot.getAvailableSlots();
            if (availableSlots > maxAvailableSlots) {
                maxAvailableSlots = availableSlots;
                bestParkingLot = parkingLot;
            }
        }

        if (bestParkingLot != null && !bestParkingLot.isParkingLotFull()) {
            return bestParkingLot;
        }

        throw new ParkingLotIsFullException("No available parking lot found");
    }
}
