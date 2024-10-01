package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.OwnerDoesNotOwnParkingLotException;
import org.example.Exceptions.ParkingLotAlreadyOwnedException;
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
    void TestOwnerCannotAttendParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        assertFalse(owner.canOwnerAttendParkingLot(parkingLot));
    }

    @Test
    void TestOwnerCanOwnerAttendParkingLotParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        owner.assign(parkingLot);
        assertTrue(owner.canOwnerAttendParkingLot(parkingLot));
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
}