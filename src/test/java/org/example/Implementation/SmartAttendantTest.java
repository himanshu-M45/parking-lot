package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.ParkingLotIsFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartAttendantTest
{
    // ------------------------------- distributed parking Tests -------------------------------
    @Test
    void TestDistributedParking() {
        ParkingLot firstParkingLot = new ParkingLot(2);
        ParkingLot secondParkingLot = new ParkingLot(2);
        ParkingLot thirdParkingLot = new ParkingLot(2);
        Attendant smartAttendant = new SmartAttendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE); // firstParkingLot

        smartAttendant.assign(firstParkingLot);
        smartAttendant.assign(secondParkingLot);
        smartAttendant.assign(thirdParkingLot);
        smartAttendant.park(firstCar);
        smartAttendant.park(secondCar);
        smartAttendant.park(thirdCar);
        smartAttendant.park(fourthCar);

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
        Attendant smartAttendant = new SmartAttendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE); // firstParkingLot

        smartAttendant.assign(firstParkingLot);
        smartAttendant.assign(secondParkingLot);
        smartAttendant.assign(thirdParkingLot);
        smartAttendant.park(firstCar);
        smartAttendant.park(secondCar);
        smartAttendant.park(thirdCar);

        assertThrows(ParkingLotIsFullException.class, () -> smartAttendant.park(fourthCar));
    }

    @Test
    void TestDistributedParkingWhenAllParkingLotsAreFullAndUnparkCarAndParkAgainInSameParkingLot() {
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingLot thirdParkingLot = new ParkingLot(1);
        Attendant smartAttendant = new SmartAttendant();
        Car firstCar = new Car(1, CarColor.BLACK); // firstParkingLot
        Car secondCar = new Car(2, CarColor.RED); // secondParkingLot
        Car thirdCar = new Car(3, CarColor.WHITE); // thirdParkingLot
        Car fourthCar = new Car(4, CarColor.BLUE);

        smartAttendant.assign(firstParkingLot);
        smartAttendant.assign(secondParkingLot);
        smartAttendant.assign(thirdParkingLot);
        smartAttendant.park(firstCar);
        Ticket secondCarTicket = smartAttendant.park(secondCar);
        smartAttendant.park(thirdCar);

        assertThrows(ParkingLotIsFullException.class, () -> smartAttendant.park(fourthCar));

        smartAttendant.unpark(secondCarTicket);

        assertDoesNotThrow(() -> smartAttendant.park(fourthCar));

        assertAll(
                () -> assertTrue(firstParkingLot.isParkingLotFull()),
                () -> assertTrue(secondParkingLot.isParkingLotFull()),
                () -> assertTrue(thirdParkingLot.isParkingLotFull())
        );
    }
}