package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.*;

import java.util.ArrayList;
import java.util.Objects;

public class ParkingLot {
    private boolean isFull = false;
    private final ArrayList<Slot> slots;
    private final Policeman policeman;
    private String owner = "";

    public ParkingLot(int numberOfSlots) {
        if (numberOfSlots <= 0) {
            throw new InvalidValueException("Number of slots should be greater than 0");
        }
        this.slots = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(new Slot()); // Initialize all slots as empty
        }
        this.policeman = Policeman.getInstance();
        this.policeman.addParkingLot(this);
    }

    public Ticket park(Car car) {
        if (car.isCarParked()) throw new CarAlreadyParkedException("Car already parked");
        for (Slot slot : slots) {
            if (!slot.isOccupied()) {
                Ticket ticket = slot.park(car);
                if (slots.stream().allMatch(Slot::isOccupied)) {
                    this.isFull = true;
                    policeman.updateParkingLotStatus(this);
                }
                return ticket;
            }
        }
        throw new ParkingLotIsFullException("No empty slot found");
    }

    public int getCountOfCarsByColor(CarColor carColor) {
        int count = 0;
        for (Slot slot : slots) {
            if (slot.isOccupied() && slot.isCarColor(carColor)) {
                count++;
            }
        }
        return count;
    }

    public Ticket getCarParkedInfoByRegNo(int registrationNumber) {
        for (Slot slot : slots) {
            try {
                Ticket ticket = slot.getTicketIfCarMatches(registrationNumber);
                if (ticket != null) {
                    return ticket;
                }
            } catch (CarNotParkedException e) {
                // Continue searching in the next slot
            }
        }
        throw new CarNotParkedException("Car not available in slot");
    }

    public Car unpark(Ticket carTicket) {
        for (Slot slot : slots) {
            try {
                Car car = slot.unpark(carTicket);
                if (this.isFull) {
                    this.isFull = false;
                    policeman.updateParkingLotStatus(this);
                }
                return car;
            } catch (InvalidTicketException e) {
                // Continue searching in the next slot
            }
        }
        throw new InvalidTicketException("Invalid ticket");
    }

    public boolean isParkingLotFull() {
        return this.isFull;
    }

    public boolean isOwned() {
        return !Objects.equals(this.owner, "");
    }

    public boolean identifyOwner(String owner) {
        return Objects.equals(this.owner, owner);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
