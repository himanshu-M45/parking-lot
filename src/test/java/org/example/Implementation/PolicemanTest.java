package org.example.Implementation;

import org.example.Enum.CarColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicemanTest {
    @Test
    void TestPolicemanInitialization() {
        Policeman policeman = new Policeman();
        assertNotNull(policeman);
    }

    @Test
    void TestPolicemanUpdatesStatusWhenParkingLotIsFull() {
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setNotifiable(policeman);
        Car car = new Car(1, CarColor.BLACK);

        parkingLot.park(car);

        assertTrue(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestAddParkingLotToPolicemanWithInitialStatus() {
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setNotifiable(policeman);
        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void Test(){
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setNotifiable(policeman);
        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestAddMultipleParkingLotsToPoliceman() {
        Policeman policeman = new Policeman();
        ParkingLot firstParkingLot = new ParkingLot(1);
        ParkingLot secondParkingLot = new ParkingLot(1);
        ParkingLot thirdParkingLot = new ParkingLot(1);
        firstParkingLot.setNotifiable(policeman);
        secondParkingLot.setNotifiable(policeman);
        thirdParkingLot.setNotifiable(policeman);

        assertAll(
                () -> assertFalse(policeman.getParkingLotStatus(firstParkingLot)),
                () -> assertFalse(policeman.getParkingLotStatus(secondParkingLot)),
                () -> assertFalse(policeman.getParkingLotStatus(thirdParkingLot))
        );
    }

    @Test
    void TestParkingLotIsFullThroughPoliceMan() {
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.setNotifiable(policeman);
        Car car = new Car(1, CarColor.BLACK);

        parkingLot.park(car);

        assertTrue(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestParkingLotIsNotFullThroughPoliceMan() {
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = new ParkingLot(2);
        parkingLot.setNotifiable(policeman);
        Car car = new Car(1, CarColor.BLACK);

        parkingLot.park(car);

        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }
}