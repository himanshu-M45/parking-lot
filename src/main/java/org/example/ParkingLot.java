package org.example;

import java.util.ArrayList;
import java.util.Objects;

public class ParkingLot {
    public boolean isFull = false;
    private final ArrayList<Car> slotArray;

    public ParkingLot(int numberOfSlots) {
        if (numberOfSlots <= 0) {
            throw new IllegalArgumentException("Number of slots should be greater than 0");
        }
        this.slotArray = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slotArray.add(null); // Initialize all slots as empty
        }
    }

    public void park(Car car) {
        if (this.isFull) {
            throw new IllegalArgumentException("Parking lot is full");
        }
        int slotToPark = getNearestSlot();
        slotArray.set(slotToPark, car);
        if (slotArray.stream().allMatch(Objects::nonNull)) {
            this.isFull = true;
        }
    }

    public int getNearestSlot() {
        for (int i = 0; i < slotArray.size(); i++) {
            if (slotArray.get(i) == null) {
                return i;
            }
        }
        throw new IllegalArgumentException("No empty slot found");
    }

    public boolean checkParkedCar(int registrationNumber) {
        for (Car car : slotArray) {
            if (car.registrationNumber == registrationNumber) {
                return true;
            }
        }
        throw new IllegalArgumentException("Car not found in parking lot");
    }

    public int getCountOfCarsByColor(CarColor carColor) {
        int count = 0;
        for (Car car : slotArray) {
            if (car != null && car.color == carColor) {
                count++;
            }
        }
        return count;
    }

    public boolean unParkCar(Car car) {
        if (checkParkedCar(car.registrationNumber)) {
            slotArray.set(slotArray.indexOf(car), null);
            this.isFull = false;
            return true;
        }
        throw new IllegalArgumentException("Car not found in parking lot");
    }
}
