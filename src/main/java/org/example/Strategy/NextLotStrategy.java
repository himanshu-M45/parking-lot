package org.example.Strategy;

import org.example.Implementation.ParkingLot;

import java.util.ArrayList;

public abstract class NextLotStrategy {
    abstract public ParkingLot getNextLot(ArrayList<ParkingLot> parkingLots);
}
