package org.example.Implementation;

import org.example.Enum.CarColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {
    @Test
    void testSlotInitialization() {
        Slot slot = new Slot(1);
        assertEquals(1, slot.getSlotNumber());
        assertFalse(slot.isOccupied());
        assertNull(slot.getCar());
    }

    @Test
    void testParkCar() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);
        slot.park(car);
        assertTrue(slot.isOccupied());
        assertEquals(car, slot.getCar());
    }

    @Test
    void testGetCar() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);
        slot.park(car);
        assertEquals(car, slot.getCar());
    }

    @Test
    void testUnparkCar() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);
        slot.park(car);
        slot.unpark();
        assertFalse(slot.isOccupied());
        assertNull(slot.getCar());
    }
}