package org.example.Implementation;

public class Ticket {
    public final Car car;
    public final int slotNumber;
    public final int parkingLotObjId;

    public Ticket(Car car, int slotNumber, int parkingLotObjId) {
        this.car = car;
        this.slotNumber = slotNumber;
        this.parkingLotObjId = parkingLotObjId;
    }
}
