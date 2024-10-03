package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.example.Role.Attendant;
import org.example.Role.Owner;
import org.example.Strategy.SmartNextLotStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OwnerTest {
    private Owner owner;
    private ParkingLot parkingLot;
    private Car car;
    private Attendant attendant;
    private Attendant smartAttendant;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(2);
        car = new Car(1, CarColor.BLACK);
        attendant = new Attendant();
        smartAttendant = new Attendant(new SmartNextLotStrategy());
        owner.assign(parkingLot, owner);
        owner.assign(parkingLot, attendant);
        owner.assign(parkingLot, smartAttendant);
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
    void TestAssignToOwnedParkingLot() {
        Attendant newAttendant = new Attendant();
        assertDoesNotThrow(() -> owner.assign(parkingLot, newAttendant));
    }

    @Test
    void TestAssignToNotOwnedParkingLot() {
        Owner newOwner = new Owner();
        ParkingLot newParkingLot = newOwner.createParkingLot(1);
        assertThrows(OwnerDoesNotOwnParkingLotException.class, () -> owner.assign(newParkingLot, attendant));
    }

    @Test
    void TestAssignMultipleAttendantsToSameParkingLot() {
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assign(parkingLot, firstAttendant)),
                () -> assertDoesNotThrow(() -> owner.assign(parkingLot, secondAttendant))
        );
    }

    @Test
    void TestAssignSameAttendantToMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assign(firstParkingLot, attendant)),
                () -> assertDoesNotThrow(() -> owner.assign(secondParkingLot, attendant))
        );
    }

    @Test
    void TestAssignSmartAttendantToOwnedParkingLot() {
        Owner newOwner = new Owner();
        assertDoesNotThrow(() -> newOwner.assign(newOwner.createParkingLot(1), smartAttendant));
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

        owner.assign(firstParkingLot, owner);
        owner.assign(secondParkingLot, owner);
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
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car secondCar = new Car(2, CarColor.WHITE);
        Car thirdCar = new Car(3, CarColor.BLUE);

        owner.assign(secondParkingLot, owner);
        owner.park(car);
        owner.park(secondCar);
        owner.park(thirdCar);

        assertAll(
                () -> assertTrue(parkingLot.isParkingLotFull()),
                () -> assertTrue(secondParkingLot.isParkingLotFull())
        );
    }

    @Test
    void TestParkCarBySmartAttendantThroughOwner() {
        ParkingLot firstParkingLot = owner.createParkingLot(2);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        owner.assign(firstParkingLot, smartAttendant);
        owner.assign(secondParkingLot, smartAttendant);
        owner.park(firstCar);
        owner.park(secondCar);

        assertAll(
                () -> assertFalse(firstParkingLot.isParkingLotFull()),
                () -> assertFalse(secondParkingLot.isParkingLotFull())
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

        owner.assign(firstParkingLot, owner);
        owner.assign(secondParkingLot, owner);
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
        Owner newOwner = spy(new Owner());
        ParkingLot newParkingLot = newOwner.createParkingLot(2);
        Car newCar = new Car(2, CarColor.BLACK);

        newParkingLot.park(car);
        newParkingLot.park(newCar);
        verify(newOwner, times(2)).updateAvailableStatus(any());
    }

    @Test
    void TestOwnerNotifiableParkingLotIsNotFull() {
        Owner newOwner = spy(new Owner());
        ParkingLot newParkingLot = newOwner.createParkingLot(1);

        newParkingLot.park(car);
        verify(newOwner, times(2)).updateAvailableStatus(any());
    }
}