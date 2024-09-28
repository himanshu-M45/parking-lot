package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarNotParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {
    @Test
    void TestSlotInitialization() {
        Slot slot = new Slot();

        assertFalse(slot.isOccupied());
    }

    @Test
    void TestParkCar() {
        Slot slot = new Slot();
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = slot.park(car);

        assertTrue(carTicket.validateTicket(carTicket));
    }

    @Test
    void TestUnparkCar() {
        Slot slot = new Slot();
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = slot.park(car);
        Car unparkedCar = slot.unpark(carTicket);

        assertEquals(car, unparkedCar);
    }

    @Test
    void TestUnparkAlreadyUnparkedCar() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);
        attendant.unpark(carTicket);

        assertThrows(InvalidTicketException.class, () -> attendant.unpark(carTicket));
    }

    @Test
    void TestGetTicketIfCarMatches() {
        Slot slot = new Slot();
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);
        Ticket carTicket = slot.getTicketIfCarMatches(1);

        assertNotNull(carTicket);
    }

    @Test
    void TestCarNotFoundExceptionIfCarIsNotAvailable() {
        Slot slot = new Slot();
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);

        assertThrows(CarNotParkedException.class, () -> slot.getTicketIfCarMatches(2));
    }

    @Test
    void TestCheckBlackColorCarIsParkedInSlot() {
        Slot slot = new Slot();
        Car car = new Car(1, CarColor.BLACK);

        slot.park(car);

        assertTrue(slot.isCarColor(CarColor.BLACK));
    }

    @Test
    void TestCheckBlackColorCarIsNotParkedInSlot() {
        Slot slot = new Slot();

        assertFalse(slot.isCarColor(CarColor.BLACK));
    }
}