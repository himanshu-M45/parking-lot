package org.example.Implementation;

import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    // ------------------------------- parking lot tests -------------------------------
    @Test
    void testParkingLotWithZeroSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> new ParkingLot(0));
    }

    @Test
    void testParkingLotWithNegativeSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> new ParkingLot(-32));
    }

    @Test
    void testNewParkingLotIsEmpty() {
        ParkingLot parkingLot = new ParkingLot(1);
        assertFalse(parkingLot.isFull);
    }

    // ------------------------------- park car tests -------------------------------
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
        Car secondCar = new Car(221, CarColor.BLUE);
        Car thirdCar = new Car(234, CarColor.GREEN);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);

        assertThrows(ParkingLotIsFullException.class, () -> parkingLot.park(thirdCar));
    }

    // ------------------------------- park car at nearest spot tests -------------------------------
    @Test
    void testParkAtNearestSpot() {
        ParkingLot parkingLot = new ParkingLot(3);
        Car firstCar = new Car(123, CarColor.RED);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.GREEN);
        Car fourthCar = new Car(456, CarColor.BLACK);

        parkingLot.park(firstCar); // slot -> 0
        Ticket secondCarTicket = parkingLot.park(secondCar); // slot -> 1
        parkingLot.park(thirdCar); // slot -> 2

        parkingLot.unpark(secondCarTicket); // unpark firstCar

        parkingLot.park(fourthCar); // slot -> 0

        int expectedSlot = 1;

        assertEquals(expectedSlot, parkingLot.getCarParkingSlotNumber(fourthCar));
    }

    // ------------------------------- count cars by color tests -------------------------------
    @Test
    void testGetCountOfBlueColorCars() {
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
    void testGetCountOfBlackColorCars() {
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
    void testCheckTheGivenCarIsAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(3);
        Car firstCar = new Car(123, CarColor.RED);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.GREEN);
        parkingLot.park(firstCar);
        parkingLot.park(secondCar);
        parkingLot.park(thirdCar);

        Ticket expectedTicket = parkingLot.getCarParkedInfoByRegNo(thirdCar.registrationNumber);

        assertEquals(thirdCar, expectedTicket.car);
    }

    @Test
    void testCheckTheGivenCarIsNotAvailableInParkingLot() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(123, CarColor.RED);

        assertThrows(NullPointerException.class, () -> parkingLot.getCarParkedInfoByRegNo(car.registrationNumber));
    }

    // ------------------------------- unpark car tests -------------------------------
    @Test
    void testUnparkCarFromParkingLot() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car(123, CarColor.RED);

        Ticket firstCarTicket = parkingLot.park(car);

        Car unparkedCar = parkingLot.unpark(firstCarTicket);
        assertEquals(car, unparkedCar);
    }

    @Test
    void testCannotUnparkCarFromParkingLotWithDifferentTicket() {
        ParkingLot parkingLot = new ParkingLot(2);
        Car car = new Car(234, CarColor.BLUE);

        Ticket anotherTicket = parkingLot.park(car);
        parkingLot.unpark(anotherTicket);

        assertThrows(InvalidTicketException.class, () -> parkingLot.unpark(anotherTicket));
    }

    @Test
    void testCannotParkSameCarTwice() {
        ParkingLot parkingLot = new ParkingLot(3);
        Car car = new Car(123, CarColor.RED);
        parkingLot.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> parkingLot.park(car));
    }

    @Test
    void testCannotUnparkUnavailableCarFromParkingLot() {
        ParkingLot parkingLot = new ParkingLot(12);

        Car car = new Car(1, CarColor.YELLOW);

        Ticket dummyTicket = new Ticket(car, 5, 1);

        assertThrows(InvalidTicketException.class, () -> parkingLot.unpark(dummyTicket));
    }

    // ------------------------------- mock tests -------------------------------
    @Test
    void testMockParkingLot() {
        ParkingLot mockParkingLot = mock(ParkingLot.class);

        when(mockParkingLot.getCountOfCarsByColor(CarColor.BLUE)).thenReturn(2);

        assertEquals(2, mockParkingLot.getCountOfCarsByColor(CarColor.BLUE));

        verify(mockParkingLot).getCountOfCarsByColor(CarColor.BLUE);
    }

    @Test
    void testMockCarAndParkIt() {
        Car mockCar = mock(Car.class);

        when(mockCar.getColor()).thenReturn(CarColor.RED);

        ParkingLot parkingLot = new ParkingLot(2);

        parkingLot.park(mockCar);

        assertEquals(CarColor.RED, mockCar.getColor());

        verify(mockCar, times(1)).getColor();
    }

    // ------------------------------- reflection tests -------------------------------
    @Test
    void testReflectionOnPrivateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ParkingLot parkingLot = new ParkingLot(7);

        Car firstCar = new Car(123, CarColor.RED);
        Car secondCar = new Car(234, CarColor.BLUE);
        Car thirdCar = new Car(345, CarColor.GREEN);

        // Use reflection to access the private method
        Method getNearestSlotMethod = ParkingLot.class.getDeclaredMethod("getNearestSlot");
        getNearestSlotMethod.setAccessible(true);

        parkingLot.park(firstCar); // slot -> 0
        Ticket secondCarTicket = parkingLot.park(secondCar); // slot -> 1
        parkingLot.park(thirdCar); // slot -> 2

        parkingLot.unpark(secondCarTicket);

        int expectedSlot = 1;
        assertEquals(expectedSlot, getNearestSlotMethod.invoke(parkingLot));
    }
}