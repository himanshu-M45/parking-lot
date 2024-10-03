package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Slot;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.example.Interface.Notifiable;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.String.valueOf;

public class ParkingLot {
    private boolean isFull = false;
    private final ArrayList<Slot> slots;
    private Notifiable notifiable;
    private final String owner;
    private final String parkingLotId;

    public ParkingLot(int numberOfSlots, String owner) {
        if (numberOfSlots <= 0) {
            throw new InvalidValueException("Number of slots should be greater than 0");
        }
        if (owner == null) {
            throw new CannotCreateParkingLotWithoutOwnerException("Owner should be provided");
        }
        this.owner = owner;
        this.parkingLotId = String.valueOf(System.identityHashCode(this));
        this.slots = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(new Slot()); // Initialize all slots as empty
        }
    }

    public Ticket park(Car car) {
        if (car.isCarParked()) throw new CarAlreadyParkedException("Car already parked");
        for (Slot slot : slots) {
            if (!slot.isOccupied()) {
                Ticket ticket = slot.park(car);
                if (slots.stream().allMatch(Slot::isOccupied)) {
                    this.isFull = true;
                    notifiable.updateAvailableStatus(parkingLotId);
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
                    notifiable.updateAvailableStatus(parkingLotId);
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

    public boolean identifyOwner(String owner) {
        return Objects.equals(this.owner, owner);
    }
    public boolean isSameParkingLot(ParkingLot parkingLot) {
        return Objects.equals(this.parkingLotId, parkingLot.parkingLotId);
    }

    public void setNotifiable(Notifiable notifiable) {
        this.notifiable = notifiable;
        this.notifiable.updateAvailableStatus(parkingLotId);
    }

    public int getAvailableSlots() {
        return (int) slots.stream().filter(slot -> !slot.isOccupied()).count();
    }
}
