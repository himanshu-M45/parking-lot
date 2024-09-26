package org.example.Implementation;

import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;

public class Slot {
    private final int slotNumber;
    private Car car;

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.car = null;
    }

    public boolean isOccupied() {
        return car != null;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public Car getCar() {
        return car;
    }

    public Ticket park(Car car) {
        if (this.car != null) {
            throw new CarAlreadyParkedException("Slot is already occupied");
        }
        this.car = car;
        car.isCarParked = true;
        return new Ticket(car.registrationNumber, this.slotNumber);
    }

    public Car unpark() {
        if (car == null) {
            throw new InvalidTicketException("Invalid ticket");
        }
        Car carToUnpark = this.car;
        this.car.isCarParked = false;
        this.car = null;
        return carToUnpark;
    }
}
