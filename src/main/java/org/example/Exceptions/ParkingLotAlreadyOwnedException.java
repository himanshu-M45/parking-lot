package org.example.Exceptions;

public class ParkingLotAlreadyOwnedException extends RuntimeException {
    public ParkingLotAlreadyOwnedException(String message) {
        super(message);
    }
}
