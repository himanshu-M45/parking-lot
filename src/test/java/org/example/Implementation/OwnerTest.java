package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
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
        assertFalse(owner.canAttend(parkingLot));
    }
    @Test

    void TestOwnerCanAttendParkingLot() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        owner.setCanAttend(parkingLot);
        assertTrue(owner.canAttend(parkingLot));
    }

    @Test
    void TestParkCarThroughOwner() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        owner.setCanAttend(parkingLot);
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

        owner.setCanAttend(firstParkingLot);
        owner.setCanAttend(secondParkingLot);
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

        owner.setCanAttend(parkingLot);
        owner.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> owner.park(car));
    }

    @Test
    void TestUnparkCarThroughOwner() {
        Owner owner = new Owner();
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(1, CarColor.BLACK);

        owner.setCanAttend(parkingLot);
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

        owner.setCanAttend(firstParkingLot);
        owner.setCanAttend(secondParkingLot);
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

        owner.setCanAttend(parkingLot);
        Ticket ticket = owner.park(car);
        owner.unpark(ticket);

        assertThrows(InvalidTicketException.class, () -> owner.unpark(ticket));
    }
}