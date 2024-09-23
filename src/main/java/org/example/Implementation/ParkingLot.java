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

    public void park(Car car) {
        if (this.isFull) {
            throw new ParkingLotIsFullException("Parking lot is full");
        }
        int slotToPark = getNearestSlot();
        slot.set(slotToPark, car);
        if (slot.stream().allMatch(Objects::nonNull)) {
            this.isFull = true;
        }
    }

    public int getNearestSlot() {
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

    public boolean checkParkedCar(int registrationNumber) {
        for (Car car : slot) {
            if (car.registrationNumber == registrationNumber) {
                return true;
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public Car unPark(Car car) {
        if (checkParkedCar(car.registrationNumber)) {
            slot.set(slot.indexOf(car), null);
            this.isFull = false;
            return car;
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public boolean checkParkingSlot(int slotNumber) {
        if (slot.get(slotNumber) == null) {
            return true;
        }
        throw new NullPointerException("Slot is not empty");
    }
}
