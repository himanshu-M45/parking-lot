package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.ParkingLotIsFullException;
import org.example.Role.Attendant;
import org.example.Role.Owner;
import org.example.Strategy.SmartNextLotStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmartAttendantTest {
    private Owner owner;
    private Attendant smartAttendant;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        smartAttendant = new Attendant(new SmartNextLotStrategy());
    }

    // ------------------------------- distributed parking Tests -------------------------------
    @Test
    void TestDistributedParking() {
        ParkingLot firstParkingLot = owner.createParkingLot(2);
        ParkingLot secondParkingLot = owner.createParkingLot(2);
        ParkingLot thirdParkingLot = owner.createParkingLot(2);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);
        Car thirdCar = new Car(3, CarColor.WHITE);
        Car fourthCar = new Car(4, CarColor.BLUE);

        owner.assign(firstParkingLot, smartAttendant);
        owner.assign(secondParkingLot, smartAttendant);
        owner.assign(thirdParkingLot, smartAttendant);
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
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        ParkingLot thirdParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);
        Car thirdCar = new Car(3, CarColor.WHITE);
        Car fourthCar = new Car(4, CarColor.BLUE);

        owner.assign(firstParkingLot, smartAttendant);
        owner.assign(secondParkingLot, smartAttendant);
        owner.assign(thirdParkingLot, smartAttendant);
        smartAttendant.park(firstCar);
        smartAttendant.park(secondCar);
        smartAttendant.park(thirdCar);

        assertThrows(ParkingLotIsFullException.class, () -> smartAttendant.park(fourthCar));
    }

    @Test
    void TestDistributedParkingWhenAllParkingLotsAreFullAndUnparkCarAndParkAgainInSameParkingLot() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        ParkingLot thirdParkingLot = owner.createParkingLot(1);
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);
        Car thirdCar = new Car(3, CarColor.WHITE);
        Car fourthCar = new Car(4, CarColor.BLUE);

        owner.assign(firstParkingLot, smartAttendant);
        owner.assign(secondParkingLot, smartAttendant);
        owner.assign(thirdParkingLot, smartAttendant);
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



//    @Test
//    void TestParkThroughBasicAndSmartAttendants() {
//        ParkingLot parkingLot = spy(owner.createParkingLot(2));
//        ParkingLot anotherParkingLot = spy(owner.createParkingLot(2));
//        Attendant attendant = new Attendant();
//
//        owner.assign(parkingLot, attendant);
//        owner.assign(anotherParkingLot, attendant);
//
//        Car firstCar = new Car(1, CarColor.BLACK);
//        Car secondCar = new Car(2, CarColor.RED);
//        Car thirdCar = new Car(3, CarColor.BLACK);
//        Car fourthCar = new Car(4, CarColor.RED);
//
//        Ticket firstCarTicket = attendant.park(firstCar);
//        verify(parkingLot, times(1)).park(firstCar);
//        verify(anotherParkingLot, times(0)).park(firstCar);
//
//        Ticket secondCarTicket = smartAttendant.park(secondCar);
//        verify(parkingLot, times(0)).park(secondCar);
//        verify(anotherParkingLot, times(1)).park(secondCar);
//
//        Ticket thirdCarTicket = attendant.park(thirdCar);
//        verify(parkingLot, times(1)).park(thirdCar);
//        verify(anotherParkingLot, times(0)).park(thirdCar);
//
//        Ticket fourthCarTicket = smartAttendant.park(fourthCar);
//        verify(parkingLot, times(0)).park(fourthCar);
//        verify(anotherParkingLot, times(1)).park(fourthCar);
//
//        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
//        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
//        assertTrue(thirdCarTicket.validateTicket(thirdCarTicket));
//        assertTrue(fourthCarTicket.validateTicket(fourthCarTicket));
//    }
}