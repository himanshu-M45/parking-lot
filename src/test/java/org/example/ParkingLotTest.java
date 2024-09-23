package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    // test to check creation parking lot
    @Test
    void testParkingLotWithZeroSlotsThrowsException() {
        assertThrows(Exception.class, () -> {
            new ParkingLot(0);
        });
    }

    @Test
    void testParkingLotWithNegativeSlotsThrowsException() {
        assertThrows(Exception.class, () -> {
            new ParkingLot(-32);
        });
    }

    @Test
    void testNewParkingLotIsEmpty() {
        ParkingLot parkingLot = new ParkingLot(1);
        assertFalse(parkingLot.isFull);
    }

    // test park feature
    @Test
    void testParkingLotWithOneSlotIsFullWhenCarParked() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(2, CarColor.BLUE);
        parkingLot.park(car);
        assertTrue(parkingLot.isFull);
    }

    @Test
    void testParkingLotWithTwoSlotsIsNotFullWhenOneCarParked() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car car = new Car(21, CarColor.RED);
        parkingLot.park(car);
        assertFalse(parkingLot.isFull);
    }

    @Test
    void testParkingLotWithThirdVehicleWhenAvailableSlotsAreTwo() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car firstCar = new Car(212, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(221, CarColor.BLUE);
        parkingLot.park(secondCar);
        Car thirdCar = new Car(234, CarColor.GREEN);

        assertThrows(Exception.class, () -> {
            parkingLot.park(thirdCar);
        });
    }

    // test to park at nearest spot possible
    @Test
    void testParkAtNearestSpot() {
        ParkingLot parkingLot = new ParkingLot(5);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);
        parkingLot.park(secondCar);
        Car thirdCar = new Car(345, CarColor.GREEN);
        parkingLot.park(thirdCar);
        // un-park secondCar
        parkingLot.unParkCar(secondCar);

        assertEquals(1, parkingLot.getNearestSlot());
    }

    // test to check availability of car in parking lot
    @Test
    void testCheckTheGivenCarIsAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);
        parkingLot.park(secondCar);
        Car thirdCar = new Car(345, CarColor.GREEN);
        parkingLot.park(thirdCar);

        assertTrue(parkingLot.checkParkedCar(thirdCar.registrationNumber));
    }

    @Test
    void testCheckTheGivenCarIsNotAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);

        assertThrows(Exception.class, () -> {
            parkingLot.checkParkedCar(secondCar.registrationNumber);
        });
    }

    // test to get count of all cars of given color
    @Test
    void testGetCountOfBlueColorCars() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);
        parkingLot.park(secondCar);
        Car thirdCar = new Car(345, CarColor.GREEN);
        parkingLot.park(thirdCar);
        Car fourthCar = new Car(456, CarColor.BLUE);
        parkingLot.park(fourthCar);

        assertEquals(2, parkingLot.getCountOfCarsByColor(CarColor.BLUE));
    }

    @Test
    void testGetCountOfBlackColorCars() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.BLACK);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);
        parkingLot.park(secondCar);
        Car thirdCar = new Car(345, CarColor.BLACK);
        parkingLot.park(thirdCar);
        Car fourthCar = new Car(456, CarColor.BLACK);
        parkingLot.park(fourthCar);

        assertEquals(3, parkingLot.getCountOfCarsByColor(CarColor.BLACK));
    }

    // test to check un-park a car from parking lot
    @Test
    void testUnParkCarFromParkingLotWhereCarIsAvailable() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);
        parkingLot.park(secondCar);

        assertTrue(parkingLot.unParkCar(secondCar));
    }

    @Test
    void testUnParkCarFromParkingLotWhereCarIsNotAvailable() {
        ParkingLot parkingLot = new ParkingLot(7);
        Car firstCar = new Car(123, CarColor.RED);
        parkingLot.park(firstCar);
        Car secondCar = new Car(234, CarColor.BLUE);

        assertThrows(Exception.class, () -> {
            parkingLot.unParkCar(secondCar);
        });
    }

}