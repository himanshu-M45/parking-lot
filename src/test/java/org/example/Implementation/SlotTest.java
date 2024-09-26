package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarNotParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {
    @Test
    void testSlotInitialization() {
        Slot slot = new Slot(1);

        assertFalse(slot.isOccupied());
    }

    @Test
    void testParkCar() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = slot.park(car);

        assertEquals(car.registrationNumber, carTicket.registrationNumber);
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

    @Test
    void testGetTicketIfCarMatches() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);
        Ticket carTicket = slot.getTicketIfCarMatches(1);

        assertNotNull(carTicket);
    }

    @Test
    void testGetTicketIfCarMatchesIfCarIsNotAvailable() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);

        assertThrows(CarNotParkedException.class, () -> slot.getTicketIfCarMatches(2));
    }

    @Test
    void testCheckBlackColorCarIsParkedInSlot() {
        Slot slot = new Slot(1);
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);

        assertTrue(slot.isCarColor(CarColor.BLACK));
    }

    @Test
    void testCheckBlackColorCarIsNotParkedInSlot() {
        Slot slot = new Slot(1);

        assertFalse(slot.isCarColor(CarColor.BLACK));
    }
}