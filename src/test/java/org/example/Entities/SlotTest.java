package org.example.Entities;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarNotParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Role.Attendant;
import org.example.Role.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {
    private Car car;
    private Attendant attendant;

    @BeforeEach
    void setUp() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        car = new Car(1, CarColor.BLACK);
        attendant = new Attendant();
        owner.assign(parkingLot, attendant);
    }

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