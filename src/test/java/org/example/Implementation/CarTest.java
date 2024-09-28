package org.example.Implementation;

import org.example.Enum.CarColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    @Test
    void TestCarRegistrationNumberCannotBe0() {
        assertThrows(Exception.class, () -> {
            new Car(0, CarColor.BLACK);
        });
    }

    @Test
    void TestCarRegistrationNumberCannotBeNegative() {
        assertThrows(Exception.class, () -> {
            new Car(-32, CarColor.BLACK);
        });
    }

    @Test
    void TestCarIsCarParked() {
        assertFalse(new Car(1, CarColor.BLACK).isCarParked());
    }

    @Test
    void TestCarIsCarIdentical() {
        Car car = new Car(1, CarColor.BLACK);
        assertTrue(car.isCarIdentical(1));
    }

    @Test
    void TestCarIsIdenticalColor() {
        Car car = new Car(1, CarColor.BLACK);
        assertTrue(car.isIdenticalColor(CarColor.BLACK));
    }

    @Test
    void TestCarNotSameColor() {
        Car car = new Car(1, CarColor.BLACK);
        assertFalse(car.isIdenticalColor(CarColor.RED));
    }

    @Test
    void TestSetCarParked() {
        Car car = new Car(1, CarColor.BLACK);
        car.setCarParked(true);
        assertTrue(car.isCarParked());
    }
}