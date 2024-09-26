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
    void testAssigningSameParkingLotTwice() {
        ParkingLot firstparkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        attendant.assign(firstparkingLot);

        assertThrows(ParkingLotAleradyAssignedException.class, () -> attendant.assign(firstparkingLot));

    }

    @Test
    void testAssignSameParkingLotToMultipleAttendants() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();

        firstAttendant.assign(parkingLot);

        assertDoesNotThrow(() -> secondAttendant.assign(parkingLot));
    }

    // ------------------------------- park through attendant tests -------------------------------
    @Test
    void testParkingCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);

        assertTrue(carTicket.validateTicket(carTicket));
    }

    @Test
    void testTryToParkCarWhenParkingLotIsFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(parkingLot);
        attendant.park(firstCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(secondCar));
    }

    @Test
    void testParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void testParkMultipleCarsInParkingLotThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(parkingLot);
        attendant.park(firstCar);

        assertDoesNotThrow(() -> attendant.park(secondCar));
    }

    @Test
    void testParkCarInMultipleParkingLotsThroughSameAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);

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
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
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
        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
    }

    @Test
    void testParkCarInSameParkingLotThroughDifferentAttendants() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        firstAttendant.assign(parkingLot);
        secondAttendant.assign(parkingLot);
        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
    }

    // ------------------------------- unpark through attendant tests -------------------------------
    @Test
    void testUnparkCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);

        assertEquals(car, attendant.unpark(carTicket));
    }

    @Test
    void testUnparkCarFromMultipleParkingLots() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        Ticket firstCarTicket = attendant.park(firstCar);
        Ticket secondCarTicket = attendant.park(secondCar);

        assertEquals(firstCar, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, attendant.unpark(secondCarTicket));
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
    void testUnparkMultipleCarsFromMultipleParkingLotsOfSameAttendant() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
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
        Attendant secondAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        firstAttendant.assign(firstParkingLot);
        secondAttendant.assign(secondParkingLot);
        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertEquals(firstCar, firstAttendant.unpark(firstCarTicket));
        assertEquals(secondCar, secondAttendant.unpark(secondCarTicket));
    }

    @Test
    void testUnparkCarsFromSameParkingLotThroughDifferentAttendants() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        firstAttendant.assign(parkingLot);
        secondAttendant.assign(parkingLot);
        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertEquals(firstCar, firstAttendant.unpark(firstCarTicket));
        assertEquals(secondCar, secondAttendant.unpark(secondCarTicket));
    }
}