package org.example.Entities;

import org.example.Enum.CarColor;
import org.example.Exceptions.InvalidValueException;

public class Car {
    private final int registrationNumber;
    private final CarColor color;
    private boolean isParked = false;

    public Car(int registrationNumber, CarColor color) {
        if (registrationNumber <= 0) {
            throw new InvalidValueException("Registration number should be greater than 0");
        }
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public boolean isCarIdentical(int registrationNumber) {
        return this.registrationNumber == registrationNumber;
    }

    public boolean isIdenticalColor (CarColor color) {
        return this.color == color;
    }

    public boolean isCarParked() {
        return isParked;
    }

    public void setCarParked(boolean isParked) {
        this.isParked = isParked;
    }
}
