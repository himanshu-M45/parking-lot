package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Entities.Ticket;
import org.example.Enum.CarColor;
import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;
import org.example.Exceptions.ParkingLotAleradyAssignedException;
import org.example.Exceptions.ParkingLotIsFullException;
import org.example.Role.Attendant;
import org.example.Role.Owner;
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
        owner.assign(parkingLot, attendant);
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
        assertThrows(ParkingLotAleradyAssignedException.class, () -> owner.assign(parkingLot, attendant));
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
}