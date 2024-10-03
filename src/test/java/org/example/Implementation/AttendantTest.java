package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
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

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(2);
        car = new Car(1, CarColor.BLACK);
        attendant = new Attendant();
        owner.assignAttendant(parkingLot, attendant);
        owner.assign(parkingLot);
    }
    
    // ------------------------------- attendant Tests -------------------------------
    @Test
    void TestAssignParkingLotToAttendant() {
        Attendant anotherAttendant = new Attendant();
        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, anotherAttendant));
    }

    @Test
    void TestAssignTwoParkingLotToAttendant() {
        ParkingLot anotherParkingLot = owner.createParkingLot(1);
        assertDoesNotThrow(() -> owner.assign(anotherParkingLot));
    }

    @Test
    void TestCannotAssignSameParkingLotTwice() {
        assertThrows(ParkingLotAleradyAssignedException.class, () -> attendant.assign(parkingLot));
    }

    @Test
    void TestAssignSameParkingLotToMultipleAttendants() {
        Attendant anotherParkingLot = new Attendant();
        assertDoesNotThrow(() -> owner.assignAttendant(parkingLot, anotherParkingLot));
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
        ParkingLot parkingLot = owner.createParkingLot(2);
        Attendant attendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        Car secondCar = new Car(2, CarColor.RED);

        attendant.assign(parkingLot);
        attendant.park(firstCar);

        assertDoesNotThrow(() -> attendant.park(secondCar));
    }

    @Test
    void TestParkCarInMultipleParkingLotsThroughSameAttendant() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
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
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(firstParkingLot);
        attendant.assign(secondParkingLot);
        attendant.park(car);

        assertThrows(CarAlreadyParkedException.class, () -> attendant.park(car));
    }

    @Test
    void TestParkCarInDifferentParkingLotThroughDifferentAttendants() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        Attendant firstAttendant = new Attendant();
        Car firstCar = new Car(1, CarColor.BLACK);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
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
        ParkingLot parkingLot = owner.createParkingLot(2);
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
        ParkingLot parkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);

        assertEquals(car, attendant.unpark(carTicket));
    }

    @Test
    void TestUnparkCarFromMultipleParkingLots() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
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
        ParkingLot parkingLot = owner.createParkingLot(1);
        Attendant attendant = new Attendant();
        Car car = new Car(1, CarColor.BLACK);

        attendant.assign(parkingLot);
        Ticket carTicket = attendant.park(car);
        attendant.unpark(carTicket);

        assertThrows(InvalidTicketException.class, () -> attendant.unpark(carTicket));
    }

    @Test
    void TestUnparkMultipleCarsFromMultipleParkingLotsOfSameAttendant() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
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
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
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
        ParkingLot parkingLot = owner.createParkingLot(2);
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

    @Test
    void TestParkThroughBasicAndSmartAttendants() {
        ParkingLot parkingLot = spy(owner.createParkingLot(2));
        ParkingLot anotherParkingLot = spy(owner.createParkingLot(2));
        Attendant smartAttendant = new Attendant(new SmartNextLotStrategy());

        attendant.assign(parkingLot);
        smartAttendant.assign(anotherParkingLot);

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