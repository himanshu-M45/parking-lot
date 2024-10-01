package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    // ------------------------------- parking lot Tests -------------------------------
    @Test
    void TestParkingLotWithZeroSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> new ParkingLot(0));
    }

    @Test
    void TestParkingLotWithNegativeSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> new ParkingLot(-32));
    }

    @Test
    void TestNewParkingLotIsEmpty() {
        ParkingLot parkingLot = new ParkingLot(1);
        assertFalse(parkingLot.isParkingLotFull());
    }

    // ------------------------------- park car Tests -------------------------------
    @Test
    void TestParkCar() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(1, CarColor.RED);

        Ticket ticket = parkingLot.park(car);

        assertNotNull(ticket);
    }

    @Test
    void TestParkingLotWithOneSlotIsFullWhenCarParked() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(2, CarColor.BLUE);

        parkingLot.park(car);

        assertTrue(parkingLot.isParkingLotFull());
    }

    @Test
    void TestParkingLotWithTwoSlotsIsNotFullWhenOneCarParked() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car car = new Car(21, CarColor.RED);

        parkingLot.park(car);

        assertFalse(parkingLot.isParkingLotFull());
    }

    @Test
    void TestParkingLotWithThirdVehicleWhenAvailableSlotsAreTwo() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car firstCar = new Car(212, CarColor.RED);
        Car secondCar = new Car(221, CarColor.BLUE);
        Car thirdCar = new Car(234, CarColor.GREEN);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);

        assertThrows(ParkingLotIsFullException.class, () -> parkingLot.park(thirdCar));
    }

    // ------------------------------- count cars by color Tests -------------------------------
    @Test
    void TestGetCountOfBlueColorCars() {
        ParkingLot parkingLot = new ParkingLot(4);
        Car firstCar = new Car(123, CarColor.RED);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.GREEN);
        Car fourthCar = new Car(456, CarColor.BLUE);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);
        parkingLot.park(thirdCar);
        parkingLot.park(fourthCar);

        int expectedCount = 2;

        assertEquals(expectedCount, parkingLot.getCountOfCarsByColor(CarColor.BLUE));
    }

    @Test
    void TestGetCountOfBlackColorCars() {
        ParkingLot parkingLot = new ParkingLot(4);
        Car firstCar = new Car(123, CarColor.BLACK);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.BLACK);
        Car fourthCar = new Car(456, CarColor.BLACK);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);
        parkingLot.park(thirdCar);
        parkingLot.park(fourthCar);

        int expectedCount = 3;

        assertEquals(expectedCount, parkingLot.getCountOfCarsByColor(CarColor.BLACK));
    }

    // ------------------------------- check car availability by regNo. -------------------------------
    @Test
    void TestCheckTheGivenCarIsAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(3);
        Car firstCar = new Car(123, CarColor.RED);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.GREEN);

        parkingLot.park(firstCar);
        Ticket secondCarTicket = parkingLot.park(secondCar);
        parkingLot.park(thirdCar);
        Ticket expectedTicket = parkingLot.getCarParkedInfoByRegNo(234);

        assertEquals(secondCarTicket, expectedTicket);
    }

    @Test
    void TestCheckTheGivenCarIsNotAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(123, CarColor.RED);

        assertThrows(CarNotParkedException.class, () -> parkingLot.getCarParkedInfoByRegNo(123));
    }

    @Test
    void TestCannotParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(3);
        Car car = new Car(123, CarColor.RED);

        parkingLot.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> parkingLot.park(car));
    }

    // ------------------------------- unpark car Tests -------------------------------
    @Test
    void TestUnparkCarFromParkingLot() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(123, CarColor.RED);

        Ticket firstCarTicket = parkingLot.park(car);
        Car unparkedCar = parkingLot.unpark(firstCarTicket);

        assertEquals(car, unparkedCar);
    }

    @Test
    void TestCannotUnparkCarFromParkingLotWithDifferentTicket() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car car = new Car(234, CarColor.BLUE);

        Ticket anotherTicket = parkingLot.park(car);
        parkingLot.unpark(anotherTicket);

        assertThrows(InvalidTicketException.class, () -> parkingLot.unpark(anotherTicket));
    }

    @Test
    void TestCannotUnparkUnavailableCarFromParkingLot() {
        ParkingLot parkingLot = new ParkingLot(12);

        Ticket dummyTicket = new Ticket();

        assertThrows(InvalidTicketException.class, () -> parkingLot.unpark(dummyTicket));
    }

    // ------------------------------- mock Tests -------------------------------
    @Test
    void TestMockParkingLot() {
        ParkingLot mockParkingLot = mock(ParkingLot.class);

        when(mockParkingLot.getCountOfCarsByColor(CarColor.BLUE)).thenReturn(2);

        assertEquals(2, mockParkingLot.getCountOfCarsByColor(CarColor.BLUE));

        verify(mockParkingLot).getCountOfCarsByColor(CarColor.BLUE);
    }
}