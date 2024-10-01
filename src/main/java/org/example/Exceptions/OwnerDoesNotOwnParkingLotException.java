package org.example.Exceptions;

public class OwnerDoesNotOwnParkingLotException extends RuntimeException {
    public OwnerDoesNotOwnParkingLotException(String message) {
        super(message);
    }
}
