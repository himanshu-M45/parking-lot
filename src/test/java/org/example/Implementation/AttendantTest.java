package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
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

        assertDoesNotThrow(() -> attendant.assign(parkingLot));
    }

    @Test
    void testAssignTwoParkingLotToAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingLot anotherParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        assertDoesNotThrow(() -> attendant.assign(anotherParkingLot));
    }

    @Test
    void testCannotAssignSameParkingLotTwice() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        assertThrows(ParkingLotAleradyAssignedException.class, () -> attendant.assign(parkingLot));
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

        assertThrows(ParkingLotAleradyAssignedException.class, () -> attendant.assign(firstparkingLot));

    }

    // ------------------------------- park through attendant tests -------------------------------
    @Test
    void testParkingCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = attendant.park(car);

        assertEquals(car, carTicket.car);
    }

    @Test
    void testTryToParkCarWhenParkingLotIsFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.park(firstCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(secondCar));
    }

    @Test
    void testParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void testParkMultipleCarsInParkingLotThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.park(firstCar);

        assertDoesNotThrow(() -> attendant.park(secondCar));
    }

    @Test
    void testParkCarInMultipleParkingLotsThroughSameAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        assertDoesNotThrow(() -> {
            attendant.park(firstCar);
            attendant.park(secondCar);
        });
    }

    @Test
    void testParkSameCarInDifferentParkingLotsOfSameAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car car = new Car(1, CarColor.BLACK);

        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void testParkCarInDifferentParkingLotThroughDifferentAttendants() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        Attendant firstAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant secondAttendant = new Attendant();
        Car secondCar = new Car(2, CarColor.RED);

        firstAttendant.assign(firstParkingLot);
        secondAttendant.assign(secondParkingLot);

        assertEquals(firstCar, firstAttendant.park(firstCar).car);
        assertEquals(secondCar, secondAttendant.park(secondCar).car);
    }

    // ------------------------------- unpark through attendant tests -------------------------------
    @Test
    void testUnparkCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = attendant.park(car);

        assertEquals(car, attendant.unpark(carTicket));
    }

    @Test
    void testUnparkCarFromMultipleParkingLots() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        Ticket firstCarTicket = attendant.park(firstCar);
        Ticket secondCarTicket = attendant.park(secondCar);

        assertEquals(firstCar, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, attendant.unpark(secondCarTicket));
    }

    @Test
    void testUnparkAlreadyUnparkedCar() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(parkingLot);
        Car car = new Car(1, CarColor.BLACK);

        Ticket carTicket = attendant.park(car);
        attendant.unpark(carTicket);

        assertThrows(InvalidTicketException.class, () -> attendant.unpark(carTicket));
    }

    @Test
    void testUnparkMultipleCarsFromMultipleParkingLotsOfSameAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        Ticket firstCarTicket = attendant.park(firstCar);
        Ticket secondCarTicket = attendant.park(secondCar);

        assertEquals(firstCar, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, attendant.unpark(secondCarTicket));
    }

    @Test
    void testUnparkMultipleCarsFromMultipleParkingLotsOfDifferentAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant firstAttendant = new Attendant();
        firstAttendant.assign(firstParkingLot);
        Attendant secondAttendant = new Attendant();
        secondAttendant.assign(secondParkingLot);

        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertEquals(firstCar, firstAttendant.unpark(firstCarTicket));
        assertEquals(secondCar, secondAttendant.unpark(secondCarTicket));
    }
}