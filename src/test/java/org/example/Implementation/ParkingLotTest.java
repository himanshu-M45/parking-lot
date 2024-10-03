package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {
    private Owner owner;
    private ParkingLot parkingLot;
    private Car car;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(4);
        car = new Car(1, CarColor.RED);
    }
    
    // ------------------------------- parking lot Tests -------------------------------
    @Test
    void TestCreatingParkingLotWithoutOwner() {
        assertThrows(CannotCreateParkingLotWithoutOwnerException.class, () -> new ParkingLot(1, null));
    }
    @Test
    void TestParkingLotWithZeroSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> owner.createParkingLot(0));
    }

    @Test
    void TestParkingLotWithNegativeSlotsThrowsException() {
        assertThrows(InvalidValueException.class, () -> owner.createParkingLot(-32));
    }

    @Test
    void TestNewParkingLotIsEmpty() {
        assertFalse(parkingLot.isParkingLotFull());
    }

    // ------------------------------- park car Tests -------------------------------
    @Test
    void TestParkCar() {
        Ticket ticket = parkingLot.park(car);

        assertNotNull(ticket);
    }

    @Test
    void TestParkingLotWithOneSlotIsNotFullWhenCarParked() {
        parkingLot.park(car);

        assertFalse(parkingLot.isParkingLotFull());
    }

    @Test
    void TestParkingLotWithTwoSlotsIsNotFullWhenOneCarParked() {
        parkingLot.park(car);

        assertFalse(parkingLot.isParkingLotFull());
    }

    @Test
    void TestParkingLotWithFifthVehicleWhenAvailableSlotsAreTwo() {
        Car firstCar = new Car(212, CarColor.RED);
        Car secondCar = new Car(221, CarColor.BLUE);
        Car thirdCar = new Car(234, CarColor.GREEN);
        Car fourthCar = new Car(245, CarColor.BLACK);
        Car fifthCar = new Car(256, CarColor.RED);

        parkingLot.park(firstCar);
        parkingLot.park(secondCar);
        parkingLot.park(thirdCar);
        parkingLot.park(fourthCar);

        assertThrows(ParkingLotIsFullException.class, () -> parkingLot.park(fifthCar));
    }

    // ------------------------------- count cars by color Tests -------------------------------
    @Test
    void TestGetCountOfBlueColorCars() {
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
        assertThrows(CarNotParkedException.class, () -> parkingLot.getCarParkedInfoByRegNo(123));
    }

    @Test
    void TestCannotParkSameCarTwice() {
        parkingLot.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> parkingLot.park(car));
    }

    // ------------------------------- unpark car Tests -------------------------------
    @Test
    void TestUnparkCarFromParkingLot() {
        Ticket firstCarTicket = parkingLot.park(car);
        Car unparkedCar = parkingLot.unpark(firstCarTicket);

        assertEquals(car, unparkedCar);
    }

    @Test
    void TestCannotUnparkCarFromParkingLotWithDifferentTicket() {
        Ticket anotherTicket = parkingLot.park(car);
        parkingLot.unpark(anotherTicket);

        assertThrows(InvalidTicketException.class, () -> parkingLot.unpark(anotherTicket));
    }

    @Test
    void TestCannotUnparkUnavailableCarFromParkingLot() {
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