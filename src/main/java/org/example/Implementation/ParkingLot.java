package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.InvalidValueException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.ArrayList;
import java.util.Objects;

public class ParkingLot {
    public boolean isFull = false;
    private final ArrayList<Car> slot;

    public ParkingLot(int numberOfSlots) {
        if (numberOfSlots <= 0) {
            throw new InvalidValueException("Number of slots should be greater than 0");
        }
        this.slot = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slot.add(null); // Initialize all slots as empty
        }
    }

    public Ticket park(Car car) {
        if (this.isFull) {
            throw new ParkingLotIsFullException("Parking lot is full");
        }
        int slotToPark = getNearestSlot(); // get the nearest slot to park the car
        slot.set(slotToPark, car); // park the car
        if (slot.stream().allMatch(Objects::nonNull)) {
            this.isFull = true;
        }
        return new Ticket(car, slotToPark);
    }

    private int getNearestSlot() {
        for (int i = 0; i < slot.size(); i++) {
            if (slot.get(i) == null) {
                return i;
            }
        }
        throw new ParkingLotIsFullException("No empty slot found");
    }

    public int getCountOfCarsByColor(CarColor carColor) {
        int count = 0;
        for (Car car : slot) {
            if (car != null && car.color == carColor) {
                count++;
            }
        }
        return count;
    }

    public Ticket getCarParkedInfoByRegNo(int registrationNumber) {
        for (Car car : slot) {
            if (car.registrationNumber == registrationNumber) {
                return new Ticket(car, getCarParkingSlotNumber(car));
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public Car unPark(Ticket carTicket) {
        if (slot.get(carTicket.slotNumber) != null) {
            Car car = slot.get(carTicket.slotNumber);
            slot.set(carTicket.slotNumber, null);
            this.isFull = false;
            return car;
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public int getCarParkingSlotNumber(Car car) {
        for (int i = 0; i < slot.size(); i++) {
            if (slot.get(i) != null && slot.get(i).registrationNumber == car.registrationNumber) {
                return i;
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }
}
