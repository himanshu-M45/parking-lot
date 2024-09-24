package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttendantTest {
    // ------------------------------- attendant tests -------------------------------
    @Test
    void testAssignParkingLotToAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        assertDoesNotThrow(() -> {attendant.assign(parkingLot);});
    }

    @Test
    void testAssignTwoParkingLotToAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingLot anotherParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        assertDoesNotThrow(() -> {attendant.assign(anotherParkingLot);});
    }

    @Test
    void testCannotAssignSameParkingLotTwice() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        assertThrows(ParkingLotAleradyAssignedException.class, () -> {attendant.assign(parkingLot);});
    }

    @Test
    void testGetTotalAssignedParkingLots() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingLot anotherParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        attendant.assign(parkingLot);
        attendant.assign(anotherParkingLot);

        int assignedParkingLots = 2;

        assertEquals(assignedParkingLots, attendant.getTotalParkingLot());
    }

    @Test
    void testAssigningSameParkingLotTwice() {
        ParkingLot firstparkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        attendant.assign(firstparkingLot);

        assertThrows(ParkingLotAleradyAssignedException.class, () -> {attendant.assign(firstparkingLot);});

    }

    // ------------------------------- park through attendant tests -------------------------------
    @Test
    void testParkingCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = attendant.park(parkingLot, car);

        assertEquals(car, carTicket.car);
    }

    @Test
    void testTryToParkCarWhenParkingLotIsFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.park(parkingLot, firstCar);

        assertThrows(ParkingLotIsFullException.class, () -> {attendant.park(parkingLot, secondCar);});
    }

    @Test
    void testParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        attendant.park(parkingLot, car);

        assertThrows(CarAlreadyParkedException.class, () -> {attendant.park(parkingLot, car);});
    }

    @Test
    void testParkMultipleCarsInParkingLotThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.park(parkingLot, firstCar);

        assertDoesNotThrow(() -> {attendant.park(parkingLot, secondCar);});
    }

    @Test
    void testParkCarInMultipleParkingLots() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        assertDoesNotThrow(() -> {attendant.park(firstParkingLot, firstCar);});
        assertDoesNotThrow(() -> {attendant.park(secondParkingLot, secondCar);});
    }

    @Test
    void testParkSameCarInDifferentParkingLots() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car car = new Car(1, CarColor.BLACK);

        attendant.park(firstParkingLot, car);

        assertThrows(CarAlreadyParkedException.class, () -> {attendant.park(secondParkingLot, car);});
    }

}