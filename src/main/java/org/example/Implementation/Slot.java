package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.CarNotParkedException;
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

    public Ticket getTicketIfCarMatches(int registrationNumber) {
        if (this.car != null && this.car.registrationNumber == registrationNumber) {
            return new Ticket(this.car.registrationNumber, this.slotNumber);
        }
        throw new CarNotParkedException("Car not available in slot");
    }

    public boolean isCarColor(CarColor carColor) {
        return this.car != null && this.car.color == carColor;
    }
}
