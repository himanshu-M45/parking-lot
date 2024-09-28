package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.CarNotParkedException;
import org.example.Exceptions.InvalidTicketException;

public class Slot {
    private Car car;
    private Ticket ticket;

    public Slot() {
        this.car = null;
        this.ticket = null;
    }

    public boolean isOccupied() {
        return car != null;
    }

    public Ticket park(Car car) {
        if (this.car != null) {
            throw new CarAlreadyParkedException("Slot is already occupied");
        }
        this.ticket = new Ticket();
        this.car = car;
        this.car.setCarParked(true);
        return ticket;
    }

    public Car unpark(Ticket ticket) {
        if (car != null && this.ticket != null && this.ticket.validateTicket(ticket)) {
            Car car = this.car;
            this.car.setCarParked(false);
            this.car = null;
            this.ticket = null;
            return car;
        }
        throw new InvalidTicketException("Invalid ticket");
    }

    public Ticket getTicketIfCarMatches(int registrationNumber) {
        if (this.car != null && this.car.isCarIdentical(registrationNumber)) {
            return this.ticket;
        }
        throw new CarNotParkedException("Car not available in slot");
    }

    public boolean isCarColor(CarColor carColor) {
        return this.car != null && this.car.isIdenticalColor(carColor);
    }
}
