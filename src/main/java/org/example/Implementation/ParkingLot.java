package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.InvalidValueException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.ArrayList;

public class ParkingLot {
    public boolean isFull = false;
    private final ArrayList<Slot> slot;

    public ParkingLot(int numberOfSlots) {
        if (numberOfSlots <= 0) {
            throw new InvalidValueException("Number of slots should be greater than 0");
        }
        this.slot = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slot.add(new Slot(i)); // Initialize all slots as empty
        }
    }

    public Ticket park(Car car) {
        if (car.isCarParked) {
            throw new CarAlreadyParkedException("Car already parked");
        }
        int slotToPark = getNearestSlot(); // get the nearest slot to park the car
        slot.get(getNearestSlot()).park(car); // park the car
        if (slot.stream().allMatch(Slot::isOccupied)) {
            this.isFull = true;
        }
        return new Ticket(car.registrationNumber, slotToPark);
    }

    private int getNearestSlot() {
        for (int i = 0; i < slot.size(); i++) {
            if (!slot.get(i).isOccupied()) {
                return i;
            }
        }
        throw new ParkingLotIsFullException("No empty slot found");
    }

    public int getCountOfCarsByColor(CarColor carColor) {
        int count = 0;
        for (Slot slot : slot) {
            if (slot.isOccupied() && slot.getCar().color == carColor) {
                count++;
            }
        }
        return count;
    }

    public Ticket getCarParkedInfoByRegNo(int registrationNumber) {
        for (Slot slot : slot) {
            if (slot.isOccupied() && slot.getCar().registrationNumber == registrationNumber) {
                return new Ticket(slot.getCar().registrationNumber, slot.getSlotNumber());
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public Car unpark(Ticket carTicket) {
        Slot slot = this.slot.get(carTicket.slotNumber);
        if (slot.isOccupied() && slot.getCar().registrationNumber == carTicket.registrationNumber) {
            Car car = slot.getCar();
            slot.unpark();
            if (this.isFull) this.isFull = false;
            return car;
        } else {
            throw new InvalidTicketException("Invalid ticket");
        }
    }

    public int getCarParkingSlotNumber(Car car) {
        for (Slot slot : slot) {
            if (slot.isOccupied() && slot.getCar().registrationNumber == car.registrationNumber) {
                return slot.getSlotNumber();
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }
}
