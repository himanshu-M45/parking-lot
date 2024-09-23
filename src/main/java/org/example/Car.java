package org.example;

public class Car {
    public int registrationNumber;
    public CarColor color;

    public Car(int registrationNumber, CarColor color) {
        if (registrationNumber <= 0) {
            throw new IllegalArgumentException("Registration number should be greater than 0");
        }
        this.registrationNumber = registrationNumber;
        this.color = color;
    }
}
