package org.example.Exceptions;

public class ParkingLotAlreadyAssignedException extends RuntimeException {
    public ParkingLotAlreadyAssignedException(String message) {
        super(message);
    }
}
