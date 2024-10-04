package org.example.Strategy;

import org.example.Entities.ParkingLot;

import java.util.ArrayList;

public abstract class NextLotStrategy {
    abstract public ParkingLot getNextLot(ArrayList<ParkingLot> parkingLots);
}
