package org.example.Implementation;

public class Ticket {
    public final Car car;
    public final int slotNumber;
    public final int parkingLotID;

    public Ticket(Car car, int slotNumber, int parkingLotID) {
        this.car = car;
        this.slotNumber = slotNumber;
        this.parkingLotID = parkingLotID;
    }
}
