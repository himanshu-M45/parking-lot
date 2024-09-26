package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.InvalidTicketException;
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

        Ticket carTicket = slot.park(car);

        assertEquals(car.registrationNumber, carTicket.registrationNumber);
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
        Car unparkedCar = slot.unpark();

        assertEquals(car, unparkedCar);
    }

    @Test
    void testUnparkAlreadyUnparkedCar() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);
        attendant.unpark(carTicket);

        assertThrows(InvalidTicketException.class, () -> attendant.unpark(carTicket));
    }
}