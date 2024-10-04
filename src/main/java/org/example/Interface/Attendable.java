package org.example.Interface;

import org.example.Entities.Car;
import org.example.Entities.Ticket;

public interface Attendable {
    Ticket park(Car car);

    Car unpark(Ticket ticket);
}
