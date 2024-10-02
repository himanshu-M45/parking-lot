package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {
    @Test
    void testOwnerInitialization() {
        Owner owner = new Owner();
        assertNotNull(owner);
    }

    @Test
    void TestCreateParkingLotToOwner() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> owner.createParkingLot(1));
    }

    @Test
    void TestCreateMultipleParkingLotsToOwner() {
        Owner owner = new Owner();
        assertDoesNotThrow(() -> owner.createParkingLot(1));
        assertDoesNotThrow(() -> owner.createParkingLot(2));
        assertDoesNotThrow(() -> owner.createParkingLot(3));
    }

    @Test
    void TestOwnerCannotAlreadyOwnedParkingLot() {
        Owner firstOwner = new Owner();
        ParkingLot parkingLot = firstOwner.createParkingLot(1);
        Owner secondOwner = new Owner();
        assertThrows(ParkingLotAlreadyOwnedException.class, () -> secondOwner.addParkingLot(parkingLot));
    }

    // ---------------------------- Attendant tests ----------------------------
    @Test
    void TestAssignAttendantToOwnedParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();

        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, attendant));
    }

    @Test
    void TestAssignAttendantToNotOwnedParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        assertThrows(OwnerDoesNotOwnParkingLotException.class, () -> owner.assignAttendant(parkingLot, attendant));
    }

    @Test
    void TestAssignMultipleAttendantsToSameParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, firstAttendant)),
                () -> assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, secondAttendant))
        );
    }

    @Test
    void TestAssignSameAttendantToMultipleParkingLots() {
        Owner owner = new Owner();
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();

        assertAll(
                () -> assertDoesNotThrow(() -> owner.assignAttendant(firstParkingLot, attendant)),
                () -> assertDoesNotThrow(() -> owner.assignAttendant(secondParkingLot, attendant))
        );
    }

    @Test
    void TestAssignSmartAttendantToOwnedParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Attendant smartAttendant = new SmartAttendant();

        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, smartAttendant));
    }

    // ---------------------------- park tests ----------------------------
    @Test
    void TestParkCarThroughOwner() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        owner.assign(parkingLot);
        Ticket ticket = owner.park(car);

        assertNotNull(ticket);
    }

    @Test
    void TestParkCarThroughOwnerWithMultipleParkingLots() {
        Owner owner = new Owner();
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
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(2);
        Car car = new Car(1, CarColor.BLACK);

        owner.assign(parkingLot);
        owner.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> owner.park(car));
    }

    @Test
    void TestParkCarThroughOwnerWithOneParkingLotNotAssignedThrowsException() {
        Owner owner = new Owner();
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        ParkingLot thirdParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);
        Car thirdCar = new Car(3, CarColor.RED);

        owner.assign(firstParkingLot);
        owner.assign(secondParkingLot);
        Ticket firstCarTicket = owner.park(firstCar);
        Ticket secondCarTicket = owner.park(secondCar);

        assertAll(
                () -> assertNotNull(firstCarTicket),
                () -> assertNotNull(secondCarTicket),
                () -> assertThrows(ParkingLotIsFullException.class, () -> owner.park(thirdCar))
        );
    }

    @Test
    void TestDefaultAttendantParkCarThroughOwner() {
        Owner owner = new Owner();
        ParkingLot firstParkingLot = owner.createParkingLot(2);
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
                () -> assertFalse(secondParkingLot.isParkingLotFull())
        );
    }

//    @Test
//    void TestParkCarBySmartAttendantThroughOwner() {
//        Owner owner = new Owner();
//        ParkingLot firstParkingLot = owner.createParkingLot(2);
//        ParkingLot secondParkingLot = owner.createParkingLot(1);
//        Attendant smartAttendant = new SmartAttendant();
//        Car firstCar = new Car(1, CarColor.BLACK);
//        Car secondCar = new Car(2, CarColor.WHITE);
//
//        owner.assign(firstParkingLot);
//        owner.assign(secondParkingLot);
//        owner.assignAttendant(firstParkingLot, smartAttendant);
//        owner.assignAttendant(secondParkingLot, smartAttendant);
//        owner.park(firstCar);
//        owner.park(secondCar);
//
//        assertAll(
//                () -> assertFalse(firstParkingLot.isParkingLotFull()),
//                () -> assertTrue(secondParkingLot.isParkingLotFull())
//        );
//    }

    // ---------------------------- unpark tests ----------------------------
    @Test
    void TestUnparkCarThroughOwner() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        owner.assign(parkingLot);
        Ticket ticket = owner.park(car);
        Car unparkedCar = owner.unpark(ticket);

        assertEquals(car, unparkedCar);
    }

    @Test
    void TestUnparkCarThroughOwnerWithMultipleParkingLots() {
        Owner owner = new Owner();
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
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        owner.assign(parkingLot);
        Ticket ticket = owner.park(car);
        owner.unpark(ticket);

        assertThrows(InvalidTicketException.class, () -> owner.unpark(ticket));
    }

    // ---------------------------- notifiable tests ----------------------------
    @Test
    void TestOwnerNotifiableParkingLotIsFull() {
        Owner owner = new Owner();
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        parkingLot.setNotifiable(owner);
        parkingLot.park(car);

        assertTrue(owner.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestOwnerNotifiableParkingLotIsNotFull() {
        Owner owner = new Owner();
        ParkingLot parkingLot = new ParkingLot(2);
        Car car = new Car(1, CarColor.BLACK);

        parkingLot.setNotifiable(owner);
        parkingLot.park(car);

        assertFalse(owner.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestOwnerNotifiableMultipleParkingLots() {
        Owner owner = new Owner();
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.WHITE);

        firstParkingLot.setNotifiable(owner);
        secondParkingLot.setNotifiable(owner);
        firstParkingLot.park(firstCar);
        secondParkingLot.park(secondCar);

        assertAll(
                () -> assertTrue(owner.getParkingLotStatus(firstParkingLot)),
                () -> assertTrue(owner.getParkingLotStatus(secondParkingLot))
        );
    }
}