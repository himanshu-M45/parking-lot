package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttendantTest {
    // ------------------------------- attendant Tests -------------------------------
    @Test
    void TestAssignParkingLotToAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        assertDoesNotThrow(() -> attendant.assign(parkingLot));
    }

    @Test
    void TestAssignTwoParkingLotToAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingLot anotherParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        attendant.assign(parkingLot);

        assertDoesNotThrow(() -> attendant.assign(anotherParkingLot));
    }

    @Test
    void TestCannotAssignSameParkingLotTwice() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();

        attendant.assign(parkingLot);

        assertThrows(ParkingLotAleradyAssignedException.class, () -> attendant.assign(parkingLot));
    }

    @Test
    void TestAssignSameParkingLotToMultipleAttendants() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();

        firstAttendant.assign(parkingLot);

        assertDoesNotThrow(() -> secondAttendant.assign(parkingLot));
    }

    // ------------------------------- park through attendant Tests -------------------------------
    @Test
    void TestParkingCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);

        assertTrue(carTicket.validateTicket(carTicket));
    }

    @Test
    void TestTryToParkCarWhenParkingLotIsFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(parkingLot);
        attendant.park(firstCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(secondCar));
    }

    @Test
    void TestParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void TestParkMultipleCarsInParkingLotThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(parkingLot);
        attendant.park(firstCar);

        assertDoesNotThrow(() -> attendant.park(secondCar));
    }

    @Test
    void TestParkCarInMultipleParkingLotsThroughSameAttendant() {
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
    void TestParkSameCarInDifferentParkingLotsOfSameAttendant() {
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
    void TestParkCarInDifferentParkingLotThroughDifferentAttendants() {
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
    void TestParkCarInSameParkingLotThroughDifferentAttendants() {
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

    // ------------------------------- unpark through attendant Tests -------------------------------
    @Test
    void TestUnparkCarThroughAttendant() {
        ParkingLot parkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);

        assertEquals(car, attendant.unpark(carTicket));
    }

    @Test
    void TestUnparkCarFromMultipleParkingLots() {
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
    void TestUnparkMultipleCarsFromMultipleParkingLotsOfSameAttendant() {
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
    void TestUnparkMultipleCarsFromMultipleParkingLotsOfDifferentAttendant() {
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
    void TestUnparkCarsFromSameParkingLotThroughDifferentAttendants() {
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

    // ------------------------------- distributed parking Tests -------------------------------
    @Test
    void TestDistributedParking() {
        ParkingLot firstParkingLot = new ParkingLot(2);
        ParkingLot secondParkingLot = new ParkingLot(2);
        ParkingLot thirdParkingLot = new ParkingLot(2);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE); // firstParkingLot

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        attendant.assign(thirdParkingLot);
        attendant.park(firstCar);
        attendant.park(secondCar);
        attendant.park(thirdCar);
        attendant.park(fourthCar);

        assertAll(
                () -> assertTrue(firstParkingLot.isParkingLotFull()),
                () -> assertFalse(secondParkingLot.isParkingLotFull()),
                () -> assertFalse(thirdParkingLot.isParkingLotFull())
        );
    }

    @Test
    void TestDistributedParkingWhenAllParkingLotsAreFull() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingLot thirdParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE); // firstParkingLot

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        attendant.assign(thirdParkingLot);
        attendant.park(firstCar);
        attendant.park(secondCar);
        attendant.park(thirdCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(fourthCar));
    }

    @Test
    void TestDistributedParkingWhenAllParkingLotsAreFullAndUnparkCarAndParkAgainInSameParkingLot() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingLot thirdParkingLot = new ParkingLot(1);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        attendant.assign(thirdParkingLot);
        Ticket firstCarTicket = attendant.park(firstCar);
        Ticket secondCarTicket = attendant.park(secondCar);
        Ticket thirdCarTicket = attendant.park(thirdCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(fourthCar));

        assertAll(
                () -> assertEquals(firstCar, attendant.unpark(firstCarTicket)),
                () -> assertEquals(secondCar, attendant.unpark(secondCarTicket)),
                () -> assertEquals(thirdCar, attendant.unpark(thirdCarTicket))
        );

        assertDoesNotThrow(() -> attendant.park(fourthCar));
    }
}