package org.example.Implementation;

import org.example.Entities.Car;
import org.example.Enum.CarColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicemanTest {
    private Owner owner;
    private ParkingLot parkingLot;
    private Car car;
    private Policeman policeman;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        parkingLot = owner.createParkingLot(2);
        car = new Car(1, CarColor.BLACK);
        owner.assign(parkingLot);
        policeman = new Policeman();
        parkingLot.setNotifiable(policeman);
    }
    
    @Test
    void TestPolicemanInitialization() {
        assertNotNull(policeman);
    }

    @Test
    void TestPolicemanUpdatesStatusWhenParkingLotIsFull() {
        Car newCar = new Car(2, CarColor.BLUE);
        parkingLot.park(car);
        parkingLot.park(newCar);

        assertTrue(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestAddParkingLotToPolicemanWithInitialStatus() {
        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void Test(){
        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestAddMultipleParkingLotsToPoliceman() {
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        ParkingLot thirdParkingLot = owner.createParkingLot(1);
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
        Car newCar = new Car(2, CarColor.BLUE);
        parkingLot.park(car);
        parkingLot.park(newCar);

        assertTrue(policeman.getParkingLotStatus(parkingLot));
    }

    @Test
    void TestParkingLotIsNotFullThroughPoliceMan() {
        parkingLot.park(car);

        assertFalse(policeman.getParkingLotStatus(parkingLot));
    }
}