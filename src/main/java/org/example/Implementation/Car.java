package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.InvalidValueException;

public class Car {
    public int registrationNumber;
    public CarColor color;

    public Car(int registrationNumber, CarColor color) {
        if (registrationNumber <= 0) {
            throw new InvalidValueException("Registration number should be greater than 0");
        }
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public int getRegistrationNumber() {
        return 0;
    }

    public CarColor getColor() {
        return null;
    }
}
