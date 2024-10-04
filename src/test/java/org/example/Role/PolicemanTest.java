package org.example.Role;

import org.example.Entities.Car;
import org.example.Entities.ParkingLot;
import org.example.Enum.CarColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicemanTest {

    @Test
    void TestPolicemanInitialization() {
        Policeman policeman = new Policeman();
        assertNotNull(policeman);
    }

    @Test
    void TestAssignPolicemanParkingLotByOwner() {
        Owner owner = spy(new Owner());
        Policeman policeman = new Policeman();
        ParkingLot parkingLot = owner.createParkingLot(1);
        owner.setPolicemanObserver(policeman, parkingLot);
        verify(owner, times(1)).setPolicemanObserver(any(), any());
    }

    @Test
    void TestPolicemanUpdatesStatusWhenParkingLotIsFull() {
        Owner owner = new Owner();
        Policeman policeman = spy(new Policeman());
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(2, CarColor.BLUE);

        verify(policeman, times(0)).updateAvailableStatus(anyString(), anyBoolean());

        owner.setPolicemanObserver(policeman, parkingLot);
        parkingLot.park(car);

        verify(policeman, times(2)).updateAvailableStatus(anyString(), anyBoolean());
    }

    @Test
    void TestAddMultipleParkingLotsToPoliceman() {
        Owner owner = spy(new Owner());
        Policeman policeman = new Policeman();
        ParkingLot firstParkingLot = owner.createParkingLot(1);
        ParkingLot secondParkingLot = owner.createParkingLot(1);
        ParkingLot thirdParkingLot = owner.createParkingLot(1);

        owner.setPolicemanObserver(policeman, firstParkingLot);
        owner.setPolicemanObserver(policeman, secondParkingLot);
        owner.setPolicemanObserver(policeman, thirdParkingLot);

        verify(owner, times(3)).updateAvailableStatus(any(), anyBoolean());
    }

    @Test
    void TestParkingLotIsNotFullThroughPoliceMan() {
        Owner owner = new Owner();
        Policeman policeman = spy(new Policeman());
        ParkingLot parkingLot = owner.createParkingLot(1);
        Car car = new Car(2, CarColor.BLUE);

        owner.setPolicemanObserver(policeman, parkingLot);
        parkingLot.park(car);

        verify(policeman, times(2)).updateAvailableStatus(anyString(), anyBoolean());
    }
}