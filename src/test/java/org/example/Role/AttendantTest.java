package org.example.Role;

import org.example.Entities.Car;
import org.example.Entities.ParkingLot;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAlreadyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;
import org.example.Strategy.SmartNextLotStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendantTest {
    private Owner owner;
    private ParkingLot parkingLot;
    private Car car;
    private Attendant attendant;
    private Attendant smartAttendant;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(2);
        car = new Car(1, CarColor.BLACK);
        attendant = new Attendant();
        owner.assign(parkingLot, attendant);
        smartAttendant = new Attendant(new SmartNextLotStrategy());
    }
    
    // ------------------------------- attendant Tests -------------------------------
    @Test
    void TestAssignParkingLotToAttendant() {
        Attendant anotherAttendant = new Attendant();
        assertDoesNotThrow(() -> owner.assign(parkingLot, anotherAttendant));
    }

    @Test
    void TestAssignTwoParkingLotToAttendant() {
        ParkingLot anotherParkingLot = owner.createParkingLot(1);
        assertDoesNotThrow(() -> owner.assign(anotherParkingLot, attendant));
    }

    @Test
    void TestCannotAssignSameParkingLotTwice() {
        assertThrows(ParkingLotAlreadyAssignedException.class, () -> owner.assign(parkingLot, attendant));
    }

    @Test
    void TestAssignSameParkingLotToMultipleAttendants() {
        Attendant anotherParkingLot = new Attendant();
        assertDoesNotThrow(() -> owner.assign(parkingLot, anotherParkingLot));
    }

    // ------------------------------- park through attendant Tests -------------------------------
    @Test
    void TestParkingCarThroughAttendant() {
        Ticket carTicket = attendant.park(car);
        assertTrue(carTicket.validateTicket(carTicket));
    }

    @Test
    void TestTryToParkCarWhenParkingLotIsFull() {
        Car secondCar = new Car(2, CarColor.RED);
        Car thirdCar = new Car(3, CarColor.BLACK);
        attendant.park(car);
        attendant.park(secondCar);

        assertThrows(ParkingLotIsFullException.class, () -> attendant.park(thirdCar));
    }

    @Test
    void TestParkSameCarTwice() {
        attendant.park(car);
        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void TestParkMultipleCarsInParkingLotThroughAttendant() {
        Car secondCar = new Car(2, CarColor.RED);

        attendant.park(car);

        assertDoesNotThrow(() -> attendant.park(secondCar));
    }

    @Test
    void TestParkCarInMultipleParkingLotsThroughSameAttendant() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(firstParkingLot, attendant);
        owner.assign(secondParkingLot, attendant);

        assertDoesNotThrow(() -> {
            attendant.park(car);
            attendant.park(secondCar);
        });
    }

    @Test
    void TestParkSameCarInDifferentParkingLotsOfSameAttendant() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);

        owner.assign(firstParkingLot, attendant);
        owner.assign(secondParkingLot, attendant);
        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void TestParkCarInDifferentParkingLotThroughDifferentAttendants() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant secondAttendant = new Attendant();
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(firstParkingLot, attendant);
        owner.assign(secondParkingLot, secondAttendant);
        Ticket firstCarTicket = attendant.park(car);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
    }

    @Test
    void TestParkCarInSameParkingLotThroughDifferentAttendants() {
        Attendant secondAttendant = new Attendant();
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(parkingLot, secondAttendant);
        Ticket firstCarTicket = attendant.park(car);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
    }

    // ------------------------------- unpark through attendant Tests -------------------------------
    @Test
    void TestUnparkCarThroughAttendant() {
        ParkingLot parkingLot = owner.createParkingLot(1);

        owner.assign(parkingLot, attendant);
        Ticket carTicket = attendant.park(car);

        assertEquals(car, attendant.unpark(carTicket));
    }

    @Test
    void TestUnparkCarFromMultipleParkingLots() {
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(secondParkingLot, attendant);
        Ticket firstCarTicket = attendant.park(car);
        Ticket secondCarTicket = attendant.park(secondCar);

        assertEquals(car, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, attendant.unpark(secondCarTicket));
    }

    @Test
    void TestUnparkAlreadyUnparkedCar() {
        Ticket carTicket = attendant.park(car);
        attendant.unpark(carTicket);

        assertThrows(InvalidTicketException.class, () -> attendant.unpark(carTicket));
    }

    @Test
    void TestUnparkMultipleCarsFromMultipleParkingLotsOfSameAttendant() {
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(secondParkingLot, attendant);
        Ticket firstCarTicket = attendant.park(car);
        Ticket secondCarTicket = attendant.park(secondCar);

        assertEquals(car, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, attendant.unpark(secondCarTicket));
    }

    @Test
    void TestUnparkMultipleCarsFromMultipleParkingLotsOfDifferentAttendant() {
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant secondAttendant = new Attendant();
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(secondParkingLot, secondAttendant);
        Ticket firstCarTicket = attendant.park(car);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertEquals(car, attendant.unpark(firstCarTicket));
        assertEquals(secondCar, secondAttendant.unpark(secondCarTicket));
    }

    @Test
    void TestUnparkCarsFromSameParkingLotThroughDifferentAttendants() {
        ParkingLot parkingLot = owner.createParkingLot(2);
        Attendant firstAttendant = new Attendant();
        Attendant secondAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        owner.assign(parkingLot, firstAttendant);
        owner.assign(parkingLot, secondAttendant);
        Ticket firstCarTicket = firstAttendant.park(firstCar);
        Ticket secondCarTicket = secondAttendant.park(secondCar);

        assertEquals(firstCar, firstAttendant.unpark(firstCarTicket));
        assertEquals(secondCar, secondAttendant.unpark(secondCarTicket));
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

    @Test
    void TestParkThroughBasicAndSmartAttendants() {
        ParkingLot parkingLot = spy(owner.createParkingLot(2));
        ParkingLot anotherParkingLot = spy(owner.createParkingLot(2));

        owner.assign(parkingLot, attendant);
        owner.assign(anotherParkingLot, smartAttendant);

        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);
        Car thirdCar = new Car(3, CarColor.BLACK);
        Car fourthCar = new Car(4, CarColor.RED);

        Ticket firstCarTicket = attendant.park(firstCar);
        verify(parkingLot, times(1)).park(firstCar);
        verify(anotherParkingLot, times(0)).park(firstCar);

        Ticket secondCarTicket = smartAttendant.park(secondCar);
        verify(parkingLot, times(0)).park(secondCar);
        verify(anotherParkingLot, times(1)).park(secondCar);

        Ticket thirdCarTicket = attendant.park(thirdCar);
        verify(parkingLot, times(1)).park(thirdCar);
        verify(anotherParkingLot, times(0)).park(thirdCar);

        Ticket fourthCarTicket = smartAttendant.park(fourthCar);
        verify(parkingLot, times(0)).park(fourthCar);
        verify(anotherParkingLot, times(1)).park(fourthCar);

        assertTrue(firstCarTicket.validateTicket(firstCarTicket));
        assertTrue(secondCarTicket.validateTicket(secondCarTicket));
        assertTrue(thirdCarTicket.validateTicket(thirdCarTicket));
        assertTrue(fourthCarTicket.validateTicket(fourthCarTicket));
    }
}