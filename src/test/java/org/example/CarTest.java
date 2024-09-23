package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    @Test
    void testVehicleRegistrationNumberCannotBe0() {
        assertThrows(Exception.class, () -> {
            new Car(0, CarColor.BLACK);
        });
    }

    @Test
    void testVehicleRegistrationNumberCannotBeNegative() {
        assertThrows(Exception.class, () -> {
            new Car(-32, CarColor.BLACK);
        });
    }
}