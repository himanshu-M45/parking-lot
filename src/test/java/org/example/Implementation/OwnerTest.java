package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.example.Strategy.SmartNextLotStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {
    private Owner owner;
    private ParkingLot parkingLot;
    private Car car;
    private Attendant attendant;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(2);
        car = new Car(1, CarColor.BLACK);
        attendant = new Attendant();
        owner.assignAttendant(parkingLot, attendant);
        owner.assign(parkingLot);
    }

    @Test
    void testOwnerInitialization() {
        assertNotNull(owner);
    }

    @Test
    void TestCreateParkingLotToOwner() {
        assertDoesNotThrow(() -> owner.createParkingLot(1));
    }

    @Test
    void TestCreateMultipleParkingLotsToOwner() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> owner.createParkingLot(1));
        assertDoesNotThrow(() -> owner.createParkingLot(2));
        assertDoesNotThrow(() -> owner.createParkingLot(3));
    }

    // ---------------------------- Attendant tests ----------------------------
    @Test
    void TestAssignAttendantToOwnedParkingLot() {
        Attendant newAttendant = new Attendant();
        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, newAttendant));
    }

    @Test
    void TestAssignAttendantToNotOwnedParkingLot() {
        Owner newOwner = new Owner();
        ParkingLot newParkingLot = newOwner.createParkingLot(1);
        assertThrows(OwnerDoesNotOwnParkingLotException.class, () -> owner.assignAttendant(newParkingLot, attendant));
    }

    @Test
    void TestAssignMultipleAttendantsToSameParkingLot() {
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, firstAttendant)),
                () -> assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, secondAttendant))
        );
    }

    @Test
    void TestAssignSameAttendantToMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assignAttendant(firstParkingLot, attendant)),
                () -> assertDoesNotThrow(() -> owner.assignAttendant(secondParkingLot, attendant))
        );
    }

    @Test
    void TestAssignSmartAttendantToOwnedParkingLot() {
        Attendant smartAttendant = new Attendant(new SmartNextLotStrategy());

        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, smartAttendant));
    }

    // ---------------------------- park tests ----------------------------
    @Test
    void TestParkCarThroughOwner() {
        Ticket ticket = owner.park(car);

        assertNotNull(ticket);
    }

    @Test
    void TestParkCarThroughOwnerWithMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        owner.assign(firstParkingLot);
        owner.assign(secondParkingLot);
        Ticket firstCarTicket = owner.park(firstCar);
        Ticket secondCarTicket = owner.park(secondCar);

        assertNotNull(firstCarTicket);
        assertNotNull(secondCarTicket);
    }

    @Test
    void TestParkAlreadyParkedCarThroughOwner() {
        owner.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> owner.park(car));
    }

    @Test
    void TestDefaultAttendantParkCarThroughOwner() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        owner.assign(firstParkingLot);
        owner.assign(secondParkingLot);
        owner.assignAttendant(firstParkingLot, attendant);
        owner.assignAttendant(secondParkingLot, attendant);
        owner.park(firstCar);
        owner.park(secondCar);

        assertAll(
                () -> assertTrue(firstParkingLot.isParkingLotFull()),
                () -> assertTrue(secondParkingLot.isParkingLotFull())
        );
    }

    @Test
    void TestParkCarBySmartAttendantThroughOwner() {
        ParkingLot firstParkingLot = owner.createParkingLot(2);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant smartAttendant = new Attendant(new SmartNextLotStrategy());
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        owner.assign(firstParkingLot);
        owner.assign(secondParkingLot);
        owner.assignAttendant(firstParkingLot, smartAttendant);
        owner.assignAttendant(secondParkingLot, smartAttendant);
        owner.park(firstCar);
        owner.park(secondCar);

        assertAll(
                () -> assertFalse(firstParkingLot.isParkingLotFull()),
                () -> assertTrue(secondParkingLot.isParkingLotFull())
        );
    }

    // ---------------------------- unpark tests ----------------------------
    @Test
    void TestUnparkCarThroughOwner() {
        Ticket ticket = owner.park(car);
        Car unparkedCar = owner.unpark(ticket);

        assertEquals(car, unparkedCar);
    }

    @Test
    void TestUnparkCarThroughOwnerWithMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        owner.assign(firstParkingLot);
        owner.assign(secondParkingLot);
        Ticket firstCarTicket = owner.park(firstCar);
        Ticket secondCarTicket = owner.park(secondCar);

        assertAll(
                () -> owner.unpark(firstCarTicket),
                () -> owner.unpark(secondCarTicket)
        );
    }

    @Test
    void TestUnparkAlreadyUnparkedCar() {
        Ticket ticket = owner.park(car);
        owner.unpark(ticket);

        assertThrows(InvalidTicketException.class, () -> owner.unpark(ticket));
    }

    // ---------------------------- notifiable tests ----------------------------
    @Test
    void TestOwnerNotifiableParkingLotIsFull() {
        Car newCar = new Car(2, CarColor.BLACK);
        parkingLot.park(car);
        parkingLot.park(newCar);

        assertTrue(owner.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestOwnerNotifiableParkingLotIsNotFull() {
        parkingLot.park(car);

        assertFalse(owner.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestOwnerNotifiableMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        firstParkingLot.park(firstCar);
        secondParkingLot.park(secondCar);

        assertAll(
                () -> assertTrue(owner.getParkingLotStatus(firstParkingLot)),
                () -> assertTrue(owner.getParkingLotStatus(secondParkingLot))
        );
    }
}