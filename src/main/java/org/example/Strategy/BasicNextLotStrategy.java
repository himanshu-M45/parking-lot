package org.example.Strategy;

import org.example.Exceptions.ParkingLotIsFullException;
import org.example.Entities.ParkingLot;

import java.util.ArrayList;

public class BasicNextLotStrategy extends NextLotStrategy {
    @Override
    public ParkingLot getNextLot(ArrayList<ParkingLot> parkingLots) {
        ParkingLot selectedLot = null;
        for (ParkingLot parkingLot : parkingLots) {
            if (!parkingLot.isParkingLotFull()) {
                selectedLot = parkingLot;
            }
        }
        if (selectedLot == null) {
            throw new ParkingLotIsFullException("No available parking lot found");
        }
        return selectedLot;
    }
}
