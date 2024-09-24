package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.InvalidValueException;
import org.example.Exceptions.ParkingLotIsFullException;

import java.util.ArrayList;
import java.util.Objects;

public class ParkingLot {
    public Integer parkingLotID;
    public boolean isFull = false;
    private final ArrayList<Car> slot;

    public ParkingLot(int numberOfSlots, int parkingLotID) {
        if (numberOfSlots <= 0 || parkingLotID <= 0) {
            throw new InvalidValueException("Number of slots should be greater than 0");
        }
        this.parkingLotID = parkingLotID;
        this.slot = new ArrayList<>(numberOfSlots);
        for (int i = 0; i < numberOfSlots; i++) {
            slot.add(null); // Initialize all slots as empty
        }
    }

    public Ticket park(Car car) {
        if (this.isFull) {
            throw new ParkingLotIsFullException("Parking lot is full");
        }
        if (slot.stream().anyMatch(car::equals) || car.isCarParked) {
            throw new CarAlreadyParkedException("Car already parked");
        }
        int slotToPark = getNearestSlot(); // get the nearest slot to park the car
        slot.set(slotToPark, car); // park the car
        if (slot.stream().allMatch(Objects::nonNull)) {
            this.isFull = true;
        }
        return new Ticket(car, slotToPark, parkingLotID);
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
                return new Ticket(car, getCarParkingSlotNumber(car), parkingLotID);
            }
        }
        throw new NullPointerException("Car not found in parking lot");
    }

    public Car unPark(Ticket carTicket) {
        if (slot.get(carTicket.slotNumber) != null && this.parkingLotID == carTicket.parkingLotID) {
            Car car = slot.get(carTicket.slotNumber);
            slot.set(carTicket.slotNumber, null);
            this.isFull = false;
            return car;
        } else {
            throw new InvalidTicketException("Invalid ticket");
        }
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
