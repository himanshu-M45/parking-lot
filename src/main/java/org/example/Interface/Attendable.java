package org.example.Interface;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Implementation.ParkingLot;

public interface Attendable {
//    void assign(ParkingLot parkingLot);
    Ticket park(Car car);
    Car unpark(Ticket ticket);
}
